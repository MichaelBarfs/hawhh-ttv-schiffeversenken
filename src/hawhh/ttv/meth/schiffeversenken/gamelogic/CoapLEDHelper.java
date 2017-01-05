package hawhh.ttv.meth.schiffeversenken.gamelogic;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.network.CoapEndpoint;

public class CoapLEDHelper {
	
	private CoapLEDHelper() {
		//endpoint = new CoapEndpoint(17000);
		client = new CoapClient();
		//client.setEndpoint(endpoint);
	}
	
	public static CoapLEDHelper getInstance (){
		if (instance == null){
			instance = new CoapLEDHelper();
		}
		return instance;
	}
	
	private static CoapLEDHelper instance;
	
	private String uri = "";
	private CoapClient client;
	//private CoapEndpoint endpoint;
	
	public enum Color{
		RED, GREEN, BLUE, PINK
	}
	
	public void setURI(String uri){
		this.uri = uri;
		client.setURI(uri);
	}
	
	public void setLED(Color color){
		if(uri.equals("")){
			return;
		}
		
		client.put("0", MediaTypeRegistry.TEXT_PLAIN);
		switch(color){
		case RED:
			client.put("r", MediaTypeRegistry.TEXT_PLAIN);
			break;
		case BLUE:
			client.put("b", MediaTypeRegistry.TEXT_PLAIN);
			break;
		case GREEN:
			client.put("g", MediaTypeRegistry.TEXT_PLAIN);
			break;
		case PINK:
			client.put("r", MediaTypeRegistry.TEXT_PLAIN);
			client.put("b", MediaTypeRegistry.TEXT_PLAIN);
			break;
		default: 
			client.put("0", MediaTypeRegistry.TEXT_PLAIN);
			break;				
		}
	}
}
