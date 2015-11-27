package com.reagankm.www.alembic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by reagan on 11/16/15.
 */

public class ScentInfo implements Comparable {
    private String id;
    private String name;
    private float rating;
    private List<String> ingredientList;


    public ScentInfo(String id, String name) {
        this.id = id;
        this.name = name;
        ingredientList = new ArrayList<>();
        rating = 0;
    }


    public ScentInfo(String id, String name, float rating) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        ingredientList = new ArrayList<>();
    }

    public ScentInfo(String id, String name, float rating, List<String> ingreds) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        ingredientList = new ArrayList<>(ingreds);
    }

    public List<String> getIngredientList() {
        return new ArrayList<>(ingredientList);
    }

    public void setIngredientList(List<String> ingreds) {
        ingredientList = new ArrayList<>(ingreds);
    }

    public void addIngredient(String ingred) {
        ingredientList.add(ingred);
    }

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

    @Override
    public int hashCode() {
        return name.hashCode() + id.hashCode();
    }


    @Override
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
