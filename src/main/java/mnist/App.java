package mnist;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import org.utilities.core.lang.exception.QuietException;
import org.utilities.io.UtilitiesIO;
import org.utilities.io.gz.EntryGZip;
import org.utilities.io.gz.UtilitiesGZip;

public class App {

	public static void main(String[] args) throws IOException {
		Path src = Paths.get("C:/data/ML databases/MNIST");

		src.resolve("img")
				.toFile()
				.mkdirs();

		File trainImages = src.resolve("train-images-idx3-ubyte.gz")
				.toFile();

		EntryGZip<File> trainImagesGZ = UtilitiesGZip.newEntryGZip(trainImages);
		GZIPInputStream inputStream = trainImagesGZ.getContent();

		readInt(inputStream, 4);
		int imgAmount = readInt(inputStream, 4);
		int width = readInt(inputStream, 4);
		int height = readInt(inputStream, 4);

		for (int k = 0; k < imgAmount; k++) {
			int[][] pixels = readPixels(inputStream, width, height);
			BufferedImage img = parseImage(pixels);
			File output = src.resolve("img")
					.resolve(String.format("%05d", k) + ".png")
					.toFile();
			ImageIO.write(img, UtilitiesIO.extension(output), output);
		}

		inputStream.close();

	}

	private static BufferedImage parseImage(int[][] pixels) {
		BufferedImage img = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < pixels.length; i++) {
			for (int j = 0; j < pixels[0].length; j++) {
				int value = pixels[i][j];
				Color color = new Color(255 - value, 255 - value, 255 - value);
				img.setRGB(i, j, color.getRGB());
			}
		}
		return img;
	}

	private static int[][] readPixels(GZIPInputStream inputStream, int width, int height) throws IOException {
		int[][] img = new int[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				img[j][i] = inputStream.read();
			}
		}
		return img;
	}

	private static int readInt(GZIPInputStream inputStream, int bytes) {
		try {
			byte[] buffer = new byte[bytes];
			inputStream.read(buffer, 0, buffer.length);
			return ByteBuffer.wrap(buffer)
					.asIntBuffer()
					.get();
		} catch (IOException e) {
			throw new QuietException(e);
		}
	}

}
