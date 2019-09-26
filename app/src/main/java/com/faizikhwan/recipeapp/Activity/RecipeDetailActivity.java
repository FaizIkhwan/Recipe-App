package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.faizikhwan.recipeapp.Database.DatabaseHelper;
import com.faizikhwan.recipeapp.Model.Recipe;
import com.faizikhwan.recipeapp.R;

public class RecipeDetailActivity extends AppCompatActivity implements View.OnClickListener {

    // Constant
    private final String TAG = "RecipeDetailActivity";

    // Interface component
    private ImageView recipeImageIV;
    private TextView recipeNameTV;
    private TextView recipeTypeTV;
    private TextView recipeIngredientTV;
    private TextView recipeStepTV;
    private Button editButton;
    private Button deleteButton;

    // Variable
    private Recipe recipe;

    //Database
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        setupActionBar();
        getInformation();
        initComponent();
        setOnClickListener();
        displayInformation();

        myDB = new DatabaseHelper(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                Intent intent = new Intent(this, EditRecipeActivity.class);
                intent.putExtra("recipe", recipe);
                startActivity(intent);
                break;
            case R.id.deleteButton:
                deleteRecipeFromDatabase();
                Toast.makeText(this, "Delete success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }

    // Implement back click
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void setupActionBar() {
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
    }

    private void getInformation() {
        Intent intent = getIntent();
        recipe = (Recipe) intent.getSerializableExtra("recipe");
    }

    private void initComponent() {
        recipeImageIV = findViewById(R.id.recipeImageIV);
        recipeNameTV = findViewById(R.id.recipeNameTV);
        recipeTypeTV = findViewById(R.id.recipeTypeTV);
        recipeIngredientTV = findViewById(R.id.recipeIngredientTV);
        recipeStepTV = findViewById(R.id.recipeStepTV);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
    }

    private void setOnClickListener() {
        editButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
    }

    private void displayInformation() {
        recipeNameTV.setText(recipe.getTitle());
        recipeTypeTV.setText(recipe.getType());
        recipeIngredientTV.setText(recipe.getIngredient());
        recipeStepTV.setText(recipe.getStep());
    }

    private void deleteRecipeFromDatabase() {
        myDB.deleteDataFromRecipe(recipe.getId());
    }
}
