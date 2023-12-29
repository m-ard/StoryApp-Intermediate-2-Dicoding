package com.latihan.ardab.submissionintermediate.data.remote

import androidx.lifecycle.MutableLiveData
import com.latihan.ardab.submissionintermediate.apinetwork.ApiConfig
import com.latihan.ardab.submissionintermediate.data.response.LoginResponse
import com.latihan.ardab.submissionintermediate.data.response.NewStoryResponse
import com.latihan.ardab.submissionintermediate.data.response.SignUpResponse
import com.latihan.ardab.submissionintermediate.data.response.StoryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RemoteData {
    val error = MutableLiveData("")
    var responsecode = ""

    fun signup(callback: SignupCallback, name: String, email: String, password: String){
        val signupinfo = SignUpResponse(
            true,
            ""
        )
        callback.onSignUp(
            signupinfo
        )
        val client = ApiConfig.getApiService().doSignup(name, email, password)
        client.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if(response.isSuccessful){
                    response.body()?.let { callback.onSignUp(it) }
                    responsecode = "201"
                    callback.onSignUp(
                        SignUpResponse(
                            true,
                            responsecode
                        )
                    )
                }else {
                    responsecode = "400"
                    callback.onSignUp(
                        SignUpResponse(
                            true,
                            responsecode
                        )
                    )
                }
            }
            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback.onSignUp(
                    SignUpResponse(
                        true,
                        t.message.toString()
                    )
                )
            }
        })
    }

    fun signin(callback: SigninCallback, email: String, password: String) {
        callback.onLogin(
            LoginResponse(
                null,
                true,
                ""
            )
        )

        val client = ApiConfig.getApiService().doSignin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if(response.isSuccessful){
                    response.body()?.let { callback.onLogin(it) }
                }else {
                    when (response.code()) {
                        200 -> responsecode = "200"
                        400 -> responsecode = "400"
                        401 -> responsecode = "401"
                        else -> error.postValue("ERROR ${response.code()} : ${response.message()}")
                    }
                    callback.onLogin(
                        LoginResponse(
                            null,
                            true,
                            responsecode
                        )
                    )
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                callback.onLogin(
                    LoginResponse(
                        null,
                        true,
                        t.message.toString()
                    )
                )
            }
        })
    }


    fun getListMapsStory(callback: GetListMapsStoryCallback, token: String){
        val client = ApiConfig.getApiService().getListMapsStory(bearer = "Bearer $token")
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(
                call: Call<StoryResponse>,
                response: Response<StoryResponse>
            ) {
                if (response.isSuccessful){
                    response.body()?.let { callback.onMapsStoryLoad(it) }
                }else{
                    val storyResponse = StoryResponse(
                        emptyList(),
                        true,
                        "Load Failed!"
                    )
                    callback.onMapsStoryLoad(storyResponse)
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                val storyResponse = StoryResponse(
                    emptyList(),
                    true,
                    t.message.toString()
                )
                callback.onMapsStoryLoad(storyResponse)
            }
        })
    }

    fun postNewStory(callback: AddNewStoryCallback, token: String, imageFile: File, desc: String, lon: String? = null, lat: String? = null){
        callback.onNewStory(
            newStoryResponse = NewStoryResponse(
                true,
                ""
            )
        )

        val description = desc.toRequestBody("text/plain".toMediaType())
        val latitude = lat?.toRequestBody("text/plain".toMediaType())
        val logitude = lon?.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        val client = ApiConfig.getApiService().postNewStory(bearer = "Bearer $token", imageMultipart, description, latitude!!, logitude!!)

        client.enqueue(object : Callback<NewStoryResponse> {
            override fun onResponse(
                call: Call<NewStoryResponse>,
                response: Response<NewStoryResponse>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error){
                        callback.onNewStory(responseBody)
                    }else{
                        callback.onNewStory(
                            newStoryResponse = NewStoryResponse(
                                true,
                                "Gagal upload file"
                            )
                        )
                    }
                }
                else{
                    callback.onNewStory(
                        newStoryResponse = NewStoryResponse(
                            true,
                            "Gagal upload file"
                        )
                    )
                }
            }

            override fun onFailure(call: Call<NewStoryResponse>, t: Throwable) {
                callback.onNewStory(
                    newStoryResponse = NewStoryResponse(
                        true,
                        "Gagal upload file"
                    )
                )
            }
        })
    }

    interface SignupCallback{
        fun onSignUp(signUpResponse: SignUpResponse)
    }

    interface SigninCallback{
        fun onLogin(loginResponse: LoginResponse)
    }


    interface AddNewStoryCallback{
        fun onNewStory(newStoryResponse: NewStoryResponse)
    }

    interface GetListMapsStoryCallback{
        fun onMapsStoryLoad(storyResponse: StoryResponse)
    }

    companion object {
        @Volatile
        private var instance: RemoteData? = null

        fun getInstance(): RemoteData =
            instance ?: synchronized(this) {
                instance ?: RemoteData()
            }
    }
}