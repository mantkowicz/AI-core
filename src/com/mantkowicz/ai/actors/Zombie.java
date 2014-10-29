package com.mantkowicz.ai.actors;

import com.badlogic.gdx.math.Vector2;
import com.mantkowicz.ai.logger.Logger;
import com.mantkowicz.ai.vars.Vars;

public class Zombie extends GameObject
{	
	private enum ZombieState
	{
		ATTACK,
		RUNAWAY,
		TRESSPASS
	};
	
	private Player player;
	
	private Vector2 wanderTarget;
	
	private int currentFrame;
	
	private boolean safe;
	private int power;
	
	private ZombieState currentState;
	
	public Zombie(float x, float y)
	{
		super("gfx/zombie.png", x, y);
		
		currentFrame = 0;
		
		wanderTarget = new Vector2(0, 0);
		
		safe = true;
		power = 1;
		
		currentState = ZombieState.TRESSPASS;
	}
	
	public void setPlayer(Player player)
	{
		this.player = player;
	}
	
	@Override
	protected void step() 
	{
		updateState();
		
		if( currentState == ZombieState.TRESSPASS )
		{
			wander();
		}
		else if( currentState == ZombieState.RUNAWAY )
		{
			
		}
		else if( currentState == ZombieState.ATTACK )
		{
			attack();
		}
		
		this.currentFrame++;
	}

	private void attack()
	{
		seek( player.getX(), player.getY() );
		
		turbo = 2.0f;
	}
	
	private void wander()
	{
		if( this.currentFrame % 100 == 0 )
		{
			this.wanderTarget.x = (float)Math.random() * ( Math.random() > 0.5 ? 1 : -1 ) * 100.0f + this.getX();
			this.wanderTarget.y = (float)Math.random() * ( Math.random() > 0.5 ? 1 : -1 ) * 100.0f + this.getY();
		}
		
		Logger.log(this, "LOSUJ x = " + String.valueOf(wanderTarget.x) + " | y = " + String.valueOf(wanderTarget.y));
		seek( wanderTarget.x, wanderTarget.y );
	}
	
	private void seek(float x, float y)
	{
		Vector2 targetVector = new Vector2( x - this.getX(), y - this.getY() );
		
		rotation = targetVector.nor().angle() - 90.0f;
	}
	
	@Override
	protected void setForward() 
	{
		forward = new Vector2(0.0f, 1.0f);
	}

	@Override
	protected void setTurbo() 
	{
	}

	@Override
	protected void setRotation() 
	{
		//rotation += 3.0f;
	}
	
	protected void updateState()
	{
		if( power > Vars.PLAYER_POWER )
		{
			currentState = ZombieState.ATTACK;
		}
		else if( safe )
		{
			currentState = ZombieState.TRESSPASS;
		}
		else
		{
			currentState = ZombieState.RUNAWAY;
		}
	}
}