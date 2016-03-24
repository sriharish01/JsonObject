package com.example.sriharish.jsonobject;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;

        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.app.ListActivity;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Color;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.ListAdapter;
        import android.widget.ListView;
        import android.widget.SimpleAdapter;
        import android.widget.TextView;

public class MainActivity extends ListActivity {

    private ProgressDialog pDialog;
    private static String url = "http://www.speechify.in/internship/android_task.php";

    private static final String TAG_RECIPE_DATA = "recipe_data";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_INGREDIENT_DATA = "ingredient_data";
    private static final String TAG_INGREDIENT_ID = "ingregient_id";
    private static final String TAG_INGREDIENT_NAME = "ingredient_name";
    JSONArray recipe_data = null;

    HashMap<String, JSONObject > recipe_datas = new HashMap<String,JSONObject>();
    List<String> recipe =new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        TextView text =(TextView) findViewById(R.id.textView);
        text.getBackground().setAlpha(80);

        final ListView lv = getListView();
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String name =  (String) lv.getItemAtPosition(position);

                // Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        Main2Activity.class);
                in.putExtra(TAG_NAME, name);
                JSONObject r= recipe_datas.get(name);

                in.putExtra(name,r.toString());
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

            }
        });

        new GetRecipes().execute();
    }

    private class GetRecipes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    recipe_data = jsonObj.getJSONArray(TAG_RECIPE_DATA);

                    // looping through All Contacts
                    for (int i = 0; i < recipe_data.length(); i++) {
                        JSONObject r = recipe_data.getJSONObject(i);

                        String name = r.getString(TAG_NAME);
                        Log.d("JsonObjext",name);

                        recipe_datas.put(name,r);
                        recipe.add(name);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            ListAdapter adapter = new CustomListAdapter(MainActivity.this,R.layout.custom_list,recipe);

            setListAdapter(adapter);
        }

    }

}
