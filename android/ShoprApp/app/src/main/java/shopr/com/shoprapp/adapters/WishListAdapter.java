package shopr.com.shoprapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import shopr.com.shoprapp.R;
import shopr.com.shoprapp.objects.ShoprProduct;
import shopr.com.shoprapp.utils.Utils;

/**
 * Created by Neil on 10/19/2016.
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
            viewHolder.descriptionTextView = (TextView) row.findViewById(R.id.wish_list_card_description);
            viewHolder.imageView = (ImageView) row.findViewById(R.id.wish_list_card_image);
            row.setTag(viewHolder);
        } else {
            viewHolder = (CardViewHolder) row.getTag();
        }

        ShoprProduct item = wishListItems.get(position);
//        viewHolder.nameTextView.setText(item.getName());
        viewHolder.nameTextView.setText(item.getUpc());
//        viewHolder.descriptionTextView.setText(item.getShortDescription());
        viewHolder.descriptionTextView.setText(item.getVendor());

        String thumbnailUrl = item.getThumbnailImage();
        if (thumbnailUrl != null && thumbnailUrl.length() >= 3) {
            Picasso.with(context).load(thumbnailUrl).into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.ic_menu_camera);
        }

        return row;
    }

    static class CardViewHolder {
        TextView nameTextView;
        TextView descriptionTextView;
        ImageView imageView;
    }
}
