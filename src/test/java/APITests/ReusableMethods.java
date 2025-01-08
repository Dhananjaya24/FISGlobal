package APITests;

import io.restassured.path.json.JsonPath;

//All the Reusable methods can added here

public class ReusableMethods {
	
	

	public static JsonPath rawToJson(String response) {
		// TODO Auto-generated method stub
		JsonPath js = new JsonPath(response);
		return js;
		
	}

}
