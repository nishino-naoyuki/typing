package jp.ac.asojuku.typing.service;

import java.io.FileReader;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvFieldAssignmentException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.ac.asojuku.typing.controller.DashboardController;
import jp.ac.asojuku.typing.csv.UserCSV;
import jp.ac.asojuku.typing.dto.LoginInfoDto;
import jp.ac.asojuku.typing.entity.UserTblEntity;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.repository.UserRepository;
import jp.ac.asojuku.typing.util.Digest;
import jp.ac.asojuku.typing.param.ErrorCode;

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
		
		return dto;
	}
}
