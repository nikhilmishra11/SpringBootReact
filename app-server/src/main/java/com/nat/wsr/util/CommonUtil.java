/**
 * 
 */
package com.nat.wsr.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletContext;

import org.springframework.http.MediaType;

/**
 * @author Nikhil 26-May-2018
 */
public class CommonUtil {

	public static <E> boolean isNotNullOrEmpty(Collection<E> collection) {
		if (null != collection && !collection.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean isNotNullOrEmptyString(String string) {
		if (null != string && !string.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotNullOrMinLengthString(String string, int minLength) {
		if (null != string && !string.isEmpty() && string.length()>=minLength) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * "dd-MMM-yyyy"
	 * @return
	 */
	public static String getTimestampInStringFormat(Timestamp timestamp) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		String stringDate = simpleDateFormat.format(new Date(timestamp.getTime()));
		return stringDate;
	}

	public static MediaType getMediaTypeForFileName(ServletContext servletContext, String fileName) {
		String mineType = servletContext.getMimeType(fileName);
		try {
			MediaType mediaType = MediaType.parseMediaType(mineType);
			return mediaType;
		} catch (Exception e) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}
