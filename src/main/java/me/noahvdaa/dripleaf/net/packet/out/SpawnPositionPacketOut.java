package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.packet.def.PacketOut;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SpawnPositionPacketOut extends PacketOut {

	private byte x;
	private byte y;
	private byte z;
	private float angle;

	public SpawnPositionPacketOut(byte x, byte y, byte z, float angle) {
		super("SpawnPositionPacketOut", 0x4B);

		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		buffer.writeLong(((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFF));
		buffer.writeFloat(angle);

		return bufferArray.toByteArray();
	}

	public byte getX() {
		return this.x;
	}

	public void setX(byte x) {
		this.x = x;
	}

	public byte getY() {
		return this.y;
	}

	public void setY(byte y) {
		this.y = y;
	}

	public byte getZ() {
		return this.z;
	}

	public void setZ(byte z) {
		this.z = z;
	}

	public float getAngle() {
		return this.angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

}
