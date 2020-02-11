package shopinpager.wingstud.shopinpagerseller.activity;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.databinding.ActivityDashboardBinding;
import shopinpager.wingstud.shopinpagerseller.fragment.Inventory;
import shopinpager.wingstud.shopinpagerseller.fragment.Notice;
import shopinpager.wingstud.shopinpagerseller.fragment.HomeFrag;
import shopinpager.wingstud.shopinpagerseller.fragment.NavMenuFrag;
import shopinpager.wingstud.shopinpagerseller.fragment.ProductUploadList;
import shopinpager.wingstud.shopinpagerseller.fragment.NewOrder;
import shopinpager.wingstud.shopinpagerseller.fragment.Payment;
import shopinpager.wingstud.shopinpagerseller.fragment.SchemeList;
import shopinpager.wingstud.shopinpagerseller.util.Constants;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class Dashboard extends AppCompatActivity implements NavMenuFrag.FragmentDrawerListener {

    Toolbar toolbar;

    private Context mContext;

    boolean doubleBackToExitPressedOnce = false;

    private ActivityDashboardBinding binding;

    private NavMenuFrag navMenuFrag;

    private Fragment fragment = new HomeFrag();
    private android.app.AlertDialog dialog;

    public static Dashboard mInstance;
    int countItem = 0;
    View badge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);

        mInstance = this;
        mContext = Dashboard.this;
        initialize();
    }

    private void initialize() {
        setSupportActionBar(binding.toolbar.toolbar);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        }
        navMenuFrag = (NavMenuFrag) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        navMenuFrag.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), binding.toolbar.toolbar);
        navMenuFrag.setDrawerListener(this);


        binding.toolbar.imvNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.startActivity(mContext, NotificationActi.class);
            }
        });

        displayView(getResources().getString(R.string.dashboard));
        if(fragment == null)
            fragment = new HomeFrag();
        loadHomeFragment();
    }

    public void setTitle(String title, boolean isShowSwitch){
        if (isShowSwitch){
            binding.toolbar.activityTitle.setVisibility(View.GONE);
            binding.toolbar.switchActive.setVisibility(View.VISIBLE);
        } else {
            binding.toolbar.activityTitle.setText(title);
            binding.toolbar.activityTitle.setVisibility(View.VISIBLE);
            binding.toolbar.switchActive.setVisibility(View.GONE);
        }
    }

    private void loadHomeFragment() {
        if(fragment!=null) {
//            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//
//            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
//                    android.R.anim.fade_out);
////            fragmentTransaction.replace(R.id.fmContainer, fragment, CURRENT_TAG);
//            fragmentTransaction.replace(R.id.fmContainer, fragment).addToBackStack(null).commit();
////            fragmentTransaction.commitAllowingStateLoss();

            Utils.replaceFrg(this, fragment, true, Constants.DEFAULT_ID);
        }else{
            Toast.makeText(this, "FRAGMNT", Toast.LENGTH_SHORT).show();
        }
    }

    public static Dashboard getInstance(){
        return mInstance;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setCountItem(countItem);
    }

    public void setCountItem(int countItem){
        if ( binding.toolbar.bellItemNo==null)return;
//        if (replyCode.equals(replyMessage)){
            if (countItem == 0){
                binding.toolbar.bellItemNo.setVisibility(View.GONE);
            }else {
                binding.toolbar.bellItemNo.setVisibility(View.VISIBLE);
                binding.toolbar.bellItemNo.setText(countItem+"");
            }
//        }
    }
    @Override
    public void onDrawerItemSelected(View view, String menuNmae) {
        displayView(menuNmae);
    }

    private void displayView(String menuNmae) {
        fragment = null;
        Inventory.ins = false;
        Inventory.outs = false;
        Fragment fragcheck = getSupportFragmentManager().findFragmentById(R.id.fmContainer);
        if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.home))) {
            fragment = new HomeFrag();
            if (!(fragcheck instanceof HomeFrag)) {
                loadHomeFragment();
            }

        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.inventory))) {
            fragment = new Inventory();
            if (!(fragcheck instanceof Inventory)) {
                loadHomeFragment();
            }
        }else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.product_upload))){
            fragment = new ProductUploadList();
//            if (!(fragcheck instanceof ProductUploadList)) {
                loadHomeFragment();
//            }
        }else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.duplicate_product))){
//            fragment = new DuplicateProduct();
//            if (!(fragcheck instanceof DuplicateProduct)) {
                ProductUploadList fragment = new ProductUploadList();
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                bundle.putString("from","duplicateProduct");
                fragment.setArguments(bundle);
                fm.beginTransaction().replace(R.id.fmContainer,fragment).addToBackStack(null).commit();
//            }
        }else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.scheme))){
//            fragment = new DuplicateProduct();
//            if (!(fragcheck instanceof DuplicateProduct)) {
            SchemeList fragment = new SchemeList();
            FragmentManager fm = getSupportFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("from","scheme");
            fragment.setArguments(bundle);
            fm.beginTransaction().replace(R.id.fmContainer,fragment).addToBackStack(null).commit();
//            }
        }else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.payment))){
           fragment = new Payment();
            if (!(fragcheck instanceof Payment)) {
                loadHomeFragment();
            }
        }else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.order))){
           fragment = new NewOrder();
            if (!(fragcheck instanceof NewOrder)) {
                loadHomeFragment();
            }
        }else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.notice_board))){
            fragment = new Notice();
            if (!(fragcheck instanceof Notice)) {
                loadHomeFragment();
            }
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.logout))) {

            dialog = Utils.retryAlertDialog(this, getResources().getString(R.string.app_name), getResources().getString(R.string.Are_you_sure_to_logout), getResources().getString(R.string.Cancel), getResources().getString(R.string.Yes), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPrefManager.setLogin(Constrants.IsLogin,false);
                    Utils.logout(Dashboard.this);
                    dialog.dismiss();
                }
            });
        }
//        if (fragment != null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.fmContainer, fragment).addToBackStack(null).commit();
//        }

    }

    @Override
    public void onBackPressed() {
        backCountToExit();
    }

    private void backCountToExit() {

        FragmentManager fm = this.getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 1 ){
            fm.popBackStack();
        } else {

            if (doubleBackToExitPressedOnce) {
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.Please_BACK_again_to_exit), Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

        }
    }
}
