/**
 * Created with IntelliJ IDEA.
 * User: yundihuang
 * Date: 2013-06-11
 * Time: 12:13 AM
 * To change this template use File | Settings | File Templates.
 */
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






//Main class
public class Draw extends JPanel implements MouseInputListener {
    static int Fwidth =800;
    static int Fheight=800;
    static int originRadius=50;
    static boolean isDrawing=false;
    float thicknessHelper=3.0f;
    MyShape currentSelected=null;
    List<MyShape> polyLines;
    MyShape drawingLine=null;
    Point2d colorStartingPoint;
    Point2d startingPointHelper;
//    MyShape currentSelected=null;


    Draw() {
        // add listeners
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    //The Origin Point in the middle of the screen.
    Origin center;
    // the application entry point
    public static void main(String[] args) {
        // add my inherited JComponent to the JFrame
        Draw canvas = new Draw();
        canvas.setBackground(Color.BLACK);
        JFrame f = new JFrame("DrawSomeThing"); // jframe is the app window
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBackground(Color.BLACK);
        f.setSize(Fwidth, Fheight); // window size
        f.setContentPane(canvas); // add canvas to jframe
//        f.setForeground(Color.black);
        f.setVisible(true); // show the window
        canvas.initialize();
    }

     //Initialize the center,and the arrayList.
    public void initialize() {
        center = new Origin(Fwidth/2-originRadius/2,Fheight/2-originRadius/2,originRadius,originRadius,Fwidth,Fheight);
        polyLines=new ArrayList<MyShape>();
    }

    public void dragColorChanger(Point2d center,Point2d current){
        double colorAngle=MyShape.angleBetweenThreePoints(center,colorStartingPoint,current);
        float f=(float)colorAngle;
        float pi=(float)Math.PI;
        Color c=Color.getHSBColor(f/pi,1.0f,1.0f);
        this.center.setColor(c);
    }
    public void dragThicknessScaler(Point2d center,Point2d current,Point2d start){
         double scaleDistance=current.distance(center);
         double originalDistance=center.distance(start);
         float tmpScale=(float)scaleDistance;
         float originTmp=(float)originalDistance;
         float factor=tmpScale/originTmp;
         thicknessHelper=factor*3.0f;

    }
    // custom graphics drawing
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);
        //Display the polylines already created.

        for (MyShape d:polyLines){
            d.paint(g2);
        }

        if (drawingLine != null)
            drawingLine.paint(g2);
       g2.setColor(center.getColor());
        center.paint(g2);
    }
    @Override
    public void mouseClicked(MouseEvent arg0) {

        if (center.isHit(arg0.getPoint())&&arg0.getButton()==1) {
            if(arg0.getClickCount()==1){
                center.colorSwitch();
                if(currentSelected!=null){
                   currentSelected.setSelected(false);
                }
            }
            else if(center.isHit(arg0.getPoint())&&arg0.getClickCount()>=2){
                thicknessHelper=3.0f;
                polyLines.clear();
            }
        }
        if (arg0.getClickCount()==2){
            thicknessHelper=3.0f;
        }
        else if (arg0.getButton()==1&&!center.isHit(arg0.getPoint())){
            boolean hitSomeThing=false;
            for (MyShape line:polyLines){
                Point2d tmp=new Point2d(arg0.getX(),arg0.getY());
                Point2d C=new Point2d(Fwidth/2-originRadius/2,Fheight/2-originRadius);
               if(!center.isHit(arg0.getPoint())&&line.hittest(tmp)){
                    line.setSelected(true);
                   line.setIsScale(true);
                   line.setScale(tmp.distance(C));
                   currentSelected=line;
                   hitSomeThing=true;
                   break;
                }
               else if(!center.isHit(arg0.getPoint())&&line.superHitTest(tmp)){
                   line.setSelected(true);
                   line.setIsScale(true);
                   line.setScale(tmp.distance(C));
                   currentSelected=line;
                   hitSomeThing=true;
                   break;
               }

               else {
                   line.setSelected(false);
                   line.setIsScale(false);
               }


            }
            if(!hitSomeThing){
                currentSelected=null;
            }
        }

        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
//        System.out.format("enter %d,%d\n", arg0.getX(), arg0.getY());
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
//        System.out.format("exit %d,%d\n", arg0.getX(), arg0.getY());
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        System.out.format("Pressing\n");
        if(arg0.getButton()==1&&center.isHit(arg0.getPoint())){      //If it hits the origin center.
          //Starts a new line.

          drawingLine=new MyShape();
          if(thicknessHelper!=3.0f){
              drawingLine.setStrokeThickness(thicknessHelper);
          }
          isDrawing=true;
          drawingLine.setColour(center.getColor());
          drawingLine.setSelected(true);            //Line is selected when drawing.
        }
        else if(currentSelected!=null){
            if(currentSelected.isSelected()){
                Point2d tmpArg0=new Point2d(arg0.getX(),arg0.getY());
                currentSelected.startingPoint=new Point2d(arg0.getX(),arg0.getY());
                if (currentSelected.hittest(tmpArg0)||currentSelected.superHitTest(tmpArg0)){
                startingPointHelper=new Point2d(arg0.getX(),arg0.getY());
                 }
            }
        }
        else if (currentSelected==null){
               this.colorStartingPoint=new Point2d(arg0.getX(),arg0.getY());
        }
        repaint();

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
//        System.out.format("release %d,%d\n", arg0.getX(), arg0.getY());

        if(drawingLine!=null){
            drawingLine.setSelected(false);
            polyLines.add(drawingLine);
            drawingLine=null;
            isDrawing=false;
        }
        if(currentSelected!=null){
          currentSelected.resetCurrentLine();
          startingPointHelper=null;
        }
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        Point2d tmpCenter=new Point2d(center.getX(),center.getY());
        Point2d tmpArg0=new Point2d(arg0.getX(),arg0.getY());
//        if(currentSelected!=null){
//            if(currentSelected.hittest(currentSelected.startingPoint)){
//                System.out.format("Victory!!!\n");
//            }
//        }
        if(drawingLine!=null&&center.getColorIndex()>-1){   //Doesn't draw when gray.
               drawingLine.addPoint(arg0.getX(), arg0.getY());
             }

        else if (currentSelected!=null&&startingPointHelper!=null){
            double kk=startingPointHelper.distance(currentSelected.startingPoint);
            currentSelected.scaleInitializer(center.getCenter(),arg0.getPoint());
            currentSelected.rotationInitializer(center.getCenter(),arg0.getPoint());
        }
        else if (currentSelected!=null&&startingPointHelper==null){
            if(colorStartingPoint!=null&&center.getColorIndex()!=-1){
                dragColorChanger(tmpCenter,tmpArg0);
                dragThicknessScaler(tmpCenter,tmpArg0,colorStartingPoint);
                currentSelected.setColour(center.getColor());
                currentSelected.setStrokeThickness(thicknessHelper);
            }
        }
        else if (currentSelected==null){
            if(colorStartingPoint!=null&&center.getColorIndex()!=-1){
            dragColorChanger(tmpCenter,tmpArg0);
            dragThicknessScaler(tmpCenter,tmpArg0,colorStartingPoint);
            }
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        repaint();
    }
}
