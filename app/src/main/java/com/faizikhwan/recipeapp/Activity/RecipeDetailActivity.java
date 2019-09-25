package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.faizikhwan.recipeapp.R;

public class RecipeDetailActivity extends AppCompatActivity {

    // Constant
    private final String TAG = "RecipeDetailActivity";

    // Interface component
    private ImageView recipeImageIV;
    private TextView recipeNameTV;
    private TextView recipeTypeTV;
    private TextView recipeIngredientTV;
    private TextView recipeStepTV;

    // Variable
    private String recipeName;
    private String recipeType;
    private String recipeIngredient;
    private String recipeStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        getInformation();
        initComponent();
        displayInformation();
    }

    private void getInformation() {
        Intent intent = getIntent();
        recipeName = intent.getStringExtra("name");
        recipeType = intent.getStringExtra("type");
        recipeIngredient = intent.getStringExtra("ingredient");
        recipeStep = intent.getStringExtra("step");
    }

    private void initComponent() {
        recipeImageIV = findViewById(R.id.recipeImageIV);
        recipeNameTV = findViewById(R.id.recipeNameTV);
        recipeTypeTV = findViewById(R.id.recipeTypeTV);
        recipeIngredientTV = findViewById(R.id.recipeIngredientTV);
        recipeStepTV = findViewById(R.id.recipeStepTV);
    }

    private void displayInformation() {
        recipeNameTV.setText(recipeName);
        recipeTypeTV.setText(recipeType);
        recipeIngredientTV.setText(recipeIngredient);
        recipeStepTV.setText(recipeStep);
    }
}
