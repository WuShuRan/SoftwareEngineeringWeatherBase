package ubibots.zuccweatherbase.display.ui;

import android.widget.ListView;

import ubibots.zuccweatherbase.R;
import ubibots.zuccweatherbase.display.DisplayHistoryActivity;

public class RecommandView {

    private static ListView recommand;

    public static ListView getRecommand() {
        return recommand;
    }

    public RecommandView(){
        recommand = (ListView) DisplayHistoryActivity.getActivity().findViewById(R.id.recommandList);
        recommand.setClickable(false);
    }
}
