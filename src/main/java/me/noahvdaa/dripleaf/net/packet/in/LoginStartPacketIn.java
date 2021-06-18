package me.noahvdaa.dripleaf.net.packet.in;

import me.noahvdaa.dripleaf.net.packet.def.PacketIn;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.DataInputStream;
import java.io.IOException;

public class LoginStartPacketIn extends PacketIn {

	private final String username;

	public LoginStartPacketIn(String username) {
		super("LoginStartPacketIn", 0x00);

		this.username = username;
	}

	public LoginStartPacketIn(DataInputStream in) throws IOException {
		super("LoginStartPacketIn", 0x00);

		this.username = DataUtils.readString(in);
	}

	public String getUsername() {
		return this.username;
	}

}
