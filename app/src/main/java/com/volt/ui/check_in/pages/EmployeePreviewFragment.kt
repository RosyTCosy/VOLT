package com.volt.ui.Authentication


import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.volt.R
import com.volt.databinding.FragmentAuthenticationBinding
import com.volt.databinding.FragmentEmployeePreviewBinding
import com.volt.voltdata.ApiHandler
import com.volt.voltdata.CacheHandler
import com.volt.voltdata.apidata.ActiveTimeSheetData
import com.volt.voltdata.apidata.EmployeeData
import com.volt.voltdata.apidata.FinalTimeSheetData
import com.volt.voltdata.appdata.AppHandler
import com.volt.voltdata.appdata.Pages
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Cache
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random
import android.app.DatePickerDialog as DatePickerDialog


class EmployeePreviewFragment : Fragment() {


    private var _binding: FragmentEmployeePreviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val apiHandler = ApiHandler()

    @RequiresApi(M)
    override fun onStart() {
        super.onStart()

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {


        _binding = FragmentEmployeePreviewBinding.inflate(inflater, container, false)
        val root: View = binding.root


        AppHandler.currentPage = Pages.EMPLOYEE_ENTRY
        val arguments = arguments
        var passedEmpId = 0
        passedEmpId = arguments!!.getInt("emp_id")
        Log.i("TK EMP_ID", passedEmpId.toString())
        for (emp in CacheHandler.getEmployeeCacheList(requireActivity())) {
            if (emp.emp_id == passedEmpId) {
                binding.employeeNameHeader.text = "${emp.first_name} ${emp.last_name}"
            }
        }
        // setVisibility(requireView())
        return root
    }


    fun newInstance(): EmployeePreviewFragment {
        return EmployeePreviewFragment()
    }


    private fun timeToDouble(time: String): Double {
        Log.i("TK Testing", (time.split(":")[0]))
        val hours = (time.split(":")[0]).toInt()
        val minutes = (time.split(":")[1]).toInt()
        return hours.toDouble() + ((minutes.toDouble() / 60))
    }

    private fun setVisibility(view: View) {
        if (AppHandler.admin) {
            AppHandler.renderTasksInSpinner(CacheHandler.getTaskCacheList(requireActivity()),
                binding.taskSpinner,
                requireActivity())
            AppHandler.renderLocationsInSpinner(CacheHandler.getLocationCacheList(requireActivity()),
                binding.locationSpinner,
                requireActivity())
        } else {
            Toast.makeText(
                requireActivity(),
                "Please Enter Foreman ID in the Settings to View Data",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setVisibility(requireView())
        binding.editTextTime.setOnClickListener {
//            val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
//            val cal = Calendar.getInstance()
//            System.out.println(dateFormat.format(cal.time))
            val c: Calendar = Calendar.getInstance()
            val mHour = c.get(Calendar.HOUR)
            val mMinute = c.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(
                requireActivity(),
                { _, hourOfDay, min ->
                    val hour = if (hourOfDay < 10) "0$hourOfDay" else hourOfDay
                    val minute = if (min < 10) "0$min" else min
                    binding.editTextTime.text = "$hour:$minute:00"
                },
                mHour,
                mMinute,
                false
            )
            timePickerDialog.show()
        }
        binding.editDateTime.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val mMonth = c.get(Calendar.MONTH)  + 1
            val mYear = c.get(Calendar.YEAR)

            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { _, Year, Month, Day ->
                    val month = if (Month + 1 < 10) "0$Month" else Month
                    val day = if (Day < 10) "0$Day" else Day
                    binding.editDateTime.text = "${month}/$day/$Year"
                },
                mYear,
                mMonth,
                mDay,
            )
            datePickerDialog.show()
        }


        if (AppHandler.admin) {
            binding.manualSubmit.setOnClickListener {
                if ((requireView().findViewById(R.id.editTextTime) as Button).text.toString() == "Enter Time") {
                    Toast.makeText(requireActivity(), "Please Enter a Time!", Toast.LENGTH_LONG)
                        .show()

                } else {
                    if (!binding.authenticationToggleButton.isChecked) {
                        val name =
                            (requireView().findViewById(R.id.empSpinner) as Spinner).selectedItem.toString()
                                .split(" ")
                        val timeSheet = FinalTimeSheetData(
                            name[0],
                            name[1],
                            (timeToDouble((requireView().findViewById(R.id.editTextTime) as Button).text.toString()) * 100).roundToInt() / 100.0
                        )
                        if (CacheHandler.finalSheetLogCheck(requireActivity(), timeSheet)) {
                            if (AppHandler.connection) {
                                apiHandler.postFinalTimeSheetWithTime(timeSheet)
                                Toast.makeText(
                                    requireActivity(),
                                    (requireView().findViewById(R.id.empSpinner) as Spinner).selectedItem.toString() + " Logged Out!",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                CacheHandler.addOffLineSignOut(timeSheet, requireActivity())
                                Toast.makeText(
                                    requireActivity(),
                                    (requireView().findViewById(R.id.empSpinner) as Spinner).selectedItem.toString() + " Logged Out Offline!",
                                    Toast.LENGTH_LONG
                                ).show()
                                CacheHandler.printAllCache(requireActivity())
                                Log.i("TK Testing", CacheHandler.getOfflineSignOutCacheList(requireActivity()).toString())
                            }
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "${name[0]} ${name[1]} Already In Offline Check Out!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {

                        val name =
                            (requireView().findViewById(R.id.empSpinner) as Spinner).selectedItem.toString()
                                .split(" ")
                        val timeSheet = ActiveTimeSheetData(
                            1,
                            Random.nextInt(1000000, 9999999),
                            name[0],
                            name[1],
                            (requireView().findViewById(R.id.editTextTime) as Button).text.toString(),
                            (requireView().findViewById(R.id.locationSpinner) as Spinner).selectedItem.toString(),
                            (requireView().findViewById(R.id.taskSpinner) as Spinner).selectedItem.toString(),
                            7,
                            "date"
                        )
                        if (CacheHandler.activeSheetLogCheck(requireActivity(), timeSheet)) {
                            if (AppHandler.connection) {
                                apiHandler.postActiveTimeSheetWithTime(timeSheet)
                                Toast.makeText(
                                    requireActivity(),
                                    (requireView().findViewById(R.id.empSpinner) as Spinner).selectedItem.toString() + " Logged In!",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                CacheHandler.addOffLineSignIn(timeSheet, requireActivity())
                                Toast.makeText(
                                    requireActivity(),
                                    (requireView().findViewById(R.id.empSpinner) as Spinner).selectedItem.toString() + " Logged In Offline!",
                                    Toast.LENGTH_LONG
                                ).show()
                                CacheHandler.printAllCache(requireActivity())
                                Log.i("TK Testing", CacheHandler.getOfflineSignInCacheList(requireActivity()).toString())
                            }
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "${name[0]} ${name[1]} Already Checked In!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(
                requireActivity(),
                "Please Enter Foreman ID in the Settings to View Data",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    fun dpToPx(dp: Int): Int {
        val density: Float = requireContext().resources
            .displayMetrics
            .density
        return (dp.toFloat() * density).roundToInt()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


//    @ExperimentalSerializationApi
//    @RequiresApi(M)
//    fun updatePage() {
//        AppHandler.pageUpdate(requireActivity())
//        if (AppHandler.connection) {
//            CacheHandler.refreshCacheData(requireActivity())
//        }
//    }
//
//
//    @RequiresApi(M)
//    private fun refreshPage() {
//        Thread.sleep(100)
//        val fragmentManager = requireActivity().supportFragmentManager
//        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(
//            R.id.nav_host_fragment_activity_main,
//            AuthenticationFragment().newInstance()
//        )
//        fragmentTransaction.addToBackStack(null)
//        fragmentTransaction.commit()
//    }
//
//    @ExperimentalSerializationApi
//    private fun setVisibility(view: View) {
//        val manual = view.findViewById(R.id.manualPane) as ConstraintLayout
//        manual.visibility = View.VISIBLE
//        if (AppHandler.admin) {
//            AppHandler.renderEmployeesInSpinner(CacheHandler.getEmployeeCacheList(requireActivity()),
//                binding.empSpinner,
//                requireActivity())
//            AppHandler.renderTasksInSpinner(CacheHandler.getTaskCacheList(requireActivity()),
//                binding.taskSpinner,
//                requireActivity())
//            AppHandler.renderLocationsInSpinner(CacheHandler.getLocationCacheList(requireActivity()),
//                binding.locationSpinner,
//                requireActivity())
//        } else {
//            Toast.makeText(
//                requireActivity(),
//                "Please Enter Foreman ID in the Settings to View Data",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//
//
