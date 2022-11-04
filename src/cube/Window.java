/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cube;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 *
 * @author NEXT
 */
public class Window extends JFrame {
    
    JPanel mainPanel;
    
    public Window(String title, int width, int height) {
        super(title);
        setupWindow(width, height);
        mainPanel = new JPanel(new BorderLayout());
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupWindow(int width, int height) {
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(width, height));
        this.setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e){
                System.out.println("Closing window...");
                System.exit(0);
            }
        });
    }
    
    public void setVisibility(boolean isVisible) {
        this.setVisible(isVisible);
    }
}
