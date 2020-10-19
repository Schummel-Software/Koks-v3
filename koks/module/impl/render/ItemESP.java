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
 * @created on 07.10.2020 : 15:47
 */

@ModuleInfo(name = "ItemESP", description = "Its show all Items in the world", category = Module.Category.RENDER)
public class ItemESP extends Module {

    public final ESPUtil espUtil = new ESPUtil();

    public Setting espMode = new Setting("ESP Mode", new String[]{"2D Style", "Box"}, "Box", this);

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(espMode.getCurrentMode());
        }
        if (event instanceof EventRender3D) {
            float partialTicks = ((EventRender3D) event).getPartialTicks();
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (entity instanceof EntityItem) {
                    EntityItem e = (EntityItem) entity;
                    double x = (e.lastTickPosX + (e.posX - e.lastTickPosX) * ((EventRender3D) event).getPartialTicks()) - mc.getRenderManager().renderPosX;
                    double y = (e.lastTickPosY + (e.posY - e.lastTickPosY) * ((EventRender3D) event).getPartialTicks()) - mc.getRenderManager().renderPosY;
                    double z = (e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * ((EventRender3D) event).getPartialTicks()) - mc.getRenderManager().renderPosZ;
                    if (espMode.getCurrentMode().equals("2D Style")) {
                        espUtil.drawCorners(x, y + 0.30, z, 5, 5, 1, 3);
                    }

                    if (espMode.getCurrentMode().equals("Box")) {
                        double size = 0.25;

                        AxisAlignedBB axisalignedbb = new AxisAlignedBB(
                                x - size,
                                y,
                                z - size,
                                x + size,
                                y + size * 2,
                                z + size);

                        espUtil.renderBox(axisalignedbb);
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