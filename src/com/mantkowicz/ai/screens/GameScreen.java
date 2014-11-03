package com.mantkowicz.ai.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.Player;
import com.mantkowicz.ai.actors.Zombie;
import com.mantkowicz.ai.main.Main;
import com.mantkowicz.ai.mapLoader.MapLoader;
import com.mantkowicz.ai.vars.Vars;
import com.mantkowicz.listener.ContactListener;

public class GameScreen implements Screen 
{
	private final boolean DRAW_DEBUG = false;
	
	private ShapeRenderer shapeRenderer;
	
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
	
	private ContactListener contactListener;
	
	public GameScreen(Main game)
	{		
		zombies = new Array<Zombie>();
		columns = new Array<Column>();
		
		lastAngle = 0.0f;
		
		shapeRenderer = new ShapeRenderer();
		
		//Gdx.input.setCursorCatched(true);
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
				
		columns = MapLoader.getInstance().getColumns();
		
		for(Column column: columns)
		{
			column.setPlayer(player);
			
			stage.addActor(column);
		}
		
		zombies = MapLoader.getInstance().getZombies();
		
		for(Zombie zombie: zombies)
		{
			
			
			zombie.setEnvironment(player, columns, zombies, stage);
			
			stage.addActor(zombie);
		}
		
		//---Setting camera
		camera = (OrthographicCamera) stage.getCamera();
		camera.position.set( player.getX(), camera.position.y = player.getY(), 0.0f);
		camera.zoom = 1.0f;
		
		//---
		contactListener = new ContactListener(zombies, columns, player);
	}
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
						
		for(Zombie zombie: zombies)
		{			
			zombie.power = 0;
			
			for(int i = 0; i < zombies.size; i++)
			{
				Zombie neighbour = zombies.get(i);
				
				if( neighbour.equals(zombie) ) continue;
				
				Vector2 distance = new Vector2( zombie.getCenter().x - neighbour.getCenter().x, zombie.getCenter().y - neighbour.getCenter().y );
				
				if( distance.len() < 100.0f )
				{
					zombie.power++;
				}
			}
		}
		
		for(Zombie zombie: zombies)
		{
			if(zombie.power > 0)
			{
				//zombie.rage = true;
			}
		}
		
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
		
		if( DRAW_DEBUG )
		{
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Line);
	
			for(Zombie zombie: zombies)
			{
				shapeRenderer.setColor(1, 0, 0, 1);
				shapeRenderer.circle(zombie.getX() + (zombie.getWidth() / 2.0f), zombie.getY() + (zombie.getHeight() / 2.0f), zombie.getWidth() / 2.0f);
	
				shapeRenderer.setColor(0, 0, 1, 1);
				float[] vertices = zombie.getCollisionBoxVertices( zombie.controller.currentRotation );
				shapeRenderer.polygon(vertices);
			}
			
			for(Column column: columns)
			{
				shapeRenderer.setColor(1, 1, 1, 1);
				shapeRenderer.circle(column.getX() + (column.getWidth() / 2.0f), column.getY() + (column.getHeight() / 2.0f), column.getWidth() / 2.0f);
			
				shapeRenderer.setColor(0, 1, 0, 1);			
				shapeRenderer.circle( column.getSafePoint().x, column.getSafePoint().y, 2);		
			}
			
			shapeRenderer.setColor(1, 1, 0, 1);
			shapeRenderer.circle(player.getX() + (player.getWidth() / 2.0f), player.getY() + (player.getHeight() / 2.0f), player.getWidth() / 2.0f);
			
			shapeRenderer.end();
		}
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
