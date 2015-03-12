package tonius.emobile.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerCellphoneBase extends Container {
    
    protected int cellphoneSlot;
    
    protected ContainerCellphoneBase(InventoryPlayer invPlayer) {
        this.cellphoneSlot = invPlayer.currentItem;
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
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
    
}
