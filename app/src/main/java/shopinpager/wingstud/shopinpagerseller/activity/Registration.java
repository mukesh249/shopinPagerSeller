package shopinpager.wingstud.shopinpagerseller.activity;

import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.ActivityRagistrationBinding;
import shopinpager.wingstud.shopinpagerseller.model.StateModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

import static shopinpager.wingstud.shopinpagerseller.util.Utils.Tosat;

public class Registration extends AppCompatActivity implements WebCompleteTask {

    ActivityRagistrationBinding binding;

    private ArrayList<StateModel> arrayList = new ArrayList<>();
    ArrayList<String> stateName = new ArrayList<>();
    ArrayList<String> stateId = new ArrayList<>();

    ArrayList<String> cityID = new ArrayList<>();
    ArrayList<String> cityName = new ArrayList<>();
    private String country_id = "", state_id = "", city_id = "101";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ragistration);
        initialize();

        binding.bottomLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(Registration.this,LoginActi.class));
                finish();
            }
        });
    }

    private void initialize() {

        setSupportActionBar(binding.toolbar.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.toolbar.toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_white));
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        StateApi("");

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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnSubmit.setOnClickListener(v -> RegistrationApi());


    }


    public void RegistrationApi() {

        if (TextUtils.isEmpty(binding.etFullName.getText().toString().trim())) {
            Utils.isEmpty(Registration.this,binding.etFullName.toString().trim(),"Name is Required");
        } else if (TextUtils.isEmpty(binding.etName.getText().toString().trim())) {
            Utils.isEmpty(Registration.this,binding.etName.toString().trim(),"Business Name is Required");
        } else if (TextUtils.isEmpty(binding.etMobileNo.getText().toString().trim())) {
            Utils.isEmpty(Registration.this,binding.etMobileNo.toString().trim(),getResources().getString(R.string.mobile_no_required));
        } else if (TextUtils.isEmpty(binding.etPincode.getText().toString().trim())) {
            Utils.isEmpty(Registration.this,binding.etPincode.toString().trim(),getResources().getString(R.string.pincode_required));
        } else if (TextUtils.isEmpty(binding.etAddress.getText().toString().trim())) {
            Utils.isEmpty(Registration.this,binding.etAddress.toString().trim(),getResources().getString(R.string.address_required));
        } else if (TextUtils.isEmpty(state_id)) {
            Tosat(Registration.this, "Please select state");
        } else if (TextUtils.isEmpty(city_id)) {
            Tosat(Registration.this, "Please select city");
        } else {
            HashMap objectNew = new HashMap();
            objectNew.put("username", binding.etName.getText().toString().trim());
            objectNew.put("business_name", binding.etFullName.getText().toString().trim());
            objectNew.put("mobile", binding.etMobileNo.getText().toString().trim());
            objectNew.put("email", binding.etEmail.getText().toString().trim());
            objectNew.put("gender", binding.genderSpinner.getSelectedItem().toString());
            objectNew.put("country_id", country_id);
            objectNew.put("state_id", state_id);
            objectNew.put("city_id", city_id);
            objectNew.put("address_2", binding.etAddress.getText().toString());
            objectNew.put("pincode", binding.etPincode.getText().toString());

            Utils.PrintMsg("Registration_obj: " + objectNew);
            new WebTask(Registration.this, WebUrls.BASE_URL + WebUrls.Registration, objectNew, Registration.this, RequestCode.CODE_Register, 1);
        }
    }

    public void StateApi() {
        HashMap objectNew = new HashMap();
        objectNew.put("country_id", country_id);
        new WebTask(Registration.this, WebUrls.BASE_URL + WebUrls.StateApi, objectNew, Registration.this, RequestCode.CODE_State, 1);
    }

    public void CityApi() {
        HashMap objectNew = new HashMap();
        objectNew.put("state_id", state_id);
        new WebTask(Registration.this, WebUrls.BASE_URL + WebUrls.CityApi, objectNew, Registration.this, RequestCode.CODE_City, 1);
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_Register == taskcode){
            System.out.println("Registration_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    String mes = "Thanks for submitting your details. Your username and password will be sent to you by SMS shortly";
                    Tosat(Registration.this,mes);
                    finish();
                }else {
                    Tosat(Registration.this,jsonObject.optString("error_message"));
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
//                        if (checkEmptyNull("33")) {


//                        }
                        binding.stateSpinner.setAdapter(state_adapter);
                        binding.stateSpinner.setSelection(
                                getIndex(binding.stateSpinner, "Rajasthan"));

                    }

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
                    ArrayAdapter<String> state_adapter;
                    if (dataArray.length() > 0) {

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.optJSONObject(i);
                            cityName.add(dataObj.optString("name"));
                            cityID.add(dataObj.optString("id"));
                        }
                        state_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, cityName);


                    }else {
                        city_id = "";
                        state_adapter = new ArrayAdapter<String>(this,
                                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.cityArray));
                    }
                    state_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.citySpinner.setAdapter(state_adapter);

                } else {
                    Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}