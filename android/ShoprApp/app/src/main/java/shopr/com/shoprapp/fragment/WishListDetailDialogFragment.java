package shopr.com.shoprapp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import shopr.com.shoprapp.R;
import shopr.com.shoprapp.adapters.WishListAdapter;
import shopr.com.shoprapp.objects.ShoprProduct;

/**
 * Created by Neil on 11/2/2016.
 *
 * @author Neil Allison
 */
public class WishListDetailDialogFragment extends DialogFragment {
    private ShoprProduct shoprProduct;
    private WishListAdapter wishListAdapter;

    public static WishListDetailDialogFragment newInstance(WishListAdapter wishListAdapter, ShoprProduct shoprProduct) {
        WishListDetailDialogFragment fragment = new WishListDetailDialogFragment();
        fragment.setWishListAdapter(wishListAdapter);
        fragment.setShoprProduct(shoprProduct);
        return fragment;
    }

    public void setWishListAdapter(WishListAdapter wishListAdapter) {
        this.wishListAdapter = wishListAdapter;
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
        final View view = inflater.inflate(R.layout.wishlist_detail_dialog_layout, null);
        builder.setView(view);

        ImageView productImage = (ImageView) view.findViewById(R.id.wishlist_detail_image);
        if (shoprProduct.getImage() != null && shoprProduct.getImage().length() > 0) {
            Picasso.with(getContext()).load(shoprProduct.getImage()).into(productImage);
        } else {
            if (shoprProduct.getThumbnailImage() != null && shoprProduct.getThumbnailImage().length() > 0) {
                Picasso.with(getContext()).load(shoprProduct.getThumbnailImage()).into(productImage);
            } else {
                productImage.setImageResource(R.drawable.ic_menu_camera);
            }
        }

        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.wishlist_detail_rating);
        String reviewAverage = shoprProduct.getCustomerReviewAverage();
        if (reviewAverage.isEmpty()) {
            ratingBar.setRating(0f);
        } else {
            ratingBar.setRating(Float.valueOf(shoprProduct.getCustomerReviewAverage()));
        }

        TextView numRatingsTextView = (TextView) view.findViewById(R.id.wishlist_detail_num_ratings);
        numRatingsTextView.setText(" (" + shoprProduct.getCustomerReviewCount() + ")");

        TextView nameTextView = (TextView) view.findViewById(R.id.wishlist_detail_name);
        nameTextView.setText(shoprProduct.getName());

        TextView descriptionTextView = (TextView) view.findViewById(R.id.wishlist_detail_description);
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

        TextView regularPriceTextView = (TextView) view.findViewById(R.id.wishlist_detail_regular_price);
        regularPriceTextView.setText(regularPriceStr);

        TextView salePriceTextView = (TextView) view.findViewById(R.id.wishlist_detail_sale_price);
        if (salePrice < regularPrice && salePrice > 0) {
            regularPriceTextView.setPaintFlags(regularPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            salePriceTextView.setText(salePriceStr);
        } else {
            salePriceTextView.setText(R.string.no_sale);
            salePriceTextView.setTextColor(Color.BLACK);
        }

        builder.setNegativeButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                wishListAdapter.remove(wishListAdapter.getWishListItems().indexOf(shoprProduct));
            }
        });

        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Ignore
            }
        });

        return builder.create();
    }
}
