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
      SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd HH:mm");
      String parsedDate=parser.format(date);
      return parsedDate;
    }
public static  Date StringToDateTime(String dateString) throws ParseException {
    SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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

    public static String StringToISO(String s) throws ParseException{
        SimpleDateFormat parserToDate=new SimpleDateFormat("dd-MM-yyyy");
        Date outputDate=parserToDate.parse(s);
        parserToDate.applyPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        String outputString=parserToDate.format(outputDate);

        return outputString;

    }

    public static String StringToShort(String s) throws ParseException {
        SimpleDateFormat parserToDate=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        Date outputDate=parserToDate.parse(s);
        parserToDate.applyPattern("dd-MM-yyyy HH:mm");
        String outputString=parserToDate.format(outputDate);

        return outputString;

    }


}




