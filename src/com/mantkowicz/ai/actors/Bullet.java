package com.mantkowicz.ai.actors;

import com.badlogic.gdx.math.Vector2;


public class Bullet extends GameObject
{
	private float bulletSpeed = 20.0f;
	
	private Vector2 bulletForward;
	private Vector2 maxDistance;
	
	public Bullet(Player player)
	{
		super("gfx/bullet.png", player.getCenter().x, player.getCenter().y);
		
		this.bulletForward = new Vector2(0, bulletSpeed);
		this.bulletForward.rotate( -player.getRotation() );
		
		this.maxDistance = new Vector2(0, 1500.0f);
		this.maxDistance.rotate( -player.getRotation() );
	}

	@Override
	protected void step() 
	{
		if(this.maxDistance.len2() < 100)
		{
			this.clear();
			this.remove();
		}
		
		this.maxDistance.sub(this.bulletForward);
	}
			
	@Override
	protected void setForward() 
	{		
		forward = this.bulletForward;
	}

	@Override
	protected void setTurbo() 
	{	
	}

	@Override
	protected void setRotation() 
	{	
	}
}
