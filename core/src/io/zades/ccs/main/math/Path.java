package io.zades.ccs.main.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 4/25/2015.
 * Because GDX's path and shit does not meet my needs
 */
public abstract class Path
{
	//List of control points for the path
	protected List<Coords> points;

	//List of calculated points alone the path
	protected List<Coords> cachedCalculatedPoints;

	protected float estimatedLength;
	protected float exactLength;

	/**
	 * Iniates the path with the given list of control points
	 * @param points The List of control points
	 */
	public Path(List<Coords> points)
	{
		this.points = points;
		this.cachedCalculatedPoints = new ArrayList<>();
		this.estimatedLength = this.estimateLength(this.points);

		this.calculatePath();
	}

	/**
	 * Finds the value of T at a given time
	 * @param t Is the time where 0 <= t <= 1
	 * @return The value of T at that point in time
	 */
	public abstract Coords valueAt(float t);

	/**
	 * Finds the derivative of T at a given time
	 * @param t Is the time where 0 <= t <= 1
	 * @return The value of T at that point in time
	 */
	public abstract Coords derivativeAt(float t);

	/**
	 * Returns the estimated length of the curve
	 * @return Estimated length of curve
	 */
	public abstract float getEstimatedLength();

	/**
	 * Returns the exact length of the curve, could be extremely intensive to calculate.
	 * This is not guaranteed to be implemented, it might just route back to the estimated version.
	 * @return Exact length of curve
	 */
	public abstract float getExactLength();

	/**
	 * Calculates the points alongs alone a line
	 */
	protected abstract void calculatePath();

	/**
	 * Attempts to estimate the length of the curve
	 * @return The estimated length of the curve
	 */
	protected abstract float estimateLength(List<Coords> points);

	public List<Coords> getPoints()
	{
		return points;
	}

	public void setPoints(ArrayList<Coords> points)
	{
		this.points = points;
	}

	public List<Coords> getCachedCalculatedPoints()
	{
		return cachedCalculatedPoints;
	}

	public void setCachedCalculatedPoints(ArrayList<Coords> cachedCalculatedPoints)
	{
		this.cachedCalculatedPoints = cachedCalculatedPoints;
	}
}
