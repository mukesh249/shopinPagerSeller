package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.ColorItemBinding;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ViewHolder> {

    Context context;
    ArrayList<String> arrayList = new ArrayList<>();

    public ColorAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ColorItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.color_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("color_code", arrayList.get(position) + "");
        if (arrayList.size()>0&&!arrayList.get(position).equals("null")) {
            holder.binding.colorView.setBackgroundColor(Color.parseColor(arrayList.get(position)));
        }else {

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ColorItemBinding binding;

        public ViewHolder(@NonNull ColorItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
