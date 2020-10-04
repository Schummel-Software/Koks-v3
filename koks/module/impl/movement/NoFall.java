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

    public Setting mode = new Setting("Mode", new String[]{"Mineplex", "Intave", "AAC4"}, "Mineplex", this);

    public TimeHelper timeHelper = new TimeHelper();

    public void mineplex() {
        if (getPlayer().fallDistance > 2) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
            getPlayer().fallDistance = 0;
        }
    }

    public void intave() {
        if (getPlayer().fallDistance > 3.8F) {
            if(timeHelper.hasReached(60)) {
                getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(true));
                PlayerCapabilities capabilities = getPlayer().capabilities;
                capabilities.isFlying = true;
                getPlayer().sendQueue.addToSendQueue(new C13PacketPlayerAbilities(capabilities));
                timeHelper.reset();
            }
        }
    }

    public void aac4() {
        if (!getPlayer().onGround && getPlayer().fallDistance > 1.2 && getPlayer().ticksExisted % 3 == 2) {
            getPlayer().motionY = 0;
            getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(true));
            getPlayer().fallDistance = 1;
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
        }
        switch (mode.getCurrentMode()) {
            case "Mineplex":
                if (event instanceof EventUpdate) {
                    mineplex();
                }
                break;
            case "AAC4":
                if (event instanceof EventUpdate) {
                    aac4();
                }
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