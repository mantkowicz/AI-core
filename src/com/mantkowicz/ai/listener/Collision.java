package com.mantkowicz.ai.listener;

import com.mantkowicz.ai.actors.GameObject;
import com.mantkowicz.ai.vars.CollisionSide;

public class Collision 
{
	public GameObject object;
	public CollisionSide side;
	
	public Collision(GameObject object, CollisionSide side) 
	{
		this.object = object;
		this.side = side;
	}
}
