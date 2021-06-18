package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PluginMessagePacketOut extends PacketOut {

	private final ConnectionHandler connectionHandler;

	private String identifier;
	private byte[] payload;

	public PluginMessagePacketOut(ConnectionHandler connectionHandler, String identifier, byte[] payload) {
		super("PluginMessagePacketOut", 0x18);

		this.connectionHandler = connectionHandler;
		this.identifier = identifier;
		this.payload = payload;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		DataUtils.writeString(buffer, identifier);
		buffer.write(payload);

		return bufferArray.toByteArray();
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public byte[] getPayload() {
		return this.payload;
	}

	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

}
