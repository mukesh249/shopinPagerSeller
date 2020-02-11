package shopinpager.wingstud.shopinpagerseller.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import shopinpager.wingstud.shopinpagerseller.Api.ApiConfig;
import shopinpager.wingstud.shopinpagerseller.Api.AppConfig;
import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.adapter.MultipleDeliveryPincodeAdapter;
import shopinpager.wingstud.shopinpagerseller.databinding.ProfileCompleteBinding;
import shopinpager.wingstud.shopinpagerseller.model.SelectedPinModel;
import shopinpager.wingstud.shopinpagerseller.model.StateModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static shopinpager.wingstud.shopinpagerseller.util.Utils.Tosat;

public class ProfileComplete extends AppCompatActivity implements WebCompleteTask , GoogleApiClient.OnConnectionFailedListener{

    ProfileCompleteBinding binding;
    int count = 1;
    private ArrayList<StateModel> arrayList = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> stateId = new ArrayList<>();

    ArrayList<String> cityID = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();

    ArrayList<String> pincodeID = new ArrayList<>();
    ArrayList<String> pincodeName = new ArrayList<>();

    private String country_id = "101", state_id = "", city_id = "", pin_id = "", seller_agreement_des = "";
    String filename;
    Uri imageUri = null, profile_uri = null, seller_uri = null, cheque_uri = null, pan_uri = null;
    private int REQUEST_CAMERA_PERMISSION = 1;
    Boolean upLogo = false, seller = false, canceCheu = false, pan = false;
    JSONObject user_kyc;
    AlertDialog dialog;
    MultipleDeliveryPincodeAdapter deliveryPincodeAdapter;
    ArrayList<SelectedPinModel> selectedPinModelArrayList = new ArrayList<>();

    String logo_image_Url,seller_image_Url;

    private LatLng center;
    private Geocoder geocoder;
    private List<Address> addresses;
    double lati,longi;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.profile_complete);

        binding.toolbar.activityTitle.setText(getResources().getString(R.string.personal_info));
        binding.toolbar.bellRL.setVisibility(View.GONE);
        binding.toolbar.switchActive.setVisibility(View.GONE);

