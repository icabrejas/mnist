package mnist;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.ann.Network;

import mnits.train.TrainLogged;

public class AppTrain {

	private static final Path SRC = Paths.get("C:/data/ML databases/MNIST");

	public static void main(String[] args) {

		System.out.println("reading train...");
		List<double[][]> train = MNISTDataSet.train(SRC)
				.log((long) 1E4)
				.toList()
				.parallelStream()
				.map(MNISTDataPair::parseVector)
				.collect(Collectors.toList());
		System.out.println("reading test...");
		List<double[][]> test = MNISTDataSet.test(SRC)
				.log((long) 1E4)
				.toList()
				.parallelStream()
				.map(MNISTDataPair::parseVector)
				.collect(Collectors.toList());

		int inputs = train.get(0)[0].length;
		int outputs = train.get(0)[1].length;

		int miniBatchSize = 10;

		Network ann = Network.basic(inputs, 30, outputs);
		ann.trainTracker = new TrainLogged(test, train.size() / miniBatchSize);
		ann.SGD(train, 30, miniBatchSize, 3.0);

	}

}
