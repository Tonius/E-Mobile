package tonius.emobile.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import tonius.emobile.common.item.ItemCellphonePearls;
import tonius.emobile.common.util.StackUtils;

public class InventoryCellphone implements IInventory {
    
    private ItemStack cellphone;
    private ItemStack[] inv = new ItemStack[1];
    
    public InventoryCellphone(ItemStack cellphone) {
        if (cellphone == null || !(cellphone.getItem() instanceof ItemCellphonePearls)) {
            throw new IllegalArgumentException("Invalid ItemStack when creating a " + this.getClass().getSimpleName() + " instance");
        }
        this.cellphone = cellphone;
        
        this.readFromNBT(StackUtils.getNBT(cellphone));
    }
    
    public ItemStack getCellphone() {
        this.markDirty();
        return this.cellphone;
    }
    
    public int getStoredPearls() {
        ItemStack pearls = this.getStackInSlot(0);
        return pearls != null ? pearls.stackSize : 0;
    }
    
    public boolean useFuel() {
        if (this.getStoredPearls() <= 0) {
            return false;
        }
        
        this.getStackInSlot(0);
        this.decrStackSize(0, 1);
        this.markDirty();
        return true;
    }
    
    public void readFromNBT(NBTTagCompound tagCompound) {
        NBTTagList tagList = tagCompound.getTagList("Inventory", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tag = tagList.getCompoundTagAt(i);
            byte slot = tag.getByte("Slot");
            if (slot >= 0 && slot < this.inv.length) {
                this.inv[slot] = ItemStack.loadItemStackFromNBT(tag);
            }
        }
    }
    
    public void writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList itemList = new NBTTagList();
        for (int i = 0; i < this.inv.length; i++) {
            ItemStack stack = this.inv[i];
            if (stack != null) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setByte("Slot", (byte) i);
                stack.writeToNBT(tag);
                itemList.appendTag(tag);
            }
        }
        tagCompound.setTag("Inventory", itemList);
    }
    
    @Override
    public int getSizeInventory() {
        return this.inv.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inv[slot];
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        ItemStack itemStack = this.getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                this.setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    this.setInventorySlotContents(slot, null);
                }
            }
        }
        return itemStack;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        ItemStack itemStack = this.getStackInSlot(slot);
        if (itemStack != null) {
            this.setInventorySlotContents(slot, null);
        }
        return itemStack;
    }
    
    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        this.inv[slot] = itemStack;
        if (itemStack != null && itemStack.stackSize > this.getInventoryStackLimit()) {
            itemStack.stackSize = this.getInventoryStackLimit();
        }
    }
    
    @Override
    public String getInventoryName() {
        return this.cellphone.getDisplayName();
    }
    
    @Override
    public boolean hasCustomInventoryName() {
        return true;
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void markDirty() {
        NBTTagCompound tagCompound = StackUtils.getNBT(this.cellphone);
        this.writeToNBT(tagCompound);
        this.cellphone.setTagCompound(tagCompound);
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }
    
    @Override
    public void openInventory() {
    }
    
    @Override
    public void closeInventory() {
    }
    
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack item) {
        return item != null && item.getItem() instanceof ItemEnderPearl;
    }
    
}
