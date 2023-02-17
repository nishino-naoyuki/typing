package jp.ac.asojuku.typing.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "ptcgs.cconfig")
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
}
