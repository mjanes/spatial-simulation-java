package spatial.setup;

import spatial.entity.SpatialEntity;
import spatial.physics.GravitationalPhysics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Setup {

    private static double sZDistance = 3000; // default distance from the user

	public static List<SpatialEntity> create() {
		ArrayList<SpatialEntity> entities = new ArrayList<>();
		//entities.addAll(basicOrbitCouple());

		entities.addAll(setup(2000, 3000, 1500, 1, 500));
		
		//entities.addAll(grid(10));
		//entities.addAll(cube());
		//entities.addAll(point());
		
		return entities;
	}
	
//	private static ArrayList<entity.Entity> basicOrbitCouple() {
//		ArrayList<entity.Entity> entities = new ArrayList<>();
//
//        entity.Entity entityLarger = new entity.Entity(0, 0, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 10000);
//		entityLarger.applyForceX(-4000);
//        entities.add(entityLarger);
//
//        entity.Entity entitySmaller = new entity.Entity(-200, -200, ThreeDimensionalEntityCanvas.EYE_DISTANCE, 500);
//        entitySmaller.applyForceX(4000);
//        entities.add(entitySmaller);
//
//        return entities;
//	}


	private static ArrayList<SpatialEntity> grid(int x) {
        sZDistance = 5000;

		ArrayList<SpatialEntity> entities = new ArrayList<>();

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < x; j++) {
				entities.add(new SpatialEntity((i - x / 2) * 100, (j - x / 2) * 100, sZDistance, 200));
			}
		}

		return entities;
	}

	private static ArrayList<SpatialEntity> point() {
		ArrayList<SpatialEntity> entities = new ArrayList<>();
		entities.add(new SpatialEntity(0, 0, sZDistance, 1000));
		return entities;
	}

	private static List<SpatialEntity> cube() {
        List<SpatialEntity> entities = new ArrayList<>();

        SpatialEntity a = new SpatialEntity(-300, -300, sZDistance + 1000, 5000);
		SpatialEntity b = new SpatialEntity(300, -300, sZDistance + 1000, 5000);
		SpatialEntity c = new SpatialEntity(-300, 300, sZDistance + 1000, 5000);
		SpatialEntity d = new SpatialEntity(300, 300, sZDistance + 1000, 5000);
		SpatialEntity e = new SpatialEntity(-300, -300, sZDistance + 2000, 5000);
		SpatialEntity f = new SpatialEntity(300, -300, sZDistance + 2000, 5000);
		SpatialEntity g = new SpatialEntity(-300, 300, sZDistance + 2000, 5000);
		SpatialEntity h = new SpatialEntity(300, 300, sZDistance + 2000, 5000);

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
	public static List<SpatialEntity> setup(int numEntities, double massDistribution, double radius, double speedDistribution, double xyTozRatio) {
        List<SpatialEntity> entities = new ArrayList<>();


        sZDistance = 25000; // distance the center object is from the user

        SpatialEntity center = new SpatialEntity(0, 0, sZDistance, 1000000);
        entities.add(center);

        double zDistribution = radius / xyTozRatio;

        // Trying to handle rotation speeds
        // http://www.astronomynotes.com/gravappl/s8.htm
        // https://en.wikipedia.org/wiki/Kepler's_laws_of_planetary_motion
        // v = Sqrt[(G M)/r].

        final double rotationFactor;
        final double modifiedGravitationalConstant = GravitationalPhysics.GRAVITATIONAL_CONSTANT / 3500;
        rotationFactor = speedDistribution * modifiedGravitationalConstant * (center.getMass() + (massDistribution * numEntities / 2));

        long time = System.currentTimeMillis();

        IntStream.range(0, numEntities).
                forEach(i -> {

                    double theta = Math.random() * 2 * Math.PI;
                    double r = radius * Math.sqrt(Math.random());
                    double x = center.getX() + r * Math.cos(theta);
                    double y = center.getY() + r * Math.sin(theta);
                    double z = sZDistance + (Math.random() * zDistribution) - zDistribution / 2;
                    double mass = Math.random() * massDistribution;
                    SpatialEntity newSpatialEntity = new SpatialEntity(x, y, z, mass);

                    double forceX = -y * mass * Math.random() * rotationFactor / center.getDistance(newSpatialEntity);
                    double forceY = x * mass * Math.random() * rotationFactor / center.getDistance(newSpatialEntity);
                    double forceZ = Math.random() * zDistribution / 10;

                    newSpatialEntity.applyForceX(forceX);
                    newSpatialEntity.applyForceY(forceY);
                    newSpatialEntity.applyForceZ(forceZ);
                    entities.add(newSpatialEntity);
                });


        System.out.println("Contstruction time: " + (System.currentTimeMillis() - time));

        return entities;
	}

}
