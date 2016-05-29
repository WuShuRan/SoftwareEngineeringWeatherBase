package ubibots.zuccweatherbase.display.ui;

import ubibots.zuccweatherbase.display.control.RequestHour;

public class DisplayView {

    public DisplayView() {
        new HourView();
        new RequestHour().executeRequest();
        new DayView();
        new RecommandView();
        new ListTab();
    }
}
