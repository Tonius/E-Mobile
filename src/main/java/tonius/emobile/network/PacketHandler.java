package tonius.emobile.network;

import tonius.emobile.EMobile;
import tonius.emobile.network.message.MessageCellphoneAuthorize;
import tonius.emobile.network.message.MessageCellphoneCancel;
import tonius.emobile.network.message.MessageCellphoneHome;
import tonius.emobile.network.message.MessageCellphonePlayer;
import tonius.emobile.network.message.MessageCellphoneSpawn;
import tonius.emobile.network.message.MessageConfigSync;
import tonius.emobile.network.message.MessageDiallingSound;
import tonius.emobile.network.message.TeleportParticle;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler {
    
    public static final SimpleNetworkWrapper instance = NetworkRegistry.INSTANCE.newSimpleChannel("EMobile");
    
    public static void preInit() {
        EMobile.logger.info("Registering network messages");
        instance.registerMessage(MessageCellphonePlayer.class, MessageCellphonePlayer.class, 0, Side.SERVER);
        instance.registerMessage(MessageCellphoneAuthorize.class, MessageCellphoneAuthorize.class, 1, Side.SERVER);
        instance.registerMessage(MessageCellphoneSpawn.class, MessageCellphoneSpawn.class, 2, Side.SERVER);
        instance.registerMessage(MessageCellphoneHome.class, MessageCellphoneHome.class, 3, Side.SERVER);
        instance.registerMessage(MessageCellphoneCancel.class, MessageCellphoneCancel.class, 4, Side.SERVER);
        instance.registerMessage(MessageConfigSync.class, MessageConfigSync.class, 5, Side.CLIENT);
        instance.registerMessage(MessageDiallingSound.class, MessageDiallingSound.class, 6, Side.CLIENT);
        instance.registerMessage(TeleportParticle.class, TeleportParticle.class, 7, Side.CLIENT);
    }
}
