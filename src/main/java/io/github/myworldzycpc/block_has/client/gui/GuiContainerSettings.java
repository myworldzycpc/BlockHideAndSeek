package io.github.myworldzycpc.block_has.client.gui;

import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import io.github.myworldzycpc.block_has.inventory.ContainerSettings;
import io.github.myworldzycpc.block_has.inventory.GuiElementLoader;
import io.github.myworldzycpc.block_has.network.BlockHasMessage;
import io.github.myworldzycpc.block_has.network.NetworkLoader;
import io.github.myworldzycpc.block_has.network.OperationType;
import io.github.myworldzycpc.block_has.util.BlockHasPlayingMode;
import io.github.myworldzycpc.block_has.util.Reference;
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

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiContainerSettings extends GuiContainer {

    public ContainerSettings inventorySlotsIn;

    private static final String TEXTURE_PATH = Reference.MOD_ID + ":" + "textures/gui/container/gui_add_map.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

    private static final String TEXTURE_PATH2 = Reference.MOD_ID + ":" + "textures/gui/container/gui_add_map_2.png";
    private static final ResourceLocation TEXTURE2 = new ResourceLocation(TEXTURE_PATH2);

    private static final int INPUT_TIME_FOR_HUNTER_TO_WAIT = FuncAlgorithms.getNextId();
    private static final int INPUT_NUMBER_OF_HUNTERS = FuncAlgorithms.getNextId();
    private static final int INPUT_TOOL_COOLING_DOWN_TIME = FuncAlgorithms.getNextId();
    private static final int INPUT_HALL_POSITION_X = FuncAlgorithms.getNextId();
    private static final int INPUT_HALL_POSITION_Y = FuncAlgorithms.getNextId();
    private static final int INPUT_HALL_POSITION_Z = FuncAlgorithms.getNextId();
    private static final int INPUT_HICA_SENSOR_SENSITIVITY = FuncAlgorithms.getNextId();
    private static final int BUTTON_DEFAULT_GAME_MODE = FuncAlgorithms.getNextId();
    private static final int BUTTON_PLAYING_GAME_MODE = FuncAlgorithms.getNextId();
    private static final int BUTTON_ADD_MAP = FuncAlgorithms.getNextId();
    private static final int BUTTON_PLAYING_MODE = FuncAlgorithms.getNextId();
    private static final int BUTTON_ADD_BANNED_BLOCK = FuncAlgorithms.getNextId();
    private static final int BUTTON_DEBUG_MODE = FuncAlgorithms.getNextId();
    private static final int BUTTON_SHOW_HUD = FuncAlgorithms.getNextId();
    private static final int BUTTON_ANTI_CHEATING = FuncAlgorithms.getNextId();
    // add button step: add id

    private static final int ELEMENTS_PADDING = 5;
    private static final int INPUT_HEIGHT = 18;

    public static boolean needUpdate = false;

    private GuiTextField inputTimeForHunterToWait;
    private GuiTextField inputNumberOfHunters;
    private GuiTextField inputToolCoolingDownTime;
    private GuiTextField inputHallPositionX;
    private GuiTextField inputHallPositionY;
    private GuiTextField inputHallPositionZ;
    private GuiTextField inputHicaSensorSensitivity;

    private GuiButton buttonDefaultGameMode;
    private GuiButton buttonPlayingGameMode;

    private GuiButton buttonAddMap;
    private GuiButton buttonPlayingMode;
    private GuiButton buttonAddBannedBlock;
    private GuiButton buttonDebugMode;
    private GuiButton buttonShowHUD;
    private GuiButton buttonAntiCheating;
    // add button step: add button

    private final List<GuiTextField> inputList = new ArrayList<>();

    private int leastX;

    private boolean hasChange = false;

    private GameType defaultGameMode;
    private GameType playingGameMode;
    private boolean debugMode;
    private BlockHasPlayingMode playingMode;
    private boolean showHUD;
    private boolean antiCheating;
    // add button step: add field

    public GuiContainerSettings(ContainerSettings inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 400;
        this.ySize = 190;
        this.inventorySlotsIn = inventorySlotsIn;
    }

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

        List<Integer> widthList = new ArrayList<>();

        widthList.add(this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.time_for_hunter_to_wait")));
        widthList.add(this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.number_of_hunters")));
        widthList.add(this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.tool_cooling_down_time")));
        widthList.add(this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings.hall_position")));
        leastX = Collections.max(widthList);
        int y = 0;
        this.fontRenderer.drawString(I18n.format("block_has.container.settings"), (this.xSize - this.fontRenderer.getStringWidth(I18n.format("block_has.container.settings"))) / 2, y += 6, 0x404040);
        this.fontRenderer.drawString(I18n.format("block_has.container.settings.time_for_hunter_to_wait"), 6, (y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(I18n.format("block_has.container.settings.number_of_hunters"), 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(I18n.format("block_has.container.settings.tool_cooling_down_time"), 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(I18n.format("block_has.container.settings.hall_position"), 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(I18n.format("block_has.container.settings.hica_sensor_sensitivity"), 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);

    }

    @Override
    public void initGui() {
        this.drawGuiContainerForegroundLayer(0, 0);
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        int columnWidth = (this.xSize - 12 - ELEMENTS_PADDING) / 2;
        int column2offsetX = offsetX + 6 + columnWidth + ELEMENTS_PADDING;

        this.inputList.clear();

        int y = offsetY + 6;
        int inputWidth = columnWidth - leastX - ELEMENTS_PADDING;

        inputList.add(inputTimeForHunterToWait = new GuiTextField(INPUT_TIME_FOR_HUNTER_TO_WAIT, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));
        inputList.add(inputNumberOfHunters = new GuiTextField(INPUT_NUMBER_OF_HUNTERS, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += INPUT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));
        inputList.add(inputToolCoolingDownTime = new GuiTextField(INPUT_TOOL_COOLING_DOWN_TIME, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += INPUT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));

        int x = offsetX + leastX + ELEMENTS_PADDING + 6;
        int oneThirdWidth = (inputWidth - ELEMENTS_PADDING * 2) / 3;
        int oneSecondWidth = (columnWidth - ELEMENTS_PADDING) / 2;
        inputList.add(inputHallPositionX = new GuiTextField(INPUT_HALL_POSITION_X, this.fontRenderer, x, y += INPUT_HEIGHT + ELEMENTS_PADDING, oneThirdWidth, INPUT_HEIGHT));
        inputList.add(inputHallPositionY = new GuiTextField(INPUT_HALL_POSITION_Y, this.fontRenderer, x += oneThirdWidth + ELEMENTS_PADDING, y, oneThirdWidth, INPUT_HEIGHT));
        inputList.add(inputHallPositionZ = new GuiTextField(INPUT_HALL_POSITION_Z, this.fontRenderer, x += oneThirdWidth + ELEMENTS_PADDING, y, oneThirdWidth, INPUT_HEIGHT));

        inputList.add(inputHicaSensorSensitivity = new GuiTextField(INPUT_HICA_SENSOR_SENSITIVITY, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += INPUT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));

        this.buttonList.add(buttonDefaultGameMode = new GuiButton(BUTTON_DEFAULT_GAME_MODE, offsetX + 6, y += 20 + ELEMENTS_PADDING, columnWidth, 20, ""));
        this.buttonList.add(buttonPlayingGameMode = new GuiButton(BUTTON_PLAYING_GAME_MODE, offsetX + 6, y += 20 + ELEMENTS_PADDING, columnWidth, 20, ""));

        // right column
        y = offsetY + 6 + fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING;
        this.buttonList.add(buttonAddMap = new GuiButton(BUTTON_ADD_MAP, column2offsetX, y, oneSecondWidth, 20, ""));
        this.buttonList.add(buttonPlayingMode = new GuiButton(BUTTON_PLAYING_MODE, column2offsetX + oneSecondWidth + ELEMENTS_PADDING, y, oneSecondWidth, 20, ""));
        this.buttonList.add(buttonAddBannedBlock = new GuiButton(BUTTON_ADD_BANNED_BLOCK, column2offsetX, y += 20 + ELEMENTS_PADDING, columnWidth, 20, ""));
        this.buttonList.add(buttonDebugMode = new GuiButton(BUTTON_DEBUG_MODE, column2offsetX, y += 20 + ELEMENTS_PADDING, oneSecondWidth, 20, ""));
        this.buttonList.add(buttonShowHUD = new GuiButton(BUTTON_SHOW_HUD, column2offsetX + oneSecondWidth + ELEMENTS_PADDING, y, oneSecondWidth, 20, ""));
        this.buttonList.add(buttonAntiCheating = new GuiButton(BUTTON_ANTI_CHEATING, column2offsetX, y += 20 + ELEMENTS_PADDING, oneSecondWidth, 20, ""));

        // add button step: init button (don't forget to change ID)

        this.updateInputsValue();
    }

    @Override
    protected void actionPerformed(@Nonnull GuiButton button) throws IOException {
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
            message.nbt.setInteger("operation", OperationType.OPEN_GUI.id);
            message.nbt.setInteger("guiId", GuiElementLoader.GUI_ADD_MAP);
            NetworkLoader.instance.sendToServer(message);
        } else if (button.id == BUTTON_ADD_BANNED_BLOCK) {
            BlockHasMessage message = new BlockHasMessage();
            message.nbt = new NBTTagCompound();
            message.nbt.setInteger("operation", OperationType.OPEN_GUI.id);
            message.nbt.setInteger("guiId", GuiElementLoader.GUI_ADD_BANNED_BLOCK);
            NetworkLoader.instance.sendToServer(message);
        } else if (button.id == BUTTON_DEBUG_MODE) {
            debugMode = !debugMode;
            this.drawSelectButton();
            updateSettingsData();
        } else if (button.id == BUTTON_PLAYING_MODE) {
            playingMode = BlockHasPlayingMode.fromId(playingMode.id + 1 % BlockHasPlayingMode.values().length);
            this.drawSelectButton();
            updateSettingsData();
        } else if (button.id == BUTTON_SHOW_HUD) {
            showHUD = !showHUD;
            this.drawSelectButton();
            updateSettingsData();
        } else if (button.id == BUTTON_ANTI_CHEATING) {
            antiCheating = !antiCheating;
            this.drawSelectButton();
            updateSettingsData();
        }
        // add button step: add actionPerformed
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
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);

        blockHasSettingsGlobal.addSettings(
                new Vec3d(getInputHallPositionX(), getInputHallPositionY(), getInputHallPositionZ()),
                getInputTimeForHunterToWait(),
                getInputNumberOfHunters(),
                getInputToolCoolingDownTime(),
                getButtonDefaultGameMode(),
                getButtonPlayingGameMode()
        );
        blockHasSettingsGlobal.setHicaSensorSensitivity(getInputHicaSensorSensitivity());
        blockHasSettingsGlobal.setPlayingMode(this.playingMode);
        blockHasSettingsGlobal.setDebugMode(this.debugMode);
        blockHasSettingsGlobal.setShowHUD(this.showHUD);
        blockHasSettingsGlobal.setAntiCheating(this.antiCheating);
        // add button step: add settings

        BlockHasMessage message = new BlockHasMessage();
        message.nbt = new NBTTagCompound();
        blockHasSettingsGlobal.writeToNBT(message.nbt);

        message.nbt.setString("player", inventorySlotsIn.player.getUniqueID().toString());
        message.nbt.setInteger("operation", OperationType.UPDATE_SETTINGS_DATA.id);

        NetworkLoader.instance.sendToServer(message);
    }

    public void updateInputsValue() {
        SettingsWorldSavedData blockHasSettingsGlobal = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world);

        this.defaultGameMode = blockHasSettingsGlobal.getDefaultGameMode();
        this.playingGameMode = blockHasSettingsGlobal.getPlayingGameMode();
        this.debugMode = blockHasSettingsGlobal.isDebugMode();
        this.playingMode = blockHasSettingsGlobal.getPlayingMode();
        this.showHUD = blockHasSettingsGlobal.isShowHUD();
        this.antiCheating = blockHasSettingsGlobal.isAntiCheating();
        this.drawSelectButton();
        // add button step: add update

        this.buttonAddMap.displayString = I18n.format("block_has.container.settings.add_map") + "...";
        this.buttonAddBannedBlock.displayString = I18n.format("block_has.container.settings.add_banned_block") + "...";

        inputTimeForHunterToWait.setText(String.valueOf(blockHasSettingsGlobal.getTimeForHunterToWait()));
        inputNumberOfHunters.setText(String.valueOf(blockHasSettingsGlobal.getNumberOfHunters()));
        inputToolCoolingDownTime.setText(String.valueOf(blockHasSettingsGlobal.getToolCoolingDownTime()));

        Vec3d hallPosition = blockHasSettingsGlobal.getHallPosition();
        inputHallPositionX.setText(String.valueOf(hallPosition.x));
        inputHallPositionY.setText(String.valueOf(hallPosition.y));
        inputHallPositionZ.setText(String.valueOf(hallPosition.z));

        inputHicaSensorSensitivity.setText(String.valueOf(blockHasSettingsGlobal.getHicaSensorSensitivity()));
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

    public double getInputHicaSensorSensitivity() {
        return FuncAlgorithms.getValueWithDefault(inputHicaSensorSensitivity.getText(), 1.0d, Double.MIN_VALUE, Double.MAX_VALUE);

    }

    public GameType getButtonDefaultGameMode() {
        return this.defaultGameMode;
    }

    public GameType getButtonPlayingGameMode() {
        return this.playingGameMode;
    }


    private void drawSelectButton() {
        buttonDefaultGameMode.displayString = I18n.format("block_has.generic.colon", I18n.format("block_has.container.settings.default_game_mode"), I18n.format("gameMode." + defaultGameMode.getName()));
        buttonPlayingGameMode.displayString = I18n.format("block_has.generic.colon", I18n.format("block_has.container.settings.playing_game_mode"), I18n.format("gameMode." + playingGameMode.getName()));
        buttonDebugMode.displayString = I18n.format("block_has.generic.colon", I18n.format("block_has.container.settings.show_debug_info"), debugMode ? I18n.format("block_has.generic.on") : I18n.format("block_has.generic.off"));
        buttonPlayingMode.displayString = I18n.format("block_has.generic.colon", I18n.format("block_has.container.settings.playing_mode"), playingMode.getDisplayName());
        buttonShowHUD.displayString = I18n.format("block_has.generic.colon", I18n.format("block_has.container.settings.show_hud"), showHUD ? I18n.format("block_has.generic.on") : I18n.format("block_has.generic.off"));
        buttonAntiCheating.displayString = I18n.format("block_has.generic.colon", I18n.format("block_has.container.settings.anti_cheating"), antiCheating ? I18n.format("block_has.generic.on") : I18n.format("block_has.generic.off"));

        // add button step: add draw (copy and modify 3 places)
    }

}

// add button step: add translation and go to SettingsWorldSavedData.
