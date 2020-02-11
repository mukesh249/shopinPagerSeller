package shopinpager.wingstud.shopinpagerseller.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.AddressviewLayoutBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.OrderProductItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.OrderProductModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.ViewHolder> {


    ArrayList<OrderProductModel> arrayList = new ArrayList<>();
    Context context;

    public OrderProductAdapter(ArrayList<OrderProductModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderProductItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.order_product_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderProductModel newOrderModel = arrayList.get(position);

        holder.binding.productNameTv.setText(newOrderModel.getName());
        holder.binding.weightTv.setText(newOrderModel.getWeight());
        holder.binding.amountTv.setText(newOrderModel.getPrice());
        holder.binding.qtyTb.setText(newOrderModel.getQty());
        holder.binding.statusTv.setText(newOrderModel.getStatus());
        Utils.setImage(context,holder.binding.productImage, SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+newOrderModel.getProduct_image());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        OrderProductItemBinding binding;
        public ViewHolder(@NonNull OrderProductItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    private void addressDialog(JSONObject addressObj){
        Utils.dismissRetryAlert();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AddressviewLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.addressview_layout,null,false);
        builder.setView(binding.getRoot());

        binding.nameTv.setText(addressObj.optString("name").toUpperCase());
        binding.typeTv.setText(String.format("Typue : %s",addressObj.optString("type")));
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
}
