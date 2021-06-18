package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class LoginSuccessPacketOut extends PacketOut {

	private final ConnectionHandler connectionHandler;

	private UUID uuid;
	private String username;

	public LoginSuccessPacketOut(ConnectionHandler connectionHandler, UUID uuid, String username) {
		super("LoginSuccessPacketOut", 0x02);

		this.connectionHandler = connectionHandler;
		this.uuid = uuid;
		this.username = username;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		buffer.writeLong(uuid.getMostSignificantBits());
		buffer.writeLong(uuid.getLeastSignificantBits());
		DataUtils.writeString(buffer, username);

		return bufferArray.toByteArray();
	}

	public UUID getUUID() {
		return this.uuid;
	}

	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
