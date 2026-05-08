package net.pastek.cobblemonextrastructures.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.pastek.cobblemonextrastructures.common.inventory.container.RandomPokemonSpawnerMenu;
import net.pastek.cobblemonextrastructures.common.packet.UpdateRandomSpawnerPayload;
import net.pastek.cobblemonextrastructures.common.tile.SpawnEntry;
import net.pastek.cobblemonextrastructures.common.tile.TileRandomPokemonSpawner;

import java.util.ArrayList;
import java.util.List;

public class RandomPokemonSpawnerScreen extends AbstractContainerScreen<RandomPokemonSpawnerMenu> {

    private EditBox radiusField;
    private EditBox weightField, speciesField, minIvField, maxIvField, levelField, shinyLuckField, natureField, itemField;
    private EditBox abilityField, m1Field, m2Field, m3Field, m4Field;
    private EditBox offsetXField, offsetYField, offsetZField;

    private List<SpawnEntry> localEntries;
    private int currentIndex = 0;

    public RandomPokemonSpawnerScreen(RandomPokemonSpawnerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 220;
        this.imageHeight = 255;

        TileRandomPokemonSpawner be = menu.getBlockEntity();
        this.localEntries = new ArrayList<>();

        for (SpawnEntry e : be.getSpawnEntries()) {
            CompoundTag tag = new CompoundTag();
            e.save(tag);
            this.localEntries.add(new SpawnEntry(tag));
        }

        if (this.localEntries.isEmpty()) {
            this.localEntries.add(new SpawnEntry());
        }
    }

    @Override
    protected void init() {
        super.init();
        TileRandomPokemonSpawner be = menu.getBlockEntity();

        int rx = this.leftPos + 10;
        int ry = this.topPos + 25;
        int widthFull = 200;
        int widthHalf = 95;

        this.addRenderableWidget(Button.builder(Component.literal("<"), btn -> changePage(-1)).bounds(rx, ry, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(">"), btn -> changePage(1)).bounds(rx + 90, ry, 20, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("+ New"), btn -> addEntry()).bounds(rx + 115, ry, 40, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal("- Del"), btn -> deleteEntry()).bounds(rx + 160, ry, 40, 20).build());

        ry += 25;

        radiusField = createField(rx, ry + 10, 45, String.valueOf(be.getTriggerRadius()), "Global Detection radius", 32);
        speciesField = createField(rx + 50, ry + 10, 150, "", "Pokémon species", 32);
        ry += 35;
        levelField = createField(rx, ry + 10, widthHalf, "", "Level (-1 for random)", 32);
        shinyLuckField = createField(rx + 105, ry + 10, widthHalf, "", "Shiny chance (1 in X)", 32);
        ry += 35;
        minIvField = createField(rx, ry + 10, widthHalf, "", "Min IVs (0-31)", 32);
        maxIvField = createField(rx + 105, ry + 10, widthHalf, "", "Max IVs (0-31)", 32);
        ry += 35;
        natureField = createField(rx, ry + 10, widthHalf, "", "Nature (e.g., Jolly)", 32);
        itemField = createField(rx + 105, ry + 10, widthHalf, "", "Held Item (mod:item)", 256);
        itemField.setMaxLength(256);

        this.addRenderableWidget(Button.builder(Component.literal("Save Settings"), (btn) -> this.onClose())
                .bounds(rx, ry + 40, widthFull, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Reset cooldown"), (btn) -> {
                    this.saveData(true);
                    this.onClose();
                })
                .bounds(rx, ry + 65, widthFull, 20)
                .build());

        int lx = this.leftPos - 170;
        int ly = this.topPos + 25;

        abilityField = createField(lx + 10, ly + 10, 135, "", "Custom Ability Name", 32);
        ly += 35;
        m1Field = createField(lx + 10, ly + 10, 135, "", "Move 1", 32);
        m2Field = createField(lx + 10, ly + 30, 135, "", "Move 2", 32);
        m3Field = createField(lx + 10, ly + 50, 135, "", "Move 3", 32);
        m4Field = createField(lx + 10, ly + 70, 135, "", "Move 4", 32);

        int bx = this.leftPos - 170;
        int by = this.topPos + 195;
        offsetXField = createField(bx + 10, by, 40, "", "X Offset", 32);
        offsetYField = createField(bx + 60, by, 40, "", "Y Offset", 32);
        offsetZField = createField(bx + 110, by, 40, "", "Z Offset", 32);

        weightField = createField(bx + 110, this.topPos + 232, 40, "", "Spawn Weight (Higher = More Common)", 32);

