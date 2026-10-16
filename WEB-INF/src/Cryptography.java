package mims.beans;
import java.math.BigInteger ;
import java.util.Random ;
import java.io.* ;
import java.util.StringTokenizer;
import java.sql.Statement;
import java.sql.ResultSet;
public class Cryptography
{
	/**
	 * Bit length of each prime number.
	 */
	int primeSize ;

	/**
	 * Two distinct large prime numbers p and q.
	 */
	BigInteger p, q ;

	/**
	 * Modulus N.
	 */
	BigInteger N ;

	/**
	 * r = ( p - 1 ) * ( q - 1 )
	 */
	BigInteger r ;

	/**
	 * Public exponent E and Private exponent D
	 */
	BigInteger E, D ;

    /**
	 * Default Constructor.
	 *
	 * Bit length of each prime number is set to 8.
	 */
	public Cryptography(){
        
		primeSize = 8 ;

		// Generate two distinct large prime numbers p and q.
		generatePrimeNumbers() ;

		// Generate Public and Private Keys.
		generatePublicPrivateKeys() ;
	}

	/**
	 * Constructor.
	 *
	 * @param	primeSize Bit length of each prime number.
	 */
	public Cryptography( int primeSize )
	{
		this.primeSize = primeSize ;

		// Generate two distinct large prime numbers p and q.
		generatePrimeNumbers() ;

		// Generate Public and Private Keys.
		generatePublicPrivateKeys() ;
	}


	/**
	 * Generate two distinct large prime numbers p and q of given bitSize.
	 */
	public void generatePrimeNumbers()
	{
		p = new BigInteger( primeSize, 10, new Random()) ;
		do
		{
			q = new BigInteger( primeSize, 10, new Random()) ;
		}
		while( q.compareTo( p ) == 0 ) ;
	}


	/**
	 * Generate Public and Private Keys.
	 */
	public void generatePublicPrivateKeys()
	{
		// N = p * q
		N = p.multiply( q ) ;


		// r = ( p - 1 ) * ( q - 1 )
		r = p.subtract( BigInteger.valueOf( 1 ) ) ;
		r = r.multiply( q.subtract( BigInteger.valueOf( 1 ) ) ) ;


		// Choose E, coprime to and less than r
		do
		{
			E = new BigInteger( 2 * primeSize, new Random() ) ;
		}
		while( ( E.compareTo( r ) != -1 ) || ( E.gcd( r ).compareTo( BigInteger.valueOf( 1 ) ) != 0 ) ) ;


		// Compute D, the inverse of E mod r
		D = E.modInverse( r ) ;
	}


	/**
	 * Encrypts the plaintext (Using Public Key).
	 *
	 * @param	message			String containing the plaintext message to be encrypted.
	 * @return	The ciphertext as a BigInteger array.
	 */
	public String encrypt( String message )
	{
		int i ;
		byte[] temp = new byte[1] ;

		byte[] digits = message.getBytes() ;

		BigInteger[] bigdigits = new BigInteger[digits.length] ;

		for( i = 0 ; i < bigdigits.length ; i++ )
		{
			temp[0] = digits[i] ;
			bigdigits[i] = new BigInteger( temp ) ;
		}

		BigInteger[] encrypted = new BigInteger[bigdigits.length] ;

		for( i = 0 ; i < bigdigits.length ; i++ )
			encrypted[i] = bigdigits[i].modPow( E, N ) ;

		char[] charArray = new char[encrypted.length] ;
        StringBuffer sb = new StringBuffer();
		for( i = 0 ; i < encrypted.length ; i++ ){
			sb.append( encrypted[i] ) ;
            if(i != encrypted.length-1)
               sb.append("O") ;
		}

		return( sb.toString().replace('1','R') ) ;
	}

    /**
	 * Encrypts the plaintext (Using Custom Public Key).
	 *
	 * @param	message			String containing the plaintext message to be encrypted.
	 * @return	The ciphertext as a BigInteger array.
	 */
	public static String encrypt( String message,BigInteger E,BigInteger N )
	{
		int i ;
		byte[] temp = new byte[1] ;

		byte[] digits = message.getBytes() ;

		BigInteger[] bigdigits = new BigInteger[digits.length] ;

		for( i = 0 ; i < bigdigits.length ; i++ )
		{
			temp[0] = digits[i] ;
			bigdigits[i] = new BigInteger( temp ) ;
		}

		BigInteger[] encrypted = new BigInteger[bigdigits.length] ;

		for( i = 0 ; i < bigdigits.length ; i++ )
			encrypted[i] = bigdigits[i].modPow( E, N ) ;

		char[] charArray = new char[encrypted.length] ;
        StringBuffer sb = new StringBuffer();
		for( i = 0 ; i < encrypted.length ; i++ ){
			sb.append( encrypted[i] ) ;
            if(i != encrypted.length-1)
               sb.append("O") ;
		}

		return( sb.toString().replace('1','R') ) ;
	}
	
