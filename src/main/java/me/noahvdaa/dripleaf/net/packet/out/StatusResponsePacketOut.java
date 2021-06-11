package me.noahvdaa.dripleaf.net.packet.out;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.noahvdaa.dripleaf.Dripleaf;
import me.noahvdaa.dripleaf.DripleafLogo;
import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StatusResponsePacketOut extends PacketOut {

	private ConnectionHandler connectionHandler;

	private String versionName;
	private int protocolVersion;
	private int maxPlayers;
	private int onlinePlayers;

	public StatusResponsePacketOut(ConnectionHandler connectionHandler, String versionName, int protocolVersion, int maxPlayers, int onlinePlayers) {
		super("StatusResponsePacketOut", 0x00);

		this.connectionHandler = connectionHandler;
		this.versionName = versionName;
		this.protocolVersion = protocolVersion;
		this.maxPlayers = maxPlayers;
		this.onlinePlayers = onlinePlayers;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		JsonObject motd = new JsonObject();

		JsonObject version = new JsonObject();
		version.addProperty("name", versionName);
		version.addProperty("protocol", protocolVersion);

		JsonObject players = new JsonObject();
		players.addProperty("max", maxPlayers);
		players.addProperty("online", onlinePlayers);
		players.add("sample", new JsonArray());

		JsonObject description = new JsonObject();
		// Customization will be available soon.
		description.addProperty("color", Dripleaf.BRAND_COLOR);
		description.addProperty("text", "Powered by Dripleaf");

		motd.add("version", version);
		motd.add("players", players);
		motd.add("description", description);
		motd.addProperty("favicon", DripleafLogo.Base64); // Temporary.

		String motdString = motd.toString();

		DataUtils.writeString(buffer, motdString);

		return bufferArray.toByteArray();
	}

	public String getVersionName() {
		return this.versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	public void setProtocolVersion(int protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getOnlinePlayers() {
		return this.onlinePlayers;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

}
