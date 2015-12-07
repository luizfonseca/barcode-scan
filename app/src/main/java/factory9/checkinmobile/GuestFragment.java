package factory9.checkinmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;


import cz.msebera.android.httpclient.Header;
import factory9.checkinmobile.guests.GuestContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class GuestFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public ProgressDialog progress;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GuestFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static GuestFragment newInstance(int columnCount) {
        GuestFragment fragment = new GuestFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        String query = getArguments().getString("query");

        View view = inflater.inflate(R.layout.fragment_guest_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            // Initializing the GuestContent first to get the Items;
            setGuests(recyclerView, query);

        }
        return view;
    }


    private void setGuests(final RecyclerView rObject, String param){
        Log.w("REQUEST", "Calling");
        RequestParams params = new RequestParams();
        params.add("query", param);

        GuestRequest gg = new GuestRequest();

        progress = new ProgressDialog(getActivity());
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage("Carregando");

        progress.show();


        gg.getGuests(params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("SUCCESS OBJECT", response.toString());
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.w("SUCCESS ARRAY", response.toString());
                GuestContent.ITEMS.clear();
                GuestContent.ITEM_MAP.clear();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jObj = response.getJSONObject(i);

                        String name = jObj.getString("name");
                        String email = jObj.getString("email");
                        String date = jObj.getString("scheduled_at");
                        String group = jObj.getString("group");
                        String barcode = jObj.getString("barcode");

                        //Log.w("ITEM", name);
                        GuestContent.addItem(
                                GuestContent.createGuestItem(i + 1, name, email, date, group, barcode));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                rObject.setAdapter(new MyGuestRecyclerViewAdapter(GuestContent.ITEMS, mListener));
                progress.dismiss();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                Log.w("FAILURE", response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String something, Throwable e) {
                Log.w("FAILURE", something);

            }
        });

    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(GuestContent.GuestItem item);
    }
}
