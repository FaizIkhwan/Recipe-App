package com.faizikhwan.recipeapp.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.faizikhwan.recipeapp.Model.Recipe;
import com.faizikhwan.recipeapp.Model.RecipeType;
import com.faizikhwan.recipeapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditRecipeActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    // Constant
    private final String TAG = "EditRecipeActivity";
    private static final int PICK_IMAGE = 100;

    // Interface component
    private EditText recipeNameET;
    private EditText recipeIngredientET;
    private EditText recipeStepET;
    private Spinner recipeTypeSpinner;
    private Button editButton;
    private Button changeImageButton;
    private ImageView recipeImageIV;

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
        recipeImageIV.setVisibility(View.GONE);
        setOnClickListener();
        setOnItemSelectedListener();
        populateSpinner();
        displayCurrentInformation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editButton:
                Log.d(TAG, "onClick editButton");
                recipeName = recipeNameET.getText().toString().trim();
                recipeIngredient = recipeIngredientET.getText().toString().trim();
                recipeStep = recipeStepET.getText().toString().trim();
                if (recipeName.isEmpty() || recipeIngredient.isEmpty() || recipeStep.isEmpty()) {
                    Toast.makeText(this, "Must fill all detail", Toast.LENGTH_SHORT).show();
                } else {
                    updateToDatabase();
                    Toast.makeText(this, "Edit success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                }
                break;
            case R.id.changeImageButton:
                Log.d(TAG, "onClick changeImageButton");
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

    private void getInformation() {
        Intent intent = getIntent();
        String recipeID = intent.getStringExtra("recipeID");
        recipe = getInformationFromDatabase(recipeID);
    }

    private Recipe getInformationFromDatabase(String recipeID) {
        Recipe recipeRes = new Recipe();
        myDB = new DatabaseHelper(this);
        Cursor res = myDB.getDataFromRecipeWithID(recipeID);
        if (res != null && res.moveToFirst()) {
            do {
                int id = res.getInt(res.getColumnIndex("ID"));
                String title = res.getString(res.getColumnIndex("TITLE"));
                String ingredient = res.getString(res.getColumnIndex("INGREDIENT"));
                String step = res.getString(res.getColumnIndex("STEP"));
                String type = res.getString(res.getColumnIndex("TYPE"));
                byte[] image = res.getBlob(res.getColumnIndex("IMAGE"));

                recipeRes = new Recipe(id, title, ingredient, step, type, image);
            } while (res.moveToNext());
        }
        return recipeRes;
    }

    private void initComponent() {
        recipeNameET = findViewById(R.id.recipeNameET);
        recipeIngredientET = findViewById(R.id.recipeIngredientET);
        recipeStepET = findViewById(R.id.recipeStepET);
        recipeTypeSpinner = findViewById(R.id.recipeTypeSpinner);
        editButton = findViewById(R.id.editButton);
        changeImageButton = findViewById(R.id.changeImageButton);
        recipeImageIV = findViewById(R.id.recipeImageIV);
    }

    private void setOnClickListener() {
        editButton.setOnClickListener(this);
        changeImageButton.setOnClickListener(this);
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
        myDB.updateDataRecipe(String.valueOf(recipe.getId()), recipeName, recipeIngredient, recipeStep, recipeTypeChoice, getImageViewByte(recipeImageIV));
    }

    private void displayCurrentInformation() {
        recipeNameET.setText(recipe.getTitle());
        recipeIngredientET.setText(recipe.getIngredient());
        recipeStepET.setText(recipe.getStep());
        recipeTypeSpinner.setSelection(getIndex(recipeTypeSpinner, recipe.getType()));

        recipeImageIV.setVisibility(View.VISIBLE);
        byte[] image = recipe.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        recipeImageIV.setImageBitmap(bitmap);
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

    private void openGallery() {
        Log.d(TAG, "openGallery");
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
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
