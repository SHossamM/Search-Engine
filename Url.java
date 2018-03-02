
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sahar
 */
public class Url {
 /**
     * This methods verifies that the given url is an http url if the
     * url is malformed exception is thrown and a null is returned if the url is not valid or verified
     * @param url
     * @return 
     */
   public static URL verifyUrl(String url) {
// Only allow HTTP URLs.
if (!(url.toLowerCase().startsWith("http://")))
{
    if(!(url.toLowerCase().startsWith("https://")))
       return null;
}
// Verify format of URL.
URL verifiedUrl = null;
try {
verifiedUrl = new URL(url);
} catch (Exception e) {
return null;
}
return verifiedUrl;
}
 /**
  * Opens a buffered stream on the url and copies the contents to OutputStream
  * @param url
  * @param os
  * @throws IOException 
  */   
     public static void saveURL(URL url, OutputStream os)throws IOException {
		InputStream in = url.openStream();
		byte[] buf = new byte[1048576];
		int n = in.read(buf);
                //byte[] testbytes=test.getBytes();
                // os.write(testbytes,0,3);
                byte[] urlBytes=((url.toString())+System.lineSeparator()).getBytes(); //change url string to bytes
                int n1=urlBytes.length;
                os.write(urlBytes,0,n1); 
                
		while (n != -1) {
			os.write(buf, 0, n);
			n = in.read(buf);
		}
	}
 /**
	 * Writes the contents of the url to a new file by calling saveURL with
	 * a file writer as argument
     * @param url
     * @param filename
     * @throws java.io.IOException
	 */
        public static void writeURLtoFile(URL url, String filename)
		throws IOException {
		
		FileOutputStream os = new FileOutputStream(filename);
		saveURL(url, os);
		os.close();
	}
}
