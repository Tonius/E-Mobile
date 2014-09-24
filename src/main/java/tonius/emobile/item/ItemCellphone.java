package tonius.emobile.item;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tonius.emobile.EMobile;
import tonius.emobile.gui.EMGuiHandler;
import tonius.emobile.gui.InventoryCellphone;
import tonius.emobile.util.StringUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCellphone extends Item {
    
    public ItemCellphone() {
        this.setMaxStackSize(1);
        this.setUnlocalizedName("emobile.cellphone");
        this.setTextureName("emobile:cellphone");
        this.setCreativeTab(CreativeTabs.tabTools);
    }
    
    public boolean usePearl(ItemStack cellphone, EntityPlayer player) {
        return new InventoryCellphone(cellphone).usePearl();
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack cellphone, World world, EntityPlayer player) {
        if (!world.isRemote) {
            player.openGui(EMobile.instance, EMGuiHandler.CELLPHONE, world, 0, 0, 0);
        }
        
        return cellphone;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean bool) {
        list.add(String.format(StringUtils.translate("tooltip.cellphone.pearls"), new InventoryCellphone(itemStack).getStoredPearls()));
    }
    
}
