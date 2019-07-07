package adpaterrecycler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Ultimatecode.atmlocatorfinder.PlaceDirectionMap;
import com.Ultimatecode.atmlocatorfinder.R;
import com.appnext.ads.fullscreen.FullScreenVideo;
import com.appnext.ads.interstitial.Interstitial;
import com.appnext.core.callbacks.OnAdLoaded;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Funality_class.DataPass;
import GetApprequest.ApiResponseGet;
import controlers.all.AnalyticsApplication;

public class AdptnearContent extends RecyclerView.Adapter<AdptnearContent.ViewHolder> implements View.OnClickListener {

    private View view;
    private Context resultScreen;
    private Intent intent;
    private ArrayList<HashMap<String, Object>> storeinfo;
    private String response, status, type_mode, getfilename, nextpage_token;
    private ProgressDialog pDialog;
    private ApiResponseGet apiResponseGet;
    private Double latitude, longitude;
    private ArrayList<HashMap<String, Object>> storevirtualmapinfo;
    private HashMap<String, Object> storevirtualmapinfosub;
    private LinearLayout content;
    private Dialog dialog;
    private int i, j;
    private List<LatLng> polyz;
    private Object latdes2, lngdes2;
    private EditText savefilename;
    private AnalyticsApplication analyticsApplication;
    private DataPass dataPass;
    private int count;
    private Activity activity;
    Interstitial interstitial_Ad;
    FullScreenVideo fullscreen_ad;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_view:
                save_frame();
                break;

            case R.id.dialog_submit:

                next_activity();
                dialog.dismiss();

                break;

