package koks.purves;

/**
 * @author kroko
 * @created on 04.10.2020 : 21:11
 */
public class PurvesManager {

    private final User user;

    public PurvesManager(String name) {
        this.user = new User(name, getRole(name));
    }

    public Role getRole(String name) {
        if (name.equalsIgnoreCase("Kroko") || name.equalsIgnoreCase("Phantom") || name.equalsIgnoreCase("Deleteboys") || name.equalsIgnoreCase("Dirt"))
            return Role.Developer;
        if (name.equalsIgnoreCase("Hizzy") || name.equalsIgnoreCase("ImShadow"))
            return Role.Admin;
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
