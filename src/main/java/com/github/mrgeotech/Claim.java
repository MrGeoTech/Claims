package com.github.mrgeotech;

import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Claim {

    private final Player owner;
    private final List<Player> members;
    private final List<Flags> flags;
    private boolean isCompleted;
    private Integer x1, x2, y1, y2 = null;
    private World world = null;

    public Claim(Player owner) {
        this.owner = owner;
        this.members = new ArrayList<>();
        this.flags = new ArrayList<>();
        this.isCompleted = false;
    }

    public Player getOwner() {
        return owner;
    }

    public List<Player> getMembers() {
        return members;
    }

    public List<Flags> getFlags() {
        return flags;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void complete() {
        this.isCompleted = true;
    }

    public boolean canComplete() {
        return x1 != null && y1 != null && x2 != null && y2 != null;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    enum Flags {

    }

}
