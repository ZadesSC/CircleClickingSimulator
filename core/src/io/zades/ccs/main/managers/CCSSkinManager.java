package io.zades.ccs.main.managers;

import io.zades.ccs.main.CCSCore;
import io.zades.ccs.main.loaders.CCSSkinLoader;
import io.zades.ccs.main.objects.CCSSkin;

import java.util.HashMap;

/**
 * Created by Darren on 10/21/2014.
 */
public class CCSSkinManager
{
	public static final String DEFAULT_SKIN_PATH = "assets/data/default-skin/";
	public static final String SKIN_PATH = "Skins/";

	private static CCSSkinManager instance;

	private CCSCore game;
	private CCSSkin currentCCSSkin;
	private CCSSkin defaultCCSSkin;

	//The list of skins by their directory, which should be unique
	private HashMap<String, CCSSkin> listOfSkinsByDirectory;

	private CCSSkinManager()
	{
		super();
	}
	private CCSSkinManager(CCSCore game)
	{
		this.game = game;
		this.setListOfSkinsByDirectory(new HashMap<String, CCSSkin>());
	}

	public static CCSSkinManager getInstance()
	{
		if(instance == null)
		{
			instance = new CCSSkinManager();
		}
		return instance;
	}

	public static CCSSkinManager initialize(CCSCore game)
	{
		if(instance == null)
		{
			instance = new CCSSkinManager(game);
		}
		return instance;
	}

	public void initAllSkins()
	{
		this.initDefaultSkin();
		this.setCurrentCCSSkin(getDefaultCCSSkin());
		//TODO: init other skins
	}

	private CCSSkin initDefaultSkin()
	{
		CCSSkin defaultSkin = new CCSSkin(DEFAULT_SKIN_PATH);
		CCSSkinLoader.loadSkin(defaultSkin, this.game);

		this.setDefaultCCSSkin(defaultSkin);
		this.getListOfSkinsByDirectory().put(defaultSkin.getLocation(), defaultSkin);

		return defaultSkin;
	}

	private CCSSkin initSkin()
	{
		//TODO: finish method
		//TODO: collision detection of skin names
		return null;
	}

	public CCSSkin getCurrentCCSSkin()
	{
		return currentCCSSkin;
	}

	public void setCurrentCCSSkin(CCSSkin currentCCSSkin)
	{
		this.currentCCSSkin = currentCCSSkin;
	}

	public CCSSkin getDefaultCCSSkin()
	{
		return defaultCCSSkin;
	}

	public void setDefaultCCSSkin(CCSSkin defaultCCSSkin)
	{
		this.defaultCCSSkin = defaultCCSSkin;
	}

	public HashMap<String, CCSSkin> getListOfSkinsByDirectory()
	{
		return listOfSkinsByDirectory;
	}

	public void setListOfSkinsByDirectory(HashMap<String, CCSSkin> listOfSkinsByDirectory)
	{
		this.listOfSkinsByDirectory = listOfSkinsByDirectory;
	}
}
