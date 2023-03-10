package jp.ac.asojuku.typing.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * 解答履歴テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="ANS_HISTORY_TBL")
public class AnsHistoryTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** anshid. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer anshid;

	/** 解答テーブル. */
	private Integer ansid;
	@OneToOne
    @JoinColumn(name="ansid",insertable=false ,updatable=false)
	private AnsTblEntity ansTbl;

	/** submit_no. */
	private Integer submitNo;

	/** answer. */
	private String answer;

	/** time. */
	private Integer time;

	/** ans_timestamp. */
	private Date ansTimestamp;

	/** correct_flg. */
	private Integer correctFlg;

	/** socre_json. */
	private String socreJson;

	/** score. */
	private Integer score;


}
