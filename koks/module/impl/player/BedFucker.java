package koks.module.impl.player;

import koks.api.settings.Setting;
import koks.event.Event;
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

    public Setting range = new Setting("Range", 10, 5, 30, true,this);

    @Override
    public void onEvent(Event event) {
        if(event instanceof EventUpdate) {
            int range = (int) this.range.getCurrentValue();
            for(int x = -range; x < range; x++)
                for(int y = -range; y < range; y++)
                    for(int z = -range; z < range; z++) {
                        BlockPos pos = getPosition().add(x, y, z);
                        Block block = getWorld().getBlockState(pos).getBlock();
                        if (block instanceof BlockBed) {
                            getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.DOWN));
                            getPlayer().sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.DOWN));
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
