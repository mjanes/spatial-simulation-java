package setup;

import java.util.ArrayList;
import java.util.Collection;

import display.ThreeDimensionalEntityCanvas;
import entity.BasePhysicalEntity;

public class Setup {
	
	public static Collection<BasePhysicalEntity> create() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
		entities.addAll(basicOrbitCouple());
		entities.addAll(randomSet(3));
		
		//entities.addAll(grid(10));
		
		//entities.addAll(cube());
		
		//entities.addAll(point());
		
		return entities;
	}
	
	private static ArrayList<BasePhysicalEntity> basicOrbitCouple() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
		
		BasePhysicalEntity entityA = new BasePhysicalEntity(-200, -200, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 500);
		entityA.setDeltaX(1.0);
		entities.add(entityA);				
		
		entities.add(new BasePhysicalEntity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 8000));
		
		return entities;
	}
	
	private static ArrayList<BasePhysicalEntity> randomSet(int n) {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
		
		BasePhysicalEntity newEntity;
		for (int i = 0; i < n; ++i) {
			newEntity = new BasePhysicalEntity((Math.random() * 800) - 400, (Math.random() * 800) - 400, ThreeDimensionalEntityCanvas.EYE_DISTANCE, Math.random() * 400);
			newEntity.setDeltaX(Math.random() / 10);
			newEntity.setDeltaY(Math.random() / 10);
			entities.add(newEntity);
		}
		
		return entities;
	}
	
	
	private static ArrayList<BasePhysicalEntity> grid(int x) {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				entities.add(new BasePhysicalEntity(i * 100, j * 100, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 200));
			}
		}

		return entities;
	}
	
	private static ArrayList<BasePhysicalEntity> point() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
		entities.add(new BasePhysicalEntity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 1000));
		return entities;
	}
	
	private static ArrayList<BasePhysicalEntity> cube() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<BasePhysicalEntity>();
		
		BasePhysicalEntity a = new BasePhysicalEntity(-300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 200);		
		BasePhysicalEntity b = new BasePhysicalEntity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 200);
		BasePhysicalEntity c = new BasePhysicalEntity(-300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 200); 
		BasePhysicalEntity d = new BasePhysicalEntity(300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 200); 		
		BasePhysicalEntity e = new BasePhysicalEntity(-300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 200); 
		BasePhysicalEntity f = new BasePhysicalEntity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 200); 
		BasePhysicalEntity g = new BasePhysicalEntity(-300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 200); 
		BasePhysicalEntity h = new BasePhysicalEntity(300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 200);
		
		// TODO: Had been planning to connect certain points in order to make a cube,
		// but realized the graphics are going to require a reworking.
		//a.addConnection(b);
		//a.addConnection(c);
		//a.addConnection(e);
		
		entities.add(a);
		entities.add(b);
		entities.add(c);
		entities.add(d);
		entities.add(e);
		entities.add(f);
		entities.add(g);
		entities.add(h);
		
		return entities;
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
