package com.example.tg.util

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

data class ImageInfo(val fileName: String, val description: String)

object JsonUtil {
    private const val TAG = "JsonUtil"

    fun loadImageInfo(context: Context, fileName: String): List<ImageInfo> {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            Log.d(TAG, "JSON loaded successfully")
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            Log.e(TAG, "Error reading JSON file: $fileName")
            return emptyList()
        }

        val listType = object : TypeToken<List<ImageInfo>>() {}.type
        return Gson().fromJson(jsonString, listType)
    }
}
