package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView lvCategories;
    private ArrayList<MenuCategory> alCategories;
    private ArrayAdapter<MenuCategory> aaCategories;

    private String loginId;
    private String apikey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCategories = (ListView)findViewById(R.id.listViewCategories);
        alCategories = new ArrayList<MenuCategory>();
        aaCategories = new ArrayAdapter<MenuCategory>(this, android.R.layout.simple_list_item_1, alCategories);
        lvCategories.setAdapter(aaCategories);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginId = prefs.getString("loginId", "");
        apikey = prefs.getString("apikey", "");

        if (loginId.equalsIgnoreCase("") || apikey.equalsIgnoreCase("")) {
            // redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        //call getMenuCategories web service so that we can display list of Categories
        HttpRequest request = new HttpRequest
                ("https://literary-tourist.000webhostapp.com/getMenuCategories.php");
        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("POST");
        request.addData("loginId", loginId);
        request.addData("apikey", apikey);
        request.execute();

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MenuCategory cat = alCategories.get(position);
                Intent intent = new Intent(MainActivity.this, DisplayMenuItemsActivity.class);
                intent.putExtra("MenuCategory", cat);
                startActivityForResult(intent, 1);
            }
        });


    }
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){
                    // process response here
                    try {
                        Log.i("JSON Results", response);
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String categoryId = jsonObj.getString("menu_item_category_id");
                            String description = jsonObj.getString("menu_item_category_description");

                            MenuCategory menuCategory = new MenuCategory(categoryId, description);
                            alCategories.add(menuCategory);
                        }
                        aaCategories.notifyDataSetChanged();

                    } catch(Exception e){
                        e.printStackTrace();
                    }


                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_add) {


            // Put the following into intent:- loginId, apikey
            Intent intent = new Intent(MainActivity.this, CreateMenuActivity.class);
            intent.putExtra("loginId", loginId);
            intent.putExtra("apikey", apikey);
            startActivity(intent);

        }
        else if (id == R.id.menu_logout){
            //Logout
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
