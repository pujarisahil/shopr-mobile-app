package shopr.com.shoprapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import shopr.com.shoprapp.R;
import shopr.com.shoprapp.utils.ShoprRestClient;
import shopr.com.shoprapp.utils.Utils;

/**
 * Created by Neil on 10/31/2016.
 *
 * @author Neil Allison
 */

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText usernameText;
    private TextInputEditText firstNameText;
    private TextInputEditText lastNameText;
    private TextInputEditText emailText;
    private TextInputEditText passwordText;
    private Button registerButton;
    private TextView loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        usernameText = (TextInputEditText) findViewById(R.id.input_username);
        firstNameText = (TextInputEditText) findViewById(R.id.input_first_name);
        lastNameText = (TextInputEditText) findViewById(R.id.input_last_name);
        emailText = (TextInputEditText) findViewById(R.id.input_email);
        passwordText = (TextInputEditText) findViewById(R.id.input_password);

        registerButton = (Button) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        loginLink = (TextView) findViewById(R.id.link_login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void register() {
        if (!validate()) {
            onRegisterFailed();
            return;
        }

        registerButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering Account...");
        progressDialog.show();

        String username = usernameText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        try {
            password = Utils.stringToSHA512(password);
        } catch (NoSuchAlgorithmException e) {
            onRegisterFailed();
            return;
        }

        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("username", username);
            jsonParams.put("firstname", firstName);
            jsonParams.put("lastname", lastName);
            jsonParams.put("email", email);
            jsonParams.put("password", password);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            ShoprRestClient.post(getApplicationContext(), "/accounts/register", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    onRegisterSuccess();
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    onRegisterFailed();
                    progressDialog.dismiss();
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            onRegisterFailed();
            progressDialog.dismiss();
        }
    }

    public void onRegisterSuccess() {
        registerButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onRegisterFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = usernameText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if (username.isEmpty() || username.length() < 6) {
            usernameText.setError("at least 6 characters");
            valid = false;
        } else {
            usernameText.setError(null);
        }

        if (firstName.isEmpty() || firstName.length() < 1) {
            firstNameText.setError("at least 1 character");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        if (lastName.isEmpty() || lastName.length() < 1) {
            lastNameText.setError("at last 1 character");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 5 || password.length() > 20) {
            passwordText.setError("between 5 and 20 alphanumeric characters");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }
}
