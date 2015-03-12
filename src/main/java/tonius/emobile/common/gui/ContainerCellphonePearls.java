package tonius.emobile.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

public class ContainerCellphonePearls extends ContainerCellphoneBase {
    
    private InventoryCellphone invCellphone;
    
    public ContainerCellphonePearls(InventoryPlayer invPlayer, InventoryCellphone invCellphone) {
        super(invPlayer);
        this.invCellphone = invCellphone;
        this.addSlotToContainer(new SlotEnderPearls(this.invCellphone, 0, 152, 8));
        this.bindPlayerInventory(invPlayer, 8, 121);
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        ItemStack invStack = player.inventory.mainInventory[this.cellphoneSlot];
        ItemStack containerStack = this.invCellphone.getCellphone();
        if (invStack != null && containerStack != null && invStack.isItemEqual(containerStack)) {
            player.inventory.mainInventory[this.cellphoneSlot] = containerStack;
        }
        return true;
    }
    
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
        ItemStack itemStack = null;
        Slot slotObject = (Slot) this.inventorySlots.get(slot);
        
        if (slotObject != null && slotObject.getHasStack()) {
            ItemStack stackInSlot = slotObject.getStack();
            itemStack = stackInSlot.copy();
            
            if (slot <= 0) {
                if (!this.mergeItemStack(stackInSlot, 1, 37, true)) {
                    return null;
                }
            } else {
                if (stackInSlot != null && stackInSlot.getItem() instanceof ItemEnderPearl) {
                    if (!this.mergeItemStack(stackInSlot, 0, 1, false)) {
                        return null;
                    }
                }
            }
            
            if (stackInSlot.stackSize == 0) {
                slotObject.putStack(null);
            } else {
                slotObject.onSlotChanged();
            }
            
            if (stackInSlot.stackSize == itemStack.stackSize) {
                return null;
            }
            slotObject.onPickupFromSlot(player, stackInSlot);
        }
        return itemStack;
    }
    
}
