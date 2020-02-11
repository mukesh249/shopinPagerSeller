package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.SelectImageItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.ImageModel;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.ViewHolder> {

    Context context;
    ArrayList<Uri> arrayListUri;
    ArrayList<ImageModel> arrayListString;

    public SelectImageAdapter(Context context, ArrayList<Uri> arrayList, ArrayList<ImageModel> arrayListString) {
        this.context = context;
        this.arrayListUri = arrayList;
        this.arrayListString = arrayListString;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectImageItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.select_image_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(arrayListString.get(position).getImage()).into(holder.binding.selectImage);
        holder.binding.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayListString.get(position).getId()!=null){
//                    raw_post = i;
//                    DeleteIamgeMethod(i);
                }else {
                    for (int k =0;k<arrayListUri.size();k++){
                        arrayListUri.remove(k);
                    }
                    arrayListString.remove(position);
                    notifyItemRemoved(position);
                    notifyItemChanged(position);
                    notifyItemRangeChanged(position, arrayListString.size());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayListString.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SelectImageItemBinding binding;
        public ViewHolder(@NonNull SelectImageItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;


        }
    }
    public void remove(int position, Uri data) {
        arrayListUri.remove(data);
        notifyItemChanged(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,arrayListUri.size());
    }
}
