package me.noahvdaa.dripleaf.net;

import me.noahvdaa.dripleaf.Dripleaf;
import me.noahvdaa.dripleaf.net.packet.in.HandshakePacketIn;
import me.noahvdaa.dripleaf.net.packet.in.LoginStartPacketIn;
import me.noahvdaa.dripleaf.net.packet.in.PingPacketIn;
import me.noahvdaa.dripleaf.net.packet.out.*;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class ConnectionHandler extends Thread {

	public ConnectionStatus status = ConnectionStatus.HANDSHAKING;
	public final Socket connection;

	private DataInputStream in;
	private DataOutputStream out;

	private String username;
	private UUID uuid;

	public ConnectionHandler(Socket connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		System.out.println("Starting thread.");
		Dripleaf.getServer().activeThreads++;
		Dripleaf.getServer().connections.add(this);

		try {
			handle();
		} catch (Exception e) {
			System.out.println("Exception on connection thread:");
			e.printStackTrace();
		}

		status = ConnectionStatus.CLOSED;

		System.out.println("Stopping thread.");
		Dripleaf.getServer().activeThreads--;
		Dripleaf.getServer().connections.remove(this);
	}

	private void handle() throws IOException, InterruptedException {
		in = new DataInputStream(connection.getInputStream());
		out = new DataOutputStream(connection.getOutputStream());

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
						HandshakePacketIn handshakePacketIn = new HandshakePacketIn(this, in);
						status = handshakePacketIn.getNextStatus();
						break;
					case STATUS:
						StatusResponsePacketOut statusResponsePacketOut = new StatusResponsePacketOut(this, Dripleaf.BRAND + " " + Dripleaf.PROTOCOL_VERSION_STRING, Dripleaf.PROTOCOL_VERSION, 0, 0);
						statusResponsePacketOut.send(out);

						// Instantly follow up with pong packet.
						PongPacketOut pongPacketOut = new PongPacketOut(this, System.currentTimeMillis());
						pongPacketOut.send(out);

						// Close the connection afterwards.
						connection.close();
						return;
					case LOGIN:
						LoginStartPacketIn loginStartPacketIn = new LoginStartPacketIn(this, in);
						username = loginStartPacketIn.getUsername();
						uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes());

						LoginSuccessPacketOut loginSuccessPacketOut = new LoginSuccessPacketOut(this, uuid, username);
						loginSuccessPacketOut.send(out);

						// Sleep for a bit.
						Thread.sleep(500);

						JoinGamePacketOut joinGamePacketOut = new JoinGamePacketOut(this, 1, false, (byte) 0, (byte) 0, 0L, 1, 8, false, false, false, true);
						joinGamePacketOut.send(out);

						// Set correct status.
						status = ConnectionStatus.PLAYING;

						// Send spawn position packet. (Not location)
						SpawnPositionPacketOut spawnPositionPacketOut = new SpawnPositionPacketOut(this, (byte) 0, (byte) 0, (byte) 0, 90f);
						spawnPositionPacketOut.send(out);

						// Send  abilities.
						PlayerAbilitiesPacketOut playerAbilitiesPacketOut = new PlayerAbilitiesPacketOut(this, (byte) 0x07, 0f, 0.1f);
						playerAbilitiesPacketOut.send(out);

						// Finally, set location.
						PlayerPositionAndLookPacketOut playerPositionAndLookPacketOut = new PlayerPositionAndLookPacketOut(this, 0d, 0d, 0d, 90f, 0f, (byte) 0x00, 1, false);
						playerPositionAndLookPacketOut.send(out);
						break;
				}
			} else if (packetID == 0x01) {
				PingPacketIn pingPacketIn = new PingPacketIn(this, in);

				// Instantly follow up with pong packet.
				PongPacketOut pongPacketOut = new PongPacketOut(this, pingPacketIn.getPayload());
				pongPacketOut.send(out);
			} else {
				// Skip packet data.
				in.skipBytes(DataUtils.getVarIntLength(packetID) - packetSize);
			}
		}
	}

	public DataInputStream getIn() {
		return this.in;
	}

	public DataOutputStream getOut() {
		return this.out;
	}

	public String getUsername() {
		return this.username;
	}

	public UUID getUUID() {
		return this.uuid;
	}

}
