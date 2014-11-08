package com.mantkowicz.ai.actors;

import com.badlogic.gdx.math.Vector2;
import com.mantkowicz.ai.vars.Vars;
import com.mantkowicz.ai.world.World;


public class Bullet extends GameObject
{
	private Vector2 maxDistance;
	private Player player;
	
	public Bullet(Player player)
	{
		super("gfx/bullet.png", player.getCenter().x, player.getCenter().y);
	
		this.player = player;
		
		this.maxDistance = new Vector2(0, 1500.0f);
		this.maxDistance.rotate( -player.getRotation() );
	}

	@Override
	protected void step() 
	{
		if( Vars.getDistance(this, World.getInstance().worldCenter) > Vars.ZOMBIE_AREA_WIDTH )
		{
			World.getInstance().removeBullet(this);
		}
	}
			
	@Override
	protected void setForward() 
	{		
		forward = new Vector2(0, 1);
		forward.rotate( -player.getRotation() );
	}

	@Override
	protected void setTurbo() 
	{	
		this.turbo = 20.0f;
	}

	@Override
	protected void setRotation() 
	{	
	}
}
