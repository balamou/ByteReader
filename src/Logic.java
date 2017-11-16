/**
* This class takes care of the bitwise logic
*
* @author Michel Balamou (michelbalamou@gmail.com)
* @version Nov 2017
*/

public class Logic
{
    /**
    * Fills any binary string that is less than 8 bits with zeros in the most
    * significant positions.
    *
    * 1010 -> 00001010
    * 110011 -> 00110011
    *
    * @param bin string representing a binary number
    * @return a string of length 8 filled with 0 in missing spots
    */
    public static String fill(String bin)
    {
      String result = "";
      for (int i = 0; i<8-bin.length(); i++)
        result += "0";

      return result + bin;
    }

    /**
    * Converts a DEC into BIN and takes its complement then converts it back into
    * DEC.
    *
    * 10 -> A -> 1010 --(FILL)--> 00001010 --(COMPLEMENT)--> 11110101 -> return 245
    * 69 -> 45 -> 01000101 --(FILL)--> 01000101 --(COMPLEMENT)--> 10111010 -> return 186
    *
    * @param dec decimal number to be complemented
    * @return a complemented binary number in decimal
    */
    public static int complement(int dec)
    {
      String bin = fill(Integer.toBinaryString(dec)); // convert DEC to BIN, and fill missing zeros
      String result = "";

      for(int i=0; i<bin.length(); i++)
         result += (bin.charAt(i)=='0' ? '1' : '0');

      return Integer.parseInt(result, 2); // convert BIN string into a DEC number
    }


    /**
    *		.d8b.    d8b   db   d8888b.
    *	 d8' `8b   888o  88   88  `8D
    *	 88ooo88   88V8o 88   88   88
    *	 88~~~88   88 V8o88   88   88
    *	 88   88   88  V888   88  .8D
    *	 YP   YP   VP   V8P   Y8888D'
    *
    * Performs a logical bitwise AND operation on two DEC numbers.
    *
    *  10 AND 171 -> 1010 AND 10101011 -> 00001010 AND 10101011 -> 00001010 -> return 10
    *  70 AND 12 -> 01000110 AND 1100 -> 00010000 AND 00001100 -> 00000000 -> return 0
    *
    * @param dec1
    * @param dec2
    * @return
    */
    public static int and(int dec1, int dec2)
    {
      // convert DEC to BIN, and fill missing zeros
      String bin1 = fill(Integer.toBinaryString(dec1));
      String bin2 = fill(Integer.toBinaryString(dec2));

      String result = "";

      for(int i=0; i<bin1.length(); i++)
      {
        boolean t1 = (bin1.charAt(i)=='1');
        boolean t2 = (bin2.charAt(i)=='1');

        result += (t1 && t2) ? '1' : '0';
      }

      return Integer.parseInt(result, 2);
    }

    /**
    * Performs an arithmetic LEFT shift.
    * The LSB becomes 0, every other bit shifts one to the left.
    *
    * 138 -> 10001010 -> 10001010 -(SHIFT)-> 00010100 -> return 20
    *
    * @param dec number to shift
    * @return shifted number
    */
    public static int asl(int dec)
    {
      String bin = fill(Integer.toBinaryString(dec));
      String result = bin.substring(1) + "0";

      return Integer.parseInt(result, 2);
    }

    /**
    * Performs an arithmetic RIGHT shift.
    * The MSB is left unchanged, every other bit shifts one to the right.
    *
    * 138 -> 10001010 -> 10001010 -(SHIFT)-> 11000101 -> return 197
    *
    * @param dec number to shift
    * @return shifted number
    */
    public static int asr(int dec)
    {
      String bin = fill(Integer.toBinaryString(dec));
      String result = bin.charAt(0)+bin.substring(0, bin.length()-1);

      return Integer.parseInt(result, 2);
    }


    /**
  	* Converts a string that represents a HEX number into a DECIMAL number
  	*
  	* @param hexString HEX string
  	* @return DEC number
  	*/
  	public static int hexToInt(String hexString)
    {
  		return Integer.parseInt(hexString, 16);
  	}

  	/**
  	* Converts a DECIMAL number into a string in HEX.
  	*
  	* @param intHex DEC number
  	* @return HEX string
  	*/
  	public static String intToHex(int dec)
  	{
  		return Integer.toHexString(dec);
  	}


    /**
  	* Truncates a number if it exceedes 255 (or FF), since each byte can only stored
  	* up to 255 but Java allows integers to exceed that number. Just returns the Last
  	* two digits of a hexadecimal number.
  	*
  	* 511 -> 1FF -(truncate)-> FF -> returns 255
  	* 2748 -> ABC -(truncate)-> BC -> returns 188
  	*
  	* @param dec decimal number to truncate in HEX
  	* @return returns the truncated DEC
  	*/
  	public static int truncate(int dec)
  	{
  		int num = dec;
  		String tmp = intToHex(dec); // convert to hexadecimal string

  		if (tmp.length()>2)
  		{
  			tmp = tmp.substring(tmp.length()-2);
  			num = hexToInt(tmp);
  		}

  		return num;
  	}
}
