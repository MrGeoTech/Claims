package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClaimBuilder implements Listener {

    public static void giveClaimItem(Player player) {
        ItemStack item = new ItemStack(Material.END_PORTAL_FRAME);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("claim-object-name"));
        meta.setLore(Claims.getColoredList("claim-object-lore"));
        item.setItemMeta(meta);
        if (player.getInventory().contains(item)) {
            player.sendMessage(Claims.getColoredString("can-only-build-one-claim-at-a-time-error"));
            return;
        }
        for (int i = 0; i < 36; i++) {
            if (player.getInventory().getItem(i) == null) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take " + player.getName() + " 100");
                player.getInventory().setItem(i, item);
                return;
            }
        }
        player.sendMessage(Claims.getColoredString("no-open-slot-error"));
    }

    public static void startClaimProcess(Player player, Block block) {
        player.sendMessage(Claims.getColoredString("claim-start-message"));
        Claims.getInstance().claims.add(new Claim(player, block.getX(), block.getY(), block.getZ()));
    }

    private final List<Player> waitList = new ArrayList<>();

    private void addWait(Player player) {
        waitList.add(player);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Claims.getInstance(), () -> waitList.remove(player), 20);
    }

    public boolean isWaiting(Player player) {
        return waitList.contains(player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (isWaiting(event.getPlayer())) return;

        if (event.getClickedBlock() == null) return;

        for (Claim claim : Claims.getInstance().claims) {
            if (claim.getOwner() == event.getPlayer()) {
                if (claim.isCompleted()) {
                    if (event.getClickedBlock().getType().equals(Material.END_PORTAL_FRAME) &&
                            claim.isClaimBlock(event.getClickedBlock())) {
                        Claims.openClaimInventory(claim, event.getPlayer());
                        addWait(event.getPlayer());
                        event.setCancelled(true);
                    } else {
                        continue;
                    }
                } else {
                    event.setCancelled(true);
                    if (Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.END_PORTAL_FRAME)) {
                        if (claim.canComplete(event.getPlayer())) {
                            claim.complete();
                            event.getPlayer().sendMessage(Claims.getColoredString("claim-completed-message"));
                        }
                    } else {
                        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                            event.getPlayer().sendMessage(Claims.getColoredString("claim-left-click-corner-message")
                                    .replaceAll("%COST%", String.valueOf(claim.getArea() * Claims.getInstance().getConfig().getInt("price-per-block"))));
                            if (claim.getWorld() == null) claim.setWorld(event.getPlayer().getWorld());
                            claim.setCorner1(event.getClickedBlock().getX(), event.getClickedBlock().getZ());
                        } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                            event.getPlayer().sendMessage(Claims.getColoredString("claim-right-click-corner-message")
                                    .replaceAll("%COST%", String.valueOf(claim.getArea() * Claims.getInstance().getConfig().getInt("price-per-block"))));
                            if (claim.getWorld() == null) claim.setWorld(event.getPlayer().getWorld());
                            claim.setCorner2(event.getClickedBlock().getX(), event.getClickedBlock().getZ());
                        }
                    }
                    addWait(event.getPlayer());
                }
                return;
            }
        }

        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            assert event.getClickedBlock() != null;

            if (event.hasItem() && event.hasBlock() && Objects.requireNonNull(event.getItem()).getType().equals(Material.END_PORTAL_FRAME)) {
                if (Claims.getInstance().claims.stream()
                        .filter(claim -> (claim.getOwner().getUniqueId() == event.getPlayer().getUniqueId() && !claim.isCompleted()) ||
                                claim.contains(event.getClickedBlock().getLocation()))
                        .toList().size() == 0) {
                    ClaimBuilder.startClaimProcess(event.getPlayer(), Objects.requireNonNull(event.getClickedBlock()).getRelative(event.getBlockFace()));
                    addWait(event.getPlayer());
                }
            }
        }
    }

}
