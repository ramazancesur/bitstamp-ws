package net.parasec.trading.bitstampws.websocket;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.runtime.Settings;
import net.parasec.trading.bitstampws.DetailOrderBookEvent;

import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class DetailOrderBookDecoder implements Decoder.Text<DetailOrderBookEvent> {

	private DslJson<Object> dslJson
			= new DslJson<Object>(Settings.withRuntime().allowArrayFormat(true).includeServiceLoader());

	public DetailOrderBookEvent decode(String s) {
		try {
			byte[] bytes = s.getBytes("UTF-8");
			return dslJson.deserialize(DetailOrderBookEvent.class, bytes, bytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean willDecode(String s) {
		return true;
	}

	public void init(EndpointConfig endpointConfig) {

	}

	public void destroy() {

	}
}
