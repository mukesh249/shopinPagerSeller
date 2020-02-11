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
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import shopinpager.wingstud.shopinpagerseller.Api.JsonDeserializer;
import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.EditPost;
import shopinpager.wingstud.shopinpagerseller.activity.ProductUpload;
import shopinpager.wingstud.shopinpagerseller.databinding.RetryAlertBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.UpdateProductQtyPopupBinding;
import shopinpager.wingstud.shopinpagerseller.model.ProductListModel;
import shopinpager.wingstud.shopinpagerseller.databinding.PopUpProductDetailsBinding;
import shopinpager.wingstud.shopinpagerseller.databinding.ProductUploadListItemBinding;
import shopinpager.wingstud.shopinpagerseller.fragment.Inventory;
import shopinpager.wingstud.shopinpagerseller.fragment.ProductUploadList;
import shopinpager.wingstud.shopinpagerseller.model.UserGetProductItemModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> implements WebCompleteTask , Filterable {


    ArrayList<ProductListModel> arrayList;
    ArrayList<ProductListModel> arrayList_flter;
    Context context;
    AlertDialog dialog;
    int raw_post;
    String type = "";

    public ProductListAdapter(ArrayList<ProductListModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.arrayList_flter = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductUploadListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.product_upload_list_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductListModel model = arrayList.get(position);

        holder.binding.IdTv.setText(String.format("ID : %s", model.getId()));
        holder.binding.createdTv.setText(String.format("%s", model.getCreated_at()));
        holder.binding.nameTv.setText(String.format("%s", model.getName()));
        holder.binding.cateTv.setText(String.format("%s", model.getCategory_name()));
        holder.binding.subCatTv.setText(String.format("%s", model.getSub_category_name()));

        if (ProductUploadList.duplicate_product) {
            holder.binding.deleteImage.setVisibility(View.VISIBLE);
        } else {
            if (String.format("%s", model.getStatus()).equals("Approved")) {
                holder.binding.deleteImage.setVisibility(View.GONE);
                holder.binding.statusTv.setTextColor(context.getResources().getColor(R.color.green));
            } else {
                holder.binding.deleteImage.setVisibility(View.VISIBLE);
                holder.binding.statusTv.setTextColor(context.getResources().getColor(R.color.red));
            }
        }

        holder.binding.statusTv.setText(String.format("%s", model.getStatus()));

        if (model.getImageArray().size() > 0)
            Utils.setImage(context, holder.binding.productImage1, SharedPrefManager.getImagePath(Constrants.ImagePath) + "/" + model.getImageArray().get(0));

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

        ProductUploadListItemBinding binding;

        public ViewHolder(@NonNull ProductUploadListItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            if (ProductUploadList.inventroy) {
                if (Inventory.ins) {
                    binding.editTv.setText(context.getString(R.string.mark_out_of_stock));
                } else {
                    binding.editTv.setText(context.getString(R.string.mark_in_stock));
                }
            } else {
                binding.editTv.setText(context.getString(R.string.edit));
            }

            binding.deleteImage.setOnClickListener(view -> {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                RetryAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context)
                        , R.layout.retry_alert
                        , null,
                        false);
                alertDialog.setView(binding.getRoot());

                binding.txtRTitle.setText(context.getResources().getString(R.string.app_name));
                binding.txtRAMsg.setText(context.getString(R.string.ar_u_sr_delt));
                binding.txtRAFirst.setText(context.getString(R.string.cancel));
                binding.txtRASecond.setText(context.getString(R.string.yes));
                dialog = alertDialog.create();
                binding.txtRASecond.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        raw_post = getAdapterPosition();
                        if (ProductUploadList.duplicate_product) {
                            DeleteDupItem(raw_post);
                        } else {
                            DeleteProduct(raw_post);
                        }
                    }
                });


                binding.txtRAFirst.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                if (dialog.getWindow() != null)
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            });

            binding.editTv.setOnClickListener(v -> {
                raw_post = getAdapterPosition();
                if (binding.editTv.getText().equals(context.getString(R.string.edit))) {
                    if (ProductUploadList.duplicate_product) {
                        context.startActivity(new Intent(context, ProductUpload.class)
                                .putExtra("id", arrayList.get(raw_post).getId())
                                .putExtra("data", arrayList.get(raw_post).getItemobje() + "")
                        );
                    } else {
                        context.startActivity(new Intent(context, EditPost.class)
                                .putExtra("id", arrayList.get(raw_post).getId())
                                .putExtra("data", arrayList.get(raw_post).getItemobje() + "")
                        );
                    }

                } else if (binding.editTv.getText().equals(context.getString(R.string.mark_out_of_stock))) {
                    type = "out_stock";
                    MarkForOutStock(raw_post);
                } else {
                    type = "in_stock";
                    updateQty(raw_post);
                }
            });
        }
    }

    AlertDialog alertDialog;
    UpdateProductQtyAdapter productQtyAdapter;
    UpdateProductQtyPopupBinding qtyPopupBinding;
    List<UserGetProductItemModel.Datum> data = new ArrayList<>();
    private void updateQty(int raw) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        qtyPopupBinding = DataBindingUtil.inflate(LayoutInflater.from(context)
                , R.layout.update_product_qty_popup, null, false);
        builder.setView(qtyPopupBinding.getRoot());
        qtyPopupBinding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        updateProduct(raw);
        qtyPopupBinding.btnCancel.setOnClickListener(v -> alertDialog.dismiss());
        qtyPopupBinding.btnSubmit.setOnClickListener(v -> {

            ArrayList<String> idAry = new ArrayList<>();
            ArrayList<String> qtyAry = new ArrayList<>();

            for (int i =0;i<data.size();i++){
                idAry.add(data.get(i).id);
                qtyAry.add(data.get(i).qty);
            }
            Log.d("id_array",idAry+"");
            Log.d("qty_array",qtyAry+"");

            HashMap objectNew = new HashMap();
            objectNew.put("product_id", arrayList.get(raw).getId());
            objectNew.put("qty", qtyAry+"");
            objectNew.put("item_id", idAry+"");

            Log.d("sellerUpdateItemQty_obj", objectNew.toString());
            new WebTask(context, WebUrls.BASE_URL + WebUrls.SellerUpdateItemQty,
                    objectNew, this, RequestCode.CODE_SellerUpdateItemQty, 1);
        });
        alertDialog = builder.create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }


    private void MarkForOutStock(int pos) {
        HashMap objectNew = new HashMap();
        objectNew.put("product_id", arrayList.get(pos).getId());
        objectNew.put("type", type);
        Log.d("MarkForOutStock_obj", objectNew.toString());
        new WebTask(context, WebUrls.BASE_URL + WebUrls.Update_stock_status, objectNew, this, RequestCode.CODE_Update_stock_status, 1);
    }

    private void updateProduct(int pos) {
        HashMap objectNew = new HashMap();
        objectNew.put("seller_id", SharedPrefManager.getUserID(Constrants.UserId));
        objectNew.put("product_id", arrayList.get(pos).getId());
        Log.d("updateProduct_obj", objectNew.toString());
        new WebTask(context, WebUrls.BASE_URL + WebUrls.UserGetProductItem, objectNew, this, RequestCode.CODE_UserGetProductItem, 1);
    }
    private void DeleteDupItem(int pos) {
        HashMap objectNew = new HashMap();
        objectNew.put("product_id", arrayList.get(pos).getId());
        Log.d("DeleteDupItem_obj", objectNew.toString());
        new WebTask(context, WebUrls.BASE_URL + WebUrls.DeleteDupItem, objectNew, this, RequestCode.CODE_Delete_Product, 1);
    }

    private void DeleteProduct(int pos) {
        HashMap objectNew = new HashMap();
        objectNew.put("product_id", arrayList.get(pos).getId());
        Log.d("DeleteProduct_obj", objectNew.toString());
        new WebTask(context, WebUrls.BASE_URL + WebUrls.Delete_Product, objectNew, this, RequestCode.CODE_Delete_Product, 1);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Delete_Product == taskcode) {
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
        if (RequestCode.CODE_Update_stock_status == taskcode) {
            Log.d("Update_stock_status_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {
                    arrayList.remove(raw_post);
                    notifyItemRemoved(raw_post);
                    notifyItemChanged(raw_post);
                    notifyItemRangeChanged(raw_post, arrayList.size());
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        if (RequestCode.CODE_UserGetProductItem == taskcode){
            UserGetProductItemModel getProductItemModel = JsonDeserializer.deserializeJson(response,UserGetProductItemModel.class);

            if (getProductItemModel.statusCode==1){
                data.clear();
                data.addAll(getProductItemModel.data);
                productQtyAdapter = new UpdateProductQtyAdapter(context,data);
                qtyPopupBinding.recyclerView.setAdapter(productQtyAdapter);
                productQtyAdapter.notifyDataSetChanged();
            }
        }
        if (RequestCode.CODE_SellerUpdateItemQty == taskcode) {
            Log.d("SellerUpdateItemQty_res", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.optInt("status_code") == 1) {
                    arrayList.remove(raw_post);
                    notifyItemRemoved(raw_post);
                    notifyItemChanged(raw_post);
                    notifyItemRangeChanged(raw_post, arrayList.size());
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void addressDialog(ProductListModel model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        PopUpProductDetailsBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(context),
                        R.layout.pop_up_product_details,
                        null,
                        false);
        builder.setView(binding.getRoot());
        Log.i("product_det", model.getColorAry() + "");

        binding.IdTv.setText(String.format("ID : %s", model.getId()));
        binding.typeTv.setText(String.format("Name : %s",model.getName()));
        binding.createdTv.setText(String.format("%s", model.getCreated_at()));
        binding.cateTv.setText(String.format("%s", model.getCategory_name()));
        binding.subCatTv.setText(String.format("%s", model.getSub_category_name()));
        binding.statusTv.setText(String.format("%s", model.getStatus()));

//        binding.colorRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//        if (model.getColorAry().size() > 0 && !model.getColorAry().get(0).equals("null")) {
//            binding.colorLL.setVisibility(View.VISIBLE);
//            ColorAdapter adapter = new ColorAdapter(context, model.getColorAry());
//            binding.colorRv.setAdapter(adapter);
//            notifyDataSetChanged();
//        } else {
//            binding.colorLL.setVisibility(View.GONE);
//        }
        if (model.getProductitem() != null && model.getProductitem().size() > 0) {
            binding.productItemRv.setVisibility(View.VISIBLE);
            binding.productItemRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            ProductItemsAdapter productItemsAdapter = new ProductItemsAdapter(context, model.getProductitem());
            binding.productItemRv.setAdapter(productItemsAdapter);
            notifyDataSetChanged();
        } else {
            binding.productItemRv.setVisibility(View.GONE);
        }

        if (model.getImageArray().size() > 0 && !model.getImageArray().get(0).equals("null")) {
            binding.imageRv.setVisibility(View.VISIBLE);
            binding.imageRv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            ImageAdapter imageAdapter = new ImageAdapter(context, model.getImageArray());
            binding.imageRv.setAdapter(imageAdapter);
            notifyDataSetChanged();
        } else {
            binding.imageRv.setVisibility(View.GONE);
        }


        AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null)
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    arrayList = arrayList_flter;
                } else {
                    ArrayList<ProductListModel> filteredList = new ArrayList<>();
                    for (ProductListModel row : arrayList_flter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())
//                                || row.getCategory_name().toLowerCase().contains(charString.toLowerCase())
//                                || row.getSub_category_name().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
                        }
                    }

                    arrayList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayList = (ArrayList<ProductListModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
