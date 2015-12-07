package factory9.checkinmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import factory9.checkinmobile.GuestFragment.OnListFragmentInteractionListener;
import factory9.checkinmobile.guests.GuestContent;


/**
 * {@link RecyclerView.Adapter} that can display a {@link factory9.checkinmobile.guests.GuestContent.GuestItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyGuestRecyclerViewAdapter extends RecyclerView.Adapter<MyGuestRecyclerViewAdapter.ViewHolder> {

    private final List<GuestContent.GuestItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyGuestRecyclerViewAdapter(List<GuestContent.GuestItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_guest, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).name);
        holder.mEmailView.setText(mValues.get(position).email);
        holder.mGroupView.setText(mValues.get(position).group);
        holder.mCheckInTimeView.setText(mValues.get(position).checkInTime);


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mEmailView;
        public final TextView mGroupView;
        public final TextView mCheckInTimeView;

        public GuestContent.GuestItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            mCheckInTimeView = (TextView) view.findViewById(R.id.day);
            mEmailView = (TextView) view.findViewById(R.id.email);
            mGroupView = (TextView) view.findViewById(R.id.group);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.barcode + "'";
        }
    }
}
