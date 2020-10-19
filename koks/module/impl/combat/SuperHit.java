package koks.module.impl.combat;

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
                    MovingObjectPosition ray = rayCastUtil.rayCast(reach.getCurrentValue());
                    if (ray == null) return;
                    Entity entity = mc.objectMouseOver.entityHit;

                    BlockPos oldPos = getPlayer().getPosition();
                    BlockPos newPos = entity.getPosition();

                    double distance = getPlayer().getDistance(newPos.getX(), newPos.getY(), newPos.getZ());
                    for (double d = 0; d < distance; d += 3) {
                        setPosition(oldPos.getX() + (newPos.getX() - mc.thePlayer.getHorizontalFacing().getFrontOffsetX() - oldPos.getX()) * d / distance, oldPos.getY() + (newPos.getY() - mc.thePlayer.getHorizontalFacing().getFrontOffsetY() - oldPos.getY()) * d / distance, oldPos.getZ() + (newPos.getZ() - mc.thePlayer.getHorizontalFacing().getFrontOffsetZ() - oldPos.getZ()) * d / distance, mc.thePlayer.onGround);
                    }

                    setPosition(newPos.getX(), newPos.getY(), newPos.getZ(), mc.thePlayer.onGround);

                    getPlayerController().attackEntity(getPlayer(), entity);

                    double distanceToOld = getPlayer().getDistance(oldPos.getX(), oldPos.getY(), oldPos.getZ());
                    for (double d = 0; d < distance; d += 3) {
                        setPosition(newPos.getX() + (oldPos.getX() - mc.thePlayer.getHorizontalFacing().getFrontOffsetX() - newPos.getX()) * d / distance, newPos.getY() + (oldPos.getY() - mc.thePlayer.getHorizontalFacing().getFrontOffsetY() - newPos.getY()) * d / distance, newPos.getZ() + (oldPos.getZ() - mc.thePlayer.getHorizontalFacing().getFrontOffsetZ() - newPos.getZ()) * d / distance, mc.thePlayer.onGround);
                    }

                    setPosition(oldPos.getX(), oldPos.getY() + 0.5, oldPos.getZ(), mc.thePlayer.onGround);
                    setPosition(oldPos.getX(), oldPos.getY(), oldPos.getZ(), mc.thePlayer.onGround);
                    getGameSettings().keyBindAttack.pressed = false;
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
