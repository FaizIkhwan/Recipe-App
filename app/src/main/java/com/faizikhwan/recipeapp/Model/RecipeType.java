package com.faizikhwan.recipeapp.Model;

public class RecipeType {

    private String type;

    public RecipeType() {}

    public RecipeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RecipeType{" +
                "type='" + type + '\'' +
                '}';
    }
}
