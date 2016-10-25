import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class VK {

	private static String APP_ID = "5684526";
	private static String SCOPE = "262146";
	
	private static String token = "b4ed90f696cad21295de69ea52086ac22c676db44036fb734e61685371585a5dea30e4e438da04f2f4f95";
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		/*try {
            Desktop.getDesktop().browse(new URL(getRequest()).toURI());
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
		
		Scanner inConsole = new Scanner(System.in);
		token = inConsole.nextLine();
		*/
		
	    final StringBuilder result = new StringBuilder();
        final URL url = new URL(getFriends(token));
        try (InputStream is = url.openStream()) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            reader.lines().forEach(result::append);
        }
        System.out.println(result.toString());
	          
	}

	
	private static String getFriends(String token) {
		return "https://api.vk.com/method/friends.get?"
				//+ "fields=nickname"
				+ "&fields=schools"				
				+ "&access_token=" + token; 
	}
	
	
	private static String getRequest() {
			return "https://oauth.vk.com/authorize?" +
				   "client_id=" + APP_ID +
				   "&redirect_uri=" + "https://oauth.vk.com/blank.html" +
				   "&scope=" + SCOPE +
				   "&display=" + "page" +
				   "&response_type=" + "token";
	}
	
}