package com.reagankm.www.alembic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class Scent {

    /**
     * An array of user items.
     */
    public static List<ScentInfo> ITEMS = new ArrayList<>();

    /**
     * A map of user items, by email.
     */
    public static Map<String, ScentInfo> ITEM_MAP = new HashMap<>();


    public static void addItem(ScentInfo item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static ScentInfo getItem(int position) {
        return ITEMS.get(position);
    }

    public static List<ScentInfo> getAllItems() {
        return ITEMS;
    }

    public static void sortItems() {
        Collections.sort(ITEMS);
    }


}

