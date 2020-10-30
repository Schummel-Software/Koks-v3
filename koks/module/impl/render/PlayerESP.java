package koks.module.impl.render;

import koks.api.settings.Setting;
import koks.event.Event;
import koks.event.impl.EventOutline;
import koks.event.impl.EventRender3D;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author kroko
 * @created on 07.10.2020 : 15:47
 */

@ModuleInfo(name = "PlayerESP", description = "Its show all Players in the world", category = Module.Category.RENDER)
public class PlayerESP extends Module {

    public Setting espMode = new Setting("ESP Mode", new String[]{"2D Style", "Box", "Shader"}, "Shader", this);

    @Override
    public void onEvent(Event event) {

        if(event instanceof EventOutline) {
            ((EventOutline) event).setOutline(espMode.getCurrentMode().equalsIgnoreCase("Glow"));
        }

        if (event instanceof EventUpdate) {
            setInfo(espMode.getCurrentMode());
        }
        if (event instanceof EventRender3D) {
            float partialTicks = ((EventRender3D) event).getPartialTicks();
            for (Entity entity : mc.theWorld.loadedEntityList) {
                if (!entity.isInvisible() && entity != mc.thePlayer && entity instanceof EntityPlayer) {
                    double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks) - mc.getRenderManager().renderPosX;
                    double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks) - mc.getRenderManager().renderPosY;
                    double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks) - mc.getRenderManager().renderPosZ;
                    if (espMode.getCurrentMode().equals("2D Style")) {
                        espUtil.drawCorners(x, y + 0.9, z, 20, 40, 10, 3);
                    }

                    if (espMode.getCurrentMode().equals("Box")) {
                        float width = 0.16F;

                        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox();
                        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(
                                axisalignedbb.minX - entity.posX + x - width,
                                axisalignedbb.minY - entity.posY + y + 0.01,
                                axisalignedbb.minZ - entity.posZ + z - width,
                                axisalignedbb.maxX - entity.posX + x + width,
                                axisalignedbb.maxY - entity.posY + y + 0.19 - (entity.isSneaking() ? 0.25 : 0),
                                axisalignedbb.maxZ - entity.posZ + z + width);

                        float width2 = 0.10F;

                        AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(
                                axisalignedbb.minX - entity.posX + x - width2,
                                axisalignedbb.minY - entity.posY + y + 0.01,
                                axisalignedbb.minZ - entity.posZ + z - width2,
                                axisalignedbb.maxX - entity.posX + x + width2,
                                axisalignedbb.maxY - entity.posY + y + 0.19 - (entity.isSneaking() ? 0.25 : 0),
                                axisalignedbb.maxZ - entity.posZ + z + width2);

                        espUtil.renderBox(axisalignedbb1);
                        if (((EntityLivingBase) entity).hurtTime != 0) {
                            espUtil.renderBox(axisalignedbb2);
                        }
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
