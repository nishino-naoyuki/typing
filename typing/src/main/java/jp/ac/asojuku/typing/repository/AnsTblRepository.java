package jp.ac.asojuku.typing.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.typing.dto.summary.RankingSummary;
import jp.ac.asojuku.typing.entity.AnsTblEntity;

public interface AnsTblRepository 
	extends JpaSpecificationExecutor<AnsTblEntity>, JpaRepository<AnsTblEntity, Integer>{

	@Modifying
	@Transactional
	@Query("delete from AnsTblEntity a where a.eqid=:eqid")
	public void delteByEqId(@Param("eqid")Integer eqid);
	
	public AnsTblEntity findByUidAndEqid(@Param("uid")Integer uid,@Param("eqid")Integer eqid);
	
	@Query("select sum(a.submitCount) from AnsTblEntity a "
			+ "left join a.eventQuestion eq "
			+ "where eq.eid = :eid and a.uid=:uid")
	public Integer getAnsCountByEidUid(@Param("eid")Integer eid,@Param("uid")Integer uid);
	
	@Query(value="select sum(COALESCE(a.score,0)) as tscore,eu.uid,eu.eid "
			+ "from event_user eu "
			+ "LEFT JOIN event_question eq ON eu.eid=eq.eid "
			+ "LEFT JOIN ans_tbl a ON (eq.eqid=a.eqid and eu.uid=a.uid) "
			+ "LEFT JOIN user_tbl u ON eu.uid = u.uid "
			+ "where u.role = 0 AND eu.del_flg=0 AND eu.eid=:eid group by eu.eid,eu.uid order by sum(a.score) DESC"
			,nativeQuery = true)
	public List<Object[]> getRanking(@Param("eid")Integer eid);
	default List<RankingSummary> findRankingSummary(Integer eid) {
	    return getRanking(eid).stream()  //*3
	      .map(RankingSummary::new)
	      .collect(Collectors.toList());
	  }
}
