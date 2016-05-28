package ubibots.zuccweatherbase;

import android.app.Application;
import android.test.ApplicationTestCase;

import junit.framework.Assert;

import ubibots.zuccweatherbase.registandlogin.control.UserManager;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testUserManager(){
        UserManager userManager = new UserManager();

        //注册
        Assert.assertEquals("欢迎使用".equals(userManager.regist("admin","510022482@qq.com","123456","123456")),true);
        userManager.remove("admin");

        Assert.assertEquals("密码和确认密码不一致".equals(userManager.regist("admin1","5100224821@qq.com","123456","123415")),true);

        Assert.assertEquals("用户名不能为空".equals(userManager.regist("","5100224822@qq.com","123456","123456")),true);

        Assert.assertEquals("邮箱不能为空".equals(userManager.regist("admin2","","123456","123456")),true);

        Assert.assertEquals("密码不能为空".equals(userManager.regist("admin3","5100224823@qq.com","","123456")),true);

        Assert.assertEquals("确认密码不能为空".equals(userManager.regist("admin4","5100224823@qq.com","123456","")),true);

        Assert.assertEquals("邮箱格式不对".equals(userManager.regist("admin5","5100224824qq.com","123456","123456")),true);

        userManager.regist("admin","510022482@qq.com","123456","123456");
        Assert.assertEquals("用户名已存在".equals(userManager.regist("admin","5100224825@qq.com","123456","123456")),true);
        Assert.assertEquals("邮箱已存在".equals(userManager.regist("admin6","510022482@qq.com","123456","123456")),true);
        userManager.remove("admin");

        //登录
        userManager.regist("admin","510022482@qq.com","zucc","zucc");

        Assert.assertEquals("用户名不能为空".equals(userManager.login("","12345")),true);

        Assert.assertEquals("密码不能为空".equals(userManager.login("admin","")),true);

        Assert.assertEquals("用户名不存在".equals(userManager.login("a2333","12345")),true);

        Assert.assertEquals("密码不正确",userManager.login("admin","12345"));

        Assert.assertEquals("欢迎使用".equals(userManager.login("admin","zucc")),true);

        userManager.remove("admin");
    }
}