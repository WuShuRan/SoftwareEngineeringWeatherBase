package ubibots.zuccweatherbase.registandlogin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import ubibots.zuccweatherbase.R;
import ubibots.zuccweatherbase.registandlogin.ui.FrmLogin;

public class RegistAndLoginActivity extends Activity {
    private static RegistAndLoginActivity registAndLoginActivity;
    private static Context context;

    public static RegistAndLoginActivity getRegistAndLoginActivity() {
        return registAndLoginActivity;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        registAndLoginActivity = this;
        context = this;

        new FrmLogin();
    }
}
