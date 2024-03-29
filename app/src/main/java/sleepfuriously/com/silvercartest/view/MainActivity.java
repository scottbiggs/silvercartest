package sleepfuriously.com.silvercartest.view;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import sleepfuriously.com.silvercartest.R;
import sleepfuriously.com.silvercartest.model.Location;
import sleepfuriously.com.silvercartest.presenter.ModelWindow;

/**
 * Starting point.  Works with both tablets and phones.
 */
public class MainActivity extends AppCompatActivity
        implements ModelWindow.ModelWindowLocationsListener {

    //-------------------------
    //  constants
    //-------------------------

    private static final String DTAG = MainActivity.class.getSimpleName();

    //-------------------------
    //  widgets
    //-------------------------

    /** displays locations list */
    private RecyclerView mLocationsRV;

    /** give the user something to look at while waiting for data access */
    ProgressDialog mProgressDialog;

    //-------------------------
    //  data
    //-------------------------

    /**
     * Whether or note the Activity is in two-pain mode, ie running on a
     * tablet.
     */
    private boolean mTwoPane;


    /** Adapter for {@link MainActivity#mLocationsRV}. */
    private LocationRVAdapter mLocationAdapter;


    //-------------------------
    //  methods
    //-------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // First things first, gotta be on the  internet!
        if (!isInternetAvailable()) {
            View v = findViewById(android.R.id.content);
            Toast.makeText(this, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
            finish();
        }


        final Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        String titleStr = getString(R.string.app_name) + " (" +
                getString(R.string.toolbar_title) + ")";
        toolbar.setTitle(titleStr);

        // back button
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // just exit program for this simple programming challenge
                finish();
            }
        });

        // search button
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // todo: remove this once the filter works
                Snackbar.make(toolbar, s + " (todo: make this work!)", Snackbar.LENGTH_SHORT).show();
                return false;   // let result fall through
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // todo: filter as the user types
                return false;
            }
        });

        if (findViewById(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // load up the recyclerview
        mLocationsRV = findViewById(R.id.locations_rv);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setTitle(R.string.loading_locations);
        mProgressDialog.show();

        ModelWindow mw = ModelWindow.getInstance();
        mw.getLocationList(this, this);


    } // onCreate(savedInstanceState)


    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetworkInfo != null)
                && activeNetworkInfo.isAvailable()
                && activeNetworkInfo.isConnected();
    }


    @Override
    public void returnLocationList(List<Location> locations, boolean successful, String msg) {

        mProgressDialog.dismiss();

        if (!successful) {
            Log.e(DTAG, "returnLocationList() is unsuccessful. Msg = " + msg);
            Toast.makeText(this, R.string.no_internet_warning, Toast.LENGTH_LONG).show();
            finish();
            return; // might be redundant?
        }

        mLocationAdapter = new LocationRVAdapter(this, locations, mTwoPane);
        mLocationsRV.setAdapter(mLocationAdapter);
    }


}
