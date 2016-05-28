package ubibots.zuccweatherbase.registandlogin.control;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ubibots.zuccweatherbase.displayhistory.DisplayHistoryActivity;
import ubibots.zuccweatherbase.registandlogin.RegistAndLoginActivity;
import ubibots.zuccweatherbase.registandlogin.intrfc.IUserManager;
import ubibots.zuccweatherbase.registandlogin.ui.FrmLogin;
import ubibots.zuccweatherbase.registandlogin.util.DBUtil;
import ubibots.zuccweatherbase.registandlogin.util.RegistAndLoginUtil;

public class UserManager implements IUserManager {

    @Override
    public String login(String userName, String userPassword) {
        String ret = null;
        try {
            ret = new ExecuteLogin().execute(userName, userPassword).get();
            if(ret.equals("��ӭʹ��")) {
                if (RegistAndLoginActivity.getRegistAndLoginActivity() != null) {
                /* �½�һ��Intent���� */
                    Intent intent = new Intent();
                /* ָ��intentҪ�������� */
                    intent.setClass(RegistAndLoginActivity.getRegistAndLoginActivity(), DisplayHistoryActivity.class);
                /* ����һ���µ�Activity */
                    RegistAndLoginActivity.getRegistAndLoginActivity().startActivity(intent);
                /* �رյ�ǰ��Activity */
                    RegistAndLoginActivity.getRegistAndLoginActivity().finish();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public class ExecuteLogin extends AsyncTask<String, Integer, String> {
        //�÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
        String testResult = null;

        @Override
        protected String doInBackground(String... params) {
            Connection conn = null;
            String ret = "";
            try {
                if ("".equals(params[0])) {
                    ret = "�û�������Ϊ��";
                    return ret;
                }

                conn = DBUtil.getConnection();
                String sql = "select userpassword from beanuser where username " +
                        "= ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, params[0]);
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    ret = "�û���������";
                    return ret;
                }

                if ("".equals(params[1])) {
                    ret = "���벻��Ϊ��";
                    return ret;
                }

                if (!RegistAndLoginUtil.MD5Encrypt(params[1]).equals(rs.getString(1))) {
                    ret = "���벻��ȷ";
                    return ret;
                }
                ret = "��ӭʹ��";
            } catch (SQLException e) {
                e.printStackTrace();
                ret = "���������ж�";
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ret;
        }

        //��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
        @Override
        protected void onPostExecute(String result) {
            testResult = result;
            if (RegistAndLoginActivity.getContext() != null) {
                Toast.makeText(RegistAndLoginActivity.getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public String regist(String userName, String userMail, String userPassword, String userPassword2) {
        String ret = null;
        try {
            ret = new ExecuteRegist().execute(userName, userMail, userPassword, userPassword2).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public class ExecuteRegist extends AsyncTask<String, Integer, String> {
        //�÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
        @Override
        protected String doInBackground(String... params) {
            String ret = "";
            Connection conn = null;
            try {
                if ("".equals(params[0])) {
                    ret = "�û�������Ϊ��";
                    return ret;
                }

                conn = DBUtil.getConnection();
                String sql = "select * from beanuser where username = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, params[0]);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    ret = "�û����Ѵ���";
                    return ret;
                }
                conn.close();

                if ("".equals(params[1])) {
                    ret = "���䲻��Ϊ��";
                    return ret;
                }

                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(params[1]);
                if (!matcher.matches()) {
                    ret = "�����ʽ����";
                    return ret;
                }

                conn = DBUtil.getConnection();
                sql = "select * from beanuser where usermail = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, params[1]);
                rs = pst.executeQuery();
                if (rs.next()) {
                    ret = "�����Ѵ���";
                    return ret;
                }
                conn.close();

                if ("".equals(params[2])) {
                    ret = "���벻��Ϊ��";
                } else if ("".equals(params[3])) {
                    ret = "ȷ�����벻��Ϊ��";
                } else if (!params[2].equals(params[3])) {
                    ret = "�����ȷ�����벻һ��";
                }
                if (!"".equals(ret)) {
                    return ret;
                }

                conn = DBUtil.getConnection();
                sql = "insert into beanuser(username, usermail, userpassword) values(?,?,?)";
                pst = conn.prepareStatement(sql);
                pst.setString(1, params[0]);
                pst.setString(2, params[1]);
                pst.setString(3, RegistAndLoginUtil.MD5Encrypt(params[2]));
                pst.executeUpdate();
                ret = "��ӭʹ��";
            } catch (SQLException e) {
                e.printStackTrace();
                ret = "���������ж�";
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ret;
        }

        //��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("��ӭʹ��")) {
                if (FrmLogin.frmRegist != null) {
                    FrmLogin.frmRegist.setTmpUserName("");
                    FrmLogin.frmRegist.setTmpUserMail("");
                }
            }
            if (FrmLogin.frmRegist != null) {
                FrmLogin.frmRegist.setTmpUserPassword("");
                FrmLogin.frmRegist.setTmpUserPassword2("");
            }
            if (RegistAndLoginActivity.getContext() != null) {
                Toast.makeText(RegistAndLoginActivity.getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public String remove(String userName) {
        String ret = null;
        try {
            ret = new ExecuteRemove().execute(userName).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public class ExecuteRemove extends AsyncTask<String, Integer, String> {
        //�÷�������������UI�̵߳��У���Ҫ�����첽�����������ڸ÷����в��ܶ�UI���еĿռ�������ú��޸�
        @Override
        protected String doInBackground(String... params) {
            String ret = "";
            if ("".equals(params[0])) {
                ret = "�û�������Ϊ��";
                return ret;
            }
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                String sql = "select * from beanuser where username = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, params[0]);
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    ret = "�û���������";
                    return ret;
                }
                conn.close();
                conn = DBUtil.getConnection();
                sql = "delete from beanuser where username = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, params[0]);
                pst.executeUpdate();
                ret = "ɾ���ɹ�";
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            return ret;
        }

        //��doInBackground����ִ�н���֮�������У�����������UI�̵߳��� ���Զ�UI�ռ��������
        @Override
        protected void onPostExecute(String result) {
        }
    }
}
