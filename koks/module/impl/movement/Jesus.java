package koks.module.impl.movement;

import koks.event.Event;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.api.settings.Setting;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:38
 */
public class Jesus extends Module {

    public Setting mode = new Setting("Mode", new String[]{"Intave"}, "Intave", this);

    public Jesus() {
        super("Jesus", "You can walk on water", Category.MOVEMENT);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(mode.getCurrentMode());
            switch (mode.getCurrentMode()) {
                case "Intave":
                    BlockPos bPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

                    if (mc.theWorld.getBlockState(bPos).getBlock() == Blocks.water || mc.theWorld.getBlockState(bPos).getBlock() == Blocks.flowing_water || mc.theWorld.getBlockState(bPos).getBlock() == Blocks.lava || mc.theWorld.getBlockState(bPos).getBlock() == Blocks.flowing_lava) {
                        if (mc.thePlayer.isInWater()) {
                            mc.gameSettings.keyBindJump.pressed = false;
                            mc.thePlayer.motionY = 0.005;

                            mc.thePlayer.onGround = true;
                            mc.thePlayer.motionZ *= 0.9F;
                            mc.thePlayer.motionX *= 0.9F;
                            break;
                        } else {
                            if (!mc.thePlayer.isCollidedHorizontally)
                                mc.thePlayer.motionY = 0;
                            else
                                mc.thePlayer.motionY = 0.02;
                        }
                    }
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
