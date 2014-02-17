package camera;

import entity.IThreeDimensionalEntity;

public class TwoDimensionalViewCamera implements IThreeDimensionalEntity {

	protected double x;
	protected double y;
	protected double z;
	
	protected double deltaX;
	protected double deltaY;
	protected double deltaZ;
	
	
	/**********************************************************************
	 * Constructors and factory methods
	 **********************************************************************/
	
	public TwoDimensionalViewCamera(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	
	/**********************************************************************
	 * Movement, positions, getters, setters
	 **********************************************************************/
	
	@Override
	public void setX(double x) {
		this.x = x;
	}

	@Override
	public double getX() {
		return x;
	}

	@Override
	public void setDeltaX(double deltaX) {
		this.deltaX = deltaX;
	}

	@Override
	public void addDeltaX(double deltaDeltaX) {
		deltaX += deltaDeltaX;
	}

	@Override
	public double getDeltaX() {
		return deltaX;
	}

	@Override
	public void moveX(double deltaX) {
		x += deltaX;
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
	public void setDeltaY(double deltaY) {
		this.deltaY = deltaY;
	}

	@Override
	public void addDeltaY(double deltaDeltaY) {
		deltaY += deltaDeltaY;
	}

	@Override
	public double getDeltaY() {
		return deltaY;
	}
	
	@Override
	public void moveY(double deltaY) {
		y += deltaY;
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
	public void setDeltaZ(double deltaZ) {
		this.deltaZ = deltaZ;
	}

	@Override
	public void addDeltaZ(double deltaDeltaZ) {
		deltaZ += deltaDeltaZ;
	}

	@Override
	public double getDeltaZ() {
		return deltaZ;
	}
	
	@Override
	public void moveZ(double deltaZ) {
		z += deltaZ;
	}

	@Override
	public void move() {
		moveX(deltaX);
		moveY(deltaY);
		moveZ(deltaZ);
	}
	
	@Override
	public double getDistance(IThreeDimensionalEntity other) {
		return Math.sqrt(
				Math.pow((x - other.getX()), 2) + 
				Math.pow((y - other.getY()), 2) + 
				Math.pow((z - other.getZ()), 2)
			);
	}

}
