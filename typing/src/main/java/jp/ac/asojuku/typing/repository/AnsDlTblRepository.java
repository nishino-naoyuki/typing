package jp.ac.asojuku.typing.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import jp.ac.asojuku.typing.entity.AnsDlTblEntity;

public interface AnsDlTblRepository 
	extends JpaSpecificationExecutor<AnsDlTblEntity>, JpaRepository<AnsDlTblEntity, Integer>{

	AnsDlTblEntity findByUidAndEdId(Integer uid,Integer edId);

	Integer countByUidAndEdId(Integer uid,Integer edId);

	void deleteByUidAndEdId(Integer uid,Integer edId);
}
