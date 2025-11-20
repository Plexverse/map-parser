package net.plexverse.mapparser.menu;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.plexverse.mapparser.MapParser;
import net.plexverse.mapparser.constant.Keys;
import net.plexverse.mapparser.enums.DataPointType;
import net.plexverse.mapparser.enums.GameType;
import net.plexverse.mapparser.mapsettings.MapSettingsManager;
import net.plexverse.mapparser.menu.items.ClickableItem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemBuilder;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataPointMenu extends PagedMenu {

    public DataPointMenu(final LivingEntity armorStandEntity) throws IOException {
        super(DataPointMenu.itemList(armorStandEntity, MapSettingsManager.getMapSettings(armorStandEntity.getWorld()).get().getGameType()));
    }

    public static List<Item> itemList(final LivingEntity armorStandEntity, final GameType gameType) {
        final List<Item> items = new ArrayList<>();
        DataPointType.values().stream().filter(dataPointType -> gameType.getDataPointTypeList().contains(dataPointType)).toList().forEach(dataPointType -> {
            final ItemBuilder itemBuilder = new ItemBuilder(dataPointType.getMaterial());
            itemBuilder.setLegacyName("§d§l" + dataPointType.getMenuName());
            itemBuilder.addLegacyLoreLines("§7", "§7Click to set this datapoints as a §f" + dataPointType.getMenuName());
            final ClickableItem simpleItem = new ClickableItem(itemBuilder, (player) -> {
                DataPointMenu.defineEntity(armorStandEntity, player, dataPointType, true);
                return null;
            });
            items.add(simpleItem);
        });
        return items;
    }

    public static void defineEntity(final LivingEntity armorStandEntity, final Player player, final DataPointType dataPointType, final boolean showUI) {
        armorStandEntity.setPersistent(true);
        armorStandEntity.getPersistentDataContainer().set(Keys.DATAPOINT_KEY, PersistentDataType.STRING, dataPointType.name());
        armorStandEntity.setCustomNameVisible(true);
        armorStandEntity.customName(MiniMessage.miniMessage().deserialize("<red>" + dataPointType.name()));
        player.sendMessage(MiniMessage.miniMessage().deserialize("<red>You have defined this armor stand as a " + dataPointType.name()));

        player.getInventory().close();

        if (showUI) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Window.builder()
                            .setUpperGui(new ModifyMenu(armorStandEntity, dataPointType).getGui())
                            .setViewer(player)
                            .build()
                            .open();
                }
            }.runTaskLater(MapParser.getMapParser(), 1L);

        }

    }
}
