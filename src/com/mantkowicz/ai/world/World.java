package com.mantkowicz.ai.world;

import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.Column;
import com.mantkowicz.ai.actors.Player;
import com.mantkowicz.ai.actors.Zombie;

public class World 
{
	public static World INSTANCE = new World();
	public static World getInstance()
	{
		return INSTANCE;
	}
	
	public Array<Column> columns;
	public Array<Zombie> zombies;
	public Player player;
	
	private World()
	{
		columns = new Array<Column>();
		zombies = new Array<Zombie>();
		player = new Player(0,0);
	}
	
	public void initialize(Array<Column> columns, Array<Zombie> zombies, Player player)
	{
		this.columns = columns;
		this.zombies = zombies;
		this.player = player;
	}
}
