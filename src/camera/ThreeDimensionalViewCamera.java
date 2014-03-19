package camera;

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
public class ThreeDimensionalViewCamera extends TwoDimensionalViewCamera {

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
		super(x, y, z);
		xAngle = 0;
		yAngle = 0;
		zAngle = 0;
	}
	
	public ThreeDimensionalViewCamera(double x, double y, double z, double xAngle, double yAngle, double zAngle) {
		super(x, y, z);
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
	}
	
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
	 ********************************************************************************/
	
	public void addDeltaLeftRight(double delta) {
		double deltaX;
		double deltaY;
		double deltaZ;
		double angle;
		
		
		// Handle rotation on Z axis
		angle = Math.toRadians(getZAngle());
		deltaX = delta * Math.cos(angle);
		deltaY = delta * Math.sin(angle);
		addDeltaX(deltaX);
		addDeltaY(deltaY);
		
		// Handle rotation on Y axis

		
		//addDeltaZ(deltaZ);
	}

	public void addDeltaUpDown(double delta) {
		double deltaX;
		double deltaY;
		double deltaZ;
		double angle;
		

		
		// Handle rotation on X axis
		
		
		// Handle rotation on Z axis
		angle = Math.toRadians(getZAngle() + 90);
		deltaX = -1 * delta * Math.cos(angle);
		deltaY = delta * Math.sin(angle);
		addDeltaX(deltaX);
		addDeltaY(deltaY);
		
		
		
		
	}
	
	public void addDeltaForwardBackwards(double delta) {
		double deltaX;
		double deltaY;
		double deltaZ;
		double angle;

		
		// Handle rotation on Y axis
		
		// Handle rotation on X axis
		angle = Math.toRadians(getXAngle());
		deltaZ = delta * Math.cos(angle);
		deltaY = delta * Math.sin(angle);
		addDeltaZ(deltaZ);
		addDeltaY(deltaY);
		
		
	}
	

}
