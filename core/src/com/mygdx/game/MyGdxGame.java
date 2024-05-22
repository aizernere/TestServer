package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.io.IOException;
import java.util.Map;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture playerTexture;
	private Client client;
	private Vector2 playerPosition;
	private int playerId;

	public MyGdxGame(int playerId, Vector2 playerPosition) {
		this.playerPosition = playerPosition;
		this.playerId = playerId;
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		playerTexture = new Texture("badlogic.jpg");

	}

	@Override
	public void render() {
		handleInput();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(playerTexture,  playerPosition.x, playerPosition.y);
		batch.end();
	}

	private void handleInput() {

		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.LEFT)) playerPosition.x -= 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.RIGHT)) playerPosition.x += 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.UP)) playerPosition.y += 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.DOWN)) playerPosition.y -= 200 * Gdx.graphics.getDeltaTime();


	}

	@Override
	public void dispose() {
		batch.dispose();
		playerTexture.dispose();
	}
}