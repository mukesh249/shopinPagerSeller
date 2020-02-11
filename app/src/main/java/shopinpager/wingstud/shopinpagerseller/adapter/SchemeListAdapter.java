package shopinpager.wingstud.shopinpagerseller.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.RetryAlertBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.SchemeListItemBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.SchemeViewBinding;
import shopinpager.wingstud.shopinpagerseller.model.SchemeListModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class SchemeListAdapter extends RecyclerView.Adapter<SchemeListAdapter.ViewHolder> implements WebCompleteTask {


    List<SchemeListModel.Datum> arrayList;
    Context context;
    AlertDialog dialog;
    int raw_post;
    String type = "";

    public SchemeListAdapter(List<SchemeListModel.Datum> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SchemeListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.scheme_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SchemeListModel.Datum model = arrayList.get(position);

        holder.binding.IdTv.setText(String.format("ID : %s", model.productId));
        holder.binding.schemeNameTv.setText(String.format("%s", model.offerName));

        if (model.getProduct.name!=null)
        holder.binding.productNameTv.setText(String.format("%s", model.getProduct.name));
        holder.binding.weightTv.setText(String.format("%s -  ₹%s", model.getProductItem.weight, model.getProductItem.sprice));

        Utils.setImage(context, holder.binding.productImage1, WebUrls.BASE_URL + WebUrls.SchemeImage + model.image);

        holder.binding.viewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressDialog(model);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SchemeListItemBinding binding;

        public ViewHolder(@NonNull SchemeListItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.deleteImage.setOnClickListener(view -> {
                AlertDialog.Builder alertDialog  = new AlertDialog.Builder(context);
                RetryAlertBinding binding= DataBindingUtil.inflate(LayoutInflater.from(context)
                        ,R.layout.retry_alert
                        ,null,
                        false);
                alertDialog.setView(binding.getRoot());

                binding.txtRTitle.setText(context.getResources().getString(R.string.app_name));
                binding.txtRAMsg.setText(context.getString(R.string.ar_u_sr_delt));
                binding.txtRAFirst.setText(context.getString(R.string.cancel));
                binding.txtRASecond.setText(context.getString(R.string.yes));
                dialog = alertDialog.create();
                binding.txtRASecond.setOnClickListener(v -> {
                    raw_post = getAdapterPosition();
                    DeleteItem(raw_post);
                });


                binding.txtRAFirst.setOnClickListener(v -> dialog.dismiss());

                if (dialog.getWindow()!=null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            });
        }
    }

    //    private void MarkForOutStock(int pos){
//        HashMap objectNew = new HashMap();
//        objectNew.put("product_id",arrayList.get(pos).getId());
//        objectNew.put("type",type);
//        Log.d("MarkForOutStock_obj",objectNew.toString());
//        new WebTask(context, WebUrls.BASE_URL+WebUrls.Update_stock_status,objectNew,this, RequestCode.CODE_Update_stock_status,1);
//    }
//
    private void DeleteItem(int pos){
        HashMap objectNew = new HashMap();
        objectNew.put("scheme_id",arrayList.get(pos).id+"");
        Log.d("DeleteItem_obj",objectNew.toString());
        new WebTask(context, WebUrls.BASE_URL+WebUrls.DeleteSchemeProduct,objectNew,this, RequestCode.CODE_DeleteSchemeProduct,1);
    }
//    private void DeleteProduct(int pos){
//        HashMap objectNew = new HashMap();
////        objectNew.put("product_id",arrayList.get(pos).getId());
//        Log.d("DeleteProduct_obj",objectNew.toString());
//        new WebTask(context, WebUrls.BASE_URL+WebUrls.Delete_Product,objectNew,this, RequestCode.CODE_Delete_Product,1);
//    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_DeleteSchemeProduct == taskcode) {
            Log.d("DeleteProduct_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {
                    arrayList.remove(raw_post);
                    notifyItemRemoved(raw_post);
                    notifyItemChanged(raw_post);
                    notifyItemRangeChanged(raw_post, arrayList.size());
                    dialog.dismiss();
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addressDialog(SchemeListModel.Datum model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        SchemeViewBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context),
                        R.layout.scheme_view,
                        null,
                        false);
        builder.setView(binding.getRoot());
//        Log.i("product_det",model.getColorAry()+"");

        binding.IdTv.setText(String.format("ID : %s", model.productId));
        binding.schemeNameTv.setText(String.format("%s", model.offerName));
        binding.productNameTv.setText(String.format("%s", model.getProduct.name));
        binding.weightTv.setText(String.format("%s -  ₹%s", model.getProductItem.weight, model.getProductItem.sprice));
        Utils.setImage(context, binding.productImage1, WebUrls.BASE_URL + WebUrls.SchemeImage + model.image);
//        if (model.getColorAry().size()>0 && !model.getColorAry().get(0).equals("null")) {
//            binding.colorLL.setVisibility(View.VISIBLE);
//            ColorAdapter adapter = new ColorAdapter(context, model.getColorAry());
//            binding.colorRv.setAdapter(adapter);
//            notifyDataSetChanged();
//        }else {
//        binding.colorLL.setVisibility(View.GONE);
////        }
//        if (model.getProductitem() != null && model.getProductitem().size() > 0) {
//            binding.productItemRv.setVisibility(View.VISIBLE);
//            binding.productItemRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
//            ProductItemsAdapter productItemsAdapter = new ProductItemsAdapter(context, model.getProductitem());
//            binding.productItemRv.setAdapter(productItemsAdapter);
//            notifyDataSetChanged();
//        } else {
//            binding.productItemRv.setVisibility(View.GONE);
//        }
//
//        if (model.getImageArray().size() > 0 && !model.getImageArray().get(0).equals("null")) {
//            binding.imageRv.setVisibility(View.VISIBLE);
//            binding.imageRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            ImageAdapter imageAdapter = new ImageAdapter(context, model.getImageArray());
//            binding.imageRv.setAdapter(imageAdapter);
//            notifyDataSetChanged();
//        } else {
//            binding.imageRv.setVisibility(View.GONE);
//        }


        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

}
