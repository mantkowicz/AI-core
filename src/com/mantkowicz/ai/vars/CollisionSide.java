package com.mantkowicz.ai.vars;

import com.badlogic.gdx.math.Vector2;
import com.mantkowicz.ai.actors.GameObject;
import com.mantkowicz.ai.listener.Collision;
import com.mantkowicz.ai.logger.Logger;

public enum CollisionSide
{
	NONE,
	LEFT,
	FRONT,
	RIGHT,
	LEFTFRONT,
	RIGHTFRONT,
	LEFTRIGHTFRONT;

	public static float calculateAvoidAngle(GameObject a, Collision collision) 
	{		
		GameObject b = collision.object;
		
		float r = b.getRadius() + a.getRadius() + 20.0f;
		float x = ( new Vector2(a.getCenter().x - b.getCenter().x, a.getCenter().y - b.getCenter().y) ).len();
		float avoidAngle = (float) Math.toDegrees( Math.asin( r/x ) );
		Logger.log("", avoidAngle);
		if( collision.side == CollisionSide.LEFT || collision.side == CollisionSide.FRONT || collision.side == CollisionSide.LEFTFRONT || collision.side == CollisionSide.LEFTRIGHTFRONT )
		{
			avoidAngle *= -1.0f;
		}
		else
		{
			avoidAngle *= 1.0f;
		}
		
		return avoidAngle;
	}
}