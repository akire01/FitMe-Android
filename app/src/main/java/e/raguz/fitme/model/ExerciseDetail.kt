package e.raguz.fitme.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class ExerciseDetail(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("uuid") val uuid : String,
    @SerializedName("category") val category : Category,
    @SerializedName("description") val description : String,
    @SerializedName("creation_date") val creation_date : String,
    @SerializedName("muscles") val muscles : List<Muscle>,
    @SerializedName("muscles_secondary") val muscles_secondary : List<Muscle>,
    @SerializedName("equipment") val equipment : List<Equipment>,
    @SerializedName("language") val language : Language,
    @SerializedName("license") val license : License,
    @SerializedName("license_author") val license_author : String,
    @SerializedName("images") val images : List<Image>,
    @SerializedName("variations") val variations : Variation?,
    @SerializedName("comments") val comments : List<Comment>
)

data class Muscle(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("is_front") val isFront : Boolean
)

data class Category (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)


data class Equipment (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String
)

data class Language (

    @SerializedName("id") val id : Int,
    @SerializedName("short_name") val short_name : String,
    @SerializedName("full_name") val full_name : String
)


data class License (

    @SerializedName("id") val id : Int,
    @SerializedName("full_name") val full_name : String,
    @SerializedName("short_name") val short_name : String,
    @SerializedName("url") val url : String
)
data class Image (
    @SerializedName("id") val id : Int,
    @SerializedName("exercise_id") val exercise_id : Int,
    @SerializedName("image") val image : String,
    @SerializedName("status") val status : String,
    @SerializedName("is_main") val is_main : Boolean
)

data class Variation(
    @SerializedName("id") val id : Int
)

data class Comment(
    @SerializedName("id") val id : Int,
    @SerializedName("exercise_id") val exercise_id : Int,
    @SerializedName("comment") val comment : String
)