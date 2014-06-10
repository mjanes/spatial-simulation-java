package physics;

import entity.Entity;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Do we want this to extend an interface of 'Physics'? Maybe? What do I want the base physics operation to be? Well, something that takes a
 * collection of entities? And... hrm... how do we want that to work? I mean... so, all these objects are going to be passed as pointers, so, we
 * can edit the objects themselves. But, some of the entities might be destroyed or new ones created.
 * 
 * Yeah, I suppose it would probably be cleaner to not have this be a separate class, but to put everything in the entities themselves. So...
 * well, makes it harder to use the same code for both this and for social network display, but that's probably just premature optimization.
 * 
 * @author mjanes
 */
public class GravitationalPhysics {
	
	public static final double GRAVITATIONAL_CONSTANT = .01;

	/** 
	 * I might want to rename this to threeDimensionalGravitationalPhysics to separate it from two dimensional objects. 
	 * Might just have both in here for now.
	 *
	 * @param entities Entities to run gravitational attraction on.
	 */
	public static void gravity(List<Entity> entities) {
        if (entities == null) return;

        int count = entities.size();
        IntStream.range(0, count).parallel()
                .forEach(i -> IntStream.range(i + 1, count)
                        .forEach(j -> GravitationalPhysics.gravitationallyAttract(entities.get(i), entities.get(j))));
	}

    /**
     * This calculates amount of force the puller object imparts on the pullee.
     *
     *
     */
    private static void gravitationallyAttract(Entity object1, Entity object2) {
        // Distance between the two points
        double distance = Entity.getDistance(object1, object2);

        // Gravitational force that the two objects will impart on each other apply Shell theorem
        double effectiveObject1Mass = getEffectiveMass(distance, object1.getRadius(), object1.getMass());
        double effectiveObject2Mass = getEffectiveMass(distance, object2.getRadius(), object2.getMass());

        // Newton's law of universal gravitation
        // F = G ((m1 * m2) / r ^ 2)
        // https://en.wikipedia.org/wiki/Newton's_law_of_universal_gravitation
        double force = (GRAVITATIONAL_CONSTANT * effectiveObject1Mass * effectiveObject2Mass) / Math.pow(distance, 2);


        // Translate the force into x, y, and z parameters using 3D pythagorean theorem
        // http://www.odeion.org/pythagoras/pythag3d.html
        // force ^ 2 = forceX ^ 2 + forceY ^ 2 + forceZ ^ 2,
        //  and forceX, forceY, forceZ are proportional to x, y, and z distance
        double xDistance = object1.getX() - object2.getX();
        double yDistance = object1.getY() - object2.getY();
        double zDistance = object1.getZ() - object2.getZ();

        double distanceForceRatio = force / distance;

        double forceX = xDistance * distanceForceRatio;
        double forceY = yDistance * distanceForceRatio;
        double forceZ = zDistance * distanceForceRatio;

        object1.applyForceX(-forceX);
        object1.applyForceY(-forceY);
        object1.applyForceZ(-forceZ);

        object2.applyForceX(forceX);
        object2.applyForceY(forceY);
        object2.applyForceZ(forceZ);
    }

    /**
     *
     * However, it is not quite that simple. As we have not yet implemented collisions, and do not
     * want to depend upon collisions, we need to account for how the gravitation force exerted by
     * an object is reduced if you are inside that object.
     *
     * Reference: https://en.wikipedia.org/wiki/Shell_theorem
     */
    private static double getEffectiveMass(double distance, double radius, double mass) {
        if (distance > radius) return mass;
        double radiusRatio = distance / radius;
        return mass * Math.pow(radiusRatio, 3);
    }

}