//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//        binding.toolbar.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
//        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        Places.initialize(ProfileComplete.this, getResources().getString(R.string.google_key));
        placesClient = Places.createClient(this);
        binding.pickupAddressEt.setOnClickListener(v -> {
            List<com.google.android.libraries.places.api.model.Place.Field>
                    fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
                    com.google.android.libraries.places.api.model.Place.Field.NAME,
                    com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                    com.google.android.libraries.places.api.model.Place.Field.LAT_LNG);

// Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .build(ProfileComplete.this);
            startActivityForResult(intent, 1);
        });

        binding.myLocation.setOnClickListener(v -> getLocation());

        deliveryPincodeAdapter = new MultipleDeliveryPincodeAdapter(ProfileComplete.this,selectedPinModelArrayList);
        binding.multiplePinCodeRv.setAdapter(deliveryPincodeAdapter);

        binding.profileCompletLl.setVisibility(View.VISIBLE);
        binding.kycLl.setVisibility(View.GONE);
        binding.sellerArgmentLL.setVisibility(View.GONE);

        binding.backBTN.setOnClickListener(v -> {
            if (count > 1)
                count--;
            ButtonShowHide(count);
        });
        binding.nextBTN.setOnClickListener(v -> {
            if (binding.nextBTN.getText().equals(getResources().getString(R.string.next))) {
                count++;
                ButtonShowHide(count);
            } else {
                if (binding.checkbox.isChecked())
                    profileStep3();
                else
                    retryAlertDialog("", "Please accept term and condition");
            }
        });
        ButtonShowHide(count);
        binding.countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (c.size()>0) {
                country_id = "101";
                StateApi();
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (stateId.size() > 0) {
                    state_id = stateId.get(position);
                    CityApi();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cityID.size() > 0) {
                    city_id = cityID.get(position);
                    DeliveryPincode();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.multiplePinCodeRv.setLayoutManager(new GridLayoutManager(ProfileComplete.this, 3));
        binding.uploadLogoLL.setOnClickListener(v -> {
            upLogo = true;
            seller = false;
            canceCheu = false;
            pan = false;
            ImageSeletePopUp();
        });
        binding.sellerImageLL.setOnClickListener(v -> {
            upLogo = false;
            seller = true;
            canceCheu = false;
            pan = false;
            ImageSeletePopUp();
        });
        binding.uploadCancelChequeLL.setOnClickListener(v -> {
            upLogo = false;
            seller = false;
            canceCheu = true;
            pan = false;
            ImageSeletePopUp();
        });
        binding.uploadPanCompyLL.setOnClickListener(v -> {
            upLogo = false;
            seller = false;
            canceCheu = false;
            pan = true;
            ImageSeletePopUp();
        });
        setFieldData();
    }

    public void setFieldData() {
        try {
            user_kyc = new JSONObject(getIntent().getExtras().getString("user_kyc", ""));
            Log.i("response_get_already", user_kyc + "");
            binding.fulNameEt.setText(SharedPrefManager.getUserName(Constrants.UserName));
            binding.mobileET.setText(SharedPrefManager.getMobile(Constrants.UserMobile));
            binding.emailEt.setText(SharedPrefManager.getUserEmail(Constrants.UserEmail));

            if (Utils.checkEmptyNull(user_kyc.optString("pincode")))
                binding.pincodeEt.setText(user_kyc.optString("pincode"));

            if (Utils.checkEmptyNull(user_kyc.optString("business_name")))
                binding.bussnessNameEt.setText(user_kyc.optString("business_name"));

            if (Utils.checkEmptyNull(user_kyc.optString("food_license_no")))
                binding.foodLicenseNoEt.setText(user_kyc.optString("food_license_no"));

            if (Utils.checkEmptyNull(user_kyc.optString("business_reg_no")))
                binding.businessRegNoEt.setText(user_kyc.optString("business_reg_no"));

            if (Utils.checkEmptyNull(user_kyc.optString("address_2")))
                binding.addressEt.setText(user_kyc.optString("address_2"));

            if (Utils.checkEmptyNull(user_kyc.optString("account_number")))
                binding.accountNoEt.setText(user_kyc.optString("account_number"));

            if (Utils.checkEmptyNull(user_kyc.optString("bank_name")))
                binding.bankNameEt.setText(user_kyc.optString("bank_name"));

            if (Utils.checkEmptyNull(user_kyc.optString("ifsc_code")))
                binding.ifscEt.setText(user_kyc.optString("ifsc_code"));

            if (Utils.checkEmptyNull(user_kyc.optString("account_holder_name")))
                binding.actHolderNameEt.setText(user_kyc.optString("account_holder_name"));

            if (Utils.checkEmptyNull(user_kyc.optString("pan_number")))
                binding.panNoEt.setText(user_kyc.optString("pan_number"));

            if (Utils.checkEmptyNull(user_kyc.optString("gst_number")))
                binding.gstNoEt.setText(user_kyc.optString("gst_number"));


            if (Utils.checkEmptyNull(user_kyc.optString("profile_image"))) {
                Utils.setImage(ProfileComplete.this, binding.logoImage,
                        WebUrls.ImageUrl + "/" + user_kyc.optString("profile_image"));
                logo_image_Url = WebUrls.ImageUrl + "/" + user_kyc.optString("profile_image");

            }

            if (Utils.checkEmptyNull(user_kyc.optString("pan_image")))
                Utils.setImage(ProfileComplete.this, binding.uploadPanCompyImage,
                        WebUrls.ImageUrl + "/" + user_kyc.optString("pan_image"));

            if (Utils.checkEmptyNull(user_kyc.optString("seller_image"))) {
                Utils.setImage(ProfileComplete.this, binding.sellerImage,
                        WebUrls.ImageUrl + "/" + user_kyc.optString("seller_image"));
                seller_image_Url = WebUrls.ImageUrl + "/" + user_kyc.optString("profile_image");
            }

            if (Utils.checkEmptyNull(user_kyc.optString("cancel_cheque")))
                Utils.setImage(ProfileComplete.this, binding.cancelChequeImage,
                        WebUrls.ImageUrl + "/" + user_kyc.optString("cancel_cheque"));


            if (Utils.checkEmptyNull(user_kyc.optString("delivery_pincode"))) {

                String delevieyPinAry[] = user_kyc.optString("delivery_pincode").split(",");
                for (int i = 0; i < delevieyPinAry.length; i++) {
                    SelectedPinModel selectedPinModel = new SelectedPinModel();
                    selectedPinModel.setPincode(delevieyPinAry[i]);
                    selectedPinModelArrayList.add(selectedPinModel);
                }

                deliveryPincodeAdapter.notifyDataSetChanged();

            }

            binding.deliveryPinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (pincodeName.size() > 0) {
//                    pin_id = pincodeID.get(position);
                        if (!pincodeID.get(position).equalsIgnoreCase("0")){
                            boolean select_mathc = false;
                            for (int i = 0; i < selectedPinModelArrayList.size(); i++) {
                                if (selectedPinModelArrayList.get(i).getPincode().equalsIgnoreCase(binding.deliveryPinSpinner.getSelectedItem().toString())) {
                                    Utils.Tosat(ProfileComplete.this, "Already selected");
                                    select_mathc = true;
                                    break;
                                } else {

                                }
                            }

                            if (!select_mathc) {
                                SelectedPinModel selectedPinModel = new SelectedPinModel();
//                            selectedPinModel.setId(pin_id);
                                selectedPinModel.setPincode(binding.deliveryPinSpinner.getSelectedItem().toString());
                                selectedPinModelArrayList.add(selectedPinModel);

                                deliveryPincodeAdapter.notifyDataSetChanged();
                            }
                        }

//                    if (selectedPinModelArrayList.contains(pincodeName.get(position))) {
//
//                    }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
    private Bitmap bitmap;

    private class ConvertUrlToBitmap extends AsyncTask<String, Long, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return true;
            } catch (Exception e) {
                Log.e("", e.toString());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean) {
                // download was successful
                // if you want to update your UI, make sure u do it on main thread. like this:
                ProfileComplete.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // update UI here
                    }
                });
            } else {
                // download failed
            }
        }
    }

    public void setData() {
        if (getIntent().getExtras() != null) {
            try {
                user_kyc = new JSONObject(getIntent().getExtras().getString("user_kyc", ""));

                if (user_kyc.optString("state_id") != null) {
                    binding.stateSpinner.setSelection(
                            getIndex(binding.stateSpinner,
                                    searchITem(stateId, stateName, user_kyc.optString("state_id"))));
                }

                //Utils.PrintMsg(user_kyc.optString("city_id") +"  :  "+searchITem(cityID, cityName, user_kyc.optString("city_id")));
//                if (user_kyc.optString("city_id") != null) {
//
//                    binding.citySpinner.setSelection(
//                            getIndex(binding.citySpinner,
//                                    searchITem(cityID, cityName, user_kyc.optString("city_id"))));
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCityData() {
        if (getIntent().getExtras() != null) {
            try {
                user_kyc = new JSONObject(getIntent().getExtras().getString("user_kyc", ""));
//
//                if (user_kyc.optString("state_id") != null) {
//                    binding.stateSpinner.setSelection(
//                            getIndex(binding.stateSpinner,
//                                    searchITem(stateId, stateName, user_kyc.optString("state_id"))));
//                }

                //Utils.PrintMsg(user_kyc.optString("city_id") +"  :  "+searchITem(cityID, cityName, user_kyc.optString("city_id")));
                if (user_kyc.optString("city_id") != null) {

                    binding.citySpinner.setSelection(
                            getIndex(binding.citySpinner,
                                    searchITem(cityID, cityName, user_kyc.optString("city_id"))));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String searchITem(ArrayList<String> IDarry, ArrayList<String> Namearry, String string) {
        for (int i = 0; i < IDarry.size(); i++) {
            if (string.equals(IDarry.get(i))) {
                return Namearry.get(i);
            }
        }
        return "";
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    public void ImageSeletePopUp() {
        try {
            final Dialog dialog = new Dialog(ProfileComplete.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.pop_profile);
            dialog.show();

            LinearLayout txtGallery = (LinearLayout) dialog.findViewById(R.id.layoutGallery);
            LinearLayout txtCamera = (LinearLayout) dialog.findViewById(R.id.layoutCamera);
            txtCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentAPIVersion = Build.VERSION.SDK_INT;
                    if (currentAPIVersion >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(ProfileComplete.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileComplete.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                        } else {

                            selectCameraImage();
                            dialog.dismiss();
                        }
                    } else {
                        selectCameraImage();
                        dialog.dismiss();
                    }
                }
            });
            txtGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentAPIVersion = Build.VERSION.SDK_INT;
                    if (currentAPIVersion >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(ProfileComplete.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProfileComplete.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                        } else {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
//                startActivityForResult(i, 100);
                        }
                    } else {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_PICK);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectCameraImage() {
        // TODO Auto-generated method stub
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 300);
    }

    public void ButtonShowHide(int count) {
        if (count == 1) {
            binding.toolbar.activityTitle.setText(getResources().getString(R.string.personal_info));
            binding.profileCompletLl.setVisibility(View.VISIBLE);
            binding.kycLl.setVisibility(View.GONE);
            binding.sellerArgmentLL.setVisibility(View.GONE);
            binding.nextBTN.setVisibility(View.VISIBLE);
            binding.backBTN.setVisibility(View.GONE);
            binding.nextBTN.setText(getResources().getString(R.string.next));
            binding.nextBTN.setBackground(getResources().getDrawable(R.drawable.grey_rounded_border));

        }
        if (count == 2) {
            profileStep1();
        }

        if (count == 3) {
            profileStep2();
        }
    }

    public void retryAlertDialog(String title, String msg) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.retry_alert, null);
        dialogBuilder.setView(dialogView);

        TextView txtRAMsg = (TextView) dialogView.findViewById(R.id.txtRAMsg);
        TextView txtRAFirst = (TextView) dialogView.findViewById(R.id.txtRAFirst);
        TextView txtRASecond = (TextView) dialogView.findViewById(R.id.txtRASecond);
        View deviderView = (View) dialogView.findViewById(R.id.deviderView);

        dialog = dialogBuilder.create();
        txtRAMsg.setText(msg);
        txtRAFirst.setText("Ok");
        txtRASecond.setVisibility(View.GONE);

        txtRAFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void profileStep1() {
        Log.i("asdfsa",selectedPinModelArrayList.size()+"");
        if (TextUtils.isEmpty(binding.fulNameEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "Full name required");
        } else if (TextUtils.isEmpty(binding.mobileET.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), getString(R.string.mobile_no_required));
        } else if (TextUtils.isEmpty(binding.emailEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), getString(R.string.email_required));
        } else if (TextUtils.isEmpty(city_id)) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "City required");
        } else if (TextUtils.isEmpty(binding.bussnessNameEt.getText().toString())) {
            retryAlertDialog(getString(R.string.app_name), "Required Business Name");
            count--;
        } else if (TextUtils.isEmpty(binding.pincodeEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), getString(R.string.pincode_required));
        }
        else if (TextUtils.isEmpty(binding.bussnessNameEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "Full name required" + "required");
        }
        else if (TextUtils.isEmpty(binding.addressEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), getString(R.string.address_required));
        }else if (TextUtils.isEmpty(binding.pickupAddressEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "please select pick address");
        }
