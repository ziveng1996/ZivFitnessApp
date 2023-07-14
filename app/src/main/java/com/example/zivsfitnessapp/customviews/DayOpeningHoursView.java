package com.example.zivsfitnessapp.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.zivsfitnessapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayOpeningHoursView extends LinearLayout {

    private AppCompatTextView day;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private List<String> days;
    private List<String> fromTimeSlots;
    private List<String> toTimeSlots;

    public DayOpeningHoursView(Context context) {
        super(context);
        init();
    }

    public DayOpeningHoursView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DayOpeningHoursView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.day_openning_hours_view, this);
        day = findViewById(R.id.day);
        fromSpinner = findViewById(R.id.fromSpinner);
        toSpinner = findViewById(R.id.toSpinner);
        fromTimeSlots = new ArrayList(Arrays.asList(
                getContext().getString(R.string.seven_am),
                getContext().getString(R.string.eight_am),
                getContext().getString(R.string.nine_am),
                getContext().getString(R.string.ten_am),
                getContext().getString(R.string.eleven_am),
                getContext().getString(R.string.twelve_pm)
        ));

        toTimeSlots = new ArrayList(Arrays.asList(
                getContext().getString(R.string.one_pm),
                getContext().getString(R.string.two_pm),
                getContext().getString(R.string.three_pm),
                getContext().getString(R.string.four_pm),
                getContext().getString(R.string.five_pm),
                getContext().getString(R.string.six_pm),
                getContext().getString(R.string.seven_pm),
                getContext().getString(R.string.eight_pm),
                getContext().getString(R.string.nine_pm),
                getContext().getString(R.string.ten_pm)
        ));

        ArrayAdapter<String> fromAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, fromTimeSlots);
        ArrayAdapter<String> toAdapter = new ArrayAdapter(getContext(), R.layout.spinner_item, toTimeSlots);

        fromSpinner.setAdapter(fromAdapter);
        toSpinner.setAdapter(toAdapter);

        fromSpinner.setSelection(0);
        toSpinner.setSelection(toTimeSlots.size() - 1);
    }

    public void setDay(String day) {
        this.day.setText(day + ":");
    }

    public int getFrom() {
        return fromSpinner.getSelectedItemPosition() + 7;
    }

    public int getTo() {
        return toSpinner.getSelectedItemPosition() + 13;
    }
}
