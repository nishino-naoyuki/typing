package jp.ac.asojuku.typing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.EventUserEntity;

public interface EventUserRepository 
	extends JpaSpecificationExecutor<EventUserEntity>, JpaRepository<EventUserEntity, Integer>{
	
	EventUserEntity findByUidAndEid(Integer uid,Integer eid);
	
	List<EventUserEntity> findByEidOrderByUid(Integer eid);
	
	List<EventUserEntity> findByUidOrderByEid(Integer uid);
}
