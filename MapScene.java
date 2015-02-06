
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.*;

/**
 * This class is where you keep track of all your locations and edges
 * and you draw them in the draw() method.
 */
public class MapScene implements Scene {
  private ChangeListener _listener;
  private Image _image;

  private Point _lineStart;
  private Point _lineEnd;
  private Point _clicked;
  private int _released;
  private int watcher = 0;
  int temp = -1;
  int temp2 = 0;
//  private int count = 0;
  JFrame frame = new JFrame();
  private boolean isClicked = false;
  
  Point a = new Point(-1,-1);
  private Path empty = new Path(a,a);
  
  int operation = 0;
  LinkedList<Location> locations = new LinkedList<Location>();
  LinkedList<Path> paths = new LinkedList<Path>();
  LinkedList<Point> removes = new LinkedList<Point>();
  LinkedList<Path> MST   = new LinkedList<Path>();
  LinkedList<Location> locationPicked = new LinkedList<Location>();
  public MapScene(Image image) {
    _image = image;
  }

  public void setMapScene(Image image){
	  _image = image;
  }
  /**
   * Call this method whenever something in the map has changed that
   * requires the map to be redrawn.
   */
  private void changeNotify() {
    if (_listener != null) _listener.stateChanged(null);
  }


  /**
   * This method will draw the entire map.
   */
  public void draw(Graphics2D g) {
    // Draw the map image
	  
    g.drawImage(_image, 0, 0, null);

 //   removeDuplicate();
    pathUpdate();
    locationUpdate();
    removeDuplicate();
  
   
 //   letGoPath();
    if(locations!=null){
    	for(int i=0; i<locations.size();i++){
        	((Location) locations.get(i)).draw(g);
        }
    }
   
    if(paths!=null){
    	int s = paths.size();
    	for(int i=0; i<s;i++){
    	//	if(temp!=-1) pathUpdate3();
        	((Path) paths.get(i)).draw(g);
        }
    }
    if(MST!=null){
    	for(int i=0; i<MST.size(); i++){
    		
    		((Path) MST.get(i)).draw2(g);
    	}
    }
    
    
    if(locationPicked!=null){
    	for(int i=0; i<locationPicked.size(); i++){
    		(locationPicked.get(i)).drawSelect(g);
    	}
    }
    
    // Draw the line
    g.setColor(Color.BLUE);
    g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    if (_lineStart != null && _lineEnd != null) {
      g.drawLine(_lineStart.x, _lineStart.y, _lineEnd.x, _lineEnd.y);
    }
    if(_lineEnd == null){}

    
  }
  public void locationUpdate(){
	  for(int i=0;i<locations.size();i++){
		  locations.get(i).id = i;
	  }
	  
	  
  };
  public void pathUpdate(){
	 
	  
	  for (int i=0;i<paths.size();i++){
		  paths.remove(empty);
	  }
	  for(int i=0;i<paths.size();i++){
		  paths.get(i).id = i + 1;
	  }
	  
	  for(int i=0; i<paths.size();i++){
		  for(int j = 0; j<locations.size();j++){
			  if(paths.get(i)._start == locations.get(j).getCenter()){
				  paths.get(i).start = locations.get(j).id;				  
			  }
			  if(paths.get(i)._end == locations.get(j).getCenter()){
				  paths.get(i).end = locations.get(j).id;				  
			  }
		  }		 
	  }
	  	  
	  
  }
  public void pathUpdate2(Location newLocation){
//	  System.out.println("Path update");
	  for(int i=0;i<paths.size();i++){
		  
		  if(paths.get(i).start == newLocation.id){
			  paths.get(i).setStart(newLocation.getCenter());
		  }
		  		  
		  else if(paths.get(i).end == newLocation.id){			  
			  paths.get(i).setEnd(newLocation.getCenter());		  
		  }
  }
 }
  
  
  public void pathUpdate3(){
	  for(int i=0;i<locations.size();i++){
		  for(int j=0; j<paths.size();j++){
			  if(paths.get(j).start == locations.get(i).id){
				  paths.get(j).setStart(locations.get(j).getCenter());
			  }
			  else if(paths.get(j).end == locations.get(i).id){
				  paths.get(j).setEnd(locations.get(j).getCenter());
			  }
		  }
	  }
  }
  
  public int findPath(Point st, Point end){
	  
	  for(int i=0;i<paths.size();i++){
		  if((paths.get(i)._end == st)||(paths.get(i)._end == end)){
			  if((paths.get(i)._start == st)||(paths.get(i)._start == end)){
				  return i;
			  }
		  }
	  }
	  return -1;
  }
  
  public boolean isRelevant(Point location){
	  for(int i=0;i<paths.size();i++){

	  }
	  return false;
  }
  
  public void freePath(Point joint){
	  int size = paths.size();
	  for(int i=0;i<size;i++){
		  if((paths.get(i)._start == joint)||(paths.get(i)._end == joint)){
			  paths.set(i, empty);
//			  g.setBackground(Color.green);
		  }
	  }
  }
  public void removePath(int index){
	  paths.set(index, empty);
  }
  
  public void removeDuplicate(){
	  for(int i = 0; i<paths.size(); i++){
		  for(int j = 0; j<paths.size(); j++){
			  if(i == j){}
			  else if(i != j){
//				  System.out.println("find two duplicates");
				  if(( paths.get(i).end == paths.get(j).end )&&( paths.get(i).start == paths.get(j).start )||( paths.get(i).start == paths.get(j).end )&&( paths.get(i).end == paths.get(j).start )   ){
//					  System.out.println("find one duplicates");
					  paths.remove(j);
				  }
			  }
		  }
	  }
  }
  
  private boolean isDuplicate(Path p){
	  for(int i=0;i<paths.size();i++){
		  if(((paths.get(i)._start) == p._start)&&(paths.get(i)._end) == p._end){
//			  System.out.println("Duplicates");
			  return true;
		  }
//		  System.out.println("not Duplicates");
	  }
	  
	  return false;
  }
  private int insideLocation(Point p){
	  for(int i=0;i<locations.size();i++){
		  if(((Location) locations.get(i)).isThisYou(p) == true){
			  return i;
		  }
	  }
	  return -1;
  }
 
  private Location findLocation(Point p){
	  
		  for(int i=0;i<locations.size();i++){
			  if(((Location) locations.get(i)).isThisYou(_lineStart) == true){
				  System.out.println(i);
				  System.out.println("p:" + p.getX() + " , " +p.getY());
				  return ((Location)locations.get(i));
			  }
		  }

	  System.out.println("can find nothing in location list");
	  return null;
  }
  

  public void mousePressed(Point p) {
    // Mark the beginning of the line
//	  operation = 0;
    _lineEnd = null;
    _released = 0;
    _lineStart = p;
  }


  public void mouseDragged(Point p) {
    // Mark the end of the line
//	  operation = 0;
    _lineEnd = p;
    _released = 0;
   // _clicked = null;
    changeNotify(); // redraw the map
  }
  
  public void mouseReleased(Point p){
	  _released = 1;
	  _lineEnd  = null;
	  _lineStart = null;
//	  System.out.println("released");
  }
  
  public void mouseClicked(Point p){
	  _lineEnd = null;
	  _lineStart = null;
	  _clicked = p;
	    _released = 0;
//	  System.out.println("Clicked");
	  changeNotify();
	  
  }

  
  public int getWidth() { return _image.getWidth(null); }
  public int getHeight() { return _image.getHeight(null); }


  public void addChangeListener(ChangeListener listener) {
    _listener = listener;
  }
}
