package tonius.emobile.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;

public class ContainerCellphone extends Container {

    private InventoryCellphone invCellphone;
    private int cellphoneSlot;

    public ContainerCellphone(InventoryCellphone invCellphone, InventoryPlayer invPlayer) {
        this.invCellphone = invCellphone;
        this.cellphoneSlot = invPlayer.currentItem;

        this.addSlotToContainer(new SlotEnderPearls(this.invCellphone, 0, 152, 8));

        this.bindPlayerInventory(invPlayer, 8, 121);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
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

    protected void bindPlayerInventory(InventoryPlayer invPlayer, int xOffset, int yOffset) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            if (i == this.cellphoneSlot) {
                this.addSlotToContainer(new SlotLocked(invPlayer, i, xOffset + i * 18, yOffset + 58));
            } else {
                this.addSlotToContainer(new Slot(invPlayer, i, xOffset + i * 18, yOffset + 58));
            }
        }
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        ItemStack invStack = player.inventory.mainInventory[this.cellphoneSlot];
        ItemStack containerStack = this.invCellphone.getCellphone();
        if (invStack != null && containerStack != null && invStack.isItemEqual(containerStack)) {
            player.inventory.mainInventory[this.cellphoneSlot] = containerStack;
        } else {
            player.entityDropItem(((Slot) this.inventorySlots.get(0)).getStack(), 1.0F);
        }
        this.invCellphone.save();
    }

}
