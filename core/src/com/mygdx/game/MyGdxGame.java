package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyGdxGame extends ApplicationAdapter {

	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private Client client;
	private PlayerPosition myPosition;
	private List<PlayerPosition> otherPlayerPositions;
	private boolean connected;

	public MyGdxGame(int id, float x, float y) {
		this.myPosition = new PlayerPosition(id,x,y);
	}

	public void create () {
		otherPlayerPositions = new ArrayList<>();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 600);
		shapeRenderer = new ShapeRenderer();

		client = new Client();
		client.start();

		Kryo kryo = client.getKryo();
		kryo.register(PlayerPosition.class);

			try {
				client.connect(5000, "localhost", 54555, 54777);
				connected = true;
			} catch (IOException e) {
				Gdx.app.log("GameClient", "Unable to connect to server: " + e.getMessage());
				Gdx.app.exit();
			}


		client.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof PlayerPosition) {
					PlayerPosition position = (PlayerPosition) object;
					updatePlayerPosition(position);
				}
			}
		});
	}

	private void updatePlayerPosition(PlayerPosition position) {
		boolean updated = false;
		for (PlayerPosition pos : otherPlayerPositions) {
			if (pos.id == position.id) {
				pos.x = position.x;
				pos.y = position.y;
				updated = true;
				break;
			}
		}
		if (!updated) {
			otherPlayerPositions.add(position);
		}
	}

	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		handleInput();
		camera.update();

		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		//update MY position
		//shapeRenderer.rect(myPosition.x, myPosition.y, 20, 20);

		//update Other Position
		for (PlayerPosition position : otherPlayerPositions) {
			if(position.id!= myPosition.id){
				shapeRenderer.rect(position.x, position.y, 20, 20);
			}
		}



		shapeRenderer.end();
	}

	private void handleInput() {
		float speed = 200 * Gdx.graphics.getDeltaTime();
		if (Gdx.input.isKeyPressed(Input.Keys.W)) myPosition.y += speed;
		if (Gdx.input.isKeyPressed(Input.Keys.S)) myPosition.y -= speed;
		if (Gdx.input.isKeyPressed(Input.Keys.A)) myPosition.x -= speed;
		if (Gdx.input.isKeyPressed(Input.Keys.D)) myPosition.x += speed;

		// Send updated position to server
		client.sendTCP(myPosition);
	}


	public void dispose () {
		shapeRenderer.dispose();
		client.stop();
	}
}

