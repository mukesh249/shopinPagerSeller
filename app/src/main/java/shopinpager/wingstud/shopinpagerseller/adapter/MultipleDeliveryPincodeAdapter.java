package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.MulitiplePincodeItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.SelectedPinModel;

public class MultipleDeliveryPincodeAdapter extends RecyclerView.Adapter<MultipleDeliveryPincodeAdapter.ViewHolder> {

    Context context;
    ArrayList<SelectedPinModel> arrayList;

    public MultipleDeliveryPincodeAdapter(Context context, ArrayList<SelectedPinModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MulitiplePincodeItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.mulitiple_pincode_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("color_code", arrayList.get(position) + "");
        SelectedPinModel selectedPinModel = arrayList.get(position);
        holder.binding.deliveryPin.setText(selectedPinModel.getPincode());

        holder.binding.clearIv.setOnClickListener(v -> {
            if (arrayList.size()>1) {
                arrayList.remove(position);
                notifyItemRemoved(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MulitiplePincodeItemBinding binding;

        public ViewHolder(@NonNull MulitiplePincodeItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