	/**
	 * Decrypts the ciphertext (Using Private Key).
	 *
	 * @param	encrypted		BigInteger array containing the ciphertext to be decrypted.
	 * @return	The decrypted plaintext.
	 */
	public static String decrypt( String encrypted,BigInteger D,BigInteger N )
	{
		int i ;

		String decrypted1 = encrypted.replace('R','1');
		
		StringTokenizer tokens = new StringTokenizer(decrypted1,"O");

		BigInteger[] decrypted2 = new BigInteger[tokens.countTokens()] ;

		for( i = 0 ; i < decrypted2.length ; i++ ){
		    String token = tokens.nextToken();
			decrypted2[i] = new BigInteger(token).modPow( D, N ) ;
		}

		char[] charArray = new char[decrypted2.length] ;

		for( i = 0 ; i < charArray.length ; i++ )
			charArray[i] = (char) ( decrypted2[i].intValue() ) ;

		return( new String( charArray ) ) ;
	}


	/**
	 * Get prime number p.
	 *
	 * @return	Prime number p.
	 */
	public BigInteger getp()
	{
		return( p ) ;
	}


	/**
	 * Get prime number q.
	 *
	 * @return	Prime number q.
	 */
	public BigInteger getq()
	{
		return( q ) ;
	}


	/**
	 * Get r.
	 *
	 * @return	r.
	 */
	public BigInteger getr()
	{
		return( r ) ;
	}


	/**
	 * Get modulus N.
	 *
	 * @return	Modulus N.
	 */
	public BigInteger getN()
	{
		return( N ) ;
	}


	/**
	 * Get Public exponent E.
	 *
	 * @return	Public exponent E.
	 */
	public BigInteger getE()
	{
		return( E ) ;
	}


	/**
	 * Get Private exponent D.
	 *
	 * @return	Private exponent D.
	 */
	public BigInteger getD()
	{
		return( D ) ;
	}
 
    public void writeData(String id,String name,String uname,String pwd,String file){
	 FileOutputStream  outputFileOutputStream = null;
	 try{
		outputFileOutputStream = new FileOutputStream(new File(file));
		String data = id+","+name+","+uname+","+pwd;
		byte[] bytes = data.getBytes();
        outputFileOutputStream.write(bytes);
     }catch(Exception e){e.printStackTrace();}
	 finally{try{
		if(outputFileOutputStream!=null)
  		  outputFileOutputStream.close();
	 }catch(Exception e){}
	 }
	}
    public static void provisionUser(String id,String name,String domain,String file){
       
	   Cryptography object = new Cryptography();

	   String username = name+object.getp().toString(  )+""+object.getq().toString(  );
	   String password = object.getN().toString(  )+""+object.getE().toString(  );
	   String keys = object.getN().toString(  )+"O"+object.getE().toString(  )+"O"+object.getD().toString(  );
      
       Statement stmt = DBUtil.createStatement("");
       String query = "insert into logindetails values("+id+",'"+username+"','"+object.encrypt(password)+"','"+object.encrypt(password)+"','"+keys+"','Inventor','"+domain+"','"+UserBean.getTodayDate()+"','','Active')";
       try{
	    int n = stmt.executeUpdate(query);
		if(n==1)
			object.writeData(id,name,username,password,file);
	   }catch(Exception e){e.printStackTrace();}
	}
	
	/**
	 * Cryptography Main program for Unit Testing.
	 */
	public static void main( String[] args ) throws IOException
	{
		
		// Get bit length of each prime number
		
		Cryptography object = null;
                int primeSize = 8;

		if(args.length!=0){
		  primeSize = Integer.parseInt( args[0] ) ;
		  object = new Cryptography( primeSize );
		}else{
          object = new Cryptography() ;
        }

		// Generate Public and Private Keys

		System.out.println( "Key Size: [" + primeSize + "]" );
		System.out.println( "" ) ;

		System.out.println( "Generated prime numbers p and q" );
		System.out.println( "p: [" + object.getp().toString( 16 ).toUpperCase() + "]" );
		System.out.println( "q: [" + object.getq().toString( 16 ).toUpperCase() + "]" );
		System.out.println( "" ) ;

		System.out.println( "The public key is the pair (N, E) which will be published." ) ;
		System.out.println( "N: [" + object.getN().toString( 16 ).toUpperCase() + "]" ) ;
		System.out.println( "E: [" + object.getE().toString( 16 ).toUpperCase() + "]" ) ;
		System.out.println( "" ) ;

		System.out.println( "The private key is the pair (N, D) which will be kept private." ) ;
		System.out.println( "N: [" + object.getN().toString( 16 ).toUpperCase() + "]" ) ;
		System.out.println( "D: [" + object.getD().toString( 16 ).toUpperCase() + "]" ) ;
		System.out.println( "" ) ;


		// Get message (plaintext) from user
		System.out.print( "Please enter message (plaintext): " ) ;
		String plaintext = ( new BufferedReader( new InputStreamReader( System.in ) ) ).readLine() ;
		System.out.println( "" ) ;

		// Encrypt Message using system generated keys
		String ciphertext = object.encrypt( plaintext ) ;
		System.out.println( "ciphertext : "+ciphertext ) ;

		System.out.println( "" ) ;
		
		Cryptography object1 = new Cryptography(16);
		String recoveredPlaintext = object1.decrypt( ciphertext ,object.getD(),object.getN()) ;

		System.out.println( "Recovered plaintext: " + recoveredPlaintext  ) ;
	}
}