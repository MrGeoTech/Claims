package com.github.mrgeotech;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

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
            if
        }
    }

}
