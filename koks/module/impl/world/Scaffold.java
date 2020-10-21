package koks.module.impl.world;

import koks.Koks;
import koks.api.settings.Setting;
import koks.api.util.RandomUtil;
import koks.api.util.RayCastUtil;
import koks.api.util.RotationUtil;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventMotion;
import koks.event.impl.EventSafeWalk;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author avox | lmao | kroko
 * @created on 04.09.2020 : 10:23
 */

@ModuleInfo(name = "Scaffold", description = "Its place blocks under you", category = Module.Category.WORLD)
public class Scaffold extends Module {

    private final List blackList;

    public BlockPos finalPos;
    public boolean shouldBuildDown;

    private final RotationUtil rotationUtil = new RotationUtil();
    private final TimeHelper timeUtil = new TimeHelper();
    private final RandomUtil randomutil = new RandomUtil();
    private final RayCastUtil rayCastUtil = new RayCastUtil();

    public Setting delay = new Setting("Delay", 0, 0, 100, true, this);
    public Setting motion = new Setting("Motion", 1, 0, 5, false, this);

    public Setting pitchVal = new Setting("Pitch", 82, 70, 90, true, this);

    public Setting sneak = new Setting("Sneak", false, this);
    public Setting sneakAfterBlocks = new Setting("Sneak After...", 10, 0, 20, true, this);

    public Setting swingItem = new Setting("Swing Item", true, this);
    public Setting safeWalk = new Setting("SafeWalk", true, this);
    public Setting onGround = new Setting("OnGround", true, this);

    public Setting downScaffold = new Setting("DownScaffold", false, this);

    public Setting randomHit = new Setting("Random Hit", false, this);
    public Setting sprint = new Setting("Sprint", false, this);

    public Setting rayCast = new Setting("RayCast", true, this);

    public Setting simpleRotations = new Setting("Simple Rotations", true, this);
    public Setting intave = new Setting("Intave", false, this);

    public Setting alwaysLook = new Setting("AlwaysLook", true, this);

    public float pitch, yaw;
    public int sneakCount;

    public Scaffold() {
        this.blackList = Arrays.asList(Blocks.crafting_table, Blocks.chest, Blocks.enchanting_table, Blocks.anvil, Blocks.sand, Blocks.gravel, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.ice, Blocks.packed_ice, Blocks.cobblestone_wall, Blocks.water, Blocks.lava, Blocks.web, Blocks.sapling, Blocks.rail, Blocks.golden_rail, Blocks.activator_rail, Blocks.detector_rail, Blocks.tnt, Blocks.red_flower, Blocks.yellow_flower, Blocks.flower_pot, Blocks.tallgrass, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.ladder, Blocks.torch, Blocks.stone_button, Blocks.wooden_button, Blocks.redstone_torch, Blocks.redstone_wire, Blocks.furnace, Blocks.cactus, Blocks.oak_fence, Blocks.acacia_fence, Blocks.nether_brick_fence, Blocks.birch_fence, Blocks.dark_oak_fence, Blocks.jungle_fence, Blocks.oak_fence, Blocks.acacia_fence_gate, Blocks.snow_layer, Blocks.trapdoor, Blocks.ender_chest, Blocks.beacon, Blocks.hopper, Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.carpet);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventMotion) {
            if (((EventMotion) event).getType() == EventMotion.Type.PRE) {

                ((EventMotion) event).setYaw(yaw);
                ((EventMotion) event).setPitch(pitch);

            }
        }


