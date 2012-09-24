package com.brentpanther.minecraft;

import java.util.Scanner;

public class CommandListener implements Runnable {
	
	private SimpleMinecraftBackup smb;
	
	public CommandListener(SimpleMinecraftBackup smb) {
		this.smb = smb;
	}

	@Override
	public void run() {
		Scanner scanner = new Scanner(System.in);
		String line = "";
		while((line = scanner.nextLine()) != null) {
			smb.sendMessage(line);
			if(line.equalsIgnoreCase("STOP"))  break;
		}
		scanner.close();
	}	

}
