package controllers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.asynchttpclient.DefaultAsyncHttpClientConfig;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import play.libs.ws.WSAuthScheme;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import play.libs.ws.ahc.AhcWSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import scala.concurrent.ExecutionContextExecutor;
import scala.concurrent.duration.Duration;

/** Then Bankin main endpoint. */
@Singleton
public class BankinController extends Controller {

	/** Public client key for this test credentials. */
	private static final String CLIENT_ID = "sMgdYUzUPpo1DxbR67qP2ZbuTmU7H9gikvWPigDnQro9fk0PsRcb4EvI0iRheAJr";
	/**
	 * Private secret key for this test credentials. It shouldn't be stored in
	 * plaintext but as it is test credentials, we don't mind.
	 */
	private static final String CLIENT_SECRET = "775683bc70d94beaa8044c81b2f16006";

	@Inject private ActorSystem actorSystem;
	@Inject private ExecutionContextExecutor exec;
	@Inject private ActorMaterializer materializer;

    public CompletionStage<Result> list(String user, String password) {
		final WSClient ws = new AhcWSClient(new DefaultAsyncHttpClientConfig.Builder().build(), materializer);
		return ws
		    .url("")
		    .setQueryParameter("client_id", CLIENT_ID)
		    .setQueryParameter("client_secret", CLIENT_SECRET)
		    .setAuth(user, password, WSAuthScheme.BASIC)
		    .execute("GET")
		    .thenApplyAsync(this::toList, exec);
		}
	
	public CompletionStage<Result> stats(String user, String password) {
		final WSClient ws = new AhcWSClient(new DefaultAsyncHttpClientConfig.Builder().build(), materializer);
		return ws
		    .url("")
		    .setQueryParameter("client_id", CLIENT_ID)
		    .setQueryParameter("client_secret", CLIENT_SECRET)
		    .setAuth(user, password, WSAuthScheme.BASIC)
		    .execute("GET")
		    .thenApplyAsync(this::toStats, exec);
    }

	private Result toLists(WSResponse fromBridge) {
		if (fromBridge.getStatus() >= 400) {
			return Results.status(fromBridge.getStatus(), fromBridge.getBody());
		}
		return Results.status(fromBridge.getStatus(), fromBridge.getBody());
	}
	
	private Result toStats(WSResponse fromBridge) {
		return Results.status(fromBridge.getStatus(), fromBridge.getBody());
	}
	
    private CompletionStage<String> getFutureMessage(long time, TimeUnit timeUnit) {
        CompletableFuture<String> future = new CompletableFuture<>();
        actorSystem.scheduler().scheduleOnce(
            Duration.create(time, timeUnit),
            () -> future.complete("Hi!"),
            exec
        );
        return future;
    }

}
