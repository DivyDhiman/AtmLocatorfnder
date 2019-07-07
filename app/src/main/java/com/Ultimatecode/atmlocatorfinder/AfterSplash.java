package com.Ultimatecode.atmlocatorfinder;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import controlers.all.AnalyticsApplication;
import helpwindow.MainSwipe;

public class AfterSplash extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Intent intent;
    private Context context;
    private Dialog dialog;
    private CheckBox priorityaccuratyhigh, priorityaccuratybalance, priorityaccuratylow;
    private SeekBar radius;
    private int progressvalues, maxV = 10;
    private TextView radiusdidisplay;
    private String checkboxcheck, check_again;
    private LinearLayout linearLayout_atm, linearLayout_bank;
    private boolean checkfirsttime;
    private AnalyticsApplication analyticsApplication;
    private CoordinatorLayout coordinate_layout;
    private final int PERMISSION_REQUEST_CODE = 1, PERMISSION_REQUEST_CODE2 = 2, PERMISSION_REQUEST_CODE3 = 3, PERMISSION_REQUEST_CODE4 = 4;
    private Activity activity;
    private int check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.after_splash);
        context = AfterSplash.this;
        activity = this;

        analyticsApplication = new AnalyticsApplication();
        setCustomActionBar();

        check_again = "false";
        initilize();
    }

    private void initilize() {
        coordinate_layout = (CoordinatorLayout) findViewById(R.id.coordinate_layout);
        linearLayout_atm = (LinearLayout) findViewById(R.id.linearLayout_atm);
        linearLayout_bank = (LinearLayout) findViewById(R.id.linearLayout_bank);

        linearLayout_atm.setOnClickListener(this);
        linearLayout_bank.setOnClickListener(this);
    }


    private boolean checkPermission(String access) {
        int result = ContextCompat.checkSelfPermission(context, access);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(final String access, final int request_code) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, access)) {
            ActivityCompat.requestPermissions(activity, new String[]{access}, request_code);

        } else {
            ActivityCompat.requestPermissions(activity, new String[]{access}, request_code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        if (checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    if (check == 1) {
                                        cick_method("atm", "atmtrue");
                                    } else if (check == 2) {
                                        cick_method("bank", "banktrue");
                                    }
                                } else {
                                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE4);
                                }
                            } else {
                                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE3);
                            }
                        }
                    }
                }
                break;

            case PERMISSION_REQUEST_CODE2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                if (check == 1) {
                                    cick_method("atm", "atmtrue");
                                } else if (check == 2) {
                                    cick_method("bank", "banktrue");
                                }
                            } else {
                                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE4);
                            }
                        } else {
                            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE3);
                        }
                    }
                }
                break;

            case PERMISSION_REQUEST_CODE3:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            if (check == 1) {
                                cick_method("atm", "atmtrue");
                            } else if (check == 2) {
                                cick_method("bank", "banktrue");
                            } else {
                                Gallerypic();
                            }
                        }
                    }
                }
                break;

            case PERMISSION_REQUEST_CODE4:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (check == 1) {
                            cick_method("atm", "atmtrue");
                        } else if (check == 2) {
                            cick_method("bank", "banktrue");
                        } else {
                            Gallerypic();
                        }
                    }
                }
                break;

        }
    }

    public void cick_method(String name, String name2) {
        checkfirsttime = (boolean) analyticsApplication.get_all(context, "firsttime", getString(R.string.Boolean));
        if (checkfirsttime) {
            callmethod(name);
        } else {
            check_again = name2;
            dialogShowSetting();
        }
    }

    public void check_apiversion(int i) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (i == 1 || i == 2) {
                if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                if (i == 1) {
                                    cick_method("atm", "atmtrue");
                                } else if (i == 2) {
                                    cick_method("bank", "banktrue");
                                }
                            } else {
                                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE4);
                            }
                        } else {
                            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE3);
                        }

                    } else {
                        requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION, PERMISSION_REQUEST_CODE2);
                    }
                } else {
                    requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE);
                }
            } else {
                if (checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Gallerypic();
                    } else {
                        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE4);
                    }
                } else {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, PERMISSION_REQUEST_CODE3);
                }
            }

        } else {
            if (i == 1) {
                cick_method("atm", "atmtrue");
            } else if (i == 2) {
                cick_method("bank", "banktrue");
            } else {
                Gallerypic();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.linearLayout_atm:
                check = 1;
                check_apiversion(1);
                break;

            case R.id.linearLayout_bank:
                check = 1;
                check_apiversion(2);
                break;

            case R.id.priorityaccuratyhigh:

                checkbocall("one", true, false, false);

                break;

            case R.id.priorityaccuratybalance:

                checkbocall("two", false, true, false);

                break;

            case R.id.priorityaccuratylow:

                checkbocall("three", false, false, true);

                break;

            case R.id.save_changes:
                checkfirsttime = (boolean) analyticsApplication.get_all(context, "firsttime",

                        getString(R.string.Boolean)

                );
                if (checkfirsttime)

                {
                    analyticsApplication.set_all(context, "checkbox", checkboxcheck, getString(R.string.String));
                    analyticsApplication.set_all(context, "pvalue", progressvalues, getString(R.string.Integer));
                    dialog.dismiss();
                } else

                {
                    analyticsApplication.set_all(context, "firsttime", true, getString(R.string.Boolean));
                    analyticsApplication.set_all(context, "checkbox", checkboxcheck, getString(R.string.String));
                    analyticsApplication.set_all(context, "pvalue", progressvalues, getString(R.string.Integer));
                    dialog.dismiss();
                    if (check_again.equals("atmtrue")) {
                        callmethod("atm");
                    } else if (check_again.equals("banktrue")) {
                        callmethod("bank");
                    }
                }

                break;

            case R.id.setting_coustomaction:

                PopupMenu popupMenu = new PopupMenu(context, v) {
                    @Override
                    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.savesetting:
                                check_again = "false";
                                dialogShowSetting();
                                return true;

                            case R.id.virtualmap:
                                check = 3;
                                check_apiversion(3);
                                return true;

                            case R.id.help:
                                intent = new Intent(context, MainSwipe.class);
                                intent.putExtra("firstt", true);
                                startActivity(intent);
                                return true;
                            default:
                                return super.onMenuItemSelected(menu, item);

                        }
                    }
                };

                popupMenu.inflate(R.menu.menu);
                popupMenu.show();
                break;

            case R.id.dialog_cancel:
                dialog.dismiss();
                break;

        }
    }

    private void checkbocall(String type, boolean one, boolean two, boolean three) {
        checkboxcheck = type;

        priorityaccuratyhigh.setChecked(one);
        priorityaccuratybalance.setChecked(two);
        priorityaccuratylow.setChecked(three);

    }

    private void callmethod(String value) {
        intent = new Intent(AfterSplash.this, MapsActivity.class);
        intent.putExtra("identify", value);
        startActivity(intent);
        analyticsApplication.Animation_all(context, R.anim.leftanim, R.anim.leftanimmain);
    }

    //Custom Action Bar
    private void setCustomActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView text_coustomaction = (TextView) findViewById(R.id.text_coustomaction);
        ImageView setting_coustomaction = (ImageView) findViewById(R.id.setting_coustomaction);
        setting_coustomaction.setOnClickListener(this);
        text_coustomaction.setText(R.string.atmbanklocator);

    }

    private void Gallerypic() {
        ArrayList<HashMap<String, Object>> imagelist;
        HashMap<String, Object> imagelistsub;

        File[] listFile;

        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(android.os.Environment.getExternalStorageDirectory(), "VirtualMapImages");

            imagelist = new ArrayList<>();

            if (!file.exists()) {
                file.mkdirs();

            }

            listFile = file.listFiles();
            if (listFile.length == 0) {
                analyticsApplication.Shackbarall(coordinate_layout, getString(R.string.noimage));
            } else {

                for (int i = 0; i < listFile.length; i++) {

                    imagelistsub = new HashMap<>();
                    imagelistsub.put("imagepath", listFile[i].getAbsolutePath());
                    imagelistsub.put("imagename", listFile[i].getName());
                    imagelist.add(imagelistsub);
                }

                intent = new Intent(AfterSplash.this, Showallimages.class);
                intent.putExtra("imagelist", imagelist);
                startActivity(intent);
            }

        }
    }

    private void dialogShowSetting() {

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radius_dialouge);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        radius = (SeekBar) dialog.findViewById(R.id.radius);
        priorityaccuratyhigh = (CheckBox) dialog.findViewById(R.id.priorityaccuratyhigh);
        priorityaccuratybalance = (CheckBox) dialog.findViewById(R.id.priorityaccuratybalance);
        priorityaccuratylow = (CheckBox) dialog.findViewById(R.id.priorityaccuratylow);
        radiusdidisplay = (TextView) dialog.findViewById(R.id.radiusdidisplay);
        Button save_changes = (Button) dialog.findViewById(R.id.save_changes);
        Button dialog_cancel = (Button) dialog.findViewById(R.id.dialog_cancel);

        checkboxcheck = String.valueOf(analyticsApplication.get_all(context, "checkbox", getString(R.string.String)));

        if ("one".equals(checkboxcheck)) {
            priorityaccuratyhigh.setChecked(true);

        } else if ("two".equals(checkboxcheck)) {
            priorityaccuratybalance.setChecked(true);

        } else if ("three".equals(checkboxcheck)) {
            priorityaccuratylow.setChecked(true);
        } else {
            checkboxcheck = "three";
            analyticsApplication.set_all(context, "checkbox", checkboxcheck, getString(R.string.String));
            priorityaccuratylow.setChecked(true);
        }

        progressvalues = (int) analyticsApplication.get_all(context, "pvalue", getString(R.string.Integer));
        radius.setMax(maxV);

        if (progressvalues == 0) {
            progressvalues = 2;
            radius.setProgress(progressvalues);
            analyticsApplication.set_all(context, "pvalue", progressvalues, getString(R.string.Integer));
            radiusdidisplay.setText(getString(R.string.Radius) + progressvalues + "Km");

        } else {
            radius.setProgress(progressvalues);
            radiusdidisplay.setText(getString(R.string.Radius) + progressvalues + "Km");
        }

        radius.setOnSeekBarChangeListener(this);
        priorityaccuratyhigh.setOnClickListener(this);
        priorityaccuratybalance.setOnClickListener(this);
        priorityaccuratylow.setOnClickListener(this);
        dialog_cancel.setOnClickListener(this);

        save_changes.setOnClickListener(this);
        dialog.show();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        radiusdidisplay.setText(getString(R.string.Radius) + progressvalues + "Km");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progressvalues = seekBar.getProgress();
        radiusdidisplay.setText(getString(R.string.Radius) + progressvalues + "Km");
    }

}
