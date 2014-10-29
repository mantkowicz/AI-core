package com.mantkowicz.ai.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mantkowicz.ai.controller.Controller;
import com.mantkowicz.ai.logger.Logger;

public abstract class GameObject extends Actor
{	
	protected Texture texture;
	protected TextureRegion textureRegion;

	private Controller controller;
	
	protected Vector2 forward;
	protected float speed;
	protected float turbo;
	public float rotation;
	
	protected abstract void step();
	protected abstract void setForward();
	protected abstract void setTurbo();
	protected abstract void setRotation();
	
	public GameObject(String avatarPath, float x, float y)
	{
		texture = new Texture( Gdx.files.internal(avatarPath) );
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		textureRegion = new TextureRegion(texture);
		
		this.setSize(texture.getWidth(), texture.getHeight());
		this.setPosition(x, y);
		
		controller = new Controller(this);
		
		forward = new Vector2(0, 0);
		speed = 1;
		turbo = 1;
		rotation = 0;
	}
	
	@Override
	public void act(float delta)
	{
		step();
	
		setForward();
		setTurbo();
		setRotation();
		
		controller.act(forward, speed, turbo, rotation);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		batch.draw(textureRegion, this.getX(), this.getY(), textureRegion.getRegionWidth() / 2.0f, textureRegion.getRegionHeight() / 2.0f, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), 1.0f, 1.0f, rotation);
	}
			
	protected void dispose()
	{
		texture.dispose();
	}
}