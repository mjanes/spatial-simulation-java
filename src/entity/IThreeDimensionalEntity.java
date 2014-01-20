package entity;

public interface IThreeDimensionalEntity extends ITwoDimensionalEntity {
	public void setZ(double z);
	public double getZ();
	public void setDeltaZ(double deltaZ);
	public double getDeltaZ();
	
	public double getDistance(IThreeDimensionalEntity other);
}
