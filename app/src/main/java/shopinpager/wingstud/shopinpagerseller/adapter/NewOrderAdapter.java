package shopinpager.wingstud.shopinpagerseller.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

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
import shopinpager.wingstud.shopinpagerseller.activity.OrderedProductDetail;
import shopinpager.wingstud.shopinpagerseller.databinding.AddressviewLayoutBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.NewOrderItemBinding;
import shopinpager.wingstud.shopinpagerseller.fragment.NewOrder;
import shopinpager.wingstud.shopinpagerseller.model.NewOrderModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class NewOrderAdapter extends RecyclerView.Adapter<NewOrderAdapter.ViewHolder> implements WebCompleteTask {


    ArrayList<NewOrderModel> arrayList;
    Context context;
    int raw_post;
    public NewOrderAdapter(ArrayList<NewOrderModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewOrderItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.new_order_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewOrderModel newOrderModel = arrayList.get(position);

        holder.binding.oderId.setText(newOrderModel.getOrder_id());
        holder.binding.orderdate.setText(newOrderModel.getOrder_date());
        holder.binding.numOfProducts.setText(newOrderModel.getNum_of_pro());
        holder.binding.amountTv.setText(Double.parseDouble(newOrderModel.getTotal_amount())+Double.parseDouble(newOrderModel.getShipping_charge())+"");
        holder.binding.qtyTb.setText(newOrderModel.getQty());

        if (newOrderModel.getImageArray().size()==1){
            holder.binding.layouImae2.setVisibility(View.GONE);
            holder.binding.productImage1.setVisibility(View.VISIBLE);
            holder.binding.productImage2.setVisibility(View.GONE);

            Utils.ImageSet(context,holder.binding.productImage1, SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(0));
        }else if (newOrderModel.getImageArray().size() == 2){
            holder.binding.layouImae2.setVisibility(View.GONE);
            holder.binding.productImage1.setVisibility(View.VISIBLE);
            holder.binding.productImage2.setVisibility(View.VISIBLE);

            int width = 130;
            int height = 130;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            holder.binding.productImage1.setLayoutParams(parms);

            Utils.ImageSet(context,holder.binding.productImage1, SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(0));
            Utils.ImageSet(context,holder.binding.productImage2,SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(1));
        }else if (newOrderModel.getImageArray().size()==3){
            holder.binding.layouImae2.setVisibility(View.VISIBLE);
            holder.binding.productImage1.setVisibility(View.VISIBLE);
            holder.binding.productImage2.setVisibility(View.VISIBLE);
            holder.binding.productImage3.setVisibility(View.VISIBLE);
            holder.binding.productImage4.setVisibility(View.GONE);
            int width = 130;
            int height = 130;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            Utils.ImageSet(context,holder.binding.productImage1,SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(0));
            Utils.ImageSet(context,holder.binding.productImage2,SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(1));
            Utils.ImageSet(context,holder.binding.productImage3,SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(2));
        }else if (newOrderModel.getImageArray().size()==4) {
            holder.binding.layouImae2.setVisibility(View.VISIBLE);
            holder.binding.productImage1.setVisibility(View.VISIBLE);
            holder.binding.productImage2.setVisibility(View.VISIBLE);
            holder.binding.productImage3.setVisibility(View.VISIBLE);
            holder.binding.productImage4.setVisibility(View.VISIBLE);
            int width = 130;
            int height = 130;
            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(width,height);
            Utils.ImageSet(context,holder.binding.productImage1,SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(0));
            Utils.ImageSet(context,holder.binding.productImage2,SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(1));
            Utils.ImageSet(context,holder.binding.productImage3,SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(2));
            Utils.ImageSet(context,holder.binding.productImage4,SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getImageArray().get(3));
        }else {
            Utils.ImageSet(context,holder.binding.productImage1, SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getProduct_image());
        }

        holder.binding.txtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressDialog(newOrderModel.getAddress());
            }
        });

        holder.binding.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, OrderedProductDetail.class)
                        .putExtra("id",newOrderModel.getId())
                        .putExtra("order_no",newOrderModel.getOrder_id())
                        .putExtra("activity","neworder")
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        NewOrderItemBinding binding;
        public ViewHolder(@NonNull NewOrderItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            if (NewOrder.pending){
                binding.assignTv.setVisibility(View.VISIBLE);
                binding.cancelTv.setVisibility(View.VISIBLE);
            }else {
                binding.assignTv.setVisibility(View.GONE);
                binding.cancelTv.setVisibility(View.GONE);
            }
            binding.assignTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    raw_post = getAdapterPosition();
                    orderAssign(raw_post);
                }
            });
            binding.cancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    raw_post = getAdapterPosition();
                    orderCancel(raw_post);
                }
            });
        }
    }

    private void addressDialog(JSONObject addressObj){
        Utils.dismissRetryAlert();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AddressviewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.addressview_layout,null,false);
        builder.setView(binding.getRoot());

        binding.nameTv.setText(addressObj.optString("name").toUpperCase());
        binding.typeTv.setText(String.format("Type : %s",addressObj.optString("type")));
        binding.houseTv.setText(String.format("House %s",addressObj.optString("house")));
        binding.streetTv.setText(String.format("Street : %s",addressObj.optString("street")));
        binding.addressTv.setText(String.format("Address : %s",addressObj.optString("address")));
        binding.cityTv.setText(String.format("City : %s",addressObj.optString("city")));
        binding.stateTv.setText(String.format("%s (%s)",addressObj.optString("state"),addressObj.optString("pincode")));

        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow()!=null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void orderAssign(int pos){
        HashMap objectNew = new HashMap();
        objectNew.put("order_id",arrayList.get(pos).getId());
        objectNew.put("user_id",SharedPrefManager.getUserID(Constrants.UserId));

        Log.d("orderAssign_obj",objectNew.toString());
        new WebTask(context, WebUrls.BASE_URL+WebUrls.OrderAssign,objectNew,this, RequestCode.CODE_OrderAssign,1);
    }
    private void orderCancel(int pos){
        HashMap objectNew = new HashMap();
        objectNew.put("order_id",arrayList.get(pos).getId());

        Log.d("orderAssign_obj",objectNew.toString());
        new WebTask(context, WebUrls.BASE_URL+WebUrls.OrderCancel,objectNew,this, RequestCode.CODE_OrderAssign,1);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_OrderAssign == taskcode){
            Log.d("orderAction_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code")==1) {
                    arrayList.remove(raw_post);
                    notifyItemRemoved(raw_post);
                    notifyItemChanged(raw_post);
                    notifyItemRangeChanged(raw_post, arrayList.size());
                    Toast.makeText(context,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (RequestCode.CODE_Update_stock_status==taskcode){
            Log.d("Update_stock_status_res",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code")==1) {
                    arrayList.remove(raw_post);
                    notifyItemRemoved(raw_post);
                    notifyItemChanged(raw_post);
                    notifyItemRangeChanged(raw_post, arrayList.size());
                    Toast.makeText(context,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
