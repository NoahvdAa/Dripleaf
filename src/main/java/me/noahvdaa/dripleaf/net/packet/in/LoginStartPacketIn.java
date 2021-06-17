package me.noahvdaa.dripleaf.net.packet.in;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketIn;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.DataInputStream;
import java.io.IOException;

public class LoginStartPacketIn extends PacketIn {

	final ConnectionHandler connectionHandler;

	private final String username;

	public LoginStartPacketIn(ConnectionHandler connectionHandler, String username) {
		super("LoginStartPacketIn", 0x00);

		this.connectionHandler = connectionHandler;
		this.username = username;
	}

	public LoginStartPacketIn(ConnectionHandler connectionHandler, DataInputStream in) throws IOException {
		super("LoginStartPacketIn", 0x00);

		this.connectionHandler = connectionHandler;
		this.username = DataUtils.readString(in);
	}

	public String getUsername() {
		return this.username;
	}

}
