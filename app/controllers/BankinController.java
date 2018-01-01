package controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.ObjectMapper;

import bridge.BridgeApi;
import bridge.entities.BridgeError;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import services.ArrondiService;

/** The Bankin main controller. */
@Singleton
public class BankinController extends Controller {

	/*
	 * Might put these static methods in a helper somewhere.
	 */
	
	private static Result handleError(Throwable e) {
		if (e instanceof BridgeError) {
			final BridgeError be = (BridgeError) e;
			return Results.status(be.getStatus(), be.getBody());
		} else {
			// better than nothing
			e.printStackTrace();
			return Results.internalServerError();
		}
	}

	private static String toJson(Object result) {
		try {
			return new ObjectMapper().writeValueAsString(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Inject private BridgeApi bridgeApi;
	@Inject private ArrondiService arrondiService;
	
	public CompletionStage<Result> list(String user, String password) {
		return bridgeApi
				.authenticate(user, password)
				.thenComposeAsync(bridgeApi::transactions)
				.thenApply(arrondiService::computeArrondis)
				.thenApply(BankinController::toJson)
				.thenApply(Results::ok)
				.exceptionally(BankinController::handleError);
	}

	public CompletionStage<Result> stats(String user, String password, String since, String until) {
		final LocalDate sinceDate = LocalDate.parse(since, DateTimeFormatter.ISO_DATE);
		final LocalDate untilDate = LocalDate.parse(until, DateTimeFormatter.ISO_DATE);
		// Still have to check how to handle error gracefully with async flow
		return bridgeApi
				.authenticate(user, password)
				.thenComposeAsync(auth -> bridgeApi.transactions(auth, sinceDate, untilDate))
				.thenApply(arrondiService::aggregateStats)
				.thenApply(BankinController::toJson)
				.thenApply(Results::ok)
				.exceptionally(BankinController::handleError);
	}

}
