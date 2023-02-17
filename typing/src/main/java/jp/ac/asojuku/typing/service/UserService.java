package jp.ac.asojuku.typing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.entity.UserTblEntity;
import jp.ac.asojuku.typing.repository.UserRepository;
import jp.ac.asojuku.typing.util.Digest;

@Service
public class UserService {
	@Autowired 
	UserRepository userRepository;

	/**
	 * ログイン処理
	 * @param mail
	 * @param pass
	 * @return
	 */
	public LoginInfoDto login(String mail,String pass) {
		LoginInfoDto dto = null;
		
		String pwdHash = Digest.createPassword(mail, pass);
		
		UserTblEntity userEntity = userRepository.getUser(mail, pwdHash);
		
		if( userEntity != null ) {
			dto = getLoginInfoFrom(userEntity);
		}
		
		return dto;
	}
	
	/**
	 * @param userEntity
	 * @return
	 */
	private LoginInfoDto getLoginInfoFrom(UserTblEntity userEntity) {
		LoginInfoDto dto = new LoginInfoDto();
		
		dto.setUid( userEntity.getUid() );
		dto.setMail(userEntity.getMail());
		dto.setName( userEntity.getName() );
		
		return dto;
	}
}
