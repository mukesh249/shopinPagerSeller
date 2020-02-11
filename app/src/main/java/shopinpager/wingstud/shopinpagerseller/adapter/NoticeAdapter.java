package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.model.NoticeModel;
import shopinpager.wingstud.shopinpagerseller.databinding.OrderListRowBinding;

/**
 * Created by mukesh verma on 13-09-2019.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<NoticeModel> list;

    public NoticeAdapter(Context mContext, ArrayList<NoticeModel> list){
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderListRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.order_list_row, parent, false);

        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoticeModel bean = list.get(position);
        holder.binding.titleTv.setText(bean.getHeading());
        holder.binding.descriptionTv.setText(Html.fromHtml(bean.getDescription()));
        holder.binding.postdateTV.setText(String.format("Posted : %s",bean.getCreated_at()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private OrderListRowBinding binding;

        public MyViewHolder(final OrderListRowBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }
}
