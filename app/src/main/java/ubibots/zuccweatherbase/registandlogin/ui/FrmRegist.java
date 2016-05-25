package ubibots.zuccweatherbase.registandlogin.ui;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import ubibots.zuccweatherbase.R;
import ubibots.zuccweatherbase.registandlogin.RegistAndLoginActivity;
import ubibots.zuccweatherbase.registandlogin.control.UserManager;

public class FrmRegist {
    private String tmpUserName;
    private String tmpUserMail;
    private String tmpUserPassword;
    private String tmpUserPassword2;

    public void setTmpUserName(String tmpUserName) {
        this.tmpUserName = tmpUserName;
    }

    public void setTmpUserMail(String tmpUserMail) {
        this.tmpUserMail = tmpUserMail;
    }

    public void setTmpUserPassword(String tmpUserPassword) {
        this.tmpUserPassword = tmpUserPassword;
    }

    public void setTmpUserPassword2(String tmpUserPassword2) {
        this.tmpUserPassword2 = tmpUserPassword2;
    }

    public FrmRegist(){
        tmpUserName = "";
        tmpUserMail = "";
        tmpUserPassword = "";
        tmpUserPassword2 = "";
    }

    public void registDialog() {
        LayoutInflater inflater = RegistAndLoginActivity.getRegistAndLoginActivity().getLayoutInflater();
        final View layout = inflater.inflate(R.layout.regist_layout,
                (ViewGroup) RegistAndLoginActivity.getRegistAndLoginActivity().findViewById(R.id.registdialog));
        EditText userName = (EditText) layout.findViewById(R.id.edtusername);
        EditText userMail = (EditText) layout.findViewById(R.id.edtusermail);
        EditText userPassword = (EditText) layout.findViewById(R.id.edtuserpassword);
        EditText userPassword2 = (EditText) layout.findViewById(R.id.edtuserpassword2);
        if (!"".equals(tmpUserName)) {
            userName.setText(tmpUserName);
        }
        if (!"".equals(tmpUserMail)) {
            userMail.setText(tmpUserMail);
        }
        if (!"".equals(tmpUserPassword)) {
            userPassword.setText(tmpUserPassword);
        }
        if (!"".equals(tmpUserPassword2)) {
            userPassword2.setText(tmpUserPassword2);
        }
        new AlertDialog.Builder(RegistAndLoginActivity.getContext()).setTitle("Regist").setView(layout)
                .setPositiveButton("确定", (dialog, which) -> {
                    tmpUserName = userName.getText().toString();
                    tmpUserMail = userMail.getText().toString();
                    tmpUserPassword = userPassword.getText().toString();
                    tmpUserPassword2 = userPassword2.getText().toString();
                    new UserManager().regist(tmpUserName, tmpUserMail, tmpUserPassword, tmpUserPassword2);
                }).setNegativeButton("取消", null).show()
                .setCanceledOnTouchOutside(false);
    }
}
