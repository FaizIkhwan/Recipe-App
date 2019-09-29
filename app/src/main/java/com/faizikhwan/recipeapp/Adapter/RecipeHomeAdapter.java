package com.faizikhwan.recipeapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.faizikhwan.recipeapp.Activity.RecipeDetailActivity;
import com.faizikhwan.recipeapp.Model.Recipe;
import com.faizikhwan.recipeapp.R;

import java.util.List;

public class RecipeHomeAdapter extends RecyclerView.Adapter<RecipeHomeAdapter.ViewHolder>  {

    //Log
    private static String TAG = "RecipeHomeAdapter";

    private Context mContext;
    private List<Recipe> mRecipe;

    public RecipeHomeAdapter(Context mContext, List<Recipe> mRecipe){
        this.mRecipe = mRecipe;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_recipe_home, parent, false);
        return new RecipeHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Recipe recipe = mRecipe.get(position);
        holder.recipeNameTV.setText(recipe.getTitle());
        holder.recipeTypeTV.setText(recipe.getType());

        byte[] image = recipe.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.recipeImageIV.setImageBitmap(bitmap);

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra(mContext.getResources().getString(R.string.recipe_id), String.valueOf(recipe.getId()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipe.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView recipeNameTV;
        private TextView recipeTypeTV;
        private ImageView recipeImageIV;
        private Button viewButton;

        ViewHolder(View itemView) {
            super(itemView);

            recipeNameTV = itemView.findViewById(R.id.recipeNameTV);
            recipeTypeTV = itemView.findViewById(R.id.recipeTypeTV);
            recipeImageIV = itemView.findViewById(R.id.recipeImageIV);
            viewButton = itemView.findViewById(R.id.viewButton);
        }
    }
}
