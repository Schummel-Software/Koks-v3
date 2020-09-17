package koks.module.impl.movement;

import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.api.settings.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:56
 */
public class NoCobweb extends Module {

    public Setting mode = new Setting("Mode", new String[] {"Intave"}, "Intave", this);

    public NoCobweb() {
        super("NoCobweb", "You doesn't affect by cobweb", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Intave":
                    BlockPos bPos = new BlockPos(mc.thePlayer.getPosition());
                    if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 0.1, mc.thePlayer.posZ)).getBlock() == Blocks.web) {
                        mc.thePlayer.motionY = 0.06;
                    }
                    break;
            }
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }
}
