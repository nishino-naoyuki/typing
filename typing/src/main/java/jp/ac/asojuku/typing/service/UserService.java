package jp.ac.asojuku.typing.service;

import java.io.FileReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvFieldAssignmentException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.ac.asojuku.typing.controller.DashboardController;
import jp.ac.asojuku.typing.csv.UserCSV;
import jp.ac.asojuku.typing.dto.CreateUserDto;
import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.entity.UserTblEntity;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.repository.UserRepository;
import jp.ac.asojuku.typing.util.Digest;
import jp.ac.asojuku.typing.param.ErrorCode;
import jp.ac.asojuku.typing.param.RoleId;

@Service
public class UserService {
	Logger logger = LoggerFactory.getLogger(UserService.class);
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
		userEntity.setRole(createUserDto.getRoleId());
		userEntity.setAffiliation(createUserDto.getAffiliation());
		
		return userEntity;
	}
}