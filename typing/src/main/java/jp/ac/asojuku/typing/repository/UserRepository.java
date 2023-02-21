package jp.ac.asojuku.typing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jp.ac.asojuku.typing.entity.UserTblEntity;

public interface UserRepository 
	extends JpaSpecificationExecutor<UserTblEntity>, JpaRepository<UserTblEntity, Integer>{
	
	@Query("select u from UserTblEntity u where mail = :mail and password = :password and delFlg=0")
	public UserTblEntity getUser(@Param("mail")String mail,@Param("password")String password);
	
	@Query("select u from UserTblEntity u where mail = :mail and delFlg=0")
	public UserTblEntity getUserByMil(@Param("mail")String mail);

}
