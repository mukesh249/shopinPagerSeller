package shopinpager.wingstud.shopinpagerseller.fragment;


import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import shopinpager.wingstud.shopinpagerseller.activity.PendingPayment;
import shopinpager.wingstud.shopinpagerseller.activity.PerviousPayment;
import shopinpager.wingstud.shopinpagerseller.databinding.PaymentBinding;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Payment extends Fragment implements WebCompleteTask {

    private Context mContext;
    private View view;
    private PaymentBinding binding;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.payment, container, false);
        view = binding.getRoot();

        binding.viewDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), PerviousPayment.class).putExtra("act","today")));
        binding.pendViewDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), PendingPayment.class)));
        binding.paidViewDetails.setOnClickListener(v -> startActivity(new Intent(getActivity(), PerviousPayment.class).putExtra("act","paid")));
        PaymentOverView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Dashboard) getContext()).setTitle(mContext.getString(R.string.payment_overview), false);
    }


    public void PaymentOverView(){

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));

        new WebTask(getActivity(), WebUrls.BASE_URL+ WebUrls.PaymentOverView,objectNew, Payment.this, RequestCode.CODE_PaymentOverView,1);

    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_PaymentOverView == taskcode){
            System.out.println("PaymentOverView_res: "+response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code")==1){
                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    binding.amountTv.setText(String.format("%.2f",dataObj.optDouble("today_payemnt")));
                    binding.pendAmountTv.setText(String.format("%.2f",dataObj.optDouble("pending_amount")));
                    binding.paidAmountTv.setText(String.format("%.2f",dataObj.optDouble("paid_amount")));

                }else {
                    Utils.Tosat(getActivity(),jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
