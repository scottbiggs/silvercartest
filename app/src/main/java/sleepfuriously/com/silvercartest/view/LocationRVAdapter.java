package sleepfuriously.com.silvercartest.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sleepfuriously.com.silvercartest.model.Location;
import sleepfuriously.com.silvercartest.R;

/**
 * Created on 2019-08-09.
 */
public class LocationRVAdapter extends RecyclerView.Adapter<LocationRVAdapter.ViewHolder> {

    //----------------------
    //  constants
    //----------------------

    private static final String DTAG = LocationRVAdapter.class.getSimpleName();

    //----------------------
    //  data
    //----------------------

    /** parent will always be MainActivity */
    private final MainActivity mParentActivity;

    /** THE data to display */
    private final List<Location> mLocationList;

    /** TRUE iff in two-pane mode (tablet) */
    private final boolean mTwoPane;

    /** The currently selected item in the RV. Needed for highlighting etc. */
    private int mSelected = -1;

    /** Normal background color for a Location list item */
    private int mNormalBackground;

    /** The color to use for a Location list item that is currently selected */
    private int mHighlightBackground;


    //------------------------
    //  listener
    //------------------------

    /** Click Listener for the items in the RecyclerView. */
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // The tag tells which location we're dealing with
            int currentlySelected = (int) view.getTag();
            Location location = mLocationList.get(currentlySelected);

            // redraw the selection
            notifyItemChanged(mSelected);
            notifyItemChanged(currentlySelected);
            mSelected = currentlySelected;

/*
            todo: implement this!!!!

            if (mTwoPane) {
                // Start the new Fragment
                Bundle bundle = new Bundle();
                bundle.putLong(PhotosFragment.ALBUM_ID_KEY, album.id);

                PhotosFragment frag = new PhotosFragment();
                frag.setArguments(bundle);

                FragmentManager mgr = mParentActivity.getSupportFragmentManager();
                FragmentTransaction transaction = mgr.beginTransaction();

                // Replace the Fragment in the item_detail_container with the new Fragment.
                transaction.replace(R.id.item_detail_container, frag);
                transaction.commit();
            }

            else {
                // Start new Activity
                Context context = view.getContext();
                Intent intent = new Intent(context, PhotosActivity.class);

                intent.putExtra(PhotosFragment.ALBUM_ID_KEY, album.id);
                context.startActivity(intent);
            }
*/
        }

    };


    //----------------------
    //  methods
    //----------------------

    LocationRVAdapter(MainActivity parent,
                      List<Location> items,
                      boolean twoPane) {
        mLocationList = items;
        mParentActivity = parent;
        mTwoPane = twoPane;

        // Pre-calculate the colors to avoid slowing down onBindViewHolder
        mNormalBackground = parent.getResources().getColor(R.color.location_item_background_normal);
        mHighlightBackground = parent.getResources().getColor(R.color.location_item_background_highlight);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        // set data
        Location loc = mLocationList.get(position);
        holder.id = loc.id;

        holder.nameTv.setText(loc.name);
        holder.airportCodeTv.setText(loc.airport_code);
        holder.descriptionTv.setText(loc.description);

        // todo: set the images

        // Store the adapter position of this item for use during onClick
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mOnClickListener);

        // Possibly highlight this as a selected item
        if (position == mSelected) {
            holder.backgroundView.setBackgroundColor(mHighlightBackground);
        }
        else {
            holder.backgroundView.setBackgroundColor(mNormalBackground);
        }
    }

    @Override
    public int getItemCount() {
        return mLocationList.size();
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //  classes
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    class ViewHolder extends RecyclerView.ViewHolder {
        long id;
        final TextView nameTv, airportCodeTv, descriptionTv;
        final ImageView locationIv, locationLocoIv;
        final View backgroundView;  // allows access to the background color

        ViewHolder(View v) {
            super(v);

            nameTv = v.findViewById(R.id.name_tv);
            airportCodeTv = v.findViewById(R.id.airport_code_tv);
            descriptionTv = v.findViewById(R.id.description_tv);

            locationIv = v.findViewById(R.id.loc_image);
            locationLocoIv = v.findViewById(R.id.loc_logo);

            backgroundView = v.findViewById(R.id.background_cl);
        }
    }


}
