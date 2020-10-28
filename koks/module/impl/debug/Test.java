package koks.module.impl.debug;

import god.buddy.aot.BCompiler;
import koks.api.settings.Setting;
import koks.api.settings.SettingInfo;
import koks.api.util.TimeHelper;
import koks.event.Event;
import koks.event.impl.EventKeyPress;
import koks.event.impl.EventUpdate;
import koks.module.Module;
import koks.module.ModuleInfo;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldSettings;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author deleteboys | lmao | kroko
 * @created on 14.09.2020 : 11:56
 */

@ModuleInfo(name = "Test", description = "A Test Module", category = Module.Category.DEBUG)
public class Test extends Module {

    private final TimeHelper timeHelper = new TimeHelper();

    @SettingInfo(name = "testFloat", max = 100, min = 0, onlyInt = true)
    public float testFloat = 1;

    @BCompiler(aot = BCompiler.AOT.AGGRESSIVE)
    @Override
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            /* if(getPlayer().isEating()) {
                getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer(true));
            }*/

            getPlayer().noClip = true;


            getPlayer().sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getX(), getY(), getZ(), true));

            getPlayer().onGround = true;

            /*getPlayer().isInWeb = false;*/
        }
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
    }

}