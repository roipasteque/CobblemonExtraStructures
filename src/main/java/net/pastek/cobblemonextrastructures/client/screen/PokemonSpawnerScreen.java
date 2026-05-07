package net.pastek.cobblemonextrastructures.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.pastek.cobblemonextrastructures.common.inventory.container.PokemonSpawnerMenu;
import net.pastek.cobblemonextrastructures.common.packet.UpdateSpawnerPayload;
import net.pastek.cobblemonextrastructures.common.tile.TilePokemonSpawner;

public class PokemonSpawnerScreen extends AbstractContainerScreen<PokemonSpawnerMenu> {
    private EditBox radiusField, speciesField, minIvField, maxIvField, levelField, shinyLuckField, natureField, itemField;
    private EditBox abilityField, m1Field, m2Field, m3Field, m4Field;
    private EditBox offsetXField, offsetYField, offsetZField;

    public PokemonSpawnerScreen(PokemonSpawnerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 220;
        this.imageHeight = 255;
    }

    @Override
    protected void init() {
        super.init();
        TilePokemonSpawner be = menu.getBlockEntity();

        int rx = this.leftPos + 10;
        int ry = this.topPos + 25;
        int widthFull = 200;
        int widthHalf = 95;

        speciesField = createField(rx, ry + 10, widthFull, be.getSpecies(), "Pokémon species (e.g., porygon)", 32);
        ry += 35;
        radiusField = createField(rx, ry + 10, widthHalf, String.valueOf(be.getTriggerRadius()), "Detection radius", 32);
        levelField = createField(rx + 105, ry + 10, widthHalf, String.valueOf(be.getPokemonLevel()), "Level (-1 for random)", 32);
        ry += 35;
        minIvField = createField(rx, ry + 10, widthHalf, String.valueOf(be.getMinIv()), "Min IVs (0-31)", 32);
        maxIvField = createField(rx + 105, ry + 10, widthHalf, String.valueOf(be.getMaxIv()), "Max IVs (0-31)", 32);
        ry += 35;
        shinyLuckField = createField(rx, ry + 10, widthHalf, String.valueOf(be.getShinyLuck()), "Shiny chance (1 in X)", 32);
        natureField = createField(rx + 105, ry + 10, widthHalf, be.getNature(), "Nature (e.g., Jolly)", 32);
        ry += 35;
        itemField = createField(rx, ry + 10, widthFull, be.getHeldItem(), "Held Item (mod:item)", 256);
        itemField.setMaxLength(256);

        this.addRenderableWidget(Button.builder(Component.literal("Save Settings"), (btn) -> this.onClose())
                .bounds(rx, ry + 40, widthFull, 20).build());

        int lx = this.leftPos - 170;
        int ly = this.topPos + 25;
        int moveWidth = 135;

        abilityField = createField(lx + 10, ly + 10, moveWidth, be.getAbility(), "Custom Ability Name", 32);
        ly += 35;
        m1Field = createField(lx + 10, ly + 10, moveWidth, be.getMove1(), "Move 1", 32);
        m2Field = createField(lx + 10, ly + 30, moveWidth, be.getMove2(), "Move 2", 32);
        m3Field = createField(lx + 10, ly + 50, moveWidth, be.getMove3(), "Move 3", 32);
        m4Field = createField(lx + 10, ly + 70, moveWidth, be.getMove4(), "Move 4", 32);

        int bx = this.leftPos - 170;
        int by = this.topPos + 195;
        int coordWidth = 40;

        offsetXField = createField(bx + 10, by, coordWidth, String.valueOf(be.getOffsetX()), "X Offset", 32);
        offsetYField = createField(bx + 60, by, coordWidth, String.valueOf(be.getOffsetY()), "Y Offset", 32);
        offsetZField = createField(bx + 110, by, coordWidth, String.valueOf(be.getOffsetZ()), "Z Offset", 32);

        this.addRenderableWidget(Button.builder(Component.literal("Reset cooldown"), (btn) -> {
                    this.save(true);
                    this.onClose();
                })
                .bounds(rx, ry + 65, widthFull, 20)
                .tooltip(Tooltip.create(Component.literal("Clears cooldown and forgets previous Pokémon")))
                .build());
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

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
        drawPanel(graphics, leftPos, topPos, imageWidth, imageHeight, "Base Stats");
        drawPanel(graphics, leftPos - 175, topPos, 165, 160, "Moves & Ability");
        drawPanel(graphics, leftPos - 175, topPos + 165, 165, 60, "Spawn Coordinates");
    }

