package tonius.emobile.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotLocked extends Slot {
    
    public SlotLocked(IInventory inv, int slotId, int x, int y) {
        super(inv, slotId, x, y);
    }
    
    @Override
    public void putStack(ItemStack stack) {
    }
    
    @Override
    public ItemStack decrStackSize(int slot) {
        return null;
    }
    
    @Override
    public boolean canTakeStack(EntityPlayer player) {
        return false;
    }
    
    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
    
}
