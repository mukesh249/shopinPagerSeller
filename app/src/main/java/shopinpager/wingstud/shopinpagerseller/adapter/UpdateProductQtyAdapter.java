package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.UpdateProductQtyItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.UserGetProductItemModel;

public class UpdateProductQtyAdapter extends RecyclerView.Adapter<UpdateProductQtyAdapter.ViewHolder> {

    Context context;
    List<UserGetProductItemModel.Datum> arrayList;

    public UpdateProductQtyAdapter(Context context, List<UserGetProductItemModel.Datum> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UpdateProductQtyItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.update_product_qty_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserGetProductItemModel.Datum datum = arrayList.get(position);

        holder.binding.wightTv.setText(datum.weight);
        holder.binding.udpateQtyEt.setText(datum.qty);

        holder.binding.udpateQtyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                datum.qty = holder.binding.udpateQtyEt.getText().toString();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        UpdateProductQtyItemBinding binding;

        public ViewHolder(@NonNull UpdateProductQtyItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
