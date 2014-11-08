package com.mantkowicz.ai.listener;

import com.badlogic.gdx.math.Vector2;
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
			
	public Collision checkCollision(GameObject object)
	{	
		Vector2 stepVector = new Vector2(object.forward.x, object.forward.y);
		stepVector.rotate( object.rotation );
		
		Vector2 leftPoint = getLeftCollisionPoint(object, stepVector);
		Vector2 rightPoint = getRightCollisionPoint(object, stepVector);
		
		Collision collision = new Collision();
		
		for(Column column: World.getInstance().columns)
		{
			if( object.equals(column) )
			{
				continue;
			}
			
			if( Vars.getDistance(object, column) <= (column.getRadius() + object.getRadius() ) )
			{
				collision.addCollision(column, CollisionSide.DIRECT);
			}
			else if( isPointInObject(leftPoint, column) )
			{
				collision.addCollision(column, CollisionSide.PRELEFT);
				break;
			}
			else if( isPointInObject(rightPoint, column) )
			{
				collision.addCollision(column, CollisionSide.PRERIGHT);
				break;
			}
		}
		
		for(Zombie zombie: World.getInstance().zombies)
		{
			if( object.equals(zombie) )
			{
				continue;
			}
			
			if( Vars.getDistance(object, zombie) <= (zombie.getRadius() + object.getRadius() ) )
			{
				collision.addCollision(zombie, CollisionSide.DIRECT);
			}
		}
		
		return collision;
	}
	
	public Vector2 getLeftCollisionPoint(GameObject object, Vector2 stepVector)
	{
		return getCollisionPoint( object, stepVector, -1.0f );
	}
	
	public Vector2 getRightCollisionPoint(GameObject object, Vector2 stepVector)
	{
		return getCollisionPoint( object, stepVector, 1.0f );
	}
	
	private Vector2 getCollisionPoint(GameObject object, Vector2 stepVector, float parameter)
	{
		float x = (object.getWidth() / 2.0f) * parameter;
		float y = object.getHeight();
		
		Vector2 v = new Vector2( x, y );
		
		v.rotate( object.rotation );
		
		v.add( object.getCenter() );
		v.add( stepVector );
		
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
