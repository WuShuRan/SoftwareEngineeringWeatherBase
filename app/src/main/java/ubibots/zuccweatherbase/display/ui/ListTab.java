package ubibots.zuccweatherbase.display.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ubibots.zuccweatherbase.R;
import ubibots.zuccweatherbase.display.DisplayHistoryActivity;
import ubibots.zuccweatherbase.display.control.UpdateRecommand;

public class ListTab {

    private int currentTab;
    private ListView listView;
    private TextView tempView;
    private TextView humiView;

    public int getCurrentTab() {
        return currentTab;
    }

    public ListView getListView() {
        return listView;
    }

    public ListTab() {
        tempView = (TextView) DisplayHistoryActivity.getActivity().findViewById(R.id.imageView);
        humiView = (TextView) DisplayHistoryActivity.getActivity().findViewById(R.id.imageView2);

        listView = (ListView) DisplayHistoryActivity.getActivity().findViewById(R.id.listview);
        listView.setBackgroundColor(Color.GRAY);
        listView.setCacheColorHint(0);
        final List<String> data = new ArrayList<>();
        data.add("ÿʱ");
        data.add("ÿ��");
        data.add("��Ƽ�");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(DisplayHistoryActivity.getActivity(), android.R.layout.simple_expandable_list_item_1, data);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String click = data.get(position);
            switch (click) {
                case "ÿʱ":
                    hourVisible();
                    currentTab = 0;
                    break;
                case "ÿ��":
                    dayVisible();
                    currentTab = 1;
                    break;
                case "��Ƽ�":
                    recommandVisable();
                    currentTab = 2;
                    break;
            }
        });
        hourVisible();
        currentTab = 0;
    }

    private void hourVisible() {
        if (HourView.getHourViewPager() != null) {
            HourView.getHourViewPager().setVisibility(View.VISIBLE);
            dayInvisible();
            recommandInVisable();
        }
        if (HourView.getHourProgressBar().getVisibility() != View.GONE) {
            HourView.getHourProgressBar().setVisibility(View.VISIBLE);
        }
        if (tempView != null) {
            tempView.setVisibility(View.VISIBLE);
        }
        if (humiView != null) {
            humiView.setVisibility(View.VISIBLE);
        }
    }

    private void hourInvisible() {
        if (HourView.getHourViewPager() != null) {
            HourView.getHourViewPager().setVisibility(View.INVISIBLE);
            if (HourView.getHourProgressBar().getVisibility() != View.GONE) {
                HourView.getHourProgressBar().setVisibility(View.INVISIBLE);
            }
        }
    }

    private void dayVisible() {
        if (DayView.getDayViewPager() != null) {
            DayView.getDayViewPager().setVisibility(View.VISIBLE);
            hourInvisible();
            recommandInVisable();
        }
        if (DayView.getDayProgressBar().getVisibility() != View.GONE) {
            DayView.getDayProgressBar().setVisibility(View.VISIBLE);
        }
        if (tempView != null) {
            tempView.setVisibility(View.VISIBLE);
        }
        if (humiView != null) {
            humiView.setVisibility(View.VISIBLE);
        }
    }

    private void dayInvisible() {
        if (DayView.getDayViewPager() != null) {
            DayView.getDayViewPager().setVisibility(View.INVISIBLE);
            if (DayView.getDayProgressBar().getVisibility() != View.GONE) {
                DayView.getDayProgressBar().setVisibility(View.INVISIBLE);
            }
        }
    }

    private void recommandVisable() {
        if (RecommandView.getRecommand() != null) {
            RecommandView.getRecommand().setVisibility(View.VISIBLE);
            if (HourView.getHourProgressBar().getVisibility() == View.GONE && DayView.getDayProgressBar().getVisibility() == View.GONE) {
                new UpdateRecommand().compareData();
            }
            else{
                Toast.makeText(DisplayHistoryActivity.getContext(), "�����ĵȴ����ݻ�ȡ���", Toast.LENGTH_SHORT).show();
            }
            hourInvisible();
            dayInvisible();
        }
        if (tempView != null) {
            tempView.setVisibility(View.INVISIBLE);
        }
        if (humiView != null) {
            humiView.setVisibility(View.INVISIBLE);
        }
    }

    private void recommandInVisable() {
        if (RecommandView.getRecommand() != null) {
            RecommandView.getRecommand().setVisibility(View.INVISIBLE);
        }
    }
}
