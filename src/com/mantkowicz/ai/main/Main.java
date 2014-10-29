package com.mantkowicz.ai.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mantkowicz.ai.screens.GameScreen;

public class Main extends Game 
{
	SpriteBatch batch;
	Texture img;	
	
	@Override
	public void create () 
	{
		Screen gameScreen = new GameScreen(this);
		this.setScreen(gameScreen);
	}

	@Override
	public void render () 
	{
		super.render();
	}
}
