/*
*  MyShape: See ShapeDemo2 for an example how to use this class.
*
*/
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.vecmath.*;


// simple shape class
class MyShape {

    // shape model
    int relativeXpoint = 400;
    int relativeYpoint = 400;

    ArrayList<Point2d> points;
    ArrayList<MyShape> CircularInstance;
    Boolean isFilled = false; // shape is polyline or polygon
    Boolean isClosed = false; // polygon is filled or not
    Boolean isScale = false;   //if we need to Scale or not.

    Color colour = Color.BLACK;
	float strokeThickness = 3.0f;
   MyShape(){
   }
   MyShape(MyShape k){
     this.setColour(k.colour);
     for (int i=0;i<k.points.size();i++){
          this.addPoint(k.points.get(i).x,k.points.get(i).y);
     }
   }
    public Color getColour() {
		return colour;
	}
    public void setIsScale(Boolean isScale){
        this.isScale=isScale;
    }
	public void setColour(Color colour) {
		this.colour = colour;
	}
    public float getStrokeThickness() {
		return strokeThickness;
	}
	public void setStrokeThickness(float strokeThickness) {
		this.strokeThickness = strokeThickness;
	}
	public Boolean getIsFilled() {
		return isFilled;
	}
	public void setIsFilled(Boolean isFilled) {
		this.isFilled = isFilled;
	}
	public Boolean getIsClosed() {
		return isClosed;
	}
	public void setIsClosed(Boolean isClosed) {
		this.isClosed = isClosed;
	}
	
	// for selection
	boolean isSelected;
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	// for drawing
    Boolean hasChanged = false; // dirty bit if shape geometry changed
    int[] x_points, y_points;

	public float rotation = 0;
    public double rotateAdder=0;
    public double angelFromTheOrigin=0;
	public double scale = 1.0;
    public double scaleMultiplyier=0;
    public Point2d startingPoint;
    public void setScale(double scale){
        this.scale=scale;
    }
    // replace all points with array
    public void setPoints(double[][] pts) {
        points = new ArrayList<Point2d>();
        for (double[] p : pts) {
            points.add(new Point2d(p[0],p[1]));
        }
        hasChanged = true;
    }
    
    // add a point to end of shape
    public void addPoint(double x, double y) {
    	if (points == null)
    		points = new ArrayList<Point2d>();
    	points.add(new Point2d(x,y));
    	hasChanged = true;
    }

    
    // paint the shape
    public void paint(Graphics2D g2) {

        //update the shape in java Path2D object if it changed
        if (hasChanged) {
            x_points = new int[points.size()];
            y_points = new int[points.size()];
            for (int i=0; i < points.size(); i++) {
                x_points[i] = (int)points.get(i).x;
                y_points[i] = (int)points.get(i).y;
            }
            hasChanged = false;
        }

        //don't draw if path2D is empty (not shape)
        if (x_points != null) {
        	
        	// special draw for selection
        	if (isSelected) {
        		g2.setColor(Color.YELLOW);
        		g2.setStroke(new BasicStroke(strokeThickness * 4));
            	if (isClosed)
                    g2.drawPolygon(x_points, y_points, points.size());
                else {
                    g2.drawPolyline(x_points,y_points,points.size());
        	}
            }
        	
        	g2.setColor(colour);

            // call right drawing function
            if (isFilled) {
                g2.fillPolygon(x_points, y_points, points.size());
            }
            else {
            	g2.setStroke(new BasicStroke(strokeThickness)); 
            	if (isClosed)
                    g2.drawPolygon(x_points, y_points, points.size());
                else {
                    double angle=this.angelFromTheOrigin;
                    if (angle>0.09){
                        g2.drawPolyline(x_points, y_points, points.size());
                        CircularInstance=new ArrayList<MyShape>();
                        for(int i=1;i<(double)2*Math.PI/angle;i++){
                            MyShape test = new MyShape(this);
                            test.setSelected(false);
                            test.rotateTransform((double)i*angle);
                            test.x_points = new int[this.points.size()];
                            test.y_points = new int[this.points.size()];
                            for (int j=0; j < points.size(); j++) {
                                test.x_points[j] = (int)test.points.get(j).x;
                                test.y_points[j] = (int)test.points.get(j).y;

                            }
                            CircularInstance.add(test);
                            hasChanged = false;
                            g2.drawPolyline(test.x_points,test.y_points,test.points.size());
                        }
                    }
                    else{
                    g2.drawPolyline(x_points, y_points, points.size());
            }
        }
    }
        }
    }

