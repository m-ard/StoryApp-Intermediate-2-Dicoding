package com.latihan.ardab.submissionintermediate.apinetwork

import com.latihan.ardab.submissionintermediate.data.response.LoginResponse
import com.latihan.ardab.submissionintermediate.data.response.NewStoryResponse
import com.latihan.ardab.submissionintermediate.data.response.SignUpResponse
import com.latihan.ardab.submissionintermediate.data.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.QueryMap


interface ApiService {

    @FormUrlEncoded
    @POST("/v1/login")
    fun doSignin(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("/v1/register")
    fun doSignup(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<SignUpResponse>

    @GET("v1/stories")
    suspend fun getListStory(
        @Header("Authorization") bearer: String?,
        @QueryMap queries: Map<String, Int>,
    ): StoryResponse

    @Multipart
    @POST("/v1/stories")
    fun postNewStory(
        @Header("Authorization") bearer: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody?,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?
    ): Call<NewStoryResponse>

    @GET("/v1/stories?location=1")
    fun getListMapsStory(
        @Header("Authorization") bearer: String?
    ): Call<StoryResponse>
}