//        else if (seller_uri==null) {
//            count--;
//            retryAlertDialog(getString(R.string.app_name), "Select Seller Image");
//        }
//        else if (profile_uri==null) {
//            count--;
//            retryAlertDialog(getString(R.string.app_name), "Select Logo Image");
//        }


        else if (selectedPinModelArrayList.size()==0){
            count--;
            retryAlertDialog(getString(R.string.app_name), "Please select delivery pin code");
        }else if (center==null){
            count--;
            retryAlertDialog(getString(R.string.app_name), "Please select pick address");
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();

            MultipartBody.Part profile_image = null, seller_image = null;

            if (profile_uri != null) {
                File file1 = new File(getPath(ProfileComplete.this, profile_uri));
                Log.d("logo_image: ", file1 + "");
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file1);
                profile_image = MultipartBody.Part.createFormData("profile_image", file1.getName(), requestBody);
            } else {
                //create a file to write bitmap data
//                String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

                try {

                    //Convert bitmap to byte array
                    binding.logoImage.buildDrawingCache();
                    Bitmap bitmap = binding.logoImage.getDrawingCache();
//                    Bitmap bitmap = ((BitmapDrawable)binding.logoImage.getDrawable()).getBitmap();

//                    Log.d("logo_image_Url", logo_image_Url);
//                    Bitmap bitmap = null;
//                            new ConvertUrlToBitmap().execute(logo_image_Url);


//                    try {
//                        URL url = new URL(logo_image_Url);
//                        bitmap= BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    } catch (IOException e) {
//                        System.out.println(e + "");
//                    }

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    Log.d("bit_image1", bitmapdata.toString());
                    //write the bytes in file
                    File file1 = new File(getFilesDir() + ".png");
                    file1.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file1);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file1);
                    profile_image = MultipartBody.Part.createFormData("profile_image", file1.getName(), requestBody);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            if (seller_uri != null) {
                File file2 = new File(getPath(ProfileComplete.this, seller_uri));
                Log.d("seller_image: ", file2 + "");
                RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
                seller_image = MultipartBody.Part.createFormData("seller_image", file2.getName(), requestBody2);
            } else {
                //create a file to write bitmap data

                try {
                    //Convert bitmap to byte array
                    binding.sellerImage.buildDrawingCache();
                    Bitmap bitmap = binding.sellerImage.getDrawingCache();
//                    Bitmap bitmap = ((BitmapDrawable)binding.sellerImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 80 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();

//                    Log.d("logo_image_Url", seller_image_Url.toString());
//                    Bitmap bitmap = getBitmapFromURL(seller_image_Url);
//                    try {
//                        URL url = new URL(seller_image_Url);
//                        bitmap= BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    } catch (IOException e) {
//                        System.out.println(e + "");
//                    }

                    Log.d("bit_image2", bitmap.toString());
                    //write the bytes in file
                    File file2 = new File(getFilesDir() + ".png");
                    file2.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file2);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
                    seller_image = MultipartBody.Part.createFormData("seller_image", file2.getName(), requestBody2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            StringBuffer deliveryPin = new StringBuffer();

            for (int i = 0; i < selectedPinModelArrayList.size(); i++) {
                if (i == 0) {
                    deliveryPin.append(selectedPinModelArrayList.get(i).getPincode());
                } else {
                    deliveryPin.append("," + selectedPinModelArrayList.get(i).getPincode());
                }
            }

            Log.d("deliveryPin", deliveryPin + "");
            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SharedPrefManager.getUserID(Constrants.UserId));
            RequestBody username = RequestBody.create(MediaType.parse("text/plain"), binding.fulNameEt.getText() + "");
            RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), binding.mobileET.getText() + "");
            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), binding.emailEt.getText() + "");
            RequestBody countryid = RequestBody.create(MediaType.parse("text/plain"), country_id);
            RequestBody stateid = RequestBody.create(MediaType.parse("text/plain"), state_id);
            RequestBody cityid = RequestBody.create(MediaType.parse("text/plain"), city_id);
            RequestBody pincode = RequestBody.create(MediaType.parse("text/plain"), binding.pincodeEt.getText() + "");
            RequestBody delivery_pincode = RequestBody.create(MediaType.parse("text/plain"), deliveryPin + "");
            RequestBody address_1 = RequestBody.create(MediaType.parse("text/plain"), binding.pickupAddressEt.getText() + "");
            RequestBody food_license_no = RequestBody.create(MediaType.parse("text/plain"), binding.foodLicenseNoEt.getText() + "");
            RequestBody business_reg_no = RequestBody.create(MediaType.parse("text/plain"), binding.businessRegNoEt.getText() + "");
            RequestBody business_name = RequestBody.create(MediaType.parse("text/plain"), binding.bussnessNameEt.getText() + "");
            RequestBody address_2 = RequestBody.create(MediaType.parse("text/plain"), binding.addressEt.getText() + "");
            RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), lati + "");
            RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), longi + "");

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<JsonObject> call = getResponse.ProfileComplete(
                    username,
                    mobile,
                    email,
                    countryid,
                    stateid,
                    cityid,
                    pincode,
                    delivery_pincode,
                    address_1,
                    food_license_no,
                    business_reg_no,
                    business_name,
                    address_2,
                    user_id,
                    latitude,
                    longitude,
                    profile_image,
                    seller_image
            );
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject obj = new JSONObject(response.body().toString());
                            Log.d("profile_step_res", obj.toString());
                            progressDialog.dismiss();
                            if (obj.optInt("status_code") == 1) {
                                Toast.makeText(ProfileComplete.this, obj.optString("message"), Toast.LENGTH_LONG).show();
                                binding.toolbar.activityTitle.setText(getResources().getString(R.string.kcy_com));
                                binding.profileCompletLl.setVisibility(View.GONE);
                                binding.kycLl.setVisibility(View.VISIBLE);
                                binding.sellerArgmentLL.setVisibility(View.GONE);
                                binding.nextBTN.setVisibility(View.VISIBLE);
                                binding.backBTN.setVisibility(View.VISIBLE);
                                binding.nextBTN.setText(getResources().getString(R.string.next));
                                binding.nextBTN.setBackground(getResources().getDrawable(R.drawable.grey_rounded_border));
                            } else {
                                Toast.makeText(ProfileComplete.this, obj.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            JSONObject responseJ = new JSONObject(response.errorBody().string());
                            System.out.println("error response " + responseJ.toString());
                            progressDialog.dismiss();

                        } catch (Exception e) {

                        }

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(ProfileComplete.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("fail response.." + t.toString());
//                        dismissProgressBar();
                }
            });
        }
    }

    public void profileStep2() {

        if (TextUtils.isEmpty(binding.accountNoEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "Account number is required");
        } else if (TextUtils.isEmpty(binding.bankNameEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "Bank name is required");
        } else if (TextUtils.isEmpty(binding.ifscEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "IFSC code is required");
        } else if (TextUtils.isEmpty(binding.actHolderNameEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "Account holder name is required");
        } else if (TextUtils.isEmpty(binding.panNoEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "Pan no. is required");
        } else if (TextUtils.isEmpty(binding.gstNoEt.getText())) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "GST no. is required");
        } else if (cheque_uri == null) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "Select Cancel cheque Image");
        } else if (pan_uri == null) {
            count--;
            retryAlertDialog(getString(R.string.app_name), "Select Pan Copy");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();

            MultipartBody.Part cancel_cheque_image, pan_image;

            File file1;
            if (cheque_uri != null) {
                file1 = new File(getPath(ProfileComplete.this, cheque_uri));

            } else {
                //create a file to write bitmap data
//                file1 = new File(this.getCacheDir(), "image");
                file1 = new File(getFilesDir() + ".png");
                try {
                    file1.createNewFile();
                    //Convert bitmap to byte array
                    binding.cancelChequeImage.buildDrawingCache();
                    Bitmap bitmap = binding.cancelChequeImage.getDrawingCache();
//                    Bitmap bitmap = ((BitmapDrawable)binding.cancelChequeImage.getDrawable()).getBitmap();

                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    Log.d("bit_image", bitmapdata.toString());
//write the bytes in file
                    FileOutputStream fos = new FileOutputStream(file1);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file1);
            cancel_cheque_image = MultipartBody.Part.createFormData("cancel_cheque", file1.getName(), requestBody);


            File file2;
            if (pan_uri != null) {
                file2 = new File(getPath(ProfileComplete.this, pan_uri));

            } else {

                //create a file to write bitmap data
//                file2 = new File(this.getCacheDir(), "image");
                file2 = new File(getFilesDir() + ".png");
                try {
                    file2.createNewFile();
                    binding.uploadPanCompyImage.buildDrawingCache();
                    Bitmap bitmap = binding.uploadPanCompyImage.getDrawingCache();
//                    Bitmap bitmap = ((BitmapDrawable)binding.uploadPanCompyImage.getDrawable()).getBitmap();

                    //Convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    Log.d("bit_image", bitmapdata.toString());
                    //write the bytes in file

                    FileOutputStream fos = new FileOutputStream(file2);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            RequestBody requestBody2 = RequestBody.create(MediaType.parse("image/*"), file2);
            pan_image = MultipartBody.Part.createFormData("pan_image", file2.getName(), requestBody2);

            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SharedPrefManager.getUserID(Constrants.UserId));
            RequestBody account_number = RequestBody.create(MediaType.parse("text/plain"), binding.accountNoEt.getText() + "");
            RequestBody bank_name = RequestBody.create(MediaType.parse("text/plain"), binding.bankNameEt.getText() + "");
            RequestBody ifsc_code = RequestBody.create(MediaType.parse("text/plain"), binding.ifscEt.getText() + "");
            RequestBody account_holder_name = RequestBody.create(MediaType.parse("text/plain"), binding.actHolderNameEt.getText() + "");
            RequestBody pan_number = RequestBody.create(MediaType.parse("text/plain"), binding.panNoEt.getText() + "");
            RequestBody gst_number = RequestBody.create(MediaType.parse("text/plain"), binding.gstNoEt.getText() + "");

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<JsonObject> call = getResponse.ProfileComplete2(
                    account_number,
                    bank_name,
                    ifsc_code,
                    account_holder_name,
                    pan_number,
                    gst_number,
                    user_id,
                    cancel_cheque_image,
                    pan_image
            );
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject obj = new JSONObject(response.body().toString());
                            Log.d("profile_step2_res", obj.toString());
                            progressDialog.dismiss();
                            if (obj.optInt("status_code") == 1) {
                                binding.desAgmntTv.setText(Html.fromHtml(obj.optJSONObject("seller_agreement").optString("description")));
                                Toast.makeText(ProfileComplete.this, obj.optString("message"), Toast.LENGTH_LONG).show();
                                binding.toolbar.activityTitle.setText(getResources().getString(R.string.seller_agr));
                                binding.profileCompletLl.setVisibility(View.GONE);
                                binding.kycLl.setVisibility(View.GONE);
                                binding.sellerArgmentLL.setVisibility(View.VISIBLE);
                                binding.nextBTN.setVisibility(View.VISIBLE);
                                binding.backBTN.setVisibility(View.VISIBLE);
                                binding.nextBTN.setText(getResources().getString(R.string.submit));
                                binding.nextBTN.setBackground(getResources().getDrawable(R.drawable.color_primary_border));
                            } else {
                                Toast.makeText(ProfileComplete.this, obj.optString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {

                        try {
                            JSONObject responseJ = new JSONObject(response.errorBody().string());
                            System.out.println("error response " + responseJ.toString());
                            progressDialog.dismiss();

                        } catch (Exception e) {

                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Toast.makeText(ProfileComplete.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("fail response.." + t.toString());
//                        dismissProgressBar();
                }
            });
        }
    }

    public void profileStep3() {
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        new WebTask(ProfileComplete.this, WebUrls.BASE_URL + WebUrls.ProfileUpdateStep3,
                objectNew, ProfileComplete.this, RequestCode.CODE_ProfileUpdateStep3, 1);
    }


    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
//            image_url = getPath(ProductUpload.this, data.getData());
            filename = getPath(ProfileComplete.this, data.getData());
            try {
                //getting bitmap object from uri
                if (upLogo) {
                    profile_uri = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profile_uri);
                    binding.logoImage.setImageBitmap(bitmap);
                }
                if (seller) {
                    seller_uri = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), seller_uri);
                    binding.sellerImage.setImageBitmap(bitmap);
                }
                if (canceCheu) {
                    cheque_uri = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cheque_uri);
                    binding.cancelChequeImage.setImageBitmap(bitmap);
                }
                if (pan) {
                    pan_uri = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pan_uri);
                    binding.uploadPanCompyImage.setImageBitmap(bitmap);
                }

