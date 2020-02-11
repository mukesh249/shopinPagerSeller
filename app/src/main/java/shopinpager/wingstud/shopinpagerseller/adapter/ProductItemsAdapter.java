package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.ProductitemsItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.Product_item_Model;

public class ProductItemsAdapter extends RecyclerView.Adapter<ProductItemsAdapter.ViewHolder> {

    Context context;
    ArrayList<Product_item_Model> arrayList = new ArrayList<>();

    public ProductItemsAdapter(Context context, ArrayList<Product_item_Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductitemsItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext())
                        ,R.layout.productitems_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product_item_Model model = arrayList.get(position);
        holder.binding.noTv.setText(String.format("%d",position+1));
        holder.binding.weightTv.setText(String.format("%s",model.getWeight()));
        holder.binding.offerTv.setText(String.format("%s",model.getOffer()));
        holder.binding.priceTv.setText(String.format("%s",model.getPrice()));
        holder.binding.qtyTv.setText(String.format("%s",model.getQty()));
        holder.binding.sPriceTv.setText(String.format("%s",model.getSprice()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size()>0?arrayList.size():3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ProductitemsItemBinding binding;
        public ViewHolder(@NonNull ProductitemsItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
