package shopr.com.shoprapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import shopr.com.shoprapp.R;

/**
 * Created by Neil on 10/18/2016.
 *
 * @author Neil Allison
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment_view, container, false);

        RelativeLayout televisionsLayout = (RelativeLayout) view.findViewById(R.id.home_category_televisions_layout);
        televisionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Show television deals sprint 3", Toast.LENGTH_SHORT).show();
            }
        });

        RelativeLayout appliancesLayout = (RelativeLayout) view.findViewById(R.id.home_category_appliances_layout);
        appliancesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Show appliance deals sprint 3", Toast.LENGTH_SHORT).show();
            }
        });

        RelativeLayout computersLayout = (RelativeLayout) view.findViewById(R.id.home_category_computers_layout);
        computersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Show computer deals sprint 3", Toast.LENGTH_SHORT).show();
            }
        });

        RelativeLayout cellPhonesLayout = (RelativeLayout) view.findViewById(R.id.home_category_cellphones_layout);
        cellPhonesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Show cell phone deals sprint 3", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
