package shopr.com.shoprapp.fragment;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import shopr.com.shoprapp.R;
import shopr.com.shoprapp.activities.LoginActivity;
import shopr.com.shoprapp.adapters.ShoppingCartAdapter;
import shopr.com.shoprapp.objects.ShoprProduct;
import shopr.com.shoprapp.utils.ShoprRestClient;
import shopr.com.shoprapp.utils.SpinnerDialogFragment;

/**
 * Created by Neil on 10/18/2016.
 *
 * @author Neil Allison
 */
public class ShoppingCartFragment extends Fragment {
    private ShoppingCartAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.shopping_cart_fragment_layout, container, false);
        final List<ShoprProduct> products = new ArrayList<>();
        String url = "/user/shopping-cart";
        final RequestParams params = new RequestParams();
        final DialogFragment spinnerDialog = SpinnerDialogFragment.newInstance(
                "Loading",
                "Getting shopping cart..."
        );
        spinnerDialog.show(getFragmentManager(), "product_spinner");
        ShoprRestClient.get(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONArray response = new JSONArray(new String(responseBody));
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject curObj = response.getJSONObject(i);
                        ShoprProduct product = new ShoprProduct();
                        product.setUpc((String) curObj.get("upc"));
                        product.setName((String) curObj.get("name"));
                        product.setRegularPrice((Double) curObj.get("regular_price"));
                        product.setSalePrice((Double) curObj.get("sale_price"));
                        product.setImage((String) curObj.get("image"));
                        product.setThumbnailImage((String) curObj.get("thumbnail"));
                        product.setShortDescription((String) curObj.get("short_desc"));
                        product.setLongDescription((String) curObj.get("long_desc"));
                        product.setCustomerReviewCount(Long.valueOf((Integer) curObj.get("cust_review_count")));
                        product.setCustomerReviewAverage((String) curObj.get("cust_review_avg"));
                        product.setVendor((String) curObj.get("vendor"));
                        product.setCategoryPath((String) curObj.get("category_path"));
                        product.setQuantity((Integer) curObj.get("quantity"));
                        products.add(product);
                    }
                } catch (JSONException e) {
                    Log.e("ShoppingCartFragment", "Error parsing JSON");
                } finally {
                    spinnerDialog.dismiss();
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Snackbar.make(view, "Not logged in?", Snackbar.LENGTH_INDEFINITE).setAction("LOGIN", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).show();
                spinnerDialog.dismiss();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.shopping_cart_fragment_recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ShoppingCartAdapter(this.getContext(), getFragmentManager(), recyclerView, products);
        recyclerView.setAdapter(adapter);

        setUpItemTouchHelper();

        return view;
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(ShoppingCartFragment.this.getContext(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) ShoppingCartFragment.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                ShoppingCartAdapter testAdapter = (ShoppingCartAdapter) recyclerView.getAdapter();
                if (testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ShoppingCartAdapter adapter = (ShoppingCartAdapter) recyclerView.getAdapter();
                adapter.remove(swipedPosition);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }
}
