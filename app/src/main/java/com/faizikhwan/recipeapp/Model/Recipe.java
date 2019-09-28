package com.faizikhwan.recipeapp.Model;

import java.io.Serializable;
import java.util.Arrays;

public class Recipe implements Serializable {

    private int id;
    private String title;
    private String ingredient;
    private String step;
    private String type;
    private byte[] image;

    public Recipe() {}

    public Recipe(int id, String title, String ingredient, String step, String type, byte[] image) {
        this.id = id;
        this.title = title;
        this.ingredient = ingredient;
        this.step = step;
        this.type = type;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", ingredient='" + ingredient + '\'' +
                ", step='" + step + '\'' +
                ", type='" + type + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
