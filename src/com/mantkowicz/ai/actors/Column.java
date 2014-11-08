package com.mantkowicz.ai.actors;

import com.badlogic.gdx.math.Vector2;
import com.mantkowicz.ai.world.World;

public class Column extends GameObject
{	
	public Column(float x, float y)
	{
		super("gfx/column.png", x, y);
	}

	@Override
	protected void step() 
	{
		
	}
	
	public Vector2 getSafePoint()
	{
		Player player = World.getInstance().player;
		
		float angle = (new Vector2( this.getCenter().x - player.getCenter().x, this.getCenter().y - player.getCenter().y )).angle();
		
		Vector2 translation = new Vector2(0, this.getRadius() * 3);
		translation.rotate(angle - 90.0f);
		
		return new Vector2( this.getCenter().x + translation.x, this.getCenter().y + translation.y );
	}
}
