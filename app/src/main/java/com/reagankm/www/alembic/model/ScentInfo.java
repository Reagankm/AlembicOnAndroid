package com.reagankm.www.alembic.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores data about a scent including its name,
 * ID, rating and ingredient list.
 *
 * @author Reagan Middlebrook
 * @version Phase 2
 */
public class ScentInfo implements Comparable {

    /** The scent's ID. */
    private String id;

    /** The scent's name. */
    private String name;

    /** The scent's rating. */
    private float rating;

    /** The scent's ingredient list. */
    private List<String> ingredientList;

    /**
     * Constructs a ScentInfo with the given name and ID.
     *
     * @param id the id
     * @param name the name
     */
    public ScentInfo(String id, String name) {
        this.id = id;
        this.name = name;
        ingredientList = new ArrayList<>();
        rating = 0;
    }

    /**
     * Constructs a ScentInfo with the given ID, name, and rating.
     *
     * @param id the id
     * @param name the name
     * @param rating the rating
     */
    public ScentInfo(String id, String name, float rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        ingredientList = new ArrayList<>();
    }

    /**
     * Constructs a ScentInfo with the given ID, name, rating, and
     * ingredient list.
     * @param id the id
     * @param name the name
     * @param rating the rating
     * @param ingreds the ingredient list
     */
    public ScentInfo(String id, String name, float rating, List<String> ingreds) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        ingredientList = new ArrayList<>(ingreds);
    }

    /**
     * Gets a copy of the ingredient list.
     *
     * @return a copy of the list
     */
    public List<String> getIngredientList() {
        return new ArrayList<>(ingredientList);
    }

    /**
     * Determines whether two ScentInfo objects are equal to each other (which
     * is the case if they have the same name and ID. It is not necessary to have
     * the same rating.)
     *
     * @param o the object to check for equality
     * @return true if they are equal, otherwise false
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o instanceof ScentInfo) {
            ScentInfo other = (ScentInfo) o;

            if (other.name.equals(this.name) && other.id.equals(this.id)) {
                return true;
            }
        }

        return false;

    }

    /**
     * Generates a hash code based off the name and id.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return name.hashCode() + id.hashCode();
    }

    /**
     * Compares two ScentInfo objects for ordering (based on Name and then ID).
     *
     * @param o the other object
     * @return less than 0 if this object comes before the other, 0 if they are equal,
     * otherwise a value greater than 0
     *
     */
    @Override
    public int compareTo(Object o){

        ScentInfo other = (ScentInfo) o;

        int result = name.compareTo(other.name);

        if (result == 0) {
            result = id.compareTo(other.id);
        }

        return result;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the rating.
     *
     * @return the rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * Returns a String representation of the object.
     * @return the string representation
     */
    @Override
    public String toString() {
        return name;
    }
}
