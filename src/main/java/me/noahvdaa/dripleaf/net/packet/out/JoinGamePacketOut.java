package me.noahvdaa.dripleaf.net.packet.out;

import me.noahvdaa.dripleaf.Dripleaf;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class JoinGamePacketOut extends PacketOut {

	private int entityId;
	private boolean isHardcore;
	private byte gamemode;
	private byte previousGamemode;
	private long hashedSeed;
	private int maxPlayers;
	private int viewDistance;
	private boolean reducedDebugInfo;
	private boolean enableRespawnScreen;
	private boolean isDebug;
	private boolean isFlat;

	public JoinGamePacketOut(int entityId, boolean isHardcore, byte gamemode, byte previousGamemode, long hashedSeed, int maxPlayers, int viewDistance, boolean reducedDebugInfo, boolean enableRespawnScreen, boolean isDebug, boolean isFlat) {
		super("JoinGamePacketOut", 0x26);

		this.entityId = entityId;
		this.isHardcore = isHardcore;
		this.gamemode = gamemode;
		this.previousGamemode = previousGamemode;
		this.hashedSeed = hashedSeed;
		this.maxPlayers = maxPlayers;
		this.viewDistance = viewDistance;
		this.reducedDebugInfo = reducedDebugInfo;
		this.enableRespawnScreen = enableRespawnScreen;
		this.isDebug = isDebug;
		this.isFlat = isFlat;
	}

	@Override
	public byte[] serialize() throws IOException {
		ByteArrayOutputStream bufferArray = new ByteArrayOutputStream();
		DataOutputStream buffer = new DataOutputStream(bufferArray);

		buffer.writeInt(entityId);
		buffer.writeBoolean(isHardcore);
		buffer.writeByte(gamemode);
		buffer.writeByte(previousGamemode);
		DataUtils.writeVarInt(buffer, 1);
		DataUtils.writeString(buffer, "minecraft:the_end");

		DataUtils.writeCompoundTag(buffer, Dripleaf.getServer().sharedObjectCacher.getDimensionCodec());
		DataUtils.writeCompoundTag(buffer, Dripleaf.getServer().sharedObjectCacher.getDimensionTag());

		DataUtils.writeString(buffer, "minecraft:the_end");
		buffer.writeLong(hashedSeed);
		DataUtils.writeVarInt(buffer, maxPlayers);
		DataUtils.writeVarInt(buffer, viewDistance);
		buffer.writeBoolean(reducedDebugInfo);
		buffer.writeBoolean(enableRespawnScreen);
		buffer.writeBoolean(isDebug);
		buffer.writeBoolean(isFlat);

		return bufferArray.toByteArray();
	}

	public int getEntityId() {
		return this.entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public boolean getHardcore() {
		return this.isHardcore;
	}

	public void setHardcore(boolean isHardcore) {
		this.isHardcore = isHardcore;
	}

	public byte getGamemode() {
		return this.gamemode;
	}

	public void setGamemode(byte gamemode) {
		this.gamemode = gamemode;
	}

	public byte getPreviousGamemode() {
		return this.previousGamemode;
	}

	public void setPreviousGamemode(byte previousGamemode) {
		this.previousGamemode = previousGamemode;
	}

	public long getHashedSeed() {
		return this.hashedSeed;
	}

	public void setHashedSeed(long hashedSeed) {
		this.hashedSeed = hashedSeed;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public int getViewDistance() {
		return this.viewDistance;
	}

	public void setViewDistance(int viewDistance) {
		this.viewDistance = viewDistance;
	}

	public boolean hasReducedDebugInfo() {
		return this.reducedDebugInfo;
	}

	public void setReducedDebugInfo(boolean hasReducedDebugInfo) {
		this.reducedDebugInfo = reducedDebugInfo;
	}

	public boolean hasRespawnScreenEnabled() {
		return this.enableRespawnScreen;
	}

	public void setRespawnScreenEnabled(boolean enableRespawnScreen) {
		this.enableRespawnScreen = enableRespawnScreen;
	}

	public boolean isDebug() {
		return this.isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public boolean isFlat() {
		return this.isFlat;
	}

	public void setFlat(boolean isFlat) {
		this.isFlat = isFlat;
	}

}
