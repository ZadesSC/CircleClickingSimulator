package io.zades.ccs.main.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import io.zades.ccs.main.CCSCore;
import io.zades.ccs.main.math.Bezier;
import io.zades.ccs.main.objects.beatmaps.Beatmap;
import io.zades.ccs.main.objects.HitObject;
import io.zades.ccs.main.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Darren on 10/26/2014.
 */
public class OsuBeatmapFileParser
{
	public static final String OSU_VERSION_PATTERN		= "\\Aosu file format v\\(d+\\)\\z";
	public static final String SECTION_PATTERN			= "\\A\\[[a-zA-z0-9]+\\]\\z";
	public static final String KEY_GROUP_NAME			= "KEY";
	public static final String VALUE_GROUP_NAME			= "VALUE";
	public static final String KV_PATTERN				= "\\A(?<" + KEY_GROUP_NAME + ">.+)\\s*:\\s*(?<" + VALUE_GROUP_NAME + ">.+)\\s*\\z";


	public static final String BLANK_SECTION			= "";
	public static final String GENERAL_SECTION 			= "[General]";
	public static final String EDITOR_SECTION 			= "[Editor]";
	public static final String METADATA_SECTION 		= "[Metadata";
	public static final String DIFFICULTY_SECTION 		= "[Difficulty]";
	public static final String EVENTS_SECTION 			= "[Events]";
	public static final String TIMING_POINTS_SECTIONS 	= "[TimingPoints]";
	public static final String COLORS_SECTIONS 			= "[Colors]";
	public static final String HIT_OBJECTS_SECTION 		= "[HitObjects]";

	private CCSCore game;

	private List<String> objectLines;
	private List<String> timingLines ;
	private List<String> eventLines;
	private List<String> kvLines;

	private String currentSection;

	public OsuBeatmapFileParser(CCSCore game)
	{
		this.game = game;

		this.objectLines = new ArrayList<>();
		this.timingLines = new ArrayList<>();
		this.eventLines = new ArrayList<>();
		this.kvLines = new ArrayList<>();

		this.currentSection = "";
	}

	public Beatmap parseFile(Beatmap beatmap, FileHandle fileh) throws Exception
	{
		//sanity checks
		if(beatmap == null)
		{
			beatmap = new Beatmap();
		}

		String[] readLines = fileh.readString().split("\n");

		for(String line: readLines)
		{
			line = line.trim();

			if(line.equals("") || line == null)
			{
				continue;
			}

			if (currentSection.equals("") && beatmap.getOsuFileVersion() == -1)
			{
				Pattern pattern = Pattern.compile(OSU_VERSION_PATTERN);
				Matcher matcher = pattern.matcher(line);
				if(matcher.find())
				{
					beatmap.setOsuFileVersion(Integer.parseInt(matcher.group()));
					continue;
				}
			}

			Pattern sectionPattern = Pattern.compile(SECTION_PATTERN);
			Matcher sectionsMatcher = sectionPattern.matcher(line);
			if(sectionsMatcher.matches())
			{
				currentSection = line;
			}

			switch (currentSection)
			{
				case BLANK_SECTION:
					break;
				case EVENTS_SECTION:
					eventLines.add(line);
					break;
				case TIMING_POINTS_SECTIONS:
					timingLines.add(line);
					break;
				case HIT_OBJECTS_SECTION:
					objectLines.add(line);
					break;
				case GENERAL_SECTION:
					break;
				case METADATA_SECTION:
					break;
				case DIFFICULTY_SECTION:
					break;
				case COLORS_SECTIONS:
					break;
				default:
					kvLines.add(line);
					break;
			}
		}

		this.parseKVPairs(beatmap, kvLines);
		this.parseHitObjects(beatmap, objectLines);

		return beatmap;
	}

	private void parseKVPairs(Beatmap beatmap, List<String> lines)
	{
		Pattern pattern = Pattern.compile(KV_PATTERN);

		for(String line: lines)
		{
			line = line.trim();

			Matcher matcher = pattern.matcher(line);
			if(matcher.find())
			{
				String key = matcher.group(KEY_GROUP_NAME);
				String value = matcher.group(VALUE_GROUP_NAME);

				if (beatmap.getKvPairs().containsKey(key))
				{
					Gdx.app.log(OsuBeatmapFileParser.class.toString(), "WARNING: Key " + key + " already exists in beatmap");
				} else
				{
					beatmap.getKvPairs().put(key, value);
				}
			}
		}
	}

