package com.reagankm.www.alembic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 */
public class Scent {

    /**
     * An array of ScentInfo items.
     */
    public static List<ScentInfo> ITEMS = new ArrayList<>();

    /**
     * A map of scent items, by ID.
     */
    public static Map<String, ScentInfo> ITEM_MAP = new HashMap<>();

    /**
     * Adds a ScentInfo item to the list and map.
     * @param item the item ot add
     */
    public static void addItem(ScentInfo item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }

    /**
     * Sets the Item list and map to the contents of the given list of items.
     * @param newList the new list of items
     */
    public static void setAllItems(List<ScentInfo> newList) {
        ITEMS.clear();
        ITEM_MAP.clear();

        for (ScentInfo s : newList) {
            addItem(s);
        }

        sortItems();
    }

    /**
     * Returns the list of scent info items.
     *
     * @return the list
     */
    public static List<ScentInfo> getAllItems() {
        return ITEMS;
    }

    /**
     * Sorts the list of items.
     */
    public static void sortItems() {
        Collections.sort(ITEMS);
    }


}

