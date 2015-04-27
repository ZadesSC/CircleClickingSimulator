package io.zades.ccs.main.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.zades.ccs.main.CCSCore;
import io.zades.ccs.main.objects.CCSSkin;
import io.zades.ccs.main.objects.HitObject;
import io.zades.ccs.main.objects.beatmaps.Beatmap;
import io.zades.ccs.main.objects.beatmaps.DifficultyTable;
import javafx.util.Pair;

import java.util.HashMap;

/**
 * Created by Zades on 12/24/2014.
 */
public class BeatmapGraphicsManager
{
    private CCSCore game;
    private Beatmap beatmap;
    private HashMap<Long, Pair<HitObject, Boolean>> drawableHitObjects;

    private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

    public BeatmapGraphicsManager(CCSCore game, Beatmap beatmap)
    {
        this.game = game;
        this.beatmap = beatmap;
        this.drawableHitObjects = new HashMap<>();

        this.batch = new SpriteBatch();
	    this.shapeRenderer = new ShapeRenderer();


   }

    //This method exists so that if a long ass song is played the loading doesn't stop the default thread
    public void init()
    {
        //TODO: fix le hardcode
        this.beatmap.getDifficultyData().setApproachRate(9);
        this.beatmap.getDifficultyData().setOverallDifficulty(8);

        for(HitObject hitObject: this.beatmap.getHitObjects())
        {
            //TODO: look into why there isn't a 0th index
            if(hitObject != null)
            {
                this.drawableHitObjects.put(hitObject.getOffsetTime(), new Pair<>(hitObject, false));
            }
        }
    }

    public void draw(long elapsedTime)
    {
        long maxBeforeDrawTime = elapsedTime - DifficultyTable.odDefaultTable[this.beatmap.getDifficultyData().getOverallDifficulty()][1];
        long maxAfterDrawTime = elapsedTime + DifficultyTable.arDefaultTable[this.beatmap.getDifficultyData().getApproachRate()];

	    Gdx.app.debug(BeatmapGraphicsManager.class.toString(), "AR:" + this.beatmap.getDifficultyData().getApproachRate() + " at: " + DifficultyTable.arDefaultTable[this.beatmap.getDifficultyData().getApproachRate()]);


	    this.batch.begin();
	    this.batch.setProjectionMatrix(this.game.camera.combined);

	    this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
	    this.shapeRenderer.setColor(Color.WHITE);

	    //TODO: might be more effiecnt to iterate over the list of hitobjects
        for(long time = maxBeforeDrawTime; time <= maxAfterDrawTime; time++)
        {
            if(this.drawableHitObjects.containsKey(time))
            {
                this.drawHitObject(elapsedTime, this.drawableHitObjects.get(time).getKey());
            }
        }

        this.batch.end();
	    this.shapeRenderer.end();
    }

    private void drawHitObject(long elapsedTime, HitObject hitObject)
    {
        switch(hitObject.getHitObjectType())
        {
            case HitObject.CIRCLE_HIT_OBJECT:
                this.drawHitCircleObject(elapsedTime, hitObject);
                break;
            case HitObject.SLIDER_HIT_OBJECT:
                //this.drawHitSliderObject(elapsedTime, hitObject);
                break;
            default:
                this.drawHitCircleObject(elapsedTime, hitObject);
                break;

        }
    }

