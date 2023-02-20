package jp.ac.asojuku.typing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "typing.config")
@Data
public class SystemConfig {
	private static SystemConfig config;
	
	@Autowired
	@Lazy
	public void setCofing(SystemConfig config) {
		SystemConfig.config = config;
	}
	
	public static SystemConfig getInstance() {
		return SystemConfig.config;
	}

	private String salt;	//パスワードハッシュソルト
	private String bannerbasedir;//
	private Integer provlimit;	//仮テーブルの有効期限
	private String nologindisplay;	//ログイン不要画面一覧
	private String studentdenied;	//学生アクセス不可URL
	private String csvoutputdir;	//CSVの出力ディレクトリ
	private String csvoutputencode;	//CSVの出力ファイルのエンコード
	private String csvuploaddir;	//CSVのアップロードディレクトリ
}
