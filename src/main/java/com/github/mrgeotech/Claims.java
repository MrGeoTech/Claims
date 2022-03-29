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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
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
        return getInstance().getConfig().contains(path) ? color(getInstance().getConfig().getString(path)) : path;
    }

    public static List<String> getColoredList(String path) {
        return (getInstance().getConfig().contains(path) ?
                getInstance().getConfig().getStringList(path) :
                new ArrayList<>(Collections.singleton(path))).stream().map(Claims::color).toList();
    }

    public static List<String> getInfoList(Claim claim, String path) {
        return getColoredList(path).stream()
                .map(s -> s.replaceAll("%owner%", Objects.requireNonNull(claim.getOwner().getName()))
                        .replaceAll("%area%", claim.getArea() + "")
                        .replaceAll("%volume%", claim.getVolume() + "")
                        .replaceAll("%coord1%", claim.getCorner1())
                        .replaceAll("%coord2%", claim.getCorner2())).toList();
    }

    public static List<String> getFlagLore(Claim claim, String path, Claim.Flag flag) {
        return getColoredList(path).stream()
                .map(s -> s.replaceAll("%" + flag.name() + "%", claim.getFlags().contains(flag) ?
                                Claims.getColoredString("flag-enabled") :
                                Claims.getColoredString("flag-disabled")
                )).toList();
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
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("" + ChatColor.RESET);
        item.setItemMeta(meta);
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
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("" + ChatColor.RESET);
        item.setItemMeta(meta);
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

    public static void openFlagsInventory(Claim claim, Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, Claims.getColoredString("claim-flag-inventory-title"));

        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("go-back-to-menu-item-name"));
        meta.setLore(Claims.getColoredList("go-back-to-menu-item-lore"));
        item.setItemMeta(meta);
        inventory.setItem(18, item);
        item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName("" + ChatColor.RESET);
        item.setItemMeta(meta);
        inventory.setItem(19, item);
        inventory.setItem(20, item);
        inventory.setItem(21, item);
        inventory.setItem(22, item);
        inventory.setItem(23, item);
        inventory.setItem(24, item);
        inventory.setItem(25, item);
        inventory.setItem(26, item);

        item = new ItemStack(Material.CHEST);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("open-chest-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "open-chest-flag-lore %OPEN_CHEST%", Claim.Flag.OPEN_CHEST));
        item.setItemMeta(meta);
        inventory.setItem(0, item);
        item = new ItemStack(Material.OAK_DOOR);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("interact-redstone-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "interact-redstone-flag-lore %INTERACT_REDSTONE%", Claim.Flag.INTERACT_REDSTONE));
        item.setItemMeta(meta);
        inventory.setItem(1, item);
        item = new ItemStack(Material.ENDER_PEARL);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("use-pearl-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "use-pearl-flag-lore %USE_PEARL%", Claim.Flag.USE_PEARL));
        item.setItemMeta(meta);
        inventory.setItem(2, item);
        item = new ItemStack(Material.CHORUS_FRUIT);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("use-chorus-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "use-chorus-flag-lore %USE_CHORUS%", Claim.Flag.USE_CHORUS));
        item.setItemMeta(meta);
        inventory.setItem(3, item);
        item = new ItemStack(Material.GRASS_BLOCK);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("place-blocks-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "place-blocks-flag-lore %PLACE_BLOCKS%", Claim.Flag.PLACE_BLOCKS));
        item.setItemMeta(meta);
        inventory.setItem(4, item);
        item = new ItemStack(Material.STONE_PICKAXE);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("break-blocks-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "break-blocks-flag-lore %BREAK_BLOCKS%", Claim.Flag.BREAK_BLOCKS));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS);
        item.setItemMeta(meta);
        inventory.setItem(5, item);
        item = new ItemStack(Material.ANVIL);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("use-anvils-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "use-anvils-flag-lore %USE_ANVILS%", Claim.Flag.USE_ANVILS));
        item.setItemMeta(meta);
        inventory.setItem(6, item);
        item = new ItemStack(Material.HOPPER);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("pick-up-items-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "pick-up-items-flag-lore %PICK_UP_ITEMS%", Claim.Flag.PICK_UP_ITEMS));
        item.setItemMeta(meta);
        inventory.setItem(7, item);
        item = new ItemStack(Material.TNT);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("creeper-explosions-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "creeper-explosions-flag-lore %CREEPER_EXPLOSIONS%", Claim.Flag.CREEPER_EXPLOSIONS));
        item.setItemMeta(meta);
        inventory.setItem(8, item);
        item = new ItemStack(Material.ENDERMAN_SPAWN_EGG);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("enderman-grief-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "enderman-grief-flag-lore %ENDERMAN_GRIEF%", Claim.Flag.ENDERMAN_GRIEF));
        item.setItemMeta(meta);
        inventory.setItem(9, item);
        item = new ItemStack(Material.SNOW_BLOCK);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("snow-formation-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "snow-formation-flag-lore %SNOW_FORMATION%", Claim.Flag.SNOW_FORMATION));
        item.setItemMeta(meta);
        inventory.setItem(10, item);
        item = new ItemStack(Material.WATER_BUCKET);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("snow-melt-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "snow-melt-flag-lore %SNOW_MELT%", Claim.Flag.SNOW_MELT));
        item.setItemMeta(meta);
        inventory.setItem(11, item);
        item = new ItemStack(Material.ICE);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("ice-formation-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "ice-formation-flag-lore %ICE_FORMATION%", Claim.Flag.ICE_FORMATION));
        item.setItemMeta(meta);
        inventory.setItem(12, item);
        item = new ItemStack(Material.WATER_BUCKET);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("ice-melting-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "ice-melting-flag-lore %ICE_MELTING%", Claim.Flag.ICE_MELT));
        item.setItemMeta(meta);
        inventory.setItem(13, item);
        item = new ItemStack(Material.WATER_BUCKET);
        meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Claims.getColoredString("mycelium-spread-flag-name"));
        meta.setLore(Claims.getFlagLore(claim, "mycelium-spread-flag-lore %MYCELIUM_SPREAD%", Claim.Flag.MYCELIUM_SPREAD));
        item.setItemMeta(meta);
        inventory.setItem(14, item);

        player.openInventory(inventory);
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
                case 1 -> {
                    if (claim.isShown())
                        claim.hide();
                    else
                        claim.show(1200);
                }
                case 3 -> Claims.openMembersInventory(claim, (Player) event.getWhoClicked());
                case 5 -> Claims.openFlagsInventory(claim, (Player) event.getWhoClicked());
                case 8 -> claims.remove(claim.delete());
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

            if (event.getCurrentItem() == null || !(event.getCurrentItem().getItemMeta() instanceof SkullMeta)) return;
            OfflinePlayer player = ((SkullMeta) event.getCurrentItem().getItemMeta()).getOwningPlayer();
            assert player != null;
            System.out.println(player.getName());
            if (claim.getOwner() == player || claim.getMembers().contains(player)) return;
            System.out.println(player.getName());
            claim.getMembers().add(player);
        } else if (event.getWhoClicked().getOpenInventory().getTitle().equals(Claims.getColoredString("claim-flag-inventory-title"))) {
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
                case 0 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.OPEN_CHEST);
                    else claim.getFlags().remove(Claim.Flag.OPEN_CHEST);
                }
                case 1 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.INTERACT_REDSTONE);
                    else claim.getFlags().remove(Claim.Flag.INTERACT_REDSTONE);
                }
                case 2 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.USE_PEARL);
                    else claim.getFlags().remove(Claim.Flag.USE_PEARL);
                }
                case 3 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.USE_CHORUS);
                    else claim.getFlags().remove(Claim.Flag.USE_CHORUS);
                }
                case 4 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.PLACE_BLOCKS);
                    else claim.getFlags().remove(Claim.Flag.PLACE_BLOCKS);
                }
                case 5 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.BREAK_BLOCKS);
                    else claim.getFlags().remove(Claim.Flag.BREAK_BLOCKS);
                }
                case 6 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.USE_ANVILS);
                    else claim.getFlags().remove(Claim.Flag.USE_ANVILS);
                }
                case 7 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.PICK_UP_ITEMS);
                    else claim.getFlags().remove(Claim.Flag.PICK_UP_ITEMS);
                }
                case 8 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.CREEPER_EXPLOSIONS);
                    else claim.getFlags().remove(Claim.Flag.CREEPER_EXPLOSIONS);
                }
                case 9 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.ENDERMAN_GRIEF);
                    else claim.getFlags().remove(Claim.Flag.ENDERMAN_GRIEF);
                }
                case 10 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.SNOW_FORMATION);
                    else claim.getFlags().remove(Claim.Flag.SNOW_FORMATION);
                }
                case 11 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.SNOW_MELT);
                    else claim.getFlags().remove(Claim.Flag.SNOW_MELT);
                }
                case 12 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.ICE_FORMATION);
                    else claim.getFlags().remove(Claim.Flag.ICE_FORMATION);
                }
                case 13 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.ICE_MELT);
                    else claim.getFlags().remove(Claim.Flag.ICE_MELT);
                }
                case 14 -> {
                    if (event.getClick().isLeftClick()) claim.getFlags().add(Claim.Flag.MYCELIUM_SPREAD);
                    else claim.getFlags().remove(Claim.Flag.MYCELIUM_SPREAD);
                }
                case 18 -> {
                    openClaimInventory(claim, (Player) event.getWhoClicked());
                    return;
                }
            }
            openFlagsInventory(claim, (Player) event.getWhoClicked());
        }
    }

}
