package jp.ac.asojuku.typing.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.ac.asojuku.typing.config.SystemConfig;
import jp.ac.asojuku.typing.csv.UserCSV;
import jp.ac.asojuku.typing.dto.CSVProgressDto;
import jp.ac.asojuku.typing.dto.RankingDto;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.form.UserInputCSVForm;
import jp.ac.asojuku.typing.service.CsvService;
import jp.ac.asojuku.typing.service.EventService;
import jp.ac.asojuku.typing.service.UserService;
import jp.ac.asojuku.typing.util.FileUtils;

@RestController
public class FileController {
	Logger logger = LoggerFactory.getLogger(FileController.class);
	private static final String RANKINGCSV = "ranking.csv";
	private static final String EVENTRESULT = "eventresult.csv";

	@Autowired
	UserService userService;
	@Autowired
	CsvService csvService;
	
	/**
	 * CSV登録処理
	 * @param userInputCSVForm
	 * @param err
	 * @return
	 * @throws SystemErrorException
	 * @throws Exception
	 */
	@RequestMapping(value= {"/mastermainte/csventry"}, method=RequestMethod.POST)
    public Object csventry(
    		@Valid UserInputCSVForm userInputCSVForm,BindingResult err
    		) throws SystemErrorException,Exception {
		

		 //ディレクトリを作成する
	    File uploadDir = mkCSVUploaddirs();

	    //出力ファイル名を決定する
	    File uploadFile = new File(uploadDir.getPath() + "/" + "temp.csv");
	    //アップロードファイルを取得
	    byte[] bytes = userInputCSVForm.getInputuserfile().getBytes();
	    //出力ストリームを取得
	    try(BufferedOutputStream uploadFileStream =
               new BufferedOutputStream(new FileOutputStream(uploadFile))){
		    //ストリームに書き込んでクローズ
		    uploadFileStream.write(bytes);
	    }

        
        //エラーチェックを行う
	    List<String> errMsg = new ArrayList<>();
        List<UserCSV> userList = userService.checkForCSV(uploadFile.getAbsolutePath(),errMsg,"");

        //もうファイルはいらないので削除
		FileUtils.delete(uploadFile.getParentFile());
		
		//if( errors.isHasErr() ){
        if(errMsg.size()>0) {
			String jsonMsg =  outputErrorResult(errMsg);
			logger.info(jsonMsg);
			return jsonMsg;
		}

		//登録処理
        userService.insertByCSV(userList);

		//処理件数を返す
        return outputResult(userList);
	}

	/**
	 * ランキングのデータをダウンロードする
	 * @param eid
	 * @return
	 * @throws SystemErrorException
	 */
	@RequestMapping(value= {"/download/ranking"}, method=RequestMethod.GET)
	public Object downloadEventRanking(
			@ModelAttribute("eid")Integer eid
			) throws SystemErrorException {

		//ディレクトリを作成する
	    File uploadDir = mkCSVUploaddirs();
	    byte[] csvBinary = csvService.getRankingCSV(eid, uploadDir);

		 //もうファイルはいらないので削除
		FileUtils.delete(uploadDir);
		
		// レスポンスデータとして返却
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("filename", RANKINGCSV);
		headers.setContentLength(csvBinary.length);
		return new HttpEntity<byte[]>(csvBinary, headers);
	}

	/**
	 * 大会の結果データをダウンロードする
	 * 
	 * @param eid
	 * @return
	 * @throws SystemErrorException
	 */
	@RequestMapping(value= {"/download/result"}, method=RequestMethod.GET)
	public Object downloadEventResult(
			@ModelAttribute("eid")Integer eid
			) throws SystemErrorException {

	    byte[] csvBinary;
	    
		try {
			csvBinary = csvService.getEventResult(eid);
		} catch (UnsupportedEncodingException e) {
			throw new SystemErrorException(e);
		}
		
		// レスポンスデータとして返却
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("filename", EVENTRESULT);
		headers.setContentLength(csvBinary.length);
		return new HttpEntity<byte[]>(csvBinary, headers);
	}
	

	@RequestMapping(value= {"/download/excelq"}, method=RequestMethod.POST)
	public Object downloadExcelQuestion(
			@ModelAttribute("eid")Integer eid,
			@ModelAttribute("no")Integer no
			) {
		byte[] csvBinary = "aaaa".getBytes();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("filename", RANKINGCSV);
		headers.setContentLength(csvBinary.length);
		return new HttpEntity<byte[]>(csvBinary, headers);
	}
	/** -private- **/
    /**
     * CSVファイルアップロード用のディレクトリを作成する
     * @return
     * @throws AZCafeException
     */
    private File mkCSVUploaddirs() throws SystemErrorException{
    	
    	//アップロードディレクトリを取得する
    	StringBuilder filePath = new StringBuilder(SystemConfig.getInstance().getCsvuploaddir());
    	
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        File uploadDir = new File(filePath.toString(), sdf.format(now));
        // 既に存在する場合はプレフィックスをつける
        int prefix = 0;
        while(uploadDir.exists()){
            prefix++;
            uploadDir =
                    new File(filePath.toString() + sdf.format(now) + "-" + String.valueOf(prefix));
        }

        // フォルダ作成
        FileUtils.makeDir( uploadDir.toString());

        return uploadDir;
    }

	/**
	 * 処理結果のJSON文字列を作成する
	 * 
	 * @param userList
	 * @return
	 * @throws JsonProcessingException
	 */
	private String outputResult(List<UserCSV> userList ) throws JsonProcessingException {

		CSVProgressDto progress = new CSVProgressDto();
		
		progress.setTotal(userList.size());
		progress.setNow(userList.size());

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(progress);

        logger.trace("jsonString:{}",jsonString);

        return jsonString;

	}
	

	/**
	 * エラー処理時のJSON文字列を作成する
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	private String outputErrorResult(List<String> errMsg) throws JsonProcessingException {
		CSVProgressDto progress = new CSVProgressDto();
		StringBuffer sb = new StringBuffer();

//		for( ActionError error : errors.getList() ){
//			sb.append( error.getMessage() );
//			sb.append("\n");
//		}
		for( String error : errMsg ){
			sb.append( error );
			sb.append("\n");
		}
		progress.setErrorMsg(sb.toString());

		ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(progress);

        logger.trace("jsonString:{}",jsonString);

        return jsonString;
	}
}
