package koks.friends;

import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author kroko
 * @created on 14.10.2020 : 17:50
 */
public class FriendManager {

    public HashMap<String, String> friends = new HashMap<>();

    public void addFriend(String name, String alias) {
        if(!friends.containsKey(name)) {
            friends.put(name, alias);
        }
    }

    public void removeFriend(String name) {
        friends.remove(name);
    }

    public boolean isFriend(String name) {
        return friends.containsKey(name);
    }

    public String getName(String name) {
        return friends.get(name);
    }
}
