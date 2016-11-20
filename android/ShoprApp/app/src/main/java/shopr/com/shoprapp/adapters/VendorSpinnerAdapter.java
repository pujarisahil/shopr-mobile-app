package shopr.com.shoprapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import shopr.com.shoprapp.R;

/**
 * Created by Neil on 11/3/2016.
 *
 * @author Neil Allison
 */

public class VendorSpinnerAdapter extends BaseAdapter {
    private Context context;
    private String[] vendors;
    private int[] logos;
    private Double[] salePrices;

    public VendorSpinnerAdapter(Context context, String[] vendors, int[] logos, Double[] salePrices) {
        this.context = context;
        this.vendors = vendors;
        this.logos = logos;
        this.salePrices = salePrices;
    }

    @Override
    public int getCount() {
        return vendors.length;
    }

    @Override
    public Object getItem(int i) {
        return vendors[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.product_detail_customer_spinner_row, parent, false);

            TextView salePrice = (TextView) view.findViewById(R.id.product_detail_spinner_sale_price);
            String salePriceText = "$" + String.format(Locale.US, "%.2f", salePrices[i]);
            salePrice.setText(salePriceText);

            ImageView vendorLogo = (ImageView) view.findViewById(R.id.product_detail_spinner_vendor_image);
            vendorLogo.setImageResource(logos[i]);
        }

        return view;
    }
}
