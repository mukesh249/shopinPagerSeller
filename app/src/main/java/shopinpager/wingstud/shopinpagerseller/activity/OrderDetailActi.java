package shopinpager.wingstud.shopinpagerseller.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.adapter.OrderDetailAdapter;
import shopinpager.wingstud.shopinpagerseller.databinding.ActivityOrderDetailBinding;
import shopinpager.wingstud.shopinpagerseller.model.OrderdetailsModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

import static shopinpager.wingstud.shopinpagerseller.util.Utils.Tosat;

public class OrderDetailActi extends AppCompatActivity implements WebCompleteTask {

    Toolbar toolbar;
    private Context mContext;
    private ActivityOrderDetailBinding binding;

    private ArrayList<OrderdetailsModel> deliveryArray = new ArrayList<>();
    private ArrayList<OrderdetailsModel> returnArray = new ArrayList<>();
    private ArrayList<OrderdetailsModel> exchangeArray = new ArrayList<>();
    private ArrayList<OrderdetailsModel> cancelledArray = new ArrayList<>();

    private String date;
    OrderDetailAdapter adapter;
    String acti = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);

        if (getIntent().getExtras() != null)
            date = getIntent().getExtras().getString("date", "");

        mContext = OrderDetailActi.this;
        initialize();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OrderDetailAdapter(OrderDetailActi.this, deliveryArray);
        binding.recyclerView.setAdapter(adapter);

        binding.shippedOrdTv.setOnClickListener(v -> {
            binding.shippedOrdTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));

            binding.shippedOrdTv.setTextColor(getResources().getColor(R.color.white));
            binding.returnTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.exchangeTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.cancelledTv.setTextColor(getResources().getColor(R.color.dark_gray));
            adapter = new OrderDetailAdapter(OrderDetailActi.this, deliveryArray);
            binding.recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
        binding.returnTv.setOnClickListener(v -> {
            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.shippedOrdTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));

            binding.shippedOrdTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.returnTv.setTextColor(getResources().getColor(R.color.white));
            binding.exchangeTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.cancelledTv.setTextColor(getResources().getColor(R.color.dark_gray));
            adapter = new OrderDetailAdapter(OrderDetailActi.this, returnArray);
            binding.recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
        binding.exchangeTv.setOnClickListener(v -> {

            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.shippedOrdTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.shippedOrdTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.returnTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.exchangeTv.setTextColor(getResources().getColor(R.color.white));
            binding.cancelledTv.setTextColor(getResources().getColor(R.color.dark_gray));
            adapter = new OrderDetailAdapter(OrderDetailActi.this, exchangeArray);
            binding.recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        });

        binding.cancelledTv.setOnClickListener(v -> {
            binding.returnTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.shippedOrdTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.exchangeTv.setBackgroundColor(getResources().getColor(R.color.Grey_200));
            binding.cancelledTv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

            binding.shippedOrdTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.returnTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.exchangeTv.setTextColor(getResources().getColor(R.color.dark_gray));
            binding.cancelledTv.setTextColor(getResources().getColor(R.color.white));
            adapter = new OrderDetailAdapter(OrderDetailActi.this, cancelledArray);
            binding.recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        binding.toolbar.activityTitle.setText(getString(R.string.order_details));
        binding.toolbar.imvNotification.setVisibility(View.GONE);
        binding.toolbar.bellRL.setVisibility(View.GONE);


        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbar.imvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startActivity(mContext, NotificationActi.class);
            }
        });
        if (getIntent().getExtras() != null) {
            acti = getIntent().getExtras().getString("activity", "");
            if (acti.equals("today")) {
                TodayPaymentDetails();
            } else if (acti.equals("pending")) {
                PendPaymentDetails();
            } else if (acti.equals("paid")) {
                PaidPaymentDetails();
            }
        }


    }

    public void TodayPaymentDetails() {
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("order_date", date);

        System.out.println("TodayPaymentDetails_obj: " + objectNew);
        new WebTask(this, WebUrls.BASE_URL + WebUrls.TodayPaymentDetails, objectNew, OrderDetailActi.this, RequestCode.CODE_PaidPaymentDetails, 1);

    }

    public void PendPaymentDetails() {

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("order_date", date);

        System.out.println("PendPaymentDetails_obj: " + objectNew);
        new WebTask(this, WebUrls.BASE_URL + WebUrls.PendPaymentDetails, objectNew, OrderDetailActi.this, RequestCode.CODE_PaidPaymentDetails, 1);

    }

    public void PaidPaymentDetails() {

        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("order_date", date);

        System.out.println("PaidPaymentDetails_obj: " + objectNew);
        new WebTask(this, WebUrls.BASE_URL + WebUrls.PaidPaymentDetails, objectNew, OrderDetailActi.this, RequestCode.CODE_PaidPaymentDetails, 1);

    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_PaidPaymentDetails == taskcode) {
            System.out.println("PaidPaymentDetails_res: " + response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONObject dataObj = jsonObject.optJSONObject("data");
                    binding.totalAmtTv.setText(String.format("%.2f", dataObj.optDouble("order_amount")));
                    binding.tcsTv.setText(String.format("%.2f", dataObj.optDouble("tcs")));
                    binding.tComTv.setText(String.format("%.2f", dataObj.optDouble("admin_commission")));
                    binding.payAbleAmtTv.setText(String.format("%.2f", dataObj.optDouble("payable_amount")));
                    JSONArray deliAry = dataObj.optJSONArray("delivered_order");
                    JSONArray canlAry = dataObj.optJSONArray("cancelled_order");
                    JSONArray retnAry = dataObj.optJSONArray("return_order");
                    JSONArray exchAry = dataObj.optJSONArray("exchange_order");
                    deliveryArray.clear();
                    returnArray.clear();
                    exchangeArray.clear();
                    cancelledArray.clear();
                    if (deliAry.length() > 0) {
                        for (int i = 0; i < deliAry.length(); i++) {
                            JSONObject listObj = deliAry.getJSONObject(i);
                            OrderdetailsModel orderdetailsModel = new OrderdetailsModel();
                            orderdetailsModel.setId(listObj.optString("id"));
                            orderdetailsModel.setOrder_id(listObj.optString("order_id"));
                            orderdetailsModel.setOrder_no(listObj.optJSONObject("order").optString("order_id"));
                            orderdetailsModel.setDate(listObj.optJSONObject("order").optString("delivery_date"));
                            orderdetailsModel.setOrder_no(listObj.optJSONObject("order").optString("order_id"));
                            orderdetailsModel.setStatus(listObj.optString("status"));
//                            double price = Double.parseDouble(String.format("%.2f",listObj.optDouble("price")));
//                            int qty = Integer.parseInt(listObj.optString("qty"));
                            orderdetailsModel.setPrice(String.format("%.2f", listObj.optJSONObject("order").optDouble("total_amount")));
                            deliveryArray.add(orderdetailsModel);
                        }
                    }
                    if (retnAry.length() > 0) {
                        for (int i = 0; i < retnAry.length(); i++) {
                            JSONObject listObj = retnAry.getJSONObject(i);
                            OrderdetailsModel orderdetailsModel = new OrderdetailsModel();
                            orderdetailsModel.setId(listObj.optString("id"));
                            orderdetailsModel.setOrder_id(listObj.optString("order_id"));
                            orderdetailsModel.setOrder_no(listObj.optJSONObject("order").optString("order_id"));
                            orderdetailsModel.setDate(listObj.optJSONObject("order").optString("shipped_date"));
                            orderdetailsModel.setStatus(listObj.optString("status"));
//                            double price = Double.parseDouble(String.format("%.2f",listObj.optDouble("price")));
//                            int qty = Integer.parseInt(listObj.optString("qty"));
                            orderdetailsModel.setPrice(String.format("%.2f", listObj.optJSONObject("order").optDouble("total_amount")));
                            returnArray.add(orderdetailsModel);
                        }
                    }
                    if (exchAry.length() > 0) {
                        for (int i = 0; i < exchAry.length(); i++) {
                            JSONObject listObj = exchAry.getJSONObject(i);
                            OrderdetailsModel orderdetailsModel = new OrderdetailsModel();
                            orderdetailsModel.setId(listObj.optString("id"));
                            orderdetailsModel.setOrder_id(listObj.optString("order_id"));
                            orderdetailsModel.setOrder_no(listObj.optJSONObject("order").optString("order_id"));
                            orderdetailsModel.setDate(listObj.optJSONObject("order").optString("shipped_date"));
                            orderdetailsModel.setStatus(listObj.optString("status"));
//                            double price = Double.parseDouble(String.format("%.2f",listObj.optDouble("price")));
//                            int qty = Integer.parseInt(listObj.optString("qty"));
                            orderdetailsModel.setPrice(String.format("%.2f", listObj.optJSONObject("order").optDouble("total_amount")));
                            exchangeArray.add(orderdetailsModel);
                        }
                    }
                    if (canlAry.length() > 0) {
                        for (int i = 0; i < canlAry.length(); i++) {
                            JSONObject listObj = canlAry.getJSONObject(i);
                            OrderdetailsModel orderdetailsModel = new OrderdetailsModel();
                            orderdetailsModel.setId(listObj.optString("id"));
                            orderdetailsModel.setOrder_id(listObj.optString("id"));
                            orderdetailsModel.setOrder_no(listObj.optString("order_id"));
                            orderdetailsModel.setDate(listObj.optString("delivery_date"));
                            orderdetailsModel.setStatus(listObj.optString("status"));
//                            double price = Double.parseDouble(String.format("%.2f",listObj.optDouble("price")));
//                            int qty = Integer.parseInt(listObj.optString("qty"));
                            orderdetailsModel.setPrice(String.format("%.2f", listObj.optDouble("total_amount")));
                            cancelledArray.add(orderdetailsModel);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Tosat(OrderDetailActi.this, jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
