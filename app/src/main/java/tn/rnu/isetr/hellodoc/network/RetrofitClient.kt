package tn.rnu.isetr.hellodoc.network

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val cookieJar = object : CookieJar {

        private val cookies = mutableListOf<Cookie>()

        override fun saveFromResponse(
            url: HttpUrl,
            newCookies: List<Cookie>
        ) {
            synchronized(cookies) {
                newCookies.forEach { newCookie ->
                    cookies.removeAll {
                        it.name() == newCookie.name() &&
                                it.domain() == newCookie.domain() &&
                                it.path() == newCookie.path()
                    }
                    cookies.add(newCookie)
                }
            }
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            synchronized(cookies) {
                cookies.removeAll { it.expiresAt() <= System.currentTimeMillis() }
                return cookies.filter { it.matches(url) }
            }
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .addInterceptor { chain ->

            val originalRequest = chain.request()

            val csrfCookie = cookieJar
                .loadForRequest(originalRequest.url())
                .firstOrNull { it.name() == "XSRF-TOKEN" }

            val request = originalRequest.newBuilder().apply {
                if (csrfCookie != null) {
                    header("X-XSRF-TOKEN", csrfCookie.value())
                }
            }.build()

            chain.proceed(request)
        }
        .build()

    val authApi: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }
}
