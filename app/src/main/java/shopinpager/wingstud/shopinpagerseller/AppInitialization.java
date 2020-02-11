package shopinpager.wingstud.shopinpagerseller;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.Log;

import shopinpager.wingstud.shopinpagerseller.Common.SharedPrefManager;


public class AppInitialization extends Application {
    private static Typeface fontRegular;
    private static Typeface fontBold;

    private String assetFolderName = "fonts/";


    @Override
    public void onCreate() {
        super.onCreate();

        fontRegular = Typeface.createFromAsset(getAssets(), assetFolderName + "lato_regular.ttf");
        fontBold = Typeface.createFromAsset(getAssets(), assetFolderName + "lato_bold.ttf");
        try {
            PackageInfo packageinfo = null;
            packageinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = packageinfo.versionName.toString();
            SharedPrefManager.setBuildVersion(this, version);
            Log.d("versionCode", version);

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Typeface getFontRegular() {
        return fontRegular;
    }

    public static Typeface getFontBold() {
        return fontBold;
    }

}
