import java.awt.*;
import java.util.*;
import java.awt.geom.Point2D;
/**
 * Created with IntelliJ IDEA.
 * User: yundihuang
 * Date: 2013-06-13
 * Time: 5:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class Origin extends Shape{
    private int colorIndex;
    static int Fwidth;
    static int Fheight;
    private java.util.List<Color> colorList= Arrays.asList(Color.blue,Color.pink,Color.green,Color.red);
    Origin(int x, int y, int width, int height,int Fwidth,int Fheight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.setColor(Color.gray);
        colorIndex=-1;
        this.Fwidth=Fwidth;
        this.Fheight=Fheight;
    }
    Point2D getCenter(){
        return new Point2D.Double(Fwidth/2,Fheight/2);
    }
    int getColorIndex(){
        return colorIndex;
    }
    void paint(Graphics2D g) {
        g.fillOval(this.x,this.y,this.width,this.height);
        g.setColor(this.color);
    }
    boolean isHit(Point point){
        double radiusSquire=(double)(this.width/2)*(double)(this.width/2);
        //circle function.
        if ((Math.pow((point.getX()-(double)Fwidth/2),2)+Math.pow((point.getY()-(double)Fheight/2),2))<=radiusSquire)
            return true;
        else return false;
    }
    void colorSwitch(){
        if (this.colorIndex==-1){
            colorIndex++;
            this.setColor(colorList.get(this.colorIndex));
        }
        else{
            colorIndex++;
            this.setColor(colorList.get(this.colorIndex%colorList.size()));
        }
    }
}
