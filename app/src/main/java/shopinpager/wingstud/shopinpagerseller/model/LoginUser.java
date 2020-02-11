package shopinpager.wingstud.shopinpagerseller.model;

public class LoginUser {

    private String mobileNumber;
    private String strPassword;

    public LoginUser(String MobileNumber, String Password) {
        mobileNumber = MobileNumber;
        strPassword = Password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public boolean isMobileValid() {
        return getMobileNumber().length() != 10 ;
    }


    public boolean isPasswordLengthGreaterThan5() {
        return getStrPassword().length() > 5;
    }
}
