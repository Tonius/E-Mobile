package tonius.emobile.gui.client;

import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import tonius.emobile.EMobile;
import tonius.emobile.gui.container.ContainerCellphoneRF;
import tonius.emobile.item.ItemCellphoneRF;
import tonius.emobile.util.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

public class GuiCellphoneRF extends GuiCellphoneBase {
    
    private static final DecimalFormat formatter = new DecimalFormat("###,###");
    
    public GuiCellphoneRF(ContainerCellphoneRF container) {
        super(container);
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(int param1, int param2) {
        super.drawGuiContainerForegroundLayer(param1, param2);
        this.fontRendererObj.drawString(StringUtils.translate("gui.cellphone.rf"), 8, 6, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        super.drawGuiContainerBackgroundLayer(f, i, j);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.guiLeft + 127, this.guiTop + 7, 176, 18, 42, 14);
        this.drawTexturedModalRect(this.guiLeft + 127, this.guiTop + 7, 176, 32, (int) (42 * ((double) this.getEnergyStored() / (double) this.getMaxEnergyStored())), 14);
    }
    
    @Override
    protected void getTooltipLines(List lines, int mouseX, int mouseY) {
        super.getTooltipLines(lines, mouseX, mouseY);
        int energy = this.getEnergyStored();
        if (this.func_146978_c(127, 7, 42, 14, mouseX, mouseY)) {
            lines.add(formatter.format(energy) + " / " + formatter.format(this.getMaxEnergyStored()) + " RF");
            if (energy == 0) {
                lines.add(StringUtils.ITALIC + StringUtils.translate("gui.cellphone.rf.1"));
                lines.add(StringUtils.ITALIC + StringUtils.translate("gui.cellphone.rf.2"));
                if (this.mc.thePlayer.capabilities.isCreativeMode) {
                    lines.add(StringUtils.BRIGHT_GREEN + StringUtils.ITALIC + StringUtils.translate("gui.cellphone.pearls.creative"));
                }
            }
            lines.add(StringUtils.LIGHT_GRAY + StringUtils.ITALIC + String.format(StringUtils.translate("gui.cellphone.rf.3"), formatter.format(EMobile.cellphoneRF.energyPerUse)));
        }
    }
    
    @Override
    protected boolean hasEnoughFuel() {
        return this.mc.thePlayer.capabilities.isCreativeMode || this.getEnergyStored() >= EMobile.cellphoneRF.energyPerUse;
    }
    
    private int getEnergyStored() {
        ItemStack cellphone = this.mc.thePlayer.getCurrentEquippedItem();
        if (cellphone != null && cellphone.getItem() instanceof ItemCellphoneRF) {
            ItemCellphoneRF cellphoneItem = (ItemCellphoneRF) cellphone.getItem();
            return cellphoneItem.getEnergyStored(cellphone);
        }
        return 0;
    }
    
    private int getMaxEnergyStored() {
        ItemStack cellphone = this.mc.thePlayer.getCurrentEquippedItem();
        if (cellphone != null && cellphone.getItem() instanceof ItemCellphoneRF) {
            ItemCellphoneRF cellphoneItem = (ItemCellphoneRF) cellphone.getItem();
            return cellphoneItem.getMaxEnergyStored(cellphone);
        }
        return 0;
    }
    
}
