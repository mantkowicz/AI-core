package com.mantkowicz.ai.listener;

import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.GameObject;
import com.mantkowicz.ai.vars.CollisionSide;

public class Collision 
{
	public Array<GameObject> objects;
	public Array<CollisionSide> collisionSides;
	
	public Collision() 
	{		
		this.objects = new Array<GameObject>();
		this.collisionSides = new Array<CollisionSide>();
	}
	
	public void addCollision(GameObject object, CollisionSide collisionSide)
	{
		this.objects.add(object);
		this.collisionSides.add(collisionSide);
	}
	
	public boolean isNull()
	{
		if( objects.size > 0 && collisionSides.size > 0 )
		{
			return false;
		}
		
		return true;
	}
}
