package koks.api.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.util.*;
import optifine.Reflector;

import java.util.List;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 20:18
 */
public class RayCastUtil {

    private final Minecraft mc = Minecraft.getMinecraft();
    private Entity pointedEntity;

    public Entity rayCastedEntity(double range, float yaw, float pitch) {
        Entity entity = mc.getRenderViewEntity();

        if (entity != null && mc.theWorld != null) {
            mc.pointedEntity = null;
            double d0 = range;
            MovingObjectPosition objectMouseOver = entity.rayTrace(d0, 1.0F);
            double d1 = range;
            Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
            boolean flag = false;

            if (mc.playerController.extendedReach()) {
                d0 = 6.0D;
                d1 = 6.0D;
            } else if (d0 > range) {
                flag = true;
            }

            if (objectMouseOver != null) {
                d1 = objectMouseOver.hitVec.distanceTo(vec3);
            }

            float yawCos = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
            float yawSin = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
            float pitchCos = -MathHelper.cos(-pitch * 0.017453292F);
            float pitchSin = MathHelper.sin(-pitch * 0.017453292F);

            Vec3 vec31 = new Vec3(yawSin * pitchCos, pitchSin, yawCos * pitchCos);
            Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
            pointedEntity = null;
            Vec3 vec33 = null;
            float f = 1.0F;
            List<Entity> list = mc.theWorld.getEntitiesInAABBexcluding(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double) f, (double) f, (double) f), Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
                public boolean apply(Entity p_apply_1_) {
                    return p_apply_1_.canBeCollidedWith();
                }
            }));
            double d2 = d1;

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = list.get(j);
                float f1 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f1, f1, f1);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                if (axisalignedbb.isVecInside(vec3)) {
                    if (d2 >= 0.0D) {
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                } else if (movingobjectposition != null) {
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                    if (d3 < d2 || d2 == 0.0D) {
                        boolean flag1 = false;

                        if (Reflector.ForgeEntity_canRiderInteract.exists())
                        {
                            flag1 = Reflector.callBoolean(entity1, Reflector.ForgeEntity_canRiderInteract, new Object[0]);
                        }

                        if (!flag1 && entity1 == entity.ridingEntity) {
                            if (d2 == 0.0D) {
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        } else {
                            pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }

            if (pointedEntity != null && flag && vec3.distanceTo(vec33) > range) {
                pointedEntity = null;
                mc.objectMouseOver = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec33, null, new BlockPos(vec33));
            }

            if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null)) {
                mc.objectMouseOver = new MovingObjectPosition(pointedEntity, vec33);

                if (pointedEntity instanceof EntityLivingBase || pointedEntity instanceof EntityItemFrame) {
                    mc.pointedEntity = pointedEntity;
                }
            }
        }
        return pointedEntity;
    }

    public MovingObjectPosition rayCastedBlock(float yaw, float pitch) {
        float range = mc.playerController.getBlockReachDistance();

        float cosYaw = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float sinYaw = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float cosPitch = -MathHelper.cos(-pitch * 0.017453292F);
        float sinPitch = MathHelper.sin(-pitch * 0.017453292F);

        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 vec31 = new Vec3(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);

        MovingObjectPosition ray = mc.theWorld.rayTraceBlocks(vec3, vec32, false);

        if (ray.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            return ray;

        return null;
    }

}