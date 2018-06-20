package bei.zi.mu.mvp.WordListActivity

import bei.zi.mu.Const
import bei.zi.mu.http.bean.GroupNameBean
import bei.zi.mu.http.bean.GroupWordBean
import bei.zi.mu.http.bean.WordBean
import bei.zi.mu.mvp.BasePresenter

/**
 * Created by sodino on 2018/4/26.
 */
public class Presenter(v : View) : BasePresenter<View>(v) {


    public fun reqWordList(type: Int) {
        var list : List<WordBean>? = null
        if (type == Const.WordTag.RECENT_100) {
            list = WordBean.findRecent100()
        } else {
            list = WordBean.findByWordTag(type)
        }

        if (list == null || list.isEmpty()) {
            view?.respEmpty()
        } else {
            view?.respWordList(list)
        }
    }
    public fun reqWordList(group: String) {
        val groupNameBean = GroupNameBean.findFirstByPrimaryKey(group)
        var groupId = 0L
        if (groupNameBean != null) {
            groupId = groupNameBean.id
        }

        var list : List<WordBean>? = null

        if (groupId != 0L) {
            list = GroupWordBean.findByGroupId(groupId)
        }

        if (list == null || list.isEmpty()) {
            view?.respEmpty()
        } else {
            view?.respWordList(list)
        }
    }

    override fun onDestroy() {

    }
}