    //find the angle between tree points.
    static double angleBetweenThreePoints(Point2d C, Point2d P1,Point2d P2){
        Vector2d v=new Vector2d(); //v=P2-C
        v.sub(P1,C);          //v=P2-C.
        Vector2d v2=new Vector2d();//v2=P1-C
        v2.sub(P2,C);
        return v.angle(v2);

    }
    // find closest point
    static Point2d closestPoint(Point2d M, Point2d P1, Point2d P2)
    {
        Vector2d v = new Vector2d();
        v.sub(P1,P2); // v = P2 - P1

        // early out if line is less than 1 pixel long
        if (v.lengthSquared() < 0.5)
            return P2;

        Vector2d u = new Vector2d();
        u.sub(M,P2); // u = M - P1

        // scalar of vector projection ...
        double s = u.dot(v)  // u dot v
                / v.dot(v); // v dot v

        // find point for constrained line segment
        if (s < 0)
            return P2;
        else if (s > 1)
            return P1;
        else {
            Point2d I = P2;
            Vector2d w = new Vector2d();
            w.scale(s, v); // w = s * v
            I.add(w); // I = P0 + w
            return I;
    }
    }
    
    // return perpendicular vector
    static public Vector2d perp(Vector2d a)
    {
    	return new Vector2d(-a.y, a.x);
    }
    
    // line-line intersection
    // return (NaN,NaN) if not intersection, otherwise returns intersecting point
    static Point2d lineLineIntersection(Point2d P0, Point2d P1, Point2d Q0, Point2d Q1)
    {
    	
    	// TODO: implement

    	return new Point2d();
    }
    

    // affine transform helper
    // return P_prime = T * P    
    Point2d transform( AffineTransform T, Point2d P) {
    	Point2D.Double p = new Point2D.Double(P.x-relativeXpoint, P.y-relativeYpoint);
    	Point2D.Double q = new Point2D.Double();
    	T.transform(p, q);
    	return new Point2d(q.x+relativeXpoint, q.y+relativeYpoint);
    }
    public void scaleTransform(double scale){
        AffineTransform T = new AffineTransform();
        T.scale(scale,scale);
        for(Point2d point:points){
            Point2d tmp= new Point2d(transform(T,point));
            point.x=tmp.x;
            point.y=tmp.y;
        }
        hasChanged=true;
    }
    public void rotateTransform(double rotate){
        AffineTransform T=new AffineTransform();
        T.rotate(rotate);
        for (Point2d point:points){
            Point2d tmp=new Point2d(transform(T,point));
            point.x=tmp.x;
            point.y=tmp.y;
        }
        hasChanged=true;
    }
    // hit test with this shape
    public boolean hittest(Point2d point2d)
    {   
    	if (points != null) {
        double min=100;
        for(int i=0;i<points.size()-1;i++){
            Point2d p0=new Point2d(points.get(i).x,points.get(i).y);
            Point2d p1=new Point2d(points.get(i+1).x,points.get(i+1).y);
            double d = point2d.distance(closestPoint(point2d,p0,p1));
            if (min>d){
                min=d;
            }
        }
        if (min<3){
         return true;
    	}
        }
    	
    	return false;
    }
    public boolean superHitTest(Point2d point2d){
        if (CircularInstance!=null){
        for (int i=0;i<CircularInstance.size();i++){
             if (CircularInstance.get(i).hittest(point2d)){
                 return true;
             }
        }
        return false;
        }
        else return false;
    }
    void resetCurrentLine(){
        this.scaleMultiplyier=0;
        this.setScale(1.0);
        this.startingPoint=null;
        this.rotateAdder=0;
    }
    void scaleInitializer(Point2D center,Point2D arg0){
        double distanceFromTheCenter=arg0.distance(center);
        if (this.scaleMultiplyier!=0){
            double tmp= distanceFromTheCenter/this.scaleMultiplyier;
            this.setScale(tmp);
            this.scaleMultiplyier=distanceFromTheCenter;
        }
        else{
            this.scaleMultiplyier=distanceFromTheCenter;
            this.scale=1.0;
        }
        this.scaleTransform(this.scale);
    }
    void rotationInitializer(Point2D center,Point2D arg0){
        Point2d tmpCenter=new Point2d(center.getX(),center.getY());
        Point2d tmpArg0=new Point2d(arg0.getX(),arg0.getY());
        double angelFromtheOriginal=this.angleBetweenThreePoints(tmpCenter, this.startingPoint, tmpArg0);
        this.angelFromTheOrigin=angelFromtheOriginal;
        this.rotateAdder=Math.abs(angelFromtheOriginal-this.rotateAdder);
        this.rotateTransform(this.rotateAdder);
        this.rotateAdder=angelFromtheOriginal;
    }
}


