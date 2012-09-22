package com.brentpanther.minecraft;
import java.util.regex.Matcher;


public class WorldSaver implements Listener, Runnable {
	
	private SimpleMinecraftBackup t;
	private int sleepTime;
	private State state;
	private String worldName;
	private PlayerInformation info;
	private enum State {
		LOADING("Preparing level \"(.*)\""), 
		WAITING(""), 
		BACKING_UP("Saved the world");	
		String matcher;
		
		State(String matcher) {
			this.matcher = matcher;
		}
	}
	
	WorldSaver(SimpleMinecraftBackup t, int minutes, PlayerInformation info) {
		this.t = t;
		sleepTime = minutes * 60000;
		state = State.LOADING;
		this.info = info;
		t.sl.add(this);
	}

	@Override
	public void handleMessage(Matcher m) {
		switch(state) {
			case LOADING:
				worldName = m.group(1);
				t.sl.remove(this);
				state = State.WAITING;
				break;
			case WAITING:
				break;
			case BACKING_UP:
				try {
					BackupWorld.backup(worldName);
				} catch (Exception e) {
					e.printStackTrace();
				}
				t.sendMessage("say The world is saved!");
				t.sl.remove(this);
				state = State.WAITING;
				break;
		}
	}

	@Override
	public String getMatcher() {
		return state.matcher;
	}

	@Override
	public void run() {
		while(true) {
			try {
				if(state != State.WAITING) {
					Thread.sleep(2000);
					continue;
				} 
				Thread.sleep(sleepTime);
				if(!info.shouldSave(sleepTime)) continue;
				state = State.BACKING_UP;
				t.sl.add(this);
				t.sendMessage("say The world is being saved...");
				t.sendMessage("save-off");
				t.sendMessage("save-all");
			} catch (InterruptedException e) {
				break;
			}
		}
	}

}
