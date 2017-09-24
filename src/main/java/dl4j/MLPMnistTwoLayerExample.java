package dl4j;

import java.io.IOException;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MLPMnistTwoLayerExample {

	private static final Logger log = LoggerFactory.getLogger(MLPMnistTwoLayerExample.class);

	public static void main(String[] args) throws IOException {

		// number of rows and columns in the input pictures
		int numRows = 28;
		int numColumns = 28;
		// number of output classes
		int outputNum = 10;
		// batch size for each epoch
		int batchSize = 128;
		// random number seed for reproducibility
		int rngSeed = 123;
		// number of epochs to perform
		int numEpochs = 15;
		// learning rate
		double rate = 0.0015;

		// Get the DataSetIterators:
		MnistDataSetIterator mnistTrain = new MnistDataSetIterator(batchSize, true, rngSeed);
		MnistDataSetIterator mnistTest = new MnistDataSetIterator(batchSize, false, rngSeed);

		log.info("Build model....");

		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
				// include a random seed for reproducibility
				.seed(rngSeed)
				// use stochastic gradient descent as an optimization algorithm
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				.iterations(1)
				.activation(Activation.RELU)
				.weightInit(WeightInit.XAVIER)
				// specify the learning rate
				.learningRate(rate)
				// specify the rate of change of the learning rate.
				.updater(Updater.NESTEROVS)
				.momentum(0.98)
				// regularize learning model
				.regularization(true)
				.l2(rate * 0.005)
				.list()
				// create the first input layer.
				.layer(0, new DenseLayer.Builder().nIn(numRows * numColumns)
						.nOut(500)
						.build())
				// create the second input layer
				.layer(1, new DenseLayer.Builder().nIn(500)
						.nOut(100)
						.build())
				// create hidden layer
				.layer(2, new OutputLayer.Builder(LossFunction.NEGATIVELOGLIKELIHOOD).activation(Activation.SOFTMAX)
						.nIn(100)
						.nOut(outputNum)
						.build())
				// use backpropagation to adjust weights
				.pretrain(false)
				.backprop(true)
				.build();

		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		// print the score with every iteration
		model.setListeners(new ScoreIterationListener(5));

		log.info("Train model....");
		for (int i = 0; i < numEpochs; i++) {
			log.info("Epoch " + i);
			model.fit(mnistTrain);
		}

		log.info("Evaluate model....");
		// create an evaluation object with 10 possible classes
		Evaluation eval = new Evaluation(outputNum);
		while (mnistTest.hasNext()) {
			DataSet next = mnistTest.next();
			// get the networks prediction
			INDArray output = model.output(next.getFeatureMatrix());
			// check the prediction against the true class
			eval.eval(next.getLabels(), output);
		}

		log.info(eval.stats());
		log.info("****************Example finished********************");

	}
}
