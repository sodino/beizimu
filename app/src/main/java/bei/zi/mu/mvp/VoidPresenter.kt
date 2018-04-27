package bei.zi.mu.mvp

/**
 * Created by sodino on 2018/4/27.
 */
public class VoidPresenter(v : VoidView) : BasePresenter<VoidView>(v) {
    companion object {
        val void : VoidPresenter = VoidPresenter(VoidView.void)
    }

    override fun onDestroy() {
    }

}