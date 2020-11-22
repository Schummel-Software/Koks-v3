package koks.module.impl.combat;

import koks.Koks;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.*;
import koks.module.Module;
import koks.api.settings.Setting;
import koks.module.ModuleInfo;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
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

@ModuleInfo(name = "KillAura", description = "Its damage the entitys arround you", category = Module.Category.COMBAT)
public class KillAura extends Module {

    float hasenRange;
    long lastAttack;

    // TARGET SETTINGS
    public Setting attackType = new Setting("Attack Type", new String[]{"Single", "Switch", "Hybrid"}, "Single", this);
    public Setting preferType = new Setting("Prefer", new String[]{"Health", "Distance"}, "Distance", this);
    public Setting player = new Setting("Player", true, this);
    public Setting armorStands = new Setting("ArmorStands", false, this);
    public Setting animals = new Setting("Animals", false, this);
    public Setting villager = new Setting("Villager", false, this);
    public Setting mobs = new Setting("Mobs", false, this);

    // BASIC ATTACK SETTINGS
    public Setting hazeRange = new Setting("Haze Range", false, this);
    public Setting maxHazeRangeDistance = new Setting("Max Haze Range Distance", 1.0F, 0.0F, 3.0F, false, this);
    public Setting hitRange = new Setting("Hit Range", 3.0F, 3.0F, 6.0F, false, this);
    public Setting cps = new Setting("CPS", 5.0F, 5.0F, 20.0F, true, this);
    public Setting hurtTime = new Setting("HurtTime", 10.0F, 0.0F, 10.0F, true, this);
    public Setting fov = new Setting("Field of View", 360.0F, 10.0F, 360.0F, true, this);
    public Setting failChance = new Setting("Failing Chance", 5.0F, 0.0F, 20.0F, true, this);

    // AUTO BLOCK SETTINGS
    public Setting autoBlock = new Setting("AutoBlock", false, this);
    public Setting blockMode = new Setting("BlockMode", new String[]{"On Attack", "Half", "Full"}, "On Attack", this);

    // MOVEMENT SETTINGS
    public Setting mouseFix = new Setting("MouseFix", true, this);
    public Setting fixMovement = new Setting("Fix Movement", true, this);
    public Setting stopSprinting = new Setting("Stop Sprinting", true, this);

    public Setting attackMode = new Setting("Attack Mode", new String[]{"PlayerController", "MouseClick", "Packet"}, "PlayerController", this);

    // CUSTOM SETTINGS
    public Setting noSwing = new Setting("NoSwing", false, this);
    public Setting noSwingType = new Setting("NoSwingType", new String[]{"Vanilla", "Packet", "ServerSide"}, "Packet", this);
    public Setting crackSize = new Setting("Crack Size", 5.0F, 1.0F, 10.0F, true, this);

    // ANTI BOT SETTINGS
    public Setting healthNaNCheck = new Setting("Health NaN Check", false, this);
    public Setting groundCheck = new Setting("GroundCheck", false, this);
    public Setting nameCheck = new Setting("Name Check", true, this);
    public Setting ignoreInvisible = new Setting("Ignore Invisible", true, this);
    public Setting ignoreDeath = new Setting("IgnoreDeath", true, this);
    public Setting throughWalls = new Setting("Through Walls", false, this);
    public Setting soundCheck = new Setting("Sound Check", false, this);

    //TODO: HitBefore, TablistCheck

    public ArrayList<Entity> entities = new ArrayList<>();
    public Entity finalEntity;

    public Rotations rotations;

    public int switchCounter;
    public float yaw, pitch, curYaw, curPitch;
    public boolean failing;

    public ArrayList<Entity> madeSound = new ArrayList();

    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            /* Koks.getKoks().moduleManager.getModule(Scaffold.class).setToggled(false);*/

            setInfo(entities.size() + "");
            failing = new Random().nextInt(100) < failChance.getCurrentValue();
            if (stopSprinting.isToggled() && finalEntity != null) {
                mc.gameSettings.keyBindSprint.pressed = false;
                mc.thePlayer.setSprinting(false);
            }

