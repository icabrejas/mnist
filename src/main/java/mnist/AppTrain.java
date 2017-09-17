package mnist;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.ann.Network;

import mnits.train.TrainLogged;

public class AppTrain {

	private static final Path SRC = Paths.get("C:/data/ML databases/MNIST");

	public static void main(String[] args) {

		System.out.println("reading train...");
		List<double[][]> train = MNISTDataSet.train(SRC)
				.map(MNISTDataPair::parseVector)
				.log((long) 1E4)
				.toList();
		System.out.println("reading test...");
		List<double[][]> test = MNISTDataSet.test(SRC)
				.map(MNISTDataPair::parseVector)
				.log((long) 1E4)
				.toList();

		int inputs = train.get(0)[0].length;
		int outputs = train.get(0)[1].length;
		Network ann = new Network(inputs, 30, outputs);
		ann.trainTracker = new TrainLogged(test, 6000);

		ann.SGD(train, 30, 10, 3.0);

	}

}
