package com.mantkowicz.ai.listener;

import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.Zombie;
import com.mantkowicz.ai.vars.CollisionSide;

public class Collision 
{
	public Array<Column> columns;
	public Array<CollisionSide> columnCollisionSides;
	
	public Array<Zombie> zombies;
	public Array<CollisionSide> zombieCollisionSides;
	
	public Collision() 
	{		
		this.columns = new Array<Column>();
		this.columnCollisionSides = new Array<CollisionSide>();
		
		this.zombies = new Array<Zombie>();
		this.zombieCollisionSides = new Array<CollisionSide>();
	}
	
	public void addCollision(Column column, CollisionSide columnCollisionSide)
	{
		this.columns.add(column);
		this.columnCollisionSides.add(columnCollisionSide);
	}
	
	public void addCollision(Zombie zombie, CollisionSide zombieCollisionSide)
	{
		this.zombies.add(zombie);
		this.zombieCollisionSides.add(zombieCollisionSide);
	}
	
	public boolean isNull()
	{
		if( columns.size > 0 && zombies.size > 0 )
		{
			return false;
		}
		
		return true;
	}

	public boolean willBeColumnCollision() 
	{
		if( columnCollisionSides.size == 0 )
		{
			return false;
		}

		for(int i = 0; i < columnCollisionSides.size; i++)
		{
			if( columnCollisionSides.get(i) == CollisionSide.PRELEFT || columnCollisionSides.get(i) == CollisionSide.PRERIGHT )
			{
				return true;
			}
		}
		
		return false;
	}
}
