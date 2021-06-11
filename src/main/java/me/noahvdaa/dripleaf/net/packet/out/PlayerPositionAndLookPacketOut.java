package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayerPositionAndLookPacketOut extends PacketOut {

	private ConnectionHandler connectionHandler;

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private byte flags;
	private int teleportId;
	private boolean dismountVehicle;

	public PlayerPositionAndLookPacketOut(ConnectionHandler connectionHandler, double x, double y, double z, float yaw, float pitch, byte flags, int teleportId, boolean dismountVehicle) {
		super("PlayerPositionAndLookPacketOut", 0x38);

		this.connectionHandler = connectionHandler;
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

	// TODO: Add getters and setters, CBA rn.

}
