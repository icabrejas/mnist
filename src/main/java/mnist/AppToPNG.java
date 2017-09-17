package mnist;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.utilities.core.lang.exception.QuietException;
import org.utilities.io.UtilitiesIO;

public class AppToPNG {

	private static final Path SRC = Paths.get("C:/data/ML databases/MNIST");
	private static final String TRAIN = "train";
	private static final String TEST = "test";

	public static void main(String[] args) throws IOException {
		MNISTDataSet.train(SRC)
				.limit(100)
				.forEach(dataPair -> AppToPNG.write(SRC, TRAIN, dataPair));
		MNISTDataSet.test(SRC)
				.limit(100)
				.forEach(dataPair -> AppToPNG.write(SRC, TEST, dataPair));
	}

	public static void write(Path src, String dataSet, MNISTDataPair dataPair) {
		BufferedImage image = dataPair.getImage()
				.parseImage();
		File file = getFile(src, dataSet, dataPair);
		writeQuietly(image, file);
	}

	public static File getFile(Path src, String dataSet, MNISTDataPair dataPair) {
		int id = dataPair.getId();
		int number = dataPair.getLabel()
				.getNumber();
		return src.resolve("img")
				.resolve(dataSet)
				.resolve(String.format("%05d", id) + "-" + number + ".png")
				.toFile();
	}

	public static void writeQuietly(BufferedImage image, File file) throws QuietException {
		try {
			file.getParentFile()
					.mkdirs();
			ImageIO.write(image, UtilitiesIO.extension(file), file);
		} catch (IOException e) {
			throw new QuietException(e);
		}
	}

}
