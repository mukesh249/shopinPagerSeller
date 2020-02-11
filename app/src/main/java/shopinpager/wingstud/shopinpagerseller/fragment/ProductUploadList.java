package shopinpager.wingstud.shopinpagerseller.fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.Dashboard;
import shopinpager.wingstud.shopinpagerseller.activity.ProductUpload;
import shopinpager.wingstud.shopinpagerseller.adapter.ProductListAdapter;
import shopinpager.wingstud.shopinpagerseller.databinding.UploadProductListBinding;
import shopinpager.wingstud.shopinpagerseller.model.ProductListModel;
import shopinpager.wingstud.shopinpagerseller.model.Product_item_Model;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

import static shopinpager.wingstud.shopinpagerseller.util.Utils.Tosat;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductUploadList extends Fragment implements WebCompleteTask {

    private Context context;
    private View view;

    public ProductUploadList() {
        // Required empty public constructor
    }

    ArrayList<ProductListModel> arrayList = new ArrayList<>();
    ProductListAdapter adapter;
    UploadProductListBinding binding;
    public static boolean inventroy = false, product_up = false, duplicate_product = false;
    String cat_id = "";

    @Override
    public void onAttach(Context mcontext) {
        super.onAttach(context);
        context = mcontext;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.upload_product_list, container, false);

        ((Dashboard) context).setTitle("Product List", false);

        context = getActivity();

        binding.addFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivity(context, ProductUpload.class);
            }
        });

        if (getArguments() != null && getArguments().getString("from", "").equals("inventory")) {
            inventroy = true;
            product_up = false;
            duplicate_product = false;
            cat_id = getArguments().getString("cat_id", "");
            binding.addFAB.setVisibility(View.GONE);
            InventoryList(1);

        } else if (getArguments() != null && getArguments().getString("from", "").equals("duplicateProduct")) {
            duplicate_product = true;
            inventroy = false;
            product_up = false;
            cat_id = getArguments().getString("cat_id", "");
            binding.addFAB.setVisibility(View.GONE);
            DuplicateProduct(1);
        } else {
            inventroy = false;
            product_up = true;
            duplicate_product = false;
            binding.addFAB.setVisibility(View.VISIBLE);
            ProducUpltList(1);
        }
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProductListAdapter(arrayList, context);
        binding.recyclerView.setAdapter(adapter);

        if (TextUtils.isEmpty(binding.searchEt.getText().toString())) {
            binding.clearIcon.setVisibility(View.GONE);
            hideKeyboard(getActivity());
        } else {
//            Selection.setSelection(binding.searchEt.getText(), binding.searchEt.getText().toString().length());
            binding.clearIcon.setVisibility(View.VISIBLE);
        }
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.searchEt.getText().toString())) {
                    binding.clearIcon.setVisibility(View.GONE);
                    hideKeyboard(getActivity());
                } else {
                    binding.clearIcon.setVisibility(View.VISIBLE);
                }
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                adapter.getFilter().filter(binding.searchEt.getText());
//                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });
        binding.clearIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchEt.setText("");
                binding.clearIcon.setVisibility(View.GONE);
            }
        });

        return binding.getRoot();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null && getArguments().getString("from", "").equals("inventory")) {
            inventroy = true;
            product_up = false;
            duplicate_product = false;
            cat_id = getArguments().getString("cat_id", "");
            binding.addFAB.setVisibility(View.GONE);
            InventoryList(5);

        } else if (getArguments() != null && getArguments().getString("from", "").equals("duplicateProduct")) {
            duplicate_product = true;
            inventroy = false;
            product_up = false;
            cat_id = getArguments().getString("cat_id", "");
            binding.addFAB.setVisibility(View.GONE);
            DuplicateProduct(5);
        } else {
            inventroy = false;
            product_up = true;
            duplicate_product = false;
            binding.addFAB.setVisibility(View.VISIBLE);
            ProducUpltList(5);
        }

    }

    public void ProducUpltList(int method) {
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.Product_list, objectNew, ProductUploadList.this, RequestCode.CODE_Notice, method);
    }

    public void DuplicateProduct(int method) {
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.DuplicateProductList, objectNew, ProductUploadList.this, RequestCode.CODE_DuplicateProductList, method);
    }

    public void InventoryList(int method) {
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("cat_id", cat_id);
        if (Inventory.ins) {
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.Inventory_in_stk_prod_list, objectNew, ProductUploadList.this, RequestCode.CODE_Notice, method);
        } else {
            new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.Inventory_ot_stk_prod_list, objectNew, ProductUploadList.this, RequestCode.CODE_Notice, method);
        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Notice == taskcode) {
            arrayList.clear();
            System.out.println("ProducUpltList_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    if (dataArray.length() > 0) {
//                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            ProductListModel model = new ProductListModel();
                            model.setItemobje(dataArray.getJSONObject(i));
                            model.setId(dataObj.optString("id"));
                            model.setCreated_at(dataObj.optString("created_at"));
                            model.setName(dataObj.optString("name"));

                            if (dataObj.optJSONObject("main_category") != null)
                                model.setCategory_name(dataObj.optJSONObject("main_category").optString("name"));
                            if (dataObj.optJSONObject("sub_category") != null)
                                model.setSub_category_name(dataObj.optJSONObject("sub_category").optString("name"));
                            if (dataObj.optInt("is_admin_approved") == 1) {
                                model.setStatus("Approved");
                            } else {
                                model.setStatus("Pending");
                            }
                            if (dataObj.optJSONArray("product_image").length() > 0) {
                                JSONArray productImage = dataObj.optJSONArray("product_image");
                                ArrayList<String> ImageArray = new ArrayList<>();
                                for (int j = 0; j < productImage.length(); j++) {
                                    ImageArray.add(productImage.getJSONObject(j).optString("image"));
                                }
                                model.setImageArray(ImageArray);
                            }
                            JSONArray productAry = dataObj.optJSONArray("product_item");
                            if (productAry.length() > 0) {
                                ArrayList<Product_item_Model> itemModelAry = new ArrayList<>();
                                for (int j = 0; j < productAry.length(); j++) {
                                    Product_item_Model productItemModel = new Product_item_Model();
                                    JSONObject proObj = productAry.optJSONObject(j);
                                    if (proObj.optString("seller_id").compareTo(SharedPrefManager.getUserID(Constrants.UserId)) == 0) {
                                        productItemModel.setWeight(proObj.optString("weight"));
                                        productItemModel.setPrice(String.format("%s", proObj.optString("price")));
                                        productItemModel.setOffer(proObj.optString("offer"));
                                        productItemModel.setQty(proObj.optString("qty"));
                                        productItemModel.setItemid(proObj.optString("id"));
                                        productItemModel.setSprice(String.format("%s", proObj.optString("sprice")));
                                        itemModelAry.add(productItemModel);
                                    }
                                }
                                model.setProductitem(itemModelAry);
                            }

                            if (!dataObj.optString("color").equals("null") && !TextUtils.isEmpty(dataObj.optString("color"))) {
                                String[] clrAry = dataObj.optString("color").split(",");
                                ArrayList<String> colorArray = new ArrayList<>();
                                Collections.addAll(colorArray, clrAry);
                                model.setColorAry(colorArray);
                            }

                            arrayList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
//                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Tosat(getActivity(), jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_DuplicateProductList == taskcode) {
            arrayList.clear();
            System.out.println("DuplicateProductList_res: " + response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code") == 1) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");

                    if (dataArray.length() > 0) {
//                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            ProductListModel model = new ProductListModel();
                            model.setItemobje(dataArray.getJSONObject(i).optJSONObject("product"));

                            model.setId(dataObj.optString("product_id"));
                            model.setSeller_id(dataObj.optString("seller_id"));
                            model.setCreated_at(dataObj.optString("created_at"));

                            model.setCategory_name(dataObj.optJSONObject("product").optJSONObject("main_category").optString("name"));
                            model.setSub_category_name(dataObj.optJSONObject("product").optJSONObject("sub_category").optString("name"));
                            if (dataObj.optJSONObject("product").optInt("is_admin_approved") == 1) {
                                model.setStatus("Approved");
                            } else {
                                model.setStatus("Pending");
                            }
                            if (dataObj.optJSONObject("product").optJSONArray("product_image").length() > 0) {
                                JSONArray productImage = dataObj.optJSONObject("product").optJSONArray("product_image");
                                ArrayList<String> ImageArray = new ArrayList<>();
                                for (int j = 0; j < productImage.length(); j++) {
                                    ImageArray.add(productImage.getJSONObject(j).optString("image"));
                                }
                                model.setImageArray(ImageArray);
                            }
                            JSONArray productAry = dataObj.optJSONObject("product").optJSONArray("product_item");
                            if (productAry.length() > 0) {
                                ArrayList<Product_item_Model> itemModelAry = new ArrayList<>();
                                for (int j = 0; j < productAry.length(); j++) {
                                    Product_item_Model productItemModel = new Product_item_Model();
                                    JSONObject proObj = productAry.optJSONObject(j);
                                    if (proObj.optString("seller_id").compareTo(SharedPrefManager.getUserID(Constrants.UserId)) == 0) {
                                        productItemModel.setWeight(proObj.optString("weight"));
                                        productItemModel.setPrice(String.format("%s", proObj.optString("price")));
                                        productItemModel.setOffer(proObj.optString("offer"));
                                        productItemModel.setQty(proObj.optString("qty"));
                                        productItemModel.setSprice(String.format("%s", proObj.optDouble("sprice")));
                                        itemModelAry.add(productItemModel);
                                    }
                                }
                                model.setProductitem(itemModelAry);
                            }

                            if (!dataObj.optJSONObject("product").optString("color").equals("null")
                                    && !TextUtils.isEmpty(dataObj.optJSONObject("product").optString("color"))) {
                                String[] clrAry = dataObj.optJSONObject("product").optString("color").split(",");
                                ArrayList<String> colorArray = new ArrayList<>();
                                Collections.addAll(colorArray, clrAry);
                                model.setColorAry(colorArray);
                            }

                            arrayList.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
//                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    }
                } else {
                    Tosat(getActivity(), jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
