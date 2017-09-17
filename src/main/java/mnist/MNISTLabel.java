package mnist;

public class MNISTLabel {

	private int number;

	public MNISTLabel(int number) {
		this.number = number;
	}

	public double[] parseVector() {
		double[] values = new double[10];
		values[number] = 1;
		return values;
	}

	public int getNumber() {
		return number;
	}

	@Override
	public String toString() {
		return "MNISTLabel [number=" + number + "]";
	}
}
