package display;

import java.util.Collection;

import javax.swing.JFrame;

import entity.BasePhysicalEntity;

public class UIFrame extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	
	private TwoDimensionalEntityDisplayPanel displayPanel;
	
	public UIFrame(int width, int height) {
		setSize(width, height);
		setVisible(true);
		displayPanel = new TwoDimensionalEntityDisplayPanel(width, height);
		this.setContentPane(displayPanel);
	}
	
	public void setEntities(Collection<BasePhysicalEntity> entities) {
		displayPanel.setEntities(entities);
	}
}
