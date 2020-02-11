package shopinpager.wingstud.shopinpagerseller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.ActivityLoginBinding;
import shopinpager.wingstud.shopinpagerseller.model.LoginUser;
import shopinpager.wingstud.shopinpagerseller.model.LoginViewModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class LoginActi extends AppCompatActivity implements WebCompleteTask {

    Context mContext;
    private ActivityLoginBinding binding;

    private String userName;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            String newToken = instanceIdResult.getToken();
            Log.i("newToken", newToken);
            SharedPrefManager.setDeviceToken(Constrants.Token,newToken);
        });
        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);
        mContext = LoginActi.this;
        loginViewModel.getUser().observe(this, loginUser -> {
            if (Utils.isValidMobileNumber(mContext, Objects.requireNonNull(loginUser).getMobileNumber()) &&
                    Utils.isPassword(mContext, Objects.requireNonNull(loginUser).getStrPassword())) {
                userName = binding.etMobileNo.getText().toString();
                LoingApi();
//                    binding.sellerTv.setText(loginUser.getMobileNumber() +"  "+loginUser.getStrPassword());
                SharedPrefManager.getInstance(mContext).hideSoftKeyBoard(LoginActi.this);
            }
        });


        binding.sellerTv.setOnClickListener(view -> startActivity(new Intent(mContext, Registration.class)));
        binding.forgotTv.setOnClickListener(v -> startActivity(new Intent(mContext,ForgetPassword.class)));
    }

//    public void loginProcess(View view) {
//        userName = binding.etMobileNo.getText().toString();
//        if (Utils.isValidMobileNumber(mContext, userName) && Utils.isPassword(mContext,binding.passwordEt.getText().toString().trim()) ){
//            LoingApi();
//        }
//    }

    public void LoingApi() {

        HashMap objectNew = new HashMap();
        objectNew.put("mobile", userName);
        objectNew.put("password", binding.passwordEt.getText().toString().trim());
        objectNew.put("device_token",SharedPrefManager.getDeviceToken(Constrants.Token));

        new WebTask(LoginActi.this, WebUrls.BASE_URL + WebUrls.Login, objectNew, LoginActi.this, RequestCode.CODE_Login, 1);

    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_Login == taskcode) {
            System.out.println("Login_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {

//                    SharedPrefManager.setImagePath(Constrants.ImagePath,jsonObject.optString("image_path"));
                    JSONObject dataObj = jsonObject.optJSONArray("data").optJSONObject(0);

                    SharedPrefManager.setUserID(Constrants.UserId, dataObj.optString("id"));
                    SharedPrefManager.setUserName(Constrants.UserName, dataObj.optString("username"));
                    SharedPrefManager.setUserEmail(Constrants.UserEmail, dataObj.optString("email"));
                    SharedPrefManager.setMobile(Constrants.UserMobile, dataObj.optString("mobile"));
                    SharedPrefManager.setProfilePic(Constrants.UserPic, dataObj.optJSONObject("user_kyc").optString("seller_image"));
                    SharedPrefManager.setAddress(Constrants.UserAddress, dataObj.optJSONObject("user_kyc").optString("address_1"));
                    if (dataObj.optString("verify_status").equalsIgnoreCase("verified")) {
                        SharedPrefManager.setLogin(Constrants.IsLogin, true);
                        Utils.Tosat(this, jsonObject.optString("message"));
                        startActivity(new Intent(LoginActi.this, Dashboard.class));
                        finish();
                    } else if (dataObj.optString("verify_status").equalsIgnoreCase("kyc_completed")) {
                        startActivity(new Intent(LoginActi.this, Waiting.class));
                    }else {
                        startActivity(new Intent(LoginActi.this, ProfileComplete.class)
                                .putExtra("user_kyc",dataObj.optJSONObject("user_kyc")+""));
                    }
                } else {
                    Utils.Tosat(this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
