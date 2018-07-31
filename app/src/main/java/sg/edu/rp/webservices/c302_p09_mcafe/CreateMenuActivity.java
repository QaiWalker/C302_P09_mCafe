package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class CreateMenuActivity extends AppCompatActivity {

    Button btnAdd;
    TextView tvTitle, tvItem, tvPrice;
    EditText etItem, etPrice;

    private String loginId;
    private String apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_menu);

        tvTitle = findViewById(R.id.textViewTitle);
        tvItem = findViewById(R.id.textViewItem);
        tvPrice = findViewById(R.id.textViewPrice);

        etItem = findViewById(R.id.editTextItem);
        etPrice = findViewById(R.id.editTextPrice);

        btnAdd = findViewById(R.id.btnAdd);

        Intent i = getIntent();
        final String categoryId = i.getStringExtra("categoryId");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        loginId = prefs.getString("loginId", "");
        apikey = prefs.getString("apikey", "");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginId.equalsIgnoreCase("") || apikey.equalsIgnoreCase("")) {
                    // redirect back to login screen
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
                HttpRequest request = new HttpRequest
                        ("http://10.0.2.2/C302_P09_mCafe/addMenuItem.php");
                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("firstName", etItem.getText().toString());
                request.addData("lastName", etPrice.getText().toString());
                request.addData("loginId", loginId);
                request.addData("apikey", apikey);
                request.addData("categoryId", categoryId);
                request.execute();
            }
        });
    }
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);
                        Toast.makeText(getApplicationContext(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
}