            manageEntities();
        }

        if (event instanceof EventPacket) {
            Packet packet = ((EventPacket) event).getPacket();
            if(packet instanceof S29PacketSoundEffect) {
                S29PacketSoundEffect soundEffect = (S29PacketSoundEffect) packet;
                for(Entity entity : getWorld().loadedEntityList) {
                    if(entity != getPlayer() && entity.getDistance(soundEffect.getX(), soundEffect.getY(), soundEffect.getZ()) <= 1) {
                        madeSound.add(entity);
                    }
                }
            }
        }

        if (event instanceof EventMotion) {
            if (((EventMotion) event).getType() == EventMotion.Type.PRE) {
                if (finalEntity != null) {
                    float[] rotations = rotationUtil.faceEntity(finalEntity, mouseFix.isToggled(), curYaw, curPitch, this.rotations.smooth.isToggled(), this.rotations.accuracy.getCurrentValue(), this.rotations.precision.getCurrentValue(), this.rotations.predictionMultiplier.getCurrentValue());
                    yaw = rotations[0];
                    pitch = rotations[1];

                    if (this.rotations.lockView.isToggled()) {
                        mc.thePlayer.rotationYaw = yaw;
                        mc.thePlayer.rotationPitch = pitch;
                    } else {
                        ((EventMotion) event).setYaw(yaw);
                        ((EventMotion) event).setPitch(pitch);
                    }

                    curPitch = pitch;
                    curYaw = yaw;
                }
            }
        }

        if (event instanceof EventAttack) {

            if (finalEntity != null) {

                if (finalEntity.hurtResistantTime == 10 && hasenRange < maxHazeRangeDistance.getCurrentValue() && hazeRange.isToggled()) {
                    hasenRange += 0.5F;
                    lastAttack = System.currentTimeMillis() / 1000;
                }

                if (!hazeRange.isToggled())
                    hasenRange = 0;

                if (System.currentTimeMillis() / 1000 - lastAttack >= 2) {
                    hasenRange = 0;
                }

                if (canBlock() && autoBlock.isToggled() && blockMode.getCurrentMode().equalsIgnoreCase("Full"))
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());


                long cps = (long) this.cps.getCurrentValue() + randomUtil.getRandomInt(-1, 1);

                cps = cps < 10 ? cps : cps + 5;
                if (((EntityLivingBase) finalEntity).hurtTime <= hurtTime.getCurrentValue()) {
                    if (timeHelper.hasReached((1000L / cps) + (long) randomUtil.getRandomGaussian(20))) {
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
            } else {
                hasenRange = 0;
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
        Entity rayCastEntity = rayCastUtil.rayCastedEntity(hitRange.getCurrentValue() + hasenRange, yaw, pitch);

        if (!failing && (rayCastEntity != null || throughWalls.isToggled() && !getPlayer().canEntityBeSeen(finalEntity))) {


            for (int i = 0; i < crackSize.getCurrentValue(); i++)
                mc.effectRenderer.emitParticleAtEntity(finalEntity, EnumParticleTypes.CRIT);

            if (canBlock() && autoBlock.isToggled() && blockMode.getCurrentMode().equals("Full"))
                mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));

            /*if (mc.currentScreen != null)
                getPlayer().closeScreen();*/


            Entity attackEntity = !getPlayer().canEntityBeSeen(finalEntity) && throughWalls.isToggled() ? finalEntity : rayCastEntity;

            switch (attackMode.getCurrentMode()) {
                case "PlayerController":
                    mc.playerController.attackEntity(mc.thePlayer, attackEntity);
                    break;
                case "MouseClick":
                    mc.clickMouse();
                    break;
                case "Packet":
                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(attackEntity, C02PacketUseEntity.Action.ATTACK));
                    break;
            }

            if (switchCounter < entities.size())
                switchCounter++;
            else
                switchCounter = 0;

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
        }
    }

    boolean canBlock() {
        return getPlayer().getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    public void manageEntities() {
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
                if (entities.size() - 1 >= switchCounter)
                    if (entities.get(switchCounter).getDistanceToEntity(getPlayer()) <= hitRange.getCurrentValue() + hasenRange)
                        finalEntity = entities.get(switchCounter);
                    else
                        entities.remove(entities.get(switchCounter));
                else
                    switchCounter = 0;
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

        boolean healthcheck = true;
        int maxhealth = 22;

        if (entity == null)
            return false;
        if (!(entity instanceof EntityLivingBase))
            return false;
        if (!armorStands.isToggled() && entity instanceof EntityArmorStand)
            return false;
        if (!villager.isToggled() && entity instanceof EntityVillager)
            return false;
        if (!mobs.isToggled() && entity instanceof EntityMob)
            return false;
        if (!animals.isToggled() && (entity instanceof EntityAnimal))
            return false;
        if (!player.isToggled() && entity instanceof EntityPlayer)
            return false;
        if (entity.getDistanceToEntity(mc.thePlayer) > hitRange.getCurrentValue() + hasenRange + 1.0D)
            return false;
        if (entity == mc.thePlayer)
            return false;
        if (entity.isDead)
            return false;
        EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
        if (healthNaNCheck.isToggled() && !Float.isNaN(entityLivingBase.getHealth()))
            return false;
        if (groundCheck.isToggled() && entity.onGround && getWorld().getBlockState(entity.getPosition().add(0, -0.05, 0)).getBlock() == Blocks.air)
            return false;
        if(soundCheck.isToggled() && !madeSound.contains(entity))
            return false;
        if (nameCheck.isToggled() && entity instanceof EntityPlayer && !checkedName(entity))
            return false;
        if (ignoreInvisible.isToggled() && entity.isInvisible())
            return false;
        if (!throughWalls.isToggled() && !mc.thePlayer.canEntityBeSeen(entity))
            return false;
        if (((EntityLivingBase) entity).deathTime != 0 && ignoreDeath.isToggled())
            return false;
        if (((EntityLivingBase) entity).isOnSameTeam(getPlayer()) && Koks.getKoks().moduleManager.getModule(Teams.class).isToggled())
            return false;
        if (Koks.getKoks().friendManager.isFriend(entity.getName()) && Koks.getKoks().moduleManager.getModule(Friends.class).isToggled())
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
        hasenRange = 0;
        rotations = (Rotations) Koks.getKoks().moduleManager.getModule(Rotations.class);
        finalEntity = null;
        curYaw = getPlayer().rotationYaw;
        curPitch = getPlayer().rotationPitch;
        entities.clear();
    }

    @Override
    public void onDisable() {

    }

}