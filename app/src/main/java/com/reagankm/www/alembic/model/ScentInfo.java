package com.reagankm.www.alembic.model;

/**
 * Created by reagan on 11/16/15.
 */
/**
 * A dummy item representing a piece of content.
 */
public class ScentInfo implements Comparable {
    private String id;
    private String name;
    private float rating;

    public ScentInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public ScentInfo(String id, String name, float rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
    }

    public int compareTo(Object o){

        ScentInfo other = (ScentInfo) o;

        int result = name.compareTo(other.name);

        if (result == 0) {
            result = id.compareTo(other.id);
        }

        return result;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return name;
    }
}
