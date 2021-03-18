package e.raguz.fitme.api

import e.raguz.fitme.model.Category
import e.raguz.fitme.model.Exercise
import e.raguz.fitme.model.ExerciseDetail
import e.raguz.fitme.model.ResponseWrapper
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val API_URL = "https://wger.de/api/v2/"
interface ApplicationApi {

    @GET("exercise/?format=json&language=2&limit=50&offset=0")
    fun fetchExercises() : Call<ResponseWrapper<Exercise>>

    @GET("exerciseinfo/{id}/?format=json")
    fun fetchExerciseDetails(@Path("id") id: Int) : Call<ExerciseDetail>

    @GET("exercisecategory/?format=json")
    fun fetchCategories() : Call<ResponseWrapper<Category>>

    companion object {
        val client: ApplicationApi = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApplicationApi::class.java)
    }

}