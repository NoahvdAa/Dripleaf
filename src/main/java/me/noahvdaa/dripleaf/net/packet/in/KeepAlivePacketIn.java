package me.noahvdaa.dripleaf.net.packet.in;

import me.noahvdaa.dripleaf.net.packet.def.PacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class KeepAlivePacketIn extends PacketIn {

	private final long payload;

	public KeepAlivePacketIn(long payload) {
		super("KeepAlivePacketIn", 0x0F);

		this.payload = payload;
	}

	public KeepAlivePacketIn(DataInputStream in) throws IOException {
		super("KeepAlivePacketIn", 0x0F);

		this.payload = in.readLong();
	}

	public long getPayload() {
		return this.payload;
	}

}
