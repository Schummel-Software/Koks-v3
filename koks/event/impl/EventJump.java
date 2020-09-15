package koks.event.impl;

import koks.event.Event;

/**
 * @author deleteboys | lmao | kroko
 * @created on 13.09.2020 : 05:29
 */
public class EventJump extends Event {

    private float yaw;

    public EventJump(float yaw) {
        this.yaw = yaw;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
