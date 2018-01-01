package entities;

/** Json mapping to the . We map only the fields we need. */
public class ArrondisStats {

	public double sum;

	public long count;

	public double average;

	public double getSum() {
		return sum;
	}

	public void setSum(double sum) {
		this.sum = sum;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public double getAverage() {
		return average;
	}

	public void setAverage(double average) {
		this.average = average;
	}

	@Override
	public String toString() {
		return "ArrondisStats [sum=" + sum + ", count=" + count + ", average=" + average + "]";
	}

}
