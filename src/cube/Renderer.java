/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cube;


import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import java.awt.DisplayMode;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import static javax.management.Query.lt;

/**
 *
 * @author NEXT
 */
public class Renderer implements GLEventListener, KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    public static DisplayMode dm, dm_old;
    private GLU glu = new GLU();
    
    private static final int CANVAS_WIDTH  = 640;
    private static final int CANVAS_HEIGHT = 480;
    
    private static final float DEFAULT_CAMERA_ANGLE_X = 45.0f;
    private static final float DEFAULT_CAMERA_ANGLE_Y = 45.0f;
    
    private static final float DEFAULT_ZOOM = -5.0f;
    private static final float MIN_ZOOM = -10.0f;
    private static final float MAX_ZOOM = 0.0f;
    private float zoom = DEFAULT_ZOOM;
    
    private static final float ZERO_F = 0.0f;
    private static final float ONE_F  = 1.0f;
    private static final float TWO_F  = 2.0f;
    
    private static final int CAMERA_ROTATE_STEP_DEGREES  = 5;
    
    private float cameraAngleX = DEFAULT_CAMERA_ANGLE_X;
    private float cameraAngleY = DEFAULT_CAMERA_ANGLE_Y;
    private float cameraAngleZ = ZERO_F;
    
    private float xAngle = 10;
    private float yAngle = 10;
    private float zAngle = 10;
    private int r1 = 1;
    private int c1 = 1;
    private int r2 = 1;
    private int c2 = 1;
    private float p[] = {0,0,0};
    
    private int mouseX = CANVAS_WIDTH/2;
    private int mouseY = CANVAS_HEIGHT/2;
   
    final float[] zRotate(float p[], float sin, float cos) {
      float temp;
      temp = cos * p[0] + sin * p[1];
      p[1] = -sin * p[0] + cos * p[1];
      p[0] = temp;
      return p;
   }
 
   final float[] yRotate(float p[], float sin, float cos) {
      float temp;
      temp = cos * p[0] + sin * p[2];
      p[2] = -sin * p[0] + cos * p[2];
      p[0] = temp;
      return p;
   }
 
   final float[] xRotate(float p[], float sin, float cos) {
      float temp;
      temp = cos * p[1] + sin * p[2];
      p[2] = -sin * p[1] + cos * p[2];
      p[1] = temp;
      return p;
   }
    
    
    @Override
    public void init(GLAutoDrawable drawable) {    
        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
    }    

    @Override    
    public void display(GLAutoDrawable drawable) {    
        final GL2 gl = drawable.getGL().getGL2();  
  
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef(ZERO_F, ZERO_F, -5.0f ); 

        // Rotate The Cube On X, Y & Z
        gl.glTranslatef(ZERO_F, ZERO_F, zoom);
        gl.glRotatef(cameraAngleX, ONE_F, ZERO_F, ZERO_F);
	gl.glRotatef(cameraAngleY, ZERO_F, ONE_F, ZERO_F);
	gl.glRotatef(cameraAngleZ, ZERO_F, ZERO_F, ONE_F);
        
        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 6; l++) {
                        
                        
                        if(l==0){ //TOP
                            if(j == 2){
                                gl.glColor3f(1f,0f,0f); //red
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] ); // Top Right Of The Quad (Left)
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                        
                        
                        }else if(l == 1){ //BOTTOM
                            if(j == 0){
                                gl.glColor3f(0f,1f,0f); //green
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] ); // Top Right Of The Quad (Left)
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                        
                        
                        }else if(l == 2){ //FRONT
                            if(k == 2){
                                gl.glColor3f(0f,0f,1f); //blue
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] ); // Top Right Of The Quad (Left)
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                        
                        
                        }else if(l == 3){ //BACK
                            if(k == 0){
                                gl.glColor3f(1f,1f,0f); //yellow
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] ); // Top Right Of The Quad (Left)
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                        
                        
                        }else if(l == 4){ //LEFT
                            if(i == 0){
                                gl.glColor3f(1f,0f,1f ); //purple
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] ); // Top Right Of The Quad (Left)
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -3.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                        
                        
                        }else if(l == 5){ //RIGHT
                            if(i == 2){
                                gl.glColor3f(0f,1f, 1f ); //sky blue
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] ); // Top Right Of The Quad (Left)
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -1.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -1.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            p[0] = -1.1f+ i*2.1f;
                            p[1] = -3.1f+ j*2.1f;
                            p[2] = -3.1f+ k*2.1f;
                            if(r1 == i+1){
                            p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
                            }
                            gl.glVertex3f( p[0],p[1],p[2] );
                            
                        }
                }
                }
            }
        }
        gl.glEnd(); // Done Drawing The Quad
        gl.glFlush();
        xAngle += 0.1;
    }  
    
    @Override    
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height ) {        
        final GL2 gl = drawable.getGL().getGL2();
//        if(height lt;= 0)
//         height = 1;

        final float h = ( float ) width / ( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();

        glu.gluPerspective( 90.0f, h, 1.0, 20.0 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }  
    
    @Override    
    public void dispose(GLAutoDrawable drawable) {    
        
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        final int buffer = 2;

        if (e.getX() < mouseX - buffer) {
            cameraAngleY -= CAMERA_ROTATE_STEP_DEGREES;
        }
        else if (e.getX() > mouseX + buffer) {
            cameraAngleY += CAMERA_ROTATE_STEP_DEGREES;
        }

        if (e.getY() < mouseY - buffer) {
            cameraAngleX -= CAMERA_ROTATE_STEP_DEGREES;
        }
        else if (e.getY() > mouseY + buffer) {
            cameraAngleX += CAMERA_ROTATE_STEP_DEGREES;
        }

        mouseX = e.getX();
        mouseY = e.getY();
    }
    
    @Override public void keyReleased(KeyEvent e) { }
    @Override public void keyTyped(KeyEvent e) { }
    @Override public void mouseClicked(MouseEvent e) { }
    @Override public void mouseEntered(MouseEvent e) { }
    @Override public void mouseExited(MouseEvent e) { }
    @Override public void mousePressed(MouseEvent e) { }
    @Override public void mouseReleased(MouseEvent e) { }
    @Override public void mouseMoved(MouseEvent e) { }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom += -e.getWheelRotation();
        
        if(zoom > MAX_ZOOM) {
            zoom = MAX_ZOOM;
        }
        if(zoom < MIN_ZOOM) {
            zoom = MIN_ZOOM;
        }
    }
}
