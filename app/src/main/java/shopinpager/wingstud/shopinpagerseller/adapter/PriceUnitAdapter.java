package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.ProductUpload;
import shopinpager.wingstud.shopinpagerseller.databinding.PriceUnitItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.PriceUnitModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class PriceUnitAdapter extends RecyclerView.Adapter<PriceUnitAdapter.ViewHolder> {

    Context context;
    ArrayList<PriceUnitModel> arrayList = new ArrayList<>();
    PriceUnitItemBinding binding;

    public PriceUnitAdapter(Context context, ArrayList<PriceUnitModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.price_unit_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PriceUnitModel model = arrayList.get(position);

        if (position==0){
            holder.binding.addIg.setVisibility(View.GONE);
            holder.binding.removeIg.setVisibility(View.GONE);
        } else {
            holder.binding.removeIg.setVisibility(View.VISIBLE);
            holder.binding.addIg.setVisibility(View.GONE);
        }

        if (Utils.checkEmptyNull(model.getWeight()))
        holder.binding.weightEt.setText(model.getWeight());
        if (Utils.checkEmptyNull(model.getPrice()))
        holder.binding.priceEt.setText(model.getPrice());
        if (Utils.checkEmptyNull(model.getOffer()))
        holder.binding.offerEt.setText(model.getOffer());
        if (Utils.checkEmptyNull(model.getQty()))
        holder.binding.qtyEt.setText(model.getQty());



        holder.binding.weightEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayList.get(position).setWeight(holder.binding.weightEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.binding.priceEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayList.get(position).setPrice(holder.binding.priceEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.binding.offerEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayList.get(position).setOffer(holder.binding.offerEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        holder.binding.qtyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                arrayList.get(position).setQty(holder.binding.qtyEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PriceUnitItemBinding binding;

        public ViewHolder(@NonNull PriceUnitItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.addIg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.PrintMsg("Position: "+getAdapterPosition() );
                    ProductUpload.getInstance().addItem(getAdapterPosition());
                }
            });
            binding.removeIg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.PrintMsg("Position: "+getAdapterPosition() );
                    if (arrayList.size()>1) {
                        int pos = getAdapterPosition();
                        remove(pos,arrayList.get(pos));
                    }
                }
            });
        }
    }
    public void remove(int position, PriceUnitModel data) {
        arrayList.remove(data);
//        notifyItemChanged(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,arrayList.size());
    }
}
