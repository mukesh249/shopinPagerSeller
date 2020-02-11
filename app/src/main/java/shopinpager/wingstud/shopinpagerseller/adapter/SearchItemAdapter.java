package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.ProductUpload;
import shopinpager.wingstud.shopinpagerseller.databinding.SearchItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.SearchModel;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.ViewHolder> {

    Context context;
    List<SearchModel.Datum> arrayList;
    public SearchItemAdapter(Context context, List<SearchModel.Datum> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.search_item,parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchModel.Datum model = arrayList.get(position);
        holder.binding.searchItem.setText(String.format("%s",model.name));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SearchItemBinding binding;
        public ViewHolder(@NonNull SearchItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
//
            binding.searchItemLL.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                ProductUpload.getInstance().getsearchData(arrayList.get(pos));
//                context.startActivity(new Intent(context, EditPost.class)
//                        .putExtra("id",arrayList.get(pos).id)
//                        .putExtra("data",arrayList.get(pos)+"")
//                );
            });

        }
    }
}
