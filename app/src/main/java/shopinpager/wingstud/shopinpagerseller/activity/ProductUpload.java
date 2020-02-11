package shopinpager.wingstud.shopinpagerseller.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import shopinpager.wingstud.shopinpagerseller.Api.ApiConfig;
import shopinpager.wingstud.shopinpagerseller.Api.AppConfig;
import shopinpager.wingstud.shopinpagerseller.Api.JsonDeserializer;
import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.adapter.ColorListAdapter;
import shopinpager.wingstud.shopinpagerseller.adapter.PriceUnitAdapter;
import shopinpager.wingstud.shopinpagerseller.adapter.SearchItemAdapter;
import shopinpager.wingstud.shopinpagerseller.adapter.SelectImageAdapter;
import shopinpager.wingstud.shopinpagerseller.databinding.AddBrandPopupBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.ProductUploadBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.SearchPopupBinding;
import shopinpager.wingstud.shopinpagerseller.model.ColorModel;
import shopinpager.wingstud.shopinpagerseller.model.ImageModel;
import shopinpager.wingstud.shopinpagerseller.model.PriceUnitModel;
import shopinpager.wingstud.shopinpagerseller.model.SearchModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static shopinpager.wingstud.shopinpagerseller.util.Utils.Tosat;
import static shopinpager.wingstud.shopinpagerseller.util.Utils.checkEmptyNull;

public class ProductUpload extends AppCompatActivity implements WebCompleteTask {

    private Toolbar toolbar;
    private ProductUploadBinding binding;

    ArrayList<String> BrandName = new ArrayList<>();
    ArrayList<String> BrandId = new ArrayList<>();
    ArrayList<String> CatName = new ArrayList<>();
    ArrayList<String> CatId = new ArrayList<>();
    ArrayList<String> SubCatName = new ArrayList<>();
    ArrayList<String> SubCatId = new ArrayList<>();

    ArrayList<PriceUnitModel> price = new ArrayList<>();
    ArrayList<ColorModel> colorAry = new ArrayList<>();
    ColorListAdapter colorListAdapter;
    PriceUnitAdapter priceUnitAdapter;

    private int REQUEST_CAMERA_PERMISSION = 1;
    String brand_id = "", cat_id = "", sub_cat_id = "", product_id = "";
    public static ProductUpload mInstance;
    Uri imageUri;
    ArrayList<Uri> upload_image_array = new ArrayList<>();
    SelectImageAdapter selectImageAdapter;

    ArrayList<ImageModel> ImageArray = new ArrayList<>();
    ArrayList<String> colorArray = new ArrayList<>();

    //--------------Search item
    List<SearchModel.Datum> searchModelArrayList = new ArrayList<>();
    SearchItemAdapter searchItemAdapter;
    SearchPopupBinding searchPopupBinding;
    PopupWindow dialog;

