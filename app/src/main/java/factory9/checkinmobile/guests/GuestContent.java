package factory9.checkinmobile.guests;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import factory9.checkinmobile.GuestRequest;


public class GuestContent {


    /**

     * An array of Guest items.
     */
    public static final List<GuestItem> ITEMS = new ArrayList<GuestItem>();

    /**
     * A map of Guest items, by ID.
     */
    public static final Map<String, GuestItem> ITEM_MAP = new HashMap<String, GuestItem>();



    public static void addItem(GuestItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    public static GuestItem createGuestItem(int id, String name, String email, String date, String group, String barcode){
        return new GuestItem(String.valueOf(id), name, email, date, group, barcode);
    }
//
//    private static GuestItem createDummyItem(int position) {
//        return new GuestItem(String.valueOf(position), "Item " + position, makeDetails(position));
//    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class GuestItem {
        public final String id;

        public final String name;
        public final String email;
        public final String checkInTime;
        public final String group;
        public final String barcode;

        public GuestItem(String id, String name, String email, String date, String group, String barcode) {
            this.id = id;
            this.name = name;
            this.email = email;
            this.checkInTime = date;
            this.group = group;
            this.barcode = barcode;

            // Old asserting.
            //this.content = content;
            //this.details = details;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
