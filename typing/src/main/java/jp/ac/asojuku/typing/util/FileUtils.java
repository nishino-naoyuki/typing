package jp.ac.asojuku.typing.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import jp.ac.asojuku.typing.config.SystemConfig;



public class FileUtils {
	Logger logger = LoggerFactory.getLogger(FileUtils.class);
	 private static final int BUFFER_SIZE = 4096;
	 
	/**
	 * ファイルの中身をStringに読み込む
	 * 
	 * @param file
	 * @return
	 */
	public static String read(File file) {
		List<String> lineList = readLine(file.getAbsolutePath());
		
		StringBuilder sb = new StringBuilder();
		for(String line : lineList) {
			if(sb.length() > 0 ) sb.append("\n");
			sb.append(line);
		}
		
		return sb.toString();
	}
	/**
	 * ファイルから1行ごとのデータをリストとして読み込む
	 * @param filePath
	 * @return
	 */
	public static List<String> readLine(String filePath) {
		List<String> lineList = new ArrayList<>();

		if( filePath == null || filePath.length() == 0){
			return lineList;
		}
	   // FileReader fr = null;
	    //BufferedReader br = null;
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"))){
	       // fr = new FileReader(filePath);
	        ///br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"UTF-8"));

	        String line;
	        while ((line = br.readLine()) != null) {
	        	lineList.add(line);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        //try {
	       //     if(br != null )
	       //     	br.close();
	       //     if(fr != null)
	        //    	fr.close();
	        //} catch (IOException e) {
	        //    e.printStackTrace();
	        //}
	    }

	    return lineList;
	}
	
	/**
	 * 指定されたファイル名をアイコンフォルダから削除する
	 * 
	 * @param fname
	 */
	public static void deleteIconFile(String fname) {

		String imgdir = SystemConfig.getInstance().getBannerbasedir() ;
		
		delete(new File(imgdir,fname));
	}
	/**
	 * アイコンファイルのアップロード
	 * アップロードディレクトリは　ベースDIR/ユーザーID/ファイル名
	 * @param userId
	 * @param multipartFile
	 * @return
	 * @throws AZCafeException
	 */
	public static String uploadIconFile(MultipartFile multipartFile)  {
		
		if( multipartFile == null || StringUtils.isEmpty(multipartFile.getOriginalFilename()) ) {
			return null;
		}
		
		String imgdir = SystemConfig.getInstance().getBannerbasedir() ;
		
		//ディレクトリ（なければ作成）
		makeDir(imgdir);
		
		//ファイル名を作成する
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateFormat = sdf.format(now);
		
		String fileName = "icon_" + dateFormat + multipartFile.getOriginalFilename();
		
		//ファイルをコピーする
		File uploadFilePath = new File(imgdir + "/" + fileName);
		try {
			multipartFile.transferTo(uploadFilePath);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			//throw new AZCafeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			//throw new AZCafeException(e);
		}
		
		return fileName;
	}

	/**
	 * ディレクトリ作成
	 * @param dir
	 */
	public static void makeDir(String dir){
		//Fileオブジェクトを生成する
        File f = new File(dir);

        if (!f.exists()) {
            //フォルダ作成実行
            f.mkdirs();
        }
        //権限を７７７に
        f.setExecutable(true,false);
        f.setWritable(true, false);
        f.setReadable(true, false);
	}

