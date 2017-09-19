package mnits.train;

import java.util.List;

import org.ann.Network;
import org.ann.TrainTracker;
import org.utilities.core.time.TicToc;
import org.utilities.core.time.UtilitiesTime;
import org.utilities.core.util.lambda.LambdaInt;

public class TrainLogged implements TrainTracker {

	private TrainTracker tracker;

	public TrainLogged(List<double[][]> test, int period) {
		TicToc tic = UtilitiesTime.tic();
		LambdaInt k = new LambdaInt(0);
		this.tracker = (Network ann, int epoch, int minibatch) -> {
			if (k.get() % period == 0) {
				String testError = TrainTracker.testError(ann, test);
				System.out.print(
						String.format("%03d", epoch) + '.' + String.format("%04d", minibatch) + ": " + testError + " ");
				tic.toc();
				tic.tic();
			}
			k.add(1);
		};
	}

	@Override
	public void accept(Network ann, int epoch, int minibatch) {
		tracker.accept(ann, epoch, minibatch);
	}

}
