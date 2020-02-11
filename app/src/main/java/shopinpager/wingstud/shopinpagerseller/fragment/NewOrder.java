package shopinpager.wingstud.shopinpagerseller.fragment;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import shopinpager.wingstud.shopinpagerseller.databinding.NewOrderBinding;
import shopinpager.wingstud.shopinpagerseller.model.NewOrderModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

import static shopinpager.wingstud.shopinpagerseller.util.Utils.Tosat;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewOrder extends Fragment implements WebCompleteTask {

    private Context mContext;
    private View view;
    private NewOrderBinding binding;
    private NewOrderAdapter newOrderAdapter;
    private ArrayList<NewOrderModel> arrayList = new ArrayList<>();
    String order_status="pending";
    public static boolean pending = false, assign = false,delivered = false,cancelled = false, returnor = false, exchange=false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.new_order, container, false);
        view = binding.getRoot();

        binding.pendingTv.setOnClickListener(v -> {
            pending = true;
            assign = false;
            delivered = false;
            cancelled = false;
            returnor = false;
            exchange = false;
            GetOrderList();
        });
        binding.assignTv.setOnClickListener(v -> {
            pending = false;
            assign = true;
            delivered = false;
            cancelled = false;
            returnor = false;
            exchange = false;

            GetOrderList();
        });
        binding.deliveredTv.setOnClickListener(v -> {
            pending = false;
            assign = false;
            delivered = true;
            cancelled = false;
            returnor = false;
            exchange = false;

            GetOrderList();
        });
        binding.cancelledTv.setOnClickListener(v -> {
            pending = false;
            assign = false;
            delivered = false;
            cancelled = true;
            returnor = false;
            exchange = false;

            GetOrderList();
        });
        binding.returnTv.setOnClickListener(v -> {
            pending = false;
            assign = false;
            delivered = false;
            cancelled = false;
            returnor = true;
            exchange = false;
            GetOrderList();
        });
        binding.exchangeTv.setOnClickListener(v -> {
            pending = false;
            assign = false;
            delivered = false;
            cancelled = false;
            returnor = false;
            exchange = true;
            GetOrderList();
        });
        binding.noticeFAB.setOnClickListener(v -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fmContainer,new Notice()).addToBackStack(null).commit());


        initialize();
        return view;
    }

    public void ClickTv(TextView textView){

    }

    @Override
    public void onResume() {
        super.onResume();
        ((Dashboard) getContext()).setTitle(mContext.getString(R.string.todays_orders), false);
    }

    private void initialize() {
        binding.rvTOrders.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvTOrders.setItemAnimator(new DefaultItemAnimator());
        GetOrderList();
    }


    public void GetOrderList(){
        if (pending){
            order_status = getResources().getString(R.string.pending);
            binding.pendingTv.setBackground(getResources().getDrawable(R.drawable.color_primary_border));
            binding.assignTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.deliveredTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.white));
        }else if (assign){
            order_status = "assign_to_rider";
            binding.pendingTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.assignTv.setBackground(getResources().getDrawable(R.drawable.color_primary_border));
            binding.deliveredTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.white));
        }else if (delivered){
            order_status = getResources().getString(R.string.delivered);
            binding.pendingTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.assignTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.deliveredTv.setBackground(getResources().getDrawable(R.drawable.color_primary_border));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.white));
        }else if (cancelled){
            order_status = getResources().getString(R.string.cancelled);
            binding.pendingTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.assignTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.deliveredTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.cancelledTv.setBackground(getResources().getDrawable(R.drawable.color_primary_border));
            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.white));
        }else if (returnor){
            order_status = getResources().getString(R.string.rtrn);
            binding.pendingTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.assignTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.deliveredTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.returnTv.setBackground(getResources().getDrawable(R.drawable.color_primary_border));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.white));
        }else if (exchange){
            order_status = getResources().getString(R.string.exchange);
            binding.pendingTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.assignTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.deliveredTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.white));
            binding.exchangeTv.setBackground(getResources().getDrawable(R.drawable.color_primary_border));
        }
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        if (returnor){
            System.out.println("ReturnOrderList_obj: " + objectNew);
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.ReturnOrderList, objectNew, NewOrder.this, RequestCode.CODE_GetOrderList, 1);
        }
        else if (exchange){
            System.out.println("ExchangeOrderList_obj: " + objectNew);
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.ExchangeOrderList, objectNew, NewOrder.this, RequestCode.CODE_GetOrderList, 1);
        }else {
            objectNew.put("order_status", order_status.toLowerCase());
            System.out.println("GetOrderList_obj: " + objectNew);
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.GetOrderList, objectNew, NewOrder.this, RequestCode.CODE_GetOrderList, 1);
        }

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
                        binding.rvTOrders.setVisibility(View.VISIBLE);
                        for (int i=0;i<dataArray.length();i++){
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            NewOrderModel newOrderModel = new NewOrderModel();

                            newOrderModel.setQty(dataObj.optString("qty"));
                            newOrderModel.setNum_of_pro(dataObj.optString("number_of_products"));
                            newOrderModel.setOrder_date(dataObj.optString("created_at"));


                            String amount;
                            if (returnor || exchange){
                                newOrderModel.setId(dataObj.optJSONObject("order").optString("id"));
                                newOrderModel.setOrder_id(dataObj.optJSONObject("order").optString("order_id"));
                                newOrderModel.setProduct_image(dataObj.optString("product_image"));

                                newOrderModel.setShipping_charge(dataObj.optJSONObject("order").optString("shipping_charge"));
                                amount =  dataObj.optString("amount");
                                newOrderModel.setTotal_amount(amount);

                            }else {
                                amount =  dataObj.optString("total_amount");
                                newOrderModel.setTotal_amount(amount);

                                newOrderModel.setShipping_charge(dataObj.optString("shipping_charge"));

                                newOrderModel.setId(dataObj.optString("id"));
                                newOrderModel.setOrder_id(dataObj.optString("order_id"));
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

                            }

                            arrayList.add(newOrderModel);
                        }
                        newOrderAdapter= new NewOrderAdapter(arrayList,mContext);
                        binding.rvTOrders.setAdapter(newOrderAdapter);
                        newOrderAdapter.notifyDataSetChanged();
                    }else {
                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.rvTOrders.setVisibility(View.GONE);
                    }
                }else {
                    Tosat(getActivity(),jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