    private void drawHitCircleObject(long elapsedTime, HitObject hitObject)
    {
        float hitObjectX = (float)hitObject.getCoords().get(0).x;
        float hitObjectY = (float)hitObject.getCoords().get(0).y;

        //draw the appraoch circle
        if(hitObject.getOffsetTime() >= elapsedTime)
        {
            Texture approachCircle = this.game.assetManager.get(this.game.ccsSkinManager.getCurrentCCSSkin().getLocation() + CCSSkin.APPROACH_CIRCLE, Texture.class);

            //The scale is the |currentTime - finalTime|/arTime * 3 + 1
            //TODO: make modular?
            float scaleRatio = (float)Math.abs(elapsedTime - hitObject.getOffsetTime())/(float)DifficultyTable.arDefaultTable[this.beatmap.getDifficultyData().getApproachRate()] * 2 + 1;
            Gdx.app.debug(BeatmapGraphicsManager.class.toString(), "Curent Ratio for " + hitObject.getOffsetTime() + " is: " + scaleRatio);

            this.batch.draw(approachCircle, hitObjectX - (approachCircle.getWidth() * scaleRatio)/2, hitObjectY - (approachCircle.getHeight() * scaleRatio)/2,
                    approachCircle.getWidth() * scaleRatio, approachCircle.getHeight() * scaleRatio);

        }

        //draw only the circle
        //TODO: overlay circle?
        Texture hitCircle = this.game.assetManager.get(this.game.ccsSkinManager.getCurrentCCSSkin().getLocation() + CCSSkin.HIT_CIRCLE, Texture.class);
        Gdx.app.debug(BeatmapGraphicsManager.class.toString(), "Attempting to draw:" + hitObject.getOffsetTime() + " with (" + hitObject.getCoords().get(0).x + ", " + hitObject.getCoords().get(0).y + ")" +  " at: " + (float)hitObject.getCoords().get(0).x + " " + (float)hitObject.getCoords().get(0).y );
        Gdx.app.debug(BeatmapGraphicsManager.class.toString(), "Elapsed Time " + elapsedTime);

        this.batch.draw(hitCircle,  hitObjectX - hitCircle.getWidth()/2, hitObjectY - hitCircle.getHeight()/2);
    }

    private void drawHitSliderObject(long elapsedTime, HitObject hitObject)
    {
        //Draws the circle before the slider
        this.drawHitCircleObject(elapsedTime, hitObject);

        //Draws slider
        switch(hitObject.getSliderType())
        {
            case "L":
                break;
            case "C":
                break;
            case "P":
                break;
            case "B":
                this.drawBezier(elapsedTime, hitObject);
                break;
            default:
                //TODO: add this back in?
                //this shouldn't happen, throw error
                //throw new Exception("Hit Object recognized as a slider but have invalid arguments");
        }
    }

    private void drawBezier(long elapsedTime, HitObject hitObject)
    {
        for(int i = 0; i < hitObject.getCurve().getCachedCalculatedPoints().size() - 1; i++)
        {
            //draw straight lines for now
	        this.shapeRenderer.line((float)hitObject.getCurve().getCachedCalculatedPoints().get(i).x,(float)hitObject.getCurve().getCachedCalculatedPoints().get(i)
			        .y,
                    (float)hitObject.getCurve().getCachedCalculatedPoints().get(i + 1).x, (float)hitObject.getCurve().getCachedCalculatedPoints().get(i + 1).y);

	        this.drawNormal(hitObject.getCurve().getCachedCalculatedDervPoints().get(i), hitObject.getCurve()
			        .getCachedCalculatedPoints().get(i));
        }
    }

	private void drawNormal(Vector2 slope, Vector2 location)
	{
		//point slope form is:
		//(y-y1) = m(x-x1)
		//Parametrize it and it becomes:
		//(y-y1)/m = (x-x1) = t
		//x = t + x1
		//y = tm + y1
		//Using t as from 10 to -10
//		float t = 0.1f;
//		float xUpper = t * (float)slope.x + (float)location.x;
//		float yUpper = t * (float)slope.y + (float)location.y;
//
//		float xLower = -1 * t * (float)slope.x + (float)location.x;
//		float yLower = -1 * t * (float)slope.y + (float)location.y;
//		this.shapeRenderer.line(xUpper, yUpper, xLower, yLower);

		//Try 2:
		//Vector2 rotatedVector21 = slope.getNormalizedVector2(Vector2.Vector2Type.LIBGDX).add(location).rotate(location, (float) (Math.PI/2));
		//Vector2 rotatedVector22 = slope.getNormalizedVector2(Vector2.Vector2Type.LIBGDX).add(location).rotate(location, (float) (-1 * Math.PI/2));

		//this.shapeRenderer.line(rotatedVector21.toGdxVector2(), rotatedVector22.toGdxVector2());
	}

    private void drawHitSpinnerObject(long elapsedTime, HitObject hitObject)
    {

    }
}
