package com.reagankm.www.alembic.model;

/**
 * Created by reagan on 11/16/15.
 */
/**
 * A dummy item representing a piece of content.
 */
public class ScentInfo implements Comparable {
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
