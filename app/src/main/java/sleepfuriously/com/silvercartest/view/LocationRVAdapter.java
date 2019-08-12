package sleepfuriously.com.silvercartest.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import sleepfuriously.com.silvercartest.model.Location;
import sleepfuriously.com.silvercartest.R;
import sleepfuriously.com.silvercartest.presenter.ModelWindow;

/**
 * Created on 2019-08-09.
 */
public class LocationRVAdapter
        extends RecyclerView.Adapter<LocationRVAdapter.ViewHolder>
        implements Filterable {

    //----------------------
    //  constants
    //----------------------

    private static final String DTAG = LocationRVAdapter.class.getSimpleName();

    //----------------------
    //  data
    //----------------------

    /** parent will always be MainActivity */
    private final MainActivity mParentActivity;

    /** original (entire) data list */
    private final List<Location> mLocationList;

    /** THIS is the list of locations to diplay */
    private List<Location> mFilteredLocationList;

    /** TRUE iff in two-pane mode (tablet) */
    private final boolean mTwoPane;

    /** The currently selected item in the RV. Needed for highlighting etc. */
    private int mSelected = -1;

    /** Normal background for a Location list item */
    private Drawable mNormalBackground;

    /** The background to use for a Location list item that is currently selected */
    private Drawable mHighlightBackground;


    //------------------------
    //  listener
    //------------------------

    /** Click Listener for the items in the RecyclerView. */
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // The tag tells which location we're dealing with
            int currentlySelected = (int) view.getTag();
            Location location = mFilteredLocationList.get(currentlySelected);

            // redraw the selection
            notifyItemChanged(mSelected);
            notifyItemChanged(currentlySelected);
            mSelected = currentlySelected;

/*
            todo: implement this!!!!

            if (mTwoPane) {
                // Start the new Fragment
                Bundle bundle = new Bundle();
                bundle.putLong(LocationDetailFragment.LOCATION_ID_KEY, location.id);

                LocationDetailFragment frag = new LocationDetailFragment();
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

                intent.putExtra(LocationDetailFragment.LOCATION_ID_KEY, location.id);
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
        mFilteredLocationList = filterBookableLocations(mLocationList);

        mParentActivity = parent;
        mTwoPane = twoPane;

        // Pre-calculate the colors to avoid slowing down onBindViewHolder
        mNormalBackground = parent.getDrawable(R.drawable.rounded_normal);
        mHighlightBackground = parent.getDrawable(R.drawable.rounded_selected);
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
        Location loc = mFilteredLocationList.get(position);
        holder.id = loc.id;

        holder.nameTv.setText(loc.name);
        holder.airportCodeTv.setText(loc.airport_code);
        holder.descriptionTv.setText(loc.description);

        // set the images
        String photoUrl = ModelWindow.IMAGE_BASE_URL + ModelWindow.IMAGE_ASSET_SPECIFIER +
                loc.asset_code + ModelWindow.IMAGE_FILENAME_SUFFIX;
        Picasso.with(mParentActivity).load(photoUrl).into(holder.locationIv);


        // Store the adapter position of this item for use during onClick
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mOnClickListener);

        // Possibly highlight this as a selected item
        if (position == mSelected) {
            holder.backgroundView.setBackground(mHighlightBackground);
        }
        else {
            holder.backgroundView.setBackground(mNormalBackground);
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredLocationList.size();
    }


    /**
     * Returns a list with all the non-bookable locations removed
     *
     * If the input is null or empty, then that's what is returned.
     */
    private List<Location> filterBookableLocations(List<Location> inList) {

        if ((inList == null) || (inList.size() == 0)) {
            return inList;
        }

        List<Location> filtered = new ArrayList<>();
        for (Location loc : inList) {
            if (loc.bookable) {
                filtered.add(loc);
            }
        }
        return filtered;
    }


    /**
     * Figures out and filters our locations list.  Sorry, but this works
     * pretty much entirely by side-effect.  todo: make this functional!
     *
     * preconditions:
     *      mLocationList       Holds all the locations that we're interested in
     *
     * side-effects:
     *      mFilteredLocationList   Holds only the items that fit our filter criteria
     *
     * @return
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if ((constraint == null) || (constraint.length() == 0)) {
                    // no constraints, use entire list
                    mFilteredLocationList = mLocationList;
                    return null;
                }
                List<Location> filteredList = new ArrayList<>();
                for (Location loc : mLocationList) {
                    // check here for matches and other filter criteria
                    // todo: add search bar to our condition
                    if (loc.bookable) {
                        filteredList.add(loc);
                    }
                }

                mFilteredLocationList = filteredList;

                FilterResults results = new FilterResults();
                results.values = mFilteredLocationList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults filterResults) {
                // todo: check to see if this is redundant with the above side-effect

                if ((filterResults != null) && (filterResults.values != null)) {
                    mFilteredLocationList = (List<Location>) filterResults.values;

                    notifyDataSetChanged();
                }
            }
        };

    } // getFilter()


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
