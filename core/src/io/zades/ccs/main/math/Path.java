package io.zades.ccs.main.math;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 4/25/2015.
 * Because GDX's path and shit does not meet my needs
 */
public abstract class Path
{
	//List of control points for the path
	protected List<Vector2> points;

	//List of calculated points alone the path
	protected List<Vector2> cachedCalculatedPoints;

	//List of control points for the derivative
	protected List<Vector2> dervPoints;

	//List of calculated points alone the derivative
	protected List<Vector2> cachedCalculatedDervPoints;

	protected float estimatedLength;
	protected float exactLength;

	/**
	 * Iniates the path with the given list of control points
	 * @param points The List of control points
	 */
	public Path(List<Vector2> points)
	{
		this.points = points;
	}

	/**
	 * Finds the value of T at a given time
	 * @param t Is the time where 0 <= t <= 1
	 * @return The value of T at that point in time
	 */
	public abstract Vector2 valueAt(float t);

	/**
	 * Finds the derivative of T at a given time
	 * @param t Is the time where 0 <= t <= 1
	 * @return The value of T at that point in time
	 */
	public abstract Vector2 derivativeAt(float t);

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
	protected abstract float estimateLength(List<Vector2> points);

	public List<Vector2> getPoints()
	{
		return points;
	}

	public void setPoints(ArrayList<Vector2> points)
	{
		this.points = points;
	}

	public List<Vector2> getCachedCalculatedPoints()
	{
		return cachedCalculatedPoints;
	}

	public void setCachedCalculatedPoints(ArrayList<Vector2> cachedCalculatedPoints)
	{
		this.cachedCalculatedPoints = cachedCalculatedPoints;
	}

	public List<Vector2> getDervPoints()
	{
		return dervPoints;
	}

	public void setDervPoints(List<Vector2> dervPoints)
	{
		this.dervPoints = dervPoints;
	}

	public List<Vector2> getCachedCalculatedDervPoints()
	{
		return cachedCalculatedDervPoints;
	}

	public void setCachedCalculatedDervPoints(List<Vector2> cachedCalculatedDervPoints)
	{
		this.cachedCalculatedDervPoints = cachedCalculatedDervPoints;
	}
}
