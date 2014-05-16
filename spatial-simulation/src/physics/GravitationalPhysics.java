package physics;

import java.util.Collection;

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
	
	private static final double GRAVITATIONAL_CONSTANT = 100;
	
	/** 
	 * I might want to rename this to threeDimensionalGravitationalPhysics to separate it from two dimensional objects. 
	 * Might just have both in here for now.
	 * 
	 * Another question, can we depend upon this being sorted when it is giving to us? 
	 * Well, for this one, it's not going to matter, as it's always going to be O(n^2)
	 * 
	 * @param entities
	 * @return
	 */
	public static Collection<BasePhysicalEntity> gravity(Collection<BasePhysicalEntity> entities) {
		// At the moment this is not allowing for 
		for (BasePhysicalEntity puller : entities) {
			for (BasePhysicalEntity pullee : entities) {
				if (puller != pullee) {
					gravitationalPull(puller, pullee);
				}
			}
		}
		return entities;
	}

	/**
	 * This calculates amount of force the puller object imparts on the pullee.
	 *  
	 * F = G ((m1 * m2) / r ^ 2)
	 * 
	 * However, it is not quite that simple. As we have not yet implemented collisions, and do not
	 * want to depend upon collisions, we need to account for how the gravitation force exerted by
	 * an object is reduced if you are inside that object.
	 * 
	 * Reference: https://en.wikipedia.org/wiki/Shell_theorem
	 * 
	 * @param puller
	 * @param pullee
	 */
	public static void gravitationalPull(BasePhysicalEntity puller, BasePhysicalEntity pullee) {
		// Distance between the two points
		double distance = distance(puller, pullee);
		
		// Gravitational force that the puller will impart on the pullee
		double force = GRAVITATIONAL_CONSTANT * puller.getMass() / Math.pow(distance, 2); 
		
		// Check for whether or not the pullee object's center is within the sphere of the puller, and vary
		// the force appropriately, via the Shell theorem
		if (distance < puller.getRadius()) {
			force = force * (distance / puller.getRadius());
		}
		
		// Not sure ratio is the proper term here, but the next block of lines is arrive at how the one dimensional 
		// force is translated into x, y, and z forces.
		double ratio = force / distance;

		double forceX = (puller.getX() - pullee.getX()) * ratio;
		double forceY = (puller.getY() - pullee.getY()) * ratio;
		double forceZ = (puller.getZ() - pullee.getZ()) * ratio;		
		
		// Divided by pullee's mass, as the greater its mass, the harder it is to move.
		// TODO: Get rid of addDeltaX functions, and replace with a forceX function?
		// TODO: Learn physics terminology...
		double deltaDeltaX = (forceX) / pullee.getMass();
		double deltaDeltaY = (forceY) / pullee.getMass();
		double deltaDeltaZ = (forceZ) / pullee.getMass();
		
		
		pullee.addDeltaX(deltaDeltaX);
		pullee.addDeltaY(deltaDeltaY);
		pullee.addDeltaZ(deltaDeltaZ);
	}

	
	
	/******************************************************************************************************
	 *  Distance
	 *  
	 *  This should be perhaps put in a root physics class. Hrm... Still need to determine how to organize 
	 *  all this.
	 ******************************************************************************************************/
	
	public static double distance(BasePhysicalEntity a, BasePhysicalEntity b) {
		return Math.sqrt(
					Math.pow((a.getX() - b.getX()), 2) + 
					Math.pow((a.getY() - b.getY()), 2) + 
					Math.pow((a.getZ() - b.getZ()), 2)
				);
	}
	
}
