package tonius.emobile.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tonius.emobile.gui.client.GuiCellphonePearls;
import tonius.emobile.gui.client.GuiCellphoneRF;
import tonius.emobile.gui.container.ContainerCellphonePearls;
import tonius.emobile.gui.container.ContainerCellphoneRF;
import tonius.emobile.inventory.InventoryCellphone;
import tonius.emobile.item.ItemCellphone;
import tonius.emobile.item.ItemCellphoneRF;
import cpw.mods.fml.common.network.IGuiHandler;

public class EMGuiHandler implements IGuiHandler {
    
    public static final int CELLPHONE_PEARL = 0;
    public static final int CELLPHONE_RF = 1;
    
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        case CELLPHONE_PEARL:
            ItemStack cellphonePearls = player.getCurrentEquippedItem();
            if (cellphonePearls != null && cellphonePearls.getItem() instanceof ItemCellphone) {
                return new ContainerCellphonePearls(player.inventory, new InventoryCellphone(cellphonePearls));
            }
            break;
        case CELLPHONE_RF:
            ItemStack cellphoneRF = player.getCurrentEquippedItem();
            if (cellphoneRF != null && cellphoneRF.getItem() instanceof ItemCellphoneRF) {
                return new ContainerCellphoneRF(player.inventory, cellphoneRF);
            }
        }
        return null;
    }
    
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
        case CELLPHONE_PEARL:
            ItemStack cellphonePearls = player.getCurrentEquippedItem();
            if (cellphonePearls != null && cellphonePearls.getItem() instanceof ItemCellphone) {
                return new GuiCellphonePearls(new ContainerCellphonePearls(player.inventory, new InventoryCellphone(cellphonePearls)));
            }
            break;
        case CELLPHONE_RF:
            ItemStack cellphoneRF = player.getCurrentEquippedItem();
            if (cellphoneRF != null && cellphoneRF.getItem() instanceof ItemCellphone) {
                return new GuiCellphoneRF(new ContainerCellphoneRF(player.inventory, cellphoneRF));
            }
        }
        return null;
    }
    
}
