package koks.module.impl.combat;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventPacket;
import koks.event.impl.EventUpdate;
import koks.event.impl.EventVelocity;
import koks.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 12:15
 */
public class Velocity extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Cancel", "Jump", "Intave", "AAC3", "AAC4"}, "Cancel", this);

    public Velocity() {
        super("Velocity", "Reduces your knockback", Category.COMBAT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
        }
        switch (mode.getCurrentMode()) {
            case "Cancel":
                if (event instanceof EventPacket && ((EventPacket) event).getType() == EventPacket.Type.RECEIVE) {
                    Packet packet = ((EventPacket) event).getPacket();
                    if (packet instanceof S12PacketEntityVelocity || packet instanceof S27PacketExplosion) {
                        event.setCanceled(true);
                    }
                }
                break;
            case "Jump":
                if (event instanceof EventUpdate) {
                    if (getHurtTime() == 10 && getPlayer().onGround) {
                        getPlayer().jump();
                    }
                }
                break;
            case "AAC3":
                if (event instanceof EventUpdate) {
                    if (mc.thePlayer.hurtTime > 0) {
                        mc.thePlayer.motionX *= 0.8;
                        mc.thePlayer.motionZ *= 0.8;
                        mc.thePlayer.motionY *= 1;
                    }
                }
                break;
            case "Intave":
                if (event instanceof EventVelocity) {
                    if (getHurtTime() == 10) {
                        getPlayer().setPosition(getX(), getY(), getZ());
                    } else if (getHurtTime() == 9) {
                        event.setCanceled(true);
                    }
                }
                break;
            case "AAC4":
                if (event instanceof EventVelocity) {
                    if (getHurtTime() != 0) {
                        ((EventVelocity) event).setHorizontal(93);
                        ((EventVelocity) event).setVertical(86);
                    }
                }
                break;
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}