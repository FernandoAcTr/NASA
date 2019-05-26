package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class FormatDate {
    private final GregorianCalendar calendar;
    private final Date date;
    
    private final String day, month, year;
    
    public FormatDate() {
        calendar = new GregorianCalendar();
        date = new Date();
        calendar.setTime(date);

        day = formatDay(calendar.get(GregorianCalendar.DAY_OF_MONTH));
        month = formatMonth(calendar.get(GregorianCalendar.MONTH)+1);
        year =  calendar.get(GregorianCalendar.YEAR)+"";
    }
    
    private String formatDay(int diaCalendario){                 
        String v_dia = diaCalendario <= 9 ? "0"+diaCalendario : ""+diaCalendario;
        return v_dia;
    }
    
    private String formatMonth(int mesCalendario){               
        String v_mes = mesCalendario <= 9 ? "0"+mesCalendario : ""+mesCalendario; 
        return v_mes;
    }
    
    public Date getDate(){
        return date;
    }

    public String getTime(){
        String format = "HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date systemHour = new Date();
        return formatter.format(systemHour);
    }

    @Override
    public String toString(){
        return year + "-" + month + "-" + day;
    }

}