        loadPageToUI();
    }

    private void changePage(int direction) {
        saveUIToPage();
        currentIndex += direction;
        if (currentIndex < 0) currentIndex = localEntries.size() - 1;
        if (currentIndex >= localEntries.size()) currentIndex = 0;
        loadPageToUI();
    }

    private void addEntry() {
        saveUIToPage();
        localEntries.add(new SpawnEntry());
        currentIndex = localEntries.size() - 1;
        loadPageToUI();
    }

    private void deleteEntry() {
        localEntries.remove(currentIndex);
        if (localEntries.isEmpty()) localEntries.add(new SpawnEntry());
        if (currentIndex >= localEntries.size()) currentIndex = localEntries.size() - 1;
        loadPageToUI();
    }

    private void saveUIToPage() {
        if (currentIndex >= 0 && currentIndex < localEntries.size()) {
            SpawnEntry e = localEntries.get(currentIndex);
            e.weight = parseSafeInt(weightField.getValue(), 10);
            e.species = speciesField.getValue();
            e.minIv = parseSafeInt(minIvField.getValue(), 0);
            e.maxIv = parseSafeInt(maxIvField.getValue(), 31);
            e.pokemonLevel = parseSafeInt(levelField.getValue(), -1);
            e.shinyLuck = parseSafeInt(shinyLuckField.getValue(), 4096);
            e.nature = natureField.getValue();
            e.heldItem = itemField.getValue();
            e.ability = abilityField.getValue();
            e.move1 = m1Field.getValue(); e.move2 = m2Field.getValue();
            e.move3 = m3Field.getValue(); e.move4 = m4Field.getValue();
            e.spawnOffsetX = parseSafeDouble(offsetXField.getValue(), 0.5);
            e.spawnOffsetY = parseSafeDouble(offsetYField.getValue(), 0.0);
            e.spawnOffsetZ = parseSafeDouble(offsetZField.getValue(), 0.5);
        }
    }

    private void loadPageToUI() {
        if (currentIndex >= 0 && currentIndex < localEntries.size()) {
            SpawnEntry e = localEntries.get(currentIndex);
            weightField.setValue(String.valueOf(e.weight));
            speciesField.setValue(e.species);
            minIvField.setValue(String.valueOf(e.minIv));
            maxIvField.setValue(String.valueOf(e.maxIv));
            levelField.setValue(String.valueOf(e.pokemonLevel));
            shinyLuckField.setValue(String.valueOf(e.shinyLuck));
            natureField.setValue(e.nature);
            itemField.setValue(e.heldItem);
            abilityField.setValue(e.ability);
            m1Field.setValue(e.move1); m2Field.setValue(e.move2);
            m3Field.setValue(e.move3); m4Field.setValue(e.move4);
            offsetXField.setValue(String.valueOf(e.spawnOffsetX));
            offsetYField.setValue(String.valueOf(e.spawnOffsetY));
            offsetZField.setValue(String.valueOf(e.spawnOffsetZ));
        }
    }

    private void saveData(boolean isReset) {
        saveUIToPage();
        PacketDistributor.sendToServer(new UpdateRandomSpawnerPayload(
                menu.getBlockEntity().getBlockPos(),
                parseSafeDouble(radiusField.getValue(), 5.0),
                localEntries,
                isReset
        ));
    }

    @Override
    public void onClose() {
        saveData(false);
        super.onClose();
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) { }

    private EditBox createField(int x, int y, int width, String defaultValue, String tooltipText, int maxLen) {
        EditBox field = new EditBox(this.font, x, y, width, 16, Component.literal(tooltipText));
        field.setMaxLength(maxLen);
        field.setValue(defaultValue);
        field.setTooltip(Tooltip.create(Component.literal(tooltipText)));
        this.addRenderableWidget(field);
        return field;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        drawPanel(graphics, leftPos, topPos, imageWidth, imageHeight, "List Configuration");
        drawPanel(graphics, leftPos - 175, topPos, 165, 160, "Moves & Ability");
        drawPanel(graphics, leftPos - 175, topPos + 165, 165, 60, "Spawn Coordinates");
        drawPanel(graphics, leftPos - 175, topPos + 228, 165, 27, "");
    }

    private void drawPanel(GuiGraphics graphics, int x, int y, int w, int h, String label) {
        graphics.fill(x, y, x + w, y + h, 0xFF101010);
        graphics.fill(x + 2, y + 2, x + w - 2, y + h - 2, 0xFF2B2B2B);
        if(!label.isEmpty()) {
            graphics.fill(x + 2, y + 18, x + w - 2, y + 19, 0xFF444444);
            graphics.drawString(this.font, label, x + 8, y + 6, 0xAAAAAA);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        int color = 0xCCCCCC;
        int rx = this.leftPos + 10;
        int ry = this.topPos + 25;

        String pageText = "Entry " + (currentIndex + 1) + " / " + localEntries.size();
        graphics.drawString(this.font, pageText, rx + 25, ry + 6, 0xFFD700);

        ry += 25;
        graphics.drawString(this.font, "Radius", rx, ry, color);
        graphics.drawString(this.font, "Species", rx + 50, ry, color);
        graphics.drawString(this.font, "Level", rx, ry + 35, color);
        graphics.drawString(this.font, "Shiny (1/X)", rx + 105, ry + 35, color);
        graphics.drawString(this.font, "Min IV", rx, ry + 70, color);
        graphics.drawString(this.font, "Max IV", rx + 105, ry + 70, color);
        graphics.drawString(this.font, "Nature", rx, ry + 105, color);
        graphics.drawString(this.font, "Held Item", rx + 105, ry + 105, color);

        int lx = this.leftPos - 170;
        int ly = this.topPos + 25;
        graphics.drawString(this.font, "Ability", lx + 10, ly, color);
        graphics.drawString(this.font, "Movepool", lx + 10, ly + 35, color);

        int bx = this.leftPos - 170;
        int by = this.topPos + 185;
        graphics.drawString(this.font, "X", bx + 25, by, color);
        graphics.drawString(this.font, "Y", bx + 75, by, color);
        graphics.drawString(this.font, "Z", bx + 125, by, color);

        graphics.drawString(this.font, "Spawn Weight:", bx + 10, this.topPos + 236, color);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.getFocused() != null && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private int parseSafeInt(String s, int def) { try { return Integer.parseInt(s); } catch (Exception e) { return def; } }
    private double parseSafeDouble(String s, double def) { try { return Double.parseDouble(s); } catch (Exception e) { return def; } }
}