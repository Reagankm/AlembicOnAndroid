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

    public static void sortItems() {
        Collections.sort(ITEMS);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ScentInfo implements Comparable {
        public String id;
        public String name;

        public ScentInfo(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public int compareTo(Object o){

            ScentInfo other = (ScentInfo) o;

            int result = name.compareTo(other.name);

            if (result == 0) {
                result = id.compareTo(other.id);
            }

            return result;
        }




        @Override
        public String toString() {
            return name;
        }
    }
}

