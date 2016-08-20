package tonius.emobile.item;

import cofh.api.energy.IEnergyContainerItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tonius.emobile.EMobile;
import tonius.emobile.gui.EMGuiHandler;
import tonius.emobile.util.StackUtils;

import java.text.DecimalFormat;
import java.util.List;

public class ItemCellphoneRF extends ItemCellphone implements IEnergyContainerItem {
    
    private static final DecimalFormat formatter = new DecimalFormat("###,###");
    public int maxEnergy;
    public int maxInput;
    public int energyPerUse;
    
    public ItemCellphoneRF(int maxEnergy, int maxInput, int energyPerUse) {
        this.maxEnergy = maxEnergy;
        this.maxInput = maxInput;
        this.energyPerUse = energyPerUse;
        this.setUnlocalizedName("emobile.cellphone.rf");
        this.setTextureName("emobile:cellphone.rf");
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    @Override
    public boolean useFuel(ItemStack cellphone, EntityPlayer player) {
        int energy = this.getEnergyStored(cellphone);
        int energyExtracted = Math.min(energy, this.energyPerUse);
        energy -= energyExtracted;
        StackUtils.getNBT(cellphone).setInteger("Energy", energy);
        return energyExtracted == this.energyPerUse;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack cellphone, World world, EntityPlayer player) {
        if (!world.isRemote) {
            player.openGui(EMobile.instance, EMGuiHandler.CELLPHONE_RF, world, 0, 0, 0);
        }
        
        return cellphone;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean bool) {
        list.add(formatter.format(this.getEnergyStored(itemStack)) + " / " + formatter.format(this.getMaxEnergyStored(itemStack)) + " RF");
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(this));
        ItemStack charged = new ItemStack(this);
        StackUtils.getNBT(charged).setInteger("Energy", this.getMaxEnergyStored(charged));
        list.add(charged);
    }
    
    @Override
    public boolean showDurabilityBar(ItemStack itemStack) {
        return true;
    }
    
    @Override
    public double getDurabilityForDisplay(ItemStack itemStack) {
        double stored = this.getMaxEnergyStored(itemStack) - this.getEnergyStored(itemStack) + 1;
        double max = this.getMaxEnergyStored(itemStack) + 1;
        return stored / max;
    }
    
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        int energy = this.getEnergyStored(container);
        int energyReceived = Math.min(this.getMaxEnergyStored(container) - energy, Math.min(maxReceive, this.maxInput));
        
        if (!simulate) {
            energy += energyReceived;
            StackUtils.getNBT(container).setInteger("Energy", energy);
        }
        return energyReceived;
    }
    
    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        return 0;
    }
    
    @Override
    public int getEnergyStored(ItemStack container) {
        return StackUtils.getNBT(container).getInteger("Energy");
    }
    
    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return this.maxEnergy;
    }
    
}
