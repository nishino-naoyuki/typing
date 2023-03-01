package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.EventTblEntity;

public interface EventRepository 
	extends JpaSpecificationExecutor<EventTblEntity>, JpaRepository<EventTblEntity, Integer>{
}
