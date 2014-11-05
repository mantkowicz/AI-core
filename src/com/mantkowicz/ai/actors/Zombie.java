package com.mantkowicz.ai.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.listener.Collision;
import com.mantkowicz.ai.listener.ContactListener;
import com.mantkowicz.ai.logger.Logger;
import com.mantkowicz.ai.vars.CollisionSide;
import com.mantkowicz.ai.vars.Vars;
import com.mantkowicz.ai.world.World;

public class Zombie extends GameObject implements Comparable<Zombie>
{	
	private enum ZombieState
	{
		ATTACK,
		RUNAWAY,
		TRESSPASS
	};
	
	public Image rageImage;
	public Image runImage;
	
	public Collision collision;
	
	
	private int currentFrame;
	
	private boolean safe;
	
	public boolean isNeighbourInRage;
	public int neighbours;
	
	public boolean rage = false;
	
	private ZombieState currentState;
	
	private Array<Texture> textures;
	
	private ContactListener contactListener;
	
	public Zombie(float x, float y)
	{
		super("gfx/zombie.png", x, y);
		
		contactListener = new ContactListener();
		
		textures = new Array<Texture>();
		
		this.rageImage = createImage("gfx/rage.png");
		this.runImage = createImage("gfx/runAway.png");
		
		currentFrame = 0;
		
		collision = new Collision(null, CollisionSide.NONE);
		
		safe = false;
		neighbours = 1;
					
		currentState = ZombieState.TRESSPASS;
	}
		
	private Image createImage(String path)
	{
		Texture texture = new Texture( Gdx.files.internal(path) );
		texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		textures.add(texture);
		
		Image image = new Image(texture);
		image.setVisible(false);
		
		return image;
	}
	
	@Override
	protected void step() 
	{		
		Logger.log(this, collision.side);
	
		checkSafe();
		updateState();
	
		forward = new Vector2(0.0f, 1.0f);
		
		if( currentState == ZombieState.TRESSPASS )
		{				
			rageImage.setVisible(false);
			runImage.setVisible(false);
			
			controller.setImmediatelyRotation(true);
			
			wander();
		}
		else if( currentState == ZombieState.RUNAWAY )
		{				
			rageImage.setVisible(false);
			runImage.setVisible(true);
			
			controller.setImmediatelyRotation(true);
			
			runAway();
		}
		else if( currentState == ZombieState.ATTACK )
		{
			rageImage.setVisible(true);
			runImage.setVisible(false);
			
			controller.setImmediatelyRotation(true);
			
			attack();
		}
				
		this.currentFrame++;

		collision = contactListener.checkCollision(this);
		
		if( collision.side != CollisionSide.NONE )
		{	forward.x = 0;
		forward.y = 0;
			rotation += CollisionSide.calculateAvoidAngle( this, collision );
		}
	}

	private void attack()
	{
		turbo = 2.0f;
		
		rageImage.toFront();
		rageImage.setRotation(-this.getRotation());
		rageImage.setOrigin((rageImage.getWidth()/2.0f),(rageImage.getHeight()/2.0f));
		rageImage.setPosition(this.getX() + (this.getWidth()/2.0f) - (rageImage.getWidth()/2.0f), this.getY() + (this.getHeight()/2.0f) - (rageImage.getHeight()/2.0f));
		
		Player player = World.getInstance().player;
			
		seek( player.getX(), player.getY() );
	}
	
	private void wander()
	{		
		if( this.currentFrame % 100 == 0 )
		{
			if( getDistance(this, World.getInstance().worldCenter) > Vars.ZOMBIE_AREA_WIDTH )
			{
				rotation = getAngle(this, World.getInstance().worldCenter);
				controller.setImmediatelyRotation(true);
			}
			else
			{
				rotation = (float)Math.random() * 360.0f;
			}
		}
	}
	
	private void checkSafe()
	{
		Array<Column> columns = World.getInstance().columns;
		Player player = World.getInstance().player;
		
		boolean hidden = false;
		safe = false;
		for(Column column: columns)
		{
			hidden = Intersector.intersectSegmentCircle(this.getCenter(), player.getCenter(), column.getCenter(), column.getSquaredRadius() );		
			if(hidden)
			{
				safe = true;
				break;
			}
		}
	}
	
	private void runAway()
	{
		turbo = 1.5f;
		
		runImage.toFront();
		runImage.setRotation(-this.getRotation());
		runImage.setOrigin((rageImage.getWidth()/2.0f),(rageImage.getHeight()/2.0f));
		runImage.setPosition(this.getX() + (this.getWidth()/2.0f) - (rageImage.getWidth()/2.0f), this.getY() + (this.getHeight()/2.0f) - (rageImage.getHeight()/2.0f));
		
		
		Column nearestColumn = getNearestColumn();
		
		seek(nearestColumn.getSafePoint().x, nearestColumn.getSafePoint().y);
	}
	
	private Column getNearestColumn()
	{
		Array<Column> columns = World.getInstance().columns;
		
		Column nearestColumn = columns.first();
		
		for(Column column: columns)
		{
			Vector2 distance = new Vector2( this.getX() - column.getX(), this.getY() - column.getY() );
			Vector2 currentDistance = new Vector2( this.getX() - nearestColumn.getX(), this.getY() - nearestColumn.getY() );
			
			if( distance.len() < currentDistance.len() )
			{
				nearestColumn = column;
			}
		}
		
		return nearestColumn;		
	}
	
	private void seek(float x, float y)
	{
		Vector2 targetVector = new Vector2( x - this.getX(), y - this.getY() );
		
		rotation = targetVector.nor().angle() - 90.0f;
	}
	
	
		
	public void updateNeighbours()
	{
		this.neighbours = 0;
		
		for(Zombie zombie: World.getInstance().zombies)
		{
			if( zombie.equals(this) )
			{
				continue;
			}
			
			if( getDistance(this, zombie) < Vars.DISTANCE )
			{
				if( zombie.rage )
				{
					this.rage = true;
				}
				
				this.neighbours++;
			}
		}
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
	
	protected void updateState()
	{
		if( rage || currentState == ZombieState.ATTACK )
		{
			currentState = ZombieState.ATTACK;
		}
		else if( currentState != ZombieState.ATTACK && safe )
		{
			currentState = ZombieState.TRESSPASS;
		}
		else if( currentState != ZombieState.ATTACK && !safe )
		{
			rageImage.setVisible(false);
			controller.setImmediatelyRotation(false);
			
			currentState = ZombieState.RUNAWAY;
		}
	}
	
	public void dispose()
	{
		super.dispose();
		
		for(Texture texture: textures)
		{
			texture.dispose();
		}
	}

	@Override
	public int compareTo(Zombie zombie) 
	{
		if( zombie.neighbours > this.neighbours )
		{
			return 1;
		}
		else if( zombie.neighbours < this.neighbours )
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}
}