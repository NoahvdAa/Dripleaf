package me.noahvdaa.dripleaf.net.packet.def;

public class Packet {

	private final String name;
	private final int id;

	public Packet(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

}
