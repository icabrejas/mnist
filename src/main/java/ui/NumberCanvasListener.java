package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import org.utilities.graphics.awt.UtilitiesImage;

import com.google.common.base.Supplier;

public class NumberCanvasListener implements MouseMotionListener, MouseListener {

	private int r = 15;
	private MouseEvent prevDrag;

	private Supplier<BufferedImage> img;
	private Runnable onMouseDoubleClicked = () -> {
	};
	private Supplier<Point> origin = () -> new Point(0, 0);
	private Runnable afterMouseDragged = () -> {
	};

	public NumberCanvasListener(Supplier<BufferedImage> img) {
		this.img = img;
	}

	public static NumberCanvasListener newInstance(Supplier<BufferedImage> img) {
		return new NumberCanvasListener(img);
	}

	public NumberCanvasListener origin(Supplier<Point> origin) {
		this.origin = origin;
		return this;
	}

	public NumberCanvasListener onMouseDoubleClicked(Runnable onMouseDoubleClicked) {
		this.onMouseDoubleClicked = onMouseDoubleClicked;
		return this;
	}

	public NumberCanvasListener afterMouseDragged(Runnable afterMouseDragged) {
		this.afterMouseDragged = afterMouseDragged;
		return this;
	}

	@Override
	public void mouseMoved(MouseEvent evt) {
	}

	@Override
	public void mouseDragged(MouseEvent evt) {
		if (prevDrag != null) {
			Graphics2D g2 = (Graphics2D) img.get()
					.getGraphics();
			UtilitiesImage.applyRenderingHints(g2);
			Color color = g2.getColor();
			g2.setColor(Color.BLACK);
			Stroke stroke = g2.getStroke();
			g2.setStroke(new BasicStroke(2 * r, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			Point origin = this.origin.get();
			int x = evt.getX() - origin.x;
			int y = evt.getY() - origin.y;
			g2.drawLine(prevDrag.getX() - origin.x, prevDrag.getY() - origin.y, x, y);
			g2.fillOval(x - r, y - r, 2 * r, 2 * r);
			g2.setColor(color);
			g2.setStroke(stroke);
			afterMouseDragged.run();
		}
		prevDrag = evt;
	}

	@Override
	public void mouseReleased(MouseEvent evt) {
		prevDrag = null;
	}

	@Override
	public void mousePressed(MouseEvent evt) {

	}

	@Override
	public void mouseExited(MouseEvent evt) {

	}

	@Override
	public void mouseEntered(MouseEvent evt) {

	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		if (evt.getClickCount() == 2 && evt.getButton() == 1) {
			onMouseDoubleClicked.run();
		}
	}

}
