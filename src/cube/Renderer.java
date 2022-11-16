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
    
    private static float[][][][][][] cube = new float[3][3][3][6][4][3];
    private static String[][][] cubeColors = new String[6][3][3];
    private static int[] selectedCube = new int[3];
    private static float offset = 0.05f;
    private static boolean rotating = false;
    private static float rotateAngle = 0.05f;
    private static boolean opposite;
    
    private static final int TOP = 0;
    private static final int BOTTOM = 1;
    private static final int FRONT = 2;
    private static final int BACK = 3;
    private static final int LEFT = 4;
    private static final int RIGHT = 5;
    
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
    
    private static final float SECTION_ROTATE_ANGLE = (float) Math.toRadians(90);
    
    private float xAngle = 0;
    private float yAngle = 0;
    private float zAngle = 0;
    private int x1 = 0;
    private int y1 = 0;
    private int z1 = 0;
    private float p[] = {0,0,0};
    
    private int mouseX = CANVAS_WIDTH/2;
    private int mouseY = CANVAS_HEIGHT/2;
    
    private final String WHITE = "WHITE";
    private final String YELLOW = "YELLOW";
    private final String RED = "RED";
    private final String ORANGE = "ORANGE";
    private final String GREEN = "GREEN";
    private final String BLUE = "BLUE";
    private final String PURPLE = "PURPLE";
    private final String BLACK = "BLACK";
    private final float ALPHA = 0.65f;
   
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
    
   interface Rotation {
        void rotate(int n);
   }
   
   private final Rotation[] rotateActions = new Rotation[] {
        new Rotation() {@Override public void rotate(int n) { xRotationCounterclockwise(n); } },
        new Rotation() {@Override public void rotate(int n) { xRotationClockwise(n); } },
        new Rotation() {@Override public void rotate(int n) { yRotationCounterclockwise(n); } },
        new Rotation() {@Override public void rotate(int n) { yRotationClockwise(n); } },
        new Rotation() {@Override public void rotate(int n) { zRotationClockwise(n); } },
        new Rotation() {@Override public void rotate(int n) { zRotationCounterclockwise(n); } },
    };
    
    @Override
    public void init(GLAutoDrawable drawable) {    
        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    if(i == 0){
                        cubeColors[i][j][k] = WHITE;
                    }else if(i == 1){
                        cubeColors[i][j][k] = YELLOW;
                    }else if(i == 2){
                        cubeColors[i][j][k] = BLUE;
                    }else if(i == 3){
                        cubeColors[i][j][k] = GREEN;
                    }else if(i == 4){
                        cubeColors[i][j][k] = RED;
                    }else {
                        cubeColors[i][j][k] = ORANGE;
                    }
                }
            }
        }
        
        selectedCube[0] = TOP;
        selectedCube[1] = 1;
        selectedCube[2] = 1;
        
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    //TOP
                    cube[x][y][z][0][0][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][0][0][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][0][0][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][0][1][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][0][1][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][0][1][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][0][2][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][0][2][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][0][2][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][0][3][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][0][3][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][0][3][2] = -1.1f+ z*2.1f;
                    
                    //BOTTOM
                    cube[x][y][z][1][0][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][1][0][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][1][0][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][1][1][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][1][1][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][1][1][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][1][2][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][1][2][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][1][2][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][1][3][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][1][3][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][1][3][2] = -3.1f+ z*2.1f;
                    
                    //FRONT
                    cube[x][y][z][2][0][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][2][0][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][2][0][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][2][1][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][2][1][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][2][1][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][2][2][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][2][2][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][2][2][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][2][3][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][2][3][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][2][3][2] = -1.1f+ z*2.1f;
                    
                    //BACz
                    cube[x][y][z][3][0][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][3][0][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][3][0][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][3][1][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][3][1][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][3][1][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][3][2][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][3][2][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][3][2][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][3][3][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][3][3][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][3][3][2] = -3.1f+ z*2.1f;
                    
                    //LEFT
                    cube[x][y][z][4][0][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][4][0][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][4][0][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][4][1][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][4][1][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][4][1][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][4][2][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][4][2][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][4][2][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][4][3][0] = -3.1f+ x*2.1f;
                    cube[x][y][z][4][3][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][4][3][2] = -1.1f+ z*2.1f;
                    
                    //RxGHT
                    cube[x][y][z][5][0][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][5][0][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][5][0][2] = -3.1f+ z*2.1f;
                    
                    cube[x][y][z][5][1][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][5][1][1] = -1.1f+ y*2.1f;
                    cube[x][y][z][5][1][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][5][2][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][5][2][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][5][2][2] = -1.1f+ z*2.1f;
                    
                    cube[x][y][z][5][3][0] = -1.1f+ x*2.1f;
                    cube[x][y][z][5][3][1] = -3.1f+ y*2.1f;
                    cube[x][y][z][5][3][2] = -3.1f+ z*2.1f;
                }
            }
        }
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
        
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        
        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    for (int side = 0; side < 6; side++) {
                        
                        boolean selected = false;
                        
                        if(side==0 && y == 2){ //TOP
                            this.glColor(gl, cubeColors[side][x][z]);
                            if(side == selectedCube[0] && x == selectedCube[1] && z == selectedCube[2]){
                                this.glSelectedCubeColor(gl, cubeColors[side][x][z]);
                                selected = true;
                            }
                        }else if(side == 1 && y == 0){ //BOTTOM
                            this.glColor(gl, cubeColors[side][x][z]);
                            if(side == selectedCube[0] && x == selectedCube[1] && z == selectedCube[2]){
                                 this.glSelectedCubeColor(gl, cubeColors[side][x][z]);
                                 selected = true;
                            }
                        }else if(side == 2 && z == 2){ //FRONT
                            this.glColor(gl, cubeColors[side][x][y]);
                            if(side == selectedCube[0] && x == selectedCube[1] && y == selectedCube[2]){
                                 this.glSelectedCubeColor(gl, cubeColors[side][x][y]);
                                 selected = true;
                            }
                        }else if(side == 3 && z == 0){ //BACK
                            this.glColor(gl, cubeColors[side][x][y]);
                            if(side == selectedCube[0] && x == selectedCube[1] && y == selectedCube[2]){
                                 this.glSelectedCubeColor(gl, cubeColors[side][x][y]);
                                 selected = true;
                            }
                        }else if(side == 4 && x == 0){ //LEFT
                            this.glColor(gl, cubeColors[side][y][z]);
                            if(side == selectedCube[0] && y == selectedCube[1] && z == selectedCube[2]){
                                 this.glSelectedCubeColor(gl, cubeColors[side][y][z]);
                                 selected = true;
                            }
                        }else if(side == 5 && x == 2){ //RIGHT
                            this.glColor(gl, cubeColors[side][y][z]);
                            if(side == selectedCube[0] && y == selectedCube[1] && z == selectedCube[2]){
                                 this.glSelectedCubeColor(gl, cubeColors[side][y][z]);
                                 selected = true;
                            }
                        }else{
                            this.glColor(gl, BLACK);
                        }
                        this.smallCubeSection(gl, x, y, z, side);
                        if(selected){
                            this.glColor(gl, PURPLE);
                            this.drawBorder(gl, x, y, z, side);
                        }
                        if(x1 != 0){
                            if(opposite){
                                xAngle -= Math.toRadians(rotateAngle);
                            }else{
                                xAngle += Math.toRadians(rotateAngle);
                            }
                            if(Math.toDegrees(Math.abs(xAngle)) >= 90) {
                                xAngle = 0;
                                x1 = 0;
                                if(selectedCube[0] == TOP) {
                                    //x1 = selectedCube[1] + 1;
                                    this.xRotationCounterclockwise(selectedCube[1] + 1);
                                }else if(selectedCube[0] == FRONT) {
                                    this.xRotationCounterclockwise(selectedCube[1] + 1);
                                    //x1 = selectedCube[1] + 1;
                                }else if(selectedCube[0] == BACK) {
                                    this.xRotationClockwise(selectedCube[1] + 1);
                                    //x1 = selectedCube[1] + 1;
                                }else if(selectedCube[0] == BOTTOM) {
                                    this.xRotationCounterclockwise(selectedCube[1] + 1);
                                    //x1 = selectedCube[1] + 1;
                                }
                            }
                        }
                        if(y1 != 0){
                            if(opposite){
                                yAngle -= Math.toRadians(rotateAngle);
                            }else{
                                yAngle += Math.toRadians(rotateAngle);
                            }
                             if(Math.toDegrees(Math.abs(yAngle)) >= 90) {
                                yAngle = 0;
                                y1 = 0;
                            }
                        }
                        if(z1 != 0){
                            if(opposite){
                                zAngle -= Math.toRadians(rotateAngle);
                            }else{
                                zAngle += Math.toRadians(rotateAngle);
                            }
                            if(Math.toDegrees(Math.abs(zAngle)) >= 90) {
                                zAngle = 0;
                                z1 = 0;
                                if(selectedCube[0] == LEFT) {
                                    this.zRotationCounterclockwise(3 - selectedCube[2]);
                                    //z1 = selectedCube[2] + 1;
                                }else if(selectedCube[0] == RIGHT) {
                                    this.zRotationClockwise(3 - selectedCube[2]);
                                    //z1 = selectedCube[2] + 1;
                                }
                            }
                        }
                    }
                }
            }
        }
        gl.glEnd(); // Done Drawing The Quad
        gl.glFlush();
    }
    private void drawBorder(GL2 gl, int i, int j, int k, int side){
        if(side == TOP){
            for (int l = 0; l < 4; l++) {
                p[0] = cube[i][j][k][side][l][0];
                p[1] = cube[i][j][k][side][l][1];
                p[2] = cube[i][j][k][side][l][2];
                p = zRotate(p,(float) Math.sin(yAngle),(float)Math.cos(yAngle));
                if(l == 0){
                    gl.glVertex3f( p[0]+offset,p[1]-0.01f,p[2]-offset );//topleft
                }else if(l == 1){
                    gl.glVertex3f( p[0]-offset,p[1]-0.01f,p[2]-offset );//topright
                }else if(l == 2){
                    gl.glVertex3f( p[0]-offset,p[1]-0.01f,p[2]+offset );//bottomleft
                }else{
                    gl.glVertex3f( p[0]+offset,p[1]-0.01f,p[2]+offset );//bottomright
                }
            }
        }else if(side == BOTTOM){
            for (int l = 0; l < 4; l++) {
                p[0] = cube[i][j][k][side][l][0];
                p[1] = cube[i][j][k][side][l][1];
                p[2] = cube[i][j][k][side][l][2];
                p = zRotate(p,(float) Math.sin(yAngle),(float)Math.cos(yAngle));
                if(l == 0){
                    gl.glVertex3f( p[0]+offset,p[1]+0.01f,p[2]+offset );//topleft
                }else if(l == 1){
                    gl.glVertex3f( p[0]-offset,p[1]+0.01f,p[2]+offset );//topright
                }else if(l == 2){
                    gl.glVertex3f( p[0]-offset,p[1]+0.01f,p[2]-offset );//bottomleft
                }else{
                    gl.glVertex3f( p[0]+offset,p[1]+0.01f,p[2]-offset );//bottomright
                }
            }
        }else if(side == FRONT){
            for (int l = 0; l < 4; l++) {
                p[0] = cube[i][j][k][side][l][0];
                p[1] = cube[i][j][k][side][l][1];
                p[2] = cube[i][j][k][side][l][2];
                p = zRotate(p,(float) Math.sin(yAngle),(float)Math.cos(yAngle));
                if(l == 0){
                    gl.glVertex3f( p[0]+offset,p[1]+offset,p[2]-0.01f );//topleft
                }else if(l == 1){
                    gl.glVertex3f( p[0]-offset,p[1]+offset,p[2]-0.01f );//topright
                }else if(l == 2){
                    gl.glVertex3f( p[0]-offset,p[1]-offset,p[2]-0.01f );//bottomleft
                }else{
                    gl.glVertex3f( p[0]+offset,p[1]-offset,p[2]-0.01f );//bottomright
                }
            }
        }else if(side == BACK){
            for (int l = 0; l < 4; l++) {
                p[0] = cube[i][j][k][side][l][0];
                p[1] = cube[i][j][k][side][l][1];
                p[2] = cube[i][j][k][side][l][2];
                p = zRotate(p,(float) Math.sin(yAngle),(float)Math.cos(yAngle));
                if(l == 0){
                    gl.glVertex3f( p[0]+offset,p[1]-offset,p[2]+0.01f );//topleft
                }else if(l == 1){
                    gl.glVertex3f( p[0]-offset,p[1]-offset,p[2]+0.01f );//topright
                }else if(l == 2){
                    gl.glVertex3f( p[0]-offset,p[1]+offset,p[2]+0.01f );//bottomleft
                }else{
                    gl.glVertex3f( p[0]+offset,p[1]+offset,p[2]+0.01f );//bottomright
                }
            }
        }else if(side == LEFT){
            for (int l = 0; l < 4; l++) {
                p[0] = cube[i][j][k][side][l][0];
                p[1] = cube[i][j][k][side][l][1];
                p[2] = cube[i][j][k][side][l][2];
                p = zRotate(p,(float) Math.sin(yAngle),(float)Math.cos(yAngle));
                if(l == 0){
                    gl.glVertex3f( p[0]+0.01f,p[1]+offset,p[2]+offset );//topleft
                }else if(l == 1){
                    gl.glVertex3f( p[0]+0.01f,p[1]+offset,p[2]-offset );//topright
                }else if(l == 2){
                    gl.glVertex3f( p[0]+0.01f,p[1]-offset,p[2]-offset );//bottomleft
                }else{
                    gl.glVertex3f( p[0]+0.01f,p[1]-offset,p[2]+offset );//bottomright
                }
            }
        }else if(side == RIGHT){
            for (int l = 0; l < 4; l++) {
                p[0] = cube[i][j][k][side][l][0];
                p[1] = cube[i][j][k][side][l][1];
                p[2] = cube[i][j][k][side][l][2];
                p = zRotate(p,(float) Math.sin(yAngle),(float)Math.cos(yAngle));
                if(l == 0){
                    gl.glVertex3f( p[0]-0.01f,p[1]+offset,p[2]-offset );//topleft
                }else if(l == 1){
                    gl.glVertex3f( p[0]-0.01f,p[1]+offset,p[2]+offset );//topright
                }else if(l == 2){
                    gl.glVertex3f( p[0]-0.01f,p[1]-offset,p[2]+offset );//bottomleft
                }else{
                    gl.glVertex3f( p[0]-0.01f,p[1]-offset,p[2]-offset );//bottomright
                }
            }
        }
        
    }
    private void smallCubeSection(GL2 gl, int i, int j, int k, int side) {
        for (int l = 0; l < 4; l++) {
            p[0] = cube[i][j][k][side][l][0];
            p[1] = cube[i][j][k][side][l][1];
            p[2] = cube[i][j][k][side][l][2];
            
            if(x1 == i + 1){
                p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
            }
            if(y1 == j + 1) {
                p = yRotate(p,(float) Math.sin(yAngle),(float)Math.cos(yAngle));
            }
            if(z1 == k + 1) {
                p = zRotate(p,(float) Math.sin(zAngle),(float)Math.cos(zAngle));
            }
            
            gl.glVertex3f( p[0],p[1],p[2] );
        }
    }
    
    private void glColor(GL2 gl, String color) {
        switch(color) {
            case WHITE:
                gl.glColor3f(ONE_F, ONE_F, ONE_F);
                break;
            case YELLOW:
                gl.glColor3f(ONE_F, ONE_F, ZERO_F);
                break;
            case RED:
                gl.glColor3f(ONE_F, ZERO_F, ZERO_F);
                break;
            case ORANGE:
                gl.glColor3f(ONE_F, ONE_F/2, ZERO_F);
                break;
            case GREEN:
                gl.glColor3f(ZERO_F, ONE_F, ZERO_F);
                break;
            case BLUE:
                gl.glColor3f(ZERO_F, ZERO_F, ONE_F);
                break;
            case PURPLE:
                gl.glColor3f(ONE_F, ZERO_F, ONE_F);
                break;
            case BLACK:
                gl.glColor3f(0f,0f,0f);
                break;
        }
    }
    
    private void glSelectedCubeColor(GL2 gl, String color) {
        switch(color) {
            case WHITE:
                gl.glColor4f(ONE_F, ONE_F, ONE_F, 0.8f);
                break;
            case YELLOW:
                gl.glColor4f(ONE_F, ONE_F, ZERO_F, ALPHA);
                break;
            case RED:
                gl.glColor4f(ONE_F, ZERO_F, ZERO_F, ALPHA);
                break;
            case ORANGE:
                gl.glColor4f(ONE_F, ONE_F/2, ZERO_F, ALPHA);
                break;
            case GREEN:
                gl.glColor4f(ZERO_F, ONE_F, ZERO_F, ALPHA);
                break;
            case BLUE:
                gl.glColor4f(ZERO_F, ZERO_F, ONE_F, ALPHA);
                break;
        }
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
        switch(e.getKeyCode()) {
            case KeyEvent.VK_A: // L'
                //rotating = true;
//                r1 = 1;
//                xAngle -= SECTION_ROTATE_ANGLE;
//                this.xRotationClockwise();
                if(selectedCube[0] == TOP){
                    if(selectedCube[1] == 0){
                        selectedCube[0] = LEFT;
                        selectedCube[1] = 2 - selectedCube[1];
                    }else{
                        selectedCube[1] -= 1;
                    }
                }else if(selectedCube[0] == FRONT){
                    if(selectedCube[1] == 0){
                        selectedCube[0] = LEFT;
                        selectedCube[1] = 2 - selectedCube[1];
                        int c = selectedCube[1];
                        selectedCube[1] = selectedCube[2];
                        selectedCube[2] = c;
                    }else{
                        selectedCube[1] -= 1;
                    }
                }else if(selectedCube[0] == LEFT){
                    if(selectedCube[2] == 0){
                        selectedCube[0] = BACK;
                        int c = selectedCube[1];
                        selectedCube[1] = selectedCube[2];
                        selectedCube[2] = c;
                    }else{
                        selectedCube[2] -= 1;
                    }
                }
                else if(selectedCube[0] == BACK){
                    if(selectedCube[1] == 2){
                        selectedCube[0] = RIGHT;
                        selectedCube[1] = 2-selectedCube[1];
                        int c = selectedCube[1];
                        selectedCube[1] = selectedCube[2];
                        selectedCube[2] = c;
                    }else{
                        selectedCube[1] += 1;
                    }
                }else if(selectedCube[0] == RIGHT){
                    if(selectedCube[2] == 2){
                        selectedCube[0] = FRONT;
                        int c = selectedCube[1];
                        selectedCube[1] = selectedCube[2];
                        selectedCube[2] = c;
                    }else{
                        selectedCube[2] += 1;
                    }
                }else if(selectedCube[0] == BOTTOM){
                    if(selectedCube[1] == 0){
                        selectedCube[0] = LEFT;
                        selectedCube[1] = selectedCube[1];
                    }else{
                        selectedCube[1] -= 1;
                    }
                }
                break;
            case KeyEvent.VK_W:
//                z1 = 3;
//                xAngle += SECTION_ROTATE_ANGLE;
//                this.zRotationCounterclockwise();
                  if(selectedCube[0] == TOP){
                    if(selectedCube[2] == 0){
                        selectedCube[0] = BACK;
                        selectedCube[2] = 2 - selectedCube[2];
                    }else{
                        selectedCube[2] -= 1;
                    }
                }else if(selectedCube[0] == FRONT){
                    if(selectedCube[2] == 2){
                        selectedCube[0] = TOP;
                    }else{
                        selectedCube[2] += 1;
                    }
                }else if(selectedCube[0] == LEFT){
                    if(selectedCube[1] == 2){
                        selectedCube[0] = TOP;
                        selectedCube[1] = 2 - selectedCube[1];
                    }else{
                        selectedCube[1] += 1;
                    }
                }
                else if(selectedCube[0] == BACK){
                    if(selectedCube[2] == 2){
                        selectedCube[0] = TOP;
                        selectedCube[2] = 2-selectedCube[2];
                    }else{
                        selectedCube[2] += 1;
                    }
                }else if(selectedCube[0] == RIGHT){
                    if(selectedCube[1] == 2){
                        selectedCube[0] = TOP;
                    }else{
                        selectedCube[1] += 1;
                    }
                }else if(selectedCube[0] == BOTTOM){
                    if(selectedCube[2] == 2){
                        selectedCube[0] = FRONT;
                        selectedCube[2] = 2-selectedCube[2];
                    }else{
                        selectedCube[2] += 1;
                    }
                }
                break;
            case KeyEvent.VK_S:
//                z1 = 3;
//                xAngle -= SECTION_ROTATE_ANGLE;
//                this.zRotationClockwise();
                if(selectedCube[0] == TOP){
                    if(selectedCube[2] == 2){
                        selectedCube[0] = FRONT;
                    }else{
                        selectedCube[2] += 1;
                    }
                }else if(selectedCube[0] == FRONT){
                    if(selectedCube[2] == 0){
                        selectedCube[0] = BOTTOM;
                        selectedCube[2] = 2 - selectedCube[2];
                    }else{
                        selectedCube[2] -= 1;
                    }
                }else if(selectedCube[0] == LEFT){
                    if(selectedCube[1] == 0){
                        selectedCube[0] = BOTTOM;
                    }else{
                        selectedCube[1] -= 1;
                    }
                }
                else if(selectedCube[0] == BACK){
                    if(selectedCube[2] == 0){
                        selectedCube[0] = BOTTOM;
                    }else{
                        selectedCube[2] -= 1;
                    }
                }else if(selectedCube[0] == RIGHT){
                    if(selectedCube[1] == 0){
                        selectedCube[0] = BOTTOM;
                        selectedCube[1] = 2-selectedCube[1];
                    }else{
                        selectedCube[1] -= 1;
                    }
                }else if(selectedCube[0] == BOTTOM){
                    if(selectedCube[2] == 0){
                        selectedCube[0] = BACK;
                    }else{
                        selectedCube[2] -= 1;
                    }
                }
                break;
//            case KeyEvent.VK_E: // R
//                r1 = 3;
//                xAngle += SECTION_ROTATE_ANGLE;
//                this.xRotationCounterclockwise();
//                break;
            case KeyEvent.VK_D: // R'
                if(selectedCube[0] == TOP){
                    if(selectedCube[1] == 2){
                        selectedCube[0] = RIGHT;
                    }else{
                        selectedCube[1] += 1;
                    }
                }else if(selectedCube[0] == FRONT){
                    if(selectedCube[1] == 2){
                        selectedCube[0] = RIGHT;
                        int c = selectedCube[1];
                        selectedCube[1] = selectedCube[2];
                        selectedCube[2] = c;
                    }else{
                        selectedCube[1] += 1;
                    }
                }else if(selectedCube[0] == LEFT){
                    if(selectedCube[2] == 2){
                        selectedCube[0] = FRONT;
                        selectedCube[2] = 2 - selectedCube[2];
                        int c = selectedCube[1];
                        selectedCube[1] = selectedCube[2];
                        selectedCube[2] = c;
                    }else{
                        selectedCube[2] += 1;
                    }
                }else if(selectedCube[0] == BACK){
                    if(selectedCube[1] == 0){
                        selectedCube[0] = LEFT;
                        int c = selectedCube[1];
                        selectedCube[1] = selectedCube[2];
                        selectedCube[2] = c;
                    }else{
                        selectedCube[1] -= 1;
                    }
                }else if(selectedCube[0] == RIGHT){
                    if(selectedCube[2] == 0){
                        selectedCube[0] = BACK;
                        selectedCube[2] = 2 - selectedCube[2];
                        int c = selectedCube[1];
                        selectedCube[1] = selectedCube[2];
                        selectedCube[2] = c;
                    }else{
                        selectedCube[2] -= 1;
                    }
                }else if(selectedCube[0] == BOTTOM){
                    if(selectedCube[1] == 2){
                        selectedCube[0] = RIGHT;
                        selectedCube[1] = 2 - selectedCube[1];
                    }else{
                        selectedCube[1] += 1;
                    }
                }
                //r1 = 3;
                //xAngle -= SECTION_ROTATE_ANGLE;
                //this.xRotationClockwise();
                
                break;
            case KeyEvent.VK_UP:
                if(selectedCube[0] == TOP) {
                    opposite = false;
                    x1 = selectedCube[1] + 1;
                    //this.xRotationCounterclockwise(selectedCube[1] + 1);
                }else if(selectedCube[0] == LEFT) {
                    //this.zRotationCounterclockwise(3 - selectedCube[2]);
                    opposite = false;
                    z1 = selectedCube[2] + 1;
                }else if(selectedCube[0] == RIGHT) {
                    //this.zRotationClockwise(3 - selectedCube[2]);
                    opposite = true;
                    z1 = selectedCube[2] + 1;
                }else if(selectedCube[0] == FRONT) {
                   //this.xRotationCounterclockwise(selectedCube[1] + 1);
                   opposite = false;
                    x1 = selectedCube[1] + 1;
                }else if(selectedCube[0] == BACK) {
                    //this.xRotationClockwise(selectedCube[1] + 1);
                    opposite = true;
                    x1 = selectedCube[1] + 1;
                }else if(selectedCube[0] == BOTTOM) {
                    //this.xRotationCounterclockwise(selectedCube[1] + 1);
                    opposite = true;
                    x1 = selectedCube[1] + 1;
                }
                
                break;
            case KeyEvent.VK_DOWN:
                if(selectedCube[0] == TOP) {
                    this.xRotationClockwise(selectedCube[1] + 1);
                }else if(selectedCube[0] == LEFT) {
                    this.zRotationClockwise(3 - selectedCube[2]);
                }else if(selectedCube[0] == RIGHT) {
                    this.zRotationCounterclockwise(3 - selectedCube[2]);
                }else if(selectedCube[0] == FRONT) {
                    this.xRotationClockwise(selectedCube[1] + 1);
                }else if(selectedCube[0] == BACK) {
                    this.xRotationCounterclockwise(selectedCube[1] + 1);
                }else if(selectedCube[0] == BOTTOM) {
                    this.xRotationClockwise(selectedCube[1] + 1);
                }
                break;
            case KeyEvent.VK_LEFT:
                if(selectedCube[0] == TOP) {
                    this.zRotationClockwise(3 - selectedCube[2]);
                }else if(selectedCube[0] == LEFT) {
                    this.yRotationClockwise(selectedCube[1] + 1);
                }else if(selectedCube[0] == RIGHT) {
                    this.yRotationClockwise(selectedCube[1] + 1);
                }else if(selectedCube[0] == FRONT) {
                    this.yRotationClockwise(selectedCube[2] + 1);
                }else if(selectedCube[0] == BACK) {
                    this.yRotationClockwise(selectedCube[2] + 1);
                }else if(selectedCube[0] == BOTTOM) {
                    this.zRotationCounterclockwise(3 - selectedCube[2]);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if(selectedCube[0] == TOP) {
                    this.zRotationCounterclockwise(3 - selectedCube[2]);
                }else if(selectedCube[0] == LEFT) {
                    this.yRotationCounterclockwise(selectedCube[1] + 1);
                }else if(selectedCube[0] == RIGHT) {
                    this.yRotationCounterclockwise(selectedCube[1] + 1);
                }else if(selectedCube[0] == FRONT) {
                    this.yRotationCounterclockwise(selectedCube[2] + 1);
                }else if(selectedCube[0] == BACK) {
                    this.yRotationCounterclockwise(selectedCube[2] + 1);
                }else if(selectedCube[0] == BOTTOM) {
                    this.zRotationClockwise(3 - selectedCube[2]);
                }
                break;
            case KeyEvent.VK_Z:
                this.scramble();
                break;
        }
    }
    
    public void scramble() {
        for(int i = 0; i < 20; i++) {
            java.util.Random rand = new java.util.Random();
            int num = rand.nextInt(rotateActions.length);
            int rotateNum = rand.nextInt(2);
            int rotateSide = rotateNum == 0 ? 3 : 1;
            rotateActions[num].rotate(rotateSide);
        }
    }
    
    private void xRotationCounterclockwise(int rx) {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[TOP][rx - 1][2-i];
            cubeColors[TOP][rx - 1][2-i] = cubeColors[FRONT][rx - 1][i];
            cubeColors[FRONT][rx - 1][i] = cubeColors[BOTTOM][rx - 1][i];
            cubeColors[BOTTOM][rx - 1][i] = cubeColors[BACK][rx - 1][2-i];
            cubeColors[BACK][rx - 1][2-i] = c;
        }
        if(rx == 1){
        rotateSideCounterClockwise(LEFT);
         }else if(rx == 3){
        rotateSideCounterClockwise(RIGHT);
         }
    }
    
    private void xRotationClockwise(int rx) {
         for (int i = 0; i < 3; i++) {
            String c = cubeColors[TOP][rx - 1][i];
            cubeColors[TOP][rx - 1][i] = cubeColors[BACK][rx - 1][i];
            cubeColors[BACK][rx - 1][i] = cubeColors[BOTTOM][rx - 1][2-i];
            cubeColors[BOTTOM][rx - 1][2-i] = cubeColors[FRONT][rx - 1][2-i];
            cubeColors[FRONT][rx - 1][2-i] = c;
        }
         if(rx == 1){
        rotateSideClockwise(LEFT);
         }else if(rx == 3){
        rotateSideClockwise(RIGHT);
         }
    }
    
    private void yRotationCounterclockwise(int ry) {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[FRONT][2 - i][ry - 1];
            cubeColors[FRONT][2 - i][ry - 1] = cubeColors[LEFT][ry - 1][2-i];
            cubeColors[LEFT][ry - 1][2-i] = cubeColors[BACK][i][ry - 1];
            cubeColors[BACK][i][ry - 1] = cubeColors[RIGHT][ry - 1][i];
            cubeColors[RIGHT][ry - 1][i] = c;
        }
        if(ry == 1){
            rotateSideCounterClockwise(BOTTOM);
        }else if(ry == 3){
            rotateSideCounterClockwise(TOP);
        }
    }
    
    private void yRotationClockwise(int ry) {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[FRONT][i][ry - 1];
            cubeColors[FRONT][i][ry - 1] = cubeColors[RIGHT][ry - 1][2-i];
            cubeColors[RIGHT][ry - 1][2-i] = cubeColors[BACK][2-i][ry - 1];
            cubeColors[BACK][2-i][ry - 1] = cubeColors[LEFT][ry - 1][i];
            cubeColors[LEFT][ry - 1][i] = c;
        }
        if(ry == 1){
            rotateSideClockwise(BOTTOM);
        }else if(ry == 3){
            rotateSideClockwise(TOP);
        }
    }
    
    private void zRotationClockwise(int rz) {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[LEFT][i][3 - rz];
            cubeColors[LEFT][i][3 - rz] = cubeColors[TOP][i][3 - rz];
            cubeColors[TOP][i][3 - rz] = cubeColors[RIGHT][2-i][3 - rz];
            cubeColors[RIGHT][2-i][3 - rz] = cubeColors[BOTTOM][2-i][3 - rz];
            cubeColors[BOTTOM][2-i][3 - rz] = c;
        }
        if(rz == 1){
            rotateSideClockwise(FRONT);
        }else if(rz == 3){
            rotateSideClockwise(BACK);
        }
    }
    
    private void zRotationCounterclockwise(int rz) {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[LEFT][2-i][3 - rz];
            cubeColors[LEFT][2-i][3 - rz] = cubeColors[BOTTOM][i][3 - rz];
            cubeColors[BOTTOM][i][3 - rz] = cubeColors[RIGHT][i][3 - rz];
            cubeColors[RIGHT][i][3 - rz] = cubeColors[TOP][2-i][3 - rz];
            cubeColors[TOP][2-i][3 - rz] = c;
        }
        if(rz == 1){
            rotateSideCounterClockwise(FRONT);
        }else if(rz == 3){
            rotateSideCounterClockwise(BACK);
        }
    }
    
    private void rotateSideClockwise(int side){
        String c = cubeColors[side][0][0];
        cubeColors[side][0][0] = cubeColors[side][0][2];
        cubeColors[side][0][2] = cubeColors[side][2][2];
        cubeColors[side][2][2] = cubeColors[side][2][0];
        cubeColors[side][2][0] = c;
        c = cubeColors[side][0][1];
        cubeColors[side][0][1] = cubeColors[side][1][2];
        cubeColors[side][1][2] = cubeColors[side][2][1];
        cubeColors[side][2][1] = cubeColors[side][1][0];
        cubeColors[side][1][0] = c;
    }
    
    private void rotateSideCounterClockwise(int side){
        String c = cubeColors[side][0][0];
        cubeColors[side][0][0] = cubeColors[side][2][0];
        cubeColors[side][2][0] = cubeColors[side][2][2];
        cubeColors[side][2][2] = cubeColors[side][0][2];
        cubeColors[side][0][2] = c;
        c = cubeColors[side][0][1];
        cubeColors[side][0][1] = cubeColors[side][1][0];
        cubeColors[side][1][0] = cubeColors[side][2][1];
        cubeColors[side][2][1] = cubeColors[side][1][2];
        cubeColors[side][1][2] = c;
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
