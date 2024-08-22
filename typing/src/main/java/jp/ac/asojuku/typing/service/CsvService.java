package jp.ac.asojuku.typing.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import jp.ac.asojuku.typing.config.SystemConfig;
import jp.ac.asojuku.typing.dto.ExcelFileDto;
import jp.ac.asojuku.typing.dto.summary.RankingSummary;
import jp.ac.asojuku.typing.entity.AnsDlTblEntity;
import jp.ac.asojuku.typing.entity.AnsTblEntity;
import jp.ac.asojuku.typing.entity.EventDownloadEntity;
import jp.ac.asojuku.typing.entity.EventQuestionEntity;
import jp.ac.asojuku.typing.entity.EventUserEntity;
import jp.ac.asojuku.typing.entity.UserTblEntity;
import jp.ac.asojuku.typing.exception.SystemErrorException;
import jp.ac.asojuku.typing.param.TypingConst;
import jp.ac.asojuku.typing.param.csv.EventResultCSV;
import jp.ac.asojuku.typing.param.csv.RankingCSV;
import jp.ac.asojuku.typing.util.FileUtils;

@Service
public class CsvService extends ServiceBase{
	Logger logger = LoggerFactory.getLogger(CsvService.class);
	private static final String RANKINGCSV = "ranking.csv";
	private static final String NOTANSWERD = "-";
	private static final Integer DELETE = 1;

	@Autowired
	ResourceLoader resourceLoader;
	
