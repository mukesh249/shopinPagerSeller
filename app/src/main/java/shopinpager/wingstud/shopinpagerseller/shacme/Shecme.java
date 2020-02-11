package shopinpager.wingstud.shopinpagerseller.shacme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

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
import shopinpager.wingstud.shopinpagerseller.databinding.ActivityShecmeBinding;
import shopinpager.wingstud.shopinpagerseller.model.ProductNameModel;
import shopinpager.wingstud.shopinpagerseller.model.SelectNameModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Shecme extends AppCompatActivity implements WebCompleteTask {

    private int REQUEST_CAMERA_PERMISSION = 1;
    Uri imageUri;
    private ActivityShecmeBinding binding;
    String select_id = "", cat_id = "", sub_cat_id = "", product_id = "";

    ArrayList<String> CatName = new ArrayList<>();
    ArrayList<String> CatId = new ArrayList<>();
    ArrayList<String> SubCatName = new ArrayList<>();
    ArrayList<String> SubCatId = new ArrayList<>();
    ArrayList<String> ProductName = new ArrayList<>();
    ArrayList<String> ProductId = new ArrayList<>();
    ArrayList<String> SelectName = new ArrayList<>();
    ArrayList<String> SelectId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shecme);

//        binding.selectImageRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.selectImageLL.setOnClickListener(v -> ImagePopup());

        CatMethod();
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
                    GetProductNameForScheme();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        binding.productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ProductName.size() > 1 && position > 0) {
                    product_id = ProductId.get(position);
                    GetProductItemForScheme();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (SelectName.size() > 1 && position > 0) {
                    select_id = SelectId.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.toolbar.bellRL.setVisibility(View.GONE);
        binding.toolbar.activityTitle.setText("Upload Scheme");
        binding.imageViewLL.setVisibility(View.GONE);
        binding.submitProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productUploadMethod();
            }
        });
    }

    public void CatMethod() {
        HashMap objectNew = new HashMap();
        new WebTask(this, WebUrls.BASE_URL + WebUrls.Cat_list, objectNew,
                Shecme.this, RequestCode.CODE_Cat_list, 5);
    }

    public void SubCatMethod() {
        HashMap objectNew = new HashMap();
        objectNew.put("cat_id", cat_id);
        new WebTask(this, WebUrls.BASE_URL + WebUrls.SubCat_list, objectNew,
                Shecme.this, RequestCode.CODE_SubCat_list, 5);
    }

    public void GetProductNameForScheme() {
        HashMap objectNew = new HashMap();
        objectNew.put("sub_category_id", sub_cat_id);
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        Log.i("ProductName_obj", objectNew + "");
        new WebTask(this, WebUrls.BASE_URL + WebUrls.GetProductNameForScheme, objectNew,
                Shecme.this, RequestCode.CODE_GetProductNameForScheme, 5);
    }

    public void GetProductItemForScheme() {
        HashMap objectNew = new HashMap();
        objectNew.put("product_id", product_id);
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        Log.i("ProductItem_obj", objectNew + "");
        new WebTask(this, WebUrls.BASE_URL + WebUrls.GetProductItemForScheme, objectNew,
                Shecme.this, RequestCode.CODE_GetProductItemForScheme, 5);
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

    public void productUploadMethod() {
        if (binding.mainCatSpinner.getSelectedItem()!=null&&binding.mainCatSpinner.getSelectedItem().toString().isEmpty()) {
            retryAlertDialog(getString(R.string.app_name), "Please select main category");
        } else if (binding.subCatSpinner.getSelectedItem()!=null &&binding.subCatSpinner.getSelectedItem().toString().isEmpty()) {
            retryAlertDialog(getString(R.string.app_name), "Please select sub category");
        } else if (binding.productSpinner.getSelectedItem()!=null &&binding.productSpinner.getSelectedItem().toString().isEmpty()) {
            retryAlertDialog(getString(R.string.app_name), "Please select product name");
        } else if (binding.selectSpinner.getSelectedItem()!=null &&binding.selectSpinner.getSelectedItem().toString().isEmpty()) {
            retryAlertDialog(getString(R.string.app_name), "Please select scheme name");
        } else if (!Utils.checkEmptyNull(binding.schemeNameEt.getText().toString())) {
            retryAlertDialog(getString(R.string.app_name), "Please enter scheme name");
        } else if (imageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading, please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();

            MultipartBody.Part filePartmultipleImages;
            Log.d("upload_image_array", imageUri.toString());

            File file = new File(getPath(Shecme.this, imageUri));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            filePartmultipleImages = MultipartBody.Part.createFormData("scheme_image", file.getName(), requestBody);

            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SharedPrefManager.getUserID(Constrants.UserId));
            RequestBody catId = RequestBody.create(MediaType.parse("text/plain"), cat_id);
            RequestBody subCatId = RequestBody.create(MediaType.parse("text/plain"), sub_cat_id);
            RequestBody productId = RequestBody.create(MediaType.parse("text/plain"), product_id);
            RequestBody productItemId = RequestBody.create(MediaType.parse("text/plain"), select_id);
            RequestBody scheme_name = RequestBody.create(MediaType.parse("text/plain"), binding.schemeNameEt.getText() + "");

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<JsonObject> call = getResponse.uploadSchemeProduct(
                    user_id,
                    catId,
                    subCatId,
                    productId,
                    productItemId,
                    scheme_name,
                    filePartmultipleImages
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
                                Toast.makeText(Shecme.this, obj.optString("message"), Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(Shecme.this, obj.optString("message"), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(Shecme.this, "" + t.toString(), Toast.LENGTH_SHORT).show();
                    System.out.println("fail response.." + t.toString());
//                        dismissProgressBar();
                }
            });

        } else {
            retryAlertDialog(getString(R.string.app_name), "Please Select Image");
        }
    }



    @Override
    public void onComplete(String response, int taskcode) {
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

                    }
                } else {
                    Utils.Tosat(this, jsonObject.optString("error_message"));
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

                    }
                } else {
                    Utils.Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_GetProductNameForScheme == taskcode) {
            System.out.println("ProductName_res: " + response);

            ProductNameModel productName = JsonDeserializer.deserializeJson(response, ProductNameModel.class);
            if (productName.statusCode==1){
                ProductName.clear();
                ProductId.clear();
                if (!productName.data.isEmpty()){
                    for (int i = 0; i < productName.data.size(); i++) {
                        ProductName.add(productName.data.get(i).name);
                        ProductId.add(productName.data.get(i).id+"");
                    }
                    ProductName.add(0, "Select Product Name");
                    ProductId.add(0, "0");
                    ArrayAdapter<String> product_Adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, ProductName);
                    product_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.productSpinner.setAdapter(product_Adapter);
                }
            }
        }
        if (RequestCode.CODE_GetProductItemForScheme == taskcode) {
            System.out.println("ProductItem_res: " + response);

            SelectNameModel selectNameModel = JsonDeserializer.deserializeJson(response, SelectNameModel.class);
            if (selectNameModel.statusCode==1){
                SelectName.clear();
                SelectId.clear();
                if (!selectNameModel.data.isEmpty()){
                    for (int i = 0; i < selectNameModel.data.size(); i++) {
                        SelectName.add(String.format("%s - ₹%s",selectNameModel.data.get(i).weight,selectNameModel.data.get(i).sprice));
                        SelectId.add(selectNameModel.data.get(i).id+"");
                    }
                    SelectName.add(0, "Select Product Name");
                    SelectId.add(0, "0");
                    ArrayAdapter<String> select_Adapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, SelectName);
                    select_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.selectSpinner.setAdapter(select_Adapter);
                }
            }
        }
    }

    private void ImagePopup() {
        try {
            final Dialog dialog = new Dialog(Shecme.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.pop_profile);
            dialog.show();

            LinearLayout txtGallery = (LinearLayout) dialog.findViewById(R.id.layoutGallery);
            LinearLayout txtCamera = (LinearLayout) dialog.findViewById(R.id.layoutCamera);
            txtCamera.setOnClickListener(v -> {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(Shecme.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Shecme.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                    } else {

                        selectCameraImage();
                        dialog.dismiss();
                    }
                } else {
                    selectCameraImage();
                    dialog.dismiss();
                }
            });
            txtGallery.setOnClickListener(v -> {
                int currentAPIVersion = Build.VERSION.SDK_INT;
                if (currentAPIVersion >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(Shecme.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Shecme.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
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
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                binding.selectImage.setImageBitmap(selectedImage);
                binding.imageViewLL.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else if (requestCode == 300) {
            Uri selectedImageUri = null;
            selectedImageUri = imageUri;
            final InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                binding.selectImage.setImageBitmap(selectedImage);
                binding.imageViewLL.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
}
