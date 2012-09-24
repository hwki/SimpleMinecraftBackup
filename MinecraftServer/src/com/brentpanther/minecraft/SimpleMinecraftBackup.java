package com.brentpanther.minecraft;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class SimpleMinecraftBackup {
	
	private Process p;
	public ServerListener sl;
	private BufferedWriter bw;
	private CommandListener cl;
	
	public SimpleMinecraftBackup(Integer backupMinutes) throws IOException {
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", "minecraft_server.jar", "nogui");
		builder.redirectErrorStream(true);
		p = builder.start();
		bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
		cl = new CommandListener(this);
		new Thread(cl).start();
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		sl = new ServerListener(br);
		new Thread(sl).start();
		PlayerInformation info = new PlayerInformation(sl);
		WorldSaver saver = new WorldSaver(this, backupMinutes, info);
		new Thread(saver).start();
	}
	
	public static void main(String[] args) throws IOException {
		if(args.length!=1 || !args[0].matches("[0-9]+")) {
			System.out.println("You need to supply the number of minutes between world backups.");
		} else {
			final SimpleMinecraftBackup a = new SimpleMinecraftBackup(Integer.valueOf(args[0]));
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable(){
				@Override
				public void run() {
					a.p.destroy();
				}
			}));
		}
	}

	public void sendMessage(String message) {
		try {
			bw.write(message + "\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
