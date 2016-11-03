package shopr.com.shoprapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

public class ShoppingCartAdapter extends BaseAdapter {
    private Context context;
    private List<ShoprProduct> shoppingCartItems;

    public ShoppingCartAdapter(Context context, List<ShoprProduct> shoppingCartItems) {
        this.context = context;
        this.shoppingCartItems = shoppingCartItems;
    }

    @Override
    public int getCount() {
        return shoppingCartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingCartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CardViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.shopping_cart_card_layout, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.nameTextView = (TextView) row.findViewById(R.id.shopping_cart_card_name);
            viewHolder.priceTextView = (TextView) row.findViewById(R.id.shopping_cart_card_price);
            viewHolder.quantityTextView = (TextView) row.findViewById(R.id.shopping_cart_card_quantity);
            viewHolder.productImageView = (ImageView) row.findViewById(R.id.shopping_cart_card_image);
            viewHolder.vendorLogoImageView = (ImageView) row.findViewById(R.id.shopping_cart_card_vendor_image);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }

        ShoprProduct item = shoppingCartItems.get(position);

        viewHolder.nameTextView.setText(item.getName().substring(0, Math.min(item.getName().length(), 70)));
        if (item.getName().length() > 70) {
            viewHolder.nameTextView.append("...");
        }

        Double regularPrice = item.getRegularPrice();
        Double salePrice = item.getSalePrice();
        Double price = Math.min(regularPrice, salePrice);
        String priceStr = "$" + String.format(Locale.US, "%.2f", price);
        viewHolder.priceTextView.setText(priceStr);

        Integer quantity = item.getQuantity();
        String quantityStr = "Qty: " + quantity;
        viewHolder.quantityTextView.setText(quantityStr);

        String thumbnailUrl = item.getThumbnailImage();
        if (thumbnailUrl != null && thumbnailUrl.length() > 0) {
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
        TextView priceTextView;
        TextView quantityTextView;
        ImageView productImageView;
        ImageView vendorLogoImageView;
    }
}
