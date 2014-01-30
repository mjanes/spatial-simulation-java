package display;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.Collection;

import javax.swing.JFrame;

import entity.BasePhysicalEntity;

public class UIFrame extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	
	private TwoDimensionalEntityDisplayPanel displayPanel;
	
	private BufferStrategy strategy;
	
	
	public UIFrame(int width, int height) {
		setSize(width, height);		
		displayPanel = new TwoDimensionalEntityDisplayPanel(width, height);
		setContentPane(displayPanel);
		setIgnoreRepaint(true);
		pack();
		setVisible(true);		
		

		// TODO: I would prefer to put this in the TwoDimensionalEntityDisplayPanel, but I am still learning how this all works.
		createBufferStrategy(2);
		strategy = getBufferStrategy();
	}
	
	public void setEntities(Collection<BasePhysicalEntity> entities) {
		displayPanel.setEntities(entities);
	}
	
	public void incrementGraphics() {
		Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
		displayPanel.paint(g);
		
		g.dispose();
		strategy.show();
	}
}