        if (event instanceof EventUpdate) {
            if (downScaffold.isToggled()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    shouldBuildDown = true;
                    mc.gameSettings.keyBindSneak.pressed = false;
                } else {
                    shouldBuildDown = false;
                }
            }
            BlockPos pos = new BlockPos(mc.thePlayer.posX, (mc.thePlayer.getEntityBoundingBox()).minY - 1.0D - (shouldBuildDown ? 1 : 0), mc.thePlayer.posZ);
            getPlayer().setSprinting(sprint.isToggled());
            if (sprint.isToggled()) {
                getPlayer().sendQueue.addToSendQueue(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.STOP_SPRINTING));
            }

            getBlockPosToPlaceOn(pos);

            pitch = getPitch(360);

            if (simpleRotations.isToggled()) {
                setYawSimple();
            } else {
                setYaw();
            }

        }
        if (event instanceof EventSafeWalk) {
            if (downScaffold.isToggled()) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    shouldBuildDown = true;
                    mc.gameSettings.keyBindSneak.pressed = false;
                } else {
                    shouldBuildDown = false;
                }
            }
            if (safeWalk.isToggled()) {
                ((EventSafeWalk) event).setSafe(true);
            }
        }
    }


    public void setYaw() {
        float[] rotations = rotationUtil.faceBlock(finalPos, yaw, pitch, 360);
        yaw = rotations[0] + 15;
    }


    public void setYawSimple() {
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

        this.yaw = mc.thePlayer.rotationYaw + yaw;
    }

    public float getPitch(int speed) {
        if (mc.thePlayer.onGround) {
            return pitchVal.getCurrentValue();
        } else {
            return rotationUtil.faceBlock(finalPos, yaw, pitch, speed)[1];
        }
    }

    public void placeBlock(BlockPos pos, EnumFacing face) {
        finalPos = pos;
        ItemStack silentItemStack = null;
        if (mc.thePlayer.getCurrentEquippedItem() == null || (!(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof net.minecraft.item.ItemBlock))) {
            for (int i = 0; i < 9; i++) {
                if (mc.thePlayer.inventory.getStackInSlot(i) != null && mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemBlock) {
                    ItemBlock itemBlock = (ItemBlock) mc.thePlayer.inventory.getStackInSlot(i).getItem();
                    if (this.blackList.contains(itemBlock.getBlock()))
                        continue;
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
                    silentItemStack = mc.thePlayer.inventory.getStackInSlot(i);
                    if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D - (shouldBuildDown ? 1 : 0), mc.thePlayer.posZ)).getBlock() instanceof net.minecraft.block.BlockAir) {
                        if (swingItem.isToggled())
                            mc.thePlayer.swingItem();
                        else
                            mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                    }
                    break;
                }
            }
        } else {
            silentItemStack = (mc.thePlayer.getCurrentEquippedItem().getItem() instanceof net.minecraft.item.ItemBlock) ? mc.thePlayer.getCurrentEquippedItem() : null;
            if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D - (shouldBuildDown ? 1 : 0), mc.thePlayer.posZ)).getBlock() instanceof net.minecraft.block.BlockAir) {
                if (blackList.contains(((ItemBlock) silentItemStack.getItem()).getBlock()))
                    return;
                mc.thePlayer.swingItem();
            }
        }

        if (sneakCount >= sneakAfterBlocks.getCurrentValue() && sneak.isToggled())
            mc.gameSettings.keyBindSneak.pressed = true;

        if (mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D - (shouldBuildDown ? 1 : 0), mc.thePlayer.posZ)).getBlock() instanceof net.minecraft.block.BlockAir) {
            if (!simpleRotations.isToggled())
                setYaw();
            boolean rayCasted = !rayCast.isToggled() || rayCastUtil.isRayCastBlock(pos, rayCastUtil.rayCastedBlock(yaw, pitch, silentItemStack, intave.isToggled()));
            if (rayCasted) {
                if (timeUtil.hasReached(mc.thePlayer.onGround ? (randomutil.getRandomLong((long) delay.getCurrentValue(), (long) delay.getCurrentValue() + 1)) : 20L)) {
                    if (blackList.contains(((ItemBlock) silentItemStack.getItem()).getBlock()))
                        return;

                    MovingObjectPosition ray = rayCastUtil.rayCastedBlock(yaw, pitch, silentItemStack, intave.isToggled());
                    if (intave.isToggled())
                        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, silentItemStack, ray.getBlockPos(), ray.sideHit, ray.hitVec);
                    else
                        mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, silentItemStack, pos, face, new Vec3(pos.getX() + (this.randomHit.isToggled() ? randomutil.getRandomDouble(0, 0.7) : 0), pos.getY() + (this.randomHit.isToggled() ? randomutil.getRandomDouble(0, 0.7) : 0), pos.getZ() + (this.randomHit.isToggled() ? randomutil.getRandomDouble(0, 0.7) : 0)));
                    sneakCount++;

                    mc.thePlayer.motionX *= motion.getCurrentValue();
                    mc.thePlayer.motionZ *= motion.getCurrentValue();

                    if (sneakCount > sneakAfterBlocks.getCurrentValue())
                        sneakCount = 0;

                    timeUtil.reset();
                }
            } else if (intave.isToggled() && !rayCast.isToggled()) {
                mc.rightClickMouse();
            } else {

                if(sprint.isToggled())
                    getPlayer().sendQueue.addToSendQueue(new C0BPacketEntityAction(getPlayer(), C0BPacketEntityAction.Action.START_SPRINTING));

                timeUtil.reset();
            }
        } else {
            mc.gameSettings.keyBindSneak.pressed = false;
            timeUtil.reset();
            if (!simpleRotations.isToggled())
                setYaw();
        }
        shouldBuildDown = false;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        mc.gameSettings.keyBindSneak.pressed = false;
        sneakCount = 0;
        yaw = 0;
        pitch = 0;
        mc.timer.timerSpeed = 1F;
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
    }

    public void getBlockPosToPlaceOn(BlockPos pos) {
        BlockPos blockPos1 = pos.add(-1, 0, 0);
        BlockPos blockPos2 = pos.add(1, 0, 0);
        BlockPos blockPos3 = pos.add(0, 0, -1);
        BlockPos blockPos4 = pos.add(0, 0, 1);
        float down = (shouldBuildDown ? 1 : 0);
        if (mc.theWorld.getBlockState(pos.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(pos.add(0, -1, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(pos.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(pos.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(pos.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(pos.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(pos.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(pos.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(pos.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(pos.add(0, 0 - down, 1), EnumFacing.NORTH);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(0, -1 - down, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(blockPos1.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(blockPos1.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(blockPos1.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(blockPos1.add(0, 0 - down, 1), EnumFacing.NORTH);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(0, -1 - down, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(blockPos2.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(blockPos2.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(blockPos2.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(blockPos2.add(0, 0 - down, 1), EnumFacing.NORTH);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(0, -1 - down, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(blockPos3.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(blockPos3.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(blockPos3.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(blockPos3.add(0, 0 - down, 1), EnumFacing.NORTH);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, -1 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(0, -1 - down, 0), EnumFacing.UP);
        } else if (mc.theWorld.getBlockState(blockPos4.add(-1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(-1, 0 - down, 0), EnumFacing.EAST);
        } else if (mc.theWorld.getBlockState(blockPos4.add(1, 0 - down, 0)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(1, 0 - down, 0), EnumFacing.WEST);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, 0 - down, -1)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(0, 0 - down, -1), EnumFacing.SOUTH);
        } else if (mc.theWorld.getBlockState(blockPos4.add(0, 0 - down, 1)).getBlock() != Blocks.air) {
            placeBlock(blockPos4.add(0, 0 - down, 1), EnumFacing.NORTH);
        }
    }

}
