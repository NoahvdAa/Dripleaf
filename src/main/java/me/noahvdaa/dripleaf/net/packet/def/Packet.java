package me.noahvdaa.dripleaf.net.packet.def;

public class Packet {

	private String name;
	private int id;

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
