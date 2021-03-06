package ubibots.zuccweatherbase.display.control;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ubibots.zuccweatherbase.display.model.BeanConstant;
import ubibots.zuccweatherbase.display.model.BeanTabMessage;
import ubibots.zuccweatherbase.display.ui.HourView;
import ubibots.zuccweatherbase.display.util.RequestUtil;

public class RequestHourStep extends AsyncTask<String, Integer, String> {

    public final static int MAX = 120;
    private BeanTabMessage hour;
    private String strURL;
    private int time;

    public RequestHourStep(BeanTabMessage hour, int time) {
        this.hour = hour;
        this.time = time;
    }

    //该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
    @Override
    protected String doInBackground(String... params) {
        //System.out.println("Url: " + params[0]);
        URL url;
        try {
            url = new URL(params[0]);
            strURL = params[0];
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setDoInput(true); //允许输入流，即允许下载
            urlConn.setDoOutput(true); //允许输出流，即允许上传
            urlConn.setUseCaches(false); //不使用缓冲
            urlConn.setRequestMethod("POST"); //使用get请求
            InputStreamReader in = new InputStreamReader(urlConn.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(in);
            String result = "";
            String readLine;
            while ((readLine = bufferedReader.readLine()) != null) {
                result += readLine;
            }
            in.close();
            urlConn.disconnect();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
    @Override
    protected void onPostExecute(String result) {
        if (result != null && time < BeanConstant.MAXTIME) {
            Pattern pattern = Pattern.compile("<TD>(.*?)</TD>");
            Matcher matcher = pattern.matcher(result);
            ArrayList<String> tmp = new ArrayList<>();
            while (matcher.find()) {
                tmp.add(matcher.group(1));
            }
            if (tmp.size() >= 3) {
                String dateString = tmp.get(0);
                double temp = Double.valueOf(tmp.get(1));
                double humi = Double.valueOf(tmp.get(2));

                //丢包重发
                if (dateString.length() != 24 || temp < 0 || humi < 0) {
                    reconnect(strURL, hour);
                    return;
                }

                dateString = dateString.substring(0, 10) + " " + dateString.substring(11, 23);
                Calendar calendar = RequestUtil.dateToCalender(dateString,"yyyy-MM-dd HH:mm:ss.SSS");
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 8);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                dateString = sdf.format(calendar.getTime());

                hour.getDate().remove(0);
                hour.getDate().add(dateString);
                hour.getTemperature().remove(0);
                hour.getTemperature().add(temp);
                hour.getHumidity().remove(0);
                hour.getHumidity().add(humi);

                //刷新界面
                RequestUtil.reflashLineView(HourView.getHourBeanLineView(), hour, "时:分:秒");

                System.out.println("Time: " + hour.getDate().get(MAX - 1) + " " + "Temperature: " + hour.getTemperature().get(MAX - 1) + " " + "Humidity: " + hour.getHumidity().get(MAX - 1) + " " + "Time: " + time);
            } else {//丢包重发
                reconnect(strURL, hour);
            }
        } else {
            RequestUtil.connectFailed();
        }
    }

    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
    @Override
    protected void onPreExecute() {
    }

    public void reconnect(String strURL, BeanTabMessage hour) {
        RequestHourStep another = new RequestHourStep(hour, time + 1);
        System.out.println("time: " + time);
        System.out.println(strURL);
        another.execute(strURL);
    }
}
