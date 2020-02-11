package shopinpager.wingstud.shopinpagerseller.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import shopinpager.wingstud.shopinpagerseller.Api.WebUrls;
import shopinpager.wingstud.shopinpagerseller.Common.Constrants;
import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;
import shopinpager.wingstud.shopinpagerseller.R;
import shopinpager.wingstud.shopinpagerseller.util.Utils;

public class SplashActi extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPrefManager.getInstance(SplashActi.this);
        SharedPrefManager.setImagePath(Constrants.ImagePath, WebUrls.BASE_URL+"public/admin/uploads/product");
        thread();

    }

    private void thread() {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                Utils.setDeviceSreenH(SplashActi.this);
                if (SharedPrefManager.isLogin(SplashActi.this)) {

                    if (SharedPrefManager.isLogin(SplashActi.this)) {

                        Utils.startActivityWithFinish(SplashActi.this, Dashboard.class);
                    }
//
                } else {
                    Utils.startActivityWithFinish(SplashActi.this, LoginActi.class);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
