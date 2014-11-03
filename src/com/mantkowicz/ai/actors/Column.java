package com.mantkowicz.ai.actors;

import com.badlogic.gdx.math.Vector2;


public class Column extends GameObject
{
	private Player player;
	
	public Column(float x, float y)
	{
		super("gfx/column.png", x, y);
	}

	@Override
	protected void step() 
	{
		
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public float getRadius()
	{
		return (this.getWidth() / 2.0f);
	}
	
	public float getSquaredRadius()
	{
		return this.getRadius() * this.getRadius();
	}
	
	public Vector2 getSafePoint()
	{
		float angle = (new Vector2( this.getCenter().x - player.getCenter().x, this.getCenter().y - player.getCenter().y )).angle();
		
		Vector2 translation = new Vector2(0, this.getRadius() * 3);
		translation.rotate(angle - 90.0f);
		
		return new Vector2( this.getCenter().x + translation.x, this.getCenter().y + translation.y );
	}
	
	@Override
	protected void setForward() 
	{		
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
