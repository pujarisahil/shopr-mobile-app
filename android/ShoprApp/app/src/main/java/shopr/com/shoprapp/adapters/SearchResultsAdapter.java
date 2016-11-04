package shopr.com.shoprapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import shopr.com.shoprapp.R;
import shopr.com.shoprapp.objects.ShoprProduct;

/**
 * Created by Neil on 11/2/2016.
 *
 * @author Neil Allison
 */

public class SearchResultsAdapter extends BaseAdapter {
    private Context context;
    private List<ShoprProduct> searchResultsItems;

    public SearchResultsAdapter(Context context, List<ShoprProduct> searchResultsItems) {
        this.context = context;
        this.searchResultsItems = searchResultsItems;
    }

    @Override
    public int getCount() {
        return searchResultsItems.size();
    }

    @Override
    public Object getItem(int i) {
        return searchResultsItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        CardViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.search_results_card_layout, viewGroup, false);
            viewHolder = new CardViewHolder();
            viewHolder.nameTextView = (TextView) row.findViewById(R.id.search_results_name);
            viewHolder.numRatingsTextView = (TextView) row.findViewById(R.id.search_results_num_ratings);
            viewHolder.descriptionTextView = (TextView) row.findViewById(R.id.search_results_description);
            viewHolder.regularPriceTextView = (TextView) row.findViewById(R.id.search_results_regular_price);
            viewHolder.lowestPriceTextView = (TextView) row.findViewById(R.id.search_results_lowest_price);
            viewHolder.ratingRatingBar = (RatingBar) row.findViewById(R.id.search_results_average_rating);
            viewHolder.productImageView = (ImageView) row.findViewById(R.id.search_results_product_image);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }

        ShoprProduct item = searchResultsItems.get(i);

        String thumbnailUrl = item.getThumbnailImage();
        if (thumbnailUrl != null && thumbnailUrl.length() > 0) {
            Picasso.with(context).load(thumbnailUrl).into(viewHolder.productImageView);
        } else {
            viewHolder.productImageView.setImageResource(R.drawable.no_image);
        }

        String reviewAverage = item.getCustomerReviewAverage();
        if (reviewAverage.isEmpty()) {
            viewHolder.ratingRatingBar.setRating(0f);
        } else {
            viewHolder.ratingRatingBar.setRating(Float.valueOf(item.getCustomerReviewAverage()));
        }

        viewHolder.numRatingsTextView.setText(" (" + item.getCustomerReviewCount() + ")");

        viewHolder.nameTextView.setText(item.getName().substring(0, Math.min(item.getName().length(), 70)));
        if (item.getName().length() > 70) {
            viewHolder.nameTextView.append("...");
        }

        String shortDesc = item.getShortDescription();
        if (shortDesc == null || shortDesc.isEmpty()) {
            viewHolder.descriptionTextView.setText("No Description");
        } else {
            viewHolder.descriptionTextView.setText(shortDesc.substring(0, Math.min(shortDesc.length(), 140)));
        }

        Double regularPrice = item.getRegularPrice();
        Double lowestPrice = item.getSalePrice();
        String regularPriceStr = "$" + String.format(Locale.US, "%.2f", regularPrice);
        String lowestPriceStr = "$" + String.format(Locale.US, "%.2f", lowestPrice);

        viewHolder.regularPriceTextView.setText(regularPriceStr);
        if (lowestPrice < regularPrice && lowestPrice > 0) {
            viewHolder.regularPriceTextView.setPaintFlags(viewHolder.regularPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.lowestPriceTextView.setText(lowestPriceStr);
        } else {
            viewHolder.regularPriceTextView.setPaintFlags(viewHolder.regularPriceTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            viewHolder.lowestPriceTextView.setText(R.string.no_sale);
            viewHolder.lowestPriceTextView.setTextColor(Color.BLACK);
        }

        return row;
    }

    private static class CardViewHolder {
        TextView nameTextView;
        TextView numRatingsTextView;
        TextView descriptionTextView;
        TextView regularPriceTextView;
        TextView lowestPriceTextView;
        RatingBar ratingRatingBar;
        ImageView productImageView;
    }
}
