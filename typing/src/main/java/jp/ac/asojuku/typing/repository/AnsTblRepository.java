package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.typing.entity.AnsTblEntity;

public interface AnsTblRepository 
	extends JpaSpecificationExecutor<AnsTblEntity>, JpaRepository<AnsTblEntity, Integer>{

	@Query(value="select * from ANS_TBL a "
			+ "left join EVENT_QUESTION eq ON a.eqid=eq.eqid "
			+ "where eq.qid = :qid and a.uid = :uid "
			+ "order by a.ans_timestamp DESC LIMIT 1",nativeQuery = true)
	AnsTblEntity getRecentlyOne(@Param("qid")Integer qid,@Param("uid")Integer uid);
}
