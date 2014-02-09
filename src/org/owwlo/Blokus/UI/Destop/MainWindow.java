package org.owwlo.Blokus.UI.Destop;

import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.owwlo.Blokus.Constants;
import org.owwlo.Blokus.Model.GameBoard;
import org.owwlo.Blokus.Model.GameBoard.BoardListener;
import org.owwlo.Blokus.Model.GameBoard.MovablePiece;

public class MainWindow extends JFrame implements ActionListener,
		MouseMotionListener {
	private static final int BLOCK_LENGTH = 14;

	private LayoutManager layout;
	private List<JButton> btnList;
	private BoardListener boardListener;

	private GameBoard gameboard;

	public MainWindow() {
		this.setSize(600, 600);
		this.setResizable(false);

		btnList = new ArrayList<JButton>();
		gameboard = new GameBoard(14, 14);

		layout = new GridLayout(BLOCK_LENGTH, BLOCK_LENGTH);
		this.setLayout(layout);
		for (int i = 0; i < BLOCK_LENGTH * BLOCK_LENGTH; i++) {
			JButton btn = new JButton();
			btn.setContentAreaFilled(false);
			btn.addActionListener(this);
			btn.addMouseMotionListener(this);
			btnList.add(btn);
			this.add(btn);
		}

		// Register listener for observing the change on GameBoard.
		boardListener = new BoardListener() {
			@Override
			public void OnBoardBitmapChanged(int[][] bmp) {
				MainWindow.this.updateState(bmp);
			}
		};

		gameboard.addBoardListener(boardListener);

		/*
		 * Test Part
		 */
		if(Constants.DEBUG) {
			MovablePiece mp1 = new MovablePiece(0);
			mp1.addPoint(new Point(0,0));
			mp1.addPoint(new Point(1,0));
			mp1.addPoint(new Point(2,0));
			mp1.addPoint(new Point(2,1));
			mp1.setPosition(new Point(0, 0));

			gameboard.addPiece(mp1, true);

			MovablePiece mp2 = new MovablePiece(0);
			mp2.addPoint(new Point(0,0));
			mp2.addPoint(new Point(1,0));
			mp2.addPoint(new Point(0,1));
			mp2.addPoint(new Point(1,1));
			mp2.setPosition(new Point(3, 1));
		}
	}

	private void updateState(int[][] bmp) {
		for (int y = 0; y < BLOCK_LENGTH; y++) {
			for (int x = 0; x < BLOCK_LENGTH; x++) {
				JButton btn = btnList.get(y * BLOCK_LENGTH + x);
				btn.setText((bmp[y][x] == Constants.NO_OCCUPY_POINT_VALUE ? ""
						: "" + bmp[y][x]));
			}
		}
	}

	public static void main(String[] args) {
		new MainWindow().setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton btn = (JButton) e.getSource();
			Point pos = getPointFromBtn(btn);
			if (Constants.DEBUG) {
				System.out.println("Mouse Click at Row=" + pos.y + " Col="
						+ pos.x);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton btn = (JButton) e.getSource();
			Point pos = getPointFromBtn(btn);
		}
	}

	private Point getPointFromBtn(JButton btn) {
		int n = btnList.indexOf(btn);
		int row = n / BLOCK_LENGTH;
		int col = n % BLOCK_LENGTH;
		if (Constants.DEBUG) {
			System.out.println("Mouse at Btn Row=" + row + " Col=" + col);
		}
		return new Point(col, row);
	}
}
