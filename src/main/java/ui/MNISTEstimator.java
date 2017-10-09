package ui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.utilities.core.lang.exception.QuietException;
import org.utilities.core.util.concurrent.UtilitiesThread;

import dl4j.MLPMnistSingleLayerExample;
import mnist.MNISTImage;

public class MNISTEstimator {

	private MultiLayerNetwork model;
	private boolean estimating;
	private BufferedImage request;
	private List<Float> estimation = Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f);
	private Runnable listener;

	public MNISTEstimator(Runnable listener) {
		try {
			this.listener = listener;
			this.model = ModelSerializer.restoreMultiLayerNetwork(MLPMnistSingleLayerExample.FILE);
		} catch (IOException e) {
			throw new QuietException(e);
		}
	}

	public List<Float> getEstimation() {
		return estimation;
	}

	public void estimate(BufferedImage img) {
		request = img;
		if (!estimating) {
			estimating = true;
			UtilitiesThread.run(this::estimate);
		} else {
			request = img;
		}
	}

	private void estimate() {
		MNISTImage mnist = new MNISTImage(28, 28, request);
		request = null;
		INDArray input = Nd4j.create(mnist.parseVector());
		INDArray output = model.output(input.getRow(0));
		estimation = parseFloatArray(output);
		listener.run();
		if (request != null) {
			estimate();
		} else {
			estimating = false;
		}
	}

	private List<Float> parseFloatArray(INDArray output) {
		List<Float> estimation = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			float value = output.getFloat(i);
			estimation.add(value);
		}
		return estimation;
	}

}
