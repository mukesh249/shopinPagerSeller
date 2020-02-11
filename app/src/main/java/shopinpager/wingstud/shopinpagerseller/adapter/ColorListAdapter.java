package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.ProductUpload;
import shopinpager.wingstud.shopinpagerseller.databinding.ColorSelectItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.ColorModel;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class ColorListAdapter extends RecyclerView.Adapter<ColorListAdapter.ViewHolder> {

    Context context;
    ArrayList<ColorModel> arrayList;
    private final ArrayList<Integer> seleccionados = new ArrayList<>();
    public static ArrayList<String> colorCodeArray = new ArrayList<>();
    public static ArrayList<String> colorArray = new ArrayList<>();

    public ColorListAdapter(Context context, ArrayList<ColorModel> arrayList, ArrayList<String> colorArray) {
        this.context = context;
        this.arrayList = arrayList;
        this.colorArray = colorArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ColorSelectItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.color_select_item, parent,
                false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("color_code", arrayList.get(position).getCode() + "");


        if (!arrayList.get(position).getCode().equals("null")) {
            if (colorArray.size() > 0) {
                for (int i = 0; i < colorArray.size(); i++) {
                    if (colorArray.get(i).equals(arrayList.get(position).getCode())) {
                        seleccionados.add(position);
                        colorCodeArray.add(arrayList.get(position).getCode());
                        holder.binding.colorlistRl.setBackground(context.getResources().getDrawable(R.drawable.color_primary_rounded_border));
                    }
                }

            }
            Utils.shapeBackground(holder.binding.colorCode, arrayList.get(position).getCode());
            holder.binding.colorNameRv.setText(String.format("%s(%s)"
                    , arrayList.get(position).getColor_name()
                    , arrayList.get(position).getCode())
            );
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ColorSelectItemBinding binding;

        public ViewHolder(@NonNull ColorSelectItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.colorlistRl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ProductUpload.isClickable) {
                       //sdfa
                    } else {
                        int position = getAdapterPosition();
                        if (seleccionados.contains(position)) {
                            seleccionados.remove((Integer) position);
                            colorCodeArray.remove(arrayList.get(position).getCode());
                            binding.colorlistRl.setBackground(context.getResources().getDrawable(R.drawable.rounded_colorlist_bg));
                        } else {
                            seleccionados.add(position);
                            colorCodeArray.add(arrayList.get(position).getCode());
                            binding.colorlistRl.setBackground(context.getResources().getDrawable(R.drawable.color_primary_rounded_border));
                        }
                    }
                }
            });

        }
    }
}
