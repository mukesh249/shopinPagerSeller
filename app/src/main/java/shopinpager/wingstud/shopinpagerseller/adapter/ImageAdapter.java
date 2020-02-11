package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.ImageItemBinding;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context context;
    ArrayList<String> arrayList = new ArrayList<>();

    public ImageAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.image_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Log.i("color_code",arrayList.get(position)+"");
//        if (arrayList.size()>0 && !arrayList.get(position).equals("[]"))
        Utils.setImage(context,holder.binding.imageView,
                SharedPrefManager.getImagePath(Constrants.ImagePath)+"/"+arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size()>0?arrayList.size():3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageItemBinding binding;
        public ViewHolder(@NonNull ImageItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
