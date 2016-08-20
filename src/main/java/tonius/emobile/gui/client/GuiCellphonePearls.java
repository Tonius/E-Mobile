package tonius.emobile.gui.client;

import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import tonius.emobile.EMobile;
import tonius.emobile.gui.container.ContainerCellphonePearls;
import tonius.emobile.util.StringUtils;

import java.util.List;

public class GuiCellphonePearls extends GuiCellphoneBase {
    
    public GuiCellphonePearls(ContainerCellphonePearls cellphone) {
        super(cellphone);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        super.drawGuiContainerForegroundLayer(param1, param2);
        this.fontRendererObj.drawString(EMobile.cellphone.getItemStackDisplayName(null), 8, 6, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.guiLeft + 151, this.guiTop + 7, 176, 0, 18, 18);
    }
    
    @Override
    protected void getTooltipLines(List lines, int mouseX, int mouseY) {
        super.getTooltipLines(lines, mouseX, mouseY);
        if (this.func_146978_c(152, 8, 16, 16, mouseX, mouseY) && this.inventorySlots.getInventory().get(0) == null && this.mc.thePlayer.inventory.getItemStack() == null) {
            lines.add(StringUtils.ITALIC + StringUtils.translate("gui.cellphone.pearls.1"));
            lines.add(StringUtils.ITALIC + StringUtils.translate("gui.cellphone.pearls.2"));
            if (this.mc.thePlayer.capabilities.isCreativeMode) {
                lines.add(StringUtils.BRIGHT_GREEN + StringUtils.ITALIC + StringUtils.translate("gui.cellphone.pearls.creative"));
            }
        }
    }
    
    @Override
    protected boolean hasEnoughFuel() {
        return this.mc.thePlayer.capabilities.isCreativeMode || this.getPearls() > 0;
    }
    
    private int getPearls() {
        ItemStack pearls = (ItemStack) this.inventorySlots.getInventory().get(0);
        return pearls != null && pearls.getItem() instanceof ItemEnderPearl ? pearls.stackSize : 0;
    }
    
}
