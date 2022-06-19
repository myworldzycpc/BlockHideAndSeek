package io.github.myworldzycpc.block_has.client.gui;

import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import io.github.myworldzycpc.block_has.func.FuncOperation;
import io.github.myworldzycpc.block_has.inventory.ContainerAddBannedBlock;
import io.github.myworldzycpc.block_has.inventory.GuiElementLoader;
import io.github.myworldzycpc.block_has.network.BlockHasMessage;
import io.github.myworldzycpc.block_has.network.NetworkLoader;
import io.github.myworldzycpc.block_has.network.OperationType;
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
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GuiContainerAddBannedBlock extends GuiContainer {

    public ContainerAddBannedBlock inventorySlotsIn;

    private static final String TEXTURE_PATH = Reference.MOD_ID + ":" + "textures/gui/container/gui_add_map.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

    private static final String TEXTURE_PATH2 = Reference.MOD_ID + ":" + "textures/gui/container/gui_add_map_2.png";
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(TEXTURE_PATH2);

    private static final int INPUT_NAME = FuncAlgorithms.getNextId();
    private static final int BUTTON_ADD_BLOCK = FuncAlgorithms.getNextId();
    private static final int BUTTON_REMOVE_BLOCK = FuncAlgorithms.getNextId();

    private static final int ELEMENTS_PADDING = 5;
    private static final int INPUT_HEIGHT = 18;
    private static final int LIST_WIDTH = 150;
    private static final int LIST_OPTION_HEIGHT = 10;

    public static boolean needUpdate = false;

    private List<GuiTextField> inputList = new ArrayList<GuiTextField>();

    private List<MapSelectButton> blockButtonList = new ArrayList<MapSelectButton>();

    private int leastX;

    private boolean hasChange = false;

    private GuiTextField inputName;

    private GuiButton buttonAddBlock;
    private GuiButton buttonRemoveBlock;

    private int selectingBlockButtonId = -1;
    private int selectingBlockIndex = -1;
    private int roll = 0;

    public GuiContainerAddBannedBlock(ContainerAddBannedBlock inventorySlotsIn) {
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

        widthList.add(this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.block_id")));
        leastX = Collections.max(widthList);
        drawRect(6, 6 + fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING, LIST_WIDTH + 6, this.ySize - 6 - 20 - ELEMENTS_PADDING, 0x63000000);
        this.fontRenderer.drawString(I18n.format("block_has.container.settings.add_banned_block"), (this.xSize - this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.add_banned_block"))) / 2, 6, 0x404040);
        int x;
        int y;
        if (this.selectingBlockButtonId == -1) {
            int rightWidth = this.xSize - 6 - LIST_WIDTH - ELEMENTS_PADDING - 6;
            int rightHeight = this.ySize - 6 - fontRenderer.FONT_HEIGHT - ELEMENTS_PADDING - 6;
            x = 6 + LIST_WIDTH + ELEMENTS_PADDING + (rightWidth - fontRenderer.getStringWidth(I18n.format("block_has.container.settings.select_block_first"))) / 2;
            y = 6 + fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING + (rightHeight - fontRenderer.FONT_HEIGHT) / 2;
            this.fontRenderer.drawString(I18n.format("block_has.container.settings.select_block_first"), x, y, 0x404040);
        } else {
            x = 6 + LIST_WIDTH + ELEMENTS_PADDING;
            y = 6;
            this.fontRenderer.drawString(I18n.format("block_has.container.settings.block_id"), x, (y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        }
    }

    @Override
    public void initGui() {
        this.drawGuiContainerForegroundLayer(0, 0);
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        this.inputList.clear();
        this.blockButtonList.clear();

        int y = offsetY + 6;
        int x = offsetX + 6 + LIST_WIDTH + ELEMENTS_PADDING + leastX + ELEMENTS_PADDING;
        int inputWidth = this.xSize - 6 - LIST_WIDTH - ELEMENTS_PADDING - leastX - ELEMENTS_PADDING - 6;
        inputList.add(inputName = new GuiTextField(INPUT_NAME, this.fontRenderer, x, y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));
        inputName.setMaxStringLength(200);
        x = offsetX + 6 + LIST_WIDTH + ELEMENTS_PADDING;
        this.buttonList.add(buttonRemoveBlock = new GuiButton(BUTTON_REMOVE_BLOCK, x, y += 20 + ELEMENTS_PADDING, this.xSize - 6 - LIST_WIDTH - ELEMENTS_PADDING - 6, 20, ""));

        this.buttonList.add(buttonAddBlock = new GuiButton(BUTTON_ADD_BLOCK, offsetX + 6, offsetY + this.ySize - 6 - 20, LIST_WIDTH, 20, ""));

        y = offsetY + 6 + fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING;
        int maxY = offsetY + this.ySize - 6 - 20 - ELEMENTS_PADDING;

        for (; y + LIST_OPTION_HEIGHT < maxY; y += LIST_OPTION_HEIGHT) {
            MapSelectButton guiButton = new MapSelectButton(FuncAlgorithms.getNextId(), offsetX + 6, y, LIST_WIDTH, LIST_OPTION_HEIGHT, "");
            this.blockButtonList.add(guiButton);
            this.buttonList.add(guiButton);
        }

        this.updateInputsValue();

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        for (int i = 0; i < this.blockButtonList.size(); i++) {
            GuiButton guiButton = this.blockButtonList.get(i);
            if (button.id == guiButton.id) {
                this.selectingBlockButtonId = guiButton.id;
                this.selectingBlockIndex = i + roll;
                this.updateInputsValue();
            }
        }
        SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);
        if (button.id == BUTTON_ADD_BLOCK) {
            BlockPos pos = this.inventorySlotsIn.player.getPosition();
            String newBannedBlock = "minecraft:air";
            BlockHasSettingsGlobal.addBannedBlock(newBannedBlock);
            this.updateSettingsData();
            this.updateInputsValue();
        } else if (button.id == BUTTON_REMOVE_BLOCK) {
            BlockHasSettingsGlobal.removeBannedBlock(this.selectingBlockIndex);
            clearSelect();
            this.updateSettingsData();
            this.updateInputsValue();
        }
    }

    private void clearSelect() {
        this.selectingBlockButtonId = -1;
        this.selectingBlockIndex = -1;
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
//        super.keyTyped(typedChar, keyCode);
        if (keyCode == 1) {
            this.mc.player.closeScreen();
        }
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
        message.nbt.setInteger("operation", OperationType.OPEN_GUI.id);
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
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);
        BlockPos pos = this.inventorySlotsIn.player.getPosition();
        if (this.selectingBlockButtonId != -1) {
            String theBlockID = this.inputName.getText();
            if (!theBlockID.contains(":")) {
                theBlockID = "minecraft:" + theBlockID;
            }
            blockHasSettingsGlobal.setBannedBlock(this.selectingBlockIndex, theBlockID);
        }
        BlockHasMessage message = new BlockHasMessage();
        message.nbt = new NBTTagCompound();
        blockHasSettingsGlobal.writeToNBT(message.nbt);

        message.nbt.setString("player", inventorySlotsIn.player.getUniqueID().toString());
        message.nbt.setInteger("operation", OperationType.UPDATE_SETTINGS_DATA.id);

        NetworkLoader.instance.sendToServer(message);
    }

    public void updateInputsValue() {
        SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);

        this.buttonAddBlock.displayString = I18n.format("block_has.container.settings.add_banned_block");
        this.buttonRemoveBlock.displayString = I18n.format("block_has.container.settings.remove_block");

        this.buttonRemoveBlock.visible = !(this.selectingBlockButtonId == -1);
        this.inputName.setVisible(!(this.selectingBlockButtonId == -1));

        List<String> bannedBlocks = BlockHasSettingsGlobal.getBannedBlocks();

        if (this.selectingBlockIndex >= bannedBlocks.size()) {
            clearSelect();
        }

        if (bannedBlocks.size() < this.blockButtonList.size()) {
            roll = 0;
        } else {
            if (roll < 0) {
                roll = 0;
            }
            if (roll + this.blockButtonList.size() > bannedBlocks.size()) {
                roll = bannedBlocks.size() - this.blockButtonList.size();
            }
        }

        for (MapSelectButton mapSelectButton : this.blockButtonList) {
            mapSelectButton.selected = mapSelectButton.id == this.selectingBlockButtonId - this.roll;
            mapSelectButton.visible = false;
        }

        for (int i = 0; i < bannedBlocks.size(); i++) {
            String bannedBlock = bannedBlocks.get(i);
            if (i == this.selectingBlockIndex) {
                this.inputName.setText(bannedBlock);
            }
            if (i - roll >= 0 && i - roll < this.blockButtonList.size()) {
                String blockName = FuncOperation.getBlockName(bannedBlock);
                if (!blockName.equals("")) {
                    this.blockButtonList.get(i - roll).displayString = I18n.format("block_has.generic.brackets", bannedBlock, blockName);
                } else {
                    this.blockButtonList.get(i - roll).displayString = bannedBlock;
                }
                this.blockButtonList.get(i - roll).visible = true;
            }
        }

    }

}
