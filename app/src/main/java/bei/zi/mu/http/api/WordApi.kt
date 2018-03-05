package bei.zi.mu.http.api

import bei.zi.mu.http.bean.Bean
import bei.zi.mu.http.bean.WordBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

/**
 * Created by sodino on 2018/3/4.
 */
const val REQ_WORD              = "index.php"
const val REQ_GITHUB_WORD       = "sodino/AllEnglishWords/master/res/words/{firstChar}/{word}.json"

interface WordApi {
    // http://www.iciba.com/index.php?a=getWordMean&c=search&list=1%2C3%2C4%2C8%2C9%2C12%2C13%2C15&word=flat
    @GET(REQ_WORD)
    fun reqIciba(@QueryMap(encoded = true) map : Map<String, String>) : Call<WordBean>

    // https://raw.githubusercontent.com/sodino/AllEnglishWords/master/res/words/a/a-horizon.json
    @GET(REQ_GITHUB_WORD)
    fun reqGithubWord(@Path("firstChar") firstChar:String, @Path("word") word:String) : Call<WordBean>
}