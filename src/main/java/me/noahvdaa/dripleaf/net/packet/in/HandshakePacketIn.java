package me.noahvdaa.dripleaf.net.packet.in;

import me.noahvdaa.dripleaf.net.ConnectionStatus;
import me.noahvdaa.dripleaf.net.packet.def.PacketIn;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.DataInputStream;
import java.io.IOException;

public class HandshakePacketIn extends PacketIn {

	private final int protocolVersion;
	private final String serverAddress;
	private final int serverPort;
	private final ConnectionStatus nextStatus;

	public HandshakePacketIn(int protocolVersion, String serverAddress, int serverPort, ConnectionStatus nextStatus) {
		super("HandshakePacketIn", 0x00);

		this.protocolVersion = protocolVersion;
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.nextStatus = nextStatus;
	}

	public HandshakePacketIn(DataInputStream in) throws IOException {
		super("HandshakePacketIn", 0x00);

		protocolVersion = DataUtils.readVarInt(in);
		serverAddress = DataUtils.readString(in);
		serverPort = in.readUnsignedShort();
		int nextState = in.read();
		nextStatus = nextState == 1 ? ConnectionStatus.STATUS : ConnectionStatus.LOGIN;
	}

	public int getProtocolVersion() {
		return this.protocolVersion;
	}

	public String getServerAddress() {
		return this.serverAddress;
	}

	public int getServerPort() {
		return this.serverPort;
	}

	public ConnectionStatus getNextStatus() {
		return this.nextStatus;
	}

}
