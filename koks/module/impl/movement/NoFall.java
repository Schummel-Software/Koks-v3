package koks.module.impl.movement;

import koks.api.settings.Setting;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.util.BlockPos;

/**
 * @author avox | lmao | kroko
 * @created on 15.09.2020 : 18:13
 */
public class NoFall extends Module {

    public NoFall() {
        super("NoFall", "Prevents you from getting falldamage", Category.MOVEMENT);
    }

    public Setting mode = new Setting("Mode", new String[]{"Mineplex", "Intave"}, "Mineplex", this);

    public TimeHelper timeHelper = new TimeHelper();

    public void mineplex() {
        if (getPlayer().fallDistance > 2) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            getPlayer().fallDistance = 0;
        }
    }

    public void intave() {
        if(getPlayer().fallDistance > 3.8F) {
            getPlayer().capabilities.isFlying = true;
            getPlayer().onGround = true;
            getTimer().timerSpeed = 1.05F;
            if(timeHelper.hasReached(60)) {
                getPlayer().onGround = true;
            }else{
                getPlayer().onGround = true;
            }
        }else{
            if(getPlayer().fallDistance == 0){
                getTimer().timerSpeed = 1.0F;
                timeHelper.reset();
            }
            getPlayer().capabilities.isFlying = false;

        }
    }

    @Override
    public void onEvent(Event event) {
        switch (mode.getCurrentMode()) {
            case "Mineplex":
                if (event instanceof EventUpdate) {
                    mineplex();
                }
                break;
            case "Intave":
                if (event instanceof EventUpdate) {
                    intave();
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