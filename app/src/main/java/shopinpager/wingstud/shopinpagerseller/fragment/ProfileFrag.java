package shopinpager.wingstud.shopinpagerseller.fragment;


import android.app.AlertDialog;
import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.Dashboard;
import shopinpager.wingstud.shopinpagerseller.databinding.ChangePasswordLayoutBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.FragmentProfileBinding;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

import static shopinpager.wingstud.shopinpagerseller.util.Utils.FirstLatterCap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFrag extends Fragment implements WebCompleteTask {

    private View view;

    private Context mContext;

    private FragmentProfileBinding binding;
    ChangePasswordLayoutBinding changePasswordLayoutBinding;
    private AlertDialog alertDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        view = binding.getRoot();

        initialize();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Dashboard) getContext()).setTitle(mContext.getString(R.string.profile), false);
    }

    private void initialize() {
        binding.txtMobile.setText(SharedPrefManager.getMobile(Constrants.UserMobile));
        binding.txtGender.setText(SharedPrefManager.getMobile(Constrants.UserGender));

        if (SharedPrefManager.getAddress(Constrants.UserAddress) != null && !SharedPrefManager.getAddress(Constrants.UserAddress).isEmpty())
            binding.txtAddress.setText(FirstLatterCap(SharedPrefManager.getAddress(Constrants.UserAddress)));
        if (SharedPrefManager.getUserName(Constrants.UserName) != null && !SharedPrefManager.getUserName(Constrants.UserName).isEmpty())
            binding.bussnessNameTv.setText(FirstLatterCap(SharedPrefManager.getUserName(Constrants.UserName)));
        if (SharedPrefManager.getUserEmail(Constrants.UserEmail) != null && !SharedPrefManager.getUserEmail(Constrants.UserEmail).isEmpty())
            binding.emailTv.setText(FirstLatterCap(SharedPrefManager.getUserEmail(Constrants.UserEmail)));

        if (SharedPrefManager.getBusinessName(Constrants.BusinessName) != null && !SharedPrefManager.getBusinessName(Constrants.BusinessName).isEmpty())
            binding.txtName.setText(FirstLatterCap(SharedPrefManager.getBusinessName(Constrants.BusinessName)));
        Utils.setImage(getActivity(), binding.imvUser, WebUrls.ImageUrl
                + "/" + SharedPrefManager.getProfilePic(Constrants.UserPic));

//        ProfileApi(SharedPrefManager.getUserID(Constrants.UserId));

//        try {
//            JSONObject jsonObject = new JSONObject(SharedPrefManager.getResponse(Constrants.Response));
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        binding.changePassword.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            changePasswordLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity())
                    , R.layout.change_password_layout, null, false);
            builder.setView(changePasswordLayoutBinding.getRoot());

            changePasswordLayoutBinding.btnCancel.setOnClickListener(v -> alertDialog.dismiss());
            changePasswordLayoutBinding.btnSubmit.setOnClickListener(v -> {

                if (TextUtils.isEmpty(changePasswordLayoutBinding.oldPassEt.getText().toString().trim())) {
                    Utils.isEmpty(getActivity(), changePasswordLayoutBinding.oldPassEt.toString().trim(), "Old Password is Required");
                } else if (TextUtils.isEmpty(changePasswordLayoutBinding.newPassEt.getText().toString().trim())) {
                    Utils.isEmpty(getActivity(), changePasswordLayoutBinding.newPassEt.toString().trim(), "New Password is required");
                } else if (!changePasswordLayoutBinding.newPassEt.getText().toString().equals(changePasswordLayoutBinding.confirmPassEt.getText().toString())) {
                    Utils.isEmpty(getActivity(), changePasswordLayoutBinding.confirmPassEt.toString().trim(), "New and confirm password not matched");
                } else {
                    HashMap objectNew = new HashMap();
                    objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId) + "");
                    objectNew.put("old_password", changePasswordLayoutBinding.oldPassEt.getText().toString() + "");
                    objectNew.put("new_password", changePasswordLayoutBinding.newPassEt.getText().toString() + "");

                    Log.d("sellerUpdateItemQty_obj", objectNew.toString());
                    new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.SellerChangePassword,
                            objectNew, ProfileFrag.this, RequestCode.CODE_SellerChangePassword, 1);
                }

            });
            alertDialog = builder.create();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            if (alertDialog.getWindow() != null)
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
        });

    }


    public void ProfileApi(String mobile) {

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", mobile);

        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.ProfileApi, objectNew, ProfileFrag.this, RequestCode.CODE_ProfileById, 1);

    }

    @Override
    public void onComplete(String response, int taskcode) {

        if (RequestCode.CODE_ProfileById == taskcode) {
            System.out.println("Profile_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {

                    JSONObject dataObj = jsonObject.optJSONObject("data");

                    binding.txtMobile.setText(dataObj.optString("mobile"));
                    binding.txtGender.setText(dataObj.optJSONObject("user_kyc").optString("gender"));
                    binding.txtAddress.setText(FirstLatterCap(dataObj.optJSONObject("user_kyc").optString("address_1")));
                    binding.txtName.setText(FirstLatterCap(dataObj.optString("username")));

                } else {
                    Utils.Tosat(getActivity(), jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_SellerChangePassword == taskcode) {
            System.out.println("changepasword_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    Utils.Tosat(getActivity(), jsonObject.optString("message"));
                    alertDialog.dismiss();
                } else {
                    Utils.Tosat(getActivity(), jsonObject.optString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
