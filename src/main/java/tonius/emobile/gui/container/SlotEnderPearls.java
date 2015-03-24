package tonius.emobile.gui.container;

import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import tonius.emobile.inventory.InventoryCellphone;

public class SlotEnderPearls extends Slot {
    
    public SlotEnderPearls(InventoryCellphone inv, int slotId, int x, int y) {
        super(inv, slotId, x, y);
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemEnderPearl;
    }
    
}
