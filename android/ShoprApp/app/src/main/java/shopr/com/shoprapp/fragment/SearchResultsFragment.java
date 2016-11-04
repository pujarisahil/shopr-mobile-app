package shopr.com.shoprapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
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
import shopr.com.shoprapp.adapters.SearchResultsAdapter;
import shopr.com.shoprapp.objects.ShoprProduct;
import shopr.com.shoprapp.utils.ShoprRestClient;

/**
 * Created by Neil on 11/2/2016.
 *
 * @author Neil Allison
 */

public class SearchResultsFragment extends Fragment {
    private JSONArray results;
    private RequestParams requestParams;

    public static SearchResultsFragment newInstance(JSONArray results, RequestParams requestParams) {
        SearchResultsFragment fragment = new SearchResultsFragment();
        fragment.setResults(results);
        fragment.setRequestParams(requestParams);
        return fragment;
    }

    public void setResults(JSONArray results) {
        this.results = results;
    }

    public void setRequestParams(RequestParams requestParams) {
        this.requestParams = requestParams;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_results_fragment_layout, container, false);
        final List<ShoprProduct> products = new ArrayList<>();
        try {
            if (results != null) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject curObj = results.getJSONObject(i);
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
                    product.setCategoryPath((String) curObj.get("category_path"));
                    products.add(product);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView listView = (ListView) view.findViewById(R.id.search_results_fragment_list_view);
        SearchResultsAdapter adapter = new SearchResultsAdapter(getContext(), products);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: finish this
                ShoprProduct product = (ShoprProduct) adapterView.getItemAtPosition(i);
                String upc = product.getUpc();
                String url = "/products/get-product";
                RequestParams params = new RequestParams();
                params.add("upc", upc);
                ShoprRestClient.get(url, params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        List<ShoprProduct> productsList = new ArrayList<>();
                        try {
                            JSONArray productsArray = new JSONArray(new String(responseBody));
                            for (int i = 0; i < productsArray.length(); i++) {
                                JSONObject curObj = productsArray.getJSONObject(i);
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
                                productsList.add(product);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Get product detail failed", Toast.LENGTH_SHORT).show();
                        }

                        DialogFragment dialogFragment = ProductDetailDialogFragment.newInstance(productsList);
                        dialogFragment.show(getFragmentManager(), "SearchResultsFragment");
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getContext(), "Get product detail failed", Toast.LENGTH_SHORT).show();
                    }
                });
                Snackbar.make(view, "Show Product Detail", Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
