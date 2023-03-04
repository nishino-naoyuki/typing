package jp.ac.asojuku.typing.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.typing.dto.summary.RankingSummary;
import jp.ac.asojuku.typing.entity.AnsTblEntity;

public interface AnsTblRepository 
	extends JpaSpecificationExecutor<AnsTblEntity>, JpaRepository<AnsTblEntity, Integer>{

	@Query("select count(*) from AnsTblEntity a "
			+ "left join a.eventQuestion eq "
			+ "where eq.eid = :eid and a.uid=:uid")
	public Integer getAnsCountByEidUid(@Param("eid")Integer eid,@Param("uid")Integer uid);
	
	@Query(value="select * from ANS_TBL a "
			+ "left join EVENT_QUESTION eq ON a.eqid=eq.eqid "
			+ "where eq.qid = :qid and a.uid = :uid "
			+ "order by a.ans_timestamp DESC LIMIT 1",nativeQuery = true)
	public AnsTblEntity getRecentlyOne(@Param("qid")Integer qid,@Param("uid")Integer uid);

	@Query(value="select sum(a.score) as tscore,a.uid "
			+ "from ANS_TBL a "
			+ "left join EVENT_QUESTION eq ON a.eqid=eq.eqid "
			+ "where eq.eid=:eid group by a.uid order by sum(a.score)"
			,nativeQuery = true)
	public List<Object[]> getRanking(@Param("eid")Integer eid);
	default List<RankingSummary> findRankingSummary(Integer eid) {
	    return getRanking(eid).stream()  //*3
	      .map(RankingSummary::new)
	      .collect(Collectors.toList());
	  }
}
