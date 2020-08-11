package it.unitn.disi.lpsmt.flatfinder.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    private Util(){

    }

    public static String dateToString( Date date ){

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        System.err.println(format.format(date));
        return format.format(date);
    }

    public static Date stringToDate( String s ) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALY);
        return format.parse(s);

    }

}
