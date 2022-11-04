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
import static javax.management.Query.lt;

/**
 *
 * @author NEXT
 */
public class Renderer implements GLEventListener {
    public static DisplayMode dm, dm_old;
   private GLU glu = new GLU();
   private float rquad = 0.0f;
   
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
        gl.glTranslatef( 0f, 0f, -5.0f ); 

        // Rotate The Cube On X, Y & Z
        gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f); 
        
        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 6; l++) {
                        if(l==0){ //TOP
                            if(j == 2){
                                gl.glColor3f(1f,0f,0f); //red color TOP
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            gl.glVertex3f(1.0f + i*2.1f , 1.0f+ j*2.1f, -1.0f+ k*2.1f); // Top Right Of The Quad (Top)
                            gl.glVertex3f( -1.0f+ i*2.1f, 1.0f+ j*2.1f, -1.0f+ k*2.1f); // Top Left Of The Quad (Top)
                            gl.glVertex3f( -1.0f+ i*2.1f, 1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Bottom Left Of The Quad (Top)
                            gl.glVertex3f( 1.0f+ i*2.1f, 1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Bottom Right Of The Quad (Top)
                        }else if(l == 1){ //BOTTOM
                            if(j == 0){
                                gl.glColor3f(0f,1f,0f); //green
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            gl.glVertex3f( 1.0f+ i*2.1f, -1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Top Right Of The Quad
                            gl.glVertex3f( -1.0f+ i*2.1f, -1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Top Left Of The Quad
                            gl.glVertex3f( -1.0f+ i*2.1f, -1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Bottom Left Of The Quad
                            gl.glVertex3f( 1.0f+ i*2.1f, -1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Bottom Right Of The Quad 
                        }else if(l == 2){ //FRONT
                            if(k == 2){
                                gl.glColor3f(0f,0f,1f); //blue
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            gl.glVertex3f( 1.0f+ i*2.1f, 1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Top Right Of The Quad (Front)
                            gl.glVertex3f( -1.0f+ i*2.1f, 1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Top Left Of The Quad (Front)
                            gl.glVertex3f( -1.0f+ i*2.1f, -1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Bottom Left Of The Quad
                            gl.glVertex3f( 1.0f+ i*2.1f, -1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Bottom Right Of The Quad 
                        }else if(l == 3){ //BACK
                            if(k == 0){
                                gl.glColor3f(1f,1f,0f); //yellow
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            gl.glVertex3f( 1.0f+ i*2.1f, -1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Bottom Left Of The Quad
                            gl.glVertex3f( -1.0f+ i*2.1f, -1.0f+ j*2.1f, -1.0f + k*2.1f); // Bottom Right Of The Quad
                            gl.glVertex3f( -1.0f+ i*2.1f, 1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Top Right Of The Quad (Back)
                            gl.glVertex3f( 1.0f+ i*2.1f, 1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Top Left Of The Quad (Back)
                        }else if(l == 4){ //LEFT
                            if(i == 0){
                                gl.glColor3f(1f,0f,1f ); //purple
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            gl.glVertex3f( -1.0f+ i*2.1f, 1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Top Right Of The Quad (Left)
                            gl.glVertex3f( -1.0f+ i*2.1f, 1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Top Left Of The Quad (Left)
                            gl.glVertex3f( -1.0f+ i*2.1f, -1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Bottom Left Of The Quad
                            gl.glVertex3f( -1.0f+ i*2.1f, -1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Bottom Right Of The Quad 
                        }else if(l == 5){ //RIGHT
                            if(i == 2){
                                gl.glColor3f(0f,1f, 1f ); //sky blue
                            }else{
                                gl.glColor3f(0f,0f,0f);
                            }
                            gl.glVertex3f( 1.0f+ i*2.1f, 1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Top Right Of The Quad (Right)
                            gl.glVertex3f( 1.0f+ i*2.1f, 1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Top Left Of The Quad
                            gl.glVertex3f( 1.0f+ i*2.1f, -1.0f+ j*2.1f, 1.0f+ k*2.1f ); // Bottom Left Of The Quad
                            gl.glVertex3f( 1.0f+ i*2.1f, -1.0f+ j*2.1f, -1.0f+ k*2.1f ); // Bottom Right Of The Quad
                        }
                }
                }
            }
        }
        gl.glEnd(); // Done Drawing The Quad
        gl.glFlush();
        
        rquad -= 0.15f;
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
}
