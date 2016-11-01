package shopr.com.shoprapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shopr.com.shoprapp.R;

/**
 * Created by Neil on 10/18/2016.
 */
public class ShoppingCartFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.shopping_cart_fragment_view, container, false);
    }
}
