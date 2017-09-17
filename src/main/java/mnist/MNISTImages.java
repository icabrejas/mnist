package mnist;

import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Supplier;

import org.utilities.core.lang.iterable.IterablePipe;
import org.utilities.core.util.function.FunctionPlus;
import org.utilities.io.IteratorCloseable;
import org.utilities.io.gz.UtilitiesGZip;

public class MNISTImages implements IterablePipe<MNISTImage> {

	private Supplier<? extends InputStream> inputStream;

	public MNISTImages(Supplier<? extends InputStream> inputStream) {
		this.inputStream = inputStream;
	}

	public static MNISTImages newInstance(Supplier<? extends InputStream> inputStream) {
		return new MNISTImages(inputStream);
	}

	public static MNISTImages newInstance(File file) {
		return new MNISTImages(FunctionPlus.parseSupplier(UtilitiesGZip::getInputStream, file));
	}

	@Override
	public Iterator<MNISTImage> iterator() {
		InputStream inputStream = this.inputStream.get();
		It it = new It(inputStream);
		return new IteratorCloseable<>(it, inputStream);
	}

	private static class It implements Iterator<MNISTImage> {

		private InputStream inputStream;
		private int n;
		private int k;
		private int width;
		private int height;

		public It(InputStream inputStream) {
			this.inputStream = inputStream;
			MNISTFile.readInt(inputStream, 4);
			this.n = MNISTFile.readInt(inputStream, 4);
			this.width = MNISTFile.readInt(inputStream, 4);
			this.height = MNISTFile.readInt(inputStream, 4);
		}

		@Override
		public boolean hasNext() {
			return k < n;
		}

		@Override
		public MNISTImage next() {
			int[] pixels = MNISTFile.readInts(inputStream, width * height);
			k++;
			return new MNISTImage(width, height, pixels);
		}

	}

}
