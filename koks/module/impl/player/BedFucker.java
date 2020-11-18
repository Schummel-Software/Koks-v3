package koks.module.impl.player;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventJump;
import koks.event.impl.EventMotion;
import koks.event.impl.EventMoveFlying;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * @author kroko
 * @created on 15.11.2020 : 00:35
 */

@ModuleInfo(name = "BedFucker", category = Module.Category.PLAYER, description = "You break automatically the block")
public class BedFucker extends Module {


    public BlockPos curPos;
    public float curYaw, curPitch;

    public Setting range = new Setting("Range", 10, 5, 30, true, this);
    public Setting delay = new Setting("Delay", 10, 0, 300, true, this);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion) {
            if (curPos != null) {
                if (((EventMotion) event).getType() == EventMotion.Type.PRE) {
                    float[] rots = rotationUtil.faceBlock(curPos, 0.0F, curYaw, curPitch, 360);
                    ((EventMotion) event).setYaw(rots[0]);
                    ((EventMotion) event).setPitch(rots[1]);
                    curYaw = rots[0];
                    curPitch = rots[1];
                }
            }
        }

        if(event instanceof EventMoveFlying) {
            ((EventMoveFlying) event).setYaw(curYaw);
        }

        if(event instanceof EventJump) {
            ((EventJump) event).setYaw(curYaw);
        }

        if (event instanceof EventUpdate) {
            int range = (int) this.range.getCurrentValue();
            if (curPos == null) {
                timeHelper.reset();
                for (int x = -range; x < range; x++)
                    for (int y = -range; y < range; y++)
                        for (int z = -range; z < range; z++) {
                            BlockPos pos = getPosition().add(x, y, z);
                            Block block = getWorld().getBlockState(pos).getBlock();
                            if (block instanceof BlockBed) {
                                curPos = pos;
                            }
                        }
            } else {
                getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, curPos, EnumFacing.DOWN));
                if (timeHelper.hasReached((long) delay.getCurrentValue())) {
                    getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, curPos, EnumFacing.DOWN));
                    curPos = null;
                    timeHelper.reset();
                }
            }
        }
    }

    @Override
    public void onEnable() {
        timeHelper.reset();
        curPos = null;
        curYaw = getPlayer().rotationYaw;
        curPitch = getPlayer().rotationPitch;
    }

    @Override
    public void onDisable() {

    }
}
