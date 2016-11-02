package shopr.com.shoprapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import shopr.com.shoprapp.R;
import shopr.com.shoprapp.objects.ShoprProduct;

/**
 * Created by Neil on 10/19/2016.
 *
 * @author Neil Allison
 */
public class WishListAdapter extends BaseAdapter {
    private Context context;
    private List<ShoprProduct> wishListItems;

    public WishListAdapter(Context context, List<ShoprProduct> wishListItems) {
        this.context = context;
        this.wishListItems = wishListItems;
    }

    @Override
    public int getCount() {
        return wishListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.wish_list_card_layout, null);
            viewHolder = new CardViewHolder();
            viewHolder.nameTextView = (TextView) row.findViewById(R.id.wish_list_card_name);
            viewHolder.regularPriceTextView = (TextView) row.findViewById(R.id.wish_list_card_regular_price);
            viewHolder.salePriceTextView = (TextView) row.findViewById(R.id.wish_list_card_sale_price);
            viewHolder.productImageView = (ImageView) row.findViewById(R.id.wish_list_card_image);
            viewHolder.vendorLogoImageView = (ImageView) row.findViewById(R.id.wish_list_card_vendor_image);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }

        ShoprProduct item = wishListItems.get(position);

        viewHolder.nameTextView.setText(item.getName().substring(0, Math.min(item.getName().length(), 70)));

        Double regularPrice = item.getRegularPrice();
        Double salePrice = item.getSalePrice();
        String regularPriceStr = "$" + regularPrice;
        String salePriceStr = "$" + salePrice;

        viewHolder.regularPriceTextView.setText(regularPriceStr);
        if (salePrice < regularPrice && salePrice > 0) {
            viewHolder.regularPriceTextView.setPaintFlags(viewHolder.regularPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.salePriceTextView.setText(salePriceStr);
        } else {
            viewHolder.salePriceTextView.setText("No Sale");
            viewHolder.salePriceTextView.setTextColor(Color.BLACK);
        }

        String thumbnailUrl = item.getThumbnailImage();
        if (thumbnailUrl != null) {
            Picasso.with(context).load(thumbnailUrl).into(viewHolder.productImageView);
        } else {
            viewHolder.productImageView.setImageResource(R.drawable.ic_menu_camera);
        }

        String vendor = item.getVendor();
        if (vendor != null) {
            if (vendor.equals("BESTBUY")) {
                viewHolder.vendorLogoImageView.setImageResource(R.drawable.bestbuy_logo);
            } else if (vendor.equals("WALMART")) {
                viewHolder.vendorLogoImageView.setImageResource(R.drawable.walmart_logo);
            }
        }

        return row;
    }

    static class CardViewHolder {
        TextView nameTextView;
        TextView regularPriceTextView;
        TextView salePriceTextView;
        ImageView productImageView;
        ImageView vendorLogoImageView;
    }
}
