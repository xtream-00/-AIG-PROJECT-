package enemy;

import java.util.Comparator;
import java.util.PriorityQueue;

import javax.swing.plaf.SliderUI;

import home.Home;
import tile.Tile;
import type.Type;

public class Enemy implements Runnable{

	private static Integer enemySpawnTime = 3000;
	private Integer x;
	private Integer y;
	private String status;
	private Thread thread;
	private Home home;
	private Integer[][]board;
	private Tile[][] tileListCreated;
	
	public Integer getX() {
		return x;
	}

	public void setX(Integer x) {
		this.x = x;
	}

	public Integer getY() {
		return y;
	}

	public void setY(Integer y) {
		this.y = y;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public Enemy(Integer x, Integer y, String status) {
		super();
		this.x = x;
		this.y = y;
		this.status = status;
		this.thread = new Thread(this);
	}

	public Enemy() {

	}

	public void setMove(Home home, Integer[][] board, Tile[][] tileListCreated) {
		this.home = home;
		this.board = board;
		this.tileListCreated = tileListCreated;
		if (this.getStatus().equals("active")) {
			this.setStatus("progressing");
			thread.start();
		}
	}
	
	public void goToHome() {
		Integer xSrc = home.getX();
		Integer ySrc = home.getY();
		PriorityQueue<Tile> queueList = new PriorityQueue<>(new Comparator<Tile>() {
			@Override
			public int compare(Tile o1, Tile o2) {
				return o1.getCost() - o2.getCost();
			}
		});
		Integer[] xPoints = {-1, -1, 0, 1, 1, 1, 0, -1};
		Integer[] yPoints = {0, -1, -1, -1, 0, 1, 1, 1};
		Tile[][] tileList = tileListCreated;
		Tile currTile;
		Integer x;
		Integer y;
		Integer cost;
		String status;
		queueList.add(new Tile(xSrc, ySrc, 0, "not_visited", null));
		while (!queueList.isEmpty()) {
			currTile = queueList.poll();
			x = currTile.getxSrc();
			 y = currTile.getySrc();
			 cost = currTile.getCost();
			 status = currTile.getStatus();
			if (status.equals("visited") || (currTile.getxSrc() == this.getX() && currTile.getySrc() == this.getY())) {
				continue;
			}
			tileList[y][x].setStatus("visted");
			for (Integer i = 0; i < xPoints.length; i++) {
				try {
					if (tileList[y + yPoints[i]][x + xPoints[i]].getStatus().equals("not_visited") &&
						 board[y + yPoints[i]][x + xPoints[i]] + cost < tileList[y + yPoints[i]][x + xPoints[i]].getCost()) {
						tileList[y + yPoints[i]][x + xPoints[i]].setCost( board[y + yPoints[i]][x + xPoints[i]] + cost);
						tileList[y + yPoints[i]][x + xPoints[i]].setParent(tileList[y][x]);
						queueList.add(new Tile((x + xPoints[i]), (y + yPoints[i]), tileList[y + yPoints[i]][x + xPoints[i]].getCost(), "not_active", tileList[y][x]));
					}
				} catch (Exception e) {
					
				}
			}
		}
		move(tileList[this.getY()][this.getX()].getParent(), this);
	}
	
	public void move(Tile parent, Enemy enemy) {
			if (parent != null) {
				enemy.setX(parent.getxSrc());
				enemy.setY(parent.getySrc());
			}
	}
	
	public void spawn() {
		setStatus("spawning");
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(enemySpawnTime);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				setStatus("active");
			}
		});
		thread.start();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			goToHome();
		}
	}

}
