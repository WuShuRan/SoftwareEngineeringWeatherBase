package ubibots.zuccweatherbase.display;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import ubibots.zuccweatherbase.R;
import ubibots.zuccweatherbase.display.ui.DisplayView;

public class DisplayHistoryActivity extends Activity {

    private static Context context;
    private static DisplayHistoryActivity activity;

    public static Context getContext() {
        return context;
    }

    public static DisplayHistoryActivity getActivity() {
        return activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_main);
        context = this;
        activity = this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        new DisplayView();
    }
}