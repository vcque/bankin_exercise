package services;

import java.util.stream.Collectors;

import bridge.entities.BridgeTransactions;
import bridge.entities.BridgeTransactions.Resource;
import entities.ArrondisStats;
import entities.Arrondis;

/** Base service for computing/manipulating arrondis. Transactions with */
public class ArrondiService {

	/** Compute The arrondis of a batch of transactions transactions. */
	public Arrondis computeArrondis(BridgeTransactions transactions) {
		final Arrondis result = new Arrondis();
		result.transactions = transactions.resources.stream()
				.filter(this::hasArrondi)
				.map(resource -> {
					final Arrondis.Transaction transaction = new Arrondis.Transaction();
					// Mostly boilerplate
					transaction.id = resource.id;
					transaction.amount = resource.amount;
					transaction.description = resource.description;
					transaction.date = resource.date;
					transaction.arrondi = computeArrondi(resource);
					return transaction;
				})
				.collect(Collectors.toList());
		
		return result;
	}

	/** Compute some aggregations stats from a batch of transactions. */
	public ArrondisStats aggregateStats(BridgeTransactions transactions) {
		final ArrondisStats result = new ArrondisStats();
		
		result.sum = transactions.resources.stream()
				.filter(this::hasArrondi)
				.mapToDouble(this::computeArrondi)
				.sum();

		result.average = transactions.resources.stream()
				.filter(this::hasArrondi)
				.mapToDouble(this::computeArrondi)
				.average().orElse(0d);

		result.count = transactions.resources.stream()
				.filter(this::hasArrondi)
				.count();

		return result;
	}

	/** Base computation of an arrondi. */
	private double computeArrondi(Resource resource) {
		return (10 - Math.abs(resource.amount) % 10) % 10;
	}

	/** Checks if an arrondi can be computed from this resource. */
	private boolean hasArrondi(Resource resource) {
		return !resource.isDeleted;
	}
}
