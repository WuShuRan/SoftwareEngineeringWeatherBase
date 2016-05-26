package ubibots.zuccweatherbase.displayhistory.ui;


import ubibots.zuccweatherbase.displayhistory.control.RequestHour;

public class DisplayView {
    public DisplayView() {
        new HourView();
        new RequestHour().executeRequest();
        new DayView();
        new ListTab();
    }
}
