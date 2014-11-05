package com.mantkowicz.ai.listener;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.Bullet;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.GameObject;
import com.mantkowicz.ai.actors.Zombie;
import com.mantkowicz.ai.logger.Logger;
import com.mantkowicz.ai.vars.CollisionSide;
import com.mantkowicz.ai.world.World;

public class ContactListener 
{	
	public ContactListener()
	{
	}
	
	public void update()
	{	
		for(Bullet bullet: World.getInstance().bullets)
		{
			for(Column column: World.getInstance().columns)
			{
				float distance = getDistance(bullet, column);

				if( distance < (column.getRadius() + 5.0f) )
				{
					World.getInstance().removeBullet(bullet);
				}
			}
			
			for(Zombie zombie: World.getInstance().zombies)
			{				
				float distance = getDistance(bullet, zombie);
				
				if( distance < zombie.getRadius() + 5.0f )
				{
					World.getInstance().removeBullet(bullet);
					World.getInstance().removeZombie(zombie);
				}
			}
		}
	}
	
	public Collision checkCollision(GameObject object)
	{	
		boolean left = false, right = false, front = false;
		
		float[] vertices = getCollisionBoxVertices( object );
		
		Collision collision = new Collision(null, CollisionSide.NONE);
		
		for(Column column: World.getInstance().columns)
		{
			//front side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[2],vertices[3]), new Vector2(vertices[4], vertices[5]), column.getCenter(), column.getSquaredRadius() ) )
			{
				front = true;
			}
			
			//left side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[0],vertices[1]), new Vector2(vertices[2], vertices[3]), column.getCenter(), column.getSquaredRadius() ) )
			{
				left = true;
			}
						
			//right side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[4],vertices[5]), new Vector2(vertices[6], vertices[7]), column.getCenter(), column.getSquaredRadius() ) )
			{
				right = true;
			}
			
			if( front || left || right )
			{
				collision.object = column;
			}
		}
		
		for(Zombie zombie: World.getInstance().zombies)
		{
			if( zombie.getX() == object.getX() && zombie.getY() == object.getY() ) continue;
			
			float zombieSquaredRadius = ( (zombie.getWidth() + 40.0f) / 2.0f);
			zombieSquaredRadius *= zombieSquaredRadius;
			
			//front side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[2],vertices[3]), new Vector2(vertices[4], vertices[5]), zombie.getCenter(), zombieSquaredRadius ) )
			{
				front = true;
			}
			
			//left side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[0],vertices[1]), new Vector2(vertices[2], vertices[3]), zombie.getCenter(), zombieSquaredRadius ) )
			{
				left = true;
			}
				
			//right side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[4],vertices[5]), new Vector2(vertices[6], vertices[7]), zombie.getCenter(), zombieSquaredRadius ) )
			{
				right = true;
			}
			
			if( front || left || right )
			{
				collision.object = zombie;
			}
		}
		
		if( left && right && front )
		{
			collision.side = CollisionSide.LEFTRIGHTFRONT;
		}
		else if( !left && right && front )
		{
			collision.side = CollisionSide.RIGHTFRONT;
		}
		else if( left && !right && front )
		{
			collision.side = CollisionSide.LEFTFRONT;
		}
		else if( !left && !right && front )
		{
			collision.side = CollisionSide.FRONT;
		}
		else if( !left && right && !front )
		{
			collision.side = CollisionSide.RIGHT;
		}
		else if( left && !right && !front )
		{
			collision.side = CollisionSide.LEFT;
		}
		
		return collision;
	}
	
	public float[] getCollisionBoxVertices(GameObject object)
	{
		Vector2 v1 = new Vector2( -object.getWidth() / 2.0f, 0 );
		Vector2 v2 = new Vector2( -object.getWidth() / 2.0f, object.getWidth() );
		Vector2 v3 = new Vector2(  object.getWidth() / 2.0f, object.getWidth() );
		Vector2 v4 = new Vector2(  object.getWidth() / 2.0f, 0 );
		
		v1.rotate( object.rotation );
		v2.rotate( object.rotation );
		v3.rotate( object.rotation );
		v4.rotate( object.rotation );
		
		v1.add(object.getCenter().add( object.forward ));
		v2.add(object.getCenter().add( object.forward ));
		v3.add(object.getCenter().add( object.forward ));
		v4.add(object.getCenter().add( object.forward ));
		
		float[] vertices = new float[]{ v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, v4.x, v4.y };
		
		return vertices;
	}
	
	private float getDistance(GameObject a, GameObject b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.getCenter().x, a.getCenter().y - b.getCenter().y );
		
		return distance.len();
	}
}
