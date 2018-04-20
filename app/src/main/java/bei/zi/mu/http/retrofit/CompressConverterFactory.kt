package bei.zi.mu.http.retrofit

import bei.zi.mu.LogCat
import bei.zi.mu.http.bean.Bean
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.nio.charset.Charset

/**
 * 后台返回的json数据结构上非常冗余，此转换器用于自己解析Json，压缩类结构。
 *
 * Created by sodino on 2018/2/5.
 */
open class CompressConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>?, retrofit: Retrofit): Converter<ResponseBody, *>? {
        val typeToken = TypeToken.get(type)
        return CompressJsonConverter(typeToken)
    }

    internal inner class CompressJsonConverter<T>(private val typeToken : TypeToken<T>) : Converter<ResponseBody, T> {

        @Throws(IOException::class)
        override fun convert(body: ResponseBody): T? {
            if (body.contentLength() == 0L) {
                return null
            }

            val source = body.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.

            val clazz = typeToken.rawType
            if (clazz == Void::class.java) {
                return clazz.newInstance() as T
            }

            val buffer = source.buffer()
            val responseBodyString = buffer.readString(Charset.forName("UTF-8"))
            if (clazz == String::class.java) {
                return responseBodyString as T
            }
            val bean = typeToken.rawType.newInstance()
            if (bean is Bean<*, *>) {
                bean.parse(responseBodyString)
                return bean as T
            }
            return null
        }
    }
}