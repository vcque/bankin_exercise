package bridge.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Json mapping to a bridge transation response. We map only the fields we need.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BridgeTransactions {

	public List<Resource> resources = new ArrayList<>(0);

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	/** Actually a transaction. */
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Resource {

		public String id;

		public String description;

		@JsonProperty("is_deleted")
		public boolean isDeleted;

		// We would prefer to use BigDecimal for representing money
		public double amount;

		// Better use localdate
		public String date;

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

		public boolean isDeleted() {
			return isDeleted;
		}

		public void setDeleted(boolean isDeleted) {
			this.isDeleted = isDeleted;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		@Override
		public String toString() {
			return "Resource [id=" + id + ", description=" + description + ", isDeleted=" + isDeleted + ", amount="
					+ amount + "]";
		}

	}

	@Override
	public String toString() {
		return "BridgeTransactions [resources=" + resources + "]";
	}

}
