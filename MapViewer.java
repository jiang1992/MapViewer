
import javax.swing.*;
import javax.swing.event.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.awt.*;

import java.awt.event.*;

import java.util.*;
import java.util.List;
import java.io.*;






public class MapViewer extends JFrame implements ActionListener{
  // The preferred size of the editor
  private final int PREFERRED_WIDTH = 680;
  private final int PREFERRED_HEIGHT = 600;
  JPanel testPanel = new JPanel();

  private ZoomPane _zoomPane;
  private MapScene _map;


  private JMenuItem open = new JMenuItem("open");

  private JMenuItem test = new JMenuItem("MST");
  private JMenuItem shortest = new JMenuItem("shortest Path");
  final Point p = new Point(50,50);
  String[] locationOption;
  private JList locationList;
  private JList locationList2;
  int idFrom;
  int idTo;
  String bitMapFile;
  Image image;
  double scale;
 
  Map<Integer,Double> path = new HashMap<Integer,Double>();//?????? 
  Map<Integer,String> pathInfo = new HashMap<Integer,String>();//?????? 
   
  Set<Location> unfound = new HashSet<Location>(); 
  Set<Location> found = new HashSet<Location>();

 
//Below Added by James/////////////////////////////////////////////////////////////////////
 
  LinkedList<Location> locationLink = new LinkedList<Location>();
  LinkedList<Path> pathLink = new LinkedList<Path>();
  String inputFile; // input File Name
  ///////////////////////Change1
  int MAX = 10000;
 
  int[] parent = new int[MAX];
  ////////////////////////////////
  //Types used for MST
  Set<Integer> points = new HashSet<Integer>(); 
  List<Path> treeEdges = new ArrayList<Path>();
 
  //////
  public void computePath(Location start)
  { 
      Location nearest = getShortestPath(start);//???start????????,??found 
     
      if(nearest == null)
      { 
          return; 
      } 
     
      Map<Location,Double> childs = nearest.getChild();
     
      found.add(nearest); 
      unfound.remove(nearest); 
       
      for(Location child:childs.keySet())
      { 
          if(unfound.contains(child))
          {
              //??????unfound? 
              Double newCompute = path.get(nearest.getID()) + childs.get(child); 
             
              if(path.get(child.getID()) > newCompute)
              {  
                  //????????????????? 
                  path.put(child.getID(), newCompute); 
                  pathInfo.put(child.getID(), pathInfo.get(nearest.getID())+ "->" + child.getID()); 
              } 
          } 
      } 
      computePath(start);//??????,?????????? 
      computePath(nearest);//???????,????????? 
  }
 
 
  //////////////////Get nearest location By James
  Location getShortestPath(Location location)
  { 
      Map<Location,Double> childs=location.getChild();
      double minDistance = Double.MAX_VALUE; 
      Location result = null; 
     
      for(Location child:childs.keySet())
      { 
          if(unfound.contains(child))
          { 
              double distance = childs.get(child); 
              if(distance < minDistance)
              { 
                  minDistance = distance; 
                  result = child; 
              } 
          } 
      } 
      return result; 
  } 
 
///////////////Print Shortest Path information by James
  public void printPathInfo()
  { 
      Set<Map.Entry<Integer, String>> pathInfos = pathInfo.entrySet(); 
      for(Map.Entry<Integer, String> pathInfo:pathInfos)
      { 
   //       System.out.println(path + ":" + pathInfo.getValue()); 
      } 
  } 

 

///////////////////????  by James    //////change2
public void union(int j, int k){ 
    for(int i = 0; i < MAX; i++){ 
        if(parent[i] == j){ 
            parent[i] = k; 

//System.out.println("the parent of " + i + " is " + k);
        } 
    } 
} 
  //////////////////////////Above added by James////////////////////////////////////////////
 


  public static void main(String[] args) {
    MapViewer mapEditor = new MapViewer();
    mapEditor.setVisible(true);
  }

  /////////////////////////////////below james//////////////////////////////////////////////////////////
  ///// MST building method
 
