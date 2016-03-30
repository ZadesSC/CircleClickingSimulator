package io.zades.ccs.main.gamestates;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.zades.ccs.main.CCSCore;
import io.zades.ccs.main.managers.GameStateManager;

/**
 * Created by Darren on 10/22/2014.
 */
public abstract class AbstractGameState extends Stage implements Screen
{
	private GameStateManager.GAME_STATE gameState;
	protected CCSCore game;

	public AbstractGameState(GameStateManager.GAME_STATE gameState, CCSCore game)
	{
		this.gameState = gameState;
		this.game = game;
	}

	//method called when entering state
	public void enterState()
	{

	}

	//method called when exiting this state
	public void exitState()
	{

	}

	@Override
	public void render(float delta)
	{
		// the following code clears the screen with the given RGB color (black)
		//moved to CCSCore
		//Gdx.gl.glClearColor( 0f, 0f, 0f, 1f );
		//Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );

		//Gdx.gl.glClearColor(0, 0, 0, 0);
		//Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		this.game.batch.begin();
		this.game.batch.setProjectionMatrix(this.game.camera.combined);
		this.game.backgroundSprite480p.draw(this.game.batch);
		this.game.batch.end();

	}

	public GameStateManager.GAME_STATE getGameState()
	{
		return gameState;
	}
}