    public static boolean isClickable = false;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.product_upload);

        mInstance = this;
        binding.toolbar.activityTitle.setText(getResources().getString(R.string.product_upload));
        initialize();
        ColorListAdapter.colorCodeArray.clear();

        binding.selectImageRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        selectImageAdapter = new SelectImageAdapter(this, upload_image_array, ImageArray);
        binding.selectImageRV.setAdapter(selectImageAdapter);
        selectImageAdapter.notifyDataSetChanged();

        binding.selectImageLL.setOnClickListener(v -> ImagePopup());
        binding.submitProductBtn.setOnClickListener(v -> {
            if (checkEmptyNull(product_id)) {
                productUpDateMethod();
            } else {
                productUploadMethod();
            }
        });
        binding.addIg.setOnClickListener(v -> addItem(price.size()));

        binding.brandNameTv.setOnClickListener(v -> addBrand());

    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //--------------------Color Adapter---------------
        binding.colorRv.setLayoutManager(new LinearLayoutManager(this));
        binding.colorRv.setNestedScrollingEnabled(false);
        Utils.recyclerView(binding.colorRv);
        colorListAdapter = new ColorListAdapter(this, colorAry, colorArray);
        binding.colorRv.setAdapter(colorListAdapter);
        colorListAdapter.notifyDataSetChanged();
        ColorMethod();
        //--------------------Color Adapter---------------


        //--------------------Price Adapter---------------
        binding.priceUnitRv.setLayoutManager(new LinearLayoutManager(this));
        binding.priceUnitRv.setNestedScrollingEnabled(false);
        Utils.recyclerView(binding.priceUnitRv);


        PriceUnitModel priceMode = new PriceUnitModel();
        if (getIntent().getExtras() != null) {
            product_id = getIntent().getExtras().getString("id", "");
            if (checkEmptyNull(product_id)) {

            }
        } else {
            price.add(0, priceMode);
        }
        priceUnitAdapter = new PriceUnitAdapter(this, price);
        binding.priceUnitRv.setAdapter(priceUnitAdapter);
        priceUnitAdapter.notifyDataSetChanged();

        //--------------------Price Adapter---------------

        binding.toolbar.bellRL.setVisibility(View.GONE);
        brandMethod();

        binding.brandSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (BrandName.size() > 1 && position > 0) {
                    brand_id = BrandId.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.mainCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (CatName.size() > 1 && position > 0) {
                    cat_id = CatId.get(position);
                    SubCatMethod();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.subCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (SubCatName.size() > 1 && position > 0) {
                    sub_cat_id = SubCatId.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        binding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(ProductUpload.this));
        Utils.recyclerView(binding.searchRecyclerView);

        binding.productNameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                if (s.length() >= 2) {
//                    searchPopup(s.toString());
                searchMethod();
//                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

//                }
            }
        });

    }

    public void addBrandMethod(String search_string) {
        HashMap objectNew = new HashMap();
//        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("name", search_string);
        new WebTask(this, WebUrls.BASE_URL + WebUrls.SellerAddBrand, objectNew, ProductUpload.this, RequestCode.CODE_SellerAddBrand, 1);
    }

//    public void searchPopup(String string) {
////        if (dialog != null && dialog.isShowing()) {
////            dialog.dismiss();
////        }
//        new Handler().postDelayed(() -> {
////            if (dialog != null && dialog.isShowing()) {
////                dialog.dismiss();
////            }
//            searchModelArrayList.clear();
//            searchPopupBinding = DataBindingUtil.inflate(getLayoutInflater(),
//                    R.layout.search_popup,
//                    null,
//                    false);
//
//            searchPopupBinding.progressBar.setVisibility(View.GONE);
//            searchPopupBinding.searchRecyclerView.setLayoutManager(new LinearLayoutManager(ProductUpload.this));
//            Utils.recyclerView(searchPopupBinding.searchRecyclerView);
//            searchItemAdapter = new SearchItemAdapter(ProductUpload.this, searchModelArrayList);
//            searchPopupBinding.searchRecyclerView.setAdapter(searchItemAdapter);
//            searchItemAdapter.notifyDataSetChanged();
//            searchMethod(string);
//
//            dialog = new PopupWindow(searchPopupBinding.getRoot(),
//                    RecyclerView.LayoutParams.MATCH_PARENT,
//                    RecyclerView.LayoutParams.WRAP_CONTENT, true);
//            dialog.setFocusable(true);
////        dialog.setBackgroundDrawable(getResources().getDrawable(R.drawable.gray_rounded_bg));
//            dialog.showAsDropDown(binding.productNameEt, 0, 0);
//        }, 500);
//    }

    //    public void searchMethod(String search_string) {
