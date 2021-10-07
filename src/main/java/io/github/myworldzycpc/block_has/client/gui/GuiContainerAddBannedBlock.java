package io.github.myworldzycpc.block_has.client.gui;

import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import io.github.myworldzycpc.block_has.inventory.ContainerAddMap;
import io.github.myworldzycpc.block_has.inventory.GuiElementLoader;
import io.github.myworldzycpc.block_has.network.BlockHasMessage;
import io.github.myworldzycpc.block_has.network.NetworkLoader;
import io.github.myworldzycpc.block_has.util.BlockHasMap;
import io.github.myworldzycpc.block_has.util.Reference;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiContainerAddBannedBlock extends GuiContainer {

    public ContainerAddMap inventorySlotsIn;

    private static final String TEXTURE_PATH = Reference.MOD_ID + ":" + "textures/gui/container/gui_add_map.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

    private static final String TEXTURE_PATH2 = Reference.MOD_ID + ":" + "textures/gui/container/gui_add_map_2.png";
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(TEXTURE_PATH2);

    private static final int INPUT_NAME = FuncAlgorithms.getNextId();
    private static final int INPUT_SPAWN_POINT_X = FuncAlgorithms.getNextId();
    private static final int INPUT_SPAWN_POINT_Y = FuncAlgorithms.getNextId();
    private static final int INPUT_SPAWN_POINT_Z = FuncAlgorithms.getNextId();
    private static final int BUTTON_ADD_MAP = FuncAlgorithms.getNextId();
    private static final int BUTTON_GET_TO_THERE = FuncAlgorithms.getNextId();
    private static final int BUTTON_REMOVE_MAP = FuncAlgorithms.getNextId();

    private static final int ELEMENTS_PADDING = 5;
    private static final int INPUT_HEIGHT = 18;
    private static final int LIST_WIDTH = 150;
    private static final int LIST_OPTION_HEIGHT = 10;

    public static boolean needUpdate = false;

    private List<GuiTextField> inputList = new ArrayList<GuiTextField>();

    private List<MapSelectButton> mapButtonList = new ArrayList<MapSelectButton>();

    private int leastX;

    private boolean hasChange = false;

    private GuiTextField inputName;
    private GuiTextField inputSpawnPointX;
    private GuiTextField inputSpawnPointY;
    private GuiTextField inputSpawnPointZ;

    private GuiButton buttonAddMap;
    private GuiButton buttonGetToThere;
    private GuiButton buttonRemoveMap;

    private int selectingMapButtonId = -1;
    private int selectingMapButtonIndex = -1;
    private int selectingMapIndex = -1;
    private int roll = 0;

    public GuiContainerAddBannedBlock(ContainerAddMap inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 400;
        this.ySize = 190;
        this.inventorySlotsIn = inventorySlotsIn;
    }

    /**
     * Draws the background layer of this container (behind the items).
     *
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, 250, this.ySize);

        this.mc.getTextureManager().bindTexture(TEXTURE2);
        this.drawTexturedModalRect(offsetX + 250, offsetY, 0, 0, this.xSize - 250, this.ySize);

    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        List<Integer> widthList = new ArrayList<Integer>();

        widthList.add(this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.map_name")));
        widthList.add(this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.map_spawn_point")));
        leastX = Collections.max(widthList);
        drawRect(6, 6 + fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING, LIST_WIDTH + 6, this.ySize - 6 - 20 - ELEMENTS_PADDING, 0x63000000);
        this.fontRenderer.drawString(I18n.format("block_has.container.settings.add_map"), (this.xSize - this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.add_map"))) / 2, 6, 0x404040);
        int x;
        int y;
        if (this.selectingMapButtonId == -1) {
            int rightWidth = this.xSize - 6 - LIST_WIDTH - ELEMENTS_PADDING - 6;
            int rightHeight = this.ySize - 6 - fontRenderer.FONT_HEIGHT - ELEMENTS_PADDING - 6;
            x = 6 + LIST_WIDTH + ELEMENTS_PADDING + (rightWidth - fontRenderer.getStringWidth(I18n.format("block_has.container.settings.select_map_first"))) / 2;
            y = 6 + fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING + (rightHeight - fontRenderer.FONT_HEIGHT) / 2;
            this.fontRenderer.drawString(I18n.format("block_has.container.settings.select_map_first"), x, y, 0x404040);
        } else {
            x = 6 + LIST_WIDTH + ELEMENTS_PADDING;
            y = 6;
            this.fontRenderer.drawString(I18n.format("block_has.container.settings.map_name"), x, (y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
            this.fontRenderer.drawString(I18n.format("block_has.container.settings.map_spawn_point"), x, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        }
    }

    @Override
    public void initGui() {
        this.drawGuiContainerForegroundLayer(0, 0);
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        this.inputList.clear();
        this.mapButtonList.clear();

        int y = offsetY + 6;
        int x = offsetX + 6 + LIST_WIDTH + ELEMENTS_PADDING + leastX + ELEMENTS_PADDING;
        int inputWidth = this.xSize - 6 - LIST_WIDTH - ELEMENTS_PADDING - leastX - ELEMENTS_PADDING - 6;
        inputList.add(inputName = new GuiTextField(INPUT_NAME, this.fontRenderer, x, y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));
        int oneThirdWidth = (inputWidth - ELEMENTS_PADDING * 2) / 3;
        inputList.add(inputSpawnPointX = new GuiTextField(INPUT_SPAWN_POINT_X, this.fontRenderer, x, y += INPUT_HEIGHT + ELEMENTS_PADDING, oneThirdWidth, INPUT_HEIGHT));
        inputList.add(inputSpawnPointY = new GuiTextField(INPUT_SPAWN_POINT_Y, this.fontRenderer, x += oneThirdWidth + ELEMENTS_PADDING, y, oneThirdWidth, INPUT_HEIGHT));
        inputList.add(inputSpawnPointZ = new GuiTextField(INPUT_SPAWN_POINT_Z, this.fontRenderer, x += oneThirdWidth + ELEMENTS_PADDING, y, oneThirdWidth, INPUT_HEIGHT));
        x = offsetX + 6 + LIST_WIDTH + ELEMENTS_PADDING;
        this.buttonList.add(buttonGetToThere = new GuiButton(BUTTON_GET_TO_THERE, x, y += 20 + ELEMENTS_PADDING, this.xSize - 6 - LIST_WIDTH - ELEMENTS_PADDING - 6, 20, ""));
        this.buttonList.add(buttonRemoveMap = new GuiButton(BUTTON_REMOVE_MAP, x, y += 20 + ELEMENTS_PADDING, this.xSize - 6 - LIST_WIDTH - ELEMENTS_PADDING - 6, 20, ""));

        int oneSecondWidth = (LIST_WIDTH - ELEMENTS_PADDING) / 2;
        this.buttonList.add(buttonAddMap = new GuiButton(BUTTON_ADD_MAP, offsetX + 6, offsetY + this.ySize - 6 - 20, LIST_WIDTH, 20, ""));

        y = offsetY + 6 + fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING;
        int maxY = offsetY + this.ySize - 6 - 20 - ELEMENTS_PADDING;

        for (; y + LIST_OPTION_HEIGHT < maxY; y += LIST_OPTION_HEIGHT) {
            MapSelectButton guiButton = new MapSelectButton(FuncAlgorithms.getNextId(), offsetX + 6, y, LIST_WIDTH, LIST_OPTION_HEIGHT, "");
            this.mapButtonList.add(guiButton);
            this.buttonList.add(guiButton);
        }

        this.updateInputsValue();

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        for (int i = 0; i < this.mapButtonList.size(); i++) {
            GuiButton guiButton = this.mapButtonList.get(i);
            if (button.id == guiButton.id) {
                this.selectingMapButtonId = guiButton.id;
                this.selectingMapButtonIndex = i;
                this.selectingMapIndex = i + roll;
                this.updateInputsValue();
            }
        }
        SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);
        if (button.id == BUTTON_ADD_MAP) {
            BlockPos pos = this.inventorySlotsIn.player.getPosition();
            BlockHasMap blockHasMap = new BlockHasMap(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), I18n.format("block_has.container.settings.unnamed"));
            BlockHasSettingsGlobal.addBlockHasMap(blockHasMap);
            this.updateSettingsData();
            this.updateInputsValue();
        } else if (button.id == BUTTON_REMOVE_MAP) {
            BlockHasSettingsGlobal.removeBlockHasMap(this.selectingMapIndex);
            clearSelect();
            this.updateSettingsData();
            this.updateInputsValue();
        } else if (button.id == BUTTON_GET_TO_THERE) {
            BlockHasMessage message = new BlockHasMessage();
            message.nbt = new NBTTagCompound();
            BlockHasSettingsGlobal.writeToNBT(message.nbt);

            message.nbt.setInteger("selectingMapIndex", this.selectingMapIndex);
            message.nbt.setString("operation", "teleport");

            NetworkLoader.instance.sendToServer(message);

        }
    }

    private void clearSelect() {
        this.selectingMapButtonId = -1;
        this.selectingMapButtonIndex = -1;
        this.selectingMapIndex = -1;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     *
     * @param mouseX
     * @param mouseY
     * @param mouseButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTextField textField : inputList) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (this.hasChange) {
            this.hasChange = false;
            this.updateSettingsData();
            this.updateInputsValue();
        }

    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     *
     * @param typedChar
     * @param keyCode
     */
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (GuiTextField textField : inputList) {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
        this.hasChange = true;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        updateSettingsData();
        BlockHasMessage message = new BlockHasMessage();
        message.nbt = new NBTTagCompound();
        message.nbt.setString("operation", "open_gui");
        message.nbt.setInteger("guiId", GuiElementLoader.GUI_SETTINGS);
        NetworkLoader.instance.sendToServer(message);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen() {
        super.updateScreen();
        for (GuiTextField textField : inputList) {
            textField.updateCursorCounter();
        }
        if (needUpdate) {
            needUpdate = false;
            this.updateInputsValue();
        }
        int mouseDWheel = Mouse.getDWheel();
        if (mouseDWheel != 0) {
            if (mouseDWheel > 0) {
                roll--;
            } else {
                roll++;
            }
            this.updateInputsValue();
        }
    }

    /**
     * Draws the screen and all the components in it.
     *
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (GuiTextField textField : inputList) {
            textField.drawTextBox();
        }
    }

    public void updateSettingsData() {
        SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);
        BlockPos pos = this.inventorySlotsIn.player.getPosition();
        if (this.selectingMapButtonId != -1) {
            BlockHasMap blockHasMap = new BlockHasMap(
                    new Vec3d(
                            FuncAlgorithms.getValueWithDefault(this.inputSpawnPointX.getText(), pos.getX(), -30000000.0d, 30000000.0d),
                            FuncAlgorithms.getValueWithDefault(this.inputSpawnPointY.getText(), pos.getY(), 0.0d, 256.0d),
                            FuncAlgorithms.getValueWithDefault(this.inputSpawnPointZ.getText(), pos.getZ(), -30000000.0d, 30000000.0d)
                    ),
                    this.inputName.getText()
            );
            BlockHasSettingsGlobal.setBlockHasMap(blockHasMap, this.selectingMapIndex);
        }
        BlockHasMessage message = new BlockHasMessage();
        message.nbt = new NBTTagCompound();
        BlockHasSettingsGlobal.writeToNBT(message.nbt);

        message.nbt.setString("player", inventorySlotsIn.player.getUniqueID().toString());
        message.nbt.setString("operation", "update_settings_data");

        NetworkLoader.instance.sendToServer(message);
    }

    public void updateInputsValue() {
        SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);

        this.buttonAddMap.displayString = I18n.format("block_has.container.settings.add_map");
        this.buttonGetToThere.displayString = I18n.format("block_has.container.settings.get_to_there");
        this.buttonRemoveMap.displayString = I18n.format("block_has.container.settings.remove_map");

        this.buttonRemoveMap.visible = !(this.selectingMapButtonId == -1);
        this.buttonGetToThere.visible = !(this.selectingMapButtonId == -1);
        this.inputName.setVisible(!(this.selectingMapButtonId == -1));
        this.inputSpawnPointX.setVisible(!(this.selectingMapButtonId == -1));
        this.inputSpawnPointY.setVisible(!(this.selectingMapButtonId == -1));
        this.inputSpawnPointZ.setVisible(!(this.selectingMapButtonId == -1));

        List<BlockHasMap> blockHasMaps = BlockHasSettingsGlobal.getBlockHasMaps();

        if (this.selectingMapIndex >= blockHasMaps.size()) {
            clearSelect();
        }

        if (blockHasMaps.size() < this.mapButtonList.size()) {
            roll = 0;
        } else {
            if (roll < 0) {
                roll = 0;
            }
            if (roll + this.mapButtonList.size() > blockHasMaps.size()) {
                roll = blockHasMaps.size() - this.mapButtonList.size();
            }
        }

        for (MapSelectButton mapSelectButton : this.mapButtonList) {
            mapSelectButton.selected = mapSelectButton.id == this.selectingMapButtonId - this.roll;
            mapSelectButton.visible = false;
        }

        for (int i = 0; i < blockHasMaps.size(); i++) {
            BlockHasMap blockHasMap = blockHasMaps.get(i);
            if (i == this.selectingMapIndex) {
                this.inputName.setText(blockHasMap.mapName);
                this.inputSpawnPointX.setText(String.valueOf(blockHasMap.spawnPoint.x));
                this.inputSpawnPointY.setText(String.valueOf(blockHasMap.spawnPoint.y));
                this.inputSpawnPointZ.setText(String.valueOf(blockHasMap.spawnPoint.z));
            }
            if (i - roll >= 0 && i - roll < this.mapButtonList.size()) {
                this.mapButtonList.get(i - roll).displayString = blockHasMap.mapName;
                this.mapButtonList.get(i - roll).visible = true;
            }
        }

    }

}
