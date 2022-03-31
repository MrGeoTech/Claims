package com.github.mrgeotech;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Claim {

    private final UUID owner;
    private final List<UUID> members;
    private final List<Flag> flags;
    private final int x, y, z;
    private boolean isCompleted;
    private boolean isShown;
    private Integer x1, x2, y1, y2 = null;
    private World world = null;

    public Claim(Player owner, int x, int y, int z) {
        this.owner = owner.getUniqueId();
        this.members = new ArrayList<>();
        this.flags = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.z = z;
        this.isCompleted = false;
        this.isShown = false;
    }

    public Claim(ConfigurationSection section) {
        this.owner = UUID.fromString(Objects.requireNonNull(section.getString("owner")));
        List<String> sMembers = section.getStringList("members");
        this.members = new ArrayList<>();
        for (String player : sMembers) {
            members.add(UUID.fromString(player));
        }
        List<String> sFlags = section.getStringList("flags");
        this.flags = new ArrayList<>();
        for (String flag : sFlags) {
            flags.add(Flag.valueOf(flag));
        }
        this.x = section.getInt("blockX");
        this.y = section.getInt("blockY");
        this.z = section.getInt("blockZ");
        this.isCompleted = true;
        this.isShown = section.getBoolean("isShown");
        this.x1 = section.getInt("corner1X");
        this.y1 = section.getInt("corner1Y");
        this.x2 = section.getInt("corner2X");
        this.y2 = section.getInt("corner2Y");
        this.world = Bukkit.getWorld(Objects.requireNonNull(section.getString("world")));
        assert this.world != null;
        this.world.setType(x, y, z, Material.END_PORTAL_FRAME);
    }

    @Nonnull
    public OfflinePlayer getOwner() {
        return Bukkit.getOfflinePlayer(owner);
    }

    public List<UUID> getMembers() {
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
        Claims.getInstance().economy.withdrawPlayer(Bukkit.getOfflinePlayer(owner), getArea() * 50);
    }

    public boolean canComplete(Player player) {
        if (x1 != null && y1 != null && x2 != null && y2 != null) {
            if (this.containsNotComplete(new Location(world, x, y, z))) {
                if (Claims.getInstance().economy.getBalance(Bukkit.getOfflinePlayer(owner)) >= getArea() * 50) {
                    return true;
                }
                player.sendMessage(Claims.getColoredString("not-enough-money-error")
                        .replaceAll("%HAVE%", String.valueOf(Claims.getInstance().economy.getBalance(player)))
                        .replaceAll("%NEED%", String.valueOf(getArea() * 50)));
                return false;
            }
            player.sendMessage(Claims.getColoredString("block-must-be-in-claim-error"));
            return false;
        }
        player.sendMessage(Claims.getColoredString("claim-cannot-complete-message"));
        return false;
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
        ParticleHandler.showLine(x, y, Bukkit.getOfflinePlayer(owner).getPlayer());
    }

    public void setCorner2(int x, int y) {
        if (x2 != null)
            ParticleHandler.hideLine(x2, y2);
        this.x2 = x;
        this.y2 = y;
        ParticleHandler.showLine(x, y, Bukkit.getOfflinePlayer(owner).getPlayer());
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
        if (x1 != null && y1 != null && x2 != null && y2 != null)
            return (Math.max(x1, x2) - Math.min(x1, x2)) * (Math.max(y1, y2) - Math.min(y1, y2));
        else
            return 0;
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

    public boolean contains(Location location) {
        return isCompleted && location.getBlockX() > Math.min(x1, x2) && location.getBlockX() < Math.max(x1, x2) &&
                location.getBlockZ() > Math.min(y1, y2) && location.getBlockZ() < Math.max(y1, y2);
    }

    public boolean containsNotComplete(Location location) {
        if (x1 != null && y1 != null && x2 != null && y2 != null)
            return location.getBlockX() > Math.min(x1, x2) && location.getBlockX() < Math.max(x1, x2) &&
                    location.getBlockZ() > Math.min(y1, y2) && location.getBlockZ() < Math.max(y1, y2);
        else
            return false;
    }

    public boolean isNotMember(OfflinePlayer player) {
        return !members.contains(player.getUniqueId()) && owner != player.getUniqueId();
    }

    public Claim delete() {
        if (isShown)
            hide();
        world.setType(x, y, z, Material.AIR);
        Objects.requireNonNull(Bukkit.getOfflinePlayer(owner).getPlayer()).closeInventory();
        return this;
    }

    public void save(ConfigurationSection section) {
        if (!isCompleted) return;
        section.set("owner", owner.toString());
        List<String> sMembers = new ArrayList<>();
        for (UUID player : members) {
            sMembers.add(player.toString());
        }
        section.set("members", sMembers);
        List<String> sFlags = new ArrayList<>();
        for (Flag flag : flags) {
            sFlags.add(flag.name());
        }
        section.set("flags", sFlags);
        section.set("blockX", x);
        section.set("blockY", y);
        section.set("blockZ", z);
        section.set("isShown", isShown);
        section.set("corner1X", x1);
        section.set("corner1Y", y1);
        section.set("corner2X", x2);
        section.set("corner2Y", y2);
        section.set("world", world.getName());
        this.delete();
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