//                RoundedBitmapDrawable img = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
//                img.setCornerRadius(5.f);
//                img.setAlpha(5);

            } catch (IOException e) {
                e.printStackTrace();
            }
//            validateViewsImage();
        } else if (requestCode == 300) {
            filename = getPath(ProfileComplete.this, imageUri);
            try {

                //getting bitmap object from uri

                //displaying selected image to imageview
                if (upLogo) {
                    profile_uri = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), profile_uri);
                    binding.logoImage.setImageBitmap(bitmap);
                }
                if (seller) {
                    seller_uri = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), seller_uri);
                    binding.sellerImage.setImageBitmap(bitmap);
                }
                if (canceCheu) {
                    cheque_uri = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), cheque_uri);
                    binding.cancelChequeImage.setImageBitmap(bitmap);
                }
                if (pan) {
                    pan_uri = imageUri;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pan_uri);
                    binding.uploadPanCompyImage.setImageBitmap(bitmap);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 51) {
            if (resultCode == RESULT_OK) {
//                getDeviceLocation();
            }
        }
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            String address = place.getName() + ", " + place.getAddress();

//            SharedPrefManager.setAddress(Constrants.DeliveryAddress,address);
            binding.pickupAddressEt.setText(address);
            center = place.getLatLng();
            if (center!=null) {
                lati = center.latitude;
                longi = center.longitude;
                Log.i("", "Place_search: " +address+"\n"+center.latitude+"  ,  " +center.longitude);
            }