//        HashMap objectNew = new HashMap();
//        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
//        objectNew.put("search_key", search_string);
//        new WebTask(this, WebUrls.BASE_URL + WebUrls.GetSearchItem, objectNew, ProductUpload.this, RequestCode.CODE_GetSearchItem, 5);
//    }
    public void searchMethod() {
        String UserId = SharedPrefManager.getUserID(Constrants.UserId);
        //----------------------------------Using Retrofit------------
//        final String[] str_response = new String[0];
        Log.i("User_id_d",UserId);
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<JsonObject> call = getResponse.searchProduct(UserId, binding.productNameEt.getText().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.i("search_res", response.body() + "");
//                try {
//                    JSONObject jsonObject = new JSONObject(response.toString());
//                    if (jsonObject.getInt("status_code") == 1) {
//                        JSONArray dataArray = jsonObject.optJSONArray("data");
//                        searchModelArrayList.clear();
//                        if (dataArray.length() > 0) {
//                            for (int i = 0; i < dataArray.length(); i++) {
//                                JSONObject dataObj = dataArray.getJSONObject(i);
//                                SearchModel model = new SearchModel();
//                                model.setJsonObject(dataObj);
//                                model.setId(dataObj.optString("id"));
//                                model.setName(dataObj.optString("name"));
//                                searchModelArrayList.add(model);
//                            }
//                            searchItemAdapter = new SearchItemAdapter(ProductUpload.this, searchModelArrayList);
//                            binding.searchRecyclerView.setAdapter(searchItemAdapter);
//                            searchItemAdapter.notifyDataSetChanged();
//                        }
//                    } else {
//                        Tosat(ProductUpload.this, jsonObject.optString("error_message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                str_response[0] = response.body() + "";

                SearchModel searchModel = JsonDeserializer.deserializeJson(response.body().toString(), SearchModel.class);
                if (searchModel.statusCode == 1) {
                    binding.searchRecyclerView.setVisibility(View.VISIBLE);
//                    binding.emptyListLL.setVisibility(View.GONE);
                    searchModelArrayList.clear();
                    searchModelArrayList.addAll(searchModel.data);
                    searchItemAdapter = new SearchItemAdapter(ProductUpload.this, searchModelArrayList);
                    binding.searchRecyclerView.setAdapter(searchItemAdapter);
                    searchItemAdapter.notifyDataSetChanged();
                } else {
//                    binding.emptyList.titleEmpty.setText("Search item not Found");
//                    binding.emptyList.desEmpty.setText("Search item not found,we will let you know as soon as available");
//                    binding.emptyList.imageEmpty.setImageDrawable(getDrawable(R.drawable.product_not_found));
                    binding.searchRecyclerView.setVisibility(View.GONE);
//                    binding.emptyListLL.setVisibility(View.VISIBLE);
                }
//                binding.progress.setVisibility(View.GONE);
//                binding.cancelBt.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("search_error", t.getMessage());
            }
        });
    }

    public void setFieldData(JSONObject dataObject) {
        binding.productNameEt.setEnabled(false);
        binding.brandSpinner.setEnabled(false);
        binding.productDesEt.setEnabled(false);
        binding.mainCatSpinner.setEnabled(false);
        binding.subCatSpinner.setEnabled(false);
        binding.gstEt.setEnabled(false);
        binding.selectImageLL.setEnabled(false);
        binding.colorRv.setEnabled(false);
        binding.colorRv.setClickable(false);

        isClickable = true;

        Log.i("sdfasdfsdf", dataObject.optString("name"));
        if (checkEmptyNull(dataObject.optString("name")))
            binding.productNameEt.setText(dataObject.optString("name"));

        if (checkEmptyNull(dataObject.optString("description")))
            binding.productDesEt.setText(dataObject.optString("description"));

        if (checkEmptyNull(dataObject.optString("p_gst")))
            binding.gstEt.setText(dataObject.optString("p_gst"));

        if (checkEmptyNull(dataObject.optString("category_id"))) {
            binding.mainCatSpinner.setSelection(
                    getIndex(binding.mainCatSpinner,
                            searchITem(CatId, CatName, dataObject.optString("category_id"))));

        }
        if (checkEmptyNull(dataObject.optString("brand_id"))) {
            binding.brandSpinner.setSelection(
                    getIndex(binding.brandSpinner,
                            searchITem(BrandId, BrandName, dataObject.optString("brand_id"))));

        }

//        if (checkEmptyNull(dataObject.optString("sub_category_id")) && SubCatId.size() > 0) {
//            binding.subCatSpinner.setSelection(
//                    getIndex(binding.subCatSpinner,
//                            searchITem(SubCatId, SubCatName, dataObject.optString("sub_category_id"))));
//        }

        try {
//            if (!dataObject.optString("color").equals("null") && !TextUtils.isEmpty(dataObject.optString("color"))) {
//                String[] clrAry = dataObject.optString("color").split(",");
//                Collections.addAll(colorArray, clrAry);
//
//                colorListAdapter = new ColorListAdapter(this, colorAry, colorArray);
//                binding.colorRv.setAdapter(colorListAdapter);
//                colorListAdapter.notifyDataSetChanged();
//            }
            if (dataObject.optJSONArray("product_image").length() > 0) {

                JSONArray productImage = dataObject.optJSONArray("product_image");
                ImageArray.clear();
                for (int j = 0; j < productImage.length(); j++) {
                    ImageModel imageModel = new ImageModel();
                    imageModel.setId(productImage.getJSONObject(j).optString("id"));
                    imageModel.setImage(SharedPrefManager.getImagePath(Constrants.ImagePath) + "/" + productImage.getJSONObject(j).optString("image"));
                    ImageArray.add(imageModel);
                }
                selectImageAdapter = new SelectImageAdapter(this, upload_image_array, ImageArray);
                binding.selectImageRV.setAdapter(selectImageAdapter);
                selectImageAdapter.notifyDataSetChanged();
            }
            if (dataObject.optJSONArray("product_item") != null && dataObject.optJSONArray("product_item").length() > 0) {
                JSONArray productAry = dataObject.optJSONArray("product_item");
                if (productAry.length() > 0) {
                    for (int j = 0; j < productAry.length(); j++) {
                        PriceUnitModel productItemModel = new PriceUnitModel();
                        if (productAry.getJSONObject(j).optString("seller_id").equals(SharedPrefManager.getUserID(Constrants.UserId))) {
                            productItemModel.setWeight(productAry.getJSONObject(j).optString("weight"));
                            productItemModel.setPrice(String.format("%.2f", productAry.getJSONObject(j).optDouble("price")));
                            productItemModel.setOffer(productAry.getJSONObject(j).optString("offer"));
                            productItemModel.setQty(productAry.getJSONObject(j).optString("qty"));
                            productItemModel.setId(productAry.getJSONObject(j).optString("id"));
                            price.add(productItemModel);
                        }
                    }
                }
                binding.priceUnitRv.setVisibility(View.VISIBLE);
                binding.priceUnitRv.setLayoutManager(new LinearLayoutManager(ProductUpload.this, LinearLayoutManager.VERTICAL, false));
                priceUnitAdapter = new PriceUnitAdapter(this, price);
                binding.priceUnitRv.setAdapter(priceUnitAdapter);
                priceUnitAdapter.notifyDataSetChanged();

            } else {
                binding.priceUnitRv.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBrand() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductUpload.this);
        AddBrandPopupBinding binding = DataBindingUtil.inflate(LayoutInflater.from(ProductUpload.this)
                , R.layout.add_brand_popup, null, false);
        builder.setView(binding.getRoot());
        alertDialog = builder.create();
        binding.btnCancel.setOnClickListener(v -> alertDialog.dismiss());
        binding.btnSubmit.setOnClickListener(v -> {
            if (!binding.brandNameTv.getText().toString().isEmpty())
                addBrandMethod(binding.brandNameTv.getText().toString());
            else {
                binding.brandNameTv.setError("Please enter brand name");
                binding.brandNameTv.requestFocus();
            }
        });
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    public void setData(JSONObject jsonObject) {
        Log.i("fdsggdfgdsfgf", jsonObject.optString("sub_category_id"));
        if (checkEmptyNull(jsonObject.optString("sub_category_id")) && SubCatId.size() > 0) {
            binding.subCatSpinner.setSelection(
                    getIndex(binding.subCatSpinner,
                            searchITem(SubCatId, SubCatName, jsonObject.optString("sub_category_id"))));
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

    private void ImagePopup() {
        try {
            final Dialog dialog = new Dialog(ProductUpload.this);
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
                        if (ActivityCompat.checkSelfPermission(ProductUpload.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProductUpload.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                        if (ActivityCompat.checkSelfPermission(ProductUpload.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ProductUpload.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                        } else {
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_PICK);
                            startActivityForResult(Intent.createChooser(intent, "Select Image"), 100);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
//            image_url = getPath(ProductUpload.this, data.getData());

            ImageModel imageModel = new ImageModel();
            imageModel.setImage(imageUri.toString());
            upload_image_array.add(imageUri);
            ImageArray.add(imageModel);
            selectImageAdapter = new SelectImageAdapter(this, upload_image_array, ImageArray);
            binding.selectImageRV.setAdapter(selectImageAdapter);
            selectImageAdapter.notifyDataSetChanged();

        } else if (requestCode == 300) {
            Uri selectedImageUri = null;
            selectedImageUri = imageUri;
            ImageModel imageModel = new ImageModel();
            imageModel.setImage(selectedImageUri.toString());
            upload_image_array.add(imageUri);
            ImageArray.add(imageModel);
            selectImageAdapter = new SelectImageAdapter(this, upload_image_array, ImageArray);
            binding.selectImageRV.setAdapter(selectImageAdapter);
            selectImageAdapter.notifyDataSetChanged();
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

    public void productUploadMethod() {
        if (TextUtils.isEmpty(binding.productNameEt.getText())) {
            retryAlertDialog(getString(R.string.app_name), getString(R.string.name_required));
        } else if (TextUtils.isEmpty(binding.productDesEt.getText())) {
            retryAlertDialog(getString(R.string.app_name), getString(R.string.description) + "required");
        } else if (TextUtils.isEmpty(binding.gstEt.getText())) {
            retryAlertDialog(getString(R.string.app_name), getString(R.string.gst) + "required");
        } else if (!checkEmptyNull(brand_id)) {
            retryAlertDialog(getString(R.string.app_name), "Please Select Brand");
        } else if (!checkEmptyNull(cat_id)) {
            retryAlertDialog(getString(R.string.app_name), "Please Select Category");
        } else if (!checkEmptyNull(sub_cat_id)) {
            retryAlertDialog(getString(R.string.app_name), "Please Select Sub Category");
        } else if (upload_image_array.size() <= 0) {
            retryAlertDialog(getString(R.string.app_name), "Please Select At-least one Image");
        } else {
            try {
                String[] stockArr = new String[ColorListAdapter.colorCodeArray.size()];
                stockArr = ColorListAdapter.colorCodeArray.toArray(stockArr);
                StringBuffer sas = new StringBuffer();
                for (int i = 0; i < ColorListAdapter.colorCodeArray.size(); i++) {
                    if (i == ColorListAdapter.colorCodeArray.size() - 1) {
                        sas.append(stockArr[i]);
                    } else {
                        sas.append(stockArr[i] + ",");
                    }
                }

                JSONArray priceUnitArray = new JSONArray();
                for (int j = 0; j < price.size(); j++) {
                    JSONObject price_unit = new JSONObject();
                    if (checkEmptyNull(price.get(j).getPrice()))
                        price_unit.put("price", price.get(j).getPrice());

                    if (checkEmptyNull(price.get(j).getWeight()))
                        price_unit.put("weight", price.get(j).getWeight());

                    if (checkEmptyNull(price.get(j).getOffer()))
                        price_unit.put("offer", price.get(j).getOffer());

                    if (checkEmptyNull(price.get(j).getQty()))
                        price_unit.put("qty", price.get(j).getQty());

                    priceUnitArray.put(price_unit);
                }


                Log.d("api_addbook_data", priceUnitArray.toString());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                final ArrayList<MultipartBody.Part> photos = new ArrayList<>();
                MultipartBody.Part filePartmultipleImages;
                Log.d("upload_image_array", upload_image_array.toString());
                for (int i = 0; i < upload_image_array.size(); i++) {
                    File file = new File(getPath(ProductUpload.this, upload_image_array.get(i)));
                    Log.d("file_path_image", file + "");
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    filePartmultipleImages = MultipartBody.Part.createFormData("product_image[]", file.getName(), requestBody);
                    photos.add(filePartmultipleImages);
                }

                RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SharedPrefManager.getUserID(Constrants.UserId));
                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), binding.productDesEt.getText() + "");
                RequestBody p_gst = RequestBody.create(MediaType.parse("text/plain"), binding.gstEt.getText() + "");
                RequestBody brandid = RequestBody.create(MediaType.parse("text/plain"), brand_id);
                RequestBody category_id = RequestBody.create(MediaType.parse("text/plain"), cat_id);
                RequestBody sub_category_id = RequestBody.create(MediaType.parse("text/plain"), sub_cat_id);
                RequestBody name = RequestBody.create(MediaType.parse("text/plain"), binding.productNameEt.getText() + "");
                RequestBody color = RequestBody.create(MediaType.parse("text/plain"), sas + "");
                RequestBody price_unit = RequestBody.create(MediaType.parse("text/plain"), priceUnitArray + "");

                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                Call<JsonObject> call = getResponse.productUploadMethod(
                        user_id,
                        description,
                        p_gst,
                        brandid,
                        category_id,
                        sub_category_id,
                        name,
                        color,
                        price_unit,
                        photos
                );
                Log.d("productUploadMethod_OBj", call.toString());
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject obj = new JSONObject(response.body().toString());
                                progressDialog.dismiss();
                                if (obj.optInt("status_code") == 1) {
                                    Toast.makeText(ProductUpload.this, obj.optString("message"), Toast.LENGTH_LONG).show();
                                    finish();
                                } else {
                                    Toast.makeText(ProductUpload.this, obj.optString("message"), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(ProductUpload.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println("fail response.." + t.toString());
//                        dismissProgressBar();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("fail response.." + e.toString());
            }
        }

    }

    public void productUpDateMethod() {

        HashMap objectNew = new HashMap();
        objectNew.put("product_id", product_id);
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        JSONArray priceUnitArray = new JSONArray();
        try {
            for (int j = 0; j < price.size(); j++) {
                JSONObject price_unit = new JSONObject();
                if (checkEmptyNull(price.get(j).getPrice()))
                    price_unit.put("price", price.get(j).getPrice());

                if (checkEmptyNull(price.get(j).getWeight()))
                    price_unit.put("weight", price.get(j).getWeight());

                if (checkEmptyNull(price.get(j).getOffer()))
                    price_unit.put("offer", price.get(j).getOffer());

                if (checkEmptyNull(price.get(j).getQty()))
                    price_unit.put("qty", price.get(j).getQty());

                if (checkEmptyNull(price.get(j).getId()))
                    price_unit.put("item_id", price.get(j).getId());
                else if (checkEmptyNull(price.get(j).getId())) {
                    price_unit.put("item_id", "0");
                }

                priceUnitArray.put(price_unit);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        objectNew.put("price_unit", priceUnitArray.toString());

        Log.i("dup_item_update_obj", objectNew + "");
        new WebTask(this, WebUrls.BASE_URL + WebUrls.AddDuplicateProduct, objectNew, ProductUpload.this, RequestCode.CODE_AddDuplicateProduct, 1);


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

        AlertDialog dialog = dialogBuilder.create();
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

    public static ProductUpload getInstance() {
        return mInstance;
    }

    @SuppressLint("DefaultLocale")
    public void getsearchData(SearchModel.Datum dataObject) {

        binding.productNameEt.setEnabled(false);
        binding.brandSpinner.setEnabled(false);
        binding.productDesEt.setEnabled(false);
        binding.mainCatSpinner.setEnabled(false);
        binding.subCatSpinner.setEnabled(false);
        binding.gstEt.setEnabled(false);
        binding.selectImageLL.setEnabled(false);
        binding.colorRv.setEnabled(false);
        binding.colorRv.setClickable(false);
        isClickable = true;

        product_id = dataObject.id+"";

        Log.i("getsearchData", dataObject.toString());
        if (checkEmptyNull(dataObject.name))
            binding.productNameEt.setText(dataObject.name);

        if (checkEmptyNull(dataObject.description))
            binding.productDesEt.setText(dataObject.description);

        if (checkEmptyNull(dataObject.pGst))
            binding.gstEt.setText(dataObject.pGst);

        if (checkEmptyNull(dataObject.categoryId)) {
            binding.mainCatSpinner.setSelection(
                    getIndex(binding.mainCatSpinner,
                            searchITem(CatId, CatName, dataObject.categoryId)));

        }
        if (checkEmptyNull(dataObject.brandId)) {
            binding.brandSpinner.setSelection(
                    getIndex(binding.brandSpinner,
                            searchITem(BrandId, BrandName, dataObject.brandId)));

        }
        if (dataObject.mainCategory!=null) {
            List<String> list = new ArrayList<String>();
            list.add(dataObject.mainCategory.name);
            cat_id = dataObject.mainCategory.id;

            Log.i("main_category_df", dataObject.mainCategory.name);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.mainCatSpinner.setAdapter(adapter);
        }

        if (dataObject.subCategory!=null) {
            List<String> list = new ArrayList<String>();
            list.add(dataObject.subCategory.name);

            Log.i("sub_category_df", dataObject.subCategory.name);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.subCatSpinner.setAdapter(adapter);
        }

        //            if (!dataObject.color.equals("null") && !TextUtils.isEmpty(dataObject.optString("color"))) {
//                String[] clrAry = dataObject.optString("color").split(",");
//                Collections.addAll(colorArray, clrAry);
//
//                colorListAdapter = new ColorListAdapter(this, colorAry, colorArray);
//                binding.colorRv.setAdapter(colorListAdapter);
//                colorListAdapter.notifyDataSetChanged();
//            }
        if (dataObject.productImage.size() > 0) {

//                JSONArray productImage = dataObject.productImage;
            ImageArray.clear();
            for (int j = 0; j < dataObject.productImage.size(); j++) {
                ImageModel imageModel = new ImageModel();
                imageModel.setId(dataObject.productImage.get(j).id);
                imageModel.setImage(SharedPrefManager.getImagePath(Constrants.ImagePath) + "/" + dataObject.productImage.get(j).image);
                ImageArray.add(imageModel);
            }
            selectImageAdapter = new SelectImageAdapter(this, upload_image_array, ImageArray);
            binding.selectImageRV.setAdapter(selectImageAdapter);
            selectImageAdapter.notifyDataSetChanged();
        }
        if (dataObject.productItem != null && dataObject.productItem.size() > 0) {
//                JSONArray productAry = dataObject.optJSONArray("product_item");
//                if (dataObject.productItem.size()  > 0) {
            for (int j = 0; j < dataObject.productItem.size() ; j++) {
                PriceUnitModel productItemModel = new PriceUnitModel();
                if (dataObject.productItem.get(j).sellerId.equals(SharedPrefManager.getUserID(Constrants.UserId))) {
                    productItemModel.setWeight(dataObject.productItem.get(j).weight);
                    productItemModel.setPrice(String.format("%.0f", dataObject.productItem.get(j).price));
                    productItemModel.setOffer(dataObject.productItem.get(j).offer);
                    productItemModel.setQty(String.format("%.0f", dataObject.productItem.get(j).qty));
                    productItemModel.setId(dataObject.productItem.get(j).id);
                    price.add(productItemModel);
                }
//                    }
            }
            binding.priceUnitRv.setVisibility(View.VISIBLE);
            binding.priceUnitRv.setLayoutManager(new LinearLayoutManager(ProductUpload.this, LinearLayoutManager.VERTICAL, false));
            priceUnitAdapter = new PriceUnitAdapter(this, price);
            binding.priceUnitRv.setAdapter(priceUnitAdapter);
            priceUnitAdapter.notifyDataSetChanged();

        } else {
            binding.priceUnitRv.setVisibility(View.GONE);
        }
        binding.searchRecyclerView.setVisibility(View.GONE);
        searchModelArrayList.clear();

    }

    public void addItem(int position) {
        PriceUnitModel priceMode = new PriceUnitModel();
        priceMode.setPlus(false);
        priceMode.setMinus(true);
        price.add(price.size(), priceMode);
        priceUnitAdapter = new PriceUnitAdapter(this, price);
        binding.priceUnitRv.setAdapter(priceUnitAdapter);
    }

    public void brandMethod() {
        HashMap objectNew = new HashMap();
        new WebTask(this, WebUrls.BASE_URL + WebUrls.Brand_list, objectNew, ProductUpload.this, RequestCode.CODE_Brand_list, 5);
    }

    public void CatMethod() {
        HashMap objectNew = new HashMap();
        new WebTask(this, WebUrls.BASE_URL + WebUrls.Cat_list, objectNew, ProductUpload.this, RequestCode.CODE_Cat_list, 5);
    }

    public void SubCatMethod() {
        HashMap objectNew = new HashMap();
        objectNew.put("cat_id", cat_id);
        new WebTask(this, WebUrls.BASE_URL + WebUrls.SubCat_list, objectNew, ProductUpload.this, RequestCode.CODE_SubCat_list, 5);
    }

    public void ColorMethod() {
        HashMap objectNew = new HashMap();
        new WebTask(this, WebUrls.BASE_URL + WebUrls.Color_list, objectNew, ProductUpload.this, RequestCode.CODE_Color_list, 5);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Brand_list == taskcode) {
            System.out.println("Brand_list_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    BrandName.clear();
                    BrandId.clear();
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            BrandName.add(dataObj.optString("name"));
                            BrandId.add(dataObj.optString("id"));
                        }
                        BrandName.add(0, "Select Brand");
                        BrandId.add(0, "0");
                        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, BrandName);
                        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.brandSpinner.setAdapter(state_adapter);
                    }
                    CatMethod();
                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Cat_list == taskcode) {
            System.out.println("Cat_list_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    CatName.clear();
                    CatId.clear();
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            CatName.add(dataObj.optString("name"));
                            CatId.add(dataObj.optString("id"));
                        }
                        CatName.add(0, "Select Category");
                        CatId.add(0, "0");

                        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, CatName);
                        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.mainCatSpinner.setAdapter(state_adapter);

                        if (getIntent().getExtras() != null) {
                            product_id = getIntent().getExtras().getString("id", "");
                            if (checkEmptyNull(product_id)) {
                                try {
                                    jsonObject = new JSONObject(getIntent().getExtras().getString("data", ""));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.i("data_dafs", jsonObject + "");
                                setFieldData(jsonObject);
                            }
                        }
                    }
                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_SubCat_list == taskcode) {
            System.out.println("SubCat_list_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    SubCatName.clear();
                    SubCatId.clear();
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            SubCatName.add(dataObj.optString("name"));
                            SubCatId.add(dataObj.optString("id"));
                        }
                        SubCatName.add(0, "Select Sub Category");
                        SubCatId.add(0, "0");
                        ArrayAdapter<String> state_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, SubCatName);
                        state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        binding.subCatSpinner.setAdapter(state_adapter);
                        if (getIntent().getExtras() != null) {
                            product_id = getIntent().getExtras().getString("id", "");
                            if (checkEmptyNull(product_id)) {
                                try {
                                    jsonObject = new JSONObject(getIntent().getExtras().getString("data", ""));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.i("data_dafs", jsonObject + "");
                                setData(jsonObject);
                            }
                        }

                    }
                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Color_list == taskcode) {
            System.out.println("Color_list_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    colorAry.clear();
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            ColorModel model = new ColorModel();
                            model.setCode(dataObj.optString("code"));
                            model.setId(dataObj.optString("id"));
                            model.setColor_name(dataObj.optString("value"));

                            colorAry.add(model);
                        }
                        colorListAdapter.notifyDataSetChanged();
                    }
                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        if (RequestCode.CODE_GetSearchItem == taskcode) {
//            System.out.println("GetSearchItem_res: " + response);
//
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                if (jsonObject.getInt("status_code") == 1) {
//                    JSONArray dataArray = jsonObject.optJSONArray("data");
//                    searchModelArrayList.clear();
//                    if (dataArray.length() > 0) {
//                        for (int i = 0; i < dataArray.length(); i++) {
//                            JSONObject dataObj = dataArray.getJSONObject(i);
//                            SearchModel model = new SearchModel();
//                            model.setJsonObject(dataObj);
//                            model.setId(dataObj.optString("id"));
//                            model.setName(dataObj.optString("name"));
//                            searchModelArrayList.add(model);
//                        }
//                        searchItemAdapter.notifyDataSetChanged();
//                    } else {
//                        if (dialog != null) {
//                            dialog.dismiss();
//                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_HIDDEN);
//                        }
//                    }
//                } else {
//                    Tosat(this, jsonObject.optString("error_message"));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
        if (RequestCode.CODE_AddDuplicateProduct == taskcode) {
            System.out.println("AddDuplicateProduct_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    Toast.makeText(ProductUpload.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_SellerAddBrand == taskcode) {
            System.out.println("SellerAddBrand_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    Toast.makeText(ProductUpload.this, jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                    alertDialog.dismiss();
                    brandMethod();
                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    private class getStudentData extends AsyncTask<String,String,String>
//    {
////        public ProgressDialog pd;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
////            pd=new ProgressDialog(ProductUpload.this);
////            pd.setMessage("Loading...");
////            pd.setCancelable(false);
////            pd.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params)
//        {
//            String str_response= BackGroundApi.getResponseofGet(WebUrls.BASE_URL+WebUrls.GetSearchItem);
//            return str_response;
//        }
//
//        @Override
//        protected void onPostExecute(String response)
//        {
//            super.onPostExecute(response);
//            System.out.println("GetSearchItem_res: " + response);
//
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                if (jsonObject.getInt("status_code") == 1) {
//                    JSONArray dataArray = jsonObject.optJSONArray("data");
//                    searchModelArrayList.clear();
////                    if (dataArray.length() > 0) {
//                        for (int i = 0; i < dataArray.length(); i++) {
//                            JSONObject dataObj = dataArray.getJSONObject(i);
//                            SearchModel model = new SearchModel();
//                            model.setId(dataObj.optString("id"));
//                            model.setName(dataObj.optString("name"));
//                            searchModelArrayList.add(model);
//                        }
////                        searchItemAdapter.notifyDataSetChanged();
////                        if (searchModelArrayList.size()>0){
////                            searchPopup();
////                        }
////                    }
////                    else {
////                        dialog.dismiss();
////                    }
//                } else {
//                    Tosat(ProductUpload.this, jsonObject.optString("error_message"));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//    }

}
