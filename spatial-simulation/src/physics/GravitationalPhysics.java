package physics;

import java.util.Collection;
import java.util.List;

import entity.BasePhysicalEntity;

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
     * Run round of physics
     *
     * @param entities
     */
    public static synchronized void updateUniverseState(List<BasePhysicalEntity> entities) {
        GravitationalPhysics.gravity(entities);
        for (BasePhysicalEntity entity : entities) {
            entity.move();
        }
    }

	/** 
	 * I might want to rename this to threeDimensionalGravitationalPhysics to separate it from two dimensional objects. 
	 * Might just have both in here for now.
	 *
	 * @param entities
	 */
	public static void gravity(List<BasePhysicalEntity> entities) {

        int count = entities.size();
        for (int i = 0; i < count; i++) {
            for (int j = i + 1; j < count; j++) {
                gravitationallyAttract(entities.get(i), entities.get(j));
            }
        }
	}

    /**
     * This calculates amount of force the puller object imparts on the pullee.
     *
     * F = G ((m1 * m2) / r ^ 2)
     *
     */
    public static void gravitationallyAttract(BasePhysicalEntity object1, BasePhysicalEntity object2) {
        // Distance between the two points
        double distance = BasePhysicalEntity.getDistance(object1, object2);

        // Gravitational force that the two objects will impart on eachother
        // apply Shell theorem
        double effectiveObject1Mass = getEffectiveMass(distance, object1.getRadius(), object1.getMass());
        double effectiveObject2Mass = getEffectiveMass(distance, object2.getRadius(), object2.getMass());

        double force = (GRAVITATIONAL_CONSTANT * effectiveObject1Mass * effectiveObject2Mass) / Math.pow(distance, 2);

        // Not sure ratio is the proper term here, but the next block of lines is arrive at how the one dimensional
        // force is translated into x, y, and z forces.
        double ratio = force / distance;

        double forceX = (object1.getX() - object2.getX()) * ratio;
        double forceY = (object1.getY() - object2.getY()) * ratio;
        double forceZ = (object1.getZ() - object2.getZ()) * ratio;

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
