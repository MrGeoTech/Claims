package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class Claim {

    private final OfflinePlayer owner;
    private final List<OfflinePlayer> members;
    private final List<Flag> flags;
    private final int x, y, z;
    private boolean isCompleted;
    private boolean isShown;
    private Integer x1, x2, y1, y2 = null;
    private World world = null;

    public Claim(Player owner, int x, int y, int z) {
        this.owner = owner;
        this.members = new ArrayList<>();
        this.flags = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.z = z;
        this.isCompleted = false;
        this.isShown = false;
    }

    @Nonnull
    public OfflinePlayer getOwner() {
        return owner;
    }

    public List<OfflinePlayer> getMembers() {
        return members;
    }

    public List<Flag> getFlags() {
        return flags;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void complete() {
        this.isCompleted = true;
        ParticleHandler.hideLine(x1, y1);
        ParticleHandler.hideLine(x2, y2);
        this.show(60);
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

    public boolean isClaimBlock(Block block) {
        return block.getX() == x && block.getY() == y && block.getZ() == z;
    }

    public void setCorner1(int x, int y) {
        if (x1 != null)
            ParticleHandler.hideLine(x1, y1);
        this.x1 = x;
        this.y1 = y;
        ParticleHandler.showLine(x, y, owner.getPlayer());
    }

    public void setCorner2(int x, int y) {
        if (x2 != null)
            ParticleHandler.hideLine(x2, y2);
        this.x2 = x;
        this.y2 = y;
        ParticleHandler.showLine(x, y, owner.getPlayer());
    }

    public void show() {
        ParticleHandler.showClaim(this);
        this.isShown = true;
    }

    public void show(int ticks) {
        show();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Claims.getInstance(), this::hide, ticks);
    }

    public void hide() {
        ParticleHandler.hideClaim(this);
        this.isShown = false;
    }

    public int getArea() {
        return (Math.max(x1, x2) - Math.min(x1, x2)) * (Math.max(y1, y2) - Math.min(y1, y2));
    }

    public int getVolume() {
        return this.getArea() * 384;
    }

    public String getCorner1() {
        return x1 + ", " + y1;
    }

    public String getCorner2() {
        return x2 + ", " + y2;
    }

    public double distance(Location location) {
        return location.distance(new Location(location.getWorld(), x, y, z));
    }

    public boolean isShown() {
        return isShown;
    }

    public Claim delete() {
        if (isShown)
            hide();
        return this;
    }

    enum Flag {
        OPEN_CHEST,
        INTERACT_REDSTONE,
        USE_PEARL,
        USE_CHORUS,
        PLACE_BLOCKS,
        BREAK_BLOCKS,
        USE_ANVILS,
        PICK_UP_ITEMS,
        CREEPER_EXPLOSIONS,
        ENDERMAN_GRIEF,
        SNOW_FORMATION,
        SNOW_MELT,
        ICE_FORMATION,
        ICE_MELT,
        MYCELIUM_SPREAD
    }

}
