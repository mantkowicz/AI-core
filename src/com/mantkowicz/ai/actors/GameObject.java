package com.mantkowicz.ai.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.controller.Controller;
import com.mantkowicz.ai.listener.Collision;
import com.mantkowicz.ai.listener.ContactListener;
import com.mantkowicz.ai.vars.CollisionSide;

public abstract class GameObject extends Actor
{	
	protected Array<Texture> textures;
	protected TextureRegion textureRegion;

	public Controller controller;
	
	public Vector2 forward;
	protected float speed;
	protected float turbo;
	
	public float lastRotation;
	public float rotation;
	
	protected boolean staticObject = true;
	
	protected abstract void step();
	
	protected ContactListener contactListener;
	protected Collision collision;
	
	public GameObject(String avatarPath, float x, float y)
	{
		textures = new Array<Texture>();
		
		Texture texture = new Texture( Gdx.files.internal(avatarPath) );
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		textures.add(texture);
		
		textureRegion = new TextureRegion(texture);
		
		this.setSize(texture.getWidth(), texture.getHeight());
		this.setPosition(x, y);
		
		contactListener = new ContactListener();
		
		controller = new Controller(this);
		
		forward = new Vector2(0, 0);
		speed = 1;
		turbo = 1;
		
		rotation = 0;
	}
	
	@Override
	public void act(float delta)
	{
		Vector2 collisionVector = new Vector2(0,0);	
				
		if( !staticObject )
		{
			collision = contactListener.checkCollision(this);
				
			for(int i = 0; i < collision.columns.size; i++ )
			{
				if( collision.columnCollisionSides.get(i) != CollisionSide.DIRECT )
				{
					continue;
				}
				
				Vector2 partCollisionVector = new Vector2( this.getCenter().x - collision.columns.get(i).getCenter().x, this.getCenter().y - collision.columns.get(i).getCenter().y );
				
				collisionVector.add(partCollisionVector);	
			}
			
			for(int i = 0; i < collision.zombies.size; i++ )
			{
				if( collision.zombieCollisionSides.get(i) != CollisionSide.DIRECT )
				{
					continue;
				}
				
				Vector2 partCollisionVector = new Vector2( this.getCenter().x - collision.zombies.get(i).getCenter().x, this.getCenter().y - collision.zombies.get(i).getCenter().y );
				
				collisionVector.add(partCollisionVector);	
			}
		}

		step();
		
		controller.act(forward, speed, turbo, rotation, collisionVector);
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
	
	protected float getSquaredRadius()
	{
		return this.getRadius() * this.getRadius();
	}
	
	protected Image createImage(String path)
	{
		Texture texture = new Texture( Gdx.files.internal(path) );
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		textures.add(texture);
		
		Image image = new Image(texture);
		image.setVisible(false);
		
		return image;
	}
					
	public void dispose()
	{
		for(Texture texture: textures)
		{
			texture.dispose();
		}
	}
}