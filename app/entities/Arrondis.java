package entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Json mapping to the /api/bankin/arrondis response. We use the "arrondi"
 * instead of "round" because it refers to larrondi.org.
 */
public class Arrondis {

	public List<Transaction> transactions = new ArrayList<>(0);

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	/** Actually a transaction. */
	public static class Transaction {

		public String id;

		public String description;

		@JsonProperty("is_deleted")
		public boolean isDeleted;

		// We would prefer to use BidDecimal for representing money
		public double amount;

		public double arrondi;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public double getAmount() {
			return amount;
		}

		public void setAmount(double amount) {
			this.amount = amount;
		}

		public double getArrondi() {
			return arrondi;
		}

		public void setArrondi(double arrondi) {
			this.arrondi = arrondi;
		}

		public boolean isDeleted() {
			return isDeleted;
		}

		public void setDeleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
		}

	}
}
