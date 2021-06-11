package me.noahvdaa.dripleaf.net.packet.out;

import com.google.gson.JsonParseException;
import me.noahvdaa.dripleaf.Dripleaf;
import me.noahvdaa.dripleaf.net.ConnectionHandler;
import me.noahvdaa.dripleaf.net.packet.def.PacketOut;
import me.noahvdaa.dripleaf.util.DataUtils;
import me.noahvdaa.dripleaf.util.NBTUtils;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JoinGamePacketOut extends PacketOut {

	private String name = "JoinGamePacketOut";
	private int id = 0x24;

	private ConnectionHandler connectionHandler;

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

	public JoinGamePacketOut(ConnectionHandler connectionHandler, int entityId, boolean isHardcore, byte gamemode, byte previousGamemode, long hashedSeed, int maxPlayers, int viewDistance, boolean reducedDebugInfo, boolean enableRespawnScreen, boolean isDebug, boolean isFlat) {
		super("JoinGamePacketOut", 0x26);

		this.connectionHandler = connectionHandler;
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

		CompoundTag dimensionCodec = (CompoundTag) NBTUtil.read("./resources/dimensions.nbt").getTag();

		// For some reason, "palette" is in the NBT file. I don't know why it's there and how to get rid of it, so I'll just leave it here.
		dimensionCodec.remove("palette");

		DataUtils.writeCompoundTag(buffer, dimensionCodec);
		CompoundTag tag = null;
		ListTag<CompoundTag> list = dimensionCodec.getCompoundTag("minecraft:dimension_type").getListTag("value").asCompoundTagList();
		for (CompoundTag each : list) {

			if (each.getString("name").equals("minecraft:the_end")) {
				tag = each.getCompoundTag("element");
				break;
			}
		}
		DataUtils.writeCompoundTag(buffer, tag != null ? tag : list.get(0));

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

	// TODO: Add getters and setters, CBA rn.

}
