package ubibots.zuccweatherbase.registandlogin.ui;

import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import ubibots.zuccweatherbase.R;
import ubibots.zuccweatherbase.registandlogin.RegistAndLoginActivity;
import ubibots.zuccweatherbase.registandlogin.util.RegistAndLoginUtil;

public class FrmLogin {

    private EditText userName;
    private EditText userPassword;
    public static FrmRegist frmRegist;

    public FrmLogin() {
        userName = (EditText) RegistAndLoginActivity.getRegistAndLoginActivity().findViewById(R.id.edtusername);
        userPassword = (EditText) RegistAndLoginActivity.getRegistAndLoginActivity().findViewById(R.id.edtuserpassword);
        Button registButton = (Button) RegistAndLoginActivity.getRegistAndLoginActivity().findViewById(R.id.regist);
        Button loginButton = (Button) RegistAndLoginActivity.getRegistAndLoginActivity().findViewById(R.id.login);

        loginButton.setOnClickListener(v -> {
            RegistAndLoginUtil.userManager.login(userName.getText().toString(), userPassword.getText().toString());
            userPassword.setText("");
        });

        frmRegist = new FrmRegist();
        registButton.setOnClickListener(v -> frmRegist.registDialog());

        ImageView appLogo = (ImageView) (RegistAndLoginActivity.getRegistAndLoginActivity().findViewById(R.id.applogo));
        appLogo.setImageDrawable(ContextCompat.getDrawable(RegistAndLoginActivity.getRegistAndLoginActivity(),R.drawable.logo));
    }
}
