package com.volt.voltdata.appdata

import android.*
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.volt.voltdata.apidata.*
import kotlinx.serialization.ExperimentalSerializationApi

class AppHandler {
    companion object {

        var admin = false
        var currentPage = Pages.SETTINGS
        var currentForeman: CurrentForeman = CurrentForeman("", "", 0, "")
        var currentCardAssign: CurrentCardAssign = CurrentCardAssign("", "")
        var connection = false
        val offlineSignIns = arrayListOf<ActiveTimeSheetData>()
        val offlineSignOuts = arrayListOf<FinalTimeSheetData>()
        var employeeData = listOf<EmployeeData>()


        @ExperimentalSerializationApi
        @RequiresApi(Build.VERSION_CODES.M)
        fun pageUpdate(activity: FragmentActivity) {
            val cm =
                activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connection = cm.activeNetwork != null
        }

        fun renderEmployeesInSpinner(
            list: List<EmployeeData>,
            spinner: Spinner,
            activity: FragmentActivity,
        ) {
            val employeeValues = arrayListOf<String>()
            for (sheet in list) {
                employeeValues.add(sheet.first_name + " " + sheet.last_name)
            }
            val adapter =
                ArrayAdapter(
                    activity,
                    R.layout.simple_spinner_item,
                    employeeValues
                )
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        fun renderTextInSpinner(
            list: List<String>,
            spinner: Spinner,
            activity: FragmentActivity,
            hint: String
        ) {
            val taskValues = arrayListOf<String>()
//            taskValues.add(hint)

            for (sheet in list) {
                taskValues.add(sheet)
            }
            val adapter =
                ArrayAdapter(
                    activity,
                    R.layout.simple_spinner_item,
                    taskValues
                )
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        fun renderTasksInSpinner(
            list: List<TaskData>,
            spinner: Spinner,
            activity: FragmentActivity,
            hint: String
        ) {
            val taskValues = arrayListOf<String>()
            taskValues.add(hint)

            for (sheet in list) {
                taskValues.add(sheet.display_name)
            }
            val adapter =
                ArrayAdapter(
                    activity,
                    R.layout.simple_spinner_item,
                    taskValues
                )
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        fun renderLocationsInSpinner(
            list: List<LocationData>,
            spinner: Spinner,
            activity: FragmentActivity,
            hint: String
        ) {
            val locationValues = arrayListOf<String>()
            locationValues.add(hint)

            for (sheet in list) {
                locationValues.add(sheet.display_name)
            }
            val adapter =
                ArrayAdapter(
                    activity,
                    R.layout.simple_spinner_item,
                    locationValues
                )
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

}