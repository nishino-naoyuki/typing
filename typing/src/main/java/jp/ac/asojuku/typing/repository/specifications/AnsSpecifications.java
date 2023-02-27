package jp.ac.asojuku.typing.repository.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import jp.ac.asojuku.typing.entity.AnsTblEntity;

public class AnsSpecifications {

    public static Specification<AnsTblEntity> uidEquals(Integer uid) {
        return uid == null ? null : new Specification<AnsTblEntity>() {
			@Override
			public Predicate toPredicate(Root<AnsTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("uid"),  uid );
			}
        };
    }
}
