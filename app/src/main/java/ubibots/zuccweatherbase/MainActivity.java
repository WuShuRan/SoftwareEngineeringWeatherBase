package ubibots.zuccweatherbase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import ubibots.zuccweatherbase.registandlogin.RegistAndLoginActivity;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 新建一个Intent对象 */
        Intent intent = new Intent();
        /* 指定intent要启动的类 */
        intent.setClass(MainActivity.this, RegistAndLoginActivity.class);
        /* 启动一个新的Activity */
        MainActivity.this.startActivity(intent);
        /* 关闭当前的Activity */
        MainActivity.this.finish();
    }
}
