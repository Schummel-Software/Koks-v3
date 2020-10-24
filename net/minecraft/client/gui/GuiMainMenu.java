package net.minecraft.client.gui;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

import koks.Koks;
import koks.api.util.GLSLSandboxShader;
import koks.api.util.LoginUtil;
import koks.api.util.RenderUtil;
import koks.api.util.fonts.GlyphPageFontRenderer;
import koks.filemanager.impl.AlteningToken;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundCategory;
import net.minecraft.client.audio.SoundEventAccessorComposite;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.stream.GuiStreamOptions;
import net.minecraft.client.gui.stream.GuiStreamUnavailable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.stream.IStream;
import net.minecraft.util.*;
import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    private RenderUtil renderUtil = new RenderUtil();
    private GLSLSandboxShader shader;

    public ArrayList<File> backgrounds = new ArrayList<>();

    public String curBackground = "DEFAULT";

    public GuiMainMenu() {
        try {
            this.shader = new GLSLSandboxShader("/mainmenu.fsh");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load the Shader");
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */

    public boolean windowShowed = true;

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_SPACE) {
            Keyboard.enableRepeatEvents(false);
            windowShowed = !windowShowed;
        }
        if (windowShowed) {
            if (!email.isFocused() && !password.isFocused()) {
                if (keyCode <= Keyboard.KEY_0 && keyCode >= Keyboard.KEY_1) {
                    int key = Integer.parseInt(Keyboard.getKeyName(keyCode));
                    if (key <= indexSize + 1 && key != 0) {
                        currentIndex = key - 1;
                        System.out.println(currentIndex);
                        if (currentIndex == 5) {
                            mc.shutdown();
                        }
                    }
                }
            }

            if (currentIndex == 3) {
                Keyboard.enableRepeatEvents(true);
                if (email.isFocused()) {
                    email.textboxKeyTyped(typedChar, keyCode);
                    if (keyCode == Keyboard.KEY_RETURN) {
                        password.setFocused(true);
                        email.setFocused(false);
                    } else if (keyCode == Keyboard.KEY_DOWN) {
                        password.setFocused(true);
                        email.setFocused(false);
                    } else if (keyCode == Keyboard.KEY_ESCAPE) {
                        email.setFocused(false);
                    }
                } else if (password.isFocused()) {
                    password.textboxKeyTyped(typedChar, keyCode);
                    if (keyCode == Keyboard.KEY_RETURN) {
                        for (GuiButton button : buttonList) {
                            if (button.displayString.equalsIgnoreCase("Login"))
                                actionPerformed(button);
                        }
                    } else if (keyCode == Keyboard.KEY_UP) {
                        password.setFocused(false);
                        email.setFocused(true);
                    } else if (keyCode == Keyboard.KEY_ESCAPE) {
                        password.setFocused(false);
                    }
                } else {
                    if (keyCode == Keyboard.KEY_RETURN) {
                        for (GuiButton button : buttonList) {
                            if (button.displayString.equalsIgnoreCase("Login"))
                                actionPerformed(button);
                        }
                    }
                }
            }
        }
    }

    public int yOptions;

    private static final GameSettings.Options[] field_146440_f = new GameSettings.Options[]{GameSettings.Options.FOV};

    public void initGui() {
        File backgrounds = new File(mc.mcDataDir + "/Koks/Backgrounds");

        if (!backgrounds.exists())
            backgrounds.mkdirs();

        ScaledResolution sr = new ScaledResolution(mc);

        buttonList.add(new GuiButton(99, sr.getScaledWidth() / 2 - 50 / 2, sr.getScaledHeight() / 2 + 55, 50, 20, "Login"));

        yOptions = 3;

        int ix = 0;
        for (GameSettings.Options gamesettings$options : field_146440_f) {
            if (gamesettings$options.getEnumFloat()) {
                this.buttonList.add(new GuiOptionSlider(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + ix % 2 * 160, this.height / yOptions + 12 + 24 * (ix >> 1), gamesettings$options));
            } else {
                GuiOptionButton guioptionbutton = new GuiOptionButton(gamesettings$options.returnEnumOrdinal(), this.width / 2 - 155 + ix % 2 * 160, this.height / yOptions - 12 + 24 * (ix >> 1), gamesettings$options, mc.gameSettings.getKeyBinding(gamesettings$options));
                this.buttonList.add(guioptionbutton);
            }

            ++ix;
        }

        this.buttonList.add(new GuiButton(110, this.width / 2 - 155, this.height / yOptions + 48 - 6, 150, 20, I18n.format("options.skinCustomisation", new Object[0])));
        this.buttonList.add(new GuiButton(8675309, this.width / 2 + 5, this.height / yOptions + 48 - 6, 150, 20, "Super Secret Settings...") {
            public void playPressSound(SoundHandler soundHandlerIn) {
                SoundEventAccessorComposite soundeventaccessorcomposite = soundHandlerIn.getRandomSoundFromCategories(new SoundCategory[]{SoundCategory.ANIMALS, SoundCategory.BLOCKS, SoundCategory.MOBS, SoundCategory.PLAYERS, SoundCategory.WEATHER});

                if (soundeventaccessorcomposite != null) {
                    soundHandlerIn.playSound(PositionedSoundRecord.create(soundeventaccessorcomposite.getSoundEventLocation(), 0.5F));
                }
            }
        });

        this.buttonList.add(new GuiButton(106, this.width / 2 - 155, this.height / yOptions + 72 - 6, 150, 20, I18n.format("options.sounds", new Object[0])));
        this.buttonList.add(new GuiButton(107, this.width / 2 + 5, this.height / yOptions + 72 - 6, 150, 20, I18n.format("options.stream", new Object[0])));
        this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height / yOptions + 96 - 6, 150, 20, I18n.format("options.video", new Object[0])));
        this.buttonList.add(new GuiButton(100, this.width / 2 + 5, this.height / yOptions + 96 - 6, 150, 20, I18n.format("options.controls", new Object[0])));
        this.buttonList.add(new GuiButton(102, this.width / 2 - 155, this.height / yOptions + 120 - 6, 150, 20, I18n.format("options.language", new Object[0])));
        this.buttonList.add(new GuiButton(103, this.width / 2 + 5, this.height / yOptions + 120 - 6, 150, 20, I18n.format("options.chat.title", new Object[0])));
        this.buttonList.add(new GuiButton(105, this.width / 2 - 155, this.height / yOptions + 144 - 6, 150, 20, I18n.format("options.resourcepack", new Object[0])));
        this.buttonList.add(new GuiButton(104, this.width / 2 + 5, this.height / yOptions + 144 - 6, 150, 20, I18n.format("options.snooper.view", new Object[0])));
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height / yOptions + 168, I18n.format("gui.done", new Object[0])));

        email = new GuiTextField(187, mc.fontRendererObj, sr.getScaledWidth() / 2 - 180 / 2, sr.getScaledHeight() / 3 + 25, 180, 20);
        email.setFocused(false);

        password = new GuiTextField(1337, mc.fontRendererObj, sr.getScaledWidth() / 2 - 180 / 2, sr.getScaledHeight() / 3 + (25 + 27), 180, 20);
        password.setFocused(false);
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException {

        if (windowShowed) {
            switch (button.id) {
                case 109:
                    this.mc.displayGuiScreen(new GuiYesNo(this, (new ChatComponentTranslation("difficulty.lock.title", new Object[0])).getFormattedText(), (new ChatComponentTranslation("difficulty.lock.question", new Object[]{new ChatComponentTranslation(this.mc.theWorld.getWorldInfo().getDifficulty().getDifficultyResourceKey(), new Object[0])})).getFormattedText(), 109));
                    break;
                case 110:
                    this.mc.gameSettings.saveOptions();
                    this.mc.displayGuiScreen(new GuiCustomizeSkin(this));
                    break;
                case 8675309:
                    this.mc.entityRenderer.activateNextShader();
                    break;
                case 101:
                    this.mc.gameSettings.saveOptions();
                    this.mc.displayGuiScreen(new GuiVideoSettings(this, mc.gameSettings));
                    break;
                case 100:
                    this.mc.gameSettings.saveOptions();
                    this.mc.displayGuiScreen(new GuiControls(this, mc.gameSettings));
                    break;
                case 102:
                    this.mc.gameSettings.saveOptions();
                    this.mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, this.mc.getLanguageManager()));
                    break;
                case 103:
                    this.mc.gameSettings.saveOptions();
                    this.mc.displayGuiScreen(new ScreenChatOptions(this, mc.gameSettings));
                    break;
                case 104:
                    this.mc.gameSettings.saveOptions();
                    this.mc.displayGuiScreen(new GuiSnooper(this, mc.gameSettings));
                    break;
                case 200:
                    this.mc.gameSettings.saveOptions();
                    currentIndex = lastIndex;
                    break;
                case 105:
                    this.mc.gameSettings.saveOptions();
                    this.mc.displayGuiScreen(new GuiScreenResourcePacks(this));
                    break;
                case 106:
                    this.mc.gameSettings.saveOptions();
                    this.mc.displayGuiScreen(new GuiScreenOptionsSounds(this, mc.gameSettings));
                    break;
                case 107:
                    this.mc.gameSettings.saveOptions();
                    IStream istream = this.mc.getTwitchStream();

                    if (istream.func_152936_l() && istream.func_152928_D()) {
                        this.mc.displayGuiScreen(new GuiStreamOptions(this, mc.gameSettings));
                    } else {
                        GuiStreamUnavailable.func_152321_a(this);
                    }
                    break;
                case 99:
                    if (!Koks.getKoks().alteningApiKey.equals("") && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                        loginUtil.generate(Koks.getKoks().alteningApiKey);
                    } else {
                        if (password.getText().isEmpty() && email.getText().isEmpty()) {
                            try {
                                String clipboard = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                                String[] args = clipboard.split(":");
                                if (args.length == 1) {
                                    if (args[0].contains("@alt")) {
                                        loginUtil.login(clipboard);
                                    } else if (args[0].startsWith("api-")) {
                                        Koks.getKoks().alteningApiKey = clipboard;
                                        Koks.getKoks().fileManager.writeFile(AlteningToken.class);
                                        loginUtil.status = "§aUpdated Altening API Token";
                                    } else {
                                        if (Koks.getKoks().alteningApiKey != null) {
                                            loginUtil.generate(Koks.getKoks().alteningApiKey);
                                        }
                                    }
                                } else if (args.length == 2) {
                                    if (args[0].contains("@") && clipboard.contains(":")) {
                                        loginUtil.login(args[0], args[1]);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (!password.getText().isEmpty() && !email.getText().isEmpty()) {
                            loginUtil.login(email.getText(), password.getText());
                        } else if (!email.getText().isEmpty() && password.getText().isEmpty()) {
                            if (email.getText().startsWith("api-")) {
                                Koks.getKoks().alteningApiKey = email.getText();
                                Koks.getKoks().fileManager.writeFile(AlteningToken.class);
                                loginUtil.status = "§aUpdated Altening API Token";
                            } else if (email.getText().contains("@alt.com")) {
                                loginUtil.login(email.getText());
                            }

                        }
                    }
                    break;
            }
        }
    }

    public GuiTextField email, password;

    public int currentIndex = 0;
    public int size = 40;
    public int indexSize = 6;
    public int wheight = 120;
    public int wwidth = 200;
    public int dicke = 5;
    public int drawIndexSize = currentIndex;
    public int lastIndex = 0;

    public GlyphPageFontRenderer fontRenderer = GlyphPageFontRenderer.create("Arial", 120, true, true, true);

    public LoginUtil loginUtil = new LoginUtil();

    public String getRightName(int right, boolean edit) {
        switch (right) {
            case 0:
                return "New";
            case 1:
                return edit ? "Edit" : "Options";
            case 2:
                return "Delete";
        }
        return "";
    }

    public String getIndexName(int index) {
        switch (index) {
            case 0:
                return "Home";
            case 1:
                return "Singleplayer";
            case 2:
                return "Multiplayer";
            case 3:
                return "Alts";
            case 4:
                return "Proxys";
            case 5:
                return "Settings";
            case 6:
                return "Exit";
        }
        return "";
    }

    public String getOptionName(int option) {
        switch (option) {
            case 0:
                return "Client Color";
            case 1:
                return "Background";
            case 2:
                return "Open Folder";
            case 3:
                return "Reset Client";
        }
        return "";
    }

    public HashMap<Integer, Double> saveX = new HashMap<>();
    public HashMap<Integer, Double> saveY = new HashMap<>();

    public boolean drag = false, dragOptions = false, dragColor = false, dragBackground = false;
    public double x, y, dragX, dragY, dragOptionsX, dragOptionsY, optionsX, optionsY, colorX, colorY, dragColorX, dragColorY, backgroundX, backgroundY, dragBackgroundX, dragBackgroundY;
    public int scaleX;
    public int scaleY;


    public boolean showOptions, showColorPicker, showBackgrounds;

    public int rightX, rightY;
    public int rightWidth = 20, rightHeight = 30;
    public boolean showRight;
    public int rightOutline = 2;
    public int rightOptions = 2;

    public int optionSize = 4;

    public float hue = 1;
    int colorSize = 150;
    int pixel = 60;

    int backgroundWidth = 70;
    int backgroundHeight = 23;

    public void listFilesFromFolder(File folder, ArrayList<File> list) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory())
                listFilesFromFolder(file, list);
            else if (!list.contains(file))
                list.add(file);
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        RenderUtil renderUtil = new RenderUtil();

        for (GuiButton button : buttonList) {
            if ((!saveY.containsKey(button.id) && !saveX.containsKey(button.id)) || (scaleY != sr.getScaledHeight() || scaleX != sr.getScaledWidth())) {

                if (saveY.containsKey(button.id)) {
                    saveY.remove(button.id);
                    saveX.remove(button.id);
                }

                saveX.put(button.id, (double) button.xPosition);
                saveY.put(button.id, (double) button.yPosition);

                scaleX = sr.getScaledWidth();
                scaleY = sr.getScaledHeight();
            }
        }

        if (drag) {
            x = dragX + mouseX;
            y = dragY + mouseY;
        }

        if (dragOptions) {
            optionsX = dragOptionsX + mouseX;
            optionsY = dragOptionsY + mouseY;
        }

        if (dragColor) {
            colorX = dragColorX + mouseX;
            colorY = dragColorY + mouseY;
        }

        if (dragBackground) {
            backgroundX = dragBackgroundX + mouseX;
            backgroundY = dragBackgroundY + mouseY;
        }

        int x = (int) this.x;
        int y = (int) this.y;

        int optionsX = (int) this.optionsX;
        int optionsY = (int) this.optionsY;

        int backgroundX = (int) this.backgroundX;
        int backgroundY = (int) this.backgroundY;


        GlStateManager.enableAlpha();
        GlStateManager.disableCull();

        this.shader.useShader(this.width, this.height, mouseX, mouseY, (System.currentTimeMillis() - Koks.getKoks().initTime) / 1000F);

        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);

        GL11.glEnd();

        GL20.glUseProgram(0);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        String s = "§7Based on §rMinecraft 1.8.8 §8(§4\u00A9 Mojang AB§8)";

        Color wColor = new Color(0x141214);
        Color hoverColor = new Color(wColor.getRed(), wColor.getGreen(), wColor.getBlue(), 130);
        Color outlineColor = new Color(0x393739);

        if (showColorPicker) {
            int xC = (int) (sr.getScaledWidth() / 2 + colorX);
            int yC = (int) (sr.getScaledHeight() / 2 + colorY);

            drawRect(xC - 7, yC + colorSize / 360 - 10 - 15, xC + colorSize + 17, yC + (360 * colorSize) / 360 + 1 + 6, wColor.getRGB());
            fontRendererObj.drawString("§lx", xC + colorSize + 17 - fontRendererObj.getStringWidth("§lx") - 2, yC + colorSize / 360 - 10 - 15 + fontRendererObj.FONT_HEIGHT / 2 - 2, Color.red.getRGB(), false);
            for (int i = 0; i < 360; i++) {
                renderUtil.drawRect(xC + colorSize + 5, yC + (i * colorSize) / 360, xC + colorSize + 10, yC + (i * colorSize) / 360 + 1, Color.HSBtoRGB(1.0F - i / 360.0F, 1, 1));

                if (hue == 1.0F - i / 360.0F) {
                    drawRect(xC + colorSize + 5, yC + (i * colorSize) / 360, xC + colorSize + 10, yC + (i * colorSize) / 360 + 1, wColor.getRGB());
                }

                if (Mouse.isButtonDown(0) && mouseX >= xC + colorSize + 5 && mouseX <= xC + colorSize + 10 && mouseY >= yC + (i * colorSize) / 360 && mouseY <= yC + (360 * colorSize) / 360 + 1) {
                    hue = 1.0F - i / 360.0F;
                }
            }

            for (int i = xC; i < xC + colorSize; i = (i + colorSize / pixel)) {
                for (int j = yC; j < yC + colorSize; j = (j + colorSize / pixel)) {
                    renderUtil.drawRect(i, j, i + colorSize / pixel + 1.0F, j + colorSize / pixel + 1.0F, Color.HSBtoRGB(hue, (float) (i - xC) / colorSize, 1.0F - (float) (j - yC) / colorSize));

                    if (Mouse.isButtonDown(0) && !dragColor && mouseX >= i && mouseY >= j && mouseX <= i + colorSize / pixel + 1.0F && mouseY <= j + colorSize / pixel + 1.0F) {
                        Koks.getKoks().clientColor = new Color(Color.HSBtoRGB(hue, (float) (i - xC) / colorSize, 1.0F - (float) (j - yC) / colorSize));
                    }
                }
            }

            for (int i = xC; i < xC + colorSize; i = (i + colorSize / pixel)) {
                for (int j = yC; j < yC + colorSize; j = (j + colorSize / pixel)) {
                    if (Koks.getKoks().clientColor.getRGB() == Color.HSBtoRGB(hue, (float) (i - xC) / colorSize, 1.0F - (float) (j - yC) / colorSize)) {
                        renderUtil.drawOutlineRect(i, j, i + colorSize / pixel + 1.0F, j + colorSize / pixel + 1.0F, 1, outlineColor.getRGB(), wColor.getRGB());
                    }
                }
            }
        }

        this.drawString(this.fontRendererObj, s, 2, this.height - 10, -1);

        StringBuilder author = new StringBuilder();
        for (int ix = 0; ix < Koks.getKoks().AUTHORS.length; ix++) {
            author.append(Koks.getKoks().AUTHORS[ix]).append(", ");
        }

        author = new StringBuilder(author.substring(0, author.length() - 2));

        this.drawString(this.fontRendererObj, "§7Made by§8: §r" + author, 2, this.height - 20, -1);

        this.drawString(this.fontRendererObj, "§7Version§8: §r" + Koks.getKoks().VERSION, 2, this.height - 30, -1);

        if (showRight) {

            rightWidth = 120;
            rightHeight = 20;

            rightOptions = 2;

            for (int ix = 0; ix < rightOptions; ix++) {
                drawRect(rightX - rightOutline, rightY + rightHeight * ix, rightX, rightY + rightHeight * (ix + 1), Koks.getKoks().clientColor.getRGB());
                drawRect(rightX + rightWidth, rightY + rightHeight * ix, rightX + rightWidth + rightOutline, rightY + rightHeight * (ix + 1), Koks.getKoks().clientColor.getRGB());

                boolean hover = mouseX >= rightX && mouseY >= rightY + rightHeight * ix && mouseX <= rightX + rightWidth && mouseY <= rightY - 1 + rightHeight * (ix + 1);

                drawRect(rightX, rightY + rightHeight * ix, rightX + rightWidth, rightY + rightHeight * (ix + 1), hover ? outlineColor.darker().getRGB() : wColor.getRGB());

                drawCenteredString(fontRendererObj, getRightName(ix, false), rightX + rightWidth / 2, rightY + rightHeight * ix + 6, hover ? Koks.getKoks().clientColor.getRGB() : -1);

            }
        }

        if (windowShowed) {

            drawRect(sr.getScaledWidth() / 2 + x - wwidth, sr.getScaledHeight() / 2 + y - wheight, sr.getScaledWidth() / 2 + x + wwidth, sr.getScaledHeight() / 2 + y + wheight, wColor.getRGB());

            //LEFT
            drawRect(sr.getScaledWidth() / 2 + x - wwidth - dicke, sr.getScaledHeight() / 2 + y - wheight, sr.getScaledWidth() / 2 + x - wwidth, sr.getScaledHeight() / 2 + y + wheight, outlineColor.getRGB());
            //RIGHT
            drawRect(sr.getScaledWidth() / 2 + x + wwidth, sr.getScaledHeight() / 2 + y - wheight, sr.getScaledWidth() / 2 + x + wwidth + dicke, sr.getScaledHeight() / 2 + y + wheight, outlineColor.getRGB());
            //UP
            drawRect(sr.getScaledWidth() / 2 + x - wwidth - dicke, sr.getScaledHeight() / 2 + y - wheight - dicke, sr.getScaledWidth() / 2 + x + wwidth + dicke, sr.getScaledHeight() / 2 + y - wheight, outlineColor.getRGB());
            //DOWN
            drawRect(sr.getScaledWidth() / 2 + x - wwidth - dicke, sr.getScaledHeight() / 2 + y + wheight, sr.getScaledWidth() / 2 + x + wwidth + dicke, sr.getScaledHeight() / 2 + y + wheight + dicke, outlineColor.getRGB());

            //CHOOSE


            drawIndexSize = indexSize;

            if (currentIndex == 0) {
                drawIndexSize = 0;
            }

            int picWidth = size - 3;
            int picHeight = size - 3;

            for (int index = 0; index <= drawIndexSize; index++) {
                drawRect(sr.getScaledWidth() / 2 + x - wwidth + size * index, sr.getScaledHeight() / 2 + y - wheight - size, sr.getScaledWidth() / 2 + x - wwidth + size * index + size, sr.getScaledHeight() / 2 + y - wheight, currentIndex == index ? outlineColor.getRGB() : wColor.getRGB());
                int move = 0;
                if (index == 0)
                    move = 2;
                renderUtil.drawPicture(sr.getScaledWidth() / 2 + x - wwidth + size * index + move, sr.getScaledHeight() / 2 + y - wheight - size, picWidth, picHeight, new ResourceLocation("client/icons/MainMenu/" + getIndexName(index) + ".png"));
            }
            drawRect(sr.getScaledWidth() / 2 + x - wwidth, sr.getScaledHeight() / 2 + y - wheight - size - dicke, sr.getScaledWidth() / 2 + x - wwidth + size * drawIndexSize + size, sr.getScaledHeight() / 2 + y - wheight - size, outlineColor.getRGB());
            drawRect(sr.getScaledWidth() / 2 + x - wwidth - dicke, sr.getScaledHeight() / 2 + y - wheight - size - dicke, sr.getScaledWidth() / 2 + x - wwidth, sr.getScaledHeight() / 2 + y - wheight, outlineColor.getRGB());
            drawRect(sr.getScaledWidth() / 2 + x - wwidth + size * drawIndexSize + size, sr.getScaledHeight() / 2 + y - wheight - size - dicke, sr.getScaledWidth() / 2 + x - wwidth + size * drawIndexSize + size + dicke, sr.getScaledHeight() / 2 + y - wheight, outlineColor.getRGB());

            if (currentIndex == 0) {
                for (int index = 1; index <= indexSize; index++) {
                    drawRect(sr.getScaledWidth() / 2 + x - wwidth / 2 + size * (index - 1), sr.getScaledHeight() / 2 + y + wheight - size, sr.getScaledWidth() / 2 + x - wwidth / 2 + size * (index - 1) + size, sr.getScaledHeight() / 2 + y + wheight, wColor.getRGB());
                    renderUtil.drawPicture(sr.getScaledWidth() / 2 + x - wwidth / 2 + size * (index - 1), sr.getScaledHeight() / 2 + y + wheight - size, picWidth, picHeight, new ResourceLocation("client/icons/MainMenu/" + getIndexName(index) + ".png"));
                }
            }

            for (GuiButton button : this.buttonList) {

                if ((button.id >= 100 && button.id <= 110) || button.id == 2 || button.id == 200 || button.id == 8675309) {
                    button.visible = currentIndex == 4;
                }

                switch (button.id) {
                    case 99:
                        button.visible = currentIndex == 3;
                        break;
                }
            }


            if (drag) {
                for (GuiButton button : buttonList) {

                    double scaleFactorX = sr.getScaledWidth_double() / (double) Toolkit.getDefaultToolkit().getScreenSize().width;
                    double scaleFactorY = sr.getScaledHeight_double() / (double) Toolkit.getDefaultToolkit().getScreenSize().height;

                    int calcX = (int) (sr.getScaledWidth() / 2 - saveX.get(button.id));

                    int posX = (int) ((sr.getScaledWidth() / 2 + x - calcX));

                    int calcY = (int) (sr.getScaledHeight() / 2 - saveY.get(button.id));
                    int posY = (int) (sr.getScaledHeight() / 2 + y - calcY);


                    button.xPosition = posX;
                    button.yPosition = posY;

                }
            }

            if (!drag) {
                for (GuiButton button : buttonList) {
                    if (saveX.containsKey(button.id) && saveY.containsKey(button.id)) {

                        double scaleFactorX = sr.getScaledWidth_double() / (double) Toolkit.getDefaultToolkit().getScreenSize().width;
                        double scaleFactorY = sr.getScaledHeight_double() / (double) Toolkit.getDefaultToolkit().getScreenSize().height;

                        int calcX = (int) (sr.getScaledWidth() / 2 - saveX.get(button.id));

                        int posX = (int) ((sr.getScaledWidth() / 2 + x - calcX));

                        int calcY = (int) (sr.getScaledHeight() / 2 - saveY.get(button.id));
                        int posY = (int) (sr.getScaledHeight() / 2 + y - calcY);


                        button.xPosition = posX;
                        button.yPosition = posY;
                    }


                }
            }

            if (currentIndex == 0) {
                wheight = 80;
                /* GlyphPageFontRenderer fontRenderer = GlyphPageFontRenderer.create("Arial", 50, true ,true, true);
            fontRenderer.drawString(Koks.getKoks().NAME,sr.getScaledWidth() / 2 - fontRenderer.getStringWidth("Welcome " + Koks.getKoks().purvesManager.getPrefix()) / 2, sr.getScaledHeight() / 2 - 10, Color.white.getRGB(), true);
   */

                double scale = 1.5;
                float xPos = sr.getScaledWidth() / 2 + x - fontRenderer.getStringWidth(Koks.getKoks().NAME) / 2;
                float yPos = sr.getScaledHeight() / 2 + y - fontRenderer.getFontHeight() - 10;

                fontRenderer.drawString(Koks.getKoks().NAME, xPos, yPos, Color.white.getRGB(), true);
                fontRendererObj.drawString("Welcome " + Koks.getKoks().CLManager.getPrefix(), xPos + 2, sr.getScaledHeight() / 2 + y - 14, Color.gray.getRGB(), true);
            } else {
                wheight = 120;
                switch (currentIndex) {
                    case 1:
                        //SINGLEPLAYER
                        this.mc.displayGuiScreen(new GuiSelectWorld(this));
                        currentIndex = 0;
                        break;
                    case 2:
                        //MULTIPLAYER
                        this.mc.displayGuiScreen(new GuiMultiplayer(this));
                        currentIndex = 0;
                        break;
                    case 3:
                        //ALTS

                        email.drawTextBox();
                        password.drawTextBox();
                        email.xPosition = sr.getScaledWidth() / 2 + x - 180 / 2;
                        email.yPosition = sr.getScaledHeight() / 3 + y + 25;
                        password.xPosition = sr.getScaledWidth() / 2 + x - 180 / 2;
                        password.yPosition = sr.getScaledHeight() / 3 + y + (25 + 27);

                        drawString(fontRendererObj, loginUtil.status, 5, 5, new Color(0xAAAAAA).getRGB());

                        if (email.getText().isEmpty()) {
                            String email = "Email / TheAltening";
                            drawString(fontRendererObj, email, sr.getScaledWidth() / 2 + x - fontRendererObj.getStringWidth(email) / 2, sr.getScaledHeight() / 3 + y + 31, Color.gray.getRGB());
                        }

                        if (password.getText().isEmpty()) {
                            String password = "Password";
                            drawString(fontRendererObj, password, sr.getScaledWidth() / 2 + x - fontRendererObj.getStringWidth(password) / 2, sr.getScaledHeight() / 3 + y + (25 + 27 + 6), Color.gray.getRGB());
                        }

                        break;
                }
            }
        } else {
            drag = false;
            for (GuiButton button : buttonList) {
                button.visible = false;
            }
        }

        if (showBackgrounds) {

            drawRect(sr.getScaledWidth() / 2 + backgroundX - backgroundWidth, sr.getScaledHeight() / 2 + backgroundY - backgroundHeight, sr.getScaledWidth() / 2 + backgroundX + backgroundWidth, sr.getScaledHeight() / 2 + backgroundY, outlineColor.getRGB());
            drawCenteredString(fontRendererObj, "§l§nBackgrounds", sr.getScaledWidth() / 2 + backgroundX, sr.getScaledHeight() / 2 + backgroundY - backgroundHeight / 2 - fontRendererObj.FONT_HEIGHT / 2, -1);

            File backgroundDir = new File(mc.mcDataDir + "/Koks/Backgrounds");

            listFilesFromFolder(backgroundDir, backgrounds);

            backgrounds.removeIf(file -> !file.exists());
            backgrounds.sort(Comparator.comparing(File::getName));
            for (int i = 0; i < backgrounds.size(); i++) {
                File file = backgrounds.get(i);
                drawRect(sr.getScaledWidth() / 2 + backgroundX - backgroundWidth, sr.getScaledHeight() / 2 + backgroundY + backgroundHeight * i, sr.getScaledWidth() / 2 + backgroundX + backgroundWidth, sr.getScaledHeight() / 2 + backgroundY + backgroundHeight * (i + 1), curBackground.equalsIgnoreCase(file.getName().replace(".fsh", "")) ? wColor.darker().getRGB() : wColor.getRGB());
                drawCenteredString(fontRendererObj, file.getName().replace(".fsh", ""), sr.getScaledWidth() / 2 + backgroundX, sr.getScaledHeight() / 2 + backgroundY + backgroundHeight * i + fontRendererObj.FONT_HEIGHT, -1);
                if (mouseX >= sr.getScaledWidth() / 2 + backgroundX - backgroundWidth && mouseX <= sr.getScaledWidth() / 2 + backgroundX + backgroundWidth && mouseY >= sr.getScaledHeight() / 2 + backgroundY + backgroundHeight * i && mouseY <= sr.getScaledHeight() / 2 + backgroundY + backgroundHeight * (i + 1)) {
                    if (Mouse.isButtonDown(0)) {
                        curBackground = file.getName().replace(".fsh", "");

                    }
                }
            }
        }

        if (showOptions) {

            drawRect(sr.getScaledWidth() / 2 + optionsX - optionWidth, sr.getScaledHeight() / 2 + optionsY - optionHeight, sr.getScaledWidth() / 2 + optionsX + optionWidth, sr.getScaledHeight() / 2 + optionsY, outlineColor.getRGB());
            drawCenteredString(fontRendererObj, "§l§nOptions", sr.getScaledWidth() / 2 + optionsX, sr.getScaledHeight() / 2 + optionsY - optionHeight + 8, -1);

            fontRendererObj.drawString("§lx", sr.getScaledWidth() / 2 + optionsX + optionWidth - fontRendererObj.getStringWidth("§lx") - 2, sr.getScaledHeight() / 2 + optionsY - optionHeight + 2, Color.red.getRGB(), false);

            for (int ix = 0; ix < optionSize; ix++) {
                boolean hover = mouseX > sr.getScaledWidth() / 2 + optionsX - optionWidth && mouseX < sr.getScaledWidth() / 2 + optionsX + optionWidth && mouseY > sr.getScaledHeight() / 2 + optionsY + optionHeight * ix && mouseY < sr.getScaledHeight() / 2 + optionsY + optionHeight * (ix + 1);
                drawRect(sr.getScaledWidth() / 2 + optionsX - optionWidth, sr.getScaledHeight() / 2 + optionsY + optionHeight * ix, sr.getScaledWidth() / 2 + optionsX + optionWidth, sr.getScaledHeight() / 2 + optionsY + optionHeight * (ix + 1), hover ? outlineColor.darker().getRGB() : wColor.getRGB());
                drawCenteredString(fontRendererObj, getOptionName(ix), sr.getScaledWidth() / 2 + optionsX, sr.getScaledHeight() / 2 + optionsY + optionHeight * ix + 8, hover ? Koks.getKoks().clientColor.getRGB() : -7829368);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
        dragOptions = false;
        dragColor = false;
        super.mouseReleased(mouseX, mouseY, state);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */


    public boolean isHoverBackgroundChose(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        for (int i = 0; i < backgrounds.size(); i++) {
            if (mouseX >= sr.getScaledWidth() / 2 + backgroundX - backgroundWidth && mouseX <= sr.getScaledWidth() / 2 + backgroundX + backgroundWidth && mouseY >= sr.getScaledHeight() / 2 + backgroundY + backgroundHeight * i && mouseY <= sr.getScaledHeight() / 2 + backgroundY + backgroundHeight * (i + 1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isHoverColorClose(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        int x = (int) (sr.getScaledWidth() / 2 + colorX);
        int y = (int) (sr.getScaledHeight() / 2 + colorY);
        return mouseX >= x + colorSize + 17 - fontRendererObj.getStringWidth("§lx") - 2 && mouseX <= x + colorSize + 17 - fontRendererObj.getStringWidth("§lx") - 2 + fontRendererObj.getStringWidth("§lx") && mouseY >= y + colorSize / 360 - 10 - 15 + fontRendererObj.FONT_HEIGHT / 2 - 2 && mouseY <= y + colorSize / 360 - 10 - 15 + fontRendererObj.FONT_HEIGHT / 2 - 2 + fontRendererObj.FONT_HEIGHT;
    }

    public boolean isHoverColor(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        int x = (int) (sr.getScaledWidth() / 2 + colorX);
        int y = (int) (sr.getScaledHeight() / 2 + colorY);
        return mouseX >= x - 7 && mouseX <= x + colorSize + 17 && mouseY >= y + colorSize / 360 - 25 && mouseY <= y + (360 * colorSize) / 360 + 7;
    }

    public boolean isHoverEmail(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mouseX >= sr.getScaledWidth() / 2 + x - 180 / 2 && mouseX <= sr.getScaledWidth() / 2 + x - 180 / 2 + email.getWidth() && mouseY >= sr.getScaledHeight() / 3 + y + 25 && mouseY <= sr.getScaledHeight() / 3 + y + 25 + 20)
            return true;
        return false;
    }

    public boolean isHoverPassword(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mouseX >= sr.getScaledWidth() / 2 + x - 180 / 2 && mouseX <= sr.getScaledWidth() / 2 + x - 180 / 2 + password.getWidth() && mouseY >= sr.getScaledHeight() / 3 + y + (25 + 27) && mouseY <= sr.getScaledHeight() / 3 + y + (25 + 27 + 20))
            return true;
        return false;
    }

    public boolean isHover(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mouseX >= sr.getScaledWidth() / 2 + x - wwidth - dicke && mouseX <= sr.getScaledWidth() / 2 + x + wwidth + dicke && mouseY >= sr.getScaledHeight() / 2 + y - wheight - dicke && mouseY <= sr.getScaledHeight() / 2 + y + wheight + dicke)
            return true;
        return false;
    }

    public boolean isHoverOptions(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        return mouseX >= sr.getScaledWidth() / 2 + optionsX - optionWidth && mouseX <= sr.getScaledWidth() / 2 + optionsX + optionWidth && mouseY >= sr.getScaledHeight() / 2 + optionsY - optionHeight && mouseY <= sr.getScaledHeight() / 2 + optionsY + optionHeight * optionSize;
    }

    public boolean isHoverOptionClose(int mouseX, int mouseY) {
        ScaledResolution sr = new ScaledResolution(mc);
        return mouseX >= sr.getScaledWidth() / 2 + optionsX + optionWidth - fontRendererObj.getStringWidth("§lx") - 2 && mouseX <= sr.getScaledWidth() / 2 + optionsX + optionWidth && mouseY >= sr.getScaledHeight() / 2 + optionsY - optionHeight && mouseY <= sr.getScaledHeight() / 2 + optionsY - optionHeight + fontRendererObj.FONT_HEIGHT;
    }


    int optionWidth = 125;
    int optionHeight = 25;

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        ScaledResolution sr = new ScaledResolution(mc);


        int x = (int) this.x;
        int y = (int) this.y;

        if (mouseButton == 0) {

            if (showOptions) {

                boolean hoverDrag = mouseX > sr.getScaledWidth() / 2 + optionsX - optionWidth && mouseX < sr.getScaledWidth() / 2 + optionsX + optionWidth && mouseY > sr.getScaledHeight() / 2 + optionsY - optionHeight && mouseY < sr.getScaledHeight() / 2 + optionsY;

                if (hoverDrag) {
                    dragOptions = true;
                    dragOptionsX = optionsX - mouseX;
                    dragOptionsY = optionsY - mouseY;
                }

                if (isHoverOptionClose(mouseX, mouseY)) showOptions = false;

                for (int ix = 0; ix < optionSize; ix++) {
                    boolean hover = mouseX > sr.getScaledWidth() / 2 + optionsX - optionWidth && mouseX < sr.getScaledWidth() / 2 + optionsX + optionWidth && mouseY > sr.getScaledHeight() / 2 + optionsY + optionHeight * ix && mouseY < sr.getScaledHeight() / 2 + optionsY + optionHeight * (ix + 1);
                    if (hover)
                        switch (ix) {
                            case 0: //CLIENTCOLOR
                                showColorPicker = true;
                                break;
                            case 1: //BACKGROUND
                                break;
                            case 2: //FOLDER
                                File file1 = new File(mc.mcDataDir + "/" + Koks.getKoks().NAME);
                                String s = file1.getAbsolutePath();

                                if (Util.getOSType() == Util.EnumOS.OSX) {
                                    try {
                                        Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
                                        return;
                                    } catch (IOException ioexception1) {
                                    }
                                } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
                                    String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[]{s});

                                    try {
                                        Runtime.getRuntime().exec(s1);
                                        return;
                                    } catch (IOException ioexception) {
                                    }
                                }
                                break;
                            case 3: //RESET
                                File file = new File(mc.mcDataDir + "/" + Koks.getKoks().NAME);
                                if (file.exists())
                                    try {
                                        FileUtils.deleteDirectory(file);
                                    } catch (Exception ignored) {
                                    }
                                if (!file.exists())
                                    file.mkdirs();

                                Koks.getKoks().fileManager.readAllFiles();
                                Koks.getKoks().fileManager.writeAllFiles();
                                break;
                        }
                }
            }

            if (showColorPicker) {
                int xC = (int) (sr.getScaledWidth() / 2 + colorX);
                int yC = (int) (sr.getScaledHeight() / 2 + colorY);

                if (!isHoverColorClose(mouseX, mouseY) && mouseX >= xC - 7 && mouseX <= xC + colorSize + 17 && mouseY >= yC + colorSize / 360 - 25 && mouseY <= yC + colorSize / 360) {
                    dragColor = true;
                    dragColorX = colorX - mouseX;
                    dragColorY = colorY - mouseY;
                } else if (isHoverColorClose(mouseX, mouseY)) {
                    showColorPicker = false;
                }
            }

            if (showRight) {
                for (int ix = 0; ix < rightOptions; ix++) {
                    boolean hover = mouseX >= rightX && mouseY >= rightY + rightHeight * ix && mouseX <= rightX + rightWidth && mouseY <= rightY - 1 + rightHeight * (ix + 1);
                    if (hover) {
                        switch (ix) {
                            case 1:
                                showOptions = true;
                                break;
                        }
                    }
                }
            }

            showRight = false;

            if (showBackgrounds) {

            }

            if (windowShowed) {
                if (isHover(mouseX, mouseY)) {
                    if (!(showColorPicker && isHoverColor(mouseX, mouseY)))
                        if (!(showOptions && isHoverOptions(mouseX, mouseY))) {
                            if (!(currentIndex == 3 && isHoverEmail(mouseX, mouseY))) {
                                if (!(currentIndex == 3 && isHoverPassword(mouseX, mouseY))) {
                                    if (!(currentIndex == 0 && mouseX >= sr.getScaledWidth() / 2 + x - wwidth / 2 + size * (1 - 1) && mouseX <= sr.getScaledWidth() / 2 + x - wwidth / 2 + size * (indexSize - 1) + size && mouseY >= sr.getScaledHeight() / 2 + y + wheight - size && mouseY <= sr.getScaledHeight() / 2 + y + wheight)) {
                                        drag = true;
                                        dragX = x - mouseX;
                                        dragY = y - mouseY;
                                    }
                                }
                            }
                        }
                }

                if (!drag) {

                    switch (currentIndex) {
                        case 3:
                            if (mouseX >= sr.getScaledWidth() / 2 + x - 180 / 2 && mouseX <= sr.getScaledWidth() / 2 + x - 180 / 2 + email.getWidth() && mouseY >= sr.getScaledHeight() / 3 + y + 25 && mouseY <= sr.getScaledHeight() / 3 + y + 25 + 20) {
                                email.setFocused(true);
                                password.setFocused(false);
                            } else if (mouseX >= sr.getScaledWidth() / 2 + x - 180 / 2 && mouseX <= sr.getScaledWidth() / 2 + x - 180 / 2 + password.getWidth() && mouseY >= sr.getScaledHeight() / 3 + y + (25 + 27) && mouseY <= sr.getScaledHeight() / 3 + y + (25 + 27 + 20)) {
                                email.setFocused(false);
                                password.setFocused(true);
                            } else {
                                email.setFocused(false);
                                password.setFocused(false);
                            }
                            break;
                    }

                    lastIndex = currentIndex;

                    if (currentIndex == 0) {
                        for (int index = 1; index <= indexSize; index++)
                            if (mouseX >= sr.getScaledWidth() / 2 + x - wwidth / 2 + size * (index - 1) && mouseX <= sr.getScaledWidth() / 2 + x - wwidth / 2 + size * (index - 1) + size && mouseY >= sr.getScaledHeight() / 2 + y + wheight - size && mouseY <= sr.getScaledHeight() / 2 + y + wheight) {
                                currentIndex = index;
                            }
                    }

                    for (int index = 0; index <= drawIndexSize; index++) {
                        if (mouseX >= sr.getScaledWidth() / 2 + x - wwidth + size * index && mouseX <= sr.getScaledWidth() / 2 + x - wwidth + size * index + size && mouseY >= sr.getScaledHeight() / 2 + y - wheight - size && mouseY <= sr.getScaledHeight() / 2 + y - wheight) {
                            currentIndex = index;
                        }
                    }

                    //0: HOME 1: SinglePlayer 2: MultiPlayer 3: ALTS 4: Language 5: Options 6: Shutdown

                    switch (currentIndex) {
                        case 6:
                            currentIndex = lastIndex;
                            this.mc.shutdown();
                            break;
                    }
                }
            }
        } else if (mouseButton == 1) {
            if ((!windowShowed || !(mouseX >= sr.getScaledWidth() / 2 + x - wwidth - dicke && mouseX <= sr.getScaledWidth() / 2 + x + wwidth + dicke && mouseY >= sr.getScaledHeight() / 2 + y - wheight - dicke - size && mouseY <= sr.getScaledHeight() / 2 + y + wheight + dicke))) {
                rightX = mouseX + rightOutline;
                rightY = mouseY;
                showRight = true;
            }
        }
    }
}
