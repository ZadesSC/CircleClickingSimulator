package io.zades.ccs.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.zades.ccs.main.managers.BeatmapManager;
import io.zades.ccs.main.managers.CCSSkinManager;
import io.zades.ccs.main.managers.GameStateManager;

public class CCSCore implements ApplicationListener
{
	//Global game stuff
	public CCSSkinManager ccsSkinManager;
	public BeatmapManager beatmapManager;
	public GameStateManager gameStateManager;
	public AssetManager assetManager;

	//Camera and viewport
	public Viewport viewport;
	public Camera camera;

	Texture texture;
	public SpriteBatch batch;
	float elapsed;

	FPSLogger fpsLogger;

	//For debugging
	public Sprite backgroundSprite600p;
	public Sprite backgroundSprite480p;
	public Sprite backgroundSprite384p;
	public Sprite originCircleSprite;

	@Override
	public void create ()
	{
		//Cross platform application settings go here
		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		this.ccsSkinManager = new CCSSkinManager(this);
		this.beatmapManager = new BeatmapManager(this);
		this.gameStateManager = new GameStateManager(this);
		this.assetManager = new AssetManager();

		//add the fucking camera and viewport shit
		this.camera = new OrthographicCamera();
		this.viewport = new ExtendViewport(800, 600, this.camera);
		this.viewport.apply();
		//this.camera.position.set(320,240,0);

		this.gameStateManager.initStates();

		this.batch = new SpriteBatch();

		this.fpsLogger = new FPSLogger();

		//debug
		this.backgroundSprite600p = new Sprite(new Texture(Gdx.files.local("assets/data/background600p.png")));
		this.backgroundSprite480p = new Sprite(new Texture(Gdx.files.local("assets/data/background480p.png")));
		this.backgroundSprite384p = new Sprite(new Texture(Gdx.files.local("assets/data/background384p.png")));
		this.originCircleSprite = new Sprite(new Texture(Gdx.files.local("assets/data/originCircle.png")));

		this.backgroundSprite600p.setPosition(-1 * (this.backgroundSprite600p.getWidth() - this.backgroundSprite384p.getWidth())/2, -1 * (this.backgroundSprite600p.getHeight() - this.backgroundSprite384p.getHeight())/2);
		this.backgroundSprite480p.setPosition(-1 * (this.backgroundSprite480p.getWidth() - this.backgroundSprite384p.getWidth())/2, -1 * (this.backgroundSprite480p.getHeight() - this.backgroundSprite384p.getHeight())/2);
		this.backgroundSprite384p.setPosition(0, 0);
		this.originCircleSprite.setPosition(-1 * this.originCircleSprite.getWidth() / 2, -1 * this.originCircleSprite.getHeight() / 2);

		//this.camera.position.set(this.camera.viewportWidth / 2, this.camera.viewportHeight / 2, 0);
		this.camera.position.set(256,192, 0);
	}

	@Override
	public void resize (int width, int height)
	{
		this.viewport.update(width, height);
	}

	@Override
	public void render ()
	{
		//Clears the screen
		elapsed += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		this.camera.update();

		//Debug shit
		this.batch.begin();
		this.batch.setProjectionMatrix(this.camera.combined);
		this.backgroundSprite600p.draw(this.batch);
		this.backgroundSprite480p.draw(this.batch);
		this.backgroundSprite384p.draw(this.batch);
		this.originCircleSprite.draw(this.batch);
		this.batch.end();

		this.gameStateManager.getCurrentState().render(Gdx.graphics.getDeltaTime());

		this.fpsLogger.log();
	}

	@Override
	public void pause ()
	{
	}

	@Override
	public void resume ()
	{
	}

	@Override
	public void dispose ()
	{
	}
}
