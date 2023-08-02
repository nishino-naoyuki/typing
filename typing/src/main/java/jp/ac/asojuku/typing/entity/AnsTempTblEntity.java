package jp.ac.asojuku.typing.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

/**
 * 解答一時テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="ANS_TEMP_TBL")
public class AnsTempTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** token. */
	@Id
	private String token;

	/** start_time. */
	private Date startTime;

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


}
