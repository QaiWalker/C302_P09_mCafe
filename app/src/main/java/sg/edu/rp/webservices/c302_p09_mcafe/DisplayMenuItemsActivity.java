package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class DisplayMenuItemsActivity extends AppCompatActivity {

    private ListView lvDisplayMenuItems;
    private ArrayList<MenuItems> alMenuItems;
    private ArrayAdapter<MenuItems> aaMenuItems;

    private String loginId;
    private String apikey;
    private String categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_menu_items);

        Intent i = getIntent();
        MenuCategory menuCategory = (MenuCategory)i.getSerializableExtra("MenuCategory");
        
        lvDisplayMenuItems = (ListView)findViewById(R.id.listViewMenuItems);
        alMenuItems = new ArrayList<MenuItems>();
        aaMenuItems = new ArrayAdapter<MenuItems>(this, android.R.layout.simple_list_item_1, alMenuItems);
        lvDisplayMenuItems.setAdapter(aaMenuItems);

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
                ("http://10.0.2.2/C302_P09_mCafe/getMenuByItemId.php?categoryId=" + menuCategory.getCategoryId());
        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("POST");
        request.addData("loginId", loginId);
        request.addData("apikey", apikey);
        request.addData("categoryId", menuCategory.getCategoryId());
        request.execute();

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

                            String itemId = jsonObj.getString("menu_item_id");
                            String categoryId = jsonObj.getString("menu_item_category_id");
                            String description = jsonObj.getString("menu_item_description");
                            double unitPrice = jsonObj.getDouble("menu_item_unit_price");

                            MenuItems menuItems = new MenuItems(itemId, categoryId, description, unitPrice);
                            alMenuItems.add(menuItems);
                        }
                        aaMenuItems.notifyDataSetChanged();

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
            Intent intent = new Intent(DisplayMenuItemsActivity.this, CreateMenuActivity.class);
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
