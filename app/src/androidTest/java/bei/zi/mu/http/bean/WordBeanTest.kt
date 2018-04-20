package bei.zi.mu.http.bean

import bei.zi.mu.App
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by sodino on 2018/4/20.
 */
class WordBeanTest {
    @Test
    fun testInsertOrUpdate() {
        val box = App.myApp.boxStore.boxFor(WordBean::class.java)
        box.removeAll()
        assertTrue(box.count() == 0L)

        val word = WordBean()
        word.name = "apple"
        word.tag = "cet4"
        word.frequence = 5



        word.insertOrUpdate()
        assertTrue(box.count() == 1L)

        val tmp = WordBean()
        tmp.name = "apple"
        tmp.tag = "考研"
        tmp.frequence = 2

        tmp.insertOrUpdate()
        assertTrue(box.count() == 1L)

        val wordResult = box.query().equal(WordBean_.name, "apple").build().findFirst()
        assertTrue(wordResult?.name.equals("apple"))
        assertTrue(wordResult?.tag.equals("cet4"))
        assertTrue(wordResult?.frequence == 5)
    }
}