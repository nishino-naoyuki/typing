package jp.ac.asojuku.typing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.EventDownloadEntity;

public interface EventDownloadRepository 
	extends JpaSpecificationExecutor<EventDownloadEntity>, JpaRepository<EventDownloadEntity, Integer>{
	
	EventDownloadEntity findByEidAndDownloadIdAndNo(Integer eId,Integer downloadId,Integer no);

	List<EventDownloadEntity> findByEidOrderByNo(Integer eId);
}