    private void drawPanel(GuiGraphics graphics, int x, int y, int w, int h, String label) {
        graphics.fill(x, y, x + w, y + h, 0xFF101010);
        graphics.fill(x + 2, y + 2, x + w - 2, y + h - 2, 0xFF2B2B2B);
        graphics.fill(x + 2, y + 18, x + w - 2, y + 19, 0xFF444444);
        graphics.drawString(this.font, label, x + 8, y + 6, 0xAAAAAA);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        int color = 0xCCCCCC;

        int rx = this.leftPos + 10;
        int ry = this.topPos + 25;
        graphics.drawString(this.font, "Species", rx, ry, color);
        graphics.drawString(this.font, "Radius", rx, ry + 35, color);
        graphics.drawString(this.font, "Level", rx + 105, ry + 35, color);
        graphics.drawString(this.font, "Min IV", rx, ry + 70, color);
        graphics.drawString(this.font, "Max IV", rx + 105, ry + 70, color);
        graphics.drawString(this.font, "Shiny (1/X)", rx, ry + 105, color);
        graphics.drawString(this.font, "Nature", rx + 105, ry + 105, color);
        graphics.drawString(this.font, "Held Item", rx, ry + 140, color);

        int lx = this.leftPos - 170;
        int ly = this.topPos + 25;
        graphics.drawString(this.font, "Ability", lx + 10, ly, color);
        graphics.drawString(this.font, "Movepool", lx + 10, ly + 35, color);

        int bx = this.leftPos - 170;
        int by = this.topPos + 185;
        graphics.drawString(this.font, "X", bx + 25, by, color);
        graphics.drawString(this.font, "Y", bx + 75, by, color);
        graphics.drawString(this.font, "Z", bx + 125, by, color);
    }

    private void save(boolean isReset) {
        PacketDistributor.sendToServer(new UpdateSpawnerPayload(
                menu.getBlockEntity().getBlockPos(),
                parseSafeDouble(radiusField.getValue(), 5.0),
                speciesField.getValue(),
                parseSafeInt(minIvField.getValue(), 0),
                parseSafeInt(maxIvField.getValue(), 31),
                parseSafeInt(levelField.getValue(), -1),
                parseSafeInt(shinyLuckField.getValue(), 4096),
                natureField.getValue(),
                itemField.getValue(),
                abilityField.getValue(),
                m1Field.getValue(),
                m2Field.getValue(),
                m3Field.getValue(),
                m4Field.getValue(),
                parseSafeDouble(offsetXField.getValue(), 0.5),
                parseSafeDouble(offsetYField.getValue(), 0.0),
                parseSafeDouble(offsetZField.getValue(), 0.5),
                isReset
        ));
    }

    @Override
    public void onClose() {
        save(false);
        super.onClose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean anyFieldFocused = speciesField.isFocused() || radiusField.isFocused() ||
                minIvField.isFocused() || maxIvField.isFocused() ||
                levelField.isFocused() || shinyLuckField.isFocused() ||
                natureField.isFocused() || itemField.isFocused() ||
                abilityField.isFocused() || m1Field.isFocused() ||
                m2Field.isFocused() || m3Field.isFocused() || m4Field.isFocused() ||
                offsetXField.isFocused() || offsetYField.isFocused() || offsetZField.isFocused();

        if (anyFieldFocused && this.minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private int parseSafeInt(String s, int def) { try { return Integer.parseInt(s); } catch (Exception e) { return def; } }
    private double parseSafeDouble(String s, double def) { try { return Double.parseDouble(s); } catch (Exception e) { return def; } }
}