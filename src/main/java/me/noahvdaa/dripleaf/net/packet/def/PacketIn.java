package me.noahvdaa.dripleaf.net.packet.def;

import me.noahvdaa.dripleaf.net.ConnectionHandler;

public class PacketIn extends Packet {

	public PacketIn(String name, int id) {
		super(name, id);
	}

	private ConnectionHandler connectionHandler;

	public ConnectionHandler getConnectionHandler() {
		return this.connectionHandler;
	}

}
