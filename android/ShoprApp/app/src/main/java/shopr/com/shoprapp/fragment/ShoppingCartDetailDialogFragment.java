package shopr.com.shoprapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import shopr.com.shoprapp.R;
import shopr.com.shoprapp.adapters.ShoppingCartAdapter;
import shopr.com.shoprapp.objects.ShoprProduct;
import shopr.com.shoprapp.utils.ShoprRestClient;

/**
 * Created by Neil on 11/2/2016.
 *
 * @author Neil Allison
 */

public class ShoppingCartDetailDialogFragment extends DialogFragment {
    private ShoprProduct shoprProduct;
    private ShoppingCartAdapter shoppingCartAdapter;

    public static ShoppingCartDetailDialogFragment newInstance(ShoppingCartAdapter shoppingCartAdapter, ShoprProduct shoprProduct) {
        ShoppingCartDetailDialogFragment fragment = new ShoppingCartDetailDialogFragment();
        fragment.setShoppingCartAdapter(shoppingCartAdapter);
        fragment.setShoprProduct(shoprProduct);
        return fragment;
    }

    public void setShoppingCartAdapter(ShoppingCartAdapter shoppingCartAdapter) {
        this.shoppingCartAdapter = shoppingCartAdapter;
    }

    public void setShoprProduct(ShoprProduct shoprProduct) {
        this.shoprProduct = shoprProduct;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        @SuppressLint("InflateParams")
        // Since no access to the parent before inflation for a dialog, need to pass null as second argument
        final View view = inflater.inflate(R.layout.shopping_cart_detail_dialog_layout, null);
        builder.setView(view);

        ImageView productImage = (ImageView) view.findViewById(R.id.shopping_cart_detail_image);
        if (shoprProduct.getImage() != null && shoprProduct.getImage().length() > 0) {
            Picasso.with(getContext()).load(shoprProduct.getImage()).into(productImage);
        } else {
            if (shoprProduct.getThumbnailImage() != null && shoprProduct.getThumbnailImage().length() > 0) {
                Picasso.with(getContext()).load(shoprProduct.getThumbnailImage()).into(productImage);
            } else {
                productImage.setImageResource(R.drawable.ic_menu_camera);
            }
        }

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.shopping_cart_detail_rating);
        String reviewAverage = shoprProduct.getCustomerReviewAverage();
        if (reviewAverage.isEmpty()) {
            ratingBar.setRating(0f);
        } else {
            ratingBar.setRating(Float.valueOf(shoprProduct.getCustomerReviewAverage()));
        }

        TextView numRatingsTextView = (TextView) view.findViewById(R.id.shopping_cart_detail_num_ratings);
        numRatingsTextView.setText(" (" + shoprProduct.getCustomerReviewCount() + ")");

        TextView nameTextView = (TextView) view.findViewById(R.id.shopping_cart_detail_name);
        nameTextView.setText(shoprProduct.getName());

        TextView descriptionTextView = (TextView) view.findViewById(R.id.shopping_cart_detail_description);
        String longDescription = shoprProduct.getLongDescription();
        String shortDescription = shoprProduct.getShortDescription();
        if (!longDescription.isEmpty()) {
            descriptionTextView.setText(longDescription);
        } else if (!shortDescription.isEmpty()) {
            descriptionTextView.setText(shortDescription);
        } else {
            descriptionTextView.setText(R.string.no_description);
        }

        Double regularPrice = shoprProduct.getRegularPrice();
        Double salePrice = shoprProduct.getSalePrice();
        String regularPriceStr = "$" + String.format(Locale.US, "%.2f", regularPrice);
        String salePriceStr = "$" + String.format(Locale.US, "%.2f", salePrice);

        TextView regularPriceTextView = (TextView) view.findViewById(R.id.shopping_cart_detail_regular_price);
        regularPriceTextView.setText(regularPriceStr);

        TextView salePriceTextView = (TextView) view.findViewById(R.id.shopping_cart_detail_sale_price);
        if (salePrice < regularPrice && salePrice > 0) {
            regularPriceTextView.setPaintFlags(regularPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            salePriceTextView.setText(salePriceStr);
        } else {
            salePriceTextView.setText(R.string.no_sale);
            salePriceTextView.setTextColor(Color.BLACK);
        }

        final EditText quantityEditText = (EditText) view.findViewById(R.id.shopping_cart_detail_quantity_edit_text);
        String quantityText = String.valueOf(shoprProduct.getQuantity());
        quantityEditText.setText(quantityText);

        Button removeProductButton = (Button) view.findViewById(R.id.shopping_cart_detail_remove_button);
        removeProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shoppingCartAdapter.remove(shoppingCartAdapter.getShoppingCartItems().indexOf(shoprProduct));
            }
        });

        Button updateQuantityButton = (Button) view.findViewById(R.id.shopping_cart_detail_update_button);
        updateQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String upc = shoprProduct.getUpc();
                String vendor = shoprProduct.getVendor();

                String url = "/user/shopping-cart-update";
                JSONObject params = new JSONObject();
                StringEntity entity = null;
                try {
                    params.put("upc", upc);
                    params.put("vendor", vendor);
                    params.put("quantity", Integer.parseInt(quantityEditText.getText().toString()));
                    entity = new StringEntity(params.toString());
                } catch (JSONException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ShoprRestClient.post(getContext(), url, entity, "application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Snackbar.make(view, "Product quantity updated", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Snackbar.make(view, "Product quantity update failed", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return builder.create();
    }
}
