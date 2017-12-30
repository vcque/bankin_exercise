package bridge.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Json mapping to a bridge transation response. We map only the fields we need.
 */
public class Transactions {

	public List<Resource> resources = new ArrayList<>(0);

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	/** Actually a transaction. */
	public static class Resource {

		public String id;

		public String description;

		@JsonProperty("is_deleted")
		public boolean isDeleted;

		// We would prefer to use BidDecimal for representing money
		public double amount;

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
	}
}
