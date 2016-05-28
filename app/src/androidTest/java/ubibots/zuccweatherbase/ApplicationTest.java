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

        //ע��
        Assert.assertEquals("��ӭʹ��".equals(userManager.regist("admin","510022482@qq.com","123456","123456")),true);
        userManager.remove("admin");

        Assert.assertEquals("�����ȷ�����벻һ��".equals(userManager.regist("admin1","5100224821@qq.com","123456","123415")),true);

        Assert.assertEquals("�û�������Ϊ��".equals(userManager.regist("","5100224822@qq.com","123456","123456")),true);

        Assert.assertEquals("���䲻��Ϊ��".equals(userManager.regist("admin2","","123456","123456")),true);

        Assert.assertEquals("���벻��Ϊ��".equals(userManager.regist("admin3","5100224823@qq.com","","123456")),true);

        Assert.assertEquals("ȷ�����벻��Ϊ��".equals(userManager.regist("admin4","5100224823@qq.com","123456","")),true);

        Assert.assertEquals("�����ʽ����".equals(userManager.regist("admin5","5100224824qq.com","123456","123456")),true);

        userManager.regist("admin","510022482@qq.com","123456","123456");
        Assert.assertEquals("�û����Ѵ���".equals(userManager.regist("admin","5100224825@qq.com","123456","123456")),true);
        Assert.assertEquals("�����Ѵ���".equals(userManager.regist("admin6","510022482@qq.com","123456","123456")),true);
        userManager.remove("admin");

        //��¼
        userManager.regist("admin","510022482@qq.com","zucc","zucc");

        Assert.assertEquals("�û�������Ϊ��".equals(userManager.login("","12345")),true);

        Assert.assertEquals("���벻��Ϊ��".equals(userManager.login("admin","")),true);

        Assert.assertEquals("�û���������".equals(userManager.login("a2333","12345")),true);

        Assert.assertEquals("���벻��ȷ",userManager.login("admin","12345"));

        Assert.assertEquals("��ӭʹ��".equals(userManager.login("admin","zucc")),true);

        userManager.remove("admin");
    }
}