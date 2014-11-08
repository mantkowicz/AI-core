package com.mantkowicz.ai.actors;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.exceptions.NoRotationException;
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
	
	public boolean isNeighbourInRage;
	public boolean rage = false;
	
	public int neighbours = 1;
	
	private boolean safe = false;
	private ZombieState currentState = ZombieState.TRESSPASS;
	
	private int currentFrame;
	
	public Zombie(float x, float y)
	{
		super("gfx/zombie.png", x, y);
		
		this.rageImage = createImage("gfx/rage.png");
		this.runImage = createImage("gfx/runAway.png");
		
		this.staticObject = false;
	}
			
	@Override
	protected void step() 
	{	
		checkSafe();
		updateState();
	
		this.currentFrame++;
		forward = new Vector2(0.0f, 1.0f);
		
		if( currentState == ZombieState.TRESSPASS )
		{				
			rageImage.setVisible(false);
			runImage.setVisible(false);
			
			controller.setImmediatelyRotation(false);
			
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
						
		if( collision.willBeColumnCollision() )
		{	
			try
			{
				rotation += CollisionSide.calculateAvoidAngle( this, collision );
			}
			catch( NoRotationException e )
			{
				forward.x = 0;
				forward.y = 0;
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

	//---AI functions
	private void wander()
	{		
		if( this.currentFrame % 100 == 0 )
		{
			if( Vars.getDistance(this, World.getInstance().worldCenter) > Vars.ZOMBIE_AREA_WIDTH )
			{
				rotation = Vars.getAngle(this, World.getInstance().worldCenter);
				controller.setImmediatelyRotation(true);
			}
			else
			{
				rotation = (float)Math.random() * 360.0f;
			}
		}
	}
	
	private void runAway()
	{
		turbo = 1.5f;
		
		runImage.toFront();
		runImage.setRotation(-this.getRotation());
		runImage.setOrigin((runImage.getWidth()/2.0f),(runImage.getHeight()/2.0f));
		runImage.setPosition(this.getX() + (this.getWidth()/2.0f) - (runImage.getWidth()/2.0f), this.getY() + (this.getHeight()/2.0f) - (runImage.getHeight()/2.0f));
		
		
		Column nearestColumn = getNearestColumn();
		
		seek(nearestColumn.getSafePoint().x, nearestColumn.getSafePoint().y);
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
	
	private void seek(float x, float y)
	{
		Vector2 targetVector = new Vector2( x - this.getX(), y - this.getY() );
		
		rotation = targetVector.nor().angle() - 90.0f;
	}
	
	//---
	
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
		
	public void updateNeighbours()
	{
		this.neighbours = 0;
		
		for(Zombie zombie: World.getInstance().zombies)
		{
			if( zombie.equals(this) )
			{
				continue;
			}
			
			if( Vars.getDistance(this, zombie) < Vars.DISTANCE )
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