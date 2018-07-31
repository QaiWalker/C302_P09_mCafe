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
import android.widget.Toast;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private EditText etLoginID, etPassword;
    private Button btnSubmit;
    private String loginId, apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginID = findViewById(R.id.editTextLoginID);
        etPassword = findViewById(R.id.editTextPassword);
        btnSubmit = findViewById(R.id.buttonSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username  = etLoginID.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.equalsIgnoreCase("")){
                    Toast.makeText(LoginActivity.this, "Login Failed. Please enter username.", Toast.LENGTH_SHORT).show();
                }
                else if (password.equalsIgnoreCase("")){
                    Toast.makeText(LoginActivity.this, "Login Failed. Please enter password.", Toast.LENGTH_SHORT).show();
                } else {
                    //Code for doLogin
                    HttpRequest request = new HttpRequest
                            ("https://literary-tourist.000webhostapp.com/doLogin.php");
                    request.setOnHttpResponseListener(mHttpResponseListener);
                    request.setMethod("POST");
                    request.addData("username", etLoginID.getText().toString());
                    request.addData("password", etPassword.getText().toString());
                    request.execute();
                }
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
                        JSONObject result = new JSONObject(response);
                        Boolean authenticated = result.getBoolean("authenticated");
                        if (authenticated == true){
                            String loginId = result.getString("id");
                            String apikey = result.getString("apikey");

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("loginId", loginId);
                            editor.putString("apikey", apikey);
                            editor.commit();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }

                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };
}
