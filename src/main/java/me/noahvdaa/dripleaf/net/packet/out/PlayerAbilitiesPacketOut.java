package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerAbilitiesPacketOut extends PacketOut {

	final ConnectionHandler connectionHandler;

	private byte flags;
	private float flyingSpeed;
	private float fovMultiplier;

	public PlayerAbilitiesPacketOut(ConnectionHandler connectionHandler, byte flags, float flyingSpeed, float fovMultiplier) {
		super("PlayerAbilitiesPacketOut", 0x32);

		this.connectionHandler = connectionHandler;
		this.flags = flags;
		this.flyingSpeed = flyingSpeed;
		this.fovMultiplier = fovMultiplier;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		buffer.writeByte(flags);
		buffer.writeFloat(flyingSpeed);
		buffer.writeFloat(fovMultiplier);

		return bufferArray.toByteArray();
	}

	public byte getFlags() {
		return this.flags;
	}

	public void setFlags(byte flags) {
		this.flags = flags;
	}

	public float getFlyingSpeed() {
		return this.flyingSpeed;
	}

	public void setFlyingSpeed(float flyingSpeed) {
		this.flyingSpeed = flyingSpeed;
	}

	public float getFovMultiplier() {
		return this.fovMultiplier;
	}

	public void setFovMultiplier(float fovMultiplier) {
		this.fovMultiplier = fovMultiplier;
	}

}
