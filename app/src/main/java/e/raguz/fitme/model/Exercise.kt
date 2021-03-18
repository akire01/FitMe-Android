package e.raguz.fitme.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class ResponseWrapper<T>(
    @SerializedName("count") val count : Int,
    @SerializedName("next") val next : String,
    @SerializedName("previous") val previous : String,
    @SerializedName("results") val results : List<T>
    )

@Entity(
    tableName = "exercise"
)
    data class Exercise (
        @PrimaryKey(autoGenerate = true)
        @SerializedName("id")
        @ColumnInfo(name="_id")
        var id : Int,
        @SerializedName("category")
        @Ignore
        val category : Int,
        @SerializedName("description")
        @ColumnInfo(name="description")
        var description : String,
        @SerializedName("name")
        @ColumnInfo(name="title")
        var name : String,
        @SerializedName("name_original")
        @Ignore
        val name_original : String,
        @SerializedName("muscles")
        @Ignore
        val muscles : List<String>,
        @SerializedName("muscles_secondary")
        @Ignore
        val muscles_secondary : List<String>,
        @SerializedName("equipment")
        @Ignore
        val equipment : List<Int>,
        @SerializedName("creation_date")
        @Ignore
        val creation_date : String,
        @SerializedName("language")
        @Ignore
        val language : Int,
        @SerializedName("uuid")
        @Ignore
        val uuid : String,
        @SerializedName("variations")
        @Ignore
        val variations : String)

    {
        constructor() : this(0, 0, "", "", "",
            listOf(), listOf(), listOf(), "", 0, "", "")
    }
