package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.EventQuestionEntity;

public interface EventQuestionRepository 
	extends JpaSpecificationExecutor<EventQuestionEntity>, JpaRepository<EventQuestionEntity, Integer>{
	
}
