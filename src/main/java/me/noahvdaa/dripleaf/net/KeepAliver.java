package me.noahvdaa.dripleaf.net;

import me.noahvdaa.dripleaf.Dripleaf;
import me.noahvdaa.dripleaf.net.packet.out.KeepAlivePacketOut;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class KeepAliver extends Thread {

	@Override
	public void run() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (ConnectionHandler connection : Dripleaf.getServer().connections) {
					// Ignore everyone that isn't playing.
					if (connection.status != ConnectionStatus.PLAYING) continue;
					KeepAlivePacketOut keepAlivePacketOut = new KeepAlivePacketOut(System.currentTimeMillis());
					try {
						keepAlivePacketOut.send(connection.getOut());
					} catch (IOException e) {
						// Swallow the exception.
					}
				}
			}
		}, 1000L, 1000L);
	}

}
