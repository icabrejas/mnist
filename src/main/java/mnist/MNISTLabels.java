package mnist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.function.Supplier;

import org.utilities.core.lang.exception.QuietException;
import org.utilities.core.lang.iterable.IterablePipe;
import org.utilities.core.util.function.FunctionPlus;
import org.utilities.io.IteratorCloseable;
import org.utilities.io.gz.UtilitiesGZip;

public class MNISTLabels implements IterablePipe<MNISTLabel> {

	private Supplier<? extends InputStream> inputStream;

	public MNISTLabels(Supplier<? extends InputStream> inputStream) {
		this.inputStream = inputStream;
	}

	public static MNISTLabels newInstance(Supplier<? extends InputStream> inputStream) {
		return new MNISTLabels(inputStream);
	}

	public static MNISTLabels newInstance(File file) {
		return new MNISTLabels(FunctionPlus.parseSupplier(UtilitiesGZip::getInputStream, file));
	}

	@Override
	public Iterator<MNISTLabel> iterator() {
		InputStream inputStream = this.inputStream.get();
		It it = new It(inputStream);
		return new IteratorCloseable<>(it, inputStream);
	}

	private static class It implements Iterator<MNISTLabel> {

		private InputStream inputStream;
		private int n;
		private int k;

		public It(InputStream inputStream) {
			this.inputStream = inputStream;
			MNISTFile.readInt(inputStream, 4);
			this.n = MNISTFile.readInt(inputStream, 4);
		}

		@Override
		public boolean hasNext() {
			return k < n;
		}

		@Override
		public MNISTLabel next() {
			try {
				int number = inputStream.read();
				k++;
				return new MNISTLabel(number);
			} catch (IOException e) {
				throw new QuietException(e);
			}
		}

	}
}
