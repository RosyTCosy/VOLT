package com.volt.voltdata

import com.volt.voltdata.apidata.*
import retrofit2.Call
import retrofit2.http.*


interface VOLTApi {
    @GET("api/active/time_sheet")
    fun getTimeSheet(): Call<List<ActiveTimeSheetData>>

    @POST("api/active")
    //@FormUrlEncoded
    fun postTimeSheet(@Body timeSheet: ActiveTimeSheetData): Call<ActiveTimeSheetData>

    @POST("api/final")
    //@FormUrlEncoded
    fun postFinalSheet(@Body timeSheet: FinalTimeSheetJson): Call<FinalTimeSheetJson>

    @GET("api/employee_data")
    fun getEmployees(): Call<List<EmployeeData>>

    @GET("api/location_data")
    fun getLocations(): Call<List<LocationData>>

    @GET("api/foreman")
    fun getForeman(): Call<List<ForemanData>>

    @GET("api/task_data")
    fun getTasks(): Call<List<TaskData>>

    @POST("api/active/emp_time_sheet")
    fun postEmpTimeSheet(@Body timeSheet: ActiveTimeSheetData) : Call<ActiveTimeSheetData>

}