	private void parseHitObjects(Beatmap beatmap, List<String> lines) throws Exception
	{
		for(String line: lines)
		{
			line = line.trim();

			HitObject hitObject = this.parseHitObject(line);
			beatmap.getHitObjects().add(hitObject);
		}
	}

	private HitObject parseHitObject(String line) throws Exception
	{
		//TODO: finish this shit
		//x, y, offset, type, hitsound, additions
		//x, y, offset, type, hitsound, slidertype, points (|x:y|x:y,...), additions
		//x, y, offset, type, hitsound, endoffset, addition
		//new combo as well as the type is encoded as bits
		//1 = circle
		//2 = slider
		//4 = new combo
		//8 = spinner

		String[] hitObjectParts = line.split(",");
		for(String str: hitObjectParts)
		{
			str = str.trim();
		}
		if(hitObjectParts.length < 6)
			{
			return null;
		}
		Vector2 convertedCoords = this.fromOsuToCCSCoordinates(new Vector2(Float.parseFloat(hitObjectParts[0]), Float.parseFloat(hitObjectParts[1])));

		int offset = Integer.parseInt(hitObjectParts[2]);
		int type = Integer.parseInt(hitObjectParts[3]);
		int hitSound = Integer.parseInt(hitObjectParts[4]);
		boolean newCombo = ((type & HitObject.NEW_COMBO) == HitObject.NEW_COMBO);

		HitObject obj = new HitObject();
		obj.getCoords().add(convertedCoords);
		obj.setOffsetTime(offset);
		obj.setHitSoundType(hitSound);
		obj.setNewCombo(newCombo);

		if((type & HitObject.CIRCLE_HIT_OBJECT) ==  HitObject.CIRCLE_HIT_OBJECT)
		{
			obj.setHitObjectType(HitObject.CIRCLE_HIT_OBJECT);
			//TODO: parse additions
		}
		else if((type & HitObject.SLIDER_HIT_OBJECT) ==  HitObject.SLIDER_HIT_OBJECT)
		{
			obj.setHitObjectType(HitObject.SLIDER_HIT_OBJECT);

			//Extracts from L|xxx:yyy|xxx:yyy etc
			String[] sliderCoords = hitObjectParts[5].split("\\|");

			//Adding the additional vector
			for(int x = 1; x < sliderCoords.length; x++)
			{
				String[] pairCoords =  sliderCoords[x].split(":");
				if(pairCoords.length != 2)
				{
					throw new Exception("Hit Object recognized as a slider but have invalid coordinates");
				}

				Vector2 convertedSliderCoords = this.fromOsuToCCSCoordinates(new Vector2(Float.parseFloat(pairCoords[0]), Float.parseFloat(pairCoords[1])));
				obj.getCoords().add(convertedSliderCoords);
			}

			Gdx.app.debug(this.getClass().toString(), "Number of Vector2: " + obj.getCoords().size());

			//Set slider type and add the path
			//TODO: add the breaks back in
			switch(sliderCoords[0])
			{
				case "L":
					obj.setSliderType(HitObject.LINEAR_SLIDER);
					//break;
				case "C":
					obj.setSliderType(HitObject.CATMULL_SLIDER);
					//break;
				case "P":
					obj.setSliderType(HitObject.PASS_THROUGH_SLIDER);
					//break;
				case "B":
					obj.setSliderType(HitObject.BEZIER_SLIDER);
					obj.setCurve(new Bezier(obj.getCoords()));
					break;
				default:
					//this shouldn't happen, throw error
					throw new Exception("Hit Object recognized as a slider but have invalid arguments");
			}

			//TODO: parse additions
			//TODO: parse edge crap
		}
		else if((type & HitObject.SPINNER_HIT_OBJECT) ==  HitObject.SPINNER_HIT_OBJECT)
		{
			obj.setHitObjectType(HitObject.SPINNER_HIT_OBJECT);
			//TODO: parse additions
		}
		else
		{
			return null;
		}

		//TODO: sort hitobjects just in case, maybe
		return obj;
	}

	public Vector2 fromOsuToCCSCoordinates(Vector2 osuCoords)
	{
		return new Vector2(osuCoords.x, World.PLAY_FIELD_HEIGHT - osuCoords.y);
	}
}