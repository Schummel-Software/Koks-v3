package koks.module.impl.world;

import koks.api.settings.Setting;
import koks.api.util.RandomUtil;
import koks.api.util.RayCastUtil;
import koks.api.util.RotationUtil;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.*;
import koks.module.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public Setting delay = new Setting("Delay", 0F, 0F, 300F, true, this);
    public Setting motion = new Setting("Motion", 1F, 0F, 1F, false, this);

    public Setting silent = new Setting("Silent", true, this);

    public Setting rayCast = new Setting("Raycast", true, this);
    public Setting intave = new Setting("Intave", true, this);

    public Setting vectorFix = new Setting("VectorFix", false, this);

    public Setting pitchVal = new Setting("Pitch", 82F, 75F, 95F, true, this);

    public Setting safeWalk = new Setting("SafeWalk", true, this);
    public Setting onlyGround = new Setting("OnlyGround", true, this);

    public Setting sneak = new Setting("Sneak", false, this);
    public Setting jumpFix = new Setting("JumpFix", false, this);

    public Setting sprint = new Setting("Sprint", false, this);

    public Setting noSwing = new Setting("NoSwing", false, this);
    public Setting noSwingType = new Setting("NoSwing-Type", new String[]{"Vanilla", "Packet"}, "Packet", this);

    public List<Block> blackList;


    public Scaffold() {
        super("Scaffold", "Place blocks in your world", Category.WORLD);
        blackList = Arrays.asList(Blocks.crafting_table, Blocks.chest, Blocks.enchanting_table, Blocks.anvil, Blocks.sand, Blocks.gravel, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.ice, Blocks.packed_ice, Blocks.cobblestone_wall, Blocks.water, Blocks.lava, Blocks.web, Blocks.sapling, Blocks.rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.tnt, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.tallgrass, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.ladder, Blocks.torch, Blocks.stone_button, Blocks.wooden_button, Blocks.redstone_torch, Blocks.redstone_wire, Blocks.furnace, Blocks.cactus, Blocks.oak_fence, Blocks.acacia_fence, Blocks.nether_brick_fence, Blocks.birch_fence, Blocks.dark_oak_fence, Blocks.jungle_fence, Blocks.oak_fence, Blocks.acacia_fence_gate, Blocks.snow_layer, Blocks.trapdoor, Blocks.ender_chest, Blocks.beacon, Blocks.hopper, Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.carpet);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventSafeWalk) {
            if (safeWalk.isToggled())
                ((EventSafeWalk) event).setSafe(!onlyGround.isToggled() || getPlayer().onGround);
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
            if(getWorld().getBlockState(pos).getBlock() instanceof BlockChest)return;
            mc.gameSettings.keyBindSprint.pressed = sprint.isToggled();
            boolean isSneaking = false;

            mc.gameSettings.keyBindSneak.pressed = sneak.isToggled();

            mc.thePlayer.setSprinting(sprint.isToggled());
            if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ)).getBlock() instanceof BlockAir && (!rayCast.isToggled() || rayCastUtil.rayCastedBlock(this.yaw, this.pitch) != null)) {

                if (timeHelper.hasReached(randomUtil.getRandomLong((long) delay.getCurrentValue(), (long) delay.getCurrentValue() + 1))) {
                    if (noSwing.isToggled()) {
                        switch (noSwingType.getCurrentMode()) {
                            case "Vanilla":
                                break;
                            case "Packet":
                                getPlayer().sendQueue.addToSendQueue(new C0APacketAnimation());
                                break;
                        }
                    } else {
                        mc.thePlayer.swingItem();
                    }

                    ItemStack blockItem = null;
                    if (silent.isToggled() && (getPlayer().getCurrentEquippedItem() == null || !(getPlayer().getCurrentEquippedItem().getItem() instanceof ItemBlock))) {
                        for (int i = 0; i < 9; i++) {
                            if (getPlayer().inventory.getStackInSlot(i) != null && getPlayer().inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                                ItemBlock itemBlock = (ItemBlock) mc.thePlayer.inventory.getStackInSlot(i).getItem();
                                if (this.blackList.contains(itemBlock.getBlock()))
                                    continue;
                                getPlayer().sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
                                blockItem = mc.thePlayer.inventory.getStackInSlot(i);
                            }
                        }
                    } else {
                        blockItem = mc.thePlayer.getCurrentEquippedItem();
                    }

                    double vecFix = randomUtil.getRandomDouble(0.2, 0.8);
                    if (!vectorFix.isToggled()) vecFix = 0;

                    if (intave.isToggled()) {
                        MovingObjectPosition ray = rayCastUtil.rayCastedBlock(yaw, pitch);
                        mc.playerController.onPlayerRightClick(getPlayer(), mc.theWorld, blockItem, ray.getBlockPos(), ray.sideHit, ray.hitVec.addVector(vecFix, vecFix, vecFix));
                    } else {

                        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, blockItem, pos, face, new Vec3(vector.xCoord + vecFix, vector.yCoord + vecFix, vector.zCoord + vecFix));
                    }
                    mc.thePlayer.motionX *= motion.getCurrentValue();
                    mc.thePlayer.motionZ *= motion.getCurrentValue();
                    timeHelper.reset();
                }
            } else {
                timeHelper.reset();

                mc.gameSettings.keyBindSneak.pressed = false;

            }
        }

        if (event instanceof EventJump) {
            if (jumpFix.isToggled())
                ((EventJump) event).setYaw(yaw);
        }
    }

    public float[] getRotation() {
        float pitch = mc.thePlayer.onGround ? pitchVal.getCurrentValue() : 90;

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
        getPlayer().sendQueue.addToSendQueue(new C09PacketHeldItemChange(getPlayer().inventory.currentItem));
    }

}