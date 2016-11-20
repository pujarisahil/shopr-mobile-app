package shopr.com.shoprapp.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Neil on 10/20/2016.
 */

public class SpinnerDialogFragment extends DialogFragment {
    public static SpinnerDialogFragment newInstance (String title, String message) {
        SpinnerDialogFragment dialogFragment = new SpinnerDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);

        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(getArguments().getString("title"));
        progressDialog.setMessage(getArguments().getString("message"));
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
