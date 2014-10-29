package com.mantkowicz.ai.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.Player;
import com.mantkowicz.ai.actors.Zombie;
import com.mantkowicz.ai.controller.MouseController;
import com.mantkowicz.ai.logger.Logger;
import com.mantkowicz.ai.main.Main;
import com.mantkowicz.ai.mapLoader.MapLoader;
import com.mantkowicz.ai.vars.Vars;

public class GameScreen implements Screen 
{
	private TiledMapRenderer mapRenderer;
	
	private Stage stage;
	private Viewport viewport;
	
	private OrthographicCamera camera;
		
	private Array<Zombie> zombies;
	private Array<Column> columns;
	private Player player;
	
	float mapWidth;
	float mapHeight;
	
	private float lastAngle;
	
	public GameScreen(Main game)
	{		
		zombies = new Array<Zombie>();
		columns = new Array<Column>();
		
		lastAngle = 0.0f;
		
		Gdx.input.setCursorCatched(true);
	}
	
	@Override
	public void show() 
	{
		MapLoader.getInstance().loadMap( "maps/map.tmx" );
		
		mapWidth = MapLoader.getInstance().getMapSize().x;
		mapHeight = MapLoader.getInstance().getMapSize().y;
		
		mapRenderer = MapLoader.getInstance().getMapRenderer();
		
		this.stage = new Stage();
		this.viewport = new StretchViewport(Vars.WIDTH, Vars.HEIGHT);
		
		stage.setViewport(viewport);
		
		//---Adding actors to stage
		player = MapLoader.getInstance().getPlayer();
		stage.addActor(player);
		
		zombies = MapLoader.getInstance().getZombies();
		
		for(Zombie zombie: zombies)
		{
			zombie.setPlayer(player);
			stage.addActor(zombie);
		}
		
		columns = MapLoader.getInstance().getColumns();
		
		for(Column column: columns)
		{
			stage.addActor(column);
		}
		
		//---Setting camera
		camera = (OrthographicCamera) stage.getCamera();
		camera.position.set( player.getX(), camera.position.y = player.getY(), 0.0f);
		camera.zoom = 1.0f;
	}
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
						
		mapRenderer.setView(camera.combined, 0, 0, mapWidth, mapHeight);

		handleInput();
		
		//---Moving and rotating camera
		camera.position.set(player.getX() + ( player.getWidth() / 2.0f ), player.getY() + ( player.getHeight() / 2.0f ), 0);
		camera.rotate( lastAngle - player.rotation );
		camera.update();
		//---
		
		lastAngle = player.rotation;
		
		mapRenderer.render();
		
		stage.act();
		stage.draw();
	}

	public void handleInput()
	{	
		//---Camera zooming
		if( Gdx.input.isKeyPressed(Keys.C) ) camera.zoom = 1.0f;
		if( Gdx.input.isKeyPressed(Keys.Z) ) camera.zoom += 0.1f;
		if( Gdx.input.isKeyPressed(Keys.X) && camera.zoom >= 0.3f ) camera.zoom -= 0.1;
		//---
		
		//---Handling exit
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) )
		{
			Gdx.app.exit();
		}
		//---
	}

	@Override
	public void resize(int width, int height) 
	{
		viewport.update(width, height);
	}

	@Override
	public void hide() 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void dispose() 
	{
		stage.dispose();	
	}

}
