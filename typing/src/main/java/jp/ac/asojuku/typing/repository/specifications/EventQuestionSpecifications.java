package jp.ac.asojuku.typing.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import jp.ac.asojuku.typing.entity.EventQuestionEntity;

public class EventQuestionSpecifications {
	  public static Specification<EventQuestionEntity> qidEquals(Integer qid) {
	        return qid == null ? null : new Specification<EventQuestionEntity>() {
				@Override
				public Predicate toPredicate(Root<EventQuestionEntity> root, CriteriaQuery<?> query,
						CriteriaBuilder cb) {
					// TODO 自動生成されたメソッド・スタブ
					return cb.equal(root.get("qid"),  qid );
				}
	        };
	    }

	  public static Specification<EventQuestionEntity> eidEquals(Integer eid) {
	        return eid == null ? null : new Specification<EventQuestionEntity>() {
				@Override
				public Predicate toPredicate(Root<EventQuestionEntity> root, CriteriaQuery<?> query,
						CriteriaBuilder cb) {
					// TODO 自動生成されたメソッド・スタブ
					return cb.equal(root.get("eid"),  eid );
				}
	        };
	    }
}
