package com.brentpanther.minecraft;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;


public class PlayerInformation {
	
	private List<String> players = new CopyOnWriteArrayList<String>();
	private long lastLeave = 0;
	
	public PlayerInformation(ServerListener sl) {
		PlayerJoinListener join = new PlayerJoinListener();
		PlayerQuitListener quit = new PlayerQuitListener();
		sl.add(join);
		sl.add(quit);
	}

	public boolean shouldSave(long lastTime) {
		return !players.isEmpty() || System.currentTimeMillis() - lastTime < lastLeave;
	}
	
	class PlayerJoinListener implements Listener {

		@Override
		public void handleMessage(Matcher m) {
			players.add(m.group(1));
		}

		@Override
		public String getMatcher() {
			return "(.*)\\[.*\\] logged in";
		}
		
	}
	
	class PlayerQuitListener implements Listener {

		@Override
		public void handleMessage(Matcher m) {
			players.remove(m.group(1));
			lastLeave = System.currentTimeMillis();
		}

		@Override
		public String getMatcher() {
			return "(.*) lost connection";
		}
		
	}

}
