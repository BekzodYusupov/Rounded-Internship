package uz.gita.rounded_internship.data.remote

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import uz.gita.rounded_internship.data.remote.model.AppData
import uz.gita.rounded_internship.utils.HEADER

interface DialogApi {
    @GET("photos/random")
    suspend fun random(
        @Header("Authorization") string: String = HEADER
//    @Query("client_id") clientId:String = HEADER
    ): Response<AppData>

}