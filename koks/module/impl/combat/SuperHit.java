package koks.module.impl.combat;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.api.util.RayCastUtil;
import koks.event.Event;
import koks.event.impl.EventBlockReach;
import koks.event.impl.EventMouseOver;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

/**
 * @author kroko
 * @created on 26.09.2020 : 13:35
 */

@ModuleInfo(name = "SuperHit", description = "You can hit players from a big distance", category = Module.Category.COMBAT)
public class SuperHit extends Module {

    public Setting reach = new Setting("Reach", 250F, 10F, 500F, true, this);

    private RayCastUtil rayCastUtil = new RayCastUtil();

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMouseOver) {
            ((EventMouseOver) event).setReach(reach.getCurrentValue());
            ((EventMouseOver) event).setDistanceCheck(false);
        }
        if (event instanceof EventBlockReach) {
            ((EventBlockReach) event).setReach(reach.getCurrentValue());
        }

        if (event instanceof EventUpdate) {
            if (getGameSettings().keyBindAttack.pressed) {
                if (mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer && validEntityName(mc.objectMouseOver.entityHit)) {
                    //RECODEN
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
