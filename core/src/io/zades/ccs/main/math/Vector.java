//package io.zades.ccs.main.math;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.math.Vector2;
//
///**
// * A class that holds 2 points, using this instead of LibGDX vector simply because I need some custom functions
// * Created by Darren on 10/26/2014.
// */
//public class Vector
//{
//	public enum VectorType {GAME, LIBGDX};
//	private double x;
//	private double y;
//
//	public Vector(double x, double y, VectorType type)
//	{
//		switch (type)
//		{
//			case GAME:
//				this.setX(x);
//				this.setY(y);
//				break;
//			case LIBGDX:
//				this.setGdxX(x);
//				this.setGdxY(y);
//				break;
//		}
//	}
//
//	public Vector getNormalizedVector(VectorType type)
//	{
//		switch (type)
//		{
//			case GAME:
//				float lengthGame = MathUtils.distance(new Vector(0,0,VectorType.GAME), this, VectorType.GAME);
//				return new Vector(this.getX()/lengthGame, this.getY()/lengthGame, VectorType.GAME);
//			case LIBGDX:
//				float lengthGdx = MathUtils.distance(new Vector(0,0,VectorType.LIBGDX), this, VectorType.LIBGDX);
//				return new Vector(this.getGdxX()/lengthGdx, this.getGdxY()/lengthGdx, VectorType.LIBGDX);
//		}
//
//		return null;
//	}
//
//	/**
//	 * Rotates this vector about another one by a certain amount of degrees
//	 * @param about
//	 * @param degrees
//	 * @return
//	 */
//	public Vector rotate(Vector about, float degrees)
//	{
//		//Get the trigs
//		float sine = (float) Math.sin(degrees);
//		float cos = (float) Math.cos(degrees);
//
//		//Translate back to origin
//		float newX = (float) (this.getX() - about.getX());
//		float newY = (float) (this.getY() - about.getX());
//
//		//Rotate point
//		float rotatedX = newX * cos - newY * sine;
//		float rotatedY = newX * sine + newY * cos;
//
//		//Translate back
//		newX = (float) (rotatedX + about.getX());
//		newY = (float) (rotatedY + about.getY());
//
//		return  new Vector(newX, newY, VectorType.GAME);
//	}
//
//	public Vector add(Vector a)
//	{
//		return new Vector(this.getX() + a.getX(), this.getY() + a.getY(), VectorType.GAME);
//	}
//
//	public Vector subtract(Vector a)
//	{
//		return new Vector(this.getX() - a.getX(), this.getY() - a.getY(), VectorType.GAME);
//	}
//
//	public com.badlogic.gdx.math.Vector2 toGdxVector()
//	{
//		return new Vector2((float)this.getGdxX(), (float)this.getGdxY());
//	}
//
//	private double getRatio()
//	{
//		//we check to ratio to know what to scale with, 0.75 is the scale of the game internals (384/512)
//		//if the screen ratio is smaller than 0.75 we match the height, if it's larger we match the width
//		if(Gdx.app.getGraphics().getHeight()/Gdx.app.getGraphics().getWidth() > 0.75)
//		{
//			return 512.0 / Gdx.app.getGraphics().getWidth();
//		}
//		else
//		{
//			return 384.0 / Gdx.app.getGraphics().getHeight();
//		}
//	}
//
//	public double getX()
//	{
//		return x;
//	}
//
//	public void setX(double x)
//	{
//		this.x = x;
//	}
//
//	public double getY()
//	{
//		return y;
//	}
//
//	public void setY(double y)
//	{
//		this.y = y;
//	}
//
//	public void setGdxX(double x)
//	{
//		//convert gdx units into internal units
//		this.x = x * this.getRatio();
//	}
//
//	public double getGdxX()
//	{
//		return this.x / this.getRatio();
//	}
//
//	public void setGdxY(double y)
//	{
//		this.y = 384 - y * this.getRatio();
//	}
//
//	public double getGdxY()
//	{
//		return Gdx.app.getGraphics().getHeight() - this.y / this.getRatio();
//
//	}
//}
