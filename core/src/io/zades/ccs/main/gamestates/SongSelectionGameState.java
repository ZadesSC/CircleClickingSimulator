package io.zades.ccs.main.gamestates;

import io.zades.ccs.main.CCSCore;
import io.zades.ccs.main.managers.GameStateManager;

/**
 * Created by Darren on 10/26/2014.
 */
public class SongSelectionGameState extends AbstractGameState
{

	public SongSelectionGameState(CCSCore game)
	{
		super(GameStateManager.GAME_STATE.SONG_SELECTION, game);

	}

	@Override
	public void render(float delta)
	{
		super.render(delta);
	}

	/**
	 * @param width
	 * @param height
	 * @see com.badlogic.gdx.ApplicationListener#resize(int, int)
	 */
	@Override
	public void resize(int width, int height)
	{

	}

	/**
	 * Called when this screen becomes the current screen for a {@link com.badlogic.gdx.Game}.
	 */
	@Override
	public void show()
	{

	}

	/**
	 * Called when this screen is no longer the current screen for a {@link com.badlogic.gdx.Game}.
	 */
	@Override
	public void hide()
	{

	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#pause()
	 */
	@Override
	public void pause()
	{

	}

	/**
	 * @see com.badlogic.gdx.ApplicationListener#resume()
	 */
	@Override
	public void resume()
	{

	}

	/**
	 * Called when this screen should release all resources.
	 */
	@Override
	public void dispose()
	{

	}
}
