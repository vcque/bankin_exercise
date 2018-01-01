package bridge;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

import bridge.entities.BridgeError;
import bridge.entities.BridgeLoginInfo;
import bridge.entities.BridgeTransactions;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Http.Status;

/** Handle calls to the bridge api. */
public class BridgeApi {

	private static final String BRIDGE_PATH = "https://sync.bankin.com/v2/";
	private static final String BRIDGE_VERSION = "2016-01-18";

	@Inject
	private WSClient ws;

	/**
	 * Private secret key for this test credentials. It shouldn't be stored in
	 * plaintext but as it is test credentials, we don't mind.
	 */	
	private static final String CLIENT_SECRET = "sMgdYUzUPpo1DxbR67qP2ZbuTmU7H9gikvWPigDnQro9fk0PsRcb4EvI0iRheAJr";

	/** Public client key for this test credentials. */
	private static final String CLIENT_ID = "775683bc70d94beaa8044c81b2f16006";

	public CompletionStage<BridgeLoginInfo> authenticate(String user, String password) {
		return request("authenticate")
			.setQueryParameter("email", user)
			.setQueryParameter("password", password)
			.execute("POST")
			.thenApply(response -> convertTo(response, BridgeLoginInfo.class));

	}
	
	public CompletionStage<BridgeTransactions> transactions(BridgeLoginInfo loginInfo) {
		return request("transactions")
			.setHeader("Authorization", "Bearer " + loginInfo.getAccessToken())
			.execute("GET")
			.thenApply(response -> convertTo(response, BridgeTransactions.class));
	}
	
	public CompletionStage<BridgeTransactions> transactions(BridgeLoginInfo loginInfo, LocalDate since, LocalDate until) {
		return request("transactions")
			.setQueryParameter("since", DateTimeFormatter.ISO_DATE.format(since))
			.setQueryParameter("until", DateTimeFormatter.ISO_DATE.format(until))
			.setHeader("Authorization", "Bearer " + loginInfo.getAccessToken())
			.execute("GET")
			.thenApply(response -> convertTo(response, BridgeTransactions.class));
	}

	/** Generic treatment of the bridge api results. */
	private <T> T convertTo(WSResponse response, Class<T> clazz) {
		if (response.getStatus() == Status.OK) {
			try {
				final T result = new ObjectMapper().readValue(response.getBody(), clazz);
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				final BridgeError error = new BridgeError();
				error.setBody(e.getMessage());
				error.setStatus(500);
				throw error;
			}
		} else {
			final BridgeError error = new BridgeError();
			error.setBody(response.getBody());
			error.setStatus(response.getStatus());
			throw error;
		}
	}
	
	/** Helper for building the request with prerequisites headers/params. */
	private WSRequest request(String path) {
		return ws
		    .url(BRIDGE_PATH + path)
		    .setHeader("Bankin-Version", BRIDGE_VERSION)
			.setQueryParameter("client_id", CLIENT_ID)
			.setQueryParameter("client_secret", CLIENT_SECRET);
				
	}
}
