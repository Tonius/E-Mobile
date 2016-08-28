package tonius.emobile.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCellphoneRF extends ContainerCellphoneBase {

    public ItemStack cellphone;
    private int lastEnergy;
    private Short energyFirstHalf = null;
    private Short energySecondHalf = null;

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
                ((ICrafting) this.crafters.get(i)).sendProgressBarUpdate(this, 0, (short) (energy >> 16));
                ((ICrafting) this.crafters.get(i)).sendProgressBarUpdate(this, 1, (short) (energy & 0xFFFF));
            }
            this.lastEnergy = energy;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int barId, int value) {
        if (barId == 0) {
            this.energyFirstHalf = (short) value;
        } else if (barId == 1) {
            this.energySecondHalf = (short) value;
        }
        if (this.energyFirstHalf != null && this.energySecondHalf != null) {
            StackUtils.getNBT(this.cellphone).setInteger("Energy", this.energyFirstHalf << 16 | this.energySecondHalf & 0xFFFF);
            this.energyFirstHalf = null;
            this.energySecondHalf = null;
        }
    }

}
