package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PlayDisconnectPacketOut extends PacketOut {

	private String reason;

	public PlayDisconnectPacketOut(String reason) {
		super("PlayDisconnectOut", 0x1A);

		this.reason = reason;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		DataUtils.writeString(buffer, reason);

		return bufferArray.toByteArray();
	}

	public String getReason() {
		return this.reason;
	}

	public void setReadon(String reason) {
		this.reason = reason;
	}

}
