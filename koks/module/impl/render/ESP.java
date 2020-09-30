package koks.module.impl.render;

import koks.api.util.ESPUtil;
import koks.event.Event;
import koks.event.impl.EventRender3D;
import koks.module.Module;
import koks.api.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.AxisAlignedBB;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 16:58
 */
public class ESP extends Module {

    public final ESPUtil espUtil = new ESPUtil();
    public Setting espMode = new Setting("ESP Mode", new String[]{"2D Style", "Box"}, "Box", this);
    public Setting player = new Setting("Player", true, this);
    public Setting chests = new Setting("Chests", true, this);
    public Setting items = new Setting("Items", true, this);


    public ESP() {
        super("ESP", "Shows the all Chests, Items and Players in the world", Category.RENDER);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventRender3D) {
            float partialTicks = ((EventRender3D) event).getPartialTicks();

            if (player.isToggled()) {
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

            if (items.isToggled()) {
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

            if (chests.isToggled()) {
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
                            AxisAlignedBB axisalignedbb = new AxisAlignedBB(
                                    x,
                                    y,
                                    z,
                                    x + 1,
                                    y + 1,
                                    z + 1);

                            espUtil.renderBox(axisalignedbb);
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