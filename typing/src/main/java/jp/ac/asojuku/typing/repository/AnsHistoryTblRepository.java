package jp.ac.asojuku.typing.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.typing.entity.AnsHistoryTblEntity;

public interface AnsHistoryTblRepository 
	extends JpaSpecificationExecutor<AnsHistoryTblEntity>, JpaRepository<AnsHistoryTblEntity, Integer>{
	
	@Query(value="select * from ans_tbl a "
			+ "left join event_question eq ON a.eqid=eq.eqid "
			+ "left join ans_history_tbl ah ON (a.ansid=ah.ansid and a.submit_count=ah.submit_no) "
			+ "where eq.qid = :qid and a.uid = :uid and ((eq.eid is null AND :eid = 0 ) OR eq.eid = :eid) "
			,nativeQuery = true)
	public AnsHistoryTblEntity getRecentlyOne(@Param("eid")Integer eid,@Param("qid")Integer qid,@Param("uid")Integer uid);

	
}
