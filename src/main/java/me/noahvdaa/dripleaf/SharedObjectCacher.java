package me.noahvdaa.dripleaf;

import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.ListTag;

import java.io.IOException;

public class SharedObjectCacher {

	private final CompoundTag dimensionCodec;
	private final CompoundTag dimensionTag;

	public SharedObjectCacher() throws IOException {
		CompoundTag dimensionCodec = (CompoundTag) NBTUtil.read("./resources/dimensions.nbt").getTag();

		// For some reason, "palette" is in the NBT file. I don't know why it's there and how to get rid of it, so I'll just leave it here.
		dimensionCodec.remove("palette");

		CompoundTag tag = null;
		ListTag<CompoundTag> list = dimensionCodec.getCompoundTag("minecraft:dimension_type").getListTag("value").asCompoundTagList();
		for (CompoundTag each : list) {
			if (each.getString("name").equals("minecraft:the_end")) {
				tag = each.getCompoundTag("element");
				break;
			}
		}

		this.dimensionCodec = dimensionCodec;
		this.dimensionTag = tag != null ? tag : list.get(0);
	}

	public CompoundTag getDimensionCodec() {
		return this.dimensionCodec;
	}

	public CompoundTag getDimensionTag() {
		return this.dimensionTag;
	}

}
