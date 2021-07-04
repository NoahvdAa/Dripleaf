package me.noahvdaa.dripleaf.net;

import me.noahvdaa.dripleaf.Dripleaf;
import me.noahvdaa.dripleaf.net.packet.in.*;
import me.noahvdaa.dripleaf.AppConstants;
import me.noahvdaa.dripleaf.net.packet.out.*;
import me.noahvdaa.dripleaf.util.DataUtils;
import me.noahvdaa.dripleaf.util.Logger;

import java.io.*;
import java.net.Socket;
import java.util.UUID;

public class ConnectionHandler extends Thread {

	public ConnectionStatus status = ConnectionStatus.HANDSHAKING;
	public final Socket connection;
	public int threadId;

	private DataInputStream in;
	private DataOutputStream out;

	private String username;
	private UUID uuid;
	private HandshakePacketIn handshakeUsed;

	public ConnectionHandler(Socket connection) {
		this.connection = connection;
	}

	@Override
	public void run() {
		threadId = Dripleaf.getServer().threadId++;
		Thread.currentThread().setName(threadId + "-ANONYMOUS");

		Logger.debug("Starting connection thread.");

		Dripleaf.getServer().connections.add(this);

		try {
			handle();
		} catch (EOFException e) {
			// Swallow exception.
		} catch (Exception e) {
			Logger.debug("Exception on connection thread:");
			if (Dripleaf.getServer().isDebugMode())
				e.printStackTrace();
		}

		status = ConnectionStatus.CLOSED;

		Logger.debug("Stopping connection thread.");
		Dripleaf.getServer().connections.remove(this);
	}

	private void handle() throws IOException, InterruptedException {
		in = new DataInputStream(connection.getInputStream());
		out = new DataOutputStream(connection.getOutputStream());

		while (true) {
			int packetSize = DataUtils.readVarInt(in);
			int packetID = DataUtils.readVarInt(in);

			Logger.debug("Starting handling of 0x" + String.format("%02X", packetID) + " with size " + packetSize + ".");

			// Legacy pings aren't supported right now.
			if (packetSize == 0xFE) {
				connection.close();
				return;
			}

			if (packetID == 0x00) {
				switch (status) {
					case HANDSHAKING:
						handshakeUsed = new HandshakePacketIn(in);
						status = handshakeUsed.getNextStatus();
						break;
					case STATUS:
						StatusResponsePacketOut statusResponsePacketOut = new StatusResponsePacketOut(Dripleaf.BRAND + " " + Dripleaf.PROTOCOL_VERSION_STRING, Dripleaf.PROTOCOL_VERSION, 0, 0);
						statusResponsePacketOut.send(out);

						// Instantly follow up with pong packet.
						PongPacketOut pongPacketOut = new PongPacketOut(System.currentTimeMillis());
						pongPacketOut.send(out);

						// Close the connection afterwards.
						connection.close();
						return;
					case LOGIN:
						LoginStartPacketIn loginStartPacketIn = new LoginStartPacketIn(in);
						username = loginStartPacketIn.getUsername();
						uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes());

						Thread.currentThread().setName(threadId + "-" + username);
						Logger.debug("Player is " + username + " (" + uuid + ")!");

						// Bungee handling
						if (Dripleaf.getServer().isBungeecordModeMode()) {
							String[] bungeeData = handshakeUsed.getServerAddress().split("\\x00");

							if (bungeeData.length < 4) {
								// Kick 'em.
								LoginDisconnectPacketOut kickPacket = new LoginDisconnectPacketOut("{\"text\":\"Please connect using the Bungeecord proxy.\", \"color\": \"red\"}");
								kickPacket.send(out);

								connection.close();
								return;
							} else {
								// Replace the UUID.
								uuid = UUID.fromString(bungeeData[2].replaceFirst("([0-9a-fA-F]{8})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]{4})([0-9a-fA-F]+)", "$1-$2-$3-$4-$5"));
							}
						}

						LoginSuccessPacketOut loginSuccessPacketOut = new LoginSuccessPacketOut(uuid, username);
						loginSuccessPacketOut.send(out);

						// Sleep for a bit.
						Thread.sleep(250);

						JoinGamePacketOut joinGamePacketOut = new JoinGamePacketOut(1, false, (byte) 0, (byte) 0, 0L, 1, 8, false, false, false, true);
						joinGamePacketOut.send(out);

						// Send server brand packet.
						ByteArrayOutputStream brandBufferArray = new ByteArrayOutputStream();
						DataOutputStream brandBuffer = new DataOutputStream(brandBufferArray);
						DataUtils.writeString(brandBuffer, Dripleaf.BRAND_LEGACY_COLOR + Dripleaf.BRAND + " " + AppConstants.version + " (" + AppConstants.commit + ")§r  ");
						PluginMessagePacketOut brandPluginMessagePacketOut = new PluginMessagePacketOut("minecraft:brand", brandBufferArray.toByteArray());
						brandPluginMessagePacketOut.send(out);

						// Send spawn position packet. (Not location)
						SpawnPositionPacketOut spawnPositionPacketOut = new SpawnPositionPacketOut((byte) 0, (byte) 0, (byte) 0, 90f);
						spawnPositionPacketOut.send(out);

						// Send abilities.
						PlayerAbilitiesPacketOut playerAbilitiesPacketOut = new PlayerAbilitiesPacketOut((byte) 0x07, 0f, 0.1f);
						playerAbilitiesPacketOut.send(out);

						// Finally, set location.
						PlayerPositionAndLookPacketOut playerPositionAndLookPacketOut = new PlayerPositionAndLookPacketOut(0d, 0d, 0d, 90f, 0f, (byte) 0x00, 1, false);
						playerPositionAndLookPacketOut.send(out);

						// Set correct status.
						status = ConnectionStatus.PLAYING;
						break;
					default:
						// Don't do anything if the connection is in a different state.
						break;
				}
			} else if (packetID == 0x01) {
				PingPacketIn pingPacketIn = new PingPacketIn(in);

				// Instantly follow up with pong packet.
				PongPacketOut pongPacketOut = new PongPacketOut(pingPacketIn.getPayload());
				pongPacketOut.send(out);
			} else if (packetID == 0x0F) {
				// We don't need to respond with anything.
				new KeepAlivePacketIn(in);
			} else if(packetID == 0x11) {
				// We don't need to respond with anything.
				new PlayerPositionPacketIn(in);
			} else if(packetID == 0x13) {
				// We don't need to respond with anything.
				new PlayerRotationPacketIn(in);
			} else {
				Logger.debug("Packet 0x" + String.format("%02X", packetID) + " is unimplemented, ignoring it.");
				// Skip packet data.
				in.skipBytes(packetSize - DataUtils.getVarIntLength(packetID));
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
