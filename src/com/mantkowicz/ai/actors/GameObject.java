package com.mantkowicz.ai.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mantkowicz.ai.controller.Controller;

public abstract class GameObject extends Actor
{	
	protected Texture texture;
	protected TextureRegion textureRegion;

	public Controller controller;
	
	public Vector2 forward;
	protected float speed;
	protected float turbo;
	
	public float lastRotation;
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
		setForward();
		setTurbo();
		setRotation();
		
		step();
		
		controller.act(forward, speed, turbo, rotation);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		super.draw(batch, parentAlpha);

		batch.draw(textureRegion, this.getX(), this.getY(), textureRegion.getRegionWidth() / 2.0f, textureRegion.getRegionHeight() / 2.0f, textureRegion.getRegionWidth(), textureRegion.getRegionHeight(), 1.0f, 1.0f, controller.getCurrentRotation());
	}
	
	public Vector2 getCenter()
	{
		return new Vector2( this.getX() + (this.getWidth() / 2.0f), this.getY() + (this.getHeight() / 2.0f) );
	}
	
	public float getRadius()
	{
		return (this.getWidth() / 2.0f);
	}
	
	public float getSquaredRadius()
	{
		return this.getRadius() * this.getRadius();
	}
		
	protected float getDistance(GameObject a, GameObject b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.getCenter().x, a.getCenter().y - b.getCenter().y );
		
		return distance.len();
	}
	protected float getDistance(Vector2 a, Vector2 b)
	{
		Vector2 distance = new Vector2( a.x - b.x, a.y - b.y );
		
		return distance.len();
	}
	protected float getDistance(GameObject a, Vector2 b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.x, a.getCenter().y - b.y );
		
		return distance.len();
	}
	protected float getDistance(Vector2 a, GameObject b)
	{
		Vector2 distance = new Vector2( a.x - b.getCenter().x, a.y - b.getCenter().y );
		
		return distance.len();
	}
	
	protected float getAngle(GameObject a, GameObject b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.getCenter().x, a.getCenter().y - b.getCenter().y );
		
		return distance.angle();
	}
	protected float getAngle(Vector2 a, Vector2 b)
	{
		Vector2 distance = new Vector2( a.x - b.x, a.y - b.y );
		
		return distance.angle();
	}
	protected float getAngle(GameObject a, Vector2 b)
	{
		Vector2 distance = new Vector2( a.getCenter().x - b.x, a.getCenter().y - b.y );
		
		return distance.angle();
	}
	protected float getAngle(Vector2 a, GameObject b)
	{
		Vector2 distance = new Vector2( a.x - b.getCenter().x, a.y - b.getCenter().y );
		
		return distance.angle();
	}
			
	public void dispose()
	{
		texture.dispose();
	}
}