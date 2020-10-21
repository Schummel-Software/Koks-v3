package koks.event.impl;

import koks.event.Event;
import net.minecraft.entity.Entity;

/**
 * @author kroko
 * @created on 20.10.2020 : 15:26
 */
public class EventAllowOutline extends Event {

    boolean allow;
    Entity entity;

    public EventAllowOutline(Entity entity, boolean allow) {
        this.entity = entity;
        this.allow = allow;
    }

    public Entity getEntity() {
        return entity;
    }

    public boolean isAllow() {
        return allow;
    }

    public void setAllow(boolean allow) {
        this.allow = allow;
    }
}
