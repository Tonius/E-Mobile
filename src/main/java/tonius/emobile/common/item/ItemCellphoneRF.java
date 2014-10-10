package tonius.emobile.common.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tonius.emobile.common.EMobile;
import tonius.emobile.common.gui.EMGuiHandler;
import tonius.emobile.common.util.StackUtils;
import tonius.emobile.common.util.StringUtils;
import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCellphoneRF extends ItemCellphone implements IEnergyContainerItem {
    
    private int maxEnergy;
    private int maxInput;
    private int energyPerUse;
    
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
        return this.extractEnergy(cellphone, this.energyPerUse, false) == this.energyPerUse;
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
        list.add(String.format(StringUtils.translate("tooltip.cellphone.rf"), this.getEnergyStored(itemStack)));
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
        int energy = this.getEnergyStored(container);
        int energyExtracted = Math.min(energy, maxExtract);
        
        if (!simulate) {
            energy -= energyExtracted;
            StackUtils.getNBT(container).setInteger("Energy", energy);
        }
        return energyExtracted;
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
