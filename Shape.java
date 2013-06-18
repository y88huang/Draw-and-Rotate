import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import javax.vecmath.Point2d;
import java.awt.event.MouseEvent;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.vecmath.*;
/**
 * Created with IntelliJ IDEA.
 * User: yundihuang
 * Date: 2013-06-13
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
abstract class Shape{
    int x; //origin point.
    int y;
    int width;      //basic height and width parameters.
    int height;
    Color color;    //Colour info.

    //Get functions.
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    public int getWidth(){
        return this.width;
    }
    public int getHeight(){
        return this.height;
    }
    public Color getColor(){
        return this.color;
    }

    //Set functions.
    public void setX(int x){
        this.x=x;
    }
    public void setY(int y){
        this.y=y;
    }
    public void setWidth(int width){
        this.width=width;
    }
    public void setHeight(int height){
        this.height=height;
    }
    public void setColor(Color tmp){
        this.color=tmp;
    }
    abstract void paint(Graphics2D g);
}
