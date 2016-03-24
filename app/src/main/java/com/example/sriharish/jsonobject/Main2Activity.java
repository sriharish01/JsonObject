package com.example.sriharish.jsonobject;


import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends ListActivity {

    private static final String TAG_RECIPE_DATA = "recipe_data";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_INGREDIENT_DATA = "ingredient_data";
    private static final String TAG_INGREDIENT_ID = "ingregient_id";
    private static final String TAG_INGREDIENT_NAME = "ingredient_name";
    JSONObject recipe;
    JSONObject ingredient;
    List ingredients = new ArrayList<String>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);

        Intent in = getIntent();

        String name = in.getStringExtra(TAG_NAME);
        String jsonstring = in.getStringExtra(name);

        try {
            recipe =new JSONObject(jsonstring);
            Log.d("Response: ", "> " + jsonstring);
        } catch (JSONException e) {
        }

        try {
            JSONArray ingredient_data = recipe.getJSONArray(TAG_INGREDIENT_DATA);
            for (int i=0; i<ingredient_data.length();i++)
            {
                ingredient = ingredient_data.getJSONObject(i);
                String ingredient_name= ingredient.getString(TAG_INGREDIENT_NAME);
                ingredients.add(ingredient_name);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView names =(TextView) findViewById(R.id.textView2);
        names.setText(name);
        ListAdapter adapter = new CustomListAdapter(this,R.layout.custom_list,ingredients);
        setListAdapter(adapter);
    }
}
