package mims.beans;
import java.util.Date;
import java.text.SimpleDateFormat;
public class Dates{
  public static String getTodayDate(){
     SimpleDateFormat dtformat = new SimpleDateFormat("DD-MM-yyyy");
	 Date d = new Date();
     return dtformat.format(d);
  }
  public static String[] getDatesDifference(String year){
     String dates[] = new String[2];
     dates[0] = "01-04-"+year.substring(0,4);
     dates[1] = "31-03-"+year.substring(0,2)+""+year.substring(5,7);
     return dates;
  }
  public static String getCurrentYear(){
     SimpleDateFormat dtformat = new SimpleDateFormat("yyyy");
	 Date d = new Date();
	 Integer y1 = Integer.parseInt(dtformat.format(d));
     dtformat = new SimpleDateFormat("yy");
     Integer y2 = Integer.parseInt(dtformat.format(d));
     dtformat = new SimpleDateFormat("MM");
	 Integer m = Integer.parseInt(dtformat.format(d));
	 if(m<4)
	   y1 = y1-1;
	 else
	   y2 = y2+1;
     String y2str = y2.toString();
	 if(y2str.length()==1)
	   y2str = "0"+y2str;
	 return (y1.toString()+"-"+y2str);
  }  
  public static String getNextYear(String year){
	 if(year.length()==7){
	  Integer y1 = Integer.parseInt(year.substring(0,4));
      Integer y2 = Integer.parseInt(year.substring(5,7));
	  y1 = y1+1;
	  y2 = y2+1;
	  String y2str = y2.toString();
	  if(y2str.length()==1)
	    y2str = "0"+y2str;
	  return (y1.toString()+"-"+y2str);     
	 }else{
      return "Invalid Year";
	 }
  } 
  public static String getPreviousYear(String year){
	 if(year.length()==7){
	  Integer y1 = Integer.parseInt(year.substring(0,4));
      Integer y2 = Integer.parseInt(year.substring(5,7));
	  y1 = y1-1;
	  y2 = y2-1;
	  String y2str = y2.toString();
	  if(y2str.length()==1)
	    y2str = "0"+y2str;
	  return (y1.toString()+"-"+y2str);     
	 }else{
      return "Invalid Year";
	 }
  }
}