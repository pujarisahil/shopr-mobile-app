package shopr.com.shoprapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;
import shopr.com.shoprapp.R;
import shopr.com.shoprapp.utils.ShoprRestClient;
import shopr.com.shoprapp.utils.SpinnerDialogFragment;

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
    private AppCompatSpinner categorySpinner;
    private FragmentManager fragmentManager;

    public static SearchDialogFragment newInstance(FragmentManager fragmentManager) {
        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setFragmentManager(fragmentManager);
        return fragment;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.search_dialog_layout, null);
        builder.setView(view);

        queryText = (TextInputEditText) view.findViewById(R.id.input_search_query);
        minPriceText = (TextInputEditText) view.findViewById(R.id.input_search_min_price);
        maxPriceText = (TextInputEditText) view.findViewById(R.id.input_search_max_price);
        orderBySpinner = (AppCompatSpinner) view.findViewById(R.id.input_search_order_by_spinner);
        categorySpinner = (AppCompatSpinner) view.findViewById(R.id.input_search_category_spinner);

        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handleSearch();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // NOT USED
            }
        });
        return builder.create();
    }

    private void handleSearch() {
        String query = queryText.getText().toString();

        if (query.isEmpty()) {
            Toast.makeText(getContext(), "Query cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String minPrice = minPriceText.getText().toString();
        String maxPrice = maxPriceText.getText().toString();
        long orderBy = orderBySpinner.getSelectedItemId();
        long category = categorySpinner.getSelectedItemId();

        String url = "/products/search";
        final RequestParams params = new RequestParams();
        params.add("query", query);

        if (!minPrice.isEmpty()) {
            params.add("minprice", minPrice);
        }

        if (!maxPrice.isEmpty()) {
            params.add("maxprice", maxPrice);
        }

        if (orderBy > 0) {
            switch ((int) orderBy) {
                case 2:
                    params.add("orderby", "cust_review_avg");
                    params.add("order", "desc");
                    break;
                case 3:
                    params.add("orderby", "sale_price");
                    params.add("order", "asc");
                    break;
                case 4:
                    params.add("orderby", "sale_price");
                    params.add("order", "desc");
                    break;
                default:
                    break;
            }
        }

        if (category > 0) {
            params.add("category", (String) categorySpinner.getSelectedItem());
        }

        final DialogFragment spinnerDialog = SpinnerDialogFragment.newInstance("Loading", "Getting search results...");
        spinnerDialog.show(getFragmentManager(), "search_spinner");
        ShoprRestClient.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Fragment fragment = SearchResultsFragment.newInstance(new JSONArray(new String(responseBody)), params);
                    fragmentManager.beginTransaction().replace(R.id.main_fragment_content, fragment).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                spinnerDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("SearchFailure", statusCode + " " + new String(responseBody));
                spinnerDialog.dismiss();
            }
        });
    }
}
