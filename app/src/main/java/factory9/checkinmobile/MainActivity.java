package factory9.checkinmobile;


import android.content.DialogInterface;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import factory9.checkinmobile.guests.GuestContent;


public class MainActivity extends FragmentActivity
        implements View.OnClickListener, MainFragment.OnFragmentInteractionListener,
        BarcodeSuccessFragment.OnFragmentInteractionListener,
        BarcodeLoadingFragment.OnFragmentInteractionListener,
        BarcodeFailureFragment.OnFragmentInteractionListener,
        GuestFragment.OnListFragmentInteractionListener {

    //UI instance variables
    private Button scanBtn;
    private Button searchBtn;
    private Button checkBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE); // for hiding title

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            loadMainFragment();
        }




        checkBtn = (Button) findViewById(R.id.check_button);
        searchBtn = (Button) findViewById(R.id.search_button);
        scanBtn = (Button) findViewById(R.id.scan_button);

        checkBtn.setOnClickListener(this);
        scanBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.scan_button) {
            //instantiate ZXing integration class
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            //start scanning
            scanIntegrator.initiateScan();
        } else if (v.getId() == R.id.search_button) {
            loadMainFragment();
        } else if (v.getId() == R.id.check_button) {
            showGuestList();
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {


        //retrieve result of scanning - instantiate ZXing object
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        //check we have a valid result
        if (scanningResult != null) {
            //get content from Intent Result
            String scanContent = scanningResult.getContents();


            // Perform the CHECKIN ACTION only if barcode is present
            if (scanContent != null && !scanContent.isEmpty()) {
                // Perform check-in
                doCheckin(scanContent);
            }


        } else {
            //invalid scan data or scan canceled
            Toast toast = Toast.makeText(getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }



    public void doCheckin(final String barcode){
        showLoadingScreen();

        GuestRequest guestRequest = new GuestRequest();
        RequestParams params = new RequestParams();

        params.put("id", barcode);

        guestRequest.checkin(params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.w("HTTP/OK: ", response.toString());

                //String group = response.get("group").toString();
                //String checkinAt = response.get("checkin_at").toString();
                loadBarcodeSuccessFragment(setBundle(barcode, "", ""));
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                loadBarcodeFailureFragment(setBundle(barcode, "", ""));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String something, Throwable e) {
                loadBarcodeFailureFragment(setBundle(barcode, "", ""));
            }

            public Bundle setBundle(String barcode, String group, String checkinAt){
                Bundle bundle = new Bundle();
                bundle.putString("barcode", barcode);
                bundle.putString("group", group);
                bundle.putString("checkinAt", checkinAt);
                return bundle;
            }
        });
    }


    public void showGuestList(){
        super.onPostResume();

        Bundle bundle = new Bundle();
        bundle.putString("query", "");


        // Load the Guest List based on search
        GuestFragment gf = new GuestFragment();
        gf.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.frameFragment, gf);

        ft.addToBackStack(null);
        ft.commit();
    }

    public void showLoadingScreen(){
        super.onPostResume();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        BarcodeLoadingFragment ob = new BarcodeLoadingFragment();

        ft.replace(R.id.frameFragment, ob);
        ft.commit();


    }

    public void loadMainFragment() {
        super.onPostResume();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        MainFragment ob = new MainFragment();

        ft.replace(R.id.frameFragment, ob);
        ft.commit();

    }

    public void loadBarcodeFailureFragment(Bundle bundle) {
        super.onPostResume();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        BarcodeFailureFragment ob = new BarcodeFailureFragment();

        ob.setArguments(bundle);
        ft.replace(R.id.frameFragment, ob);
        ft.commit();

    }


    public void loadBarcodeSuccessFragment(Bundle bundle) {
        super.onPostResume();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        BarcodeSuccessFragment ob = new BarcodeSuccessFragment();

        ob.setArguments(bundle);
        ft.replace(R.id.frameFragment, ob);
        ft.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Implementation of OnFragment Interface
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }


    @Override
    public void onListFragmentInteraction(final GuestContent.GuestItem item) {

        Log.w("CLICK", item.barcode);

        final String barcode = item.barcode;

        new AlertDialog.Builder(this)
                .setTitle("Confirma o check-in?")
                .setMessage("Deseja fazer o check in deste convidado?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        doCheckin(barcode);

                        //Toast.makeText(MainActivity.this, ":-)", Toast.LENGTH_SHORT).show();
                    }}
                )
                .setNegativeButton(android.R.string.no, null).show();    }
}
