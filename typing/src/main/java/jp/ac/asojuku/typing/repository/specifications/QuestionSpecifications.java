package jp.ac.asojuku.typing.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import jp.ac.asojuku.typing.entity.QestionTblEntity;

public class QuestionSpecifications {
	public static final Integer PRACTICEFLG_ON = 1;
	public static final Integer PRACTICEFLG_OFF = 0;

    public static Specification<QestionTblEntity> qidEquals(Integer qid) {
        return qid == null ? null : new Specification<QestionTblEntity>() {
			@Override
			public Predicate toPredicate(Root<QestionTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("qid"),  qid );
			}
        };
    }

    public static Specification<QestionTblEntity> practiceEquals(Integer practice) {
        return practice == null ? null : new Specification<QestionTblEntity>() {
			@Override
			public Predicate toPredicate(Root<QestionTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("practiceflg"),  practice );
			}
        };
    }
}
