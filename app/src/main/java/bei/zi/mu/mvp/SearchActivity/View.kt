package bei.zi.mu.mvp.SearchActivity

import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.mvp.BaseView

/**
 * Created by sodino on 2018/4/26.
 */
public interface View : BaseView {
    public fun respWord(bean : WordBean)

    public fun respError()
}