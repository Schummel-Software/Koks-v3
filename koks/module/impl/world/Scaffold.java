package koks.module.impl.world;

import koks.api.util.RandomUtil;
import koks.api.util.RayCastUtil;
import koks.api.util.RotationUtil;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventJump;
import koks.event.impl.EventMotion;
import koks.event.impl.EventSafeWalk;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import net.minecraft.block.BlockAir;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.util.Random;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 21:18
 */
public class Scaffold extends Module {

    private final RotationUtil rotationUtil = new RotationUtil();
    private final RayCastUtil rayCastUtil = new RayCastUtil();
    private final RandomUtil randomUtil = new RandomUtil();
    private final TimeHelper timeHelper = new TimeHelper();
    public float yaw, pitch;
    public int count;

    public Scaffold() {
        super("Scaffold", "Place blocks in your world", Category.WORLD);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventSafeWalk) {
            ((EventSafeWalk) event).setSafe(true);
        }
        if (event instanceof EventMotion && ((EventMotion) event).getType() == EventMotion.Type.PRE) {
            float[] rotations = getRotation();
            this.yaw = rotations[0];
            this.pitch = rotations[1];
            ((EventMotion) event).setYaw(this.yaw);
            ((EventMotion) event).setPitch(this.pitch);
        }
        if (event instanceof EventUpdate) {
            BlockPos pos = rayCastUtil.rayCastedBlock(this.yaw, this.pitch).getBlockPos();
            EnumFacing face = rayCastUtil.rayCastedBlock(this.yaw, this.pitch).sideHit;
            Vec3 vector = rayCastUtil.rayCastedBlock(this.yaw, this.pitch).hitVec;
            mc.gameSettings.keyBindSprint.pressed = false;
            mc.thePlayer.setSprinting(false);
            //mc.gameSettings.keyBindSneak.pressed = true;
            if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && rayCastUtil.rayCastedBlock(this.yaw, this.pitch) != null) {
                if (timeHelper.hasReached(randomUtil.getRandomLong(1, 2))) {
                    mc.thePlayer.swingItem();
                    double vecFix = randomUtil.getRandomDouble(0.2, 0.8);
                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos, face, new Vec3(vector.xCoord + vecFix, vector.yCoord + vecFix, vector.zCoord + vecFix));
                    timeHelper.reset();
                }
            } else {
                timeHelper.reset();
                mc.gameSettings.keyBindSneak.pressed = false;
            }
        }

        if (event instanceof EventJump) {
            ((EventJump) event).setYaw(yaw);
        }
    }

    public float[] getRotation() {
        float pitch = mc.thePlayer.onGround ? 82 : 90;

        boolean forward = mc.gameSettings.keyBindForward.isKeyDown();
        boolean left = mc.gameSettings.keyBindLeft.isKeyDown();
        boolean right = mc.gameSettings.keyBindRight.isKeyDown();
        boolean back = mc.gameSettings.keyBindBack.isKeyDown();

        float yaw = 0;

        // Only one Key directions
        if (forward && !left && !right && !back)
            yaw = 180;
        if (!forward && left && !right && !back)
            yaw = 90;
        if (!forward && !left && right && !back)
            yaw = -90;
        if (!forward && !left && !right && back)
            yaw = 0;

        // Multi Key directions
        if (forward && left && !right && !back)
            yaw = 135;
        if (forward && !left && right && !back)
            yaw = -135;

        if (!forward && left && !right && back)
            yaw = 45;
        if (!forward && !left && right && back)
            yaw = -45;

        return new float[]{mc.thePlayer.rotationYaw + yaw, pitch};
    }

    @Override
    public void onEnable() {
        count = 0;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindSneak.pressed = false;
    }

}