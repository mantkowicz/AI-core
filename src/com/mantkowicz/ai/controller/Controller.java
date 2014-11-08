package com.mantkowicz.ai.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Controller
{
	private Actor actor;
	
	private boolean immediatelyRotate = false;
	
	private float targetRotation;
	public float currentRotation;
	
	public Controller(Actor actor)
	{
		this.actor = actor;
		currentRotation = 0.0f;
	}
	
	public void act(Vector2 forward, float speed, float turbo, float rotation, Vector2 interactionVector)
	{		
		targetRotation = (rotation ) % 360;
		
		if( this.immediatelyRotate ) 
		{
			currentRotation = targetRotation;
		}
		else
		{				
				if( currentRotation < targetRotation )
				{
					
					currentRotation += 1.0f;
				}
				else if( currentRotation > targetRotation )
				{
					currentRotation -= 1.0f;
				}
			}
			
		actor.setRotation( -currentRotation );
		forward.rotate( currentRotation );
		forward.add(interactionVector);

		forward.nor();
		
		forward.scl(speed);
		forward.scl(turbo);
		
		actor.setPosition( actor.getX() + forward.x, actor.getY() + forward.y);
	}
	
	public void setImmediatelyRotation(boolean immediatelyRotate)
	{
		this.immediatelyRotate = immediatelyRotate;
	}
	
	public float getCurrentRotation()
	{
		return this.currentRotation;
	}
}
