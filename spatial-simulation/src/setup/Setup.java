package setup;

import display.ThreeDimensionalEntityCanvas;
import entity.Entity;
import physics.GravitationalPhysics;

import java.util.ArrayList;
import java.util.List;

public class Setup {
	
	public static List<Entity> create() {
		ArrayList<Entity> entities = new ArrayList<>();
		//entities.addAll(basicOrbitCouple());

		entities.addAll(randomRotatingSetWithCenter(750));
		
		//entities.addAll(grid(10));
		//entities.addAll(cube());
		//entities.addAll(point());
		
		return entities;
	}
	
	private static ArrayList<Entity> basicOrbitCouple() {
		ArrayList<Entity> entities = new ArrayList<>();

        Entity entityLarger = new Entity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 10000);
		entityLarger.applyForceX(-4000);
        entities.add(entityLarger);

        Entity entitySmaller = new Entity(-200, -200, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 500);
        entitySmaller.applyForceX(4000);
        entities.add(entitySmaller);

        return entities;
	}
	
	private static ArrayList<Entity> randomRotatingSetWithCenter(int n) {
		ArrayList<Entity> entities = new ArrayList<>();

        Entity center = new Entity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 1000000);
        entities.add(center);

        final double rotationConstant = 1.0/200;
		final double rotationFactor = GravitationalPhysics.GRAVITATIONAL_CONSTANT * Math.sqrt(center.getMass()) * rotationConstant;

		Entity newEntity;
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

			newEntity = new Entity(x, y, z, mass);
			newEntity.applyForceX(forceX);
			newEntity.applyForceY(forceY);
			entities.add(newEntity);
		}
		
		return entities;
	}
	
	
	private static ArrayList<Entity> grid(int x) {
		ArrayList<Entity> entities = new ArrayList<>();
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				entities.add(new Entity((i - x / 2) * 100, (j - x / 2) * 100, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 200));
			}
		}

		return entities;
	}
	
	private static ArrayList<Entity> point() {
		ArrayList<Entity> entities = new ArrayList<>();
		entities.add(new Entity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 1000));
		return entities;
	}
	
	private static ArrayList<Entity> cube() {
		ArrayList<Entity> entities = new ArrayList<>();
		
		Entity a = new Entity(-300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
		Entity b = new Entity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
		Entity c = new Entity(-300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
		Entity d = new Entity(300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
		Entity e = new Entity(-300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);
		Entity f = new Entity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);
		Entity g = new Entity(-300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);
		Entity h = new Entity(300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);
		
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
