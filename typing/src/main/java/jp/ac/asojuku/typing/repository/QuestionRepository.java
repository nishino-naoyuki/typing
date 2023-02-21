package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.QestionTblEntity;

public interface QuestionRepository 
	extends JpaSpecificationExecutor<QestionTblEntity>, JpaRepository<QestionTblEntity, Integer>{

}
