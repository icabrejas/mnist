package mnist;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;

import org.utilities.core.lang.iterable.IterablePipe;
import org.utilities.core.lang.iterable.IterablePipePair;
import org.utilities.core.util.lambda.LambdaInt;

public class MNISTDataSet implements IterablePipe<MNISTDataPair> {

	private static final String TRAIN_IMAGES = "train-images-idx3-ubyte.gz";
	private static final String TRAIN_LABELS = "train-labels-idx1-ubyte.gz";

	private static final String TEST_IMAGES = "t10k-images-idx3-ubyte.gz";
	private static final String TEST_LABELS = "t10k-labels-idx1-ubyte.gz";

	private MNISTImages images;
	private MNISTLabels labels;

	public MNISTDataSet(MNISTImages images, MNISTLabels labels) {
		this.images = images;
		this.labels = labels;
	}

	public static MNISTDataSet newInstance(File images, File labels) {
		return new MNISTDataSet(MNISTImages.newInstance(images), MNISTLabels.newInstance(labels));
	}

	public static MNISTDataSet train(Path src) {
		File images = src.resolve(TRAIN_IMAGES)
				.toFile();
		File labels = src.resolve(TRAIN_LABELS)
				.toFile();
		return MNISTDataSet.newInstance(images, labels);
	}

	public static MNISTDataSet test(Path src) {
		File images = src.resolve(TEST_IMAGES)
				.toFile();
		File labels = src.resolve(TEST_LABELS)
				.toFile();
		return MNISTDataSet.newInstance(images, labels);
	}

	@Override
	public Iterator<MNISTDataPair> iterator() {
		LambdaInt id = new LambdaInt(0);
		return IterablePipePair.newInstance(labels, images)
				.map(entry -> new MNISTDataPair(id.add(1), entry.getInfo(), entry.getContent()))
				.iterator();
	}

}
