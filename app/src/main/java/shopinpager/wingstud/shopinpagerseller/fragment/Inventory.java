package shopinpager.wingstud.shopinpagerseller.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import shopinpager.wingstud.shopinpagerseller.activity.Dashboard;
import shopinpager.wingstud.shopinpagerseller.adapter.InventoryAdapter;
import shopinpager.wingstud.shopinpagerseller.databinding.FragmentInventoryBinding;
import shopinpager.wingstud.shopinpagerseller.model.InventoryModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class Inventory extends Fragment implements WebCompleteTask {


    public Inventory() {
        // Required empty public constructor
    }

    private Context mContext;
    private View view;
    private FragmentInventoryBinding binding;
    private InventoryAdapter adapter;
    private ArrayList<InventoryModel> arrayList = new ArrayList<>();
    public static boolean ins = false, outs = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_inventory, container, false);

        binding.inStockTv.setOnClickListener(v -> {
            InstcokMethod();
            binding.inStockTv.setBackground(getResources().getDrawable(R.drawable.rounded_color_primary_btn_bg));
            binding.inStockTv.setTextColor(getResources().getColor(R.color.white));
            binding.outStockTv.setBackground(getResources().getDrawable(R.drawable.color_primary_rounded_border));
            binding.outStockTv.setTextColor(getResources().getColor(R.color.black));
        });

        binding.outStockTv.setOnClickListener(v -> {
            OutstcokMethod();
            binding.outStockTv.setBackground(getResources().getDrawable(R.drawable.rounded_color_primary_btn_bg));
            binding.outStockTv.setTextColor(getResources().getColor(R.color.white));
            binding.inStockTv.setBackground(getResources().getDrawable(R.drawable.color_primary_rounded_border));
            binding.inStockTv.setTextColor(getResources().getColor(R.color.black));
        });
        if (outs) {
            OutstcokMethod();
            binding.outStockTv.setBackground(getResources().getDrawable(R.drawable.rounded_color_primary_btn_bg));
            binding.outStockTv.setTextColor(getResources().getColor(R.color.white));
            binding.inStockTv.setBackground(getResources().getDrawable(R.drawable.color_primary_rounded_border));
            binding.inStockTv.setTextColor(getResources().getColor(R.color.black));
        } else {
            InstcokMethod();
            binding.inStockTv.setBackground(getResources().getDrawable(R.drawable.rounded_color_primary_btn_bg));
            binding.inStockTv.setTextColor(getResources().getColor(R.color.white));
            binding.outStockTv.setBackground(getResources().getDrawable(R.drawable.color_primary_rounded_border));
            binding.outStockTv.setTextColor(getResources().getColor(R.color.black));
        }

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new InventoryAdapter(arrayList, getActivity());
        binding.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Dashboard) getContext()).setTitle(mContext.getString(R.string.inventory), false);
    }

    public void InstcokMethod() {
        ins = true;
        outs = false;
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        System.out.println("In_Stock_obj: " + objectNew);
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.Inventory_in_stk_list, objectNew, Inventory.this, RequestCode.CODE_In_Stock, 1);
    }

    public void OutstcokMethod() {
        ins = false;
        outs = true;
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        System.out.println("Out_Stock_obj: " + objectNew);
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.Inventory_ot_stk_list, objectNew, Inventory.this, RequestCode.CODE_Out_Stock, 1);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_In_Stock == taskcode) {
            System.out.println("In_Stock_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                arrayList.clear();
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    if (dataArray.length() > 0) {
                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            InventoryModel model = new InventoryModel();
                            model.setId(dataObj.optString("id"));
                            model.setCount(dataObj.optString("count"));
                            model.setDescription(dataObj.optString("description"));
                            model.setImage(dataObj.optString("image"));
                            model.setName(dataObj.optString("name"));
                            model.setProduct_id(dataObj.optString("product_id"));
                            arrayList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    binding.relEmptyWL.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
//                    Tosat(getActivity(),jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Out_Stock == taskcode) {
            System.out.println("Out_Stock_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                arrayList.clear();
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    if (dataArray.length() > 0) {
                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            InventoryModel model = new InventoryModel();
                            model.setId(dataObj.optString("id"));
                            model.setCount(dataObj.optString("count"));
                            model.setDescription(dataObj.optString("description"));
                            model.setImage(dataObj.optString("image"));
                            model.setName(dataObj.optString("name"));
                            model.setProduct_id(dataObj.optString("product_id"));
                            arrayList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    binding.relEmptyWL.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
//                    Tosat(getActivity(),jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
