

import java.awt.Robot;
import java.net.*;
import java.io.*;

public class SearchEngine {
    public static void main(String[] args) throws Exception {

        URL oracle = new URL("https://www.codeproject.com/Questions/398241/how-to-open-url-in-java");
       String s="https://www.codeproject.com/Questions/398241/how-to-open-url-in-java";
        Robot.isRobotAllowed(s);
        writeURLtoFile(oracle, "hey1.txt");
    }
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