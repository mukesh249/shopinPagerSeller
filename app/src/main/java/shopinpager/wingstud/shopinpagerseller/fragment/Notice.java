package shopinpager.wingstud.shopinpagerseller.fragment;


import android.content.Context;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;

import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.Dashboard;
import shopinpager.wingstud.shopinpagerseller.adapter.NoticeAdapter;
import shopinpager.wingstud.shopinpagerseller.model.NoticeModel;
import shopinpager.wingstud.shopinpagerseller.databinding.NoticeFragmentBinding;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notice extends Fragment implements WebCompleteTask {

    private View view;
    private Context mContext;
    private NoticeFragmentBinding binding;
    private AbstractList<NoticeModel> orderList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    NoticeAdapter mAdapter;
    ArrayList<NoticeModel> list = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.notice_fragment, container, false);
        view = binding.getRoot();

        initialize();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((Dashboard) getContext()).setTitle(mContext.getString(R.string.notice), false);
    }

    private void initialize() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        binding.rvOrders.setLayoutManager(mLayoutManager);
        binding.rvOrders.setItemAnimator(new DefaultItemAnimator());

        mAdapter= new NoticeAdapter(mContext, list);
        binding.rvOrders.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        Notice();
    }

    public void Notice(){
        HashMap objectNew = new HashMap();
//        objectNew.put("user_id", "40");

        new WebTask(getActivity(), WebUrls.BASE_URL+ WebUrls.Notice,objectNew, Notice.this, RequestCode.CODE_Notice,1);

    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_Notice == taskcode){
            System.out.println("Notice_res: "+response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("status_code")==1){
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    list.clear();
                    if (dataArray.length()>0){
                        binding.relEmptyWL.setVisibility(View.GONE);
                        binding.rvOrders.setVisibility(View.VISIBLE);
                        for (int i=0;i<dataArray.length();i++){
                            JSONObject dataObj = dataArray.getJSONObject(i);
                            NoticeModel todayPayBean = new NoticeModel();
                            todayPayBean.setId(dataObj.optString("id"));
                            todayPayBean.setHeading(dataObj.optString("heading"));
                            todayPayBean.setDescription(dataObj.optString("description"));
                            todayPayBean.setCreated_at(dataObj.optString("created_at"));
                            list.add(todayPayBean);
                        }
                        mAdapter.notifyDataSetChanged();
                    }else {
                        binding.relEmptyWL.setVisibility(View.VISIBLE);
                        binding.rvOrders.setVisibility(View.GONE);
                    }
                }else {
                    Utils.Tosat(getActivity(),jsonObject.optString("error_message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


}
