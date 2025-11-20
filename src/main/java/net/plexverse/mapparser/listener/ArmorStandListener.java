package net.plexverse.mapparser.listener;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
import net.plexverse.mapparser.constant.Keys;
import net.plexverse.mapparser.enums.DataPointType;
import net.plexverse.mapparser.mapsettings.MapSettingsManager;
import net.plexverse.mapparser.menu.DataPointMenu;
import net.plexverse.mapparser.menu.ModifyMenu;
import net.plexverse.mapparser.objects.Team;
import net.plexverse.mapparser.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;

public class ArmorStandListener implements Listener {

    @EventHandler
    private static void onArmorStandPlace(final EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof final ArmorStand entity)) {
            return;
        }
        entity.setGravity(false);
        final ItemStack itemStack = event.getPlayer().getItemInHand();
        if (itemStack.isEmpty() || itemStack.getType() != Material.ARMOR_STAND || !itemStack.hasItemMeta() || !itemStack.getItemMeta().getPersistentDataContainer().has(Keys.DATAPOINT_KEY)) {
            return;
        }
        final String dataPointName = itemStack.getItemMeta().getPersistentDataContainer().get(Keys.DATAPOINT_KEY, PersistentDataType.STRING);
        entity.getPersistentDataContainer().set(Keys.DATAPOINT_KEY, PersistentDataType.STRING, dataPointName);
        final String teamId = itemStack.getItemMeta().getPersistentDataContainer().get(Keys.TEAM_KEY, PersistentDataType.STRING);
        if (teamId == null) {
            entity.customName(MiniMessage.miniMessage().deserialize("<red>" + dataPointName));
            return;
        }
        entity.getPersistentDataContainer().set(Keys.TEAM_KEY, PersistentDataType.STRING, teamId);
        final Team team = Team.getExistingOrCreate(teamId);
        entity.customName(MiniMessage.miniMessage().deserialize(team.getDisplayName().toUpperCase() + " <red>" + dataPointName));
        entity.setCustomNameVisible(true);
        entity.getEquipment().setHelmet(ItemBuilder.create(Material.LEATHER_HELMET).color(team.getHelmetColor()).build());
    }

    @EventHandler
    private static void onArmorStandClick(final PlayerInteractAtEntityEvent event) throws IOException {
        if (!(event.getRightClicked() instanceof final ArmorStand clickedEntity)) {
            return;
        }

        if (MapSettingsManager.getMapSettings(clickedEntity.getWorld()).isEmpty()) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(ChatColor.RED + "Oh no! You have not yet set up /mapsettings!");
            return;
        }

        if (clickedEntity.getPersistentDataContainer().has(Keys.DATAPOINT_KEY)) {
            final DataPointType dataPointType = DataPointType.valueOf(clickedEntity.getPersistentDataContainer().get(Keys.DATAPOINT_KEY, PersistentDataType.STRING));
            Window.builder().setViewer(event.getPlayer()).setUpperGui(new ModifyMenu(clickedEntity, dataPointType).getGui()).build().open();
            return;
        }

        Window.builder().setUpperGui(new DataPointMenu(clickedEntity).getGui()).setTitle("Datapoint Menu").build(event.getPlayer()).open();
    }
}
