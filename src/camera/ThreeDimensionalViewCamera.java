package camera;

import entity.IThreeDimensionalEntity;

/**
 * A camera in three dimensional space. Extends TwoDimensionalViewCamera.
 * 
 * A TwoDimensionalViewCamera exists in three dimensional space. What is added by
 * the ThreeDimensionalViewCamera class is the ability to rotate the direction it 
 * is looking in. 
 * 
 * And I understand that I'm probably using non-traditional terms for everything here
 * I suppose I should be using: https://en.wikipedia.org/wiki/Euler_angles
 * But at the moment I'm trying to teach myself things.
 * 
 * May change to official Euler angles later. Or pitch/yaw/roll.
 * 
 * @author mjanes
 *
 */
public class ThreeDimensionalViewCamera implements IThreeDimensionalEntity {
	
	protected double x;
	protected double y;
	protected double z;
	
	protected double deltaX;
	protected double deltaY;
	protected double deltaZ;
	

	// Orientation values
	// All of these are angles
	// Such that if the camera is located at x = 0, y = 0, z = 1, 
	// With orientation xAngle = 0, yAngle = 0, and zAngle = 0
	// A point located at x = 0, y = 0, z = 0, would appear in the center of the field of view
	
	// If the camera is located at x = 0, y = 0, z = -1, 
	// With orientation xAngle = 0, yAngle = 0, and zAngle = 0
	// A point located at x = 0, y = 180, z = 0, would appear in the center of the field of view
	
	// Imagine each axis, and each of these angles as a clockwise rotation around that axis
	double xAngle;
	double yAngle;
	double zAngle;
	
	
	public ThreeDimensionalViewCamera(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		xAngle = 0;
		yAngle = 0;
		zAngle = 0;
	}
	
	public ThreeDimensionalViewCamera(double x, double y, double z, double xAngle, double yAngle, double zAngle) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
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
	
	
	/*********************************************************************
	 * Angles
	 *********************************************************************/
	
	public double getXAngle() {
		return xAngle;
	}
	
	public void incrementXAngle(double increment) {
		xAngle += increment;
		xAngle = xAngle % 360;
	}
	
	public double getYAngle() {
		return yAngle;
	}
	
	public void incrementYAngle(double increment) {
		yAngle += increment;
		yAngle = yAngle % 360;
	}
	
	public double getZAngle() {
		return zAngle;
	}
	
	public void incrementZAngle(double increment) {
		zAngle += increment;
		zAngle = zAngle % 360;
	}
		
	
	/********************************************************************************
	 * Movement functions relative to the direction the camera is facing in.
	 * 
	 * After some initial math on this was broken, turning towards references:
	 * http://www.mathsisfun.com/polar-cartesian-coordinates.html
	 * https://en.wikipedia.org/wiki/Spherical_coordinate_system
	 * https://en.wikipedia.org/wiki/List_of_common_coordinate_transformations#To_Cartesian_coordinates
	 * 
	 * So, I'm going to rewrite this.
	 * 
	 * First thing is to get an xyz coordinate, from delta and the three angles.
	 * Then we convert it to left/right up/down backwards/forwards by inverting
	 * whatever xyz.
	 ********************************************************************************/
	
	public void addDeltaSelfX(final double delta) {
		double deltaX;
		double angle;
		

		// Handle rotation on Y axis
		angle = Math.toRadians(getYAngle());
		deltaX = delta * Math.cos(angle);
		final double deltaZ = delta * Math.sin(angle);		
		
		// Handle rotation on Z axis
		angle = Math.toRadians(getZAngle());
		deltaX = deltaX * Math.cos(angle);
		final double deltaY = deltaX * Math.sin(angle);
			
		// Apply the motion
		addDeltaX(deltaX);		
		addDeltaY(deltaY);
		addDeltaZ(deltaZ);
		
		assert(delta == Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)));
	}

	public void addDeltaSelfY(final double delta) {
		double deltaY;
		double angle;

		// Handle rotation on X axis
		angle = Math.toRadians(getXAngle());
		deltaY = delta * Math.cos(angle);
		final double deltaZ = delta * Math.sin(angle);

		// Handle rotation on Z axis
		angle = Math.toRadians(getZAngle());
		deltaY = deltaY * Math.cos(angle);
		final double deltaX = deltaY * Math.sin(angle);		
		
		// Apply the new motion
		addDeltaX(deltaX);
		addDeltaY(deltaY);
		addDeltaZ(deltaZ);
		
		assert(delta == Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)));
	}
	
	
	public void addDeltaSelfZ(final double delta) {
		double deltaZ;
		double angle;

		// Handle rotation on Y axis
		angle = Math.toRadians(getYAngle());
		final double deltaX = delta * Math.sin(angle);
		deltaZ = delta * Math.cos(angle);		
		
		// Handle rotation on X axis
		angle = Math.toRadians(getXAngle());
		deltaZ = deltaZ * Math.cos(angle);
		final double deltaY = deltaZ * Math.sin(angle);
		
		// Apply the new motion
		addDeltaX(deltaX);
		addDeltaY(deltaY);
		addDeltaZ(deltaZ);		
		
		assert(delta == Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2) + Math.pow(deltaZ, 2)));
	}

}
