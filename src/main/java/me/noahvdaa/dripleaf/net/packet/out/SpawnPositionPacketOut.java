package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class SpawnPositionPacketOut extends PacketOut {

	private ConnectionHandler connectionHandler;

	private byte x;
	private byte y;
	private byte z;
	private float angle;

	public SpawnPositionPacketOut(ConnectionHandler connectionHandler, byte x, byte y, byte z, float angle) {
		super("SpawnPositionPacketOut", 0x4B);

		this.connectionHandler = connectionHandler;
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = angle;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		buffer.writeLong(((x & 0x3FFFFFF) << 38) | ((z & 0x3FFFFFF) << 12) | (y & 0xFFF));
		buffer.writeFloat(angle);

		return bufferArray.toByteArray();
	}

	// TODO: Add getters and setters, CBA rn.

}
