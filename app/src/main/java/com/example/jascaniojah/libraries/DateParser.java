package com.example.jascaniojah.libraries;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Felix on 11/6/2014.
 */
public class DateParser {

public static String DateTimeToString(Date date)
    {
      SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
      String parsedDate=parser.format(date);
      return parsedDate;
    }
public static  Date StringToDateTime(String dateString) throws ParseException {
    SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date outputDate=parser.parse(dateString);
    return outputDate;
}
public static String StringToString(String s) throws ParseException {
    SimpleDateFormat parserToDate=new SimpleDateFormat("dd-MM-yyyy");
    Date outputDate=parserToDate.parse(s);
    parserToDate.applyPattern("yyyy-MM-dd");
    String outputString=parserToDate.format(outputDate);

    return outputString;


}



}