	/**
	 * 解答のZIPファイルを作成する
	 * @param eId
	 * @param uId
	 * @return
	 */
	public byte[] getExcelAnsZip(Integer eId,Integer uId) {

		//生徒が提出したフォルダを取得
		String excelBaseDir = SystemConfig.getInstance().getExcelbasedir();
		String uploadDir = excelBaseDir + "/" + TypingConst.UPLOADDIR + "/" + eId;
		String downloadDir = excelBaseDir + "/" + TypingConst.DOWNLOADDIR + "/" + eId;
		String zipfilepath = downloadDir + "/" + TypingConst.ANSZIPFNAME ;
		
		FileUtils.makeDir(downloadDir);
		
		byte[] b = null;
		try {
			File zipFile = FileUtils.compressZip(uploadDir,zipfilepath);
			//バイナリ変換
			Resource resource = resourceLoader.getResource("file:" + zipFile.getAbsolutePath());
			InputStream zipStream;
			zipStream = resource.getInputStream();
			b = IOUtils.toByteArray(zipStream);
			
			FileUtils.delete(zipfilepath);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		return b;
	}
	/**
	 * エクセル問題がダウンロード可能かを返す
	 * 
	 * @param uid
	 * @param eid
	 * @param no
	 * @return
	 */
	public boolean isExcelDownload(Integer uid,Integer eid,Integer no) {
		//no1はいつでもDL可能
		if( no == 1 ) {
			return true;
		}
		boolean isDownloadOk = false;
		//一つ前の問題を取得
		EventDownloadEntity eldEntity = eventDownloadRepository.findByEidAndNo(eid, no-1);
		//解答済みかをチェック
		if( eldEntity != null ) {
			AnsDlTblEntity ansDlEntity = ansDlTblRepository.findByUidAndEdId(uid, eldEntity.getEdId());
			isDownloadOk = (ansDlEntity != null);
		}
		
		return isDownloadOk;
	}
	
	/**
	 * エクセルのサンプルファイルのバイナリ取得
	 * @return
	 * @throws SystemErrorException
	 */
	public byte[] getSampleExcelFile(String excelSampleFilePath) throws SystemErrorException {
	    byte[] b = {};

	    try {
			//バイナリ変換
			Resource resource = resourceLoader.getResource("file:" + excelSampleFilePath);
			InputStream csvStream = resource.getInputStream();
			
			// byteへ変換
			b = IOUtils.toByteArray(csvStream);
	    	
	    } catch (IOException e) {
			e.printStackTrace();
			throw new SystemErrorException(e);
		}
	    
	    return b;
	}
	/**
	 * エクセル問題を取得しバイナリを返す
	 * 
	 * @param eid
	 * @param no
	 * @return
	 * @throws SystemErrorException 
	 */
	public ExcelFileDto getExcelQuestion(Integer eid,Integer no) throws SystemErrorException {
		ExcelFileDto excelFileDto = new ExcelFileDto();
		//問題を取得
		EventDownloadEntity eldEntity = eventDownloadRepository.findByEidAndNo(eid, no);

		if( eldEntity == null ) {
			excelFileDto.setFileName("error");
			excelFileDto.setData(new byte[1]);
			return excelFileDto;
		}
	    //出力ファイル名を決定する
		String excelqDir = SystemConfig.getInstance().getExcelbasedir();
	    File excelFile = new File(excelqDir + "/" + eldEntity.getDownloadTbl().getFilename());

	    byte[] b = {};

	    try {
			//バイナリ変換
			Resource resource = resourceLoader.getResource("file:" + excelFile.getAbsolutePath());
			InputStream csvStream = resource.getInputStream();
			
			// byteへ変換
			b = IOUtils.toByteArray(csvStream);
	    	
	    } catch (IOException e) {
			e.printStackTrace();
			throw new SystemErrorException(e);
		}

		excelFileDto.setFileName( FileUtils.getFileNameFromPath(excelFile.getAbsolutePath()) );
		excelFileDto.setData(b);
		
		return excelFileDto;
	}
	/**
	 * 大会結果を問題ごとで切り出す
	 * @param eid
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] getEventResult(Integer eid) throws UnsupportedEncodingException {
		//問題の一覧を取得する
		List<EventQuestionEntity> eqList = eventQuestionRepository.findByEidOrderByNo(eid);
		//イベントに参加しているユーザーを取得する
		List<EventUserEntity> euList = eventUserRepository.findByEidOrderByUid(eid);
		
		//ヘッダをセット
		EventResultCSV header = getHeaderList(eqList);
		//出力データを作成
		List<EventResultCSV> erCSVList = getDataList(eqList,euList);
		erCSVList.add(0, header);//リストの先頭にヘッダを追加
		
		//CSVデータとして一つにまとめる
		StringBuilder sb = new StringBuilder();
		for(EventResultCSV element : erCSVList) {
			sb.append(element.toString()).append("\n");	//1行ごとに書き込む
		}
		
		
		return sb.toString().getBytes(SystemConfig.getInstance().getCsvoutputencode());
	}
	/**
	 * 指定されたイベントのランキングをCSVデータとしてバイナリを返す
	 * @param eid
	 * @param outputDir
	 * @return
	 * @throws SystemErrorException
	 */
	public byte[] getRankingCSV(Integer eid,File outputDir) throws SystemErrorException {

		byte[] b  = {};
	    //出力ファイル名を決定する
	    File uploadFile = new File(outputDir.getPath() + "/" + RANKINGCSV);
	    //ランキング取得
	    List<RankingSummary> rankingSummaryList = ansTblRepository.findRankingSummary(eid);
	    
	    //CSV要素セット
	    List<RankingCSV> rankingCSVList = new ArrayList<>();
	    int rank = 1;
	    for(RankingSummary summary : rankingSummaryList) {
	    	RankingCSV element = new RankingCSV();
	    	UserTblEntity userEntity = userRepository.getOne(summary.getUid());
	    	element.setRank(rank);
	    	element.setName(userEntity.getName());
	    	element.setDispName(userEntity.getDispName());
	    	element.setMail(userEntity.getMail());
	    	element.setAffiliation(userEntity.getAffiliation());
	    	element.setPoint(summary.getScore());
	    	
	    	rankingCSVList.add(element);
	    	rank++;
	    }
	    String encode = SystemConfig.getInstance().getCsvoutputencode();
	    //出力する
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try{
			osw  = new OutputStreamWriter(new FileOutputStream(uploadFile), encode);
		    bw = new BufferedWriter(osw);
			StatefulBeanToCsv<RankingCSV> beanToCsv = new StatefulBeanToCsvBuilder<RankingCSV>(bw).build();		
			beanToCsv.write(rankingCSVList);
			bw.flush();

			//バイナリ変換
			Resource resource = resourceLoader.getResource("file:" + uploadFile.getAbsolutePath());
			InputStream csvStream = resource.getInputStream();
			
			// byteへ変換
			b = IOUtils.toByteArray(csvStream);
		} catch (CsvDataTypeMismatchException e) {
			e.printStackTrace();
			throw new SystemErrorException(e);
		} catch (CsvRequiredFieldEmptyException e) {
			e.printStackTrace();
			throw new SystemErrorException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SystemErrorException(e);
		}finally {
			try {
				if( bw != null ) {
					bw.close();
				}	
				if( osw != null ) {
					osw.close();
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
		
		return b;
		
	}
	
	private EventResultCSV getHeaderList(List<EventQuestionEntity> eqList){
		EventResultCSV header = new EventResultCSV();

		//１．大会名をセット
		header.addData("大会名");
		//２．名前をセット
		header.addData("名前");
		//３．表示名前をセット
		header.addData("表示名");
		//４．メールアドレスをセット
		header.addData("メールアドレス");
		//５．所属をセット
		header.addData("所属");
		for(EventQuestionEntity eqEntity : eqList) {
			header.addData(
					eqEntity.getNo().toString()+"."+
					eqEntity.getQestionTbl().getTitle());
		}
		
		return header;
	}
	private List<EventResultCSV> getDataList(List<EventQuestionEntity> eqList,List<EventUserEntity> euList){
		
		List<EventResultCSV> erCSVList = new ArrayList<>();
		for( EventUserEntity euEntity : euList) {
			if( euEntity.getDelFlg() != DELETE) {
				EventResultCSV element = new EventResultCSV();
				UserTblEntity userEntity = euEntity.getUserTbl();
				
				//１．大会名をセット
				element.addData(euEntity.getEventTbl().getName());
				//２．名前をセット
				element.addData(userEntity.getName());
				//３．表示名前をセット
				element.addData(userEntity.getDispName());
				//４．メールアドレスをセット
				element.addData(userEntity.getMail());
				//５．所属をセット
				element.addData(userEntity.getAffiliation());
				//大会の問題ごとにセットしていく
				for( EventQuestionEntity eqEntity  : eqList) {
					AnsTblEntity ansEntity = ansTblRepository.findByUidAndEqid(userEntity.getUid(), eqEntity.getEqid());
					//６～．点数をセット
					if( ansEntity != null ) {
						element.addData(ansEntity.getScore().toString());
					}else {
						element.addData(NOTANSWERD);
					}
				}
				erCSVList.add(element);
			}
		}
		
		return erCSVList;
	}
}
