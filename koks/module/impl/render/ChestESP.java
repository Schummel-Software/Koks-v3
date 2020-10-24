package koks.module.impl.render;

import koks.api.settings.Setting;
import koks.api.util.ESPUtil;
import koks.event.Event;
import koks.event.impl.EventRender3D;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author kroko
 * @created on 07.10.2020 : 15:46
 */

@ModuleInfo(name = "ChestESP", description = "Marks all chests in the world.", category = Module.Category.RENDER)
public class ChestESP extends Module {

    public final ESPUtil espUtil = new ESPUtil();

    public Setting espMode = new Setting("ESP Mode", new String[]{"2D Style", "Box", "Shader"}, "Box", this);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(espMode.getCurrentMode());
        }
        if (event instanceof EventRender3D) {
            float partialTicks = ((EventRender3D) event).getPartialTicks();
            for (TileEntity e : mc.theWorld.loadedTileEntityList) {
                if (e instanceof TileEntityChest || e instanceof TileEntityEnderChest) {
                    mc.theWorld.getBlockState(e.getPos()).getBlock();
                    double x = (e.getPos().getX() - mc.getRenderManager().renderPosX);
                    double y = (e.getPos().getY() - mc.getRenderManager().renderPosY);
                    double z = (e.getPos().getZ() - mc.getRenderManager().renderPosZ);
                    if (espMode.getCurrentMode().equals("2D Style")) {
                        espUtil.drawCorners(x + 0.5, y + 0.5, z + 0.5, 16, 16, 8, 3);
                    }
                    if (espMode.getCurrentMode().equals("Box")) {
                        AxisAlignedBB axisAlignedBB = e.getBlockType().getSelectedBoundingBox(mc.theWorld, e.getPos()).offset(-mc.getRenderManager().renderPosX, -mc.getRenderManager().renderPosY, -mc.getRenderManager().renderPosZ);
                        espUtil.renderBox(axisAlignedBB);
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
