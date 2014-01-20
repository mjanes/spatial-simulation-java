package display;

import java.util.ArrayList;
import java.util.Collection;

import entity.BasePhysicalEntity;

import java.awt.*;

import javax.swing.*;

/**
 * Initial display class. Just going to be a rectangle drawing circles that can orbit around eachother.
 * 
 * Questions:
 * 1) What sort of interface should be built? Are there qualities of a display class that will be shared between a 2d and 3d display?
 * 2) Should I make it so a 2d display can handle 3d entities? Hrm... Well, that might largely depend on the physics engine, so I can ignore it 
 * for now.
 * 
 * Basically, all this class will do, for the time being, is take an array of entities of some sort, get their x-y coordinates and get their display
 * value, and then paint them. Now... I'm not sure how I want the display value to be done. By the objects themselves, or am I going to have the
 * objects just spit out a radius and color? Hrm...
 * 
 * And... yeah, guess this is using swing. It's been years. I should really do this in javascript or something insttead.
 * 
 * @author mjanes
 */
public class TwoDimensionalEntityDisplayFrame extends JFrame {
		
	private static final long serialVersionUID = 1L;
	
	ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
	
	public TwoDimensionalEntityDisplayFrame(int width, int height) {
		setSize(width, height);
		setVisible(true);
	}
	
	public void setEntities(Collection<BasePhysicalEntity> entities) {
		this.entities = new ArrayList<BasePhysicalEntity>(entities);
	}
	
	@Override
	public void paint(Graphics g) {
		// Paint background. And yes, I'd prefer something like a super.paint() but having issues with that at the moment.
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Paint entities
		g.setColor(Color.BLACK);				
		double radius;
		for (BasePhysicalEntity entity : entities) {
			radius = entity.getRadius();
			g.fillOval((int) entity.getX(), (int) entity.getY(), (int) radius, (int) radius);
		}		
	}
}
