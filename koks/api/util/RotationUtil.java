package koks.api.util;

import koks.Koks;
import koks.module.impl.combat.KillAura;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 17:14
 */
public class RotationUtil {

    public final Minecraft mc = Minecraft.getMinecraft();
    public final RandomUtil randomUtil = new RandomUtil();

    public Vec3 getBestVector(Entity entity, float accuracy, float precision) {
        try {
            Vec3 playerVector = mc.thePlayer.getPositionEyes(1.0F);
            Vec3 nearestVector = new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

            float height = entity.height;
            float width = entity.width * accuracy;

            for (float y = 0; y < height; y += precision) {
                for (float x = -width; x < width; x += precision) {
                    for (float z = -width; z < width; z += precision) {
                        Vec3 currentVector = new Vec3(entity.posX + x * width, entity.posY + (entity.getEyeHeight() / height) * y, entity.posZ + z * width);

                        if (playerVector.distanceTo(currentVector) < playerVector.distanceTo(nearestVector))
                            nearestVector = currentVector;
                    }
                }
            }
            return nearestVector;
        } catch (Exception e) {
            return entity.getPositionVector();
        }
    }

    public float[] faceEntity(Entity entity, float currentYaw, float currentPitch, boolean smooth, float accuracy, float precision, float predictionMultiplier) {
        Vec3 rotations = getBestVector(entity, accuracy, precision);

        double x = rotations.xCoord - mc.thePlayer.posX;
        double y = rotations.yCoord - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double z = rotations.zCoord - mc.thePlayer.posZ;

        double xDiff = (entity.posX - entity.prevPosX) * predictionMultiplier;
        double zDiff = (entity.posZ - entity.prevPosZ) * predictionMultiplier;

        double distance = mc.thePlayer.getDistanceToEntity(entity);

        if (distance < 0.05)
            return new float[]{currentYaw, currentPitch};

        double angle = MathHelper.sqrt_double(x * x + z * z);
        float yawAngle = (float) (MathHelper.func_181159_b(z + zDiff, x + xDiff) * 180.0D / Math.PI) - 90.0F;
        float pitchAngle = (float) (-(MathHelper.func_181159_b(y, angle) * 180.0D / Math.PI));
        float finalPitch = pitchAngle >= 90 ? 90 : pitchAngle;
        //lmao thx
        float f = mc.gameSettings.mouseSensitivity * 0.8F + 0.2F;
        float f1 = f * f * f * 1.5F;

        float f2 = (float) ((yawAngle - currentYaw) * f1);
        float f3 = (float) ((finalPitch - currentPitch) * f1);

        float difYaw = yawAngle - currentYaw;
        float difPitch = finalPitch - currentPitch;

        float yaw = updateRotation(currentYaw + f2, yawAngle, smooth ? Math.abs(MathHelper.wrapAngleTo180_float(difYaw)) * 0.1F : 360);
        float pitch = updateRotation(currentPitch + f3, finalPitch, smooth ? Math.abs(MathHelper.wrapAngleTo180_float(difPitch)) * 0.1F : 360);

        yaw -= yaw % f1;
        pitch -= pitch % f1;

        return new float[]{yaw, pitch};
    }

    public float[] faceBlock(BlockPos pos, float currentYaw, float currentPitch, float speed) {
        double x = (pos.getX() + 0.5F) - mc.thePlayer.posX;
        double y = (pos.getY() - 3.0F) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = (pos.getZ() + 0.5F) - mc.thePlayer.posZ;

        double calculate = MathHelper.sqrt_double(x * x + z * z);
        float calcYaw = (float) (MathHelper.func_181159_b(z, x) * 180.0D / Math.PI) - 90.0F;
        float calcPitch = (float) -(MathHelper.func_181159_b(y, calculate) * 180.0D / Math.PI);
        float finalPitch = calcPitch >= 90 ? 90 : calcPitch;

        float f = mc.gameSettings.mouseSensitivity * 0.8F + 0.2F;
        float f1 = f * f * f * 1.5F;

        float f2 = (float) ((calcYaw - currentYaw) * f1);
        float f3 = (float) ((finalPitch - currentPitch) * f1);

        float yaw = updateRotation(currentYaw + f2, calcYaw, speed);
        float pitch = updateRotation(currentPitch + f3, finalPitch, speed);

        yaw -= yaw % f1;
        pitch -= pitch % f1;

        return new float[]{yaw, pitch};
    }

    public float[] calculateDiff(float v1, float v2) {
        float y = Math.abs(v1 - v2);
        if (y < 0) y += 360;
        if (y > 360) y -= 360;
        float y1 = 360 - y;
        float oneoranother = 0;
        if (y > y1) oneoranother++;
        if (y > y1) y = y1;
        return new float[]{y, oneoranother};
    }

    public float[] fixedSensitivity(float sensitivity, float yawdiff, float pitchdiff) {
        float f = sensitivity * 0.6F + 0.2F;
        float gcd = f * f * f * 8f;
        yawdiff = (int) ((yawdiff) / gcd / 0.15f);
        pitchdiff = (int) ((pitchdiff) / gcd / 0.15f);
        yawdiff = yawdiff * gcd * 0.15f;
        pitchdiff = pitchdiff * gcd * 0.15f;
        return new float[]{yawdiff, pitchdiff};
    }

    public float updateRotation(float curRot, float destination, float speed)
    {
        float f = MathHelper.wrapAngleTo180_float(destination - curRot);

        if (f > speed)
        {
            f = speed;
        }

        if (f < -speed)
        {
            f = -speed;
        }

        return curRot + f;
    }

}