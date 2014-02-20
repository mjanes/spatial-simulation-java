package main;

import java.util.Collection;

import physics.GravitationalPhysics;
import setup.Setup;
import display.UIFrame;
import entity.BasePhysicalEntity;

public class Main {
	
	protected static UIFrame frame;
	
	

	public static void main(String[] args) {
		// Rough idea:
		// 	1) Launch UI
		frame = new UIFrame(1000, 1000);
		
		//	2) Initialize objects
		Collection<BasePhysicalEntity> entities = Setup.create();
		frame.setEntities(entities);
		
		// TODO: This is temporary, will be controlling this from within the frame
		frame.start();
	}



}
