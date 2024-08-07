package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.EventDownloadEntity;

public interface EventDownloadRepository 
	extends JpaSpecificationExecutor<EventDownloadEntity>, JpaRepository<EventDownloadEntity, Integer>{
	
}
