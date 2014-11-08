package com.mantkowicz.ai.listener;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.Bullet;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.GameObject;
import com.mantkowicz.ai.actors.Zombie;
import com.mantkowicz.ai.vars.CollisionSide;
import com.mantkowicz.ai.vars.Vars;
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
				float distance = Vars.getDistance(bullet, column);

				if( distance < (column.getRadius() + 5.0f) )
				{
					World.getInstance().removeBullet(bullet);
				}
			}
			
			for(Zombie zombie: World.getInstance().zombies)
			{				
				float distance = Vars.getDistance(bullet, zombie);
				
				if( distance < zombie.getRadius() + 5.0f )
				{
					World.getInstance().removeBullet(bullet);
					World.getInstance().removeZombie(zombie);
				}
			}
		}
	}
		
	public Collision checkCollision2(GameObject object)
	{
		Collision collision = new Collision();
		
		//---------------
		
		for(Column column: World.getInstance().columns)
		{
			if( Vars.getDistance(object, column) <= (column.getRadius() + object.getRadius() ) )
			{
				collision.addCollision(column, CollisionSide.FRONT);
			}
		}
		
		for(Zombie zombie: World.getInstance().zombies)
		{
			if( Vars.getDistance(object, zombie) <= (zombie.getRadius() + object.getRadius() ) )
			{
				collision.addCollision(zombie, CollisionSide.FRONT);
			}
		}
		
		//---------------
		
		return collision;
	}
	
	public Collision checkCollision(GameObject object)
	{	
		Vector2 stepVector = new Vector2(object.forward.x, object.forward.y);
		stepVector.rotate( object.rotation );
		
		Array<Vector2> leftPoint = getLeftCollisionPoint(object, stepVector);
		Array<Vector2> rightPoint = getRightCollisionPoint(object, stepVector);
		
		Collision collision = new Collision();
		
		
		for(Column column: World.getInstance().columns)
		{
			boolean left = false, right = false;
			
			for(Vector2 point: leftPoint)
			{
				if( isPointInObject(point, column) )
				{
					left = true;
					break;
				}
			}
			
			for(Vector2 point: rightPoint)
			{
				if( isPointInObject(point, column) )
				{
					right = true;
					break;
				}
			}
			
			if( left && right )
			{
				collision.addCollision(column, CollisionSide.LEFT);
			}
			else if( left && !right )
			{
				collision.addCollision(column, CollisionSide.LEFT);
			}
			else if( !left && right )
			{
				collision.addCollision(column, CollisionSide.RIGHT);
			}
		}
		
		return collision;
	}
	
	public Array<Vector2> getLeftCollisionPoint(GameObject object, Vector2 stepVector)
	{
		return getCollisionPoint( object, stepVector, -1.0f );
	}
	
	public Array<Vector2> getRightCollisionPoint(GameObject object, Vector2 stepVector)
	{
		return getCollisionPoint( object, stepVector, 1.0f );
	}
	
	public Array<Vector2> getCollisionPoint(GameObject object, Vector2 stepVector, float parameter)
	{
		float x = (object.getWidth() / 2.0f) * parameter;
		float y1 = object.getHeight();
		
		Vector2 v1 = new Vector2( x, y1 );
		
		v1.rotate( object.rotation );
		
		v1.add( object.getCenter() );
		v1.add( stepVector );
				
		Array<Vector2> v = new Array<Vector2>();
		v.add(v1);
		
		return v;
	}
	
	private boolean isPointInObject(Vector2 point, GameObject object)
	{
		if( Vars.getDistance(point, object) < object.getRadius() )
		{
			return true;
		}
		
		return false;
	}
}
