package io.zades.ccs.main.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import io.zades.ccs.main.CCSCore;
import io.zades.ccs.main.io.OsuBeatmapFileParser;
import io.zades.ccs.main.objects.beatmaps.Beatmap;

import java.util.HashMap;

/**
 * Created by Darren on 10/22/2014.
 */
public class BeatmapManager
{
	public static final String BEATMAP_PATH = "Beatmaps/";

	private static BeatmapManager instance;
	private CCSCore game;

	private HashMap<FileHandle, Beatmap> listOfBeatmapsByDirectory;

	private BeatmapManager()
	{
		super();
	}

	private BeatmapManager(CCSCore game)
	{
		this.game = game;
		this.setListOfBeatmapsByDirectory(new HashMap<FileHandle, Beatmap>());
	}

	public static BeatmapManager getInstance()
	{
		if(instance == null)
		{
			instance = new BeatmapManager();
		}
		return instance;
	}

	public static BeatmapManager initialize(CCSCore game)
	{
		if(instance == null)
		{
			instance = new BeatmapManager(game);
		}
		return instance;
	}

	public void initAllBeatmaps()
	{

		//TODO: error check for the used directory (is a directory, etc)
		if(!this.locateBeatmapDirectory().exists() || !this.locateBeatmapDirectory().isDirectory())
		{
			Gdx.app.debug(BeatmapManager.class.toString(), "Beatmaps folder doesn't exist, generating...");
			this.locateBeatmapDirectory().mkdirs();
		}
		else
		{
			Gdx.app.debug(BeatmapManager.class.toString(), "Beatmaps folder exists, moving on...");
		}

		FileHandle[] files = this.locateBeatmapDirectory().list();

		for(FileHandle file: files)
		{
			if(file.isDirectory())
			{
				//TODO: loading the files needs to be done via asset manager
				Gdx.app.debug(BeatmapManager.class.toString(), "Folder: " + file.toString());

				//TODO: move to loader
				FileHandle[] osuFiles = file.list(".osu");
				for(FileHandle osuFile: osuFiles)
				{
					try
					{
						Gdx.app.debug(BeatmapManager.class.toString(), "File: " + osuFile.toString());

						Beatmap beatmap = new Beatmap();
						OsuBeatmapFileParser parser = new OsuBeatmapFileParser(this.game);

							beatmap = parser.parseFile(beatmap, osuFile);

						beatmap.setDirectoryLocation(file.toString());
						beatmap.setFileLocation(osuFile.toString());
						this.getListOfBeatmapsByDirectory().put(osuFile, beatmap);
						//Gdx.app.debug(BeatmapManager.class.toString(), new Json().prettyPrint(beatmap));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
	}

	private FileHandle locateBeatmapDirectory()
	{
		//TODO: change it so users can change the directory, which is going to be a pain in the ass
		return Gdx.files.local(BEATMAP_PATH);
	}

	public HashMap<FileHandle, Beatmap> getListOfBeatmapsByDirectory() {
		return listOfBeatmapsByDirectory;
	}

	public void setListOfBeatmapsByDirectory(HashMap<FileHandle, Beatmap> listOfBeatmapsByDirectory) {
		this.listOfBeatmapsByDirectory = listOfBeatmapsByDirectory;
	}
}
