package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;

import org.deeplearning4j.datasets.iterator.impl.MnistDataSetIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.utilities.core.lang.iterable.IterablePipeSequence;
import org.utilities.graphics.awt.UtilitiesImage;

public class NumberCanvas extends JPanel {

	private static final long serialVersionUID = -5931528927317772781L;

	private BufferedImage img;
	private int imageSize = 200;
	private int horizontalSpace = 10;
	private int verticalSpace = 5;
	private int numberSpace = 5;
	private int barWidth = 100;

	private MNISTEstimator estimator = new MNISTEstimator(this::repaint);

	public NumberCanvas() {
		super();
		setOpaque(false);
		NumberCanvasListener listener = NumberCanvasListener.newInstance(this::getImage)
				.origin(this::origin)
				.onMouseDoubleClicked(this::initIMG)
				.afterMouseDragged(this::repaint);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		initIMG();

		try {
			MnistDataSetIterator mnist = new MnistDataSetIterator(1, true, 123);
			mnist.next();
			INDArray row = mnist.next()
					.getFeatureMatrix()
					.getRow(0);
			BufferedImage img = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < 28; i++) {
				for (int j = 0; j < 28; j++) {
					int value = 255 - Math.round(255 * row.getFloat(i + j * 28));
					int rgb = new Color(value, value, value).getRGB();
					img.setRGB(i, j, rgb);
				}
			}
			this.img = UtilitiesImage.scale(img, (float) imageSize / 28, (float) imageSize / 28, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void initIMG() {
		img = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		repaint();
	}

	private Point origin() {
		int x = Math.round(
				(float) (getWidth() - (imageSize + horizontalSpace + numberSpace + horizontalSpace + barWidth)) / 2);
		int y = Math.round((float) (getHeight() - imageSize) / 2);
		return new Point(x, y);
	}

	public BufferedImage getImage() {
		return img;
	}

	@Override
	public void paint(Graphics g) {

		estimator.estimate(img);

		Graphics2D g2 = (Graphics2D) g;

		RenderingHints renderingHints = g2.getRenderingHints();
		Color color = g2.getColor();
		Font font = g2.getFont();
		Stroke stroke = g2.getStroke();

		UtilitiesImage.applyRenderingHints(g2);

		Point origin = origin();

		g2.drawImage(img, origin.x, origin.y, this);

		g2.setStroke(new BasicStroke(1));
		g2.setColor(Color.BLACK);
		g2.drawRect(origin.x, origin.y, img.getWidth(), img.getHeight());

		float barHeight = (float) (imageSize - 9 * verticalSpace) / 10;

		List<Float> points = IterablePipeSequence.newInstance(origin.y, origin.y + imageSize, barHeight + verticalSpace)
				.map(Double::floatValue)
				.toList();

		List<Float> estimation = estimator.getEstimation();

		g2.setFont(new Font("Arial", Font.PLAIN, Math.round(barHeight)));
		for (int i = 0; i < points.size(); i++) {
			int x = origin.x + imageSize + horizontalSpace;
			int y = Math.round(points.get(i));
			g2.setColor(Color.BLACK);
			g2.drawString("" + i, x, y + barHeight);
			x += numberSpace + horizontalSpace;
			g2.setColor(Color.WHITE);
			g2.fillRect(x, y, barWidth, Math.round(barHeight));
			g2.setColor(Color.BLUE);
			g2.fillRect(x, y, (int) (estimation.get(i) * barWidth), Math.round(barHeight));
			g2.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(1));
			g2.drawRect(x, y, barWidth, Math.round(barHeight));
		}

		g2.setColor(color);
		g2.setStroke(stroke);
		g2.setFont(font);
		g2.setRenderingHints(renderingHints);

		super.paint(g);
	}

}
