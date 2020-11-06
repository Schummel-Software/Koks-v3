package koks.changelog.impl;

import koks.changelog.Changelog;
import koks.changelog.ChangelogInfo;

/**
 * @author kroko
 * @created on 01.11.2020 : 05:17
 */

@ChangelogInfo(version = "3.0.0")
public class V3_0_0 extends Changelog{


    @Override
    public void changes() {
        added("ShaderESP to all ESP's");
        added("Pictures to the Main Menu");
        added("DormentESP");
        added("Tracers");
        added("ChestESP Cycle Mode");
        added("Swing");
        added("MotionGraph");
        added("ArrayList Mode");
        added("FakeBlock");
        added("Online Configs");
        added("AAC4 Speed");
        added("Intave GodMode for DBD (Dead By Daylight)");
        added("Discord Rich Presents");
        added("FastBow Timer Mode");
        added("WallSpeed");
        added("Step Modes");
        added("Friends");
        added("Safewalk");
        added("GommeMode");
        added("Changelog");
        added("NoRotate");
        fixed("Intave Scaffold");
        fixed("Block Bug");
        removed("ClickGUI Settings");
    }

}
