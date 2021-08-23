package io.github.myworldzycpc.block_has.client.gui;

import io.github.myworldzycpc.block_has.init.ModItems;
import io.github.myworldzycpc.block_has.inventory.ContainerDemo;
import io.github.myworldzycpc.block_has.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiContainerDemo extends GuiContainer {

    private static final String TEXTURE_PATH = Reference.MOD_ID + ":" + "textures/gui/container/gui_demo.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

    private static final int BUTTON_UP = 0;
    private static final int BUTTON_DOWN = 1;
    private static final int TEXT_FIELD_TEST = 3;
    private GuiTextField test;

    private Slot ironSlot;

    public GuiContainerDemo(ContainerDemo inventorySlotsIn) {
        super(inventorySlotsIn);
        this.xSize = 176;
        this.ySize = 133;
        this.ironSlot = inventorySlotsIn.getIronSlot();
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
        this.drawVerticalLine(30, 19, 36, 0xFF000000);
        this.drawHorizontalLine(8, 167, 43, 0xFF000000);

        String title = I18n.format("block_has.container.demo");
        this.fontRenderer.drawString(title, (this.xSize - this.fontRenderer.getStringWidth(title)) / 2, 6, 0x404040);

        ItemStack item = new ItemStack(ModItems.DEBUG);
        this.itemRender.renderItemAndEffectIntoGUI(item, 8, 20);
    }

    @Override
    public void initGui() {

        super.initGui();
        int offsetX = (this.width - this.xSize) / 2, offsetY = (this.height - this.ySize) / 2;

        this.buttonList.add(new GuiButton(BUTTON_UP, offsetX + 153, offsetY + 17, 15, 10, "") {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                if (this.visible) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F);

                    mc.getTextureManager().bindTexture(TEXTURE);
                    int x = mouseX - this.x, y = mouseY - this.y;

                    if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
                        this.drawTexturedModalRect(this.x, this.y, 1, 146, this.width, this.height);
                    } else {
                        this.drawTexturedModalRect(this.x, this.y, 1, 134, this.width, this.height);
                    }
                }
            }
        });

        this.buttonList.add(new GuiButton(BUTTON_DOWN, offsetX + 153, offsetY + 29, 15, 10, "") {
            @Override
            public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
                if (this.visible) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F);

                    mc.getTextureManager().bindTexture(TEXTURE);
                    int x = mouseX - this.x, y = mouseY - this.y;

                    if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
                        this.drawTexturedModalRect(this.x, this.y, 20, 146, this.width, this.height);
                    } else {
                        this.drawTexturedModalRect(this.x, this.y, 20, 134, this.width, this.height);
                    }
                }
            }
        });

        test = new GuiTextField(TEXT_FIELD_TEST, this.fontRenderer, offsetX + 0, offsetY + 0, 176, 20);
        test.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        ItemStack stack = this.ironSlot.getStack();
        int amount = stack.isEmpty() ? 0 : stack.getCount();

        switch (button.id) {
            case BUTTON_DOWN:
                amount = (amount + 64) % 65;
                break;
            case BUTTON_UP:
                amount = (amount + 1) % 65;
                break;
            default:
                super.actionPerformed(button);
                return;
        }

        this.ironSlot.putStack(amount == 0 ? new ItemStack(Items.IRON_INGOT, amount) : new ItemStack(Items.IRON_INGOT, amount));
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        this.test.updateCursorCounter();
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.test.textboxKeyTyped(typedChar, keyCode);

    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.test.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        test.drawTextBox();
    }

}
