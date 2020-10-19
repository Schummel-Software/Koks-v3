package koks.module.impl.combat;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventPacket;
import koks.event.impl.EventUpdate;
import koks.event.impl.EventVelocity;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

import java.util.Random;
import java.util.Set;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 12:15
 */

@ModuleInfo(name = "Velocity", description = "Reduces your knock back", category = Module.Category.COMBAT)
public class Velocity extends Module {

    public boolean wasOnGround;

    public Setting mode = new Setting("Mode", new String[]{"Cancel", "Jump", "Intave", "IntaveKeepLow", "AAC3", "AAC4", "Custom"}, "Cancel", this);

    public Setting horizontal = new Setting("Horizontal", 100, 0, 100, true, this);
    public Setting vertical = new Setting("Vertical", 100, 0, 100, true, this);
    public Setting canceled = new Setting("Canceled", false, this);
    public Setting hurtTime = new Setting("HurtTime", 10, 1, 10, true, this);
    public Setting onGround = new Setting("onGround", false, this);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
        }
        switch (mode.getCurrentMode()) {
            case "Custom":
                if(event instanceof EventVelocity) {
                    if(getHurtTime() <= hurtTime.getCurrentValue() && getHurtTime() != 0) {
                        ((EventVelocity) event).setVertical((int) vertical.getCurrentValue());
                        ((EventVelocity) event).setHorizontal((int) horizontal.getCurrentValue());
                        event.setCanceled(canceled.isToggled());
                        getPlayer().onGround = onGround.isToggled();
                    }
                }
                break;
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
                    if (getPlayer().hurtTime != 0) {
                        if (!getPlayer().onGround) {
                            if (getPlayer().hurtTime > 3 && getPlayer().hurtTime <= 5) {
                                getPlayer().motionY -= 0.01f;
                            }
                        }
                    }
                }
                break;
            case "IntaveKeepLow":
                if (event instanceof EventUpdate) {
                    switch (getHurtTime()) {
                        case 10:
                            if (getPlayer().onGround) {
                                wasOnGround = true;
                            }
                            break;
                        case 0:
                            wasOnGround = false;
                            break;
                        case 9:
                            if (wasOnGround) {
                                getPlayer().motionY = 0.0D;
                            }
                            break;
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