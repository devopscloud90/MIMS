import mims.beans.UserBean;
import mims.beans.OppBean;
import mims.beans.Cryptography;
import java.util.HashMap;
import java.math.BigInteger;

public class Sample
{
	public static void main(String args[]){
	  Cryptography.provisionUser("4","Rushi","1","E:\\Rushi\\MIMS\\WEB-INF\\src\\inventors.csv");
      UserBean user = new UserBean("4");
	  HashMap<String,String> map = user.getKeys();
	  System.out.println(map);
	  String pwd = user.getPassword();
	  System.out.println(Cryptography.decrypt(pwd,new BigInteger(map.get("3")),new BigInteger(map.get("1"))));
      OppBean bean = new OppBean("1");
      System.out.println(bean.getId()+"---"+bean.getName()+"---"+bean.getDescription());
	}
}