  //////////////////////////////change3
  public void buildTree(Path[] pathList, Location[] locationList)
  {       
      TreeSet<Integer> idInTree = new TreeSet<Integer>();   
      TreeSet<Path> edges = new TreeSet<Path>(); 
    
        
      for(int i = 0; i < pathList.length; i++)
      {         
          int start = pathList[i].start;
          int end = pathList[i].end;
          double distance = pathList[i].distance;
         
          while (!edges.add(new Path(start, end, distance)))
          {
              distance = distance + 0.00001;
          }         
          idInTree.add(pathList[i].start);
          idInTree.add(pathList[i].end);           
      }
     
      int pointNum = idInTree.size();
     


  //    System.out.println("point Number: " + pointNum);
     
     
      //????n-2?? 
      int i = 0; 
          
     
      for(Path edge:edges)
      {        
         
          if(i >= pointNum-1)
          {
              break;
          }
         
         
          int jj = parent[edge.start]; 
          int kk = parent[edge.end]; 
         
          //??? 
          if(jj != kk)
          { 
              i++; 
              treeEdges.add(edge); 
              
              union(jj,kk); 
          } 
        
      } 
         
         
         
          /*
          if(isCircle(edge))
          { 
              System.out.println("is Circle" + edge.toString());
              continue; 
          }
          else
          {  
              if(treeEdges.size() < pointNum)
              {
                treeEdges.add(edge); 
                System.out.println("not Circle" + edge.toString());
              }
              else
              {
                  return;
              }
          }
          */ 
          
      
     
      if(i != pointNum - 1)
      { 
          System.out.println("no spanning tree"); 
      } 

  }


 
  //MST printer
  public void printTreeInfo()
  { 
      double totalDistance = 0; 
      for(Path edge:treeEdges)
      { 
          totalDistance = totalDistance + edge.getDistance(); 
 //         System.out.println(edge.toString()); 
      } 
      System.out.println("Total distance: " + totalDistance); 
     
     
//      System.out.println("Inside print function, Tree Edges Size: " + treeEdges.size());

     
     
  }
 
 
  public void readXML(String s) throws Exception {
     
     
      locationLink.clear();
      pathLink.clear();
     
     
      //start reading XML
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder;
      builder = factory.newDocumentBuilder();
   
      //Here we do the actual parsing
      Document doc = builder.parse(new File(s));
      Element rootElement = doc.getDocumentElement();
  
      // System.out.println("File not existed!");

      //Here we get the root element of XML and print out
      //the value of its 'testAttr' attribute
     
     
      bitMapFile = rootElement.getAttribute("bitmap");
      scale = Double.parseDouble(rootElement.getAttribute("scale-feet-per-pixel"));
     
      System.out.println(bitMapFile + " " + scale);
 
      //Here we get a list of all elements named 'child'
      NodeList location = rootElement.getElementsByTagName("location");
      NodeList path = rootElement.getElementsByTagName("path");
     
      Location[] locationList = new Location[location.getLength()];
      Path[] pathList = new Path[path.getLength()];
     
     
      for(int i = 0; i < location.getLength(); i++)
      {
          locationList[i] = new Location();
      }
     
      for(int i = 0; i < path.getLength(); i++)
      {
          pathList[i] = new Path();
      }
     
     
      for (int i = 0; i < location.getLength(); i++)
      {
          Node childNode = location.item(i);
          Element noteElement = (Element) childNode;
    
          locationList[i].setId(Integer.parseInt(noteElement.getAttribute("id")));
          locationList[i].name = noteElement.getAttribute("name");
          locationList[i].setX(Integer.parseInt(noteElement.getAttribute("x")));
          locationList[i].setY(Integer.parseInt(noteElement.getAttribute("y")));
         
 //         System.out.println(locationList[i].id +" "+ locationList[i].name + " " + locationList[i].x + " "+ locationList[i].y);
      }
     
     
     
     
      for (int i = 0; i < path.getLength(); i++)
      {
          //System.out.println("i = " + i);
          Node childNode = path.item(i);
          Element noteElement = (Element) childNode;

          int startID = Integer.parseInt(noteElement.getAttribute("idfrom"));
          int endID = Integer.parseInt(noteElement.getAttribute("idto"));  
         
          int inStart = 0;
          int inEnd = 0;
         
          for (int j = 0; j < locationList.length; j++)
          {
              if (locationList[j].getID() == startID)
              { inStart = j;}
              if (locationList[j].getID() == endID)
              { inEnd = j;}         
          }
        
          pathList[i] = new Path(locationList[inStart].getPoint(), locationList[inEnd].getPoint());
          pathList[i].pathSetStartEnd(startID, endID);
         
 //         System.out.println("start: " + pathList[i].start + " end: " + pathList[i].end + " distance: " + pathList[i].distance );
      } 
     
      for(int i=0; i < locationList.length; i++)
      {
          locationLink.add(locationList[i]);
      }
     
      for(int i = 0; i < pathList.length; i++)
      {
          pathLink.add(pathList[i]);
      }
      _map.locations.clear();
      _map.paths.clear();
     
     
      //System.out.println("LocationLink SIZE" + locationLink.size());
     
      for(int i=0; i<locationLink.size();i++)
      {
          int j = locationList[i].id ;
          _map.locations.add(locationList[i]);
//          System.out.println("id:" + locationList[i].id + "coordinates: (" + locationList[i].x + " , " + locationList[i].y + ")");
      }     
     
      for(int i=0; i < pathLink.size();i++)
      {
          _map.paths.add(pathList[i]);
          //System.out.println("idfrom:" + locationList[i].start + "coordinates: (" + locationList[i].x + " , " + locationList[i].y + ")");
      }

     

  }
////////////////////////Above by james//////////////////////////////////////////////////////////
 
 
  public MapViewer() {
    setTitle("Map Editor");
    setSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
    setBackground(Color.gray);

    // Close when closed. For reals.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    image = new ImageIcon("purdue-map.jpg").getImage();

    _map = new MapScene(image);
    _zoomPane = new ZoomPane(_map);

    getContentPane().add(_zoomPane);
    getContentPane().add(_zoomPane.getJSlider(), "Last");

    MouseAdapter listener = new MouseAdapter() {
      public void mousePressed(MouseEvent e) {
        Point point = _zoomPane.toViewCoordinates(e.getPoint());
        _map.mousePressed(point);
      }
    };

    MouseMotionAdapter motionListener = new MouseMotionAdapter() {
      public void mouseDragged(MouseEvent e) {
        Point point = _zoomPane.toViewCoordinates(e.getPoint());
        _map.mouseDragged(point);
      }
    };
   
    MouseAdapter clickListener = new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          Point point = _zoomPane.toViewCoordinates(e.getPoint());
          _map.mouseClicked(point);
         
        }
    };
   
    MouseAdapter releaseListener = new MouseAdapter() {
        public void mouseReleased(MouseEvent e) {
          Point point = _zoomPane.toViewCoordinates(e.getPoint());
          _map.mouseReleased(point);
         
        }
    };
   _zoomPane.getZoomPanel().addMouseListener(releaseListener);
    _zoomPane.getZoomPanel().addMouseListener(listener);
    _zoomPane.getZoomPanel().addMouseListener(clickListener);
    _zoomPane.getZoomPanel().addMouseMotionListener(motionListener);
  
   
   
    JMenuBar menuBar = new JMenuBar();
   
   
    JMenu file = new JMenu("File");
   
    menuBar.add(file);
    file.add(open);           open.addActionListener(this);
    file.add(test);            test.addActionListener(this);
    file.add(shortest);        shortest.addActionListener(this);
   
    openFile();
    removeDuplicateLocation();
    image = new ImageIcon(bitMapFile).getImage();
    _map.setMapScene(image);
    this.setSize(50, 50);
    this.setSize(1000,1000);
    System.out.println(bitMapFile);
    String[] locationOption =new String[_map.locations.size()];
    for(int i=0; i<locationOption.length;i++){
        locationOption[i] = Integer.toString(_map.locations.get(i).id)+"."+ _map.locations.get(i).name;
    }
   
    locationList  = new JList(locationOption);
    locationList.setVisibleRowCount(10);
    locationList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    locationList.setSize(50, 50);
   
   
   
    JPanel testPanel = new JPanel();
   
   
   
    String[] locationOption2 = new String[_map.locations.size()];
    for(int i=0; i<locationOption2.length;i++){
        locationOption2[i] = Integer.toString(_map.locations.get(i).id) +"."+  _map.locations.get(i).name;
    }
   
    locationList2  = new JList(locationOption2);
    locationList2.setVisibleRowCount(10);
    locationList2.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

   
 
    locationList2.addListSelectionListener(
            new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent event){
                    _map.locationPicked.remove((_map.locations.get(idTo)));
                    String temp = (String) locationList2.getSelectedValue();
                    String temp2 = temp.substring(0, temp.lastIndexOf("."));
                    idTo = Integer.parseInt(temp2);
                       _map.locationPicked.add(_map.locations.get(idTo));
                       _map.mouseClicked(p);
                }
            });
   
   
    locationList.addListSelectionListener(
            new ListSelectionListener(){
                public void valueChanged(ListSelectionEvent event){
                    _map.locationPicked.remove((_map.locations.get(idFrom)));
                    String temp = (String) locationList.getSelectedValue();
                    String temp2 = temp.substring(0, temp.lastIndexOf("."));
                    idFrom = Integer.parseInt(temp2);
                    _map.locationPicked.add(_map.locations.get(idFrom));
                    _map.mouseClicked(p);
                }
            });
   
 
    testPanel.setLayout(new FlowLayout());
    testPanel.add(new JScrollPane(locationList2));
    testPanel.add(new JScrollPane(locationList));
   
   
    this.setJMenuBar(menuBar);
    this.add(testPanel,BorderLayout.LINE_START);
  }
  private void openFile(){
     
      String[] possibilities = null;
     
     
     
      inputFile = (String)JOptionPane.showInputDialog(
                          this,
                         
                          "what is your file sire",
                          "",
                          JOptionPane.PLAIN_MESSAGE,
                          null,
                          possibilities,
                          "any_name");
     
     
        File f = new File(inputFile + ".xml");
        while(!f.exists())
        {/*
           
            inputFile = (String)JOptionPane.showInputDialog(
                    this,
                   
                    "what is your file sire",
                    "",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    possibilities,
                    "text1");
            f = new File(inputFile + ".xml");*/
            System.out.println("no file");
            return;
        }
     
      String s = inputFile + ".xml";
     
      System.out.println(s);
     
      _map.locations.clear();
      _map.paths.clear();
     
      try {
          //idMinus();
        readXML(s);
    } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

   
  }
 
  public void removeDuplicateLocation(){
      for(int i=0;i<_map.locations.size();i++){
          if(i!=_map.locations.size()-1){
              if(_map.locations.get(i).getCenter() == _map.locations.get(i+1).getCenter()){
                  _map.locations.remove(i+1);
             
              }
          }
      }
  }

