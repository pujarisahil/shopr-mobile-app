package shopr.com.shoprapp.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import shopr.com.shoprapp.R;
import shopr.com.shoprapp.utils.ShoprRestClient;

/**
 * Created by Neil on 10/18/2016.
 *
 * @author Neil Allison
 */
public class FeedbackFragment extends Fragment {
    private TextInputEditText nameText;
    private TextInputEditText emailText;
    private TextInputEditText commentsText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.feedback_fragment_view, container, false);

        nameText = (TextInputEditText) view.findViewById(R.id.input_feedback_name);
        emailText = (TextInputEditText) view.findViewById(R.id.input_feedback_email);
        commentsText = (TextInputEditText) view.findViewById(R.id.input_feedback_comments);

        Button feedbackSubmitButton = (Button) view.findViewById(R.id.feedback_submit_button);
        feedbackSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });
        return view;
    }

    public void submitFeedback() {
        if (!isInputValid()) {
            Toast.makeText(getContext(), "Send feedback failed", Toast.LENGTH_LONG).show();
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(getContext(), R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Submitting feedback...");
        progressDialog.show();

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String comments = commentsText.getText().toString();

        try {
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("name", name);
            jsonParams.put("email", email);
            jsonParams.put("comments", comments);
            StringEntity entity = new StringEntity(jsonParams.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            ShoprRestClient.post(getContext(), "/server/feedback", entity, "application/json", new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Thanks for your feedback!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.i("FeedbackFragment", "Status: " + statusCode);
                    Log.i("FeedbackFragment", "Body: " + new String(responseBody));
                    Toast.makeText(getContext(), "Send feedback failed", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }
            });
        } catch (JSONException | UnsupportedEncodingException e) {
            Toast.makeText(getContext(), "Send feedback failed", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
    }

    public boolean isInputValid() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String comments = commentsText.getText().toString();

        if (name.isEmpty() || name.length() > 80) {
            nameText.setError("must be 1 to 80 characters");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (email.isEmpty() || email.length() > 100 || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("enter a valid email address");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (comments.isEmpty() || comments.length() > 500) {
            commentsText.setError("must be 1 to 500 characters");
            valid = false;
        } else {
            commentsText.setError(null);
        }

        return valid;
    }
}
