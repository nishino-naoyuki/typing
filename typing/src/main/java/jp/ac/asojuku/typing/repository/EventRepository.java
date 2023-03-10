package jp.ac.asojuku.typing.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import jp.ac.asojuku.typing.entity.EventTblEntity;

public interface EventRepository 
	extends JpaSpecificationExecutor<EventTblEntity>, JpaRepository<EventTblEntity, Integer>{
	
	List<EventTblEntity> findByPublicDateLessThanEqualAndPublicEndDateGreaterThanEqualOrderByStartDateDesc(Date pDate1,Date pDate2);
	List<EventTblEntity> findAllByOrderByPublicDateDesc();
}
