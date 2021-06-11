package me.noahvdaa.dripleaf.net.packet.in;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.ConnectionStatus;
import me.noahvdaa.dripleaf.net.packet.def.PacketIn;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.DataInputStream;
import java.io.IOException;

public class PingPacketIn extends PacketIn {

	private ConnectionHandler connectionHandler;

	private long payload;

	public PingPacketIn(ConnectionHandler connectionHandler, long payload) {
		super("PingPacketIn", 0x00);

		this.connectionHandler = connectionHandler;
		this.payload = payload;
	}

	public PingPacketIn(ConnectionHandler connectionHandler, DataInputStream in) throws IOException {
		super("PingPacketIn", 0x00);

		this.connectionHandler = connectionHandler;
		this.payload = in.readLong();
	}

	public long getPayload() {
		return this.payload;
	}

}
