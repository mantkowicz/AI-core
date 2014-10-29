package com.mantkowicz.ai.logger;

public class Logger 
{
	public static void log(Object object, Object message)
	{
		System.out.println( "|" + object.toString() +"| MESSAGE: " + message.toString() );
	}
}
