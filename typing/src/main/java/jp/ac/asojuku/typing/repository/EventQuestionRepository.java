package jp.ac.asojuku.typing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.typing.entity.EventQuestionEntity;

public interface EventQuestionRepository 
	extends JpaSpecificationExecutor<EventQuestionEntity>, JpaRepository<EventQuestionEntity, Integer>{
	
	@Modifying
	@Transactional
	@Query("delete from EventQuestionEntity e where e.eid=:eid")
	public void deleteByEid(@Param("eid") Integer eid);
	
	public List<EventQuestionEntity> findByEidOrderByNo(Integer eid);
	
	public EventQuestionEntity findByQidAndEidIsNull(Integer qid);
	public EventQuestionEntity findByQidAndEid(Integer qid,int eid);
}
