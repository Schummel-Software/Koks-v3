package koks.module.impl.combat;

import koks.api.util.*;
import koks.event.Event;
import koks.event.impl.EventJump;
import koks.event.impl.EventMotion;
import koks.event.impl.EventMoveFlying;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.api.settings.Setting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author deleteboys | lmao | kroko ist der beste coder
 * @created on 13.09.2020 : 11:56
 */
public class KillAura extends Module {

    // TARGET SETTINGS
    public Setting attackType = new Setting("Attack Type", new String[]{"Single", "Switch", "Hybrid"}, "Single", this);
    public Setting preferType = new Setting("Prefer", new String[]{"Health", "Distance"}, "Distance", this);
    public Setting player = new Setting("Player", true, this);
    public Setting animals = new Setting("Animals", false, this);
    public Setting mobs = new Setting("Mobs", false, this);

    // BASIC ATTACK SETTINGS
    public Setting hitRange = new Setting("Hit Range", 3.4F, 3.0F, 6.0F, false, this);
    public Setting preAim = new Setting("Pre Aim", true, this);
    public Setting extendedAimRange = new Setting("Aim Range", 0.1F, 0.0F, 1.0F, false, this);
    public Setting cps = new Setting("CPS", 7.0F, 5.0F, 20.0F, true, this);
    public Setting hurtTime = new Setting("HurtTime", 10.0F, 0.0F, 10.0F, true, this);
    public Setting fov = new Setting("Field of View", 360.0F, 10.0F, 360.0F, true, this);
    public Setting failChance = new Setting("Failing Chance", 7.0F, 0.0F, 20.0F, true, this);

    // AUTO BLOCK SETTINGS
    public Setting autoBlock = new Setting("AutoBlock", true, this);
    public Setting blockMode = new Setting("BlockMode", new String[]{"On Attack", "Half", "Full"}, "On Attack", this);

    // SPECIFY ROTATION SETTINGS
    public Setting smoothRotations = new Setting("Smooth Rotations", false, this);
    public Setting lockView = new Setting("Lock View", false, this);
    public Setting centerWidth = new Setting("Center Width", 2.0F, 1.0F, 10.0F, true, this);
    public Setting prediction = new Setting("Prediction", 0.5F, 0.0F, 0.75F, false, this);

    // MOVEMENT SETTINGS
    public Setting fixMovement = new Setting("Fix Movement", true, this);
    public Setting stopSprinting = new Setting("Stop Sprinting", true, this);
    public Setting usePlayerController = new Setting("Use PlayerController", true, this);

    // CUSTOM SETTINGS
    public Setting noSwing = new Setting("NoSwing", false, this);
    public Setting noSwingType = new Setting("NoSwingType", new String[]{"Vanilla", "Packet", "ServerSide"}, "Packet", this);
    public Setting crackSize = new Setting("Crack Size", 2.0F, 1.0F, 10.0F, true, this);

    // ANTI BOT SETTINGS
    public Setting healthNaNCheck = new Setting("Health NaN Check", false, this);
    public Setting nameCheck = new Setting("Name Check", true, this);
    public Setting ignoreInvisible = new Setting("Ignore Invisible", true, this);
    public Setting throughWalls = new Setting("Through Walls", false, this);
    public Setting soundCheck = new Setting("Sound Check", true, this);

    public ArrayList<Entity> entities = new ArrayList<>();
    public Entity finalEntity;

    public final RotationUtil rotationUtil = new RotationUtil();
    public final RayCastUtil rayCastUtil = new RayCastUtil();
    public final RandomUtil randomUtil = new RandomUtil();
    public final TimeHelper timeHelper = new TimeHelper();

    public int switchCounter;
    public float yaw, pitch;
    public boolean failing;

