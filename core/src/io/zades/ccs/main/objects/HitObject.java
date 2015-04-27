package io.zades.ccs.main.objects;

import com.badlogic.gdx.math.Vector2;
import io.zades.ccs.main.math.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 10/26/2014.
 */
public class HitObject
{
	public static final int CIRCLE_HIT_OBJECT = 1;
	public static final int SLIDER_HIT_OBJECT = 2;
	public static final int NEW_COMBO = 4;
	public static final int SPINNER_HIT_OBJECT = 8; //?

	public static final String LINEAR_SLIDER = "L";
	public static final String BEZIER_SLIDER = "B";
	public static final String PASS_THROUGH_SLIDER = "P";
	public static final String CATMULL_SLIDER = "C";

	public static final int NORMAL_HIT_SOUND = 0;
	public static final int WHISTLE_HIT_SOUND = 2;
	public static final int FINISH_HIT_SOUND = 3;
	public static final int CLAP_HIT_SOUND = 4;

	public static final int[] LIST_OF_HIT_OBJECTS =
	{
			CIRCLE_HIT_OBJECT,
			SLIDER_HIT_OBJECT,
			SPINNER_HIT_OBJECT,
	};

	public static final String[] LIST_OF_SLIDER_TYPES =
	{
			LINEAR_SLIDER,
			BEZIER_SLIDER,
			PASS_THROUGH_SLIDER,
			CATMULL_SLIDER
	};

	public static final int[] LIST_OF_HIT_SOUNDS =
	{
			NORMAL_HIT_SOUND,
			WHISTLE_HIT_SOUND,
			FINISH_HIT_SOUND,
			CLAP_HIT_SOUND
	};

	private int hitObjectType;
	private String sliderType;
	private int hitSoundType;

	private List<Vector2> coords;
	private Path curve;
	private long offsetTime;
	private boolean isNewCombo;

	public HitObject()
	{
		coords = new ArrayList<Vector2>();
	}

	public HitObject(List<Vector2> coords, long startTime, int type)
	{
		this.coords = coords;
		this.offsetTime = startTime;
		this.hitObjectType = type;
	}

	public int getHitObjectType()
	{
		return hitObjectType;
	}

	public void setHitObjectType(int hitObjectType)
	{
		this.hitObjectType = hitObjectType;
	}

	public List<Vector2> getCoords()
	{
		return coords;
	}

	public void setCoords(List<Vector2> coords)
	{
		this.coords = coords;
	}

	public long getOffsetTime()
	{
		return offsetTime;
	}

	public void setOffsetTime(long offsetTime)
	{
		this.offsetTime = offsetTime;
	}

	public String getSliderType()
	{
		return sliderType;
	}

	public void setSliderType(String sliderType)
	{
		this.sliderType = sliderType;
	}

	public int getHitSoundType()
	{
		return hitSoundType;
	}

	public void setHitSoundType(int hitSoundTyoe)
	{
		this.hitSoundType = hitSoundTyoe;
	}

	public boolean isNewCombo()
	{
		return isNewCombo;
	}

	public void setNewCombo(boolean isNewCombo)
	{
		this.isNewCombo = isNewCombo;
	}

	public Path getCurve()
	{
		return curve;
	}

	public void setCurve(Path curve)
	{
		this.curve = curve;
	}
}
