package shopinpager.wingstud.shopinpagerseller.model;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.view.View;


public class LoginViewModel extends ViewModel {

    public MutableLiveData<String> MobileNo = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<LoginUser> userMutableLiveData;

    public MutableLiveData<LoginUser> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }

    public void onClick(View view) {
        LoginUser loginUser = new LoginUser(MobileNo.getValue(), Password.getValue());
        userMutableLiveData.setValue(loginUser);

    }
}
