package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.faizikhwan.recipeapp.Adapter.RecipeHomeAdapter;
import com.faizikhwan.recipeapp.Database.DatabaseHelper;
import com.faizikhwan.recipeapp.Helper.XMLParser;
import com.faizikhwan.recipeapp.Model.Recipe;
import com.faizikhwan.recipeapp.Model.RecipeType;
import com.faizikhwan.recipeapp.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Constant
    private final String TAG = "HomeActivity";

    // Interface component
    private RecyclerView viewListRecipeRecycler;
    private Spinner recipeTypeSpinner;
    private Button filterButton;

    // Adapter
    private RecipeHomeAdapter recipeHomeAdapter;

    // Variable
    private ArrayList<RecipeType> recipeTypes;
    private List<Recipe> recipes;
    private String recipeTypeChoice;

    //Database
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponent();
        setOnClickListener();
        setOnItemSelectedListener();
        populateSpinner();
        getDataFromDatabase();
        setupRecyclerView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filterButton:
                Intent intent = new Intent(this, RecipeListActivity.class);
                intent.putExtra("type", recipeTypeChoice);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.recipeTypeSpinner:
                recipeTypeChoice = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initComponent() {
        viewListRecipeRecycler = findViewById(R.id.viewListRecipeRecycler);
        recipeTypeSpinner = findViewById(R.id.recipeTypeSpinner);
        filterButton = findViewById(R.id.filterButton);
    }

    private void setOnClickListener() {
        filterButton.setOnClickListener(this);
    }

    private void setOnItemSelectedListener() {
        recipeTypeSpinner.setOnItemSelectedListener(this);
    }

    private void populateSpinner() {
        recipeTypes = XMLParser.parseXMLRecipeTypes(this, "recipetypes.xml");
        List<String> types = new ArrayList<>();
        for(RecipeType recipe: recipeTypes) {
            types.add(recipe.getType());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, types);
        recipeTypeSpinner.setAdapter(adapter);
    }

    private void getDataFromDatabase() {
        recipes = new ArrayList<>();
        myDB = new DatabaseHelper(this);
        Cursor res = myDB.getDataFromRecipe();
        if (res != null && res.moveToFirst()) {
            do {
                int id = res.getInt(res.getColumnIndex("ID"));
                String title = res.getString(res.getColumnIndex("TITLE"));
                String ingredient = res.getString(res.getColumnIndex("INGREDIENT"));
                String step = res.getString(res.getColumnIndex("STEP"));
                String type = res.getString(res.getColumnIndex("TYPE"));

                recipes.add(new Recipe(id, title, ingredient, step, type));
            } while (res.moveToNext());
        } else {
            recipes = XMLParser.parseXMLRecipes(this, "recipes.xml");
            for (Recipe recipe: recipes) {
                Log.d(TAG, recipe.toString());
                myDB.insertDataRecipe(recipe.getTitle(), recipe.getIngredient(), recipe.getStep(), recipe.getType());
            }
        }
    }

    private void setupRecyclerView() {
        viewListRecipeRecycler.setHasFixedSize(true);
        viewListRecipeRecycler.setLayoutManager(new LinearLayoutManager(this));
        recipeHomeAdapter = new RecipeHomeAdapter(this, recipes);
        viewListRecipeRecycler.setAdapter(recipeHomeAdapter);
    }
}
