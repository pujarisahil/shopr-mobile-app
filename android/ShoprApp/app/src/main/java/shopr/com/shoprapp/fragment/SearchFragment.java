package shopr.com.shoprapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shopr.com.shoprapp.R;
import shopr.com.shoprapp.adapters.SearchAdapter;

/**
 * Created by Neil on 11/2/2016.
 *
 * @author Neil Allison
 */

public class SearchFragment extends Fragment {
    private SearchAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.search_fragment_layout, container, false);

        return view;
    }
}
