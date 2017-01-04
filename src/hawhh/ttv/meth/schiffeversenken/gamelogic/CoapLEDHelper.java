package hawhh.ttv.meth.schiffeversenken.gamelogic;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class CoapLEDHelper {
	
	private String uri;
	CoapClient client = new CoapClient();
	
	public enum Color{
		RED, GREEN, BLUE, PINK
	}
	
	public CoapLEDHelper(String uri) {
		this.uri = uri;
		client.setURI(uri);
	}
	
	public void setLED(Color color){
		
		
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
