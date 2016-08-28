package tonius.emobile.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import tonius.emobile.gui.client.GuiCellphonePearls;
import tonius.emobile.gui.container.ContainerCellphonePearls;
import tonius.emobile.inventory.InventoryCellphone;
import tonius.emobile.item.ItemCellphone;

public class EMGuiHandler implements IGuiHandler {

    public static final int CELLPHONE_PEARL = 0;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case CELLPHONE_PEARL:
                ItemStack cellphonePearls = player.getCurrentEquippedItem();
                if (cellphonePearls != null && cellphonePearls.getItem() instanceof ItemCellphone) {
                    return new ContainerCellphonePearls(player.inventory, new InventoryCellphone(cellphonePearls));
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
        }
        return null;
    }

}
