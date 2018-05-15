package bei.zi.mu.rxbus.event

import bei.zi.mu.http.bean.WordBean

data class ImportedWordEvent(val index : Int,      // 当前个数
                             val word  : WordBean  // 当前单词
                              ) {

}
