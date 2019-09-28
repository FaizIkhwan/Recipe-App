package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.faizikhwan.recipeapp.Adapter.RecipeHomeAdapter;
import com.faizikhwan.recipeapp.Database.DatabaseHelper;
import com.faizikhwan.recipeapp.Helper.XMLParser;
import com.faizikhwan.recipeapp.Model.Recipe;
import com.faizikhwan.recipeapp.R;

import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    // Constant
    private final String TAG = "RecipeListActivity";

    // Interface component
    private RecyclerView listOfRecipeRecyclerView;
    private TextView recipeTypeTV;

    // Adapter
    private RecipeHomeAdapter recipeHomeAdapter;

    // Variable
    private String recipeTypeChoice;
    private List<Recipe> recipes;

    //Database
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        getInformation();
        setupActionBar();
        initComponent();
        getDataFromDatabase();
        setupRecyclerView();

        recipeTypeTV.setText(recipeTypeChoice);

    }

    // Implement back click
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void getInformation() {
        Intent intent = getIntent();
        recipeTypeChoice = intent.getStringExtra("type");
    }

    private void setupActionBar() {
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button
    }

    private void initComponent() {
        listOfRecipeRecyclerView = findViewById(R.id.listOfRecipeRecyclerView);
        recipeTypeTV = findViewById(R.id.recipeTypeTV);
    }

    private void getDataFromDatabase() {
        recipes = new ArrayList<>();
        myDB = new DatabaseHelper(this);
        Cursor res = myDB.getDataFromRecipeWithType(recipeTypeChoice);
        if (res != null && res.moveToFirst()) {
            do {
                int id = res.getInt(res.getColumnIndex("ID"));
                String title = res.getString(res.getColumnIndex("TITLE"));
                String ingredient = res.getString(res.getColumnIndex("INGREDIENT"));
                String step = res.getString(res.getColumnIndex("STEP"));
                String type = res.getString(res.getColumnIndex("TYPE"));
                byte[] image = res.getBlob(res.getColumnIndex("IMAGE"));

                recipes.add(new Recipe(id, title, ingredient, step, type, image));
            } while (res.moveToNext());
        }
    }

    private void setupRecyclerView() {
        listOfRecipeRecyclerView.setHasFixedSize(true);
        listOfRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recipeHomeAdapter = new RecipeHomeAdapter(this, recipes);
        listOfRecipeRecyclerView.setAdapter(recipeHomeAdapter);
    }
}
