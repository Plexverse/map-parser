package net.plexverse.mapparser.menu.buttons;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.ItemWrapper;
import xyz.xenondevs.invui.item.AbstractPagedGuiBoundItem;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import xyz.xenondevs.invui.Click;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.PagedGui;

public class PreviousButton extends AbstractPagedGuiBoundItem {
    public PreviousButton() {
    }

    @Override
    public ItemProvider getItemProvider(Player player) {
        PagedGui<?> gui = getGui();
        if (gui != null && gui.getPage() > 0) {
            final ItemStack itemStack = new ItemStack(Material.ARROW);
            final ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName("Previous Page");
            itemStack.setItemMeta(itemMeta);
            return new ItemWrapper(itemStack);
        }
        return new ItemWrapper(new ItemStack(Material.AIR));
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull Click click) {
        PagedGui<?> gui = getGui();
        if (gui != null && gui.getPage() > 0) {
            gui.setPage(gui.getPage() - 1);
        }
    }
}