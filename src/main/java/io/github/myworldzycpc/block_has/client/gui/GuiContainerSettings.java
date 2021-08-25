package io.github.myworldzycpc.block_has.client.gui;

import io.github.myworldzycpc.block_has.func.FuncAlgorithms;
import io.github.myworldzycpc.block_has.inventory.ContainerSettings;
import io.github.myworldzycpc.block_has.network.MessageSettings;
import io.github.myworldzycpc.block_has.network.NetworkLoader;
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

    private static final int ELEMENTS_PADDING = 5;
    private static final int INPUT_HEIGHT = 18;

    private GuiTextField inputTimeForHunterToWait;
    private GuiTextField inputNumberOfHunters;
    private GuiTextField inputToolCoolingDownTime;
    private GuiTextField inputHallPositionX;
    private GuiTextField inputHallPositionY;
    private GuiTextField inputHallPositionZ;

    private List<GuiTextField> inputList = new ArrayList<GuiTextField>();

    private static int leastX;

    public GuiContainerSettings(ContainerSettings inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 250;
        this.ySize = 150;
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
        String titleGui = I18n.format("block_has.container.settings");
        String titleTimeForHunterToWait = I18n.format("block_has.container.settings.time_for_hunter_to_wait");
        String titleNumberOfHunters = I18n.format("block_has.container.settings.number_of_hunters");
        String titleToolCoolingDownTime = I18n.format("block_has.container.settings.tool_cooling_down_time");
        String titleHallPosition = I18n.format("block_has.container.settings.hall_position");
        widthList.add(this.fontRenderer.getStringWidth(titleTimeForHunterToWait));
        widthList.add(this.fontRenderer.getStringWidth(titleNumberOfHunters));
        widthList.add(this.fontRenderer.getStringWidth(titleToolCoolingDownTime));
        widthList.add(this.fontRenderer.getStringWidth(titleHallPosition));
        int y = 0;
        this.fontRenderer.drawString(titleGui, (this.xSize - this.fontRenderer.getStringWidth(titleGui)) / 2, y += 6, 0x404040);
        this.fontRenderer.drawString(titleTimeForHunterToWait, 6, (y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(titleNumberOfHunters, 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(titleToolCoolingDownTime, 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        this.fontRenderer.drawString(titleHallPosition, 6, (y += INPUT_HEIGHT + ELEMENTS_PADDING) + (INPUT_HEIGHT - fontRenderer.FONT_HEIGHT) / 2, 0x404040);
        leastX = Collections.max(widthList);

    }

    @Override
    public void initGui() {

        super.initGui();
        Keyboard.enableRepeatEvents(true);
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        int y = offsetY + 6;
        int inputWidth = this.xSize - 12 - leastX - ELEMENTS_PADDING;

        inputList.add(inputTimeForHunterToWait = new GuiTextField(INPUT_TIME_FOR_HUNTER_TO_WAIT, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += fontRenderer.FONT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));
        inputTimeForHunterToWait.setText(String.valueOf(SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world).getTimeForHunterToWait()));
        inputList.add(inputNumberOfHunters = new GuiTextField(INPUT_NUMBER_OF_HUNTERS, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += INPUT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));
        inputList.add(inputToolCoolingDownTime = new GuiTextField(INPUT_TOOL_COOLING_DOWN_TIME, this.fontRenderer, offsetX + leastX + ELEMENTS_PADDING + 6, y += INPUT_HEIGHT + ELEMENTS_PADDING, inputWidth, INPUT_HEIGHT));

        int x = offsetX + leastX + ELEMENTS_PADDING + 6;
        int oneThirdWidth = (inputWidth - ELEMENTS_PADDING * 2) / 3;
        inputList.add(inputHallPositionX = new GuiTextField(INPUT_HALL_POSITION_X, this.fontRenderer, x, y += INPUT_HEIGHT + ELEMENTS_PADDING, oneThirdWidth, INPUT_HEIGHT));
        inputList.add(inputHallPositionY = new GuiTextField(INPUT_HALL_POSITION_Y, this.fontRenderer, x += oneThirdWidth + ELEMENTS_PADDING, y, oneThirdWidth, INPUT_HEIGHT));
        inputList.add(inputHallPositionZ = new GuiTextField(INPUT_HALL_POSITION_Z, this.fontRenderer, x += oneThirdWidth + ELEMENTS_PADDING, y, oneThirdWidth, INPUT_HEIGHT));
        Vec3d hallPosition = SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world).getHallPosition();
        inputHallPositionX.setText(String.valueOf(hallPosition.x));
        inputHallPositionY.setText(String.valueOf(hallPosition.y));
        inputHallPositionZ.setText(String.valueOf(hallPosition.z));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }


    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
        for (GuiTextField textField : inputList) {
            textField.updateCursorCounter();
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
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTextField textField : inputList) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
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
        SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world).add(new Vec3d(0, 0, 0), getInputTimeForHunterToWait());
        MessageSettings message = new MessageSettings();
        message.nbt = new NBTTagCompound();
        SettingsWorldSavedData.getGlobal(inventorySlotsIn.player.world).writeToNBT(message.nbt);
        message.nbt.setString("player", inventorySlotsIn.player.getUniqueID().toString());
        NetworkLoader.instance.sendToServer(message);
    }

    public int getInputTimeForHunterToWait() {
        int num;

        try {
            num = Integer.parseInt(inputTimeForHunterToWait.getText());
        } catch (Exception e) {
            num = 30;
        }

        if (num < 0) {
            num = 30;
        }

        return num;
    }

}
