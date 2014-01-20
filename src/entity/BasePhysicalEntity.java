package entity;

/**
 * Base physical entity for the physics sim. A sphereoid in three dimensional space. 
 * 
 * @author mjanes
 */
public class BasePhysicalEntity implements IThreeDimensionalEntity, IPhysicalEntity {
	
	protected double x;
	protected double y;
	protected double z;
	
	protected double deltaX;
	protected double deltaY;
	protected double deltaZ;
	
	protected double mass = 1;
	protected double density = 1;
	
	public BasePhysicalEntity(double x, double y, double z, double mass) {
		this(x, y, z);
		this.mass = mass;
	}
	
	public BasePhysicalEntity(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public void setX(double x) {
		this.x = x;
	}
	
	@Override
	public double getX() {
		return x;		
	}	
	
	@Override
	public void setY(double y) {
		this.y = y;
	}
	
	@Override
	public double getY() {
		return y;
	}

	@Override
	public void setZ(double z) {
		this.z = z;
	}

	@Override
	public double getZ() {
		return z;
	}

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
	
	@Override
	public void move() {
		x += deltaX;
		y += deltaY;
		z += deltaZ;
	}
	
	@Override
	public double getDistance(IThreeDimensionalEntity other) {
		return Math.sqrt(
				Math.pow((x - other.getX()), 2) + 
				Math.pow((y - other.getY()), 2) + 
				Math.pow((z - other.getZ()), 2)
			);
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
	
}
