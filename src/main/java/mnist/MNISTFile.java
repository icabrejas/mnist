package mnist;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.utilities.core.lang.exception.QuietException;

public class MNISTFile {

	public static int readInt(InputStream inputStream, int bytes) {
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

	public static int[] readInts(InputStream inputStream, int n) {
		try {
			int[] pixels = new int[n];
			for (int i = 0; i < n; i++) {
				pixels[i] = inputStream.read();
			}
			return pixels;
		} catch (IOException e) {
			throw new QuietException(e);
		}
	}

}
