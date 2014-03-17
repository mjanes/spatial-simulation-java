package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Base physical entity for the physics sim. A sphereoid in three dimensional space. 
 * 
 * It has mass, velocity, spatial location, and potentially connections to other entities. 
 * 
 * @author mjanes
 */
public class BasePhysicalEntity implements IThreeDimensionalEntity, IPhysicalEntity, IConnectedEntity {
	
	protected double x;
	protected double y;
	protected double z;
	
	protected double prevX;
	protected double prevY;
	protected double prevZ;
	
	protected double deltaX;
	protected double deltaY;
	protected double deltaZ;
	
	protected double mass = 1;
	protected double density = 1;
	
	protected ArrayList<IConnectedEntity> connections = new ArrayList<IConnectedEntity>();
	
	public BasePhysicalEntity(double x, double y, double z, double mass) {
		this(x, y, z);
		this.mass = mass;
	}
	
	public BasePhysicalEntity(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}
	
	@Override
	public void setX(double x) { this.x = x; }
	
	@Override
	public double getX() { return x; }	
	
	@Override
	public void setY(double y) { this.y = y; }
	
	@Override
	public double getY() { return y; }

	@Override
	public void setZ(double z) { this.z = z; }

	@Override
	public double getZ() { return z; }
	
	
	/*********************** Previous location *******************/
	
	public double getPrevX() { return prevX; }
	public double getPrevY() { return prevY; }
	public double getPrevZ() { return prevZ; }

	
	/*********************** Delta *******************************/
	
	@Override
	public void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	@Override
	public double getDeltaX() {
		return deltaX;
	}
	
	@Override
	public void addDeltaX(double deltaDeltaX) {
		this.deltaX += deltaDeltaX;
	}

	@Override
	public void moveX(double deltaX) {
		prevX = x;
		x += deltaX;	
	}
	
	@Override
	public void setDeltaY(double deltaY) {
		this.deltaY = deltaY;
	}
	
	@Override
	public double getDeltaY() {
		return deltaY;
	}

	@Override
	public void addDeltaY(double deltaDeltaY) {
		this.deltaY += deltaDeltaY;
	}
	
	@Override
	public void moveY(double deltaY) {
		prevY = y;
		y += deltaY;
	}
		
	@Override
	public void setDeltaZ(double deltaZ) {
		this.deltaZ = deltaZ;
	}

	@Override
	public double getDeltaZ() {
		return deltaZ;
	}

	@Override
	public void addDeltaZ(double deltaDeltaZ) {
		this.deltaZ += deltaDeltaZ;
	}

	@Override
	public void moveZ(double deltaZ) {
		prevZ = z;
		z += deltaZ;
	}	
	
	@Override
	public void move() {
		moveX(deltaX);
		moveY(deltaY);
		moveZ(deltaZ);
	}	
	
	
	/*******************************************************************************************
	 * Physical side fo things
	 *******************************************************************************************/

	@Override
	public double getMass() {
		return mass;
	}
	
	@Override
	public void setMass(double mass) {
		this.mass = mass;
	}

	@Override
	public double getDensity() {
		return density;
	}

	@Override
	public void setDensity(double density) {
		this.density = density;
	}
	
	/* 
	 * Putting this in now for simple graphics
	 */
	public double getRadius() {
		double volume = mass * density;
		// volume = 4/3 * pi * r ^ 3
		// r ^ 3 = volume / (4/3 * pi)
		// r = cube root(volume / (4/3 * pi))
		return Math.pow(((volume / (4.0/3 * Math.PI))), (1.0 / 3));
	}
	
	
	
	/**********************************************************************************
	 * Involving multiple objects
	 **********************************************************************************/
	
	@Override
	public double getDistance(IThreeDimensionalEntity other) {
		return Math.sqrt(
				Math.pow((x - other.getX()), 2) + 
				Math.pow((y - other.getY()), 2) + 
				Math.pow((z - other.getZ()), 2)
			);
	}

	
	/**********************************************************************************
	 * Connections
	 **********************************************************************************/
	
	@Override
	public void addConnection(IConnectedEntity entity) {
		connections.add(entity);
	}

	@Override
	public void removeConnection(IConnectedEntity entity) {
		connections.remove(entity);
	}

	@Override
	public List<IConnectedEntity> getConnections() {
		return connections;
	}
	
	
}
