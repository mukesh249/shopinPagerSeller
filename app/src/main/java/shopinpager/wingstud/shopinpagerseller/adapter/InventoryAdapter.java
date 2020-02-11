package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.Dashboard;
import shopinpager.wingstud.shopinpagerseller.databinding.InventoryItemBinding;
import shopinpager.wingstud.shopinpagerseller.fragment.ProductUploadList;
import shopinpager.wingstud.shopinpagerseller.model.InventoryModel;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ViewHolder> {


    ArrayList<InventoryModel> arrayList = new ArrayList<>();
    Context context;

    public InventoryAdapter(ArrayList<InventoryModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InventoryItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.inventory_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InventoryModel model = arrayList.get(position);

        holder.binding.catNameTv.setText(String.format("%s(%s)",model.getName(),model.getCount()));

        holder.itemView.setOnClickListener(v -> {
            ProductUploadList fragment = new ProductUploadList();
            FragmentManager fm = ((Dashboard)context).getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("from","inventory");
            bundle.putString("cat_id",model.getId());
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fmContainer,fragment).addToBackStack(null).commit();
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        InventoryItemBinding binding;
        public ViewHolder(@NonNull InventoryItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

}
