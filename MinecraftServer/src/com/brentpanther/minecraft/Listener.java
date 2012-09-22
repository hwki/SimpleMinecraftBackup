package com.brentpanther.minecraft;
import java.util.regex.Matcher;


public interface Listener {
	
	void handleMessage(Matcher m);

	String getMatcher();

}
