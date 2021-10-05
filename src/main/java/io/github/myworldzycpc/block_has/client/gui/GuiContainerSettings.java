package io.github.myworldzycpc.block_has.client.gui;

import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import io.github.myworldzycpc.block_has.inventory.ContainerSettings;
import io.github.myworldzycpc.block_has.inventory.GuiElementLoader;
import io.github.myworldzycpc.block_has.network.BlockHasMessage;
import io.github.myworldzycpc.block_has.network.NetworkLoader;
import io.github.myworldzycpc.block_has.util.Reference;
import io.github.myworldzycpc.block_has.util.Translation;
import io.github.myworldzycpc.block_has.worldstorage.SettingsWorldSavedData;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiContainerSettings extends GuiContainer {

    public ContainerSettings inventorySlotsIn;

    private static final String TEXTURE_PATH = Reference.MOD_ID + ":" + "textures/gui/container/gui_settings.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

    private static final int INPUT_TIME_FOR_HUNTER_TO_WAIT = FuncAlgorithms.getNextId();
    private static final int INPUT_NUMBER_OF_HUNTERS = FuncAlgorithms.getNextId();
    private static final int INPUT_TOOL_COOLING_DOWN_TIME = FuncAlgorithms.getNextId();
    private static final int INPUT_HALL_POSITION_X = FuncAlgorithms.getNextId();
    private static final int INPUT_HALL_POSITION_Y = FuncAlgorithms.getNextId();
    private static final int INPUT_HALL_POSITION_Z = FuncAlgorithms.getNextId();
    private static final int BUTTON_DEFAULT_GAME_MODE = FuncAlgorithms.getNextId();
    private static final int BUTTON_PLAYING_GAME_MODE = FuncAlgorithms.getNextId();
    private static final int BUTTON_ADD_MAP = FuncAlgorithms.getNextId();

    private static final int ELEMENTS_PADDING = 5;
    private static final int INPUT_HEIGHT = 18;

    public static boolean needUpdate = false;

    private GuiTextField inputTimeForHunterToWait;
    private GuiTextField inputNumberOfHunters;
    private GuiTextField inputToolCoolingDownTime;
    private GuiTextField inputHallPositionX;
    private GuiTextField inputHallPositionY;
    private GuiTextField inputHallPositionZ;

    private GuiButton buttonDefaultGameMode;
    private GuiButton buttonPlayingGameMode;

    private GuiButton buttonAddMap;

    private List<GuiTextField> inputList = new ArrayList<GuiTextField>();

    private int leastX;

    private boolean hasChange = false;

    private GameType defaultGameMode;
    private GameType playingGameMode;

    public GuiContainerSettings(ContainerSettings inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 250;
        this.ySize = 190;
        this.inventorySlotsIn = inventorySlotsIn;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(TEXTURE);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        this.drawTexturedModalRect(offsetX, offsetY, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

        List<Integer> widthList = new ArrayList<Integer>();

        widthList.add(this.fontRenderer.getStringWidth(Translation.titleTimeForHunterToWait));
        widthList.add(this.fontRenderer.getStringWidth(Translation.titleNumberOfHunters));
        widthList.add(this.fontRenderer.getStringWidth(Translation.titleToolCoolingDownTime));
        widthList.add(this.fontRenderer.getStringWidth(Translation.titleHallPosition));
        leastX = Collections.max(widthList);
        int y = 0;
        this.fontRenderer.drawString(Translation.titleGui, (this.xSize - this.fontRenderer.getStringWidth(Translation.titleGui)) / 2, y += 6, 0x404040);
        this.fontRenderer.drawString(Translation.titleTimeForHunterToWait, 6, (y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(Translation.titleNumberOfHunters, 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(Translation.titleToolCoolingDownTime, 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(Translation.titleHallPosition, 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);

    }

    @Override
    public void initGui() {
        Translation.update();
        this.drawGuiContainerForegroundLayer(0, 0);
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        this.inputList.clear();

        int y = offsetY + 6;
        int inputWidth = this.xSize - 12 - leastX - ELEMENTS_PADDING;

        inputList.add(inputTimeForHunterToWait = new GuiTextField(INPUT_TIME_FOR_HUNTER_TO_WAIT, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));
        inputList.add(inputNumberOfHunters = new GuiTextField(INPUT_NUMBER_OF_HUNTERS, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += INPUT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));
        inputList.add(inputToolCoolingDownTime = new GuiTextField(INPUT_TOOL_COOLING_DOWN_TIME, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += INPUT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));

        int x = offsetX + leastX + ELEMENTS_PADDING + 6;
        int oneThirdWidth = (inputWidth - ELEMENTS_PADDING * 2) / 3;
        inputList.add(inputHallPositionX = new GuiTextField(INPUT_HALL_POSITION_X, this.fontRenderer, x, y += INPUT_HEIGHT + ELEMENTS_PADDING, oneThirdWidth, INPUT_HEIGHT));
        inputList.add(inputHallPositionY = new GuiTextField(INPUT_HALL_POSITION_Y, this.fontRenderer, x += oneThirdWidth + ELEMENTS_PADDING, y, oneThirdWidth, INPUT_HEIGHT));
        inputList.add(inputHallPositionZ = new GuiTextField(INPUT_HALL_POSITION_Z, this.fontRenderer, x += oneThirdWidth + ELEMENTS_PADDING, y, oneThirdWidth, INPUT_HEIGHT));

        this.buttonList.add(buttonDefaultGameMode = new GuiButton(BUTTON_DEFAULT_GAME_MODE, offsetX + 6, y += 20 + ELEMENTS_PADDING, this.xSize - 12, 20, ""));
        this.buttonList.add(buttonPlayingGameMode = new GuiButton(BUTTON_PLAYING_GAME_MODE, offsetX + 6, y += 20 + ELEMENTS_PADDING, this.xSize - 12, 20, ""));
        this.buttonList.add(buttonAddMap = new GuiButton(BUTTON_ADD_MAP, offsetX + 6, y += 20 + ELEMENTS_PADDING, this.xSize - 12, 20, ""));

        this.updateInputsValue();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == BUTTON_DEFAULT_GAME_MODE) {
            defaultGameMode = GameType.getByID(defaultGameMode.getID() + 1 % 4);
            this.drawSelectButton();
            updateSettingsData();
        } else if (button.id == BUTTON_PLAYING_GAME_MODE) {
            playingGameMode = GameType.getByID(playingGameMode.getID() + 1 % 4);
            this.drawSelectButton();
            updateSettingsData();
        } else if (button.id == BUTTON_ADD_MAP) {
            BlockHasMessage message = new BlockHasMessage();
            message.nbt = new NBTTagCompound();
            message.nbt.setString("operation", "open_gui");
            message.nbt.setInteger("guiId", GuiElementLoader.GUI_ADD_MAP);
            NetworkLoader.instance.sendToServer(message);
        }
    }


    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
        for (GuiTextField textField : inputList) {
            textField.updateCursorCounter();
        }
        if (needUpdate) {
            needUpdate = false;
            this.updateInputsValue();
        }
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        for (GuiTextField textField : inputList) {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
        this.hasChange = true;
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
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
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (GuiTextField textField : inputList) {
            textField.drawTextBox();
        }
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
        updateSettingsData();
    }

    public void updateSettingsData() {
        SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world).addSettings(
                new Vec3d(getInputHallPositionX(), getInputHallPositionY(), getInputHallPositionZ()),
                getInputTimeForHunterToWait(),
                getInputNumberOfHunters(),
                getInputToolCoolingDownTime(),
                getButtonDefaultGameMode(),
                getButtonPlayingGameMode()
        );
        BlockHasMessage message = new BlockHasMessage();
        message.nbt = new NBTTagCompound();
        SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world).writeToNBT(message.nbt);

        message.nbt.setString("player", inventorySlotsIn.player.getUniqueID().toString());
        message.nbt.setString("operation", "update_settings_data");

        NetworkLoader.instance.sendToServer(message);
    }

    public void updateInputsValue() {
        SettingsWorldSavedData BlockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);

        this.defaultGameMode = BlockHasSettingsGlobal.getDefaultGameMode();
        this.playingGameMode = BlockHasSettingsGlobal.getPlayingGameMode();
        this.drawSelectButton();

        this.buttonAddMap.displayString = Translation.addMap + "...";

        inputTimeForHunterToWait.setText(String.valueOf(BlockHasSettingsGlobal.getTimeForHunterToWait()));
        inputNumberOfHunters.setText(String.valueOf(BlockHasSettingsGlobal.getNumberOfHunters()));
        inputToolCoolingDownTime.setText(String.valueOf(BlockHasSettingsGlobal.getToolCoolingDownTime()));

        Vec3d hallPosition = BlockHasSettingsGlobal.getHallPosition();
        inputHallPositionX.setText(String.valueOf(hallPosition.x));
        inputHallPositionY.setText(String.valueOf(hallPosition.y));
        inputHallPositionZ.setText(String.valueOf(hallPosition.z));
    }

    public int getInputTimeForHunterToWait() {
        return FuncAlgorithms.getValueWithDefault(inputTimeForHunterToWait.getText(), 30, 0, Integer.MAX_VALUE);
    }

    public int getInputNumberOfHunters() {
        return FuncAlgorithms.getValueWithDefault(inputNumberOfHunters.getText(), 1, 1, Integer.MAX_VALUE);
    }

    public int getInputToolCoolingDownTime() {
        return FuncAlgorithms.getValueWithDefault(inputToolCoolingDownTime.getText(), 10, 0, Integer.MAX_VALUE);
    }

    public double getInputHallPositionX() {
        return FuncAlgorithms.getValueWithDefault(inputHallPositionX.getText(), inventorySlotsIn.player.getPosition().getX(), -30000000.0d, 30000000.0d);

    }

    public double getInputHallPositionY() {
        return FuncAlgorithms.getValueWithDefault(inputHallPositionY.getText(), inventorySlotsIn.player.getPosition().getY(), 0.0d, 256.0d);
    }

    public double getInputHallPositionZ() {
        return FuncAlgorithms.getValueWithDefault(inputHallPositionZ.getText(), inventorySlotsIn.player.getPosition().getZ(), -30000000.0d, 30000000.0d);
    }

    public GameType getButtonDefaultGameMode() {
        return this.defaultGameMode;
    }

    public GameType getButtonPlayingGameMode() {
        return this.playingGameMode;
    }


    private void drawSelectButton() {
        buttonDefaultGameMode.displayString = String.format("%s: %s", Translation.defaultGameMode, I18n.format("gameMode." + defaultGameMode.getName()));
        buttonPlayingGameMode.displayString = String.format("%s: %s", Translation.playingGameMode, I18n.format("gameMode." + playingGameMode.getName()));
    }

}
