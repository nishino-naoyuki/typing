package jp.ac.asojuku.typing.param.csv;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class RankingCSV {
	//順位
	@CsvBindByName(column = "0.順位") 
	private int rank;
	//名前
	@CsvBindByName(column = "1.名前") 
	private String name;
	//ニックネーム
	@CsvBindByName(column = "2.表示名") 
	private String dispName;
	//メールアドレス
	@CsvBindByName(column = "3.メールアドレス") 
	private String mail;
	//所属
	@CsvBindByName(column = "4.所属") 
	private String affiliation;
	//ポイント
	@CsvBindByName(column = "5.ポイント") 
	private int point;
}
