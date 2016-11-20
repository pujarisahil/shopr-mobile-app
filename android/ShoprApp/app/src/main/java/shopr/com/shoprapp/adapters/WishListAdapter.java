package shopr.com.shoprapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import shopr.com.shoprapp.R;
import shopr.com.shoprapp.fragment.WishListDetailDialogFragment;
import shopr.com.shoprapp.objects.ShoprProduct;

/**
 * Created by Neil on 10/19/2016.
 *
 * @author Neil Allison
 */
public class WishListAdapter extends RecyclerView.Adapter {
    private Context context;
    private FragmentManager fragmentManager;
    private RecyclerView recyclerView;
    private List<ShoprProduct> wishListItems;

    public WishListAdapter(Context context, FragmentManager fragmentManager, RecyclerView recyclerView, List<ShoprProduct> wishListItems) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.recyclerView = recyclerView;
        this.wishListItems = wishListItems;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_card_layout, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int productIndex = recyclerView.indexOfChild(view);
                DialogFragment dialog = WishListDetailDialogFragment.newInstance(wishListItems.get(productIndex));
                dialog.show(fragmentManager, "WishListDetailDialogFragment");
            }
        });

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ShoprProduct item = wishListItems.get(position);

        ((CardViewHolder) viewHolder).nameTextView.setText(item.getName().substring(0, Math.min(item.getName().length(), 70)));
        if (item.getName().length() > 70) {
            ((CardViewHolder) viewHolder).nameTextView.append("...");
        }

        Double regularPrice = item.getRegularPrice();
        Double salePrice = item.getSalePrice();
        String regularPriceStr = "$" + String.format(Locale.US, "%.2f", regularPrice);
        String salePriceStr = "$" + String.format(Locale.US, "%.2f", salePrice);

        ((CardViewHolder) viewHolder).regularPriceTextView.setText(regularPriceStr);
        if (salePrice < regularPrice && salePrice > 0) {
            ((CardViewHolder) viewHolder).regularPriceTextView.setPaintFlags(
                    ((CardViewHolder) viewHolder).regularPriceTextView.getPaintFlags()
                            | Paint.STRIKE_THRU_TEXT_FLAG);
            ((CardViewHolder) viewHolder).salePriceTextView.setText(salePriceStr);
        } else {
            ((CardViewHolder) viewHolder).regularPriceTextView.setPaintFlags(
                    ((CardViewHolder) viewHolder).regularPriceTextView.getPaintFlags()
                            & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            ((CardViewHolder) viewHolder).salePriceTextView.setText(R.string.no_sale);
            ((CardViewHolder) viewHolder).salePriceTextView.setTextColor(Color.BLACK);
        }

        String thumbnailUrl = item.getThumbnailImage();
        if (thumbnailUrl != null && thumbnailUrl.length() > 0) {
            Picasso.with(context).load(thumbnailUrl).into(((CardViewHolder) viewHolder).productImageView);
        } else {
            ((CardViewHolder) viewHolder).productImageView.setImageResource(R.drawable.no_image);
        }

        String vendor = item.getVendor();
        if (vendor != null) {
            if (vendor.equals("BESTBUY")) {
                ((CardViewHolder) viewHolder).vendorLogoImageView.setImageResource(R.drawable.bestbuy_logo);
            } else if (vendor.equals("WALMART")) {
                ((CardViewHolder) viewHolder).vendorLogoImageView.setImageResource(R.drawable.walmart_logo);
            }
        }
    }

    @Override
    public int getItemCount() {
        return wishListItems.size();
    }

    private class CardViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, regularPriceTextView, salePriceTextView;
        ImageView productImageView, vendorLogoImageView;

        CardViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.wish_list_card_name);
            regularPriceTextView = (TextView) itemView.findViewById(R.id.wish_list_card_regular_price);
            salePriceTextView = (TextView) itemView.findViewById(R.id.wish_list_card_sale_price);
            productImageView = (ImageView) itemView.findViewById(R.id.wish_list_card_image);
            vendorLogoImageView = (ImageView) itemView.findViewById(R.id.wish_list_card_vendor_image);
        }
    }
}
