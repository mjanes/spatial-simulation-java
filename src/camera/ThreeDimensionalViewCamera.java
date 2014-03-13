package camera;

/**
 * A camera in three dimensional space. Extends TwoDimensionalViewCamera.
 * 
 * A TwoDimensionalViewCamera exists in three dimensional space. What is added by
 * the ThreeDimensionalViewCamera class is the ability to rotate the direction it 
 * is looking in. 
 * 
 * @author mjanes
 *
 */
public class ThreeDimensionalViewCamera extends TwoDimensionalViewCamera {

	public ThreeDimensionalViewCamera(double x, double y, double z) {
		super(x, y, z);
	}

}
