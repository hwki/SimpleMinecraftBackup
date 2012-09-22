package com.brentpanther.minecraft;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ServerListener implements Runnable {
	
	private BufferedReader br;
	private List<Listener> listeners;
	
	public ServerListener(BufferedReader br) {
		this.br = br;
		listeners = new CopyOnWriteArrayList<Listener>();
	}
	
	public void add(Listener listener) {
		listeners.add(listener);
	}
	
	public void remove(Listener listener) {
		listeners.remove(listener);
	}

	@Override
	public void run() {
		String line = null;
		try {
			while((line = br.readLine()) != null) {
				String scrubbedLine = line.substring(line.indexOf("]")+2);
				for (Listener listen : listeners) {
					if(scrubbedLine.matches(listen.getMatcher() + ".*")) {
						Matcher m = Pattern.compile(listen.getMatcher()).matcher(scrubbedLine);
						m.find();
						listen.handleMessage(m);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Shutting down...");
		System.exit(0);
	}

}
