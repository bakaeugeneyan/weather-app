package com.weather.application.data.remote

import com.weather.application.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class Retrofit2 {
	
	 companion object {
		private const val BASE_URL = "https://api.open-meteo.com/"
	}
	
	val restApi: RestApi by lazy {
		getRetrofit().create(RestApi::class.java)
	}
	
	private fun getRetrofit(): Retrofit {
		val client = OkHttpClient.Builder()
		
		// Adjust timeouts for debugging
		if (BuildConfig.DEBUG) {
			client.readTimeout(5, TimeUnit.MINUTES)
				.writeTimeout(5, TimeUnit.MINUTES)
		}
		
		// Add logging interceptor for debug builds
		val loggingInterceptor = HttpLoggingInterceptor().apply {
			level = if (BuildConfig.DEBUG) {
				HttpLoggingInterceptor.Level.BODY
			} else {
				HttpLoggingInterceptor.Level.NONE
			}
		}
		client.addInterceptor(loggingInterceptor)
		
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.client(client.build())
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}
	
	 object ErrorResponseHandling {
		
		fun <T> onError(response: Response<T>): String {
			var errorMsg: String? = null
			
			try {
				val errorBody = response.errorBody()?.string()
				if (!errorBody.isNullOrEmpty()) {
					val jsonObject = JSONObject(errorBody)
					errorMsg = jsonObject.getString("message")
				}
			} catch (e: Exception) {
				errorMsg = response.message()
			}
			
			return errorMsg ?: "Unknown error"
		}
		
		fun exceptionHandling(throwable: Throwable): String {
			var errorMessage = throwable.message.toString()
			if (throwable is HttpException) {
				val errorResponse = throwable.response()!!
				
				// Default to HTTP status reason
				errorMessage = errorResponse.message()
				
				// If has errorBody
				if (errorResponse.errorBody() != null) {
					try {
						// Set to errorMessage if not empty
						val errorBodyMessage = errorResponse.errorBody()!!.string()
						if (!errorBodyMessage.isNullOrEmpty()) {
							errorMessage = errorBodyMessage
						}
					} catch (ignored: IOException) {
					}
				}
			}
			
			return errorMessage
		}
	}
}