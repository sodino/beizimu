package bei.zi.mu.mvp.WordListActivity

import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.mvp.BaseView

public interface View : BaseView {
    public fun respWordList(list : List<WordBean>)

    public fun respEmpty()
}
