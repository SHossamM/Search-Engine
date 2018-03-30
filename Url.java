import java.io.IOException;
import java.net.URL;

/**
 *
 * @author Sahar
 */
public class Url {

	private int id;
	private String url;
        private java.sql.Timestamp  date;
        private int Rank;
        private int Hash;

	public Url(int id, String url,java.sql.Timestamp  date,int Rank){
		this.id = id;
		this.url = url;
                this.date=date;
                this.Rank=Rank;
                this.Hash=-1;
	}

	public int getId() {
		return id;
	}
      
	public String getUrl() {
		return url;
	}
        public java.sql.Timestamp  getDate()
        {
            return date;
        }
        public int getRank()
        {
            return Rank;
        }
        public int getHash()
        {
            return Hash;
        }
	public void setUrl(String url) {
		this.url = url;
	}
        public void setHash(int Hash)
        {
            this.Hash=Hash;
        }


	/**
	 * This methods verifies that the given url is an http url if the
	 * url is malformed exception is thrown and a null is returned if the url is not valid or verified give MalformedURLException: no protocol:
	 *
	 * @param url
	 * @return
	 */

	public static URL verifyUrl(String url) {
		// Only allow HTTP URLs.
		if (url.isEmpty() || !(url.toLowerCase().startsWith("http://") || url.toLowerCase().startsWith("https://"))) {
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

	public URL verifyUrl(){
		return verifyUrl(url);
	}

	/**
	 * Opens a buffered stream on the url and copies the contents to OutputStream
	 *
	 * @param url
	 * @param os
	 * @throws IOException
	 */
}
