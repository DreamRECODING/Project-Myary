package com.example.myary

import android.app.Dialog
import android.app.TimePickerDialog

import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.DialogFragment

//시계 호출하는 클래스
class TimePickerFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var c: Calendar = Calendar.getInstance()

        var hour = c.get(Calendar.HOUR_OF_DAY)
        var minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(activity, activity as TimePickerDialog.OnTimeSetListener,
            hour, minute, DateFormat.is24HourFormat(activity))
    }
}
