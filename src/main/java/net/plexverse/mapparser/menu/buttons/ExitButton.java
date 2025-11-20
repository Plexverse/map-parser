package net.plexverse.mapparser.menu.buttons;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.Click;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.ItemWrapper;
import xyz.xenondevs.invui.item.AbstractBoundItem;
import xyz.xenondevs.invui.item.ItemWrapper;

import java.util.Objects;

@Data
    public class ExitButton extends AbstractBoundItem {

        @Override
        public ItemProvider getItemProvider(Player player) {
            final ItemStack itemStack = new ItemStack(Material.BARRIER);
            final ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("Exit");
            itemStack.setItemMeta(itemMeta);
            return new ItemWrapper(itemStack);
        }

        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
            player.closeInventory();
        }
    }

