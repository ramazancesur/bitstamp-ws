package net.parasec.trading.bitstampws;

import net.parasec.trading.bitstampws.websocket.Command;
import net.parasec.trading.bitstampws.websocket.Message;
import net.parasec.trading.bitstampws.websocket.WSDecoder;
import net.parasec.trading.bitstampws.websocket.WSEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Client {

	private final Logger logger = LogManager.getLogger(Client.class);


	public Client() throws URISyntaxException, IOException, DeploymentException {

		WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();

		@ClientEndpoint(encoders = { WSEncoder.class },
				decoders = { WSDecoder.class })
		final class MyEndpoint extends Endpoint {
			//Endpoint endpoint = new Endpoint() {
			@Override
			public void onOpen(Session session, EndpointConfig endpointConfig) {
				System.out.println("Websocket connected");
				session.addMessageHandler(new MessageHandler.Whole<Message>() {

					public void onMessage(Message s) {

						System.out.println(s);
					}
				});
				try {

					RemoteEndpoint.Basic basicRemoteEndpoint = session.getBasicRemote();
					if(basicRemoteEndpoint != null) {
						basicRemoteEndpoint.sendObject(new Command());
						//basicRemoteEndpoint.sendText("{\"event\": \"bts:subscribe\", \"data\": { \"channel\": \"live_orders_btcusd\" }}");
					}
				} catch(EncodeException ee) {
					ee.printStackTrace();
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}

		Endpoint endpoint = new MyEndpoint();
		ClientEndpointConfig clientEndpointConfig = ClientEndpointConfig.Builder.create()
				.encoders(Arrays.<Class<? extends Encoder>>asList(WSEncoder.class))
				.decoders(Arrays.<Class<? extends Decoder>>asList(WSDecoder.class)).build();
		Session session = webSocketContainer.connectToServer(endpoint, clientEndpointConfig, new URI("wss://ws.bitstamp.net"));

		while (session.isOpen()) {
			System.out.println("Waiting");
			try {
				TimeUnit.MILLISECONDS.sleep(10);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}