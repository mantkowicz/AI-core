package com.mantkowicz.ai.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
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
	
	private enum CollisionSide
	{
		NONE,
		LEFT,
		FRONT,
		RIGHT,
		LEFTFRONT,
		RIGHTFRONT,
		LEFTRIGHTFRONT
	}
	
	private Player player;
	private Array<Column> columns;
	private Array<Zombie> zombies;
	
	private Image rageImage;
	private Image runImage;
	
	private float collisionAngle = 0;
	
	private Vector2 wanderTargetVector;
	
	private int currentFrame;
	
	private boolean safe;
	public int power;
	
	public boolean rage = false;
	
	private ZombieState currentState;
	
	public Zombie(float x, float y)
	{
		super("gfx/zombie.png", x, y);
		
		currentFrame = 0;
		
		wanderTargetVector = new Vector2(200.0f, 0.0f);
		
		safe = false;
		power = 1;
					
		currentState = ZombieState.TRESSPASS;
	}
	
	public void setEnvironment(Player player, Array<Column> columns, Array<Zombie> zombies, Stage stage)
	{
		//--jeszcze dodawanie rageImage bo to dobre miejsce mimo ze nazwa zla
		Texture t = new Texture( Gdx.files.internal("gfx/skull.png") );
		t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		rageImage = new Image( t  );
		rageImage.setVisible(false);
		stage.addActor(rageImage);
		
		t = new Texture( Gdx.files.internal("gfx/runAway.png") );
		t.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		runImage = new Image( t );
		runImage.setVisible(false);
		stage.addActor(runImage);
		
		this.columns = columns;
		this.zombies = zombies;
		
		this.player = player;
	}
	
	@Override
	protected void step() 
	{						
		checkSafe();
		updateState();
			
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
		
		CollisionSide collisionSide = this.handleCollision( rotation );
		
		float avoidAngle = 90 - ( Math.abs(collisionAngle) % 90 );
		Logger.log(this, avoidAngle);
		
		if( collisionSide == CollisionSide.LEFT )
		{
			forward = new Vector2(0,1);

			rotation -= avoidAngle;
		}
		else if( collisionSide == CollisionSide.RIGHT )
		{
			forward = new Vector2(0,1);
			rotation += avoidAngle;
		}
		else if( collisionSide == CollisionSide.FRONT )
		{
			forward = new Vector2(0.0f, 1.0f);
			rotation += avoidAngle;
		}
		else if( collisionSide == CollisionSide.RIGHTFRONT )
		{
			forward = new Vector2(0.0f, 1.0f);
			rotation += avoidAngle;
		}
		else if( collisionSide == CollisionSide.LEFTFRONT )
		{
			forward = new Vector2(0.0f, 1.0f);
			rotation -= avoidAngle;
		}
		else if( collisionSide == CollisionSide.LEFTRIGHTFRONT )
		{
			forward = new Vector2(0.0f, 1.0f);
			rotation += avoidAngle;
		}
				
		this.currentFrame++;
	}

	private void attack()
	{
		rageImage.toFront();
		rageImage.setRotation(-this.getRotation());
		rageImage.setOrigin((rageImage.getWidth()/2.0f),(rageImage.getHeight()/2.0f));
		rageImage.setPosition(this.getX() + (this.getWidth()/2.0f) - (rageImage.getWidth()/2.0f), this.getY() + (this.getHeight()/2.0f) - (rageImage.getHeight()/2.0f));
		
		
		seek( player.getX(), player.getY() );
		
		turbo = 2.0f;
	}
	
	private void wander()
	{		
		if( this.currentFrame % 100 == 0 )
		{
			rotation = (float)Math.random() * 360.0f;
		}
	}
	
	private void checkSafe()
	{
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
	{turbo = 4.0f;
		runImage.toFront();
		runImage.setRotation(-this.getRotation());
		runImage.setOrigin((rageImage.getWidth()/2.0f),(rageImage.getHeight()/2.0f));
		runImage.setPosition(this.getX() + (this.getWidth()/2.0f) - (rageImage.getWidth()/2.0f), this.getY() + (this.getHeight()/2.0f) - (rageImage.getHeight()/2.0f));
		
		
		Column nearestColumn = getNearestColumn();
		
		seek(nearestColumn.getSafePoint().x, nearestColumn.getSafePoint().y);
	}
	
	private Column getNearestColumn()
	{
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
	
	private CollisionSide handleCollision(float r)
	{
		boolean left = false, right = false, front = false;
		
		float[] vertices = this.getCollisionBoxVertices(r);
		
		CollisionSide collisionSide = CollisionSide.NONE;
		
		for(Column column: columns)
		{
			//front side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[2],vertices[3]), new Vector2(vertices[4], vertices[5]), column.getCenter(), column.getSquaredRadius() ) )
			{
				front = true;
			}
			
			//left side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[0],vertices[1]), new Vector2(vertices[2], vertices[3]), column.getCenter(), column.getSquaredRadius() ) )
			{
				left = true;
			}
						
			//right side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[4],vertices[5]), new Vector2(vertices[6], vertices[7]), column.getCenter(), column.getSquaredRadius() ) )
			{
				right = true;
			}	
			
			if( left || right || front )
			{
				Vector2 colvec = new Vector2( this.getCenter().x - column.getCenter().x, this.getCenter().y - column.getCenter().y );
				collisionAngle = colvec.angle();
			}
		}
		
		for(Zombie zombie: zombies)
		{
			if( zombie.getX() == this.getX() && zombie.getY() == this.getY() ) continue;
			
			float zombieSquaredRadius = ( (zombie.getWidth() + 40.0f) / 2.0f);
			zombieSquaredRadius *= zombieSquaredRadius;
			
			//front side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[2],vertices[3]), new Vector2(vertices[4], vertices[5]), zombie.getCenter(), zombieSquaredRadius ) )
			{
				front = true;
			}
			
			//left side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[0],vertices[1]), new Vector2(vertices[2], vertices[3]), zombie.getCenter(), zombieSquaredRadius ) )
			{
				left = true;
			}
				
			//right side
			if( Intersector.intersectSegmentCircle(new Vector2(vertices[4],vertices[5]), new Vector2(vertices[6], vertices[7]), zombie.getCenter(), zombieSquaredRadius ) )
			{
				right = true;
			}
			
			if( left || right || front )
			{
				Vector2 colvec = new Vector2( this.getCenter().x - zombie.getCenter().x, this.getCenter().y - zombie.getCenter().y );
				collisionAngle = colvec.angle();
			}
		}
		
		if( left && right && front )
		{
			collisionSide = CollisionSide.LEFTRIGHTFRONT;
		}
		else if( !left && right && front )
		{
			collisionSide = CollisionSide.RIGHTFRONT;
		}
		else if( left && !right && front )
		{
			collisionSide = CollisionSide.LEFTFRONT;
		}
		else if( !left && !right && front )
		{
			collisionSide = CollisionSide.FRONT;
		}
		else if( !left && right && !front )
		{
			collisionSide = CollisionSide.RIGHT;
		}
		else if( left && !right && !front )
		{
			collisionSide = CollisionSide.LEFT;
		}
		
		return collisionSide;
	}
	
	public float[] getCollisionBoxVertices(float r)
	{
		Vector2 v1 = new Vector2( -this.getWidth() / 2.0f, 0 );
		Vector2 v2 = new Vector2( -this.getWidth() / 2.0f, this.getWidth() );
		Vector2 v3 = new Vector2(  this.getWidth() / 2.0f, this.getWidth() );
		Vector2 v4 = new Vector2(  this.getWidth() / 2.0f, 0 );
		
		v1.rotate( r );
		v2.rotate( r );
		v3.rotate( r );
		v4.rotate( r );
		
		v1.add(this.getCenter().add(forward));
		v2.add(this.getCenter().add(forward));
		v3.add(this.getCenter().add(forward));
		v4.add(this.getCenter().add(forward));
		
		float[] vertices = new float[]{ v1.x, v1.y, v2.x, v2.y, v3.x, v3.y, v4.x, v4.y };
		
		return vertices;
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
	}
	
	protected void updateState()
	{
		if( rage )
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
}