    public KillAura() {
        super("KillAura", "Is Legit", Category.COMBAT);

        // TARGET SETTINGS
        registerSetting(attackType);
        registerSetting(preferType);
        registerSetting(player);
        registerSetting(animals);
        registerSetting(mobs);

        // BASIC ATTACK SETTINGS
        registerSetting(hitRange);
        registerSetting(preAim);
        registerSetting(extendedAimRange);
        registerSetting(cps);
        registerSetting(hurtTime);
        registerSetting(fov);
        registerSetting(failChance);

        // AUTO BLOCK SETTINGS
        registerSetting(autoBlock);
        registerSetting(blockMode);

        // SPECIFY ROTATION SETTINGS
        registerSetting(smoothRotations);
        registerSetting(lockView);
        registerSetting(centerWidth);
        registerSetting(prediction);

        // MOVEMENT SETTINGS
        registerSetting(fixMovement);
        registerSetting(stopSprinting);
        registerSetting(usePlayerController);

        // CUSTOM SETTINGS
        registerSetting(noSwing);
        registerSetting(noSwingType);
        registerSetting(crackSize);

        // ANTI BOT SETTINGS
        registerSetting(healthNaNCheck);
        registerSetting(nameCheck);
        registerSetting(ignoreInvisible);
        registerSetting(throughWalls);
        registerSetting(soundCheck);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            setInfo(entities.size() + "");
            failing = new Random().nextInt(100) < failChance.getCurrentValue();

            if (stopSprinting.isToggled() && finalEntity != null) {
                mc.gameSettings.keyBindSprint.pressed = false;
                mc.thePlayer.setSprinting(false);
            }

            manageEntities();
        }

        if (event instanceof EventMotion) {
            if (((EventMotion) event).getType() == EventMotion.Type.PRE) {
                if (finalEntity != null) {
                    float[] rotations = rotationUtil.faceEntity(finalEntity, yaw, pitch, smoothRotations.isToggled());
                    yaw = rotationUtil.updateRotation(mc.thePlayer.rotationYaw, rotations[0], fov.getCurrentValue());
                    pitch = rotations[1];
                    if (lockView.isToggled()) {
                        mc.thePlayer.rotationYaw = yaw;
                        mc.thePlayer.rotationPitch = pitch;
                    } else {
                        ((EventMotion) event).setYaw(yaw);
                        ((EventMotion) event).setPitch(pitch);
                    }

                    if (canBlock() && autoBlock.isToggled() && blockMode.getCurrentMode().equals("Full"))
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());

                    long cps = (long) this.cps.getCurrentValue();
                    cps = cps < 10 ? cps : cps + 5;
                    if (((EntityLivingBase) finalEntity).hurtTime <= hurtTime.getCurrentValue()) {
                        if (timeHelper.hasReached(1000L / cps + (long) randomUtil.getRandomGaussian(20))) {
                            attackEntity();
                            if (canBlock() && autoBlock.isToggled() && (blockMode.getCurrentMode().equals("On Attack") || blockMode.getCurrentMode().equals("Half")))
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                            timeHelper.reset();
                        } else {
                            if (canBlock() && autoBlock.isToggled() && blockMode.getCurrentMode().equals("Half"))
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                        }
                    } else {
                        timeHelper.reset();
                        if (canBlock() && autoBlock.isToggled() && blockMode.getCurrentMode().equals("Half"))
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
                    }
                }
            }

