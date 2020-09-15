package koks.api.util;

import koks.Koks;
import koks.module.impl.combat.KillAura;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 17:14
 */
public class RotationUtil {

    public final Minecraft mc = Minecraft.getMinecraft();
    public final RandomUtil randomUtil = new RandomUtil();

    public Vec3 getBestVector(Entity entity) {
        Vec3 playerVector = mc.thePlayer.getPositionEyes(1.0F);
        Vec3 nearestVector = new Vec3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

        float height = entity.height;
        float width = entity.width * 0.6F;

        for (float y = 0; y < height; y += 0.1F) {
            for (float x = -width; x < width; x += 0.1F) {
                for (float z = -width; z < width; z += 0.1F) {
                    Vec3 currentVector = new Vec3(entity.posX + width * x, entity.posY + (entity.getEyeHeight() / height) * y, entity.posZ + width * z);

                    if (mc.thePlayer.getEntityWorld().rayTraceBlocks(playerVector, currentVector) != null) continue;

                    if (playerVector.distanceTo(currentVector) < playerVector.distanceTo(nearestVector))
                        nearestVector = currentVector;
                }
            }
        }

        return nearestVector;
    }

    public float[] faceEntity(Entity entity, float currentYaw, float currentPitch, boolean smooth) {
        Vec3 rotations = getBestVector(entity);
        double x = rotations.xCoord - mc.thePlayer.posX;
        double y = rotations.yCoord - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
        double z = rotations.zCoord - mc.thePlayer.posZ;

        KillAura killAura = (KillAura) Koks.getKoks().moduleManager.getModule(KillAura.class);
        int centerWidth = (int) killAura.centerWidth.getCurrentValue();
        float prediction = killAura.prediction.getCurrentValue();

        double xDiff = (entity.posX - entity.prevPosX) * prediction;
        double zDiff = (entity.posZ - entity.prevPosZ) * prediction;

        double distance = mc.thePlayer.getDistanceToEntity(entity);

        if (distance < 0.05)
            return new float[]{currentYaw, currentPitch};

        double angle = MathHelper.sqrt_double(x * x + z * z);
        float yawAngle = (float) ((float) (MathHelper.func_181159_b(z + zDiff, x + xDiff) * 180.0D / Math.PI) - 90.0F + randomUtil.getRandomGaussian(centerWidth / distance));
        float pitchAngle = (float) ((float) (-(MathHelper.func_181159_b(y, angle) * 180.0D / Math.PI)) + randomUtil.getRandomGaussian(centerWidth / distance));

        float speed = (float) (40 + (distance / 2) + randomUtil.getRandomFloat(-10, 10));
        float yaw = updateRotation(currentYaw, yawAngle, smooth ? speed : 360);
        float pitch = updateRotation(currentPitch, pitchAngle, smooth ? speed : 360);

        float[] yawDiff = calculateDiff(currentYaw, yaw);
        float[] pitchDiff = calculateDiff(currentPitch, pitch);
        float[] fixed = fixedSensitivity(mc.gameSettings.mouseSensitivity, yawDiff[0], pitchDiff[0]);
        yawDiff[0] = fixed[0];
        pitchDiff[0] = fixed[1];
        if (yawDiff[1] == 1) {
            if (yaw > currentYaw) currentYaw -= yawDiff[0];
            else if (yaw < currentYaw) currentYaw += yawDiff[0];
        } else {
            if (yaw > currentYaw) currentYaw += yawDiff[0];
            else if (yaw < currentYaw) currentYaw -= yawDiff[0];
        }
        if (pitchDiff[1] == 1) {
            if (pitch > currentPitch) currentPitch -= pitchDiff[0];
            else if (pitch < currentPitch) currentPitch += pitchDiff[0];
        } else {
            if (pitch > currentPitch) currentPitch += pitchDiff[0];
            else if (pitch < currentPitch) currentPitch -= pitchDiff[0];
        }

        return new float[]{currentYaw, currentPitch};
    }

    public float[] faceBlock(BlockPos pos, float currentYaw, float currentPitch, float speed) {
        double x = (pos.getX() + 0.5F) - mc.thePlayer.posX;
        double y = (pos.getY() - 3.0F) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        double z = (pos.getZ() + 0.5F) - mc.thePlayer.posZ;

        double calculate = MathHelper.sqrt_double(x * x + z * z);
        float calcYaw = (float) (MathHelper.func_181159_b(z, x) * 180.0D / Math.PI) - 90.0F;
        float calcPitch = (float) -(MathHelper.func_181159_b(y, calculate) * 180.0D / Math.PI);
        float finalPitch = calcPitch >= 90 ? 90 : calcPitch;
        float yaw = updateRotation(currentYaw, calcYaw, speed);
        float pitch = updateRotation(currentPitch, finalPitch, speed);

        float[] yawDiff = calculateDiff(currentYaw, yaw);
        float[] pitchDiff = calculateDiff(currentPitch, pitch);
        float[] fixed = fixedSensitivity(mc.gameSettings.mouseSensitivity, yawDiff[0], pitchDiff[0]);
        yawDiff[0] = fixed[0];
        pitchDiff[0] = fixed[1];
        if (yawDiff[1] == 1) {
            if (yaw > currentYaw) currentYaw -= yawDiff[0];
            else if (yaw < currentYaw) currentYaw += yawDiff[0];
        } else {
            if (yaw > currentYaw) currentYaw += yawDiff[0];
            else if (yaw < currentYaw) currentYaw -= yawDiff[0];
        }
        if (pitchDiff[1] == 1) {
            if (pitch > currentPitch) currentPitch -= pitchDiff[0];
            else if (pitch < currentPitch) currentPitch += pitchDiff[0];
        } else {
            if (pitch > currentPitch) currentPitch += pitchDiff[0];
            else if (pitch < currentPitch) currentPitch -= pitchDiff[0];
        }

        return new float[]{currentYaw, currentPitch};
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

    public float updateRotation(float current, float intended, float speed) {
        float f = MathHelper.wrapAngleTo180_float(intended - current);
        if (f > speed)
            f = speed;
        if (f < -speed)
            f = -speed;
        return current + f;
    }

}