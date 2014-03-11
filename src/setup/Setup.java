package setup;

import java.util.ArrayList;
import java.util.Collection;

import entity.BasePhysicalEntity;

public class Setup {
	
	private static ArrayList<BasePhysicalEntity> entities;

	public static Collection<BasePhysicalEntity> create() {
		entities = new ArrayList<BasePhysicalEntity>();
		basicOrbitCouple();
		randomSet(3);
		
		//randomSet(10);
		
		return entities;
	}
	
	private static void basicOrbitCouple() {
		BasePhysicalEntity entityA = new BasePhysicalEntity(200, 200, 0, 500);
		entityA.setDeltaX(1.0);
		entities.add(entityA);				
		entities.add(new BasePhysicalEntity(500, 500, 0, 8000));
	}
	
	private static void randomSet(int n) {
		BasePhysicalEntity newEntity;
		for (int i = 0; i < n; ++i) {
			newEntity = new BasePhysicalEntity(Math.random() * 800, Math.random() * 800, 0, Math.random() * 400);
			newEntity.setDeltaX(Math.random() / 10);
			newEntity.setDeltaY(Math.random() / 10);
			entities.add(newEntity);
		}
	}
	
	
	/**
	 * Experiment with creating a parametized function for setting things up.
	 * 
	 * Will create entities, with some sort of spiral motion. Beyond that... probably a lot of variables I haven't
	 * thought of yet.
	 * 
	 * @param numEntities
	 * @param massDistribution
	 * @param spatial
	 */
	public static ArrayList<BasePhysicalEntity> setup(int numEntities, double massDistribution, double spatialDistribution, double speedDistribution) {
		return null;
	}
	
}
