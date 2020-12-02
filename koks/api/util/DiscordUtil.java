package koks.api.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import koks.api.interfaces.Wrapper;

/**
 * @author kroko
 * @created on 25.10.2020 : 17:39
 */
public class DiscordUtil implements Wrapper {

    private static DiscordUtil SINGLETON;
    private DiscordRichPresence presence;
    private DiscordRPC lib;
    public DiscordUtil(){
        SINGLETON = this;
    }

    public void setupRPC(String applicationID) {
        try {
            lib = DiscordRPC.INSTANCE;
            presence = new DiscordRichPresence();
            String steamId = "";
            DiscordEventHandlers handlers = new DiscordEventHandlers();

            handlers.ready = (user) -> logger.log("Initialized Discord RPC");

            lib.Discord_Initialize(applicationID, handlers, true, steamId);

            presence.startTimestamp = System.currentTimeMillis() / 1000;
            presence.details = "Playing Koks";
            presence.largeImageKey = "koks-logo";
            lib.Discord_UpdatePresence(presence);

            new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    lib.Discord_RunCallbacks();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }, "RPC-Callback-Handler").start();

        }catch(Exception ignored){}
    }

    public void updateRPC(DiscordRichPresence presence){
        lib.Discord_UpdatePresence(presence);
    }

    public DiscordRichPresence getPresence() {
        return presence;
    }

    public static DiscordUtil getSingleton() {
        return SINGLETON;
    }
}
