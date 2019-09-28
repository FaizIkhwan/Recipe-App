package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.faizikhwan.recipeapp.Database.DatabaseHelper;
import com.faizikhwan.recipeapp.Helper.XMLParser;
import com.faizikhwan.recipeapp.Model.RecipeType;
import com.faizikhwan.recipeapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class AddRecipeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Constant
    private final String TAG = "AddRecipeActivity";
    private static final int PICK_IMAGE = 100;

    // Interface component
    private EditText recipeNameET;
    private EditText recipeIngredientET;
    private EditText recipeStepET;
    private Spinner recipeTypeSpinner;
    private Button addButton;
    private Button addImageButton;
    private ImageView recipeImageIV;

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

        setupActionBar();
        initComponent();
        recipeImageIV.setVisibility(View.GONE);
        setOnClickListener();
        setOnItemSelectedListener();
        populateSpinner();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addButton:
                recipeName = recipeNameET.getText().toString().trim();
                recipeIngredient = recipeIngredientET.getText().toString().trim();
                recipeStep = recipeStepET.getText().toString().trim();
                if (recipeName.isEmpty() || recipeIngredient.isEmpty() || recipeStep.isEmpty() || recipeImageIV.getDrawable() == null) {
                    Toast.makeText(this, "Must fill all detail and image", Toast.LENGTH_SHORT).show();
                } else {
                    saveToDatabase();
                    Toast.makeText(this, "Add success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }
                break;
            case R.id.addImageButton:
                openGallery();
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

    private void initComponent() {
        recipeNameET = findViewById(R.id.recipeNameET);
        recipeIngredientET = findViewById(R.id.recipeIngredientET);
        recipeStepET = findViewById(R.id.recipeStepET);
        recipeTypeSpinner = findViewById(R.id.recipeTypeSpinner);
        addButton = findViewById(R.id.addButton);
        addImageButton = findViewById(R.id.addImageButton);
        recipeImageIV = findViewById(R.id.recipeImageIV);
    }

    private void setOnClickListener() {
        addButton.setOnClickListener(this);
        addImageButton.setOnClickListener(this);
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
        try {
            myDB = new DatabaseHelper(this);
            myDB.insertDataRecipe(recipeName, recipeIngredient, recipeStep, recipeTypeChoice, getImageViewByte(recipeImageIV));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            recipeImageIV.setVisibility(View.VISIBLE);
            recipeImageIV.setImageURI(imageUri);
        }
    }

    private byte[] getImageViewByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
