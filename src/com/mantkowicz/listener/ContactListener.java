package com.mantkowicz.listener;

import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.Player;
import com.mantkowicz.ai.actors.Zombie;

public class ContactListener 
{
	private Array<Zombie> zombies;
	private Array<Column> columns;
	private Player player;
		
	public ContactListener(Array<Zombie> zombie, Array<Column> columns, Player player)
	{
		this.zombies = zombies;
		this.columns = columns;
		this.player = player;
	}
	
	public void draw()
	{

	}
}
