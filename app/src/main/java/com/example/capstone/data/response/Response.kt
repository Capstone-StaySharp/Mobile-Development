package com.example.capstone.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Response(
	val totalResults: Int? = null,
	val articles: List<ArticlesItem> = listOf(),
	val status: String? = null
):Parcelable

@Parcelize
data class ArticlesItem(
	val id:String? = null,
	val publishedat: String? = null,
	val author: String? = null,
	val urltoimage: String? = null,
	val title: String? = null,
	val content: String? = null
):Parcelable

data class PostResponse(

	@field:SerializedName("total_image_uploaded")
	val totalImageUploaded: Int? = null,

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("remaining_image_for_processing")
	val remainingImageForProcessing: Int? = null,

	@field:SerializedName("total_image_for_processing")
	val totalImageForProcessing: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("is_model_triggered")
	val isModelTriggered: Boolean? = null
)

data class Data(

	@field:SerializedName("is_both_eyes_closed")
	val isBothEyesClosed: Boolean? = null,

	@field:SerializedName("is_yawning")
	val isYawning: Boolean? = null
)
