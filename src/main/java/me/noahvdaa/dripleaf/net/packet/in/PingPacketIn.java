package me.noahvdaa.dripleaf.net.packet.in;

import me.noahvdaa.dripleaf.net.packet.def.PacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class PingPacketIn extends PacketIn {

	private final long payload;

	public PingPacketIn(long payload) {
		super("PingPacketIn", 0x00);

		this.payload = payload;
	}

	public PingPacketIn(DataInputStream in) throws IOException {
		super("PingPacketIn", 0x00);

		this.payload = in.readLong();
	}

	public long getPayload() {
		return this.payload;
	}

}
