package hawhh.ttv.meth.schiffeversenken.gamelogic;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 * Singleton Helper for CoAP communication with LED. 
 * @author Timo Haeckel
 *
 */
public class CoapLEDHelper {
	
	private CoapLEDHelper() {
		client = new CoapClient();
	}
	
	/**
	 * Get singleton instance
	 * @return the singleton
	 */
	public static CoapLEDHelper getInstance (){
		if (instance == null){
			//if not yet created, create an instance
			instance = new CoapLEDHelper();
		}
		return instance;
	}
	
	/**
	 * Singleton instance
	 */
	private static CoapLEDHelper instance;
	
	/**
	 * Server URI.
	 */
	private String uri = "";
	private CoapClient client;
	
	/**
	 * Colors to set rgb LED
	 * @author Timo Haeckel
	 *
	 */
	public enum Color{
		RED, GREEN, BLUE, PINK
	}
	
	/**
	 * Set the URI of the CoAP client
	 * @param uri the uri to set
	 */
	public void setURI(String uri){
		this.uri = uri;
		client.setURI(uri);
	}
	
	/**
	 * Set the LED Color
	 * @param color
	 */
	public void setLED(Color color){
		if(uri.equals("")){
			//if no URI set, don't even bother trying 
			return;
		}
		
		//reset LED
		client.put("0", MediaTypeRegistry.TEXT_PLAIN);
		
		//set the wanted color
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
