package com.github.mrgeotech;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;

public class ClaimBuilder implements Listener {

    public static void giveClaimItem(Player player) {
        ItemStack item = new ItemStack(Material.END_PORTAL, 1);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("claim-object-name"));
        List<String> lore = Claims.getInstance().getConfig().getStringList("claim-object-lore");
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, Claims.color(lore.get(i)));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        player.getInventory().addItem(item).forEach(
                (integer, itemStack) -> player.getWorld().dropItemNaturally(player.getLocation(), item)
        );
    }

    public static void startClaimProcess(Player player) {
        player.sendMessage(Claims.getColoredString("claim-start-message"));
        Claims.getInstance().claims.add(new Claim(player));
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        for (Claim claim : Claims.getInstance().claims) {
            if (claim.getOwner() == event.getPlayer() && !claim.isCompleted() && event.hasBlock()) {
                event.setCancelled(true);
                if (Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.END_PORTAL_FRAME)) {
                    if (claim.canComplete()) {
                        claim.complete();
                    } else {
                        event.getPlayer().sendMessage(Claims.getColoredString("claim-cannot-complete-message"));
                    }
                } else {
                    if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                        event.getPlayer().sendMessage(Claims.getColoredString("claim-left-click-corner-message"));
                        claim.setX1(event.getClickedBlock().getX());
                        claim.setY1(event.getClickedBlock().getY());
                        if (claim.getWorld() == null) claim.setWorld(event.getPlayer().getWorld());
                    } else if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        event.getPlayer().sendMessage(Claims.getColoredString("claim-right-click-corner-message"));
                        claim.setX2(event.getClickedBlock().getX());
                        claim.setY2(event.getClickedBlock().getY());
                    }
                }
                return;
            }
        }
    }

}
