package me.noahvdaa.dripleaf.net.packet.in;

import me.noahvdaa.dripleaf.net.packet.def.PacketIn;

import java.io.DataInputStream;
import java.io.IOException;

public class PlayerPositionPacketIn extends PacketIn {

	private final double x;
	private final double feetY;
	private final double z;
	private final boolean onGround;

	public PlayerPositionPacketIn(double x, double feetY, double z, boolean onGround) {
		super("PlayerPositionPacketIn", 0x11);

		this.x = x;
		this.feetY = feetY;
		this.z = z;
		this.onGround = onGround;
	}

	public PlayerPositionPacketIn(DataInputStream in) throws IOException {
		super("PlayerPositoinPacketIn", 0x11);

		this.x = in.readDouble();
		this.feetY = in.readDouble();
		this.z = in.readDouble();
		this.onGround = in.readBoolean();
	}

	public double getX() {
		return this.x;
	}

	public double getFeetY() {
		return this.feetY;
	}

	public double getZ() {
		return this.z;
	}

	public boolean isOnGround() {
		return this.onGround;
	}

}
