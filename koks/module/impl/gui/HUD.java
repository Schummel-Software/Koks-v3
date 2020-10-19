package koks.module.impl.gui;

import koks.Koks;
import koks.api.util.fonts.GlyphPage;
import koks.api.util.fonts.GlyphPageFontRenderer;
import koks.event.Event;
import koks.event.impl.EventKeyPress;
import koks.event.impl.EventRender2D;
import koks.event.impl.EventTick;
import koks.module.Module;
import koks.api.settings.Setting;
import koks.module.ModuleInfo;
import koks.module.impl.combat.KillAura;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Comparator;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 10:20
 */

@ModuleInfo(name = "HUD", description = "Draws the HUD", category = Module.Category.GUI)
public class HUD extends Module {

    public final FontRenderer fr = mc.fontRendererObj;
    public Setting clientName = new Setting("Name", Koks.getKoks().NAME, this);
    public Setting substring = new Setting("Substring", 2, 2, 4, true, this);
    public Setting waterMark = new Setting("Watermark", true, this);
    public Setting waterMarkFont = new Setting("Font", "NONE", this);
    public Setting arrayList = new Setting("ArrayList", true, this);
    public Setting showTags = new Setting("Show Tags", true, this);
    public Setting tabGUI = new Setting("TabGUI", true, this);
    public Setting x = new Setting("X", 5, 0, 20, true, this);
    public Setting y = new Setting("Y", 30, 20, 50, true, this);
    public Setting width = new Setting("Width", 91, 80, 100, true, this);
    public Setting height = new Setting("height", 13, 10, 20, true, this);

    public HUD() {
        setToggled(true);
    }

    @Override
    public void onEvent(Event event) {

        if(event instanceof EventTick) {
            
            if(clientName.getTyped().length() < substring.getCurrentValue())
                clientName.setTyped(Koks.getKoks().NAME.substring(0, (int) substring.getCurrentValue()));

            if (!clientName.getTyped().substring(0, (int) (substring.getCurrentValue())).startsWith(Koks.getKoks().NAME.substring(0, (int) (substring.getCurrentValue()))))
                clientName.setTyped(Koks.getKoks().NAME.substring(0, (int) substring.getCurrentValue()) + clientName.getTyped().substring((int) substring.getCurrentValue()));

        }

        if (event instanceof EventRender2D) {
            if (tabGUI.isToggled())
                Koks.getKoks().tabGUI.drawScreen((int) x.getCurrentValue(), (int) y.getCurrentValue(), (int) width.getCurrentValue(), (int) height.getCurrentValue());
            if (waterMark.isToggled())
                drawWaterMark();
            if (arrayList.isToggled())
                drawArrayList();
            KillAura killAura = (KillAura) Koks.getKoks().moduleManager.getModule(KillAura.class);
            killAura.preferType.setVisible(!killAura.attackType.getCurrentMode().equals("Switch"));
            killAura.noSwingType.setVisible(killAura.noSwing.isToggled());
            killAura.blockMode.setVisible(killAura.autoBlock.isToggled());
        }

        if (event instanceof EventKeyPress) {
            if (tabGUI.isToggled())
                Koks.getKoks().tabGUI.manageKeys((EventKeyPress) event);
        }
    }

    public GlyphPageFontRenderer waterFont;

    public void drawWaterMark() {
        GL11.glPushMatrix();
        double scale = 2.5;
        GL11.glScaled(scale, scale, scale);

        if (waterMarkFont.getTyped().equalsIgnoreCase("NONE")) {
            fr.drawStringWithShadow(clientName.getTyped(), 3, 3, Koks.getKoks().clientColor.getRGB());
        } else {
            waterFont = GlyphPageFontRenderer.create(waterMarkFont.getTyped(), 50, true, true, true);
            console.log(waterFont.getFontHeight());
            waterFont.drawString(Koks.getKoks().NAME, 3, 3, Koks.getKoks().clientColor.getRGB(), true);
        }
        GL11.glPopMatrix();

        float x = waterMarkFont.getTyped().equalsIgnoreCase("NONE") ? (float) (fr.getStringWidth(clientName.getTyped()) * scale) : waterFont.getStringWidth(clientName.getTyped());

        ScaledResolution sr = new ScaledResolution(mc);
        fr.drawStringWithShadow("v" + Koks.getKoks().VERSION, x + 10, 18, Color.LIGHT_GRAY.getRGB());
    }

    public void drawArrayList() {
        ScaledResolution sr = new ScaledResolution(mc);
        int[] offset = {fr.FONT_HEIGHT + 2};
        int[] y = {0};
        Koks.getKoks().moduleManager.getModules().stream().sorted(Comparator.comparingDouble(module -> -fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))).forEach(module -> {
            if (module.isToggled()) {
                if (module.getAnimation() < fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))
                    module.setAnimation(module.getAnimation() + 0.5);
                if (module.getAnimation() > fr.getStringWidth(module.getArrayName("§7", showTags.isToggled())))
                    module.setAnimation(module.getAnimation() - 0.5);

                Gui.drawRect((int) (sr.getScaledWidth() - module.getAnimation() - 5), y[0], sr.getScaledWidth(), y[0] + offset[0], 0xBB000000);
                Gui.drawRect((int) (sr.getScaledWidth() - module.getAnimation() - 7), y[0], (int) (sr.getScaledWidth() - module.getAnimation() - 5), y[0] + offset[0], Koks.getKoks().clientColor.getRGB());
                fr.drawStringWithShadow(module.getArrayName("§7", showTags.isToggled()), (float) (sr.getScaledWidth() - module.getAnimation() - 2), y[0] + 1.5F, Koks.getKoks().clientColor.getRGB());
                y[0] += offset[0];
            }
        });
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

}