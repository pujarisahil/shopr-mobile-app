package shopr.com.shoprapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import shopr.com.shoprapp.R;

/**
 * Created by Neil on 11/1/2016.
 *
 * @author Neil Allison
 */

public class SearchDialogFragment extends DialogFragment {
    private TextInputEditText queryText;
    private TextInputEditText minPriceText;
    private TextInputEditText maxPriceText;
    private AppCompatSpinner orderBySpinner;
    private AppCompatSpinner appCompatSpinner;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.search_dialog_layout, null);
        builder.setView(view);

        queryText = (TextInputEditText) view.findViewById(R.id.input_search_query);
        minPriceText = (TextInputEditText) view.findViewById(R.id.input_search_min_price);
        maxPriceText = (TextInputEditText) view.findViewById(R.id.input_search_max_price);
        // TODO: Map of select to query field (i.e. order by price low to high -> order by price asc
        orderBySpinner = (AppCompatSpinner) view.findViewById(R.id.input_search_order_by_spinner);

        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.i("SearchDialogFragment", "Search clicked");
                Log.i("SearchDialogFragment", queryText.getText().toString());
                handleSearch();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("SearchDialogFragment", "Search cancelled");
            }
        });
        return builder.create();
    }

    private void handleSearch() {

    }
}
