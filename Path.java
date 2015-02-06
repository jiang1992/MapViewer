import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;


public class Path implements Comparable<Path>{
	
	Point _start;
	Point _end;
	int  _xStart;
	int _yStart;
	int _xEnd;
	int _yEnd;
	int id;
	int start;
	int end;

	double distance;
	
	public Path(Point start, Point end){
		this._start = start;
		this._end = end;
		
		_xStart = (int) start.getX();
		_yStart = (int) start.getY();
		
		_xEnd   = (int) end.getX();
		_yEnd   = (int) end.getY();
								
		distance = Math.sqrt((_xEnd - _xStart)*(_xEnd - _xStart) + (_yEnd - _yStart)*(_yEnd - _yStart));
	}
	
	public void setPath(int lStart, int lEnd){
		this.start = lStart;
		this.end = lEnd;
	
	}
	
	public void setStart(Point newStart){
		
		this._start = newStart;		
		_xStart = (int) _start.getX();
		_yStart = (int) _start.getY();

	}
	public void setEnd(Point newEnd){
		
		this._end = newEnd;
		_xEnd = (int) _end.getX();
		_yEnd = (int) _end.getY();
		
	}
	
	public void draw(Graphics2D g){

		g.setStroke(new BasicStroke(5));
		g.setColor(Color.BLACK);
		g.drawLine(_xStart, _yStart, _xEnd, _yEnd);

	}
	
	
	
    public Path(int idfrom, int idto, double distance)
    {  
        this.start = idfrom;  
        this.end = idto;  
        this.distance = distance;  
    } 
    public Path()
    {
        this.start = 0;
  	    this.end = 0;
  	    this.distance = Integer.MAX_VALUE;	  
    }
	
    public int getStart() 
    {  
        return start;  
    }  
    public int getEnd() 
    {  
        return end;  
    } 
    
    public double getDistance() 
    {  
        return distance;  
    }  
    @Override  
    public String toString() 
    {  
        return start + "->" + end;  
    }
    
	
	public void pathSetStartEnd(int idfrom, int idto)
    {  
        this.start = idfrom;  
        this.end = idto;  
    } 
	public void draw2(Graphics2D g){
		g.setStroke(new BasicStroke(5));
		g.setColor(Color.GREEN);
		g.drawLine(_xStart, _yStart, _xEnd, _yEnd);
	}
	@Override
    public int compareTo(Path obj) 
    {  
        double targetDistance = obj.getDistance();  
        
        /*
        return distance>targetDis?1:(distance==targetDis?0:-1);  
        */
        
        int compareResult = 0;
        
        if (distance > targetDistance)
        {
        	compareResult = 1;
        }
        else if(distance < targetDistance)
        {
        	compareResult = -1;
        }
        
        return compareResult;      
    }
}
