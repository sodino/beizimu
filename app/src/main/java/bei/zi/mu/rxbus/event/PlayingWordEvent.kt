package bei.zi.mu.rxbus.event

import bei.zi.mu.http.bean.WordBean

data class PlayingWordEvent(val index : Int,        // 当前word在整体listWords中的序号
                            val repeatCount : Int,  // 当前word的重复播放的次数
                            val word : WordBean) {
}