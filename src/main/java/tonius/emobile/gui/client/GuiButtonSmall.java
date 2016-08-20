package tonius.emobile.gui.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

public class GuiButtonSmall extends GuiButton {
    
    public GuiButtonSmall(int id, int x, int y, int width, int height, String caption) {
        super(id, x, y, width, height, caption);
    }
    
    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            mc.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_146123_n = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.xPosition, this.yPosition + this.height / 2, 0, 46 + k * 20 + 20 - this.height / 2, this.width / 2, this.height / 2);
            this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition + this.height / 2, 200 - this.width / 2, 46 + k * 20 + 20 - this.height / 2, this.width / 2, this.height / 2);
            this.mouseDragged(mc, x, y);
            int l = 14737632;
            
            if (this.packedFGColour != 0) {
                l = this.packedFGColour;
            } else if (!this.enabled) {
                l = 10526880;
            } else if (this.field_146123_n) {
                l = 16777120;
            }
            
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, l);
        }
    }
    
}
