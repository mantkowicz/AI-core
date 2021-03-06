package com.mantkowicz.ai.controller;

import com.badlogic.gdx.Gdx;

public class MouseController 
{
	private final static int MOUSE_SENSITIVITY = 25;
	
	private static float lastX = 0;
	private static float angle = 0;
	
	private static boolean buttonPressed = false;
	
	public static float getCursorAngle() 
	{
		float x = Gdx.input.getX();
		
		if( x < lastX )
		{
			angle += (lastX - x) / 50.0f;
		}
		if( x > lastX )
		{
			angle -= ( x - lastX ) / 50.0f;
		}

		lastX = x;
		
		return angle * MOUSE_SENSITIVITY;
	}
	
	public static boolean isMouseClicked()
	{
		if( Gdx.input.isTouched() && !buttonPressed )
		{
			buttonPressed = true;
			return true;
		}
		else if( !Gdx.input.isTouched() )
		{
			buttonPressed = false;
		}
		
		return false;
		/*
		{
			buttonPressed = false;
			return true;
		}*/
	}
}
