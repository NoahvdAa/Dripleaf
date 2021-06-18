package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.packet.def.PacketOut;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PongPacketOut extends PacketOut {

	private long payload;

	public PongPacketOut(long payload) {
		super("PongPacketOut", 0x01);

		this.payload = payload;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		buffer.writeLong(payload);

		return bufferArray.toByteArray();
	}

	public long getPayload() {
		return this.payload;
	}

	public void setPayload(long payload) {
		this.payload = payload;
	}

}
