package com.mantkowicz.ai.world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mantkowicz.ai.actors.Bullet;
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
	
	public Stage stage;
	
	public Array<Column> columns;
	public Array<Zombie> zombies;
	public Array<Bullet> bullets;
	public Player player;
	
	public Vector2 worldCenter;
	
	private World()
	{
		columns = new Array<Column>();
		zombies = new Array<Zombie>();
		bullets = new Array<Bullet>();
		player = new Player(0,0);
	}
	
	public void initialize(Stage stage, Array<Column> columns, Array<Zombie> zombies, Player player, Vector2 worldCenter)
	{
		this.stage = stage;
		
		this.player = player;
		stage.addActor( this.player );
		
		this.columns = columns;
		for(Column column: columns)
		{
			stage.addActor(column);
		}
		
		this.zombies = zombies;
		for(Zombie zombie: zombies)
		{			
			stage.addActor(zombie);
			stage.addActor(zombie.rageImage);
			stage.addActor(zombie.runImage);
		}
		
		for(Column column: columns)
		{
			column.toBack();
		}
		
		this.worldCenter = worldCenter;
	}
	
	public void addBullet(Bullet bullet)
	{
		this.bullets.add(bullet);
		this.stage.addActor(bullet);
	}
	
	public void removeBullet(Bullet bullet)
	{
		bullet.clear();
		bullet.remove();
		
		bullets.removeValue(bullet, false);
	}
	
	public void removeZombie(Zombie zombie)
	{
		zombie.clear();
		zombie.remove();
		
		zombie.rageImage.clear();
		zombie.rageImage.remove();
		
		zombie.runImage.clear();
		zombie.runImage.remove();
		
		zombie.dispose();
		
		zombies.removeValue(zombie, false);
	}
	
	
}
