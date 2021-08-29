package io.github.myworldzycpc.block_has.client.gui;

import io.github.myworldzycpc.block_has.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class MapSelectButton extends GuiButton {

    private static final String TEXTURE_PATH = Reference.MOD_ID + ":" + "textures/gui/container/gui_add_map.png";
    private static final ResourceLocation TEXTURE = new ResourceLocation(TEXTURE_PATH);

    public boolean selected = false;

    public MapSelectButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            GlStateManager.color(1.0F, 1.0F, 1.0F);

            mc.getTextureManager().bindTexture(TEXTURE);
            int x = mouseX - this.x, y = mouseY - this.y;
            if (this.selected) {
                if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
                    this.drawTexturedModalRect(this.x, this.y, 0, 227, this.width, this.height);
                } else {
                    this.drawTexturedModalRect(this.x, this.y, 0, 215, this.width, this.height);
                }
            } else {
                if (x >= 0 && y >= 0 && x < this.width && y < this.height) {
                    this.drawTexturedModalRect(this.x, this.y, 0, 203, this.width, this.height);
                } else {
                    this.drawTexturedModalRect(this.x, this.y, 0, 192, this.width, this.height);
                }
            }
            FontRenderer fontrenderer = mc.fontRenderer;
            this.drawString(fontrenderer, this.displayString, this.x, this.y, 0xe0e0e0);

        }
    }
}
