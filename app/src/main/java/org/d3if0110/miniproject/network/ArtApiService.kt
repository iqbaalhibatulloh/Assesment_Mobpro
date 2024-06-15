package org.d3if0110.miniproject.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if0110.miniproject.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

private const val BASE_URL = "https://mobpro1-iqbaal-rest-api.000webhostapp.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ArtApiService {
    @GET("api/arts.php")
    suspend fun getArt(
        @Header("Authorization") userId: String
    ): OpStatus


    @Multipart
    @POST("api/arts.php")
    suspend fun postArt(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("artis") artis: RequestBody,
        @Part("jenisKarya") jenisKarya: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @FormUrlEncoded
    @POST("api/deleteArt.php")
    suspend fun deleteArt(
        @Header("Authorization") userId: String,
        @Field("id") artId: String
    ): OpStatus
}

object ArtApi {
    val service: ArtApiService by lazy {
        retrofit.create(ArtApiService::class.java)
    }

    fun getArtUrl(imageId: String): String {
        return "${BASE_URL}api/image.php?id=$imageId"
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }

