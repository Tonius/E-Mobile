package tonius.emobile.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.item.ItemStack;
import tonius.emobile.util.StackUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerCellphoneRF extends ContainerCellphoneBase {
    
    public ItemStack cellphone;
    private int lastEnergy;
    
    public ContainerCellphoneRF(InventoryPlayer invPlayer, ItemStack cellphone) {
        super(invPlayer);
        this.cellphone = cellphone;
        this.bindPlayerInventory(invPlayer, 8, 121);
    }
    
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        ItemStack invStack = player.inventory.mainInventory[this.cellphoneSlot];
        if (invStack != null && invStack.isItemEqual(this.cellphone)) {
            this.cellphone = invStack;
        }
        return true;
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        
        int energy = StackUtils.getNBT(this.cellphone).getInteger("Energy");
        
        if (energy != this.lastEnergy) {
            for (int i = 0; i < this.crafters.size(); ++i) {
                ((ICrafting) this.crafters.get(i)).sendProgressBarUpdate(this, 0, energy);
            }
            this.lastEnergy = energy;
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int barId, int value) {
        if (barId == 0) {
            StackUtils.getNBT(this.cellphone).setInteger("Energy", value);
        }
    }
    
}
