package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                Intent intent = new Intent(this, EditRecipeActivity.class);
                intent.putExtra(getResources().getString(R.string.recipe_id), String.valueOf(recipe.getId()));
                startActivity(intent);
                break;
            case R.id.deleteButton:
                deleteRecipeFromDatabase();
                Toast.makeText(this, getResources().getString(R.string.toast_delete_success), Toast.LENGTH_SHORT).show();
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
        String recipeID = intent.getStringExtra(getResources().getString(R.string.recipe_id));
        recipe = getInformationFromDatabase(recipeID);
    }

    private Recipe getInformationFromDatabase(String recipeID) {
        Recipe recipeRes = new Recipe();
        myDB = new DatabaseHelper(this);
        Cursor res = myDB.getDataFromRecipeWithID(recipeID);
        if (res != null && res.moveToFirst()) {
            do {
                int id = res.getInt(res.getColumnIndex(getResources().getString(R.string.ID)));
                String title = res.getString(res.getColumnIndex(getResources().getString(R.string.TITLE)));
                String ingredient = res.getString(res.getColumnIndex(getResources().getString(R.string.INGREDIENT)));
                String step = res.getString(res.getColumnIndex(getResources().getString(R.string.STEP)));
                String type = res.getString(res.getColumnIndex(getResources().getString(R.string.TYPE)));
                byte[] image = res.getBlob(res.getColumnIndex(getResources().getString(R.string.IMAGE)));

                recipeRes = new Recipe(id, title, ingredient, step, type, image);
            } while (res.moveToNext());
        }
        return recipeRes;
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

        byte[] image = recipe.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        recipeImageIV.setImageBitmap(bitmap);
    }

    private void deleteRecipeFromDatabase() {
        myDB.deleteDataFromRecipe(recipe.getId());
    }
}
