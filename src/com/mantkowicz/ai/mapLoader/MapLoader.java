package com.mantkowicz.ai.mapLoader;

import java.util.Iterator;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.Player;
import com.mantkowicz.ai.actors.Zombie;
import com.mantkowicz.ai.vars.Vars;

public class MapLoader 
{		
	private static MapLoader INSTANCE = new MapLoader();	
	public static MapLoader getInstance(){ return INSTANCE; }
	
	private OrthogonalTiledMapRenderer tiledMapRenderer;	
	private TiledMap tiledMap;
	
	Array<Zombie> zombies;
	Array<Column> columns;
	Player player;
	
	public void loadMap(String mapPath)
	{	
		tiledMap = new TmxMapLoader().load( mapPath );
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1.0f);
		
		zombies = new Array<Zombie>();
		columns = new Array<Column>();
		
		createActors(tiledMap);
	}
	
	private void createActors(TiledMap map) 
	{	
		MapLayers layers = map.getLayers();
		Iterator<MapLayer> layersIt = layers.iterator();
		
		while(layersIt.hasNext())
		{
			MapLayer layer = layersIt.next();
			
			MapObjects objects = layer.getObjects();
			Iterator<MapObject> objectIt = objects.iterator();
			
			while(objectIt.hasNext()) 
			{
				MapObject mapObject = objectIt.next();
				
				if (mapObject instanceof TextureMapObject)
				{
					continue;
				}
				else
				{
					float x = Float.parseFloat( mapObject.getProperties().get("x").toString() );
					float y = Float.parseFloat( mapObject.getProperties().get("y").toString() );
					
					if( checkObjectType(mapObject, "zombie") )
					{
						zombies.add( new Zombie(x, y) );
					}
					else if( checkObjectType(mapObject, "column") )
					{
						columns.add( new Column(x, y) );
					}
					else if( checkObjectType(mapObject, "player") )
					{
						player = new Player(x, y);
					}
					
					ShapeRenderer sh = new ShapeRenderer();
					sh.begin(ShapeType.Filled);
					
				}
			}
		}
	}
	
	public Array<Zombie> getZombies()
	{
		return zombies;
	}
	
	public Array<Column> getColumns()
	{
		return columns;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Vector2 getMapSize()
	{
		MapProperties mapProperties = tiledMap.getProperties();
		
		int mapWidth = mapProperties.get("width", Integer.class);
		int mapHeight = mapProperties.get("height", Integer.class);
		int tilePixelWidth = mapProperties.get("tilewidth", Integer.class);
		int tilePixelHeight = mapProperties.get("tileheight", Integer.class);

		int mapPixelWidth = mapWidth * tilePixelWidth;
		int mapPixelHeight = mapHeight * tilePixelHeight;

		return new Vector2(mapPixelWidth, mapPixelHeight);
	}
	
	private boolean isPropertyTrue(TiledMap map, String propertyKey)
	{
		if( "true".equals((String)map.getProperties().get( propertyKey )) ) return true;
		return false;
	}
	private boolean isPropertyTrue(MapLayer layer, String propertyKey)
	{
		if( "true".equals((String)layer.getProperties().get( propertyKey )) ) return true;
		return false;
	}
	private boolean isPropertyTrue(MapObject object, String propertyKey)
	{
		if( "true".equals((String)object.getProperties().get( propertyKey )) ) return true;
		return false;
	}
	
	private boolean checkObjectType(MapObject object, String typeName)
	{
		if( typeName.equals((String)object.getProperties().get( "type" )) ) return true;
		return false;
	}
	
	public OrthogonalTiledMapRenderer getMapRenderer()
	{ 
		return tiledMapRenderer; 
	}
}