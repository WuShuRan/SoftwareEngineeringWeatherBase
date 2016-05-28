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
            if(ret.equals("欢迎使用")) {
                if (RegistAndLoginActivity.getRegistAndLoginActivity() != null) {
                /* 新建一个Intent对象 */
                    Intent intent = new Intent();
                /* 指定intent要启动的类 */
                    intent.setClass(RegistAndLoginActivity.getRegistAndLoginActivity(), DisplayHistoryActivity.class);
                /* 启动一个新的Activity */
                    RegistAndLoginActivity.getRegistAndLoginActivity().startActivity(intent);
                /* 关闭当前的Activity */
                    RegistAndLoginActivity.getRegistAndLoginActivity().finish();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public class ExecuteLogin extends AsyncTask<String, Integer, String> {
        //该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
        String testResult = null;

        @Override
        protected String doInBackground(String... params) {
            Connection conn = null;
            String ret = "";
            try {
                if ("".equals(params[0])) {
                    ret = "用户名不能为空";
                    return ret;
                }

                conn = DBUtil.getConnection();
                String sql = "select userpassword from beanuser where username " +
                        "= ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, params[0]);
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    ret = "用户名不存在";
                    return ret;
                }

                if ("".equals(params[1])) {
                    ret = "密码不能为空";
                    return ret;
                }

                if (!RegistAndLoginUtil.MD5Encrypt(params[1]).equals(rs.getString(1))) {
                    ret = "密码不正确";
                    return ret;
                }
                ret = "欢迎使用";
            } catch (SQLException e) {
                e.printStackTrace();
                ret = "网络连接中断";
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

        //在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
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
        //该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
        @Override
        protected String doInBackground(String... params) {
            String ret = "";
            Connection conn = null;
            try {
                if ("".equals(params[0])) {
                    ret = "用户名不能为空";
                    return ret;
                }

                conn = DBUtil.getConnection();
                String sql = "select * from beanuser where username = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, params[0]);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    ret = "用户名已存在";
                    return ret;
                }
                conn.close();

                if ("".equals(params[1])) {
                    ret = "邮箱不能为空";
                    return ret;
                }

                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(params[1]);
                if (!matcher.matches()) {
                    ret = "邮箱格式不对";
                    return ret;
                }

                conn = DBUtil.getConnection();
                sql = "select * from beanuser where usermail = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, params[1]);
                rs = pst.executeQuery();
                if (rs.next()) {
                    ret = "邮箱已存在";
                    return ret;
                }
                conn.close();

                if ("".equals(params[2])) {
                    ret = "密码不能为空";
                } else if ("".equals(params[3])) {
                    ret = "确认密码不能为空";
                } else if (!params[2].equals(params[3])) {
                    ret = "密码和确认密码不一致";
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
                ret = "欢迎使用";
            } catch (SQLException e) {
                e.printStackTrace();
                ret = "网络连接中断";
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

        //在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPostExecute(String result) {
            if (result.equals("欢迎使用")) {
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
        //该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
        @Override
        protected String doInBackground(String... params) {
            String ret = "";
            if ("".equals(params[0])) {
                ret = "用户名不能为空";
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
                    ret = "用户名不存在";
                    return ret;
                }
                conn.close();
                conn = DBUtil.getConnection();
                sql = "delete from beanuser where username = ?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, params[0]);
                pst.executeUpdate();
                ret = "删除成功";
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

        //在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
        @Override
        protected void onPostExecute(String result) {
        }
    }
}
