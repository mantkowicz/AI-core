package com.mantkowicz.ai.actors;

import com.badlogic.gdx.math.Vector2;
import com.mantkowicz.ai.vars.Vars;
import com.mantkowicz.ai.world.World;

public class Bullet extends GameObject
{
	private Vector2 start;
	
	public Bullet()
	{
		super("gfx/bullet.png", World.getInstance().player.getCenter().x, World.getInstance().player.getCenter().y);
	
		this.start = World.getInstance().player.getCenter();
		
		this.speed = 20.0f;
		
		this.controller.setImmediatelyRotation(true);
		this.rotation = World.getInstance().player.rotation;
	}

	@Override
	protected void step() 
	{
		if( Vars.getDistance(this, start ) > 1500.0f )
		{
			World.getInstance().removeBullet(this);
		}
		else
		{
			this.forward = new Vector2(0, 1);		
		}
	}
}
