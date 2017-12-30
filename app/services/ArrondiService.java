package services;

import java.util.stream.Collectors;

import bridge.entities.Transactions;
import bridge.entities.Transactions.Resource;
import entities.ArrondiAggregation;
import entities.Arrondis;

public class ArrondiService {

	public Arrondis computeArrondis(Transactions transactions) {
		final Arrondis result = new Arrondis();
		result.transactions = transactions.resources.stream()
				.filter(this::hasArrondi)
				.map(resource -> {
					final Arrondis.Transaction transaction = new Arrondis.Transaction();
					// Mostly boilerplate
					transaction.id = resource.id;
					transaction.amount = resource.amount;
					transaction.description = resource.description;
					transaction.arrondi = computeArrondi(resource);
					return transaction;
				})
				.collect(Collectors.toList());
		
		return result;
	}

	public ArrondiAggregation aggregateStats(Transactions transactions) {
		final ArrondiAggregation result = new ArrondiAggregation();
		
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
		return 10 - resource.amount % 10;
	}

	/** Checks if an arrondi can be computed from this resource. */
	private boolean hasArrondi(Resource resource) {
		return !resource.isDeleted && resource.amount > 0;
	}
}
