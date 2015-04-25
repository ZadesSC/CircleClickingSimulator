package io.zades.ccs.main.math;

import com.badlogic.gdx.Gdx;
import io.zades.ccs.main.gamestates.MainMenuGameState;

import java.util.ArrayList;
import java.util.List;

/**
 * Man why cant I just find a bezier library, this shit is a pain in the ass
 * Created by Darren on 4/25/2015.
 */
public class Bezier extends Path
{
	public Bezier(List<Coords> points)
	{
		super(points);
	}

	@Override
	public Coords valueAt(float t)
	{
		return this.deCasteljanAlgorithm(this.points, t);
	}

	@Override
	public Coords derivativeAt(float t)
	{
		return null;
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
		this.cachedCalculatedPoints = new ArrayList<>((int)segments);

		for(int x = 0; x < segments; x++)
		{
			this.cachedCalculatedPoints.add(this.deCasteljanAlgorithm(this.points, (float) (1.0 / ((float) segments) * x)));
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
	protected float estimateLength(List<Coords> points)
	{
		float estLength = 0;

		for(int i = 0; i < points.size() - 1; i++)
		{
			//Using distance formula
			estLength += Math.sqrt( Math.pow(points.get(i).getX() - points.get(i + 1).getX(), 2) +
					Math.pow(points.get(i).getY() - points.get(i+1).getY(),2));
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
	private Coords deCasteljanAlgorithm(List<Coords> points, float t)
	{
		if(points.size() == 1)
		{
			return points.get(0);
		}
		else
		{
			List<Coords> newPoints = new ArrayList<>();
			for(int i = 0; i < points.size() - 1; i++)
			{
				double x = (1-t) * points.get(i).getX() + t * points.get(i+1).getX();
				double y = (1-t) * points.get(i).getY() + t * points.get(i+1).getY();
				newPoints.add(new Coords(x, y, Coords.CoordType.GAME));
			}

			return deCasteljanAlgorithm(newPoints, t);
		}
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
}
