package com.mantkowicz.ai.vars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.mantkowicz.ai.actors.GameObject;

public class Vars 
{
	public static float PPM = 64.0f;
	public static float WIDTH = Gdx.graphics.getWidth();
	public static float HEIGHT = Gdx.graphics.getHeight();
	
	public static final float ZOMBIE_AREA_WIDTH = 2000.0f;
	
	public static int MIN_GROUP_SIZE = 2;
	
	public static int DISTANCE = 200;
	
	public static float getDistance(GameObject a, GameObject b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.getCenter().x, a.getCenter().y - b.getCenter().y );
		
		return distance.len();
	}
	public static float getDistance(Vector2 a, Vector2 b)
	{
		Vector2 distance = new Vector2( a.x - b.x, a.y - b.y );
		
		return distance.len();
	}
	public static float getDistance(GameObject a, Vector2 b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.x, a.getCenter().y - b.y );
		
		return distance.len();
	}
	public static float getDistance(Vector2 a, GameObject b)
	{
		Vector2 distance = new Vector2( a.x - b.getCenter().x, a.y - b.getCenter().y );
		
		return distance.len();
	}
	
	public static float getAngle(GameObject a, GameObject b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.getCenter().x, a.getCenter().y - b.getCenter().y );
		
		return distance.angle();
	}
	public static float getAngle(Vector2 a, Vector2 b)
	{
		Vector2 distance = new Vector2( a.x - b.x, a.y - b.y );
		
		return distance.angle();
	}
	public static float getAngle(GameObject a, Vector2 b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.x, a.getCenter().y - b.y );
		
		return distance.angle();
	}
	public static float getAngle(Vector2 a, GameObject b)
	{
		Vector2 distance = new Vector2( a.x - b.getCenter().x, a.y - b.getCenter().y );
		
		return distance.angle();
	}
}
