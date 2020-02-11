package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.OrderDetailActi;
import shopinpager.wingstud.shopinpagerseller.databinding.PendingItemBinding;
import shopinpager.wingstud.shopinpagerseller.model.PendingListModel;

/**
 * Created by mukku on 24-08-2019.
 */

public class PendignListAdapter extends RecyclerView.Adapter<PendignListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PendingListModel> list;

    public PendignListAdapter(Context mContext, ArrayList<PendingListModel> list){
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PendingItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.pending_item, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PendingListModel orderdetailsModel = list.get(position);
        holder.binding.noTv.setText(String.format("%s",(position+1)));
        holder.binding.dateTV.setText(String.format("%s",orderdetailsModel.getOrder_date()));
        holder.binding.totalAmtTv.setText(String.format("%s",orderdetailsModel.getTotal_pending_amount()));
        holder.binding.adminComTv.setText(String.format("%s",orderdetailsModel.getTotal_commission()));
        holder.binding.TcsTv.setText(String.format("%s",orderdetailsModel.getTcs()));
        holder.binding.payAmountTv.setText(String.format("%s",orderdetailsModel.getTotal_payable_amount()));

        holder.binding.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, OrderDetailActi.class)
                        .putExtra("date",orderdetailsModel.getOrder_date())
                        .putExtra("activity","pending")
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private PendingItemBinding binding;

        public MyViewHolder(final PendingItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;
        }
    }
}
