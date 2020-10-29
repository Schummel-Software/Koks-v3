package koks.api.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import koks.Koks;
import koks.event.impl.EventMouseOver;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import optifine.Reflector;
import org.lwjgl.Sys;

import java.util.List;
import java.util.Random;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 20:18
 */

public class RayCastUtil {

    private final Minecraft mc = Minecraft.getMinecraft();
    private Entity pointedEntity;

    public Entity rayCastedEntity(double range, float yaw, float pitch) {
        Entity entity = this.mc.getRenderViewEntity();

        if (entity != null && this.mc.theWorld != null) {
            this.mc.mcProfiler.startSection("pick");
            this.mc.pointedEntity = null;
            double d0 = (double) this.mc.playerController.getBlockReachDistance();
            this.mc.objectMouseOver = entity.rayTrace(d0, 1F);

            Vec3 vec3 = entity.getPositionEyes(1F);
            boolean flag = false;
            boolean flag1 = true;

            d0 = range;
            double d1 = d0;

            if (this.mc.objectMouseOver != null) {
                d1 = this.mc.objectMouseOver.hitVec.distanceTo(vec3);
            }

            float yawCos = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
            float yawSin = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
            float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
            float pitchSin = MathHelper.sin(-pitch * 0.017453292F);

            Vec3 vec31 = new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);

            this.pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0F;
            List list = this.mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, Entity::canBeCollidedWith));
            double d2 = d1;

            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity) list.get(i);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double) f1, (double) f1, (double) f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        this.pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        boolean flag2 = false;

                        if (Reflector.ForgeEntity_canRiderInteract.exists()) {
                            flag2 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }

                        if (entity1 == entity.ridingEntity && !flag2) {
                            if (d2 == 0.0D) {
                                this.pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        } else {
                            this.pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (this.pointedEntity != null && flag && vec3.distanceTo(vec33) > 3.0D) {
                this.pointedEntity = null;
                this.mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, (EnumFacing) null, new BlockPos(vec33));
            }

            if (this.pointedEntity != null && (d2 < d1 || this.mc.objectMouseOver == null)) {
                this.mc.objectMouseOver = new MovingObjectPosition(this.pointedEntity, vec33);

                if (this.pointedEntity instanceof EntityLivingBase || this.pointedEntity instanceof EntityItemFrame) {
                    this.mc.pointedEntity = this.pointedEntity;
                }
            }

            this.mc.mcProfiler.endSection();
        }
        return pointedEntity;
    }

    public Vec3 getLook(float yaw, float pitch) {
        return Entity.getVectorForRotation(pitch, yaw);
    }

    public boolean isRayCastBlock(BlockPos bp, MovingObjectPosition ray) {
        return ray != null && ray.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && bp.equals(ray.getBlockPos());
    }

    public MovingObjectPosition rayCastedBlock(float yaw, float pitch, ItemStack silentItem, boolean intave) {
        float range = mc.playerController.getBlockReachDistance();

        Vec3 vec31 = getLook(yaw, pitch);

        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);


        MovingObjectPosition ray = mc.theWorld.rayTraceBlocks(vec3, vec32, false, false, false);
        ItemBlock itemblock = (ItemBlock) silentItem.getItem();

        if (ray != null && ray.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            return ray;
        return null;
    }
}