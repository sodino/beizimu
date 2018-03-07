package bei.zi.mu.http.bean

interface BeanInterface {
    /** true:真正取到数据，各种list或bean不会为空。false:具体的list.size==0或bean为null.  */
    fun isFilled() : Boolean
}
