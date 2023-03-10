/**
 *
 */
package jp.ac.asojuku.typing.param;

/**
 * エラーコード
 * @author nishino
 *
 */
public enum ErrorCode {

	SUCCESS("errmsg0000") ,
	//ログイン(00xx)
	ERR_LOGIN("errmsg0001"),
	//問題登録(01xx)
	ERR_QC_TITLE_EMPTY("errmsg0101"),
	ERR_QC_SENTENCE_EMPTY("errmsg0102"),
	ERR_QC_TITLE_TOO_LONG("errmsg0103"),
	ERR_QC_SENTENCE_TOO_LONG("errmsg0104"),
	//イベント関連(02xx)
	ERR_EVT_NAME_EMPTY("errmsg0201"),
	ERR_EVT_PUBS_EMPTY("errmsg0202"),
	ERR_EVT_EVTS_EMPTY("errmsg0203"),
	ERR_EVT_EVTE_EMPTY("errmsg0204"),
	ERR_EVT_PUBE_EMPTY("errmsg0205"),
	ERR_EVT_OWN_EMPTY("errmsg0206"),
	ERR_EVT_NAME_TOOLONG("errmsg0207"),
	ERR_EVT_TIMEERROR("errmsg0208"),
	
	//掲示板関連(03xx)
	ERR_CSV_FORMAT_ERROR("errmsg0401"),
	//お知らせ関連(04xx)
	ERR_INFO_TITLE_LEN("errmsg0401"),
	ERR_INFO_MSG_LEN("errmsg0402"),
	
	//パスワード関連
	ERR_PWD_CHG_NOT_MATCH("errmsg0501"),
	ERR_PWD_CHG_OLD_PWD_EMPTY("errmsg0502"),
	ERR_PWD_CHG_NEW_PWD_EMPTY("errmsg0503"),
	ERR_PWD_CHG_OLD_PWD_WRONG("errmsg0504"),
	ERR_PWD_CHG_MAIL_EMPTY("errmsg0505"),
	ERR_PWD_CHG_MAIL_NOTEXIST("errmsg0506"),
	
	ERR_SKILL_NAME_DUPLICATE("errmsg0601"),

	//その他のエラー
	ERR_INVLIDATE("errmsg9901"),
	ERR_SYSTEM_ERROR("errmsg9999"),
	;

	private String code;

	private ErrorCode(String code){ this.code = code; }

	public String getCode(){ return code; }
	public String toString() { return code; }

}