            if (((EventMotion) event).getType() == EventMotion.Type.POST) {

            }
        }

        if (fixMovement.isToggled() && finalEntity != null) {
            if (event instanceof EventJump) {
                ((EventJump) event).setYaw(yaw);
            }
            if (event instanceof EventMoveFlying) {
                ((EventMoveFlying) event).setYaw(yaw);
            }
        }
    }

    public void attackEntity() {
        Entity rayCastEntity = rayCastUtil.rayCastedEntity(hitRange.getCurrentValue(), yaw, pitch);

        if (!failing && rayCastEntity != null) {
            for (int i = 0; i < crackSize.getCurrentValue(); i++)
                mc.effectRenderer.emitParticleAtEntity(finalEntity, EnumParticleTypes.CRIT);

            if (canBlock() && autoBlock.isToggled() && blockMode.getCurrentMode().equals("Full"))
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));

            if (usePlayerController.isToggled()) {
                mc.playerController.attackEntity(mc.thePlayer, rayCastEntity);
            } else {
                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(rayCastEntity, C02PacketUseEntity.Action.ATTACK));
            }

            if (noSwing.isToggled()) {
                switch (noSwingType.getCurrentMode()) {
                    case "Vanilla":
                        break;
                    case "Packet":
                        mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                        break;
                    case "ServerSide":
                        mc.thePlayer.sendQueue.addToSendQueue(new S0BPacketAnimation());
                        break;
                }
            } else {
                mc.thePlayer.swingItem();
            }

            if (switchCounter < entities.size())
                switchCounter++;
            else
                switchCounter = 0;
        }
    }

    boolean canBlock() {
        return mc.thePlayer.getCurrentEquippedItem().getItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    public void manageEntities() {
        if (switchCounter > entities.size())
            switchCounter = 0;

        entities.removeIf(entity -> !isValid(entity));

        if (finalEntity != null && !isValid(finalEntity))
            finalEntity = null;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (isValid(entity) && !entities.contains(entity)) {
                entities.add(entity);
            }
        }

        Entity entityToSet = preferType.getCurrentMode().equals("Distance") ? getNearest() : getLowest();

        switch (attackType.getCurrentMode()) {
            case "Single":
                if (finalEntity == null) finalEntity = entityToSet;
                break;
            case "Switch":
                finalEntity = entities.get(switchCounter);
                break;
            case "Hybrid":
                finalEntity = entityToSet;
                break;
            default:
                finalEntity = entities.get(0);
                break;

        }
    }

    public Entity getNearest() {
        Entity nearest = null;

        for (Entity entity : entities) {
            if (nearest == null) {
                nearest = entity;
            } else {
                if (mc.thePlayer.getDistanceToEntity(entity) < mc.thePlayer.getDistanceToEntity(nearest))
                    nearest = entity;
            }
        }

        return nearest;

    }

    public Entity getLowest() {
        Entity lowest = null;

        for (Entity entity : entities) {
            EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
            if (entityLivingBase.getHealth() < 0 || entityLivingBase.getHealth() > 24 || Float.isNaN(entityLivingBase.getHealth())) {
                lowest = getNearest();
            } else {
                if (lowest == null) {
                    lowest = entity;
                } else {
                    if (entityLivingBase.getHealth() < ((EntityLivingBase) lowest).getHealth())
                        lowest = entity;
                }
            }
        }

        return lowest;
    }

    public boolean isValid(Entity entity) {
        if (entity == null)
            return false;
        if (!(entity instanceof EntityLivingBase))
            return false;
        if (!mobs.isToggled() && entity instanceof EntityMob)
            return false;
        if (!animals.isToggled() && (entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityArmorStand))
            return false;
        if (entity.getDistanceToEntity(mc.thePlayer) > hitRange.getCurrentValue() + (preAim.isToggled() ? extendedAimRange.getCurrentValue() : 0))
            return false;
        if (entity == mc.thePlayer)
            return false;
        if (entity.isDead)
            return false;
        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
        if (healthNaNCheck.isToggled() && !Float.isNaN(entityLivingBase.getHealth()))
            return false;
        if (nameCheck.isToggled() && entity instanceof EntityPlayer && !checkedName(entity))
            return false;
        if (ignoreInvisible.isToggled() && entity.isInvisible())
            return false;
        if (!throughWalls.isToggled() && !mc.thePlayer.canEntityBeSeen(entity))
            return false;
        return true;
    }

    public boolean checkedName(Entity entity) {
        if (!validEntityName(entity))
                return false;
        return true;
    }

    @Override
    public void onEnable() {
        finalEntity = null;
        entities.clear();
    }

    @Override
    public void onDisable() {

    }

}