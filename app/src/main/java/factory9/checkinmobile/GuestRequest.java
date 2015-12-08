package factory9.checkinmobile;


import com.loopj.android.http.*;

public class GuestRequest {

    private static AsyncHttpClient client = new AsyncHttpClient();

    private String baseAddress = "http://192.168.1.4:8089";
    private String endpoint = "/api/guests.json";



    public void getGuests(RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(this.guestUrl(), params, responseHandler);
    }

    public void checkin(RequestParams params, AsyncHttpResponseHandler responseHandler){
        client.put(this.guestUrl(), params, responseHandler);
    }



    public String guestUrl(){
        return this.baseAddress + this.endpoint;
    }

}
