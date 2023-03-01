package jp.ac.asojuku.typing.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *　変換
 * @author nishino
 *
 */
public class Exchange {

	/**
	 * LocalDateTimeをDateに変換する
	 * @param localDateTime
	 * @return
	 */
	public static Date toDate(LocalDateTime localDateTime) {
		ZoneId zone = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, zone);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
	}
	public static Date toDate(final LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	/**
	 * LocalDateTimeをTimestampに変換する
	 * @param localDateTime
	 * @return
	 */
	public static Timestamp toTimestamp(LocalDateTime localDateTime) {
		return Timestamp.valueOf(localDateTime);
	}
	/**
	 * LocalDateTimeをTimestampに変換する
	 * @param localDateTime
	 * @return
	 */
	public static Timestamp toTimestamp(LocalDate localDate) {
		return Timestamp.valueOf(LocalDateTime.of(localDate,LocalTime.of(0,0,0)));
	}

	public static String toFormatString(Date date,String fmt) {
		return toFormatString(date,fmt,"");
	}
	public static String toFormatString(Date date,String fmt,String defaultMsg) {
		String dateString = defaultMsg;
		
		if( date != null ) {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			dateString = sdf.format(date);
		}
		
		return dateString;
	}
	/**
	 * LocalDateTimeを指定された文字列ん変換する
	 * @param localDateTime
	 * @param fmt
	 * @return
	 */
	public static String toFormatString(LocalDateTime localDateTime,String fmt) {
		return toFormatString(localDateTime,fmt,"");
	}
	public static String toFormatString(LocalDateTime localDateTime,String fmt,String defaultMsg) {
		String dateString = defaultMsg;
		
		if( localDateTime != null ) {
			DateTimeFormatter sdf = DateTimeFormatter.ofPattern(fmt);
			dateString = sdf.format(localDateTime);
		}
		
		return dateString;
	}
	public static String toFormatString(LocalDate localDate,String fmt) {
		return toFormatString(localDate,fmt,"");
	}
	public static String toFormatString(LocalDate localDate,String fmt,String defaultMsg) {
		String dateString = "";
		
		if( localDate != null ) {
			DateTimeFormatter sdf = DateTimeFormatter.ofPattern(fmt);
			dateString = sdf.format(localDate);
		}
		
		return dateString;
	}
	/**
	 * 年月日時分秒を指定しＤａｔｅオブジェクトを取得する
	 * @param year
	 * @param month
	 * @param day
	 * @param hour
	 * @param minuts
	 * @param second
	 * @return
	 * @throws ParseException
	 */
	public static Date toDate(Integer year,Integer month,Integer day,Integer hour,Integer minuts,Integer second) throws ParseException {
		String dateString = String.format("%04d/%02d/%02d %02d:%02d:%02d", year,month,day,hour,minuts,second);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.parse(dateString);
	}
}
