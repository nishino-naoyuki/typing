package jp.ac.asojuku.typing.service;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jp.ac.asojuku.typing.csv.UserCSV;
import jp.ac.asojuku.typing.dto.CreateUserDto;
import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.dto.PersonalEventInfoDto;
import jp.ac.asojuku.typing.dto.UserDetailInfoDto;
import jp.ac.asojuku.typing.entity.EventUserEntity;
import jp.ac.asojuku.typing.entity.UserTblEntity;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.UserCreateForm;
import jp.ac.asojuku.typing.util.Digest;
import jp.ac.asojuku.typing.util.Token;
import jp.ac.asojuku.typing.param.RoleId;

@Service
public class UserService extends ServiceBase{
	Logger logger = LoggerFactory.getLogger(UserService.class);

	/**
	 * 更新する
	 * @param uid
	 * @param userForm
	 */
	@Transactional(rollbackFor = Exception.class)
	public void update(Integer uid,UserCreateForm userForm) {
		
		UserTblEntity userEntity = userRepository.getOne(uid);

		userEntity.setMail(userForm.getMail());
		userEntity.setName(userForm.getName());
		userEntity.setDispName(userForm.getDispName());
		userEntity.setAffiliation(userForm.getAffiliation());
		//パスワード変更
		if( StringUtils.isNoneEmpty( userForm.getPassword() ) ) {
			userEntity.setPassword(
					Digest.createPassword(userForm.getMail(), userForm.getPassword()) 
					);
		}
		userRepository.save(userEntity);
	}
	/**
	 * @param uid
	 * @return
	 */
	public UserDetailInfoDto getUserDetail(Integer uid) {
		UserTblEntity userEntity =  userRepository.getOne(uid);
		//参加した大会の履歴を取得する
		List<EventUserEntity> euList = eventUserRepository.findByUidOrderByEid(uid);
		List<PersonalEventInfoDto> peiList = new ArrayList<>();
		for( EventUserEntity euEntity : euList) {
			PersonalEventInfoDto peiDto = getPersonalEventInfo(
											euEntity.getEid(),euEntity.getUid()
											);
			peiList.add(peiDto);
		}
		//値をセット
		UserDetailInfoDto dto = new UserDetailInfoDto();
		dto.setToken(Token.getCsrfToken());
		dto.setName(userEntity.getName());
		dto.setMail(userEntity.getMail());
		dto.setDispName(userEntity.getDispName());
		dto.setAffiliation(userEntity.getAffiliation());
		dto.setUid(userEntity.getUid());
		dto.setPersonalEventInfoList(peiList);
		dto.setRoleId(userEntity.getRole());
		
		return dto;
	}
	
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
	 * CSVファイルの中身をチェックする
	 * 
	 * @param csvPath
	 * @param errors
	 * @param type
	 * @return
	 * @throws AsoBbsSystemErrException
	 */
	public List<UserCSV> checkForCSV(String csvPath, List<String> err,String type) throws SystemErrorException {
		List<UserCSV> list = null;
		CsvToBean<UserCSV> beans = null;
		
		try (FileReader fileReader = new FileReader(csvPath)){
			///////////////////////////////
			//CSVを読み込みマッピング
			beans =new CsvToBeanBuilder<UserCSV>(
                    fileReader).withType(UserCSV.class).withThrowExceptions(false).build();
			
			list = beans.parse(); 

			beans.getCapturedExceptions().stream().forEach(
					ex -> err.add( ex.getMessage())
					);
		}catch (Exception e) {
        	logger.warn("CSVパースエラー：",e);
        	err.add("CSVパースエラー");
        	err.add( e.getMessage() );
        	if( beans != null) {
				beans.getCapturedExceptions().stream().forEach(
						ex -> err.add( ex.getMessage())
						);
        	}

        }
		
		return list;
	}
	
	/**
	 * CSV登録処理
	 * CSV->DTOの変換は無駄に思えるがCSVはバリデーションの設定なども入っているし、1件登録・更新のI/Fも残す必要があるので現状仕方なし
	 * @param userList
	 */
	@Transactional(rollbackFor = Exception.class)
	public void insertByCSV(List<UserCSV> userList) {
		for(UserCSV csv : userList) {
			insertOrUpdate(getFrom(csv));
		}
	}
	
	/**
	 * 1件更新・挿入処理。すでに登録があれば更新
	 * @param targetDto
	 */
	public void insertOrUpdate(CreateUserDto targetDto) {
		if( isExist(targetDto.getMail() )) {
			//更新
			update(targetDto);
		}else {
			//新規登録
			insert(targetDto);
		}
	}
	
	/**
	 * 1件登録処理
	 * @param insertDto
	 */
	public void insert( CreateUserDto insertDto) {
		
		userRepository.save(getFrom(insertDto,null));
		
	}
	/**
	 * 1件更新処理。検索のキーはメールアドレス
	 * @param updateDto
	 */
	public void update( CreateUserDto updateDto) {
		if( updateDto == null || updateDto.getMail() == null ) {
			return ;
		}
		//更新対象を取得
		UserTblEntity userEntity = 
				userRepository.getUserByMil(updateDto.getMail());
		if( userEntity != null ) {
			userRepository.save(getFrom(updateDto,userEntity));
		}
	}
	
	/**
	 * メールアドレスを指定してユーザーが存在するかどうかを返す
	 * @param mail
	 * @return
	 */
	public boolean isExist( String mail ) {
		if( mail == null) {
			return false;
		}
		return (userRepository.getUserByMil(mail) != null);
	}
	
	
	//--- private method ---
	/**
	 * @param userEntity
	 * @return
	 */
	private LoginInfoDto getLoginInfoFrom(UserTblEntity userEntity) {
		LoginInfoDto dto = new LoginInfoDto();
		
		dto.setUid( userEntity.getUid() );
		dto.setMail(userEntity.getMail());
		dto.setName( userEntity.getName() );
		dto.setRole((RoleId.ADMIN.equals(userEntity.getRole()) ? RoleId.ADMIN:RoleId.STUDENT));
		
		return dto;
	}
	
	/**
	 * CSV→DTO変換
	 * @param userCSV
	 * @return
	 */
	private CreateUserDto getFrom(UserCSV userCSV) {
		CreateUserDto dto = new CreateUserDto();
		dto.setAffiliation(userCSV.getAffiliation());
		dto.setDispName(userCSV.getDispName());
		dto.setMail(userCSV.getMail());
		dto.setName(userCSV.getName());
		dto.setPassword(userCSV.getPassword());
		dto.setRoleId(userCSV.getRoleId());
		
		return dto;
	}
	
	/**
	 * DTO→Entity変換
	 * @param createUserDto
	 * @param updateSrcEntity
	 * @return
	 */
	private UserTblEntity getFrom(CreateUserDto createUserDto,UserTblEntity updateSrcEntity) {
		UserTblEntity userEntity = updateSrcEntity;
		if( userEntity == null ) {
			//nullの場合は新規登録、null出ない場合は更新
			userEntity = new UserTblEntity();
			userEntity.setDelFlg(0);
			userEntity.setPassword(
					Digest.createPassword(createUserDto.getMail(), createUserDto.getPassword()) 
					);
		}
		
		userEntity.setMail(createUserDto.getMail());
		userEntity.setName(createUserDto.getName());
		userEntity.setDispName(createUserDto.getDispName());
		if( createUserDto.getRoleId() != null) {
			userEntity.setRole(createUserDto.getRoleId());
		}
		userEntity.setAffiliation(createUserDto.getAffiliation());
		
		return userEntity;
	}
}
