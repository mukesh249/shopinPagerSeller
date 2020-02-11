package shopinpager.wingstud.shopinpagerseller.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.util.HashMap;

import shopinpager.wingstud.shopinpagerseller.Api.JsonDeserializer;
import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.ActivityForgetPasswordBinding;
import shopinpager.wingstud.shopinpagerseller.model.CommanResponse;

public class ForgetPassword extends AppCompatActivity implements WebCompleteTask {

    private ActivityForgetPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);


        binding.btnSubmit.setOnClickListener(v -> forgotPassword());

        binding.backIv.setOnClickListener(v -> finish());

    }
    public void forgotPassword() {

        HashMap objectNew = new HashMap();
        objectNew.put("mobile", binding.etMobileNo.getText().toString());

        new WebTask(ForgetPassword.this, WebUrls.BASE_URL + WebUrls.SellerForgotPassword, objectNew,
                ForgetPassword.this, RequestCode.CODE_ForgetPassword, 1);

    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_ForgetPassword==taskcode){
            System.out.println("ForgetPassword_res: " + response);
            CommanResponse commanResponse = JsonDeserializer.deserializeJson(response,CommanResponse.class);
            if (commanResponse.statusCode==1){
                startActivity(new Intent(ForgetPassword.this, OtpActi.class)
                        .putExtra("mobile",binding.etMobileNo.getText().toString()+"")
                        .putExtra("otp",commanResponse.otp+"")
                );
                finish();
            }
        }

    }
}
