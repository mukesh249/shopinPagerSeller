package shopinpager.wingstud.shopinpagerseller.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import shopinpager.wingstud.shopinpagerseller.Api.JsonDeserializer;
import shopinpager.wingstud.shopinpagerseller.Api.RequestCode;
import shopinpager.wingstud.shopinpagerseller.Api.WebCompleteTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebTask;
import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.activity.Dashboard;
import shopinpager.wingstud.shopinpagerseller.adapter.SchemeListAdapter;
import shopinpager.wingstud.shopinpagerseller.databinding.FragmentSchemeListBinding;
import shopinpager.wingstud.shopinpagerseller.model.SchemeListModel;
import shopinpager.wingstud.shopinpagerseller.shacme.Shecme;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchemeList extends Fragment implements WebCompleteTask {


    public SchemeList() {
        // Required empty public constructor
    }

    private SchemeListAdapter schemeListAdapter;
    private FragmentSchemeListBinding binding;
    List<SchemeListModel.Datum> datumList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_scheme_list, container, false);
        ((Dashboard) getContext()).setTitle("Scheme List", false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        schemeListAdapter = new SchemeListAdapter(datumList,getActivity());
        binding.recyclerView.setAdapter(schemeListAdapter);

        binding.addFAB.setOnClickListener(v -> startActivity(new Intent(getActivity(), Shecme.class)));
        GetSchemeProductList();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        GetSchemeProductList();
    }

    public void GetSchemeProductList() {
        HashMap objectNew = new HashMap();
        objectNew.put("user_id", SharedPrefManager.getUserID(Constrants.UserId));
        Log.i("GetSchemeList_obj",objectNew+"");
        new WebTask(getActivity(), WebUrls.BASE_URL + WebUrls.GetSchemeProductList, objectNew,
                SchemeList.this, RequestCode.CODE_GetSchemeProductList, 1);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (RequestCode.CODE_GetSchemeProductList==taskcode){

            Log.i("GetSchemeList_res",response);
            SchemeListModel schemeListModel = JsonDeserializer.deserializeJson(response,SchemeListModel.class);
            if (!schemeListModel.data.isEmpty()){
                binding.recyclerView.setVisibility(View.VISIBLE);
                binding.relEmptyWL.setVisibility(View.GONE);
                datumList.addAll(schemeListModel.data);
            }else {
                binding.recyclerView.setVisibility(View.GONE);
                binding.relEmptyWL.setVisibility(View.VISIBLE);
            }
            schemeListAdapter.notifyDataSetChanged();
        }
    }
}
