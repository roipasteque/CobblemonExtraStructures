package net.pastek.cobblemonextrastructures.common.tile;

import net.minecraft.nbt.CompoundTag;

public class SpawnEntry {
    public int weight = 10;
    public String species = "porygon";
    public int minIv = 0;
    public int maxIv = 31;
    public int pokemonLevel = -1;
    public int shinyLuck = 4096;
    public String nature = "";
    public String heldItem = "";
    public String ability = "";
    public String move1 = "", move2 = "", move3 = "", move4 = "";
    public double spawnOffsetX = 0.5;
    public double spawnOffsetY = 1.0;
    public double spawnOffsetZ = 0.5;

    public SpawnEntry() {}

    public SpawnEntry(CompoundTag tag) {
        this.load(tag);
    }

    public void save(CompoundTag tag) {
        tag.putInt("Weight", weight);
        tag.putString("Species", species);
        tag.putInt("MinIV", minIv);
        tag.putInt("MaxIV", maxIv);
        tag.putInt("PokemonLevel", pokemonLevel);
        tag.putInt("ShinyLuck", shinyLuck);
        tag.putString("Nature", nature);
        tag.putString("HeldItem", heldItem);
        tag.putString("Ability", ability);
        tag.putString("Move1", move1);
        tag.putString("Move2", move2);
        tag.putString("Move3", move3);
        tag.putString("Move4", move4);
        tag.putDouble("OffsetX", spawnOffsetX);
        tag.putDouble("OffsetY", spawnOffsetY);
        tag.putDouble("OffsetZ", spawnOffsetZ);
    }

    public void load(CompoundTag tag) {
        if (tag.contains("Weight")) weight = tag.getInt("Weight");
        if (tag.contains("Species")) species = tag.getString("Species");
        if (tag.contains("MinIV")) minIv = tag.getInt("MinIV");
        if (tag.contains("MaxIV")) maxIv = tag.getInt("MaxIV");
        if (tag.contains("PokemonLevel")) pokemonLevel = tag.getInt("PokemonLevel");
        if (tag.contains("ShinyLuck")) shinyLuck = tag.getInt("ShinyLuck");
        if (tag.contains("Nature")) nature = tag.getString("Nature");
        if (tag.contains("HeldItem")) heldItem = tag.getString("HeldItem");
        if (tag.contains("Ability")) ability = tag.getString("Ability");
        if (tag.contains("Move1")) move1 = tag.getString("Move1");
        if (tag.contains("Move2")) move2 = tag.getString("Move2");
        if (tag.contains("Move3")) move3 = tag.getString("Move3");
        if (tag.contains("Move4")) move4 = tag.getString("Move4");
        if (tag.contains("OffsetX")) spawnOffsetX = tag.getDouble("OffsetX");
        if (tag.contains("OffsetY")) spawnOffsetY = tag.getDouble("OffsetY");
        if (tag.contains("OffsetZ")) spawnOffsetZ = tag.getDouble("OffsetZ");
    }
}