//            mFusedLocationProviderClient.removeLocationUpdates(locationCallback);

        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            // TODO: Handle the error.
            Status status = Autocomplete.getStatusFromIntent(data);
            Log.i("", status.getStatusMessage());
        } else if (resultCode == RESULT_CANCELED) {
            // The user canceled the operation.

        }
    }

    public static String getPath(Context context, Uri uri) {
        String[] data = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void StateApi() {
        HashMap objectNew = new HashMap();
        objectNew.put("country_id", country_id);
        new WebTask(ProfileComplete.this, WebUrls.BASE_URL + WebUrls.StateApi, objectNew, ProfileComplete.this, RequestCode.CODE_State, 1);
    }

    public void CityApi() {
        HashMap objectNew = new HashMap();
        objectNew.put("state_id", state_id);
        new WebTask(ProfileComplete.this, WebUrls.BASE_URL + WebUrls.CityApi, objectNew, ProfileComplete.this, RequestCode.CODE_City, 1);
    }

    public void DeliveryPincode() {
        HashMap objectNew = new HashMap();
        objectNew.put("city_id", city_id);
        new WebTask(ProfileComplete.this, WebUrls.BASE_URL + WebUrls.Delovery_Pincode, objectNew, ProfileComplete.this, RequestCode.CODE_Delovery_Pincode, 1);
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_ProfileUpdateStep3 == taskcode) {
            System.out.println("ProfileUpdateStep3_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    Utils.Tosat(ProfileComplete.this, jsonObject.optString("message"));
                    finish();
                } else {
                    Utils.Tosat(ProfileComplete.this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_State == taskcode) {
            System.out.println("State_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    stateName.clear();
                    stateId.clear();
                    if (dataArray.length() > 0) {

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.optJSONObject(i);
                            StateModel stateModel = new StateModel();
                            stateModel.setId(dataObj.optString("id"));
                            stateModel.setName(dataObj.optString("name"));
                            stateModel.setCountry_id(dataObj.optString("country_id"));

                            stateName.add(dataObj.optString("name"));
                            stateId.add(dataObj.optString("id"));
                            arrayList.add(stateModel);
                        }

                        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, stateName);
                        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.stateSpinner.setAdapter(state_adapter);
                    }
                    setData();

                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_City == taskcode) {
            System.out.println("City_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    cityID.clear();
                    cityName.clear();

                    ArrayAdapter<String> city_adapter;
                    if (dataArray.length() > 0) {

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.optJSONObject(i);
                            cityName.add(dataObj.optString("name"));
                            cityID.add(dataObj.optString("id"));
                        }
                        city_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, cityName);

                    } else {
                        city_id = "";
                        city_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cityArray));
                        pin_id = "";
                        ArrayAdapter<String> pin_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.pinArray));
                        pin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.deliveryPinSpinner.setAdapter(pin_adapter);
                    }
                    city_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.citySpinner.setAdapter(city_adapter);

                    setCityData();


                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Delovery_Pincode == taskcode) {
            System.out.println("Delovery_Pincode_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    pincodeID.clear();
                    pincodeName.clear();
                    pincodeName.add("Select Pincode");
                    pincodeID.add("0");
                    ArrayAdapter<String> pin_adapter;
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.optJSONObject(i);
                            pincodeName.add(dataObj.optString("pincode"));
                            pincodeID.add(dataObj.optString("id"));
                        }
                        pin_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, pincodeName);


                    } else {
                        pin_id = "";
                        pin_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.pinArray));
                    }
                    pin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.deliveryPinSpinner.setAdapter(pin_adapter);

                    selectedPinModelArrayList.clear();
                    deliveryPincodeAdapter.notifyDataSetChanged();


                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //-----------------------------Pick Address-----------------------
    @Override
    protected void onRestart() {
        super.onRestart();
        LocationPermission();
//        startLocationUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 2;
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        } else {
//            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            Task<Location> location = fusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                lati = currentLocation.getLatitude();
                                longi = currentLocation.getLongitude();
                                center = new LatLng(lati,longi);

                                Geocoder geocoder = new Geocoder(ProfileComplete.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(lati, longi, 1);
                                    Address addressobj = addresses.get(0);
                                    if (addressobj!=null)
                                        binding.pickupAddressEt.setText(addressobj.getAddressLine(0));
//                                                               search_tv.setText(addressobj.getAddressLine(0));
//                                                               All_Book_List_Method();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                             Utils.Tosat(ProfileComplete.this, "Unable to find current location . Try again later");
                        }
                    }
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                getLocation();
                Log.i("onRequestPermissions", "onRequestPermissionsResult");
                break;
        }
    }

    public void LocationPermission() {
        //        LocationServiceOn_Off();
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager == null) {
            showSettingsAlert();
        } else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                Log.i("About GPS", "GPS is Enabled in your device");
//                getLocation();
                // Toast.makeText(ctx,"enable",Toast.LENGTH_SHORT).show();
            } else {
                //showAlert
                showSettingsAlert();
//            context.startActivity(new Intent(context, DialogActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }
    }

    public void showSettingsAlert() {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle(R.string.gps_setting);
        // Setting Dialog Message
        alertDialog.setMessage(R.string.gps_setting_menu);

        // On pressing Settings button
        alertDialog.setPositiveButton(R.string.settings,
                (dialog, which) -> {
                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                });

        // on pressing cancel button
        alertDialog.setNegativeButton(getString(R.string.cancel),
                (dialog, which) -> dialog.cancel());

        // Showing Alert Message
        alertDialog.show();
    }
}
