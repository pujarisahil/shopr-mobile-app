package shopr.com.shoprapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.wish_list_fragment_layout, container, false);
        final List<ShoprProduct> products = new ArrayList<>();
        String url = "/user/wishlist";
        final RequestParams params = new RequestParams();
        final DialogFragment spinnerDialog = SpinnerDialogFragment.newInstance(
                "Loading",
                "Getting wish list..."
        );
        spinnerDialog.show(getFragmentManager(), "product_spinner");
        ShoprRestClient.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray response = new JSONArray(new String(responseBody));
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject curObj = response.getJSONObject(i);
                        ShoprProduct product = new ShoprProduct();
                        product.setUpc((String) curObj.get("upc"));
                        product.setName((String) curObj.get("name"));
                        product.setRegularPrice((Double) curObj.get("regular_price"));
                        product.setSalePrice((Double) curObj.get("sale_price"));
                        product.setImage((String) curObj.get("image"));
                        product.setThumbnailImage((String) curObj.get("thumbnail"));
                        product.setShortDescription((String) curObj.get("short_desc"));
                        product.setLongDescription((String) curObj.get("long_desc"));
                        product.setCustomerReviewCount(Long.valueOf((Integer) curObj.get("cust_review_count")));
                        product.setCustomerReviewAverage((String) curObj.get("cust_review_avg"));
                        product.setVendor((String) curObj.get("vendor"));
                        product.setCategoryPath((String) curObj.get("category_path"));
                        products.add(product);
                    }
                } catch (JSONException e) {
                    Log.e("WishListFragment", "Error parsing JSON");
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
                        Intent intent = new Intent(getContext(), LoginActivity.class);
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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                DialogFragment dialog = WishListDetailDialogFragment.newInstance((ShoprProduct) adapterView.getItemAtPosition(i));
                dialog.show(getFragmentManager(), "WishListDetailDialogFragment");
            }
        });

        return view;
    }
}
