package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.faizikhwan.recipeapp.Database.DatabaseHelper;
import com.faizikhwan.recipeapp.Helper.XMLParser;
import com.faizikhwan.recipeapp.Model.Recipe;
import com.faizikhwan.recipeapp.Model.RecipeType;
import com.faizikhwan.recipeapp.R;

import java.util.ArrayList;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Constant
    private final String TAG = "EditRecipeActivity";

    // Interface component
    private EditText recipeNameET;
    private EditText recipeIngredientET;
    private EditText recipeStepET;
    private Spinner recipeTypeSpinner;
    private Button editButton;

    // Variable
    private Recipe recipe;
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
        setContentView(R.layout.activity_edit_recipe);

        setupActionBar();
        getInformation();
        initComponent();
        setOnClickListener();
        setOnItemSelectedListener();
        populateSpinner();
        displayCurrentInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                recipeName = recipeNameET.getText().toString();
                recipeIngredient = recipeIngredientET.getText().toString();
                recipeStep = recipeStepET.getText().toString();
                if (recipeName.isEmpty() || recipeIngredient.isEmpty() || recipeStep.isEmpty()) {
                    Toast.makeText(this, "Must fill all detail", Toast.LENGTH_SHORT).show();
                } else {
                    updateToDatabase();
                    Toast.makeText(this, "Edit success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
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
        recipeNameET = findViewById(R.id.recipeNameET);
        recipeIngredientET = findViewById(R.id.recipeIngredientET);
        recipeStepET = findViewById(R.id.recipeStepET);
        recipeTypeSpinner = findViewById(R.id.recipeTypeSpinner);
        editButton = findViewById(R.id.editButton);
    }

    private void setOnClickListener() {
        editButton.setOnClickListener(this);
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

    private void updateToDatabase() {
        myDB = new DatabaseHelper(this);
        myDB.updateDataRecipe(String.valueOf(recipe.getId()), recipeName, recipeIngredient, recipeStep, recipeTypeChoice);
    }

    private void displayCurrentInformation() {
        recipeNameET.setText(recipe.getTitle());
        recipeIngredientET.setText(recipe.getIngredient());
        recipeStepET.setText(recipe.getStep());
        recipeTypeSpinner.setSelection(getIndex(recipeTypeSpinner, recipe.getType()));
    }

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i = 0; i<spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }
}
