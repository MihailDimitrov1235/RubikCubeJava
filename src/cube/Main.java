/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cube;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

/**
 *
 * @author NEXT
 */
public class Main {
    public static void main(String[] args) {
        final GLProfile gp = GLProfile.get(GLProfile.GL2);    
        GLCapabilities cap = new GLCapabilities(gp);
        
        final GLCanvas gc = new GLCanvas(cap);    
        Renderer sq = new Renderer();    
        gc.addGLEventListener(sq);    
        gc.setSize(400, 400);
        
        Window w = new Window("Test", 640, 480);    
        w.add(gc);    
 
        w.setVisibility(true);
        final FPSAnimator animator = new FPSAnimator(gc, 300, true);
		
        animator.start();
    }
}
