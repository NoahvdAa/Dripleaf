package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerPositionAndLookPacketOut extends PacketOut {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private byte flags;
	private int teleportId;
	private boolean dismountVehicle;

	public PlayerPositionAndLookPacketOut(double x, double y, double z, float yaw, float pitch, byte flags, int teleportId, boolean dismountVehicle) {
		super("PlayerPositionAndLookPacketOut", 0x38);

		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.flags = flags;
		this.teleportId = teleportId;
		this.dismountVehicle = dismountVehicle;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		buffer.writeDouble(x);
		buffer.writeDouble(y);
		buffer.writeDouble(z);
		buffer.writeFloat(yaw);
		buffer.writeFloat(pitch);
		buffer.writeByte(flags);
		DataUtils.writeVarInt(buffer, teleportId);
		buffer.writeBoolean(dismountVehicle);

		return bufferArray.toByteArray();
	}

	public double getX() {
		return this.x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return this.y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return this.z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public float getYaw() {
		return this.yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public byte getFlags() {
		return this.flags;
	}

	public void setFlags(byte flags) {
		this.flags = flags;
	}

	public int getTeleportId() {
		return this.teleportId;
	}

	public void setTeleportId(int teleportId) {
		this.teleportId = teleportId;
	}

	public boolean shouldDismountVehicle() {
		return this.dismountVehicle;
	}

	public void setShouldDismountVehicle(boolean dismountVehicle) {
		this.dismountVehicle = dismountVehicle;
	}

}
