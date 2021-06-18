package me.noahvdaa.dripleaf.net.packet.def;

import me.noahvdaa.dripleaf.net.ConnectionHandler;

public class PacketIn extends Packet {

	private ConnectionHandler connectionHandler;

	public PacketIn(String name, int id) {
		super(name, id);
	}

	public ConnectionHandler getConnectionHandler() {
		return this.connectionHandler;
	}

}
