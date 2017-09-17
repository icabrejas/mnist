package mnist;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class MNISTImage {

	private int width;
	private int height;
	private int[] pixels;

	public MNISTImage(int width, int height, int[] pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
		if (pixels.length != width * height) {
			throw new Error();
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int get(int i, int j) {
		return pixels[i + j * height];
	}

	public double[] parseVector() {
		double[] values = new double[pixels.length];
		for (int i = 0; i < values.length; i++) {
			values[i] = (double) pixels[i] / 255;
		}
		return values;
	}

	public BufferedImage parseImage() {
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int value = get(i, j);
				Color color = new Color(255 - value, 255 - value, 255 - value);
				img.setRGB(i, j, color.getRGB());
			}
		}
		return img;
	}

	@Override
	public String toString() {
		return "MNISTImage [width=" + width + ", height=" + height + ", pixels=" + Arrays.toString(pixels) + "]";
	}

}
