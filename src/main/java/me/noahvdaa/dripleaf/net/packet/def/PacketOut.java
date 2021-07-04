package me.noahvdaa.dripleaf.net.packet.def;

import me.noahvdaa.dripleaf.Dripleaf;
import me.noahvdaa.dripleaf.util.DataUtils;
import me.noahvdaa.dripleaf.util.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketOut extends Packet {

	public PacketOut(String name, int id) {
		super(name, id);
	}

	public byte[] serialize() throws IOException {
		return null;
	}

	public void send(DataOutputStream out) throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);
		// Include id in buffer so it's calculated in size.
		DataUtils.writeVarInt(buffer, getId());
		buffer.write(serialize());

		// Write size first.
		DataUtils.writeVarInt(out, buffer.size());

		// Then the rest.
		out.write(bufferArray.toByteArray());

		Logger.debug("Sending " + getName() + " with id " + Integer.toHexString(getId()) + " and size " + buffer.size());
	}

}