	/**
	 * ファイル名から拡張子を取り除いた名前を返します。
	 * @param fileName ファイル名
	 * @return ファイル名
	 */
	public static String getPreffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(0, point);
	    }
	    return fileName;
	}
	
	/**
	 * ファイル名から拡張子を返します。
	 * @param fileName ファイル名
	 * @return ファイルの拡張子
	 */
	public static String getSuffix(String fileName) {
	    if (fileName == null)
	        return null;
	    int point = fileName.lastIndexOf(".");
	    if (point != -1) {
	        return fileName.substring(point + 1);
	    }
	    return fileName;
	}
	/**
	 * ファイルを比較する
	 * @param fileA
	 * @param fileB
	 * @return
	 */
	public static boolean fileCompare(String fileA, String fileB) {
	    boolean bRet = false;

        List<String> listA = readLine(fileA);
        List<String> listB = readLine(fileB);
        
        //どちらかが空はエラー（回答が無い？）
        if( listA.size() == 0 || listB.size() == 0) {
        	return false;
        }
        
        //最後の改行を削除
        String tailA = listA.get(listA.size()-1);
        if( tailA.equals("\n")) {
        	listA.remove(listA.size()-1);
        }
        String tailB = listB.get(listB.size()-1);
        if( tailB.equals("\n")) {
        	listB.remove(listB.size()-1);
        }
        String[] arrayA = listA.toArray(new String[0]);
        String[] arrayB = listB.toArray(new String[0]);

        bRet = Arrays.equals(arrayA, arrayB);

	    return bRet;
	}

	/**
	 * 削除
	 * @param path
	 */
	public static void delete(String path){
		File file = new File(path);

		delete(file);
	}
	public static void delete(File f){

		/*
         * ファイルまたはディレクトリが存在しない場合は何もしない
         */
        if(f.exists() == false) {
            return;
        }

        if(f.isFile()) {
            /*
             * ファイルの場合は削除する
             */
            f.delete();

        } else if(f.isDirectory()){
            /*
             * ディレクトリの場合は、すべてのファイルを削除する
             */

            /*
             * 対象ディレクトリ内のファイルおよびディレクトリの一覧を取得
             */
            File[] files = f.listFiles();

            if( files == null )
            	return;
            /*
             * ファイルおよびディレクトリをすべて削除
             */
            for(int i=0; i<files.length; i++) {
                /*
                 * 自身をコールし、再帰的に削除する
                 */
                delete( files[i] );
            }
            /*
             * 自ディレクトリを削除する
             */
            f.delete();
        }

	}

	/**
	 * コピーする
	 *
	 * @param srcPath
	 * @param dstPath
	 * @throws IOException
	 */
	public static void copy(String srcPath,String dstPath) throws IOException{

		File f = new File(dstPath);

		f.getParentFile().mkdirs();

		Files.copy(
				new File(srcPath).toPath(),
				new File(dstPath).toPath());
	}

	/**
	 * パスからファイル名を取得する
	 * @param path
	 * @return
	 */
	public static String getFileNameFromPath(String path){

		File f = new File(path);

		return f.getName();
	}

	/**
	 * 指定されたディレクトリから、指定された拡張子のファイルの一覧を取得する
	 *
	 * @param dir
	 * @param extend
	 * @return
	 */
	public static File[] getFiles(String dir,String extend){

		FilenameFilter filter = null;

		//拡張子の指定があるか？
		if( extend != null && extend.length()>0){
			//フィルタを作成する
			filter = new FilenameFilter() {
				public boolean accept(File file, String str){

					// 拡張子を指定する
					if (str.endsWith(extend)){
						return true;
					}else{
						return false;
					}
				}
			};
		}

		//ファイルの一覧を取得する
		return  new File(dir).listFiles(filter);
	}
	
	/**
	 * dirで指定されたディレクトリにfnameで指定されたファイル名で
	 * contnetの内容を出力する
	 * 
	 * @param dir
	 * @param fname
	 * @param content
	 * @return
	 * @throws AZCafeException
	 */
	public static Path outputFile(String dir,String fname,String content)  {
		Path path = Paths.get(dir,fname);
		
		if( !Files.isWritable(path.getParent()) ) {
			makeDir(path.getParent().toString());
		}
		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            // ファイルへの書き込み
			bw.write(content);

        } catch (IOException e) {
            e.printStackTrace();
            //throw new AZCafeException(e);
        }
		
		return path;
	}

	public static Path outputFile(String dir,String fname,List<String> content) {
		Path path = Paths.get(dir,fname);
		
		if( !Files.isWritable(path.getParent()) ) {
			makeDir(path.getParent().toString());
		}
		try (BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            // ファイルへの書き込み
			for(String line : content) {
				bw.write(line);
				bw.newLine();
			}

        } catch (IOException e) {
            e.printStackTrace();
            //throw new AZCafeException(e);
        }
		
		return path;
	}

	/**
	 * 指定されたエンコードでファイルを出力する
	 * 
	 * ※newBufferedWriterを使用するとUnmappableCharacterExceptionが発生する。
	 * 原因は、newBufferedWriterはCharcodeEncoderの設定がREPORTになっており
	 * 変換できないもいがあると即例外を出すようなせってになっているため。
	 * 
	 * 参考）https://www.slideshare.net/chibochibo/niobufferedwriter
	 * 
	 * @param filePath
	 * @param content
	 * @param encode
	 * @return
	 * @throws AZCafeException
	 */
	public static Path outputFile(String filePath,List<String> content,String encode)  {
		Path path = Paths.get(filePath);
		
		if( !Files.isWritable(path.getParent()) ) {
			makeDir(path.getParent().toString());
		}
		
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),Charset.forName(encode))) ) {
		//try (BufferedWriter bw = Files.newBufferedWriter(path, Charset.forName(encode))) {
            // ファイルへの書き込み
			for(String line : content) {
				try {
					bw.write(line);
					bw.newLine();
				}catch (IOException e) {
					;//none
				}
			}

        } catch (IOException e) {
            e.printStackTrace();
            //throw new AZCafeException(e);
        }
		
		return path;
	}
	
	/**
	 * 一意なディレクトリを作成する
	 * @param baseDir
	 * @return
	 */
	public static String createUniqPath(String baseDir) {
		return baseDir +"/" + Token.getCsrfToken();
	}
	
	/**
	 * 与えられたファイル名にサフィックス（設備後）をつけて返す
	 * @param filePath
	 * @param suffixStr
	 * @return
	 */
	public static String makeFilenameSuffix(String filename,String suffixStr) {
		String ext = getSuffix(filename);
		
		return filename+"_"+suffixStr+"."+ext;
	}
	
	public static File getZipFile(String dir,String outputFilename) {
		try(
            // 入力ファイルのストリームを作成
            FileInputStream fis = new FileInputStream(dir);
            // 出力ZIPファイルのストリームを作成
            FileOutputStream fos = new FileOutputStream(outputFilename);
            // ZIP出力ストリームを作成
            ZipOutputStream zos = new ZipOutputStream(fos);
				) {
            // ZIPエントリを作成
            ZipEntry zipEntry = new ZipEntry(dir);
            // ZIPエントリを追加
            zos.putNextEntry(zipEntry);
            // 入力ファイルを読み込んでZIPに書き込む
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            System.out.println("ファイルが正常に圧縮されました。");
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		return new File(outputFilename);
	}
	
    public static File compressZip(String srcPath, String destPath) throws IOException {
    	  
        //圧縮先ファイルの出力ストリームを作成
        try(FileOutputStream fos = new FileOutputStream(destPath);
            ZipOutputStream zos = new ZipOutputStream(fos);){
 
            Path entryPath = Paths.get(srcPath);
 
            //フォルダ内のファイルとサブフォルダを再帰的にZIPに追加
            compressEntry(entryPath, entryPath.getFileName().toString(), zos);
        }

		return new File(destPath);
    }
 
    private static void compressEntry(Path targetPath, String parentDirName, ZipOutputStream zos) throws IOException {
        
        //フォルダ内のフォルダ、ファイルを取得
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(targetPath)) {
            for (Path filePath : ds) {
                
                if (Files.isDirectory(filePath)) {
                    //フォルダの場合、再帰的に処置
                    compressEntry(filePath, parentDirName + "/" + filePath.getFileName(), zos);
                }
                else {
                    //ファイルの場合、圧縮
                    //System.out.println("-> 圧縮中.. " + parentDirName + "/" + filePath.getFileName().toString());
                    
                    ZipEntry zipEntry = new ZipEntry(parentDirName + "/" + filePath.getFileName().toString());
                    zos.putNextEntry(zipEntry);
 
                    try(FileInputStream fis = new FileInputStream(filePath.toFile());
                        BufferedInputStream bis = new BufferedInputStream(fis);){
 
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int len = -1;
                        while ((len = bis.read(buffer, 0, buffer.length)) != -1) {
                            zos.write(buffer, 0, len);
                        }
                    }
 
                    zos.closeEntry();
                }
            }
        }
    }
}
