package tonius.emobile.session;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

public class CellphoneSessionsManager {

    private static List<CellphoneSessionBase> sessions = new ArrayList<>();
    private static Map<EntityPlayerMP, Map<EntityPlayerMP, Boolean>> acceptedPlayers = new HashMap<>();

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
            players = new HashMap<>();
        }
        acceptedPlayers.put(player, players);

        return players;
    }

    public static boolean acceptPlayer(EntityPlayerMP accepting, EntityPlayerMP accepted, boolean permanent) {
        if (!isPlayerAccepted(accepting, accepted)) {
            getAcceptedPlayersForPlayer(accepting).put(accepted, permanent);
            if (permanent) {
                NBTTagList permanentlyAccepted = accepting.getEntityData().getTagList("EMobile.PermanentlyAccepted", 8);
                permanentlyAccepted.appendTag(new NBTTagString(accepted.getName()));
                accepting.getEntityData().setTag("EMobile.PermanentlyAccepted", permanentlyAccepted);
            }
            return true;
        }

        return false;
    }

    public static boolean deacceptPlayer(EntityPlayerMP deaccepting, EntityPlayerMP deaccepted, boolean force) {
        if (isPlayerAccepted(deaccepting, deaccepted)) {
            if (!getAcceptedPlayersForPlayer(deaccepting).get(deaccepted) || force) {
                getAcceptedPlayersForPlayer(deaccepting).remove(deaccepted);
                String deacceptedName = deaccepted.getName();
                NBTTagList permaAccepted = deaccepting.getEntityData().getTagList("EMobile.PermanentlyAccepted", 8);
                for (int i = 0; i < permaAccepted.tagCount(); i++) {
                    if (permaAccepted.getStringTagAt(i).equals(deacceptedName)) {
                        permaAccepted.removeTag(i);
                    }
                }
                deaccepting.getEntityData().setTag("EMobile.PermaAccepted", permaAccepted);
            }
            return true;
        }

        return false;
    }

    public static boolean isPlayerAccepted(EntityPlayerMP acceptor, EntityPlayerMP query) {
        if (getAcceptedPlayersForPlayer(acceptor).containsKey(query)) {
            return true;
        }

        String queryName = query.getName();
        NBTTagList permaAccepted = acceptor.getEntityData().getTagList("EMobile.PermanentlyAccepted", 8);

        for (int i = 0; i < permaAccepted.tagCount(); i++) {
            if (permaAccepted.getStringTagAt(i).equals(queryName)) {
                getAcceptedPlayersForPlayer(acceptor).put(query, true);
                return true;
            }
        }

        return false;
    }

    public static void cancelSessionsForPlayer(EntityPlayer player) {
        for (CellphoneSessionBase session : sessions) {
            if (session.isPlayerInSession(player)) {
                session.cancel(player.getName());
            }
        }
    }

    @SubscribeEvent
    public void tickEnd(TickEvent.ServerTickEvent evt) {
        if (evt.phase != TickEvent.Phase.END) {
            return;
        }

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
