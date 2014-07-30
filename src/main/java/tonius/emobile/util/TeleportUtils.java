package tonius.emobile.util;

import net.minecraft.entity.player.EntityPlayer;

public class TeleportUtils {

    public static boolean teleportPlayerToPlayer(EntityPlayer from, EntityPlayer to) {
        int destX = (int) to.posX;
        int destY = (int) to.posY;
        int destZ = (int) to.posZ;
        if (from.worldObj.provider.dimensionId != to.worldObj.provider.dimensionId) {
            return false;
        }
        from.setPositionAndUpdate(destX + 0.5D, destY + 0.5D, destZ + 0.5D);
        return true;
    }

    public static boolean teleportPlayerToPos(EntityPlayer player, int dimension, int posX, int posY, int posZ) {
        if (player.worldObj.provider.dimensionId != dimension) {
            return false;
        }
        player.setPositionAndUpdate(posX + 0.5D, posY + 0.5D, posZ + 0.5D);
        return true;
    }

}
