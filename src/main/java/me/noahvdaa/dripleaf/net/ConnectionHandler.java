package me.noahvdaa.dripleaf.net;

import me.noahvdaa.dripleaf.Dripleaf;
import me.noahvdaa.dripleaf.DripleafServer;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler extends Thread {

	private final Socket connection;
	private ConnectionStatus status = ConnectionStatus.HANDSHAKING;

	public ConnectionHandler(Socket connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		System.out.println("Starting thread.");
		Dripleaf.getServer().activeThreads++;
		try {
			handle();
		} catch (IOException e) {
			System.out.println("IOException on connection thread:");
			e.printStackTrace();
		}
		System.out.println("Stopping thread.");
		Dripleaf.getServer().activeThreads--;
	}

	private void handle() throws IOException {
		DataInputStream in = new DataInputStream(connection.getInputStream());
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());

		while (true) {
			int packetSize = DataUtils.readVarInt(in);
			int packetID = DataUtils.readVarInt(in);
			System.out.println("Starting handling of " + packetID + " with size " + packetSize + ".");

			// Legacy pings aren't supported right now.
			if (packetSize == 0xFE) {
				connection.close();
				return;
			}

			if (packetID == 0x00) {
				switch (status) {
					case HANDSHAKING:
						int protocolVersion = DataUtils.readVarInt(in);
						String serverAddress = DataUtils.readString(in);
						int serverPort = in.readUnsignedShort();
						int nextState = in.read();

						status = nextState == 1 ? ConnectionStatus.STATUS : ConnectionStatus.LOGIN;
						break;
					case STATUS:
						String response = "{\"version\":{\"name\":\"" + DripleafServer.BRAND + " " + DripleafServer.PROTOCOL_VERSION_STRING + "\",\"protocol\":" + DripleafServer.PROTOCOL_VERSION + "},\"players\":{\"max\":" + Dripleaf.getServer().activeThreads + ",\"online\":0,\"sample\":[]},\"description\":{\"text\":\"Powered by Dripleaf\",\"color\":\"green\"},\"favicon\":\"\"}";
						DataUtils.writeVarInt(out, DataUtils.getVarIntLength(0x00) + DataUtils.getVarIntLength(response.length()) + response.length());
						DataUtils.writeVarInt(out, 0x00);
						DataUtils.writeVarInt(out, response.length());
						out.write(response.getBytes());
						// Instantly follow up with ping packet.
						DataUtils.writeVarInt(out, DataUtils.getVarIntLength(0x01) + 8);
						DataUtils.writeVarInt(out, 0x01);
						out.writeLong(System.currentTimeMillis());
						return;
				}
			} else {
				// Unknown packet.
				System.out.println("Unknown packet.");
			}
		}
	}

}
