package com.mantkowicz.ai.vars;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.GameObject;
import com.mantkowicz.ai.exceptions.NoRotationException;
import com.mantkowicz.ai.listener.Collision;
import com.mantkowicz.ai.listener.ContactListener;

public enum CollisionSide
{
	NONE,
	LEFT,
	FRONT,
	RIGHT,
	LEFTFRONT,
	RIGHTFRONT,
	LEFTRIGHTFRONT;

	public static float calculateAvoidAngle(GameObject a, Collision collision) throws NoRotationException 
	{	
		Array<Float> avoidAngle = new Array<Float>();
		
		for(int i = 0; i < collision.objects.size; i++)
		{
			GameObject b = collision.objects.get(i);
			CollisionSide collisionSide = collision.collisionSides.get(i);
			
			float r = b.getRadius() + a.getRadius() + 20.0f;
			float x = ( new Vector2(a.getCenter().x - b.getCenter().x, a.getCenter().y - b.getCenter().y) ).len();
					
			float angle = (float) Math.toDegrees( Math.asin( Math.min(r/x, 1.0) ) );
			
			if( collisionSide == CollisionSide.LEFT )
			{
				angle *= -1.0f;
			}
			else
			{
				angle *= 1.0f;
			}
			
			avoidAngle.add(angle);
		}
		
		if( avoidAngle.size > 1 ) throw( new NoRotationException() );
		
		return avoidAngle.first();
	}
}