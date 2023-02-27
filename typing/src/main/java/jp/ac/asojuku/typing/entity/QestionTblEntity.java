package jp.ac.asojuku.typing.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

/**
 * 問題テーブル モデルクラス.
 * 
 * @author generated by ERMaster
 * @version $Id$
 */
@Data
@Entity 
@Table(name="QUESTION_TBL")
public class QestionTblEntity implements Serializable {

	/** serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** qid. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer qid;

	/** title. */
	private String title;

	/** sentence. */
	private String sentence;

	/** practiceflg. */
	private Integer practiceflg;

	/** difficalty. */
	private Integer difficalty;

	/** count. */
	private Integer count;

	/** 作成日時. */
	private Date createDate;

	/** 更新日時. */
	private Date updateDate;

	/** イベント問題テーブル */
	//@OneToMany
	//@JoinColumn(name="qid",insertable=false ,updatable=false)
	//private Set<EventQuestionEntity> eventQuestionSet;
	

	/**
	 * コンストラクタ.
	 */
	public QestionTblEntity() {
//		this.eventQuestionSet = new HashSet<EventQuestionEntity>();
	}
	
	@PrePersist
    public void onPrePersist() {
		setCreateDate(new Date());
		setUpdateDate(new Date());
    }

    @PreUpdate
    public void onPreUpdate() {
    	setUpdateDate(new Date());
    }
}
