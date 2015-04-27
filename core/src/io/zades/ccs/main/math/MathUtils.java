package io.zades.ccs.main.math;

import com.badlogic.gdx.math.Vector2;

/**
 * General Class of Math related functions
 * Created by Darren on 4/25/2015.
 */
public class MathUtils
{
	/**
	 * Finds the distance between two points
	 * @param point1
	 * @param point2
	 * @return
	 */
	public static float distance(Vector2 point1, Vector2 point2)
	{
		return (float) Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));

	}

}
