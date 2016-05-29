package ubibots.zuccweatherbase.display.control;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ubibots.zuccweatherbase.display.DisplayHistoryActivity;
import ubibots.zuccweatherbase.display.intrfc.IUpdateRecommand;
import ubibots.zuccweatherbase.display.ui.HourView;
import ubibots.zuccweatherbase.display.ui.RecommandView;
import ubibots.zuccweatherbase.registandlogin.util.DBUtil;

public class UpdateRecommand implements IUpdateRecommand{

    @Override
    public void compareData() {
        try {
            float currentTemperature = HourView.getHour().getTemperature().get(HourView.getHour().getTemperature().size()-1).floatValue();
            float currentHumidity = HourView.getHour().getHumidity().get(HourView.getHour().getHumidity().size()-1).floatValue();
            List<String> ret =  new ExecuteCompare().execute(currentTemperature,currentHumidity).get();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(DisplayHistoryActivity.getActivity(), android.R.layout.simple_expandable_list_item_1, ret);
            RecommandView.getRecommand().setAdapter(arrayAdapter);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private class ExecuteCompare extends AsyncTask<Float, Integer, List<String>> {
        //该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改

        @Override
        protected List<String> doInBackground(Float... params) {
            List<String> ret = new ArrayList<>();
            Connection conn = null;
            try{
                conn = DBUtil.getConnection();
                String sql = "select activityname from beanrecommandactivity " +
                        "where uptemperature > ? and downtemperature < ?" +
                        "and uphumidity > ? and downhumidity < ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setFloat(1,params[0]);
                pst.setFloat(2,params[0]);
                pst.setFloat(3,params[1]);
                pst.setFloat(4,params[1]);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    ret.add(rs.getString(1));
                }
                if(ret.size()==0){
                    ret.add("抱歉，暂时没有建议的活动");
                }
            }catch (SQLException e) {
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
        protected void onPostExecute(List<String> result) {
        }
    }
}
