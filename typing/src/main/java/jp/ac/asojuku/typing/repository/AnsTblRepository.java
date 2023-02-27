package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.typing.entity.AnsTblEntity;

public interface AnsTblRepository 
	extends JpaSpecificationExecutor<AnsTblEntity>, JpaRepository<AnsTblEntity, Integer>{

	@Query("select a from AnsTblEntity a "
			+ "left join a.eventQuestion eq "
			+ "where eq.qid = :qid "
			+ "order by a.ansTimestamp DESC")
	AnsTblEntity getRecentlyOne(@Param("qid")Integer qid);
}
