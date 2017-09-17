package mnist;

public class MNISTDataPair {

	private int id;
	private MNISTLabel label;
	private MNISTImage image;

	public MNISTDataPair(int id, MNISTLabel label, MNISTImage image) {
		this.id = id;
		this.label = label;
		this.image = image;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MNISTLabel getLabel() {
		return label;
	}

	public void setLabel(MNISTLabel label) {
		this.label = label;
	}

	public MNISTImage getImage() {
		return image;
	}

	public void setImage(MNISTImage image) {
		this.image = image;
	}

	public double[][] parseVector() {
		return new double[][] { image.parseVector(), label.parseVector() };
	}

	@Override
	public String toString() {
		return "MNISTDataPair [id=" + id + ", label=" + label + ", image=" + image + "]";
	}

}
