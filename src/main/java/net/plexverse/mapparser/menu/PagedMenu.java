package net.plexverse.mapparser.menu;

import net.plexverse.mapparser.menu.buttons.ExitButton;
import net.plexverse.mapparser.menu.buttons.ForwardButton;
import net.plexverse.mapparser.menu.buttons.PreviousButton;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.Markers;
import xyz.xenondevs.invui.gui.Structure;
import xyz.xenondevs.invui.item.Item;

import java.util.ArrayList;
import java.util.List;

public class PagedMenu {

    private PagedGui gui;
    private List<Item> content = new ArrayList<>();

    public PagedMenu(@NotNull List<Item> pool) {
        this.content = pool;
        buildGui();
    }

    private void buildGui() {
        this.gui = PagedGui.ofItems(new Structure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < - > # # #")
                        .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                        .addIngredient('<', new PreviousButton())
                        .addIngredient('-', new ExitButton())
                        .addIngredient('>', new ForwardButton()), content);
    }

    public PagedGui getGui() {
        return gui;
    }

    public void setContent(List<Item> items) {
        this.content = items;
        buildGui();
    }
}