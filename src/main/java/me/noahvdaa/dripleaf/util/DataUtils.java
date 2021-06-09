package me.noahvdaa.dripleaf.util;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataUtils {

	public static int readVarInt(DataInputStream in) throws IOException {
		int numRead = 0;
		int result = 0;
		byte read;
		do {
			read = in.readByte();
			int value = (read & 0b01111111);
			result |= (value << (7 * numRead));

			numRead++;
			if (numRead > 5) {
				throw new RuntimeException("VarInt is too big");
			}
		} while ((read & 0b10000000) != 0);

		return result;
	}

	public static String readString(DataInputStream in) throws IOException {
		int length = readVarInt(in);
		byte[] result = new byte[length];

		in.readFully(result);

		return new String(result);
	}

	public static void writeVarInt(DataOutputStream out, int value) throws IOException {
		do {
			byte temp = (byte) (value & 0b01111111);
			value >>>= 7;
			if (value != 0) {
				temp |= 0b10000000;
			}
			out.writeByte(temp);
		} while (value != 0);
	}

	public static int getVarIntLength(int value) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(buffer);
		do {
			byte temp = (byte) (value & 0b01111111);
			value >>>= 7;
			if (value != 0) {
				temp |= 0b10000000;
			}
			out.writeByte(temp);
		} while (value != 0);
		return buffer.toByteArray().length;
	}

}
