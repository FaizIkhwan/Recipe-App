package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.faizikhwan.recipeapp.Adapter.RecipeHomeAdapter;
import com.faizikhwan.recipeapp.Database.DatabaseHelper;
import com.faizikhwan.recipeapp.Helper.XMLParser;
import com.faizikhwan.recipeapp.Model.Recipe;
import com.faizikhwan.recipeapp.Model.RecipeType;
import com.faizikhwan.recipeapp.R;

import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Constant
    private final String TAG = "AddRecipeActivity";

    // Interface component
    private EditText recipeNameET;
    private EditText recipeIngredientET;
    private EditText recipeStepET;
    private Spinner recipeTypeSpinner;
    private Button addButton;

    // Variable
    private ArrayList<RecipeType> recipeTypes;
    private String recipeName;
    private String recipeIngredient;
    private String recipeStep;
    private String recipeTypeChoice;

    //Database
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        initComponent();
        setOnClickListener();
        setOnItemSelectedListener();
        populateSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                recipeName = recipeNameET.getText().toString();
                recipeIngredient = recipeIngredientET.getText().toString();
                recipeStep = recipeStepET.getText().toString();
                if (recipeName.isEmpty() || recipeIngredient.isEmpty() || recipeStep.isEmpty()) {
                    Toast.makeText(this, "Must fill all detail", Toast.LENGTH_LONG).show();
                } else {
                    saveToDatabase();
                    startActivity(new Intent(this, HomeActivity.class));
                }
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
        recipeNameET = findViewById(R.id.recipeNameET);
        recipeIngredientET = findViewById(R.id.recipeIngredientET);
        recipeStepET = findViewById(R.id.recipeStepET);
        recipeTypeSpinner = findViewById(R.id.recipeTypeSpinner);
        addButton = findViewById(R.id.addButton);
    }

    private void setOnClickListener() {
        addButton.setOnClickListener(this);
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

    private void saveToDatabase() {
        myDB = new DatabaseHelper(this);
        myDB.insertDataRecipe(recipeName, recipeIngredient, recipeStep, recipeTypeChoice);
    }
}
