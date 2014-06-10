package setup;

import entity.Entity;
import physics.GravitationalPhysics;

import java.util.ArrayList;
import java.util.List;

public class Setup {
	
	public static List<Entity> create() {
		ArrayList<Entity> entities = new ArrayList<>();
		//entities.addAll(basicOrbitCouple());

		entities.addAll(setup(2000, 3000, 1500, 1, 500));
		
		//entities.addAll(grid(10));
		//entities.addAll(cube());
		//entities.addAll(point());
		
		return entities;
	}
	
//	private static ArrayList<Entity> basicOrbitCouple() {
//		ArrayList<Entity> entities = new ArrayList<>();
//
//        Entity entityLarger = new Entity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 10000);
//		entityLarger.applyForceX(-4000);
//        entities.add(entityLarger);
//
//        Entity entitySmaller = new Entity(-200, -200, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 500);
//        entitySmaller.applyForceX(4000);
//        entities.add(entitySmaller);
//
//        return entities;
//	}
//
//
//	private static ArrayList<Entity> grid(int x) {
//		ArrayList<Entity> entities = new ArrayList<>();
//
//		for (int i = 0; i < x; i++) {
//			for (int j = 0; j < x; j++) {
//				entities.add(new Entity((i - x / 2) * 100, (j - x / 2) * 100, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 200));
//			}
//		}
//
//		return entities;
//	}
//
//	private static ArrayList<Entity> point() {
//		ArrayList<Entity> entities = new ArrayList<>();
//		entities.add(new Entity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 1000));
//		return entities;
//	}
//
//	private static ArrayList<Entity> cube() {
//		ArrayList<Entity> entities = new ArrayList<>();
//
//		Entity a = new Entity(-300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
//		Entity b = new Entity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
//		Entity c = new Entity(-300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
//		Entity d = new Entity(300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 1000, 500);
//		Entity e = new Entity(-300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);
//		Entity f = new Entity(300, -300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);
//		Entity g = new Entity(-300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);
//		Entity h = new Entity(300, 300, ThreeDimensionalEntityCanvas.EYE_DISTANCE + 2000, 500);
//
//		entities.add(a);
//		entities.add(b);
//		entities.add(c);
//		entities.add(d);
//		entities.add(e);
//		entities.add(f);
//		entities.add(g);
//		entities.add(h);
//
//		return entities;
//	}

    /**
     *
     * @param numEntities           Number of entities + one central entity, that will be created
     * @param massDistribution      Mass of each entity, other than center, will be between 0 and massDistribution
     * @param radius                Radius of the circle the entities will be uniformly distributed in.
     * @param speedDistribution     A larger number makes the entities go faster, smaller goes slower. 1 is balanced to
     *                              roughly make an entity with speed one half of speedDistribution item orbit the
     *                              center mass according to Kepler's laws of planetary motion. Admittedly that math is
     *                              fuzzy.
     * @param xyTozRatio            Entities will be given a random z value between 0 and radius / xyTozRatio
     *
     * @return All entities
     */
	public static List<Entity> setup(int numEntities, double massDistribution, double radius, double speedDistribution, double xyTozRatio) {
        ArrayList<Entity> entities = new ArrayList<>();

        final double Z_DISTANCE = 25000; // distance the center object is from the user

        Entity center = new Entity(0, 0, Z_DISTANCE, 1000000);
        entities.add(center);

        double zDistribution = radius / xyTozRatio;

        // Trying to handle rotation speeds
        // http://www.astronomynotes.com/gravappl/s8.htm
        // https://en.wikipedia.org/wiki/Kepler's_laws_of_planetary_motion
        // v = Sqrt[(G M)/r].

        final double rotationFactor;
        final double modifiedGravitationalConstant = GravitationalPhysics.GRAVITATIONAL_CONSTANT / 3000;
        rotationFactor = speedDistribution * modifiedGravitationalConstant * (center.getMass() + (massDistribution * numEntities / 2));

        for (int i = 0; i < numEntities; ++i) {
            double theta = Math.random() * 2 * Math.PI;
            double r = radius * Math.sqrt(Math.random());
            double x = center.getX() + r * Math.cos(theta);
            double y = center.getY() + r * Math.sin(theta);
            double z = Z_DISTANCE + (Math.random() * zDistribution) - zDistribution / 2;
            double mass = Math.random() * massDistribution;
            Entity newEntity = new Entity(x, y, z, mass);

            double forceX = -y * mass * Math.random() * rotationFactor / center.getDistance(newEntity);
            double forceY = x * mass * Math.random() * rotationFactor / center.getDistance(newEntity);
            double forceZ = Math.random() * zDistribution / 10;

            newEntity.applyForceX(forceX);
            newEntity.applyForceY(forceY);
            newEntity.applyForceZ(forceZ);
            entities.add(newEntity);
        }

        return entities;
	}

}
