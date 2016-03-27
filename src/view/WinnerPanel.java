package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JPanel;

import controller.Controller;


public class WinnerPanel extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public WinnerPanel() {
        this.setOpaque(false);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Shape shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(new Color(5, 53, 58, 180));
        g2d.fill(shape);
        g2d.dispose();
    }
    
    public void registerListeners(Controller controller) {
    	this.addMouseListener(controller);
    }

}