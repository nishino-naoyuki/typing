package jp.ac.asojuku.typing.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.typing.entity.QestionTblEntity;

public interface QuestionRepository 
	extends JpaSpecificationExecutor<QestionTblEntity>, JpaRepository<QestionTblEntity, Integer>{

	@Query("select q from QestionTblEntity q "
			+ "left join EventQuestionEntity eq on q.qid=eq.qid "
			+ "where ((:role=0 and q.practiceflg=1) or :role=1) "
			+ "order by q.title")
	List<QestionTblEntity> getQList(@Param("role")Integer role);
	
	List<QestionTblEntity> findByPracticeflgOrderByTitle(Integer flg);
}
