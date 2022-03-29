package com.github.mrgeotech;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Claims extends JavaPlugin implements Listener {

    public List<Claim> claims = new ArrayList<>();

    public static Claims getInstance() {
        return (Claims) Bukkit.getPluginManager().getPlugin("Claims");
    }

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s == null ? "" : s);
    }

    public static String getColoredString(String path) {
        return color(getInstance().getConfig().getString(path));
    }

    public static List<String> getColoredList(String path) {
        return getInstance().getConfig().getStringList(path).stream().map(Claims::color).toList();
    }

    public static List<String> getInfoList(Claim claim, String path) {
        return getColoredList(path).stream()
                .map(s -> s.replaceAll("%owner%", claim.getOwner().getName())
                        .replaceAll("%area%", claim.getArea() + "")
                        .replaceAll("%volume%", claim.getVolume() + "")
                        .replaceAll("%coord1%", claim.getCorner1())
                        .replaceAll("%coord2%", claim.getCorner2())).toList();
    }

    public static void openClaimInventory(Claim claim, Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, Claims.getColoredString("claim-inventory-title"));
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("claim-information-item-name"));
        meta.setLore(Claims.getInfoList(claim, "claim-information-item-lore"));
        item.setItemMeta(meta);
        inventory.setItem(0, item);
        item = new ItemStack(Material.OAK_FENCE);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("claim-view-item-name"));
        meta.setLore(Claims.getColoredList("claim-view-item-lore"));
        item.setItemMeta(meta);
        inventory.setItem(1, item);
        item = new ItemStack(Material.PLAYER_HEAD);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("claim-members-item-name"));
        meta.setLore(Claims.getColoredList("claim-members-item-lore"));
        item.setItemMeta(meta);
        inventory.setItem(3, item);
        item = new ItemStack(Material.GRAY_BANNER);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("claim-flags-item-name"));
        meta.setLore(Claims.getColoredList("claim-flags-item-lore"));
        item.setItemMeta(meta);
        inventory.setItem(5, item);
        item = new ItemStack(Material.BARRIER);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("claim-delete-item-name"));
        meta.setLore(Claims.getColoredList("claim-delete-item-lore"));
        item.setItemMeta(meta);
        inventory.setItem(8, item);
        item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        item.setItemMeta(null);
        inventory.setItem(2, item);
        inventory.setItem(4, item);
        inventory.setItem(6, item);
        inventory.setItem(7, item);
        player.openInventory(inventory);
    }

    public static void openMembersInventory(Claim claim, Player p) {
        Inventory inventory = Bukkit.createInventory(null, 27, Claims.getColoredString("members-inventory-title"));
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("go-back-to-menu-item-name"));
        meta.setLore(Claims.getColoredList("go-back-to-menu-item-lore"));
        item.setItemMeta(meta);
        inventory.setItem(18, item);
        item = new ItemStack(Material.EMERALD);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("add-member-item-name"));
        meta.setLore(Claims.getColoredList("add-member-item-lore"));
        item.setItemMeta(meta);
        inventory.setItem(20, item);
        item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        item.setItemMeta(null);
        inventory.setItem(19, item);
        inventory.setItem(21, item);
        inventory.setItem(22, item);
        inventory.setItem(23, item);
        inventory.setItem(24, item);
        inventory.setItem(25, item);
        inventory.setItem(26, item);
        item = new ItemStack(Material.PLAYER_HEAD);
        for (OfflinePlayer player : claim.getMembers()) {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            assert skullMeta != null;
            skullMeta.setOwningPlayer(player);
            skullMeta.setDisplayName(player.getName());
            skullMeta.setLore(Claims.getColoredList("member-head-lore"));
            item.setItemMeta(skullMeta);
            inventory.addItem(item);
        }

        p.openInventory(inventory);
    }

    public static void openAddMemberInventory(Claim claim, Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, Claims.getColoredString("add-member-inventory-title"));
        ItemStack item = new ItemStack(Material.PLAYER_HEAD);
        for (Player online : Bukkit.getOnlinePlayers()) {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            assert skullMeta != null;
            skullMeta.setOwningPlayer(online);
            skullMeta.setDisplayName(online.getName());
            skullMeta.setLore(Claims.getColoredList("online-player-head-lore"));
            item.setItemMeta(skullMeta);
            inventory.addItem(item);
        }

        player.openInventory(inventory);
    }

    public static void openFlagsInventory(Claim claim) {
        Inventory inventory = Bukkit.createInventory(null, 3, Claims.getColoredString("claim-flag-inventory-title"));

    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("Claims"));
        Bukkit.getPluginManager().registerEvents(new ClaimBuilder(), Bukkit.getPluginManager().getPlugin("Claims"));
    }

    @Override
    public void onDisable() {
        ParticleHandler.removeAllShapes();
        saveConfig();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getOpenInventory().getTitle().equals(Claims.getColoredString("claim-inventory-title"))) {
            event.setCancelled(true);
            Claim claim  = claims.get(0);
            double distance = claim.distance(event.getWhoClicked().getLocation());
            for (Claim claim1 : claims) {
                double distance1 = claim1.distance(event.getWhoClicked().getLocation());
                if (distance1 < distance) {
                    claim = claim1;
                    distance = distance1;
                }
            }

            switch (event.getSlot()) {
                case 1 -> {
                    if (claim.isShown())
                        claim.hide();
                    else
                        claim.show(1200);
                }
                case 3 -> Claims.openMembersInventory(claim, (Player) event.getWhoClicked());
                case 5 -> {
                }
                case 8 -> {

                }
            }
        } else if (event.getWhoClicked().getOpenInventory().getTitle().equals(Claims.getColoredString("members-inventory-title"))) {
            event.setCancelled(true);
            Claim claim = claims.get(0);
            double distance = claim.distance(event.getWhoClicked().getLocation());
            for (Claim claim1 : claims) {
                double distance1 = claim1.distance(event.getWhoClicked().getLocation());
                if (distance1 < distance) {
                    claim = claim1;
                    distance = distance1;
                }
            }

            switch (event.getSlot()) {
                case 18 -> openClaimInventory(claim, (Player) event.getWhoClicked());
                case 20 -> openAddMemberInventory(claim, (Player) event.getWhoClicked());
                case 19,21,22,23,24,25,26 -> {}
                default -> {
                    if (event.getCurrentItem() != null) {
                        ItemStack item = event.getCurrentItem();
                        if (event.isLeftClick())
                            claim.getMembers().remove(((SkullMeta) Objects.requireNonNull(item.getItemMeta())).getOwningPlayer());
                    }
                }
            }
        } else if (event.getWhoClicked().getOpenInventory().getTitle().equals(Claims.getColoredString("add-member-inventory-title"))) {
            if (event.getCurrentItem() != null || !(event.getCurrentItem().getItemMeta() instanceof SkullMeta)) return;
        }
    }

}
