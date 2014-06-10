package entity;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base physical entity for the physics sim. A sphereoid in three dimensional space. 
 * 
 * It has mass, velocity, spatial location, and potentially connections to other entities. 
 * 
 * @author mjanes
 */
public class Entity implements IDimensionalEntity, IPhysicalEntity, IConnectedEntity, ILabeled {

	protected double mX;
	protected double mY;
	protected double mZ;

    protected Array2DRowRealMatrix r4Matrix = new Array2DRowRealMatrix(new double[] {0, 0, 0, 1});

	protected double mPrevX;
	protected double mPrevY;
	protected double mPrevZ;
	
	protected double mDeltaX;
	protected double mDeltaY;
	protected double mDeltaZ;
	
	protected double mMass = 1;
	protected double mDensity = 1;
	protected double mRadius;

	protected ArrayList<IConnectedEntity> mConnections = new ArrayList<>();

    private static final double DEFAULT_DENSITY = 300;

    public Entity(double x, double y, double z, double deltaX, double deltaY, double deltaZ, double mass) {
        this(x, y, z, mass);
        setDeltaX(deltaX);
        setDeltaY(deltaY);
        setDeltaZ(deltaZ);
    }

	public Entity(double x, double y, double z, double mass) {
		this(x, y, z);
		setMass(mass);
        setDensity(Math.sqrt(mass / DEFAULT_DENSITY));
	}
	
	public Entity(double x, double y, double z) {
        setX(x);
        setY(y);
        setZ(z);
        mPrevX = x;
        mPrevY = y;
        mPrevZ = z;
	}

	@Override
	public void setX(double x) {
        mPrevX = mX;
        mX = x;
        r4Matrix.setEntry(0, 0, x);
    }
	
	@Override
	public double getX() { return mX; }
	
	@Override
	public void setY(double y) {
        mPrevY = mY;
        mY = y;
        r4Matrix.setEntry(1, 0, y);
    }
	
	@Override
	public double getY() { return mY; }

	@Override
	public void setZ(double z) {
        mPrevZ = mZ;
        mZ = z;
        r4Matrix.setEntry(2, 0, z);
    }

	@Override
	public double getZ() { return mZ; }
	
	
	/*********************** Previous location *******************/
	
	public double getPrevX() { return mPrevX; }
	public double getPrevY() { return mPrevY; }
	public double getPrevZ() { return mPrevZ; }

	
	/*********************** Delta *******************************/
	
	@Override
	public void setDeltaX(double deltaX) {
		mDeltaX = deltaX;
	}

	@Override
	public double getDeltaX() {
		return mDeltaX;
	}
	
	@Override
	public void addDeltaX(double deltaDeltaX) {
		mDeltaX += deltaDeltaX;
	}

    public void applyForceX(double forceX) {
        addDeltaX(forceX / mMass);
    }

	@Override
	public void moveX(double deltaX) {
		setX(mX + deltaX);
	}
	
	@Override
	public void setDeltaY(double deltaY) {
		mDeltaY = deltaY;
	}
	
	@Override
	public double getDeltaY() {
		return mDeltaY;
	}

	@Override
	public void addDeltaY(double deltaDeltaY) {
		mDeltaY += deltaDeltaY;
	}

    public void applyForceY(double forceY) {
        addDeltaY(forceY / mMass);
    }

	@Override
	public void moveY(double deltaY) {
		setY(mY + deltaY);
	}
		
	@Override
	public void setDeltaZ(double deltaZ) {
		mDeltaZ = deltaZ;
	}

	@Override
	public double getDeltaZ() {
		return mDeltaZ;
	}

	@Override
	public void addDeltaZ(double deltaDeltaZ) {
		mDeltaZ += deltaDeltaZ;
	}

    public void applyForceZ(double forceZ) {
        addDeltaZ(forceZ / mMass);
    }

    @Override
	public void moveZ(double deltaZ) {
		setZ(mZ + deltaZ);
	}	
	
	@Override
	public void move() {
		moveX(mDeltaX);
		moveY(mDeltaY);
		moveZ(mDeltaZ);
        //mDistanceRecord = new HashMap<>();
	}	
	
	
	/*******************************************************************************************
	 * Physical side fo things
	 *******************************************************************************************/

	@Override
	public double getMass() {
		return mMass;
	}
	
	@Override
	public void setMass(double mass) {
		mMass = mass;
        setRadius();
	}

	@Override
	public double getDensity() {
		return mDensity;
	}

	@Override
	public void setDensity(double density) {
		mDensity = density;
        setRadius();
	}
	
	public double getRadius() {
		return mRadius;
	}

    private void setRadius() {
        double volume = mMass / mDensity;
        // volume = 4/3 * pi * r ^ 3
        // r ^ 3 = volume / (4/3 * pi)
        // r = cube root(volume / (4/3 * pi))
        mRadius = Math.pow(((volume / (4.0/3 * Math.PI))), (1.0 / 3));
    }
	
	
	/**********************************************************************************
	 * Involving multiple objects
	 **********************************************************************************/
	
	@Override
	public double getDistance(IDimensionalEntity other) {
		double distance = IDimensionalEntity.getDistance(this, other);
        return distance;
    }


    /**********************************************************************************
	 * Connections
	 **********************************************************************************/
	
	@Override
	public void addConnection(IConnectedEntity entity) {
		mConnections.add(entity);
	}

	@Override
	public void removeConnection(IConnectedEntity entity) {
		mConnections.remove(entity);
	}

	@Override
	public List<IConnectedEntity> getConnections() {
		return mConnections;
	}
	
	
	/********************************************************************************
	 * Label interface. Giving the entities labels for the moment to aid
	 * 3d debugging.
	 ********************************************************************************/

	@Override
	public boolean hasLabel() {
		return true;
	}

	@Override
	public String getLabel() {
		return "x: " + mX + ", y: " + mY + ", z: " + mZ;
	}


    /******************************************************************************************************
     * Display utilities
     ******************************************************************************************************/

    public Array2DRowRealMatrix getR4Matrix() {
        return r4Matrix;
    }

    public Entity getPrevLocationAsEntity() {
        return new Entity(getPrevX(), getPrevY(), getPrevZ());
    }


    /****************************************************************************************************
     * Collision utilities
     ****************************************************************************************************/

    public boolean isOverlapping(Entity other) {
        double minDistance = getRadius() + other.getRadius();
        if (Math.abs(getX() - other.getX()) > minDistance) return false;
        if (Math.abs(getY() - other.getY()) > minDistance) return false;
        if (Math.abs(getZ() - other.getZ()) > minDistance) return false;
        return getDistance(other) < minDistance;
    }

    public static Entity collide(Entity a, Entity b) {
        double newMass = a.getMass() + b.getMass();
        double aMassProportion = a.getMass() / newMass;
        double bMassProportion = b.getMass() / newMass;

        double newDeltaX = a.getDeltaX() * aMassProportion + b.getDeltaX() * bMassProportion;
        double newDeltaY = a.getDeltaY() * aMassProportion + b.getDeltaY() * bMassProportion;
        double newDeltaZ = a.getDeltaZ() * aMassProportion + b.getDeltaZ() * bMassProportion;

        double newX = a.getX() * aMassProportion + b.getX() * bMassProportion;
        double newY = a.getY() * aMassProportion + b.getY() * bMassProportion;
        double newZ = a.getZ() * aMassProportion + b.getZ() * bMassProportion;

        return new Entity(newX, newY, newZ, newDeltaX, newDeltaY, newDeltaZ, newMass);
    }

}
