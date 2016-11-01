package shopr.com.shoprapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import shopr.com.shoprapp.R;
import shopr.com.shoprapp.activities.LoginActivity;
import shopr.com.shoprapp.adapters.WishListAdapter;
import shopr.com.shoprapp.objects.ShoprProduct;
import shopr.com.shoprapp.utils.ShoprRestClient;
import shopr.com.shoprapp.utils.SpinnerDialogFragment;

/**
 * Created by Neil on 10/18/2016.
 *
 * @author Neil Allison
 */
public class WishListFragment extends Fragment {
    private WishListAdapter adapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.wish_list_fragment_layout, container, false);
        context = getContext();
        final List<ShoprProduct> products = new ArrayList<>();
        String url = "/user/wishlist";
        RequestParams params = new RequestParams();
        final DialogFragment spinnerDialog = SpinnerDialogFragment.newInstance(
                "Loading",
                "Getting product information..."
        );
        spinnerDialog.show(getFragmentManager(), "product_spinner");
        ShoprRestClient.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Log.i("WishListFragment", "Status: " + statusCode);
                    Log.i("WishListFragment", "Body: " + new String(responseBody));
                    JSONArray response = new JSONArray(new String(responseBody));
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject curObj = response.getJSONObject(i);
                        ShoprProduct shoprProduct = new ShoprProduct();
                        shoprProduct.setVendor((String) curObj.get("vendor"));
                        shoprProduct.setUpc((String) curObj.get("upc"));
                        products.add(shoprProduct);
                    }
                } catch (JSONException e) {
                    Log.e("JSONParsing", "Error parsing JSON");
                } finally {
                    spinnerDialog.dismiss();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Snackbar.make(view, "Not logged in?", Snackbar.LENGTH_INDEFINITE).setAction("LOGIN", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).show();
                spinnerDialog.dismiss();
            }
        });

        ListView listView = (ListView) view.findViewById(R.id.wish_list_fragment_list_view);
        adapter = new WishListAdapter(this.getContext(), products);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
