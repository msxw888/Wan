import android.widget.Toast

/**
 * 静态常量
 */
object Constant {
    /**
     * baseUrl
     */
    const val REQUEST_BASE_URL = "https://wanandroid.com/"
    /**
     * Share preferences name
     */
    const val SHARED_NAME = "_preferences"
    const val LOGIN_KEY = "login"
    const val USERNAME_KEY = "username"
    const val PASSWORD_KEY = "password"

    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"
    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"
    /**
     * result null
     */
    const val RESULT_NULL = "result null!"
    /**
     * Toast
     */
    @JvmField
    var showToast: Toast? = null
    /**
     * Debug
     */
    const val INTERCEPTOR_ENABLE = false
    /**
     * url key
     */
    const val CONTENT_URL_KEY = "url"
    /**
     * title key
     */
    const val CONTENT_TITLE_KEY = "title"
    /**
     * id key
     */
    const val CONTENT_ID_KEY = "id"
    /**
     * cid key
     */
    const val CONTENT_CID_KEY = "cid"
    /**
     * childrenData key
     */
    const val CONTENT_CHILDREN_DATA_KEY = "childrenData"
    /**
     * target key
     */
    const val CONTENT_TARGET_KEY = "target"
    /**
     * share key
     */
    const val CONTENT_SHARE_TYPE = "text/plain"
    const val SEARCH_KEY = "search"
    const val LIKE_KEY = "like"

    const val MAIN_REQUEST_CODE = 100
    const val MAIN_LIKE_REQUEST_CODE = 101
}