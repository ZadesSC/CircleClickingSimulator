package io.zades.ccs.main.math;

import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Man why cant I just find a bezier library, this shit is a pain in the ass
 * Created by Darren on 4/25/2015.
 */
public class Bezier extends Path
{
	public Bezier(List<Vector2> points)
	{
		super(points);
		this.dervPoints = new ArrayList<>();
		this.cachedCalculatedPoints = new ArrayList<>();
		this.cachedCalculatedDervPoints = new ArrayList<>();
		this.estimatedLength = this.estimateLength(this.points);

		this.calculateDervPoints();
		this.calculatePath();
	}

	@Override
	public Vector2 valueAt(float t)
	{
		return this.deCasteljanAlgorithm(this.points, t);
	}

	@Override
	public Vector2 derivativeAt(float t)
	{
		return this.deCasteljanAlgorithm(this.dervPoints, t);
	}

	@Override
	public float getEstimatedLength()
	{
		return this.estimatedLength;
	}

	@Override
	public float getExactLength()
	{
		return this.getEstimatedLength();
	}

	@Override
	protected void calculatePath()
	{
		//Well I guess lets try this shit out
		long segments = this.calculateSegments();

		for(int x = 0; x < segments; x++)
		{
			//calculates points
			this.cachedCalculatedPoints.add(this.deCasteljanAlgorithm(this.points, (float) (1.0 / ((float) segments) * x)));
			//this.cachedCalculatedPoints.add(this.deCasteljanAlgorithm(this.points.size() - 1, 0, (float) (1.0 / ((float) segments) * x)));
			//calculates derv points
			//this.cachedCalculatedDervPoints.add(this.deCasteljanAlgorithm(this.dervPoints, (float) (1.0 / ((float) segments) * x)).getNormalizedVector(Vector.VectorType.GAME));
		}

		//recalculate the length
		this.estimatedLength = this.estimateLength(this.cachedCalculatedPoints);
		//TODO: either add another run to have a better estimate or redo the initial estimation calculation
	}

	/**
	 * This just uses the distance formula to add up the distances between the points to get a rough
	 * estimate of the length of the curve
	 * @return
	 */
	@Override
	protected float estimateLength(List<Vector2> points)
	{
		float estLength = 0;

		for(int i = 0; i < points.size() - 1; i++)
		{
			//Using distance formula
			estLength += Math.sqrt(Math.pow(points.get(i).x - points.get(i + 1).x, 2) +
					Math.pow(points.get(i).y - points.get(i + 1).y, 2));
		}

		return estLength;
	}

	/**
	 * Recursive function that runs in O(n^2) time that finds the location on a bezier curve given t where 0 <= t <= 1
	 * It uses de Castlejan's Algorithm:
	 * http://en.wikipedia.org/wiki/De_Casteljau%27s_algorithm
	 *
	 * @param points The list of points to evaluate
	 * @param t The position in the curve where  0 <= t <= 1
	 * @return The coords at t in the curve
	 */
	private Vector2 deCasteljanAlgorithm(List<Vector2> points, float t)
	{
		if(points.size() == 1)
		{
			return points.get(0);
		}
		else
		{
			List<Vector2> newPoints = new ArrayList<>();
			for(int i = 0; i < points.size() - 1; i++)
			{
				float x = (1-t) * points.get(i).x + t * points.get(i+1).x;
				float y = (1-t) * points.get(i).y + t * points.get(i+1).y;
				newPoints.add(new Vector2(x, y));
			}

			return deCasteljanAlgorithm(newPoints, t);
		}
	}

	/**
	 * http://protein.ektf.hu/book/export/html/51
	 * @param r
	 * @param i
	 * @param t
	 * @return
	 */
	private Vector2 deCasteljanAlgorithmAlt(int r, int i, double t)
	{
		if(r == 0)
		{
			return points.get(i);
		}

		Vector2 p1 = deCasteljanAlgorithmAlt(r - 1, i, t);
		Vector2 p2 = deCasteljanAlgorithmAlt(r - 1, i + 1, t);

		return new Vector2((float) ((1 - t) * p1.x + t * p2.x), (float) ((1 - t) * p1.y + t * p2.y));
	}

	/**
	 * Simple function used to calculate the number of segments to be drawn by using a hyperbola to smooth out short
	 * curves
	 *
	 * @return The number of segments, or 0 if length has not been determined
	 */
	private long calculateSegments()
	{
		//Hypoerbola equation:
		//y = sqrt((1+x^2)) + 10

		//TODO: get a better estimation formula
		return Math.round(Math.sqrt(1 + Math.pow(this.estimateLength(this.points), 2)) + 10);
	}

	/**
	 * Function used to calculate the control points for the derivative
	 */
	private void calculateDervPoints()
	{
		int n = this.points.size() - 1;
		for(int i = 0; i < n; i++)
		{
			float  x = n * (this.points.get(i+1).x - this.points.get(i).x);
			float y = n * (this.points.get(i+1).y - this.points.get(i).y);
			this.dervPoints.add(new Vector2(x, y));
		}
	}
}
