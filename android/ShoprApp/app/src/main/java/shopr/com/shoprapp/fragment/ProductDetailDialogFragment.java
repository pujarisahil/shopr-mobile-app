package shopr.com.shoprapp.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import shopr.com.shoprapp.R;
import shopr.com.shoprapp.activities.LoginActivity;
import shopr.com.shoprapp.adapters.VendorSpinnerAdapter;
import shopr.com.shoprapp.objects.ShoprProduct;
import shopr.com.shoprapp.utils.ShoprRestClient;

/**
 * Created by Neil on 11/2/2016.
 *
 * @author Neil Allison
 */

public class ProductDetailDialogFragment extends DialogFragment {

    private List<ShoprProduct> shoprProducts;
    private Spinner vendorPriceSpinner;
    private TextInputEditText quantityEditText;

    public static ProductDetailDialogFragment newInstance(List<ShoprProduct> shoprProduct) {
        ProductDetailDialogFragment fragment = new ProductDetailDialogFragment();
        fragment.setShoprProducts(shoprProduct);
        return fragment;
    }

    public void setShoprProducts(List<ShoprProduct> shoprProducts) {
        this.shoprProducts = shoprProducts;
    }

    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.product_detail_dialog_layout, null);
        builder.setView(view);

        ImageView productImage = (ImageView) view.findViewById(R.id.product_detail_image);
        if (shoprProducts.get(0).getImage() != null && shoprProducts.get(0).getImage().length() > 0) {
            Picasso.with(getContext()).load(shoprProducts.get(0).getImage()).into(productImage);
        } else {
            if (shoprProducts.get(0).getThumbnailImage() != null && shoprProducts.get(0).getThumbnailImage().length() > 0) {
                Picasso.with(getContext()).load(shoprProducts.get(0).getThumbnailImage()).into(productImage);
            } else {
                productImage.setImageResource(R.drawable.no_image);
            }
        }

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.product_detail_rating);
        String reviewAverage = shoprProducts.get(0).getCustomerReviewAverage();
        if (reviewAverage.isEmpty()) {
            ratingBar.setRating(0f);
        } else {
            ratingBar.setRating(Float.valueOf(shoprProducts.get(0).getCustomerReviewAverage()));
        }

        TextView numRatingsTextView = (TextView) view.findViewById(R.id.product_detail_num_ratings);
        numRatingsTextView.setText(" (" + shoprProducts.get(0).getCustomerReviewCount() + ")");

        TextView nameTextView = (TextView) view.findViewById(R.id.product_detail_name);
        nameTextView.setText(shoprProducts.get(0).getName());

        TextView descriptionTextView = (TextView) view.findViewById(R.id.product_detail_description);
        descriptionTextView.setText(shoprProducts.get(0).getLongDescription());

        vendorPriceSpinner = (Spinner) view.findViewById(R.id.product_detail_vendor_spinner);

        int numVendors = shoprProducts.size();
        String[] vendorNames = new String[numVendors];
        int[] vendorLogos = new int[numVendors];
        Double[] salePrices = new Double[numVendors];

        processVendors(vendorNames, vendorLogos, salePrices);

        VendorSpinnerAdapter vendorSpinnerAdapter = new VendorSpinnerAdapter(getContext(),
                vendorNames, vendorLogos, salePrices);
        vendorPriceSpinner.setAdapter(vendorSpinnerAdapter);

        vendorPriceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //
            }
        });

        quantityEditText = (TextInputEditText) view.findViewById(R.id.product_detail_quantity);

        Button addToCart = (Button) view.findViewById(R.id.product_detail_add_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String quantityStr = quantityEditText.getText().toString();
                if (quantityStr.isEmpty()) {
                    Snackbar.make(view, "Quantity cannot be empty", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                int quantity = Integer.parseInt(quantityStr);
                String vendor = (String) vendorPriceSpinner.getSelectedItem();
                String upc = shoprProducts.get(0).getUpc();

                String url = "/user/shopping-cart";
                JSONObject params = new JSONObject();
                StringEntity entity = null;
                try {
                    params.put("quantity", quantity);
                    params.put("vendor", vendor);
                    params.put("upc", upc);
                    entity = new StringEntity(params.toString());
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ShoprRestClient.post(getContext(), url, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Snackbar.make(view, "Product added to cart", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Snackbar.make(view, "Not logged in?", Snackbar.LENGTH_SHORT).setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).show();
                    }
                });
            }
        });
        Button addToWishList = (Button) view.findViewById(R.id.product_detail_add_wish_list);
        addToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String vendor = (String) vendorPriceSpinner.getSelectedItem();
                String upc = shoprProducts.get(0).getUpc();

                String url = "/user/wishlist";
                JSONObject params = new JSONObject();
                StringEntity entity = null;
                try {
                    params.put("vendor", vendor);
                    params.put("upc", upc);
                    entity = new StringEntity(params.toString());
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ShoprRestClient.post(getContext(), url, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Snackbar.make(view, "Product added to wishlist", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Snackbar.make(view, "Not logged in?", Snackbar.LENGTH_SHORT).setAction("LOGIN", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).show();
                    }
                });
            }
        });

        return builder.create();
    }

    private void processVendors(String[] vendorNames, int[] vendorLogos, Double[] salePrices) {
        for (int i = 0; i < shoprProducts.size(); i++) {
            vendorNames[i] = shoprProducts.get(i).getVendor();
            salePrices[i] = shoprProducts.get(i).getSalePrice();
            switch (vendorNames[i]) {
                case "BESTBUY":
                    vendorLogos[i] = R.drawable.bestbuy_logo;
                    break;
                case "WALMART":
                    vendorLogos[i] = R.drawable.walmart_logo;
                    break;
                default:
                    Log.e("ProductDetail", "Unknown Vendor");
                    break;
            }
        }
    }
}
