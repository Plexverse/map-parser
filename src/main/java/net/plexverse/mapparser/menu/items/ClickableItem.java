package net.plexverse.mapparser.menu.items;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.Click;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.AbstractItem;

import java.util.function.Function;

public class ClickableItem extends AbstractItem {

    private final ItemProvider itemProvider;
    private final Function<Player, Void> function;

    public ClickableItem(final ItemProvider itemProvider, Function<Player, Void> function) {
        this.itemProvider = itemProvider;
        this.function = function;
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        return itemProvider;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        function.apply(player);
        player.closeInventory();
    }
}
