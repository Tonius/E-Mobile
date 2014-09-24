package tonius.emobile.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class CellphoneSessionsHandler {
    
    private static List<CellphoneSessionBase> sessions = new ArrayList<CellphoneSessionBase>();
    private static Map<EntityPlayerMP, Map<EntityPlayerMP, Boolean>> acceptedPlayers = new HashMap<EntityPlayerMP, Map<EntityPlayerMP, Boolean>>();
    
    public static void addSession(CellphoneSessionBase session) {
        sessions.add(session);
    }
    
    public static void clearSessions() {
        sessions.clear();
        acceptedPlayers.clear();
    }
    
    public static boolean isPlayerInSession(EntityPlayerMP player) {
        for (CellphoneSessionBase session : sessions) {
            if (session.isPlayerInSession(player)) {
                return true;
            }
        }
        return false;
    }
    
    public static Map<EntityPlayerMP, Boolean> getAcceptedPlayersForPlayer(EntityPlayerMP player) {
        Map<EntityPlayerMP, Boolean> players = acceptedPlayers.get(player);
        if (players == null) {
            players = new HashMap<EntityPlayerMP, Boolean>();
        }
        acceptedPlayers.put(player, players);
        
        return players;
    }
    
    public static boolean acceptPlayer(EntityPlayerMP accepting, EntityPlayerMP accepted, boolean perma) {
        if (!isPlayerAccepted(accepting, accepted)) {
            getAcceptedPlayersForPlayer(accepting).put(accepted, perma);
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean deacceptPlayer(EntityPlayerMP deaccepting, EntityPlayerMP deaccepted, boolean force) {
        if (isPlayerAccepted(deaccepting, deaccepted)) {
            if (!getAcceptedPlayersForPlayer(deaccepting).get(deaccepted) || force) {
                getAcceptedPlayersForPlayer(deaccepting).remove(deaccepted);
            }
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean isPlayerAccepted(EntityPlayerMP acceptor, EntityPlayerMP query) {
        return getAcceptedPlayersForPlayer(acceptor).containsKey(query);
    }
    
    public static void cancelSessionsForPlayer(EntityPlayer player) {
        for (CellphoneSessionBase session : sessions) {
            if (session.isPlayerInSession(player)) {
                session.cancel(player.getCommandSenderName());
            }
        }
    }
    
    @SubscribeEvent
    public void tickEnd(ServerTickEvent evt) {
        if (evt.phase == Phase.END) {
            Iterator<CellphoneSessionBase> itr = sessions.iterator();
            while (itr.hasNext()) {
                CellphoneSessionBase session = itr.next();
                session.tick();
                if (!session.isValid()) {
                    itr.remove();
                }
            }
        }
    }
    
}
