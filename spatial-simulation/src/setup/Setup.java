package setup;

import java.util.ArrayList;
import java.util.List;

import display.ThreeDimensionalEntityCanvas;
import entity.BasePhysicalEntity;
import physics.GravitationalPhysics;

public class Setup {
	
	public static List<BasePhysicalEntity> create() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<>();
		//entities.addAll(basicOrbitCouple());

		entities.addAll(randomRotatingSetWithCenter(350));
		
		//entities.addAll(grid(10));
		//entities.addAll(cube());
		//entities.addAll(point());
		
		return entities;
	}
	
	private static ArrayList<BasePhysicalEntity> basicOrbitCouple() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<>();

        BasePhysicalEntity entityLarger = new BasePhysicalEntity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 10000);
		entityLarger.applyForceX(-4000);
        entities.add(entityLarger);

        BasePhysicalEntity entitySmaller = new BasePhysicalEntity(-200, -200, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 500);
        entitySmaller.applyForceX(4000);
        entities.add(entitySmaller);

        return entities;
	}
	
	private static ArrayList<BasePhysicalEntity> randomRotatingSetWithCenter(int n) {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<>();

        BasePhysicalEntity center = new BasePhysicalEntity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 1000000);
        entities.add(center);

        final double rotationConstant = 1.0/200;
		final double rotationFactor = GravitationalPhysics.GRAVITATIONAL_CONSTANT * Math.sqrt(center.getMass()) * rotationConstant;

		BasePhysicalEntity newEntity;
        double mass;
        double x;
        double y;
        double z;
        double forceX;
        double forceY;
		for (int i = 0; i < n; ++i) {
            x = (Math.random() * 800) - 400;
            y = (Math.random() * 800) - 400;
            z = ThreeDimensionalEntityCanvas.EYE_DISTANCE + (Math.random() * 10) - 5;
            mass = Math.random() * 1000;
            forceX = -y * mass * Math.random() * rotationFactor;
            forceY = x * mass * Math.random() * rotationFactor;

			newEntity = new BasePhysicalEntity(x, y, z, mass);
			newEntity.applyForceX(forceX);
			newEntity.applyForceY(forceY);
			entities.add(newEntity);
		}
		
		return entities;
	}
	
	
	private static ArrayList<BasePhysicalEntity> grid(int x) {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<>();
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				entities.add(new BasePhysicalEntity((i - x / 2) * 100, (j - x / 2) * 100, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 200));
			}
		}

		return entities;
	}
	
	private static ArrayList<BasePhysicalEntity> point() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<>();
		entities.add(new BasePhysicalEntity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 1000));
		return entities;
	}
	
	private static ArrayList<BasePhysicalEntity> cube() {
		ArrayList<BasePhysicalEntity> entities = new ArrayList<>();
		
		BasePhysicalEntity a = new BasePhysicalEntity(-300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);		
		BasePhysicalEntity b = new BasePhysicalEntity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
		BasePhysicalEntity c = new BasePhysicalEntity(-300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500); 
		BasePhysicalEntity d = new BasePhysicalEntity(300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500); 		
		BasePhysicalEntity e = new BasePhysicalEntity(-300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500); 
		BasePhysicalEntity f = new BasePhysicalEntity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500); 
		BasePhysicalEntity g = new BasePhysicalEntity(-300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500); 
		BasePhysicalEntity h = new BasePhysicalEntity(300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);		
		
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
	 * Experiment with creating a parameterized function for setting things up.
	 * 
	 * Will create entities, with some sort of spiral motion. Beyond that... probably a lot of variables I haven't
	 * thought of yet.
	 *
	 */
//	public static ArrayList<BasePhysicalEntity> setup(int numEntities, double massDistribution, double spatialDistribution, double speedDistribution) {
//		return null;
//	}

}
