package koks.cl;

import god.buddy.aot.BCompiler;

/**
 * @author krokoM
 * @created on 04.10.2020 : 21:11
 */
public class CLManager {

    private final User user;

    public CLManager(String name) {
        this.user = new User(name, getRole(name));
    }

    @BCompiler(aot = BCompiler.AOT.NORMAL)
    public Role getRole(String name) {
        if (name.equalsIgnoreCase("Kroko") || name.equalsIgnoreCase("Phantom") || name.equalsIgnoreCase("Deleteboys") || name.equalsIgnoreCase("derrealedasdirt"))
            return Role.Developer;
        if (name.equalsIgnoreCase("Haze"))
            return Role.Admin;
        if (name.equalsIgnoreCase("CrazyMemeCoke") || name.equalsIgnoreCase("Jannickel1337R"))
            return Role.Friend;
        else
            return Role.User;
    }

    public String getPrefix() {
        String color;
        switch (user.getRole()) {
            case User:
                color = "§a§lKoks User §7| §a";
                break;
            case Admin:
                color = "§c§lKoks Admin §7| §c";
                break;
            case Developer:
                color = "§b§lKoks Dev §7| §b";
                break;
            case Friend:
                color = "§b§lKoks Friend §7 §b";
                break;
            default:
                color = "";
                break;
        }
        return color + user.getUsername() + "§r";
    }

    public User getUser() {
        return user;
    }
}
