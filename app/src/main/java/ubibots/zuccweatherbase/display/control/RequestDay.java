package ubibots.zuccweatherbase.display.control;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import ubibots.zuccweatherbase.display.DisplayHistoryActivity;
import ubibots.zuccweatherbase.display.model.BeanConstant;
import ubibots.zuccweatherbase.display.model.BeanTabMessage;
import ubibots.zuccweatherbase.display.ui.DayView;
import ubibots.zuccweatherbase.display.util.RequestUtil;

public class RequestDay {

    private static RequestDayHandler requestDayHandler;

    public RequestDay(){
        requestDayHandler = new RequestDayHandler();
    }

    public void executeRequest(){
        DayView.setDay(new BeanTabMessage(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(Calendar.SECOND, dayCalendar.get(Calendar.SECOND) - BeanConstant.delayDay / 1000 * (RequestDayHistory.MAX - 1));
        for (int i = 0; i < RequestDayHistory.MAX; i++) {
            DayView.getDay().getTemperature().add(0.0);
            DayView.getDay().getHumidity().add(0.0);
            DayView.getDay().getDate().add("");
            dayHistory(DayView.getDay(), dayCalendar, i);
            dayCalendar.set(Calendar.SECOND, dayCalendar.get(Calendar.SECOND) + BeanConstant.delayDay / 1000);
        }

        Toast.makeText(DisplayHistoryActivity.getContext(), "正在获取数据中,请耐心等待...",
                Toast.LENGTH_LONG).show();
    }

    public static void dayHistory(BeanTabMessage day, Calendar calendar, int id) {
        String strUrl = RequestUtil.combineUrl((Calendar) calendar.clone());
        RequestDayHistory request = new RequestDayHistory(day, id, 0);
        request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strUrl);
    }

    private static Timer requestDayTimer = new Timer();
    private static TimerTask requestDayTask = new TimerTask() {

        @Override
        public void run() {
            // 需要做的事:发送消息
            Message message = new Message();
            message.what = 1;
            requestDayHandler.sendMessage(message);
        }
    };

    public static Timer getRequestDayTimer() {
        return requestDayTimer;
    }

    public static TimerTask getRequestDayTask() {
        return requestDayTask;
    }

    static class RequestDayHandler extends Handler {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Calendar calendar = Calendar.getInstance();
                dayStep(DayView.getDay(),calendar);
            }
            super.handleMessage(msg);
        }
    }

    public static void dayStep(BeanTabMessage day, Calendar calendar) {
        String strUrl = RequestUtil.combineUrl((Calendar) calendar.clone());
        RequestDayStep request = new RequestDayStep(day, 0);
        request.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, strUrl);
    }
}
