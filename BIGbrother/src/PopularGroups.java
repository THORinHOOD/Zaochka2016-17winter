//Ахтунг!!! Бесполезные комментарии!!!

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PopularGroups {	
	private static HashMap<String, Long> groups;
	
	private static String token;
	private static String APP_ID = "5684526";
	private static String SCOPE = "262146";
	
	
	// запрос access token
	private static String getToken = 
			"https://oauth.vk.com/authorize?"
			+ "client_id=" + APP_ID
			+ "&scope=" + SCOPE
			+ "&display=wap"
			+ "&response_type=token";
	
	
	public static void main(String[] args) throws MalformedURLException, IOException{
		
		groups = new HashMap<String, Long>();
		
		Scanner in = new Scanner(System.in);
		System.out.println("Введите id группы: ");
		String GROUP_ID = in.nextLine();
		
		
	    
	    try {
	       Desktop.getDesktop().browse(new URL(getToken).toURI());
	    } catch (URISyntaxException ex) {
	       throw new IOException(ex);
	    }
	    System.out.println("Введите токен: ");
		token = in.nextLine();
	    
		ArrayList<String> ids = getIDOfMemebers(request(getMembersOfGroup(GROUP_ID)));	
	    		
		
		
		// получение групп каждого подписчика Слона и добавление этих групп (что сказал? (а как сказать лучше?))
		int count = 0;
	    long done = 0;
		for (String id : ids) {
	    	String response  = request(getGroupsOfUser(id, token));
	    	if (!isbanned(response)) {
	    		addGroupsOfUser(response);
	    		count++;
	    		done++;
	    		if (count >= 3) {
	    			count = 0;
	    			try {
	    				//потому что у вк есть ограничения
	    				Thread.sleep(2000);
	    			} catch (InterruptedException e) {
	    				e.printStackTrace();
	    			}
	    		}
	    	}
	    	System.out.println(done + "/" + ids.size());
	    }
		
	    
	    ArrayList<Group> ar_groups = new ArrayList<Group>();
	    for (String group : groups.keySet()) {
	    	ar_groups.add(new Group(group, groups.get(group)));
	    }
	    
	    Collections.sort(ar_groups);
		
	    for (int i = 0; i < Math.min(25, ar_groups.size()); i++) {
	    	System.out.println(ar_groups.get(i).getName() + " : " + ar_groups.get(i).getCount());
	    }
	    
	}

	//проверка на то, что пользователь был забанен
	private static boolean isbanned(String response){
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(response);
			JSONObject responseJSON = (JSONObject)obj;
			if (responseJSON.containsKey("error")) {
				JSONObject error = (JSONObject) responseJSON.get("error");
				if (Long.compare(((long)error.get("error_code")), 18) == 0) {
					return true;
				} else {
					return false;
				}
				
			} else {
				return false;
			}
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	//добавление групп пользователя
	private static void addGroupsOfUser(String response) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(response);
			JSONObject responseJSON = (JSONObject)obj;
			JSONArray resp = (JSONArray) responseJSON.get("response");
			
			for (int i = 1; i < resp.size(); i++) {
				JSONObject groupJSON = (JSONObject) resp.get(i);
				String name = (String) groupJSON.get("name");
				addGroup(name);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}		
	}
	
	//обавление одной группы
	private static void addGroup(String name) {
		if (groups.containsKey(name)) {
			groups.put(name, groups.get(name) + 1);
		} else {
			groups.put(name, (long) 1);
		}
	}
	
	//запрос к вк
	private static String request(String link) throws IOException {
		final StringBuilder result = new StringBuilder();
	    final URL url = new URL(link);
	    try (InputStream is = url.openStream()) {
	         BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
	         reader.lines().forEach(result::append);
	    }
	    return result.toString();
	}
	
	//получение подписчиков 
	private static ArrayList<String> getIDOfMemebers(String response) {
		ArrayList<String> ids = new ArrayList<String>();
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(response);
			JSONObject responseJSON = (JSONObject)obj;
			JSONObject resp = (JSONObject) responseJSON.get("response");
			JSONArray idsJSON = (JSONArray) resp.get("users");
			for (int i = 0; i < idsJSON.size(); i++) {
		        ids.add(Long.toString((long) idsJSON.get(i)));
			}	
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ids;
	}
	
	private static String getMembersOfGroup(String GROUP_ID) {
		return	"https://api.vk.com/method/groups.getMembers?"
				+ "&group_id=" + GROUP_ID;
	}
	
	private static String getGroupsOfUser(String user_id, String token) {
		return "https://api.vk.com/method/groups.get?"
				+ "&user_id=" + user_id 
				+ "&extended=1"
				
				+ "&access_token=" + token;
	}	
}


class Group implements Comparable<Group>{
	private String name;
	private Long count;
	
	public Group(String name, Long count) {
		this.name = name;
		this.count = count;
	}
	
	public String getName() {
		return name;
	}
	
	public Long getCount() {
		return count;
	}

	@Override
	public int compareTo(Group arg0) {
		return Long.compare(arg0.getCount(), count);
	}
}