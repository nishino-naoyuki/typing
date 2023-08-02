package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.AnsTempTblEntity;

public interface AnsTempTblRepository 
	extends JpaSpecificationExecutor<AnsTempTblEntity>, JpaRepository<AnsTempTblEntity, String>{

	public AnsTempTblEntity findByUidAndEqid(int uid,int eqid);
}
