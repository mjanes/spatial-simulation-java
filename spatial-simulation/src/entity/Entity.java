package entity;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;

import java.util.ArrayList;
import java.util.List;

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

    // return new Array2DRowRealMatrix(new double[] {x, y, z, 1});
	
	protected double mPrevX;
	protected double mPrevY;
	protected double mPrevZ;
	
	protected double mDeltaX;
	protected double mDeltaY;
	protected double mDeltaZ;
	
	protected double mMass = 1;
	protected double mDensity = 1;
	
	protected ArrayList<IConnectedEntity> mConnections = new ArrayList<>();

    private static final double DEFAULT_DENSITY = 200;

	public Entity(double x, double y, double z, double mass) {
		this(x, y, z);
		mMass = mass;
        mDensity = Math.sqrt(mass / DEFAULT_DENSITY);
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
	}

	@Override
	public double getDensity() {
		return mDensity;
	}

	@Override
	public void setDensity(double density) {
		mDensity = density;
	}
	
	/* 
	 * Putting this in now for simple graphics
	 */
	public double getRadius() {
		double volume = mMass / mDensity;
		// volume = 4/3 * pi * r ^ 3
		// r ^ 3 = volume / (4/3 * pi)
		// r = cube root(volume / (4/3 * pi))
		return Math.pow(((volume / (4.0/3 * Math.PI))), (1.0 / 3));
	}
	
	
	
	/**********************************************************************************
	 * Involving multiple objects
	 **********************************************************************************/
	
	@Override
	public double getDistance(IDimensionalEntity other) {
		return getDistance(this, other);
    }

    public static double getDistance(IDimensionalEntity a, IDimensionalEntity b) {
        return Math.sqrt(
                Math.pow((a.getX() - b.getX()), 2) +
                        Math.pow((a.getY() - b.getY()), 2) +
                        Math.pow((a.getZ() - b.getZ()), 2)
        );
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
     * Matrix for display
     ******************************************************************************************************/

    public Array2DRowRealMatrix getR4Matrix() {
        return r4Matrix;
    }

    // TODO: This will be called a lot, create a better way of doing this. Return point?
    // Or git rid of the internal xyz values, and just have a point.
    public Entity getPrevLocationAsEntity() {
        return new Entity(getPrevX(), getPrevY(), getPrevZ());
    }
}