@Override
    public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub
        if(e.getSource() == shortest){

             
              _map.MST.clear();
             
              path.clear();
              pathInfo.clear();
              found.clear();
              unfound.clear();

             
              //MapEditor mst = new MapEditor(); 
             
              Path[] DijPathList = new Path[_map.paths.size()];
              Location[] DijLocationList = new Location[_map.locations.size()];
             
              for(int i = 0; i < _map.paths.size(); i++)
              {
                  DijPathList[i] = new Path(_map.paths.get(i)._start, _map.paths.get(i)._end);
                  DijPathList[i].pathSetStartEnd(_map.paths.get(i).getStart(), _map.paths.get(i).getEnd());
                  DijPathList[i].id = _map.paths.get(i).id;
                  //System.out.println("+++++++++_-------" + MSTpathList[i].toString());
              }
             
              for(int i = 0; i < _map.locations.size(); i++)
              {
                  DijLocationList[i] = new Location(_map.locations.get(i).getPoint());
                  DijLocationList[i].setNameID(_map.locations.get(i).name, _map.locations.get(i).id);
              }
             
              // Start initialization
              boolean[] connected = new boolean[MAX];
             
              for (int i = 0; i < MAX; i++)
              {
                  connected[i] = false;
              }
                 
              for(int j = 0; j < DijPathList.length; j++)
              {
                     if (DijPathList[j].start == idFrom)
                     {
                         path.put(DijPathList[j].end, DijPathList[j].distance);
                        pathInfo.put(DijPathList[j].end, idFrom + "->" + DijPathList[j].end);           
                        connected[DijPathList[j].end] = true;
                     }
                     else if (DijPathList[j].end == idFrom)
                     {
                         path.put(DijPathList[j].start, DijPathList[j].distance);
                        pathInfo.put(DijPathList[j].start, idFrom + "->" + DijPathList[j].start);           
                        connected[DijPathList[j].start] = true;
                     }
              }
             
               
              for (int i = 0; i < DijLocationList.length; i++)
              {
                  for (int j = 0; j < MAX; j++)
                  {
                      if ((connected[j] == false) && (DijLocationList[i].id == j))
                      {
                          path.put(i, Double.MAX_VALUE); 
                            pathInfo.put(i, idFrom + "");
                      }
                  }
              }
             
              //End initialization
             
              //Start map building  
              for(int i = 0; i < DijLocationList.length; i++)
              {
                  for (int j = 0; j < DijPathList.length; j++)
                  {
                      //System.out.println("PathList Size: " + DijPathList.length + "    j  = " + j);
                      if (DijLocationList[i].id == DijPathList[j].getStart())
                      {
                          for (int h = 0; h < DijLocationList.length; h++)
                          {
                              if (DijLocationList[h].id == DijPathList[j].getEnd())
                              {
                                  DijLocationList[i].getChild().put(DijLocationList[h], DijPathList[j].distance);
                              }
                          }
                      }     
                      if (DijLocationList[i].id == DijPathList[j].getEnd())
                      {
                          for (int h = 0; h < DijLocationList.length; h++)
                          {
                              if (DijLocationList[h].id == DijPathList[j].getStart())
                              {
                                  DijLocationList[i].getChild().put(DijLocationList[h], DijPathList[j].distance);
                              }
                          }
                      }   
                  }
                }
               
             
               Location start = new Location();
              
                for(int i = 0; i < DijLocationList.length; i++)
                {
                    if (DijLocationList[i].getID() == idFrom)
                    {
                        found.add(DijLocationList[i]);
                        start = DijLocationList[i];
                    }
                    else
                    {
                        unfound.add(DijLocationList[i]);
                    }
                }

               
               
                //End of map Building
                computePath(start);
            
                printPathInfo();
               
                String shortPath = "";
                Double pathDistance = 0.0;
               
                Set<Map.Entry<Integer, String>> pathInfos = pathInfo.entrySet(); 
                for(Map.Entry<Integer, String> pathInfo:pathInfos)
                { 
                   
                    if (pathInfo.getKey() == idTo)
                    {
                      //System.out.println(pathInfo.getKey() + ":" + pathInfo.getValue());
                       shortPath = pathInfo.getValue();
                       //pathDistance = (double)pathInfo.getKey();
                      
                    }/*
                    if (path. == idTo)
                    {
                      //System.out.println(pathInfo.getKey() + ":" + pathInfo.getValue());
                       shortPath = pathInfo.getValue();
                       pathDistance = (double)pathInfo.getKey();
                    }*/
                   
                   
                }
               
                Set<Map.Entry<Integer, Double>> paths = path.entrySet();
                for(Map.Entry<Integer, Double> pathGet:paths)
                { 
                   
                    if (pathGet.getKey() == idTo)
                    {
                      //System.out.println(pathInfo.getKey() + ":" + pathInfo.getValue());
                       pathDistance = pathGet.getValue();
                       //pathDistance = (double)pathInfo.getKey();
                      
                    }/*
                    if (path. == idTo)
                    {
                      //System.out.println(pathInfo.getKey() + ":" + pathInfo.getValue());
                       shortPath = pathInfo.getValue();
                       pathDistance = (double)pathInfo.getKey();
                    }*/
                   
                   
                }
               
               
               
               
               
                pathDistance = scale * pathDistance;
                System.out.println(pathDistance);
                ///////// distance needs to change
                JOptionPane.showMessageDialog(this,
                          "Total distance: " + pathDistance,
                          "Inane warning",
                          JOptionPane.WARNING_MESSAGE);
               
               String delims = "->";
                 String[] pathPoint = shortPath.split(delims);
                
                int[] pathID = new int[pathPoint.length];
                for (int i = 0; i < pathID.length ; i++)
                {
                    pathID[i] = Integer.parseInt(pathPoint[i]);
                }
               
               
               
               for (int j = 0; j < _map.paths.size(); j++)
             {
                  for (int i = 0; i < pathID.length - 1; i++)
                  {
                     System.out.println("  " + i);
                     if(((_map.paths.get(j).start == pathID[i]) && (_map.paths.get(j).end == pathID[i + 1]))
                        || ((_map.paths.get(j).end == pathID[i]) && (_map.paths.get(j).start == pathID[i + 1])))
                     {
                       _map.MST.add(_map.paths.get(j));
                      break;
                     }         
                  }
                }
              _map.mouseClicked(p);
        }
               
       
       
       
       
        else if(e.getSource() == test){
     
             
              for(int i = 0; i < MAX; i++)
              { 
                  parent[i] = i; 
              }
             
              _map.MST.clear();
             
             
              //MapEditor mst = new MapEditor(); 
             
             
              Path[] MSTpathList = new Path[_map.paths.size()];
              Location[] MSTlocationList = new Location[_map.locations.size()];
             
              for(int i = 0; i < _map.paths.size(); i++)
              {
                  MSTpathList[i] = new Path(_map.paths.get(i)._start, _map.paths.get(i)._end);
                  MSTpathList[i].pathSetStartEnd(_map.paths.get(i).getStart(), _map.paths.get(i).getEnd());
                  MSTpathList[i].id = _map.paths.get(i).id;
            //      System.out.println("+++++++++_-------" + MSTpathList[i].toString());
              }
             
              for(int i = 0; i < _map.locations.size(); i++)
              {
                  MSTlocationList[i] = new Location(_map.locations.get(i).getPoint());
                  MSTlocationList[i].setNameID(_map.locations.get(i).name, _map.locations.get(i).id);
              }
             
              treeEdges.clear();
             
             

             
             
              buildTree(MSTpathList, MSTlocationList);
             
              //System.out.println("before print Tree Edges Size: " + treeEdges.size());
             
             
             
             
              //printTreeInfo();
             
              double totalDistance = 0; 
              for(Path edge:treeEdges)
              { 
                  totalDistance = totalDistance + edge.getDistance(); 
    //              System.out.println(edge.toString()); 
              } 
              System.out.println("Total distance: " + totalDistance); 
              JOptionPane.showMessageDialog(this,
                      "Total distance: " + totalDistance*scale,
                      "Inane warning",
                      JOptionPane.WARNING_MESSAGE);

             
             
              for(int j = 0; j < treeEdges.size(); j++)
              {
                  //System.out.println("++++++++++++++  treeEdges ID" + treeEdges.get(j).id);
                  for (int i = 0; i < _map.paths.size(); i++)
                  {
                    if ((_map.paths.get(i).start == treeEdges.get(j).start) && (_map.paths.get(i).end == treeEdges.get(j).end))
                    {
                       
                        //double dist = treeEdges.get(j).getDistance();
                        _map.MST.add(_map.paths.get(i));
                       // System.out.println("Path ID added " + _map.paths.get(i).id);
                    }
                  }
              }
             
          _map.mouseClicked(p);
          }

       
    }
   
   




}