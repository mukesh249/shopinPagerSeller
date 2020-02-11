package shopinpager.wingstud.shopinpagerseller.fragment;


import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.Dashboard;
import shopinpager.wingstud.shopinpagerseller.adapter.NewOrderAdapter;
import shopinpager.wingstud.shopinpagerseller.databinding.FragmentHomeBinding;
import shopinpager.wingstud.shopinpagerseller.model.NewOrderModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

import static shopinpager.wingstud.shopinpagerseller.util.Utils.Tosat;
import static shopinpager.wingstud.shopinpagerseller.util.Utils.checkEmptyNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFrag extends Fragment implements WebCompleteTask {

    private View view;
    private Context mContext;
    private FragmentHomeBinding binding;
    private NewOrderAdapter newOrderAdapter;
    private ArrayList<NewOrderModel> arrayList = new ArrayList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        view = binding.getRoot();

        mContext = getActivity();
        binding.newOrderRv.setLayoutManager(new LinearLayoutManager(mContext));
        Utils.recyclerView(binding.newOrderRv);
        newOrderAdapter = new NewOrderAdapter(arrayList,mContext);
        binding.newOrderRv.setAdapter(newOrderAdapter);
        newOrderAdapter.notifyDataSetChanged();

        DashBoad();
        GetOrderList();
        binding.refreshlayout.setOnRefreshListener(() -> {
            DashBoad();
            GetOrderList();
        });

        binding.totalpedingLL.setOnClickListener(v -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fmContainer, new NewOrder())
                .addToBackStack(null).commit());
        binding.TodayAmountLL.setOnClickListener(v -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fmContainer, new Payment())
                .addToBackStack(null).commit());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Dashboard) getContext()).setTitle("Home", false);
    }

    public void DashBoad(){

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        System.out.println("DashBoad_obj: "+objectNew);
        new WebTask(getActivity(), WebUrls.BASE_URL+ WebUrls.Dashboard_data,objectNew, HomeFrag.this, RequestCode.CODE_Dashboard_data,1);

    }
    public void GetOrderList(){
        NewOrder.pending = true;
        NewOrder.assign = false;
        NewOrder.delivered = false;
        NewOrder.cancelled = false;
        NewOrder.returnor = false;
        NewOrder.exchange = false;
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("order_status","pending");

        System.out.println("GetOrderList_obj: "+objectNew);
        new WebTask(getActivity(), WebUrls.BASE_URL+ WebUrls.GetOrderList,objectNew, HomeFrag.this, RequestCode.CODE_GetOrderList,1);

    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_GetOrderList == taskcode){
            System.out.println("GetOrderList_res: "+response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code")==1){
//                    SharedPrefManager.setImagePath(Constrants.ImagePath,jsonObject.optString("image_path"));
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    arrayList.clear();
                    if (dataArray.length()>0){
                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.newOrderRv.setVisibility(View.VISIBLE);
                        for (int i=0;i<dataArray.length();i++){
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            NewOrderModel newOrderModel = new NewOrderModel();
                            newOrderModel.setId(dataObj.optString("id"));
                            newOrderModel.setOrder_id(dataObj.optString("order_id"));
                            newOrderModel.setQty(dataObj.optString("qty"));
                            newOrderModel.setNum_of_pro(dataObj.optString("number_of_products"));
                            newOrderModel.setTotal_amount(dataObj.optString("total_amount"));
                            newOrderModel.setShipping_charge(dataObj.optString("shipping_charge"));
                            newOrderModel.setOrder_date(dataObj.optString("created_at"));

                            if (dataObj.optJSONArray("product_image").length()>0) {
                                JSONArray productImage = dataObj.optJSONArray("product_image");
                                List<String> ImageArray = new ArrayList<>();
                                for (int j = 0; j < productImage.length(); j++) {
                                    ImageArray.add(productImage.get(j).toString());
                                }
                                newOrderModel.setImageArray(ImageArray);
                            }

                            if (dataObj.optJSONObject("delivery_address")!=null){
                                newOrderModel.setAddress(dataObj.optJSONObject("delivery_address"));
                            }

                            arrayList.add(newOrderModel);
                        }
                        newOrderAdapter.notifyDataSetChanged();
                    }else {
                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.newOrderRv.setVisibility(View.GONE);
                    }
                }else {
                    Tosat(getActivity(),jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Dashboard_data == taskcode) {
            System.out.println("Dashboard_data_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {

                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    binding.pendingTv.setText(dataObj.optString("pending_order"));
                    binding.totalSaleTv.setText(String.format("Rs. %.2f",dataObj.optDouble("total_paid_amount")));
                    binding.totalpayTv.setText(String.format("Rs. %.2f",dataObj.optDouble("today_payment")));

                    if (checkEmptyNull(dataObj.optString("notify_count")))
                        Dashboard.getInstance().setCountItem(dataObj.optInt("notify_count"));
                    binding.refreshlayout.setRefreshing(false);
                }

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

    }
}
