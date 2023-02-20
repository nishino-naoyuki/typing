package jp.ac.asojuku.typing.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.validators.PreAssignmentValidator;
import com.opencsv.bean.validators.MustMatchRegexExpression;

import lombok.Data;

@Data
public class UserCSV {
	//ロールID
	@PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "[1|0]")
	@CsvBindByName(column = "ロールID", required = true) 
	private int roleId;
	//メールアドレス
	@CsvBindByName(column = "メールアドレス", required = true) 
	private String mail;
	//名前
	@CsvBindByName(column = "名前", required = true) 
	private String name;
	//読み仮名
	@CsvBindByName(column = "よみがな", required = true) 
	private String yomi;
	//表示名
	@CsvBindByName(column = "表示名", required = true) 
	private String dispName;
	//パスワード
	@CsvBindByName(column = "パスワード", required = true) 
	private String password;
	//学科
	@CsvBindByName(column = "所属") 
	private String affiliation;
}
