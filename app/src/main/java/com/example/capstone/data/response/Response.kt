package com.example.capstone.data.response

import android.os.Parcelable
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
	val description: String? = null,
	val title: String? = null,
	val content: String? = null
):Parcelable