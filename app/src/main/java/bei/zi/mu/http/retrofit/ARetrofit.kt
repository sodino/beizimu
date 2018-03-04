package bei.zi.mu.http.retrofit

import bei.zi.mu.Const
import bei.zi.mu.http.api.WordApi
import bei.zi.mu.thread.ThreadPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * Created by sodino on 2018/3/4.
 */
class ARetrofit {
    companion object {
        val okHttpClient by lazy { OkHttpClient.Builder().addInterceptor(AInterceptor()).build() }

        val wordApi by lazy { getCommonRetrofit(Const.URL.ICIBA).create(WordApi::class.java) }

        fun getCommonRetrofit(baseUrl : String) : Retrofit {
            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(CompressConverterFactory())
                    .callbackExecutor(ThreadPool.threadsExecutor)
                    .client(okHttpClient)
                    .build()

            return retrofit
        }

        fun baseQueryMap() : Map<String, String> {
            // http://www.iciba.com/index.php?a=getWordMean&c=search&list=1%2C3%2C4%2C8%2C9%2C12%2C13%2C15&word=flat
            val map = HashMap<String, String>()
            map.put("a", "getWordMean")
            map.put("c", "search")
            map.put("list", "1%2C3%2C4%2C8%2C9%2C12%2C13%2C15")
            return map
        }
    }
}