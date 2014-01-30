package main;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;

import physics.GravitationalPhysics;
import setup.Setup;
import display.UIFrame;
import entity.BasePhysicalEntity;

public class Launcher {
	
	protected static UIFrame frame;
	protected static Collection<BasePhysicalEntity> entities;
	protected static UniverseController controller;

	public static void main(String[] args) {
		// Hrm... dependency injection? 
		// But rough idea:
		// 	1) Launch UI
		frame = new UIFrame(1000, 1000);
		
		//	2) Initialize objects
		entities = Setup.create();
		frame.setEntities(entities);
		//frame.repaint();
		
		//	3) Begin running physics on things
		controller = new UniverseController();
		Timer t = new Timer();
		t.schedule(controller,  0, 10);
	}

	
	private static class UniverseController extends TimerTask {

		@Override
		public void run() {
			// tell graphics to repaint
			frame.repaint();			
			
			// run round of physics
			GravitationalPhysics.gravity(entities);
			for (BasePhysicalEntity entity : entities) {
				entity.move();
			}
		}
		
	}
}
