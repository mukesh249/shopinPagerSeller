package shopinpager.wingstud.shopinpagerseller.adapter;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.OrderDetailActi;
import shopinpager.wingstud.shopinpagerseller.activity.PerviousPayment;
import shopinpager.wingstud.shopinpagerseller.model.PaymentPerModel;
import shopinpager.wingstud.shopinpagerseller.databinding.TodaysPayLayoutBinding;

/**
 * Created by hemant on 24-08-2019.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PaymentPerModel> list;
    String activty="";

    public PaymentAdapter(Context mContext, ArrayList<PaymentPerModel> list){
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TodaysPayLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.todays_pay_layout, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PaymentPerModel paymentPerModel = list.get(position);
        holder.binding.noTv.setText(String.format("%s",(position+1)));
        holder.binding.dateTV.setText(String.format("%s",paymentPerModel.getOrder_date()));
        holder.binding.adminComTv.setText(String.format("Rs. %s",paymentPerModel.getTotal_commission()));
        holder.binding.payAmountTv.setText(String.format("Rs. %s",paymentPerModel.getAmount()));

        holder.binding.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mContext.startActivity(new Intent(mContext, OrderDetailActi.class)
                       .putExtra("date",paymentPerModel.getOrder_date())
                       .putExtra("activity",activty)
               );
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TodaysPayLayoutBinding binding;

        public MyViewHolder(final TodaysPayLayoutBinding itemBinding) {
            super(itemBinding.getRoot());
            this.binding = itemBinding;

            if (((PerviousPayment)mContext).getIntent().getExtras()!=null) {
                activty = ((PerviousPayment)mContext).getIntent().getExtras().getString("act", "");
            }
        }
    }
}
