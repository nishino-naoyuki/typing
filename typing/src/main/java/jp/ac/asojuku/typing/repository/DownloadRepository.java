package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.DownloadTblEntity;

public interface DownloadRepository 
	extends JpaSpecificationExecutor<DownloadTblEntity>, JpaRepository<DownloadTblEntity, Integer>{

}
