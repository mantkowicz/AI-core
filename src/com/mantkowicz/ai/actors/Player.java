package com.mantkowicz.ai.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.mantkowicz.ai.controller.Controller;
import com.mantkowicz.ai.controller.MouseController;
import com.mantkowicz.ai.logger.Logger;


public class Player extends GameObject 
{			
	public Player(float x, float y)
	{	
		super("gfx/player.png", x, y);
		
		this.controller.setImmediatelyRotation(true);
		
		this.speed = 2;
	}
	
	@Override
	protected void step()
	{
		if( MouseController.isMouseClicked() )
		{
			this.getStage().addActor( new Bullet(this) );
		}
	}
	
	@Override
	protected void setForward() 
	{
		forward = new Vector2(0.0f, 0.0f);
		
		if( Gdx.input.isKeyPressed(Keys.W)) forward = new Vector2(0.0f, 1.0f);
		if( Gdx.input.isKeyPressed(Keys.S)) forward = new Vector2(0.0f, -1.0f);
		if( Gdx.input.isKeyPressed(Keys.A)) forward = new Vector2(-1.0f, 0.0f);
		if( Gdx.input.isKeyPressed(Keys.D)) forward = new Vector2(1.0f, 0.0f);
	}

	@Override
	protected void setTurbo() 
	{
		if( Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) ) turbo = 3.0f;
		else turbo = 1.0f;
	}

	@Override
	protected void setRotation() 
	{
		rotation = MouseController.getCursorAngle();	
	}
}
