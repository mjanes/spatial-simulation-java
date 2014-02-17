package entity;

public interface IThreeDimensionalEntity extends ITwoDimensionalEntity {
	public void setZ(double z);
	public double getZ();
	public void setDeltaZ(double deltaZ);
	public void addDeltaZ(double deltaDeltaZ);
	public double getDeltaZ();
	public void moveZ(double deltaZ);
	
	public double getDistance(IThreeDimensionalEntity other);
}
