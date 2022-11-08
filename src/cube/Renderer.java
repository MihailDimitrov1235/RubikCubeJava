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
    private int r1 = 0;
    private int c1 = 0;
    private int z1 = 0;
    private int c2 = 0;
    private float p[] = {0,0,0};
    
    private int mouseX = CANVAS_WIDTH/2;
    private int mouseY = CANVAS_HEIGHT/2;
    
    private final String WHITE = "WHITE";
    private final String YELLOW = "YELLOW";
    private final String RED = "RED";
    private final String ORANGE = "ORANGE";
    private final String GREEN = "GREEN";
    private final String BLUE = "BLUE";
    private final String BLACK = "BLACK";
   
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
        
        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    for (int side = 0; side < 6; side++) {
                        
                        
                        if(side==0 && y == 2){ //TOP
                            this.glColor(gl, cubeColors[side][x][z]);
                        }else if(side == 1 && y == 0){ //BOTTOM
                            this.glColor(gl, cubeColors[side][x][z]);
                        }else if(side == 2 && z == 2){ //FRONT
                            this.glColor(gl, cubeColors[side][x][y]);
                        }else if(side == 3 && z == 0){ //BACK
                            this.glColor(gl, cubeColors[side][x][y]);
                        }else if(side == 4 && x == 0){ //LEFT
                            this.glColor(gl, cubeColors[side][y][z]);
                        }else if(side == 5 && x == 2){ //RIGHT
                            this.glColor(gl, cubeColors[side][y][z]);
                        }else{
                            this.glColor(gl, BLACK);
                        }
                        this.smallCubeSection(gl, x, y, z, side);
                    }
                }
            }
        }
        gl.glEnd(); // Done Drawing The Quad
        gl.glFlush();
    }
    
    private void smallCubeSection(GL2 gl, int i, int j, int k, int side) {
        for (int l = 0; l < 4; l++) {
            p[0] = cube[i][j][k][side][l][0];
            p[1] = cube[i][j][k][side][l][1];
            p[2] = cube[i][j][k][side][l][2];
//            
//            if(r1 == i + 1){
//                //p = xRotate(p,(float) Math.sin(xAngle),(float)Math.cos(xAngle));
//            }
//            if(c1 == j + 1) {
//                //p = yRotate(p,(float) Math.sin(yAngle),(float)Math.cos(yAngle));
//            }
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
            case BLACK:
                gl.glColor3f(0f,0f,0f);
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
            case KeyEvent.VK_Q: // L
                r1 = 1;
                xAngle += SECTION_ROTATE_ANGLE;
                this.xRotationCounterclockwise();
                break;
            case KeyEvent.VK_A: // L'
                r1 = 1;
                xAngle -= SECTION_ROTATE_ANGLE;
                this.xRotationClockwise();
                break;
            case KeyEvent.VK_W:
                z1 = 3;
                xAngle += SECTION_ROTATE_ANGLE;
                this.zRotationCounterclockwise();
                break;
            case KeyEvent.VK_S:
                z1 = 3;
                xAngle -= SECTION_ROTATE_ANGLE;
                this.zRotationClockwise();
                break;
            case KeyEvent.VK_E: // R
                r1 = 3;
                xAngle += SECTION_ROTATE_ANGLE;
                this.xRotationCounterclockwise();
                break;
            case KeyEvent.VK_D: // R'
                r1 = 3;
                xAngle -= SECTION_ROTATE_ANGLE;
                this.xRotationClockwise();
                break;
            case KeyEvent.VK_R:
                c1 = 1;
                yAngle += SECTION_ROTATE_ANGLE;
                this.yRotationClockwise();
                break;
            case KeyEvent.VK_F:
                c1 = 1;
                yAngle -= SECTION_ROTATE_ANGLE;
                this.yRotationCounterclockwise();
                break;
            case KeyEvent.VK_T:
                c1 = 3;
                yAngle += SECTION_ROTATE_ANGLE;
                this.yRotationClockwise();
                break;
            case KeyEvent.VK_G:
                c1 = 3;
                yAngle -= SECTION_ROTATE_ANGLE;
                this.yRotationCounterclockwise();
                break;
        }
    }
    
    private void xRotationCounterclockwise() {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[TOP][r1 - 1][2-i];
            cubeColors[TOP][r1 - 1][2-i] = cubeColors[FRONT][r1 - 1][i];
            cubeColors[FRONT][r1 - 1][i] = cubeColors[BOTTOM][r1 - 1][i];
            cubeColors[BOTTOM][r1 - 1][i] = cubeColors[BACK][r1 - 1][2-i];
            cubeColors[BACK][r1 - 1][2-i] = c;
        }
        if(r1 == 1){
        rotateSideCounterClockwise(LEFT);
         }else if(r1 == 3){
        rotateSideCounterClockwise(RIGHT);
         }
    }
    
    private void xRotationClockwise() {
         for (int i = 0; i < 3; i++) {
            String c = cubeColors[TOP][r1 - 1][i];
            cubeColors[TOP][r1 - 1][i] = cubeColors[BACK][r1 - 1][i];
            cubeColors[BACK][r1 - 1][i] = cubeColors[BOTTOM][r1 - 1][2-i];
            cubeColors[BOTTOM][r1 - 1][2-i] = cubeColors[FRONT][r1 - 1][2-i];
            cubeColors[FRONT][r1 - 1][2-i] = c;
        }
         if(r1 == 1){
        rotateSideClockwise(LEFT);
         }else if(r1 == 3){
        rotateSideClockwise(RIGHT);
         }
    }
    
    private void yRotationCounterclockwise() {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[FRONT][2 - i][c1 - 1];
            cubeColors[FRONT][2 - i][c1 - 1] = cubeColors[LEFT][c1 - 1][2-i];
            cubeColors[LEFT][c1 - 1][2-i] = cubeColors[BACK][i][c1 - 1];
            cubeColors[BACK][i][c1 - 1] = cubeColors[RIGHT][c1 - 1][i];
            cubeColors[RIGHT][c1 - 1][i] = c;
        }
        if(c1 == 1){
            rotateSideCounterClockwise(BOTTOM);
        }else if(c1 == 3){
            rotateSideCounterClockwise(TOP);
        }
    }
    
    private void yRotationClockwise() {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[FRONT][i][c1 - 1];
            cubeColors[FRONT][i][c1 - 1] = cubeColors[RIGHT][c1 - 1][2-i];
            cubeColors[RIGHT][c1 - 1][2-i] = cubeColors[BACK][2-i][c1 - 1];
            cubeColors[BACK][2-i][c1 - 1] = cubeColors[LEFT][c1 - 1][i];
            cubeColors[LEFT][c1 - 1][i] = c;
        }
        if(c1 == 1){
            rotateSideClockwise(BOTTOM);
        }else if(c1 == 3){
            rotateSideClockwise(TOP);
        }
    }
    
    private void zRotationClockwise() {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[LEFT][i][3 - z1];
            cubeColors[LEFT][i][3 - z1] = cubeColors[TOP][i][3 - z1];
            cubeColors[TOP][i][3 - z1] = cubeColors[RIGHT][2-i][3 - z1];
            cubeColors[RIGHT][2-i][3 - z1] = cubeColors[BOTTOM][2-i][3 - z1];
            cubeColors[BOTTOM][2-i][3 - z1] = c;
        }
        if(z1 == 1){
            rotateSideClockwise(FRONT);
        }else if(z1 == 3){
            rotateSideClockwise(BACK);
        }
    }
    
    private void zRotationCounterclockwise() {
        for (int i = 0; i < 3; i++) {
            String c = cubeColors[LEFT][2-i][3 - z1];
            cubeColors[LEFT][2-i][3 - z1] = cubeColors[BOTTOM][i][3 - z1];
            cubeColors[BOTTOM][i][3 - z1] = cubeColors[RIGHT][i][3 - z1];
            cubeColors[RIGHT][i][3 - z1] = cubeColors[TOP][2-i][3 - z1];
            cubeColors[TOP][2-i][3 - z1] = c;
        }
        if(z1 == 1){
            rotateSideCounterClockwise(FRONT);
        }else if(z1 == 3){
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
