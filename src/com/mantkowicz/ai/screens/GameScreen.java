package com.mantkowicz.ai.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.Zombie;
import com.mantkowicz.ai.listener.ContactListener;
import com.mantkowicz.ai.main.Main;
import com.mantkowicz.ai.mapLoader.MapLoader;
import com.mantkowicz.ai.vars.CollisionSide;
import com.mantkowicz.ai.vars.Vars;
import com.mantkowicz.ai.world.World;

public class GameScreen implements Screen 
{
	private final boolean DRAW_DEBUG = false;
	
	private ShapeRenderer shapeRenderer;
	
	private TiledMapRenderer mapRenderer;
	
	private World world;
	
	private Stage stage;
	private Viewport viewport;
	
	private OrthographicCamera camera;

	float mapWidth;
	float mapHeight;
	
	private float lastAngle;
	
	private ContactListener contactListener;
	
	private FPSLogger fpsLogger = new FPSLogger();
	
	public GameScreen(Main game)
	{				
		lastAngle = 0.0f;
		
		shapeRenderer = new ShapeRenderer();
		
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
		
		world = World.getInstance();
		world.initialize(stage, MapLoader.getInstance().getColumns(), MapLoader.getInstance().getZombies(), MapLoader.getInstance().getPlayer(), MapLoader.getInstance().getWorldCenter());
		
		//---Setting camera
		camera = (OrthographicCamera) stage.getCamera();
		camera.position.set( world.player.getX(), camera.position.y = world.player.getY(), 0.0f);
		camera.zoom = 2.0f;
		
		//---
		contactListener = new ContactListener();
	}
	
	@Override
	public void render(float delta) 
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		contactListener.update();
		
		handleZombieRage();
		
		mapRenderer.setView(camera.combined, 0, 0, mapWidth, mapHeight);

		handleInput();
		
		//---Moving and rotating camera
		camera.position.set(world.player.getX() + ( world.player.getWidth() / 2.0f ), world.player.getY() + ( world.player.getHeight() / 2.0f ), 0);
		camera.rotate( lastAngle - world.player.rotation );
		camera.update();
		//---
		
		lastAngle = world.player.rotation;
		
		mapRenderer.render();
		
		stage.act();
		stage.draw();
		
		if( DRAW_DEBUG )
		{
			fpsLogger.log();
			
			shapeRenderer.setProjectionMatrix(camera.combined);
			shapeRenderer.begin(ShapeType.Line);
	
			for(int i = 0; i < world.zombies.size; i++)
			{
				Zombie zombie = world.zombies.get(i);
				
				shapeRenderer.setColor(1, 0, 0, 1);
				shapeRenderer.circle(zombie.getCenter().x, zombie.getCenter().y, zombie.getRadius());
				
				shapeRenderer.setColor(1, 0, 1, 1);
				shapeRenderer.circle(zombie.getCenter().x, zombie.getCenter().y, Vars.DISTANCE / 2.0f);
	
				shapeRenderer.setColor(0, 0, 1, 1);

				if(contactListener.checkCollision(zombie).columnCollisionSides.contains(CollisionSide.PRELEFT, true))
				{
					shapeRenderer.setColor(1, 1, 0, 1);
				}
				
				Vector2 left = contactListener.getLeftCollisionPoint(zombie, new Vector2(0,0));
				Vector2 right = contactListener.getRightCollisionPoint(zombie, new Vector2(0,0));
				
				shapeRenderer.circle(left.x, left.y, 2.0f);
				
				if(contactListener.checkCollision(zombie).columnCollisionSides.contains(CollisionSide.PRERIGHT, true))
				{
					shapeRenderer.setColor(1, 1, 0, 1);
				}

				shapeRenderer.circle(right.x, right.y, 2.0f);
			}
			
			for(Column column: world.columns)
			{
				shapeRenderer.setColor(1, 1, 1, 1);
				shapeRenderer.circle(column.getCenter().x, column.getCenter().y, column.getRadius());
			
				shapeRenderer.setColor(0, 1, 0, 1);			
				shapeRenderer.circle( column.getSafePoint().x, column.getSafePoint().y, 2);		
			}
			
			shapeRenderer.setColor(1, 1, 0, 1);
			shapeRenderer.circle(world.player.getCenter().x, world.player.getCenter().y, world.player.getRadius());
		
			shapeRenderer.setColor(1, 1, 1, 1);
			shapeRenderer.circle(world.worldCenter.x, world.worldCenter.y, Vars.ZOMBIE_AREA_WIDTH);
			
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

	private void handleZombieRage()
	{
		for(int i = 0; i < world.zombies.size; i++)
		{
			world.zombies.get(i).updateNeighbours();
		}
		
		world.zombies.sort();

		for(int i = 0; i < world.zombies.size; i++)
		{
			world.zombies.get(i).updateNeighbours();
		
			if( world.zombies.get(i).isNeighbourInRage || world.zombies.get(i).neighbours >= Vars.MIN_GROUP_SIZE || world.zombies.size <= Vars.MIN_GROUP_SIZE + 1 )
			{
				world.zombies.get(i).rage = true;
			}
		}
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
