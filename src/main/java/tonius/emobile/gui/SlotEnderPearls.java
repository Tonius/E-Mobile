package tonius.emobile.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

public class SlotEnderPearls extends Slot {
    
    private InventoryPlayer invPlayer;
    
    public SlotEnderPearls(InventoryCellphone inv, InventoryPlayer invPlayer, int slotId, int x, int y) {
        super(inv, slotId, x, y);
        this.invPlayer = invPlayer;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemEnderPearl;
    }
    
    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
        ItemStack invStack = this.invPlayer.mainInventory[this.slotNumber];
        ItemStack containerStack = ((InventoryCellphone) this.inventory).getCellphone();
        if (invStack != null && containerStack != null && invStack.isItemEqual(containerStack)) {
            this.invPlayer.mainInventory[this.slotNumber] = containerStack;
        }
    }
    
}