            case R.id.dialog_cancel:
                dialog.dismiss();
                break;
        }

    }

    private void next_activity() {
        intent = new Intent(resultScreen, PlaceDirectionMap.class);
        intent.putExtra("Poly", (Serializable) polyz);
        intent.putExtra("InfoData", storevirtualmapinfo);
        intent.putExtra("latdes", String.valueOf(latdes2));
        intent.putExtra("lngdes", String.valueOf(lngdes2));
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        resultScreen.startActivity(intent);

        ((Activity) resultScreen).overridePendingTransition(R.anim.slide_up_info, R.anim.no_change);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public TextView text_allresult, text_allvicinity;
        private CardView card_view;

        public ViewHolder(View view) {
            super(view);

            mView = view;

            text_allresult = (TextView) view.findViewById(R.id.text_allresult);
            text_allvicinity = (TextView) view.findViewById(R.id.text_allvicinity);
            card_view = (CardView) view.findViewById(R.id.card_view);

        }
    }

    public AdptnearContent(Context context, ArrayList<HashMap<String, Object>> storeinfo2, Double lat, Double lng, String next_token, Activity activity) {
        resultScreen = context;
        storeinfo = storeinfo2;
        latitude = lat;
        longitude = lng;
        nextpage_token = next_token;
        this.activity = activity;
    }

    public void add(ArrayList<HashMap<String, Object>> dataadd_all, String token) {
        for (int i = 0; i < dataadd_all.size(); i++) {
            HashMap data_add = new HashMap<>();
            data_add = dataadd_all.get(i);
            storeinfo.add(data_add);
            nextpage_token = token;
        }
    }

    public void save_frame() {
        try {
            content.setDrawingCacheEnabled(true);
            Bitmap bitmap = content.getDrawingCache();
            File file, f = null;
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
                file = new File(android.os.Environment.getExternalStorageDirectory(), "VirtualMapImages");
                if (!file.exists()) {
                    file.mkdirs();
                }

                if (isEmptye(savefilename) == true) {
                    savefilename.setError(resultScreen.getString(R.string.filename));
                } else {

                    i = (int) analyticsApplication.get_all(resultScreen, "file", resultScreen.getString(R.string.Integer));
                    if (0 == i) {
                        i = 1;
                        analyticsApplication.set_all(resultScreen, "file", i, resultScreen.getString(R.string.Integer));
                    } else {
                        i = i + 1;
                        analyticsApplication.set_all(resultScreen, "file", i, resultScreen.getString(R.string.Integer));
                    }

                    getfilename = savefilename.getText().toString();

                    f = new File(file.getAbsolutePath() + File.separator + getfilename + ".png");
                }
                FileOutputStream ostream = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
                ostream.close();
                Toast.makeText(resultScreen, resultScreen.getString(R.string.savevirtual), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(resultScreen, resultScreen.getString(R.string.imagefailed), Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adptnear_content, parent, false);

        apiResponseGet = new ApiResponseGet();
        analyticsApplication = new AnalyticsApplication();
        dataPass = (DataPass) resultScreen;

        // set the ad unit ID

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdptnearContent.ViewHolder holder, final int position) {


        if (position == storeinfo.size() - 1) {
            if (!nextpage_token.equals("NoToken")) {
                dataPass.Hit_api(true);
            } else {
                dataPass.Hit_api(false);
            }

        }
        holder.text_allresult.setText(storeinfo.get(position).get("name").toString());
        holder.text_allvicinity.setText(storeinfo.get(position).get("vicinity").toString());

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = (int) analyticsApplication.get_all(resultScreen, "Count", resultScreen.getString(R.string.Integer));
                if (count == 0) {
                    count = count +1;
                    analyticsApplication.set_all(resultScreen, "Count", count, resultScreen.getString(R.string.Integer));
                    type_mode = String.valueOf(analyticsApplication.get_all(resultScreen, "Mode", resultScreen.getString(R.string.String)));
                    latdes2 = storeinfo.get(position).get("lat");
                    lngdes2 = storeinfo.get(position).get("lng");

                    new directionalldetail(storeinfo.get(position).get("lat"), storeinfo.get(position).get("lng"), storeinfo.get(position).get("name").toString()).execute();

                } else if (count == 2 || count == 5 || count == 7) {
                    if (count == 2) {
                        Addshow();
                        count = count + 1;
                        analyticsApplication.set_all(resultScreen, "Count", count, resultScreen.getString(R.string.Integer));
                    } else if (count == 5) {
                        Addshow();
                        count = count+1;
                        analyticsApplication.set_all(resultScreen, "Count", count, resultScreen.getString(R.string.Integer));
                    }else if (count == 7) {
                        Addshow2();
                        count = 3;
                        analyticsApplication.set_all(resultScreen, "Count", count, resultScreen.getString(R.string.Integer));
                    }
                } else {
                    count = count + 1;
                    analyticsApplication.set_all(resultScreen, "Count", count, resultScreen.getString(R.string.Integer));
                    type_mode = String.valueOf(analyticsApplication.get_all(resultScreen, "Mode", resultScreen.getString(R.string.String)));
                    latdes2 = storeinfo.get(position).get("lat");
                    lngdes2 = storeinfo.get(position).get("lng");

                    new directionalldetail(storeinfo.get(position).get("lat"), storeinfo.get(position).get("lng"), storeinfo.get(position).get("name").toString()).execute();
                }
            }
        });
    }

    private void Addshow() {
        interstitial_Ad = new Interstitial(activity, resultScreen.getString(R.string.interstitial_full_screen));
        interstitial_Ad.loadAd();
        interstitial_Ad.setOnAdLoadedCallback(new OnAdLoaded() {
            @Override
            public void adLoaded() {
                interstitial_Ad.showAd();
            }
        });
    }

    private void Addshow2() {
        fullscreen_ad = new FullScreenVideo(activity, resultScreen.getString(R.string.interstitial_full_screen));
        fullscreen_ad.loadAd();
        fullscreen_ad.setOnAdLoadedCallback(new OnAdLoaded() {
            @Override
            public void adLoaded() {
                fullscreen_ad.showAd();
            }
        });
    }

    @Override
    public int getItemCount() {
        return storeinfo.size();
    }


    class directionalldetail extends AsyncTask<String, String, String> {
        Double latdes, lngdes;
        String desname;

        public directionalldetail(Object lat, Object lng, String name) {
            latdes = Double.valueOf(lat.toString());
            lngdes = Double.valueOf(lng.toString());
            desname = name;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(resultScreen);
            pDialog.setMessage(resultScreen.getString(R.string.Pleasewait));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {

            storevirtualmapinfo = new ArrayList<>();


            try {
                String s = "http://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude + "&destination=" + latdes + "," + lngdes + "&sensor=false&mode=" + type_mode;
                response = apiResponseGet.getapi(s);

                JSONObject jsonObject = new JSONObject(response);
                status = jsonObject.getString("status");
                if ("OK".equals(status)) {

                    JSONArray routesArray = jsonObject.getJSONArray("routes");
                    JSONObject route = routesArray.getJSONObject(0);
                    JSONArray jsonArray = route.getJSONArray("legs");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    storevirtualmapinfosub = new HashMap<>();

                    JSONObject jsonObject2 = jsonObject1.getJSONObject("distance");
                    storevirtualmapinfosub.put("totaldistance", jsonObject2.getString("text"));

                    JSONObject jsonObject3 = jsonObject1.getJSONObject("duration");
                    storevirtualmapinfosub.put("totaltime", jsonObject3.getString("text"));

                    storevirtualmapinfosub.put("mapvInfo", "My Location");
                    storevirtualmapinfosub.put("Destination", desname);
                    storevirtualmapinfo.add(storevirtualmapinfosub);

                    storevirtualmapinfosub = new HashMap<>();
                    storevirtualmapinfosub.put("mapvInfo", jsonObject1.getString("start_address"));
                    storevirtualmapinfo.add(storevirtualmapinfosub);

                    storevirtualmapinfosub = new HashMap<>();
                    storevirtualmapinfosub.put("mapvInfo", jsonObject1.getString("end_address"));
                    storevirtualmapinfo.add(storevirtualmapinfosub);

                    storevirtualmapinfosub = new HashMap<>();
                    storevirtualmapinfosub.put("mapvInfo", resultScreen.getString(R.string.Destination) + "\n" + desname);
                    storevirtualmapinfo.add(storevirtualmapinfosub);

                    JSONObject poly = route.getJSONObject("overview_polyline");
                    String polyline = poly.getString("points");
                    polyz = decodePoly(polyline);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();


            if (response != null) {

                if ("OK".equals(status)) {
                    dialogShowForgot();
                } else {
                    dataPass.Data_send("noplace");
                }

            } else {
                dataPass.Data_send("internetfailed");
            }
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }


    //Show dialouge for adding new stall
    private void dialogShowForgot() {

        dialog = new Dialog(resultScreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdialog_pass);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextView total_time = (TextView) dialog.findViewById(R.id.total_time);
        TextView total_dis = (TextView) dialog.findViewById(R.id.total_dis);
        savefilename = (EditText) dialog.findViewById(R.id.savefilename);

        total_time.setText(resultScreen.getString(R.string.TotalTime) + storevirtualmapinfo.get(0).get("totaltime").toString());
        total_dis.setText(resultScreen.getString(R.string.TotalDistance) + storevirtualmapinfo.get(0).get("totaldistance").toString());

        RecyclerView recycler_virtualview = (RecyclerView) dialog.findViewById(R.id.recycler_virtualview);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(recycler_virtualview.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycler_virtualview.setLayoutManager(layoutManager);
        recycler_virtualview.setAdapter(new AdapterVirtualMAp(recycler_virtualview.getContext(), storevirtualmapinfo));

        Button dialog_submit = (Button) dialog.findViewById(R.id.dialog_submit);
        Button dialog_cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
        Button save_view = (Button) dialog.findViewById(R.id.save_view);
        content = (LinearLayout) dialog.findViewById(R.id.frame_save);

        j = (int) analyticsApplication.get_all(resultScreen, "file", resultScreen.getString(R.string.Integer));

        if (0 == j) {
            j = 1;
            savefilename.setText("virtualmap" + String.valueOf(j));
        } else {
            j = j + 1;
            savefilename.setText("virtualmap" + String.valueOf(j));
        }


        save_view.setOnClickListener(this);
        dialog_submit.setOnClickListener(this);

        dialog_cancel.setOnClickListener(this);
        dialog.show();

    }


    private boolean isEmptye(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}




