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
 * 解答テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="ANS_TBL")
public class AnsTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** ansid. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ansid;

	/** ユーザーテーブル. */
	private Integer uid;
	@OneToOne
    @JoinColumn(name="uid",insertable=false ,updatable=false)
	private UserTblEntity userTbl;

	/** イベント問題文. */
	private Integer eqid;
	@OneToOne
    @JoinColumn(name="eqid",insertable=false ,updatable=false)
	private EventQuestionEntity eventQuestion;

	/** answer. */
	private String answer;

	/** time. */
	private Integer time;

	/** ans_timestamp. */
	private Date ansTimestamp;

	/** correct_flg. */
	private Integer correctFlg;

	/** difflog. */
	private String log;

	/** score. */
	private Integer score;


}