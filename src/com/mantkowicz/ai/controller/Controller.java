package com.mantkowicz.ai.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mantkowicz.ai.logger.Logger;

public class Controller
{
	private Actor actor;
	
	public Controller(Actor actor)
	{
		this.actor = actor;
	}
	
	public void act(Vector2 forward, float speed, float turbo, float rotation)
	{
		forward.scl(speed);
		forward.scl(turbo);
		forward.rotate( rotation );

		actor.setRotation( -rotation );
		actor.setPosition( actor.getX() + forward.x, actor.getY() + forward.y);
	}
}
