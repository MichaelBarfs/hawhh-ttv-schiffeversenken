package hawhh.ttv.meth.schiffeversenken;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import hawhh.ttv.meth.schiffeversenken.gamelogic.CoapLEDHelper;
import hawhh.ttv.meth.schiffeversenken.gamelogic.StartUp;
import hawhh.ttv.meth.schiffeversenken.gamelogic.CoapLEDHelper.Color;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField localAddress = new JTextField("localhost");
	private JTextField localPort = new JTextField("9001");
	private JTextField serverAddress = new JTextField("localhost");
	private JTextField serverPort = new JTextField("9000");
	private JTextField nodeCount = new JTextField("1");
	private JCheckBox isServer = new JCheckBox();
	private JTextField coapLEDURI = new JTextField("coap://localhost:5683/led/");
	
	private JLabel localAddressL = new JLabel("Local Address: ");
	private JLabel localPortL = new JLabel("Local Port: ");
	private JLabel serverAddressL = new JLabel("Server Address: ");
	private JLabel serverPortL = new JLabel("Server Port: ");
	private JLabel nodeCountL = new JLabel("Node Count: ");
	private JLabel isServerL = new JLabel("Is Server: ");
	private JLabel coapLEDURIL = new JLabel("Coap LED URI: ");
	
	private CoapLEDHelper coapHelper;
	
	public GUI() {

    	coapHelper = CoapLEDHelper.getInstance();
		
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	
		GridLayout layout = new GridLayout(8, 2);

	    JPanel panel = new JPanel(layout);
	    
	    panel.add("localAddressL", localAddressL);
	    panel.add("localAddress", localAddress);
		
	    panel.add("localPortL", localPortL);
	    panel.add("localPort", localPort);
		
	    panel.add("serverAddressL", serverAddressL);
	    panel.add("serverAddress", serverAddress);
		
		panel.add("serverPortL", serverPortL);
		panel.add("serverPort", serverPort);
		
		panel.add("nodeCountL", nodeCountL);
		panel.add("nodeCount", nodeCount);
		
		panel.add("isServerL", isServerL);
		panel.add("isServer", isServer);
		
		panel.add("coapLEDURIL", coapLEDURIL);
		panel.add("coapLEDURI", coapLEDURI);
		
		JButton go = new JButton("go");
		panel.add("go", go);

	    //Add action listener to button
	    go.addActionListener(new ActionListener() {

		    public void actionPerformed(ActionEvent e)
		    {
		        //Perform function when button is pressed
				coapHelper.setURI(coapLEDURI.getText());
				coapHelper.setLED(Color.RED);
		    	StartUp starter;
				try {
					starter = new StartUp(localAddress.getText(), localPort.getText(), serverAddress.getText(), serverPort.getText(), Integer.parseInt(nodeCount.getText()), isServer.isSelected());

					String go = "";
					while(!go.equals("go")){
						go = JOptionPane.showInputDialog("Type go to start");
					}
					coapHelper.setLED(Color.GREEN);
					starter.startGame();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		    }
	    });
	    
	    this.setLocation(1000, 500);
	    this.add(panel);
	    this.pack();
	    this.setVisible(true);
	    
	}

}
