package com.volt.voltdata.apidata

import com.volt.voltdata.apidata.*
import retrofit2.Call
import retrofit2.http.*


interface VOLTApi {
    @GET("api/active/time_sheet")
    fun getActiveTimeSheet(): Call<List<ActiveTimeSheetData>>

    @POST("api/active")
    //@FormUrlEncoded
    fun postTimeSheet(@Body timeSheet: ActiveTimeSheetData): Call<ActiveTimeSheetData>

    @POST("api/active/time")
    //@FormUrlEncoded
    fun postTimeSheetWithTime(@Body timeSheet: ActiveTimeSheetData): Call<ActiveTimeSheetData>

    @POST("api/active/manual")
    //@FormUrlEncoded
    fun postManualActiveTimeSheet(@Body timeSheet: ActiveTimeSheetData): Call<ActiveTimeSheetData>

    @GET("api/final")
    fun getFinalTimeSheet(): Call<List<FinalTimeSheetData>>

    @POST("api/final")
    //@FormUrlEncoded
    fun postFinalSheet(@Body timeSheet: FinalTimeSheetData): Call<FinalTimeSheetData>

    @POST("api/final/time")
    //@FormUrlEncoded
    fun postFinalSheetWithTime(@Body timeSheet: FinalTimeSheetData): Call<FinalTimeSheetData>

    @POST("api/final/extra")
    fun postExtraFinalSheet(@Body timeSheet: ExtraFinalTimeSheetData): Call<ExtraFinalTimeSheetData>

    @POST("api/employee_data/update")
    fun updateEmployee(@Body employeeData: EmployeeData): Call<EmployeeData>


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