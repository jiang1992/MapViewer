
import java.awt.*;
import java.awt.geom.*;
import java.util.*;


public class Location {
  private static final int SIZE = 20;
  private final static float dash1[] = {3.0f};
  private final static BasicStroke dashed = new BasicStroke(1.0f, 
                                            BasicStroke.CAP_BUTT, 
                                            BasicStroke.JOIN_MITER, 
                                            3.0f, dash1, 0.0f);

  private Point _point;
  int x;
  int y;
  private boolean _selected;
  String name = "unknown";
  int id = 0;
  
  ////////////////////////////// James/////////////////////////////////////////////
  private Map<Location,Double> child = new HashMap<Location,Double>();/////////////
  /////////////////////////////////////////////////////////////////////////////////
  
  public Location(Point point) {
    _point = point;
    x = (int) point.getX();
    y = (int) point.getY();
    this.id = 0;
  }
  
  public Point getPoint()
  {
	  return _point;
  }
  
  public int getID() 
  {  
      return this.id;  						////////////////
  } 
  
  public void setNameID(String name, int id)
  {
	  this.name = name;
	  this.id = id;
  }
  
  //////////////////////////////////////////////////////////
  public Location(String name)          ////////////////
  {  									/////////////////
      this.name=name;  					//////////////////
  } 									//////////////////
  											///////////////
  public Location()//////////////////////////////////
  {  										////////////
      this.name="unknown";/////////////////////////////
      this.id = 0;							/////////////
      this.x = 0;						////////////////
      this.y = 0;							//////////////////
  } 										//////////////
  public String getName() {  ///////////////////////////////////
      return name;  						////////////////
  } 										////////////////
  											///////////////////
  public Map<Location, Double> getChild() {  //////////////////////
      return child;  							//////////////////
  } 												/////////////
  public void setChild(Map<Location, Double> child) {  //////////////
      this.child = child;  								////////////
  }    														///////
  //////////////////////////////////////////////////////////// ////
  public void draw(Graphics g) {
    g.setColor(Color.RED);
    g.fillOval((int) _point.getX() - SIZE/2, 
               (int) _point.getY() - SIZE/2, 
               SIZE, SIZE);
   
  }

  public void drawSelect(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.BLACK);
    g2.setStroke(dashed);
    g.drawRect((int) _point.getX() - SIZE/2,
               (int) _point.getY() - SIZE/2,
               SIZE, SIZE);
    g.setColor(Color.YELLOW);
    g.fillOval((int) _point.getX() - SIZE/2, 
               (int) _point.getY() - SIZE/2, 
               SIZE+5, SIZE+5);
  }

  
  public void setName(String locationName){
	  this.name = locationName;
  }
  
  public void setId(int newId){
	  this.id = newId;
  }
  public void setX(int newX){
	  
	  this.x = newX;
	  Point temp = new Point(this.x, this.y);
	  this._point = temp;
  }
  
  public void setY(int newY){
	  this.y = newY;
	  Point temp = new Point(this.x, this.y);
	  this._point = temp;
  }
  
  public void setCenter(Point p){
	  _point = p;
	  x = (int) p.getX();
	  y = (int) p.getY();
  }
  public int getX(){
	  return (int) _point.getX();
  }
  public int getY(){
	  return (int) _point.getY();
  }
  /**
   * Return true if this point is inside of this location.
   */
  public boolean isThisYou(Point p) {
    int x = (int) _point.getX();
    int y = (int) _point.getY();
    int px = (int) p.getX();
    int py = (int) p.getY();
    int radius = SIZE/2;
    return px > x - radius && px < x + radius && 
           py > y - radius && py < y + radius;
  }
  public Point getCenter(){
	  return _point;
  }
}
