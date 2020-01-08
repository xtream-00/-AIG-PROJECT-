package panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Vector;
import javax.swing.JPanel;
import enemy.Enemy;
import home.Home;
import tile.Tile;
import tower.Tower;
import type.Type;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener, Runnable {
	
	private Boolean isGameOver;
	private Boolean isPlaceable;
	private Integer xCursor;
	private Integer yCursor;
	private Integer boardWitdh;
	private Integer boardHeight;
	private static Integer[][] board;
	private Integer spawnTime;
	private Home home;
	private Vector<Enemy> enemyList;
	private Vector<Tower> towerList;
	private Random random;
	private Thread gameThread;
	
	public GamePanel() {
		boardWitdh = 40;
		boardHeight = 30;
		board = new Integer[boardHeight + 5][boardWitdh + 5];
		isGameOver = false;
		enemyList = new Vector<>();
		towerList = new Vector<>();
		random = new Random();
	}
	
	public void newGame(){
		createWall();
		createFloor();
		createHome();
		createEnemy();
		spawnTime = 3000;
		
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void createWall() {
		for (Integer i = 0; i < boardHeight; i++) {
			for (Integer j = 0; j < boardWitdh; j++) {
				if (i == 0 || i == (boardHeight - 1) || j == 0 || j == (boardWitdh - 1)) {
					board[i][j] = Type.WALL;
				}
			}
		}
	}
	
	public void createFloor() {
		for (Integer i = 0; i < boardHeight; i++) {
			for (Integer j = 0; j < boardWitdh; j++) {
				if (i != 0 && i != (boardHeight - 1) && j != 0 && j != (boardWitdh - 1)) {
					board[i][j] = Type.FLOOR;
				}
			}
		}
	}
	
	public void createEnemy() {
		for (Integer i = 1; i < boardHeight - 1; i++) {
			for (Integer j = 1; j < boardWitdh - 1; j++) {
				if (i == 1 ||  j == 1 || j == (boardWitdh - 2)) {
					enemyList.add(new Enemy(j, i, "not_active"));
					board[i][j] = Type.SPAWNER;
				}
			}
		}
		
		Thread enemySpawner = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!isGameOver){
					System.out.println("Spawning in: "+ spawnTime);
					try {
						Thread.sleep(spawnTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					Integer enemyToBeSpawn;
					enemyToBeSpawn = random.nextInt(enemyList.size());
					
					while(!enemyList.get(enemyToBeSpawn).getStatus().equals("not_active")){
						enemyToBeSpawn++;
						if(enemyToBeSpawn > enemyList.size()-1) enemyToBeSpawn = 0;
					}
					
					enemyList.get(enemyToBeSpawn).spawn();
					
					if(spawnTime > 100) spawnTime -= 100;
				}
			}
		});
		
		enemySpawner.start();
	}
	
	public static void addBoardWeight(int x, int y, int cost){
		board[y][x] += cost;
	}
	
	public static void removeBoardWeight(int x, int y, int cost){
		board[y][x] -= cost;
	}
	
	public void createHome() {
		home = new Home(20, 25, 3);
		board[home.getY()][home.getX()] = Type.HOME;
	}
	
	public void createTower(Integer x, Integer y) {
		towerList.add(new Tower(x, y, null));
		board[y][x] += Type.TOWER;
		createDamageArea(x, y);
	}
	
	public void createDamageArea(Integer xCentral, Integer yCentral) {
		Integer c = 0;
		for (int i = (yCentral - 5); i <= yCentral + 5; i++) {
			for (int j = (xCentral - c); j <= xCentral + c; j++) {
				if (i >= 2 && i <= (boardHeight - 2) && j >= 2 && j <= (boardWitdh - 3)) {
					board[i][j] += Type.RADIUS;
				}
			}
			if (i < yCentral) {
				c++;
			} else {
				c--;
			}
		}
	}
	
	public void drawWall(Graphics g) {
		for (Integer i = 0; i < boardHeight; i++) {
			for (Integer j = 0; j < boardWitdh; j++) {
				if (i == 0 || i == (boardHeight - 1) || j == 0 || j == (boardWitdh - 1)) {
					g.fillRect(j * 20, i * 20, 20, 20);
				}
			}
		}
	}
	
	public void drawFloor(Graphics g) {
		for (Integer i = 0; i < boardHeight; i++) {
			for (Integer j = 0; j < boardWitdh; j++) {
				if (i != 0 && i != (boardHeight - 1) && j != 0 && j != (boardWitdh - 1)) {
					g.drawRect(j * 20, i * 20, 20, 20);
				}
			}
		}
	}
	
	public void drawHome(Graphics g) {
		g.setColor(Color.BLUE);
		Polygon homeShape = new Polygon();
		homeShape.addPoint(home.getX() * 20 + 0, home.getY() * 20 + 10);		
		homeShape.addPoint(home.getX() * 20+10, home.getY() * 20 + 0);
		homeShape.addPoint(home.getX() * 20+20, home.getY() * 20 + 10);
		homeShape.addPoint(home.getX() * 20+20, home.getY() * 20 + 20);
		homeShape.addPoint(home.getX() * 20 + 0, home.getY() * 20 +20);
		g.fillPolygon(homeShape);
	}
	
	public void drawEnemy(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setStroke(new BasicStroke(2));
		g2d.setColor(Color.RED);
		for (Enemy enemy : enemyList) {
			if (enemy.getStatus().equals("active") || enemy.getStatus().equals("progressing")) {
				g2d.fillOval(enemy.getX() * 20 + 3, enemy.getY() * 20 + 3, 15, 15);
			} else if(enemy.getStatus().equals("not_active") || enemy.getStatus().equals("spawning")) {
				g2d.drawLine(enemy.getX() * 20, enemy.getY() * 20, (enemy.getX() + 1) * 20, (enemy.getY() + 1) * 20);
				g2d.drawLine((enemy.getX() + 1) * 20, enemy.getY() * 20, enemy.getX()  * 20, (enemy.getY() + 1) * 20);
			}
		}
	}
	
	public void drawTower(Graphics g) {
		for (Tower tower: towerList) {
			Polygon towerShape = new Polygon();
			towerShape.addPoint(tower.getX() * 20 + 0, tower.getY() * 20 +20);
			towerShape.addPoint(tower.getX() * 20+10, tower.getY() * 20 + 0);
			towerShape.addPoint(tower.getX() * 20+20, tower.getY() * 20 + 20);
			g.setColor(new Color(255, 0, 255, 60));
			g.fillRect(tower.getX() * 20, tower.getY() * 20, 20, 20);
			g.setColor(new Color(0, 255, 0));
			g.fillPolygon(towerShape);

			Integer c = 0;
			for (int i = (tower.getY() - 5); i <= tower.getY() + 5; i++) {
				for (int j = (tower.getX() - c); j <= tower.getX() + c; j++) {
					if (i >= 2 && i <= (boardHeight - 2) && j >= 2 && j <= (boardWitdh - 3)) {
						g.setColor(new Color(255, 0, 255, 60));
						g.fillRect(j * 20, i * 20, 20, 20);
					}
				}
				if (i < tower.getY()) {
					c++;
				} else {
					c--;
				}
			}
		}
	}
	
	public void drawDamageArea(Graphics g) {
		for (Integer i = 0; i < boardHeight; i++) {
			for (Integer j = 0; j < boardWitdh; j++) {
				if (board[i][j] == Type.RADIUS) {
					g.setColor(new Color(255, 0, 255, 60));
					g.fillRect(j * 20, i * 20, 20, 20);
				}
			}
		}
	}
	
	public void drawHighLight(Graphics g) {
		if (xCursor != null && yCursor != null) {
			if (board[yCursor][xCursor] == Type.WALL ||
				 board[yCursor][xCursor] == Type.ENEMY ||
				 board[yCursor][xCursor] == Type.TOWER ||
				 board[yCursor][xCursor] == Type.HOME ||
				 board[yCursor][xCursor] == Type.RADIUS) {				
				g.setColor(new Color(0, 0, 0, 130) );
				g.fillRect(xCursor * 20, yCursor * 20, 20, 20); 
				isPlaceable = false;
			} else {
				g.setColor(new Color(0, 255, 0, 130) );
				g.fillRect(xCursor * 20, yCursor * 20, 20, 20);
				isPlaceable = true;
			}
		}
	}
	
	//TODO buat draw menunya disini aja, gak perlu manggil lagi
	public void drawMenu(Graphics g) {
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawWall(g);
		drawFloor(g);
		drawHome(g);
		drawEnemy(g);
		drawTower(g);
//		drawDamageArea(g);
		drawHighLight(g);
		drawMenu(g);
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (isPlaceable) {
			createTower(xCursor, yCursor);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		xCursor = (e.getX() - 8) / 20;
		yCursor = (e.getY() - 31) / 20;
		if (xCursor < 0 || xCursor > boardWitdh - 1) {
			xCursor = null;
		}
		if (yCursor < 0 || yCursor > boardHeight - 1) {
			yCursor = null;
		}
	}

	public Tile[][] createTile() {
		Tile[][] tileList = new Tile[boardHeight][boardWitdh];
		for (int i = 0; i < boardHeight; i++) {
			for (int j = 0; j < boardWitdh; j++) {
				tileList[i][j] = new Tile(j, i, Integer.MAX_VALUE, "not_visited", null);
			}
		}
		tileList[home.getY()][home.getY()].setCost(0);
		return tileList;
	}
	
	@Override
	public void run() {
		home.active();
		while (!isGameOver) {
			repaint();
			for (Enemy enemy: enemyList) {
				if(!enemy.getStatus().equals("destroyed")){
					if (enemy.getStatus().equals("active") || 
							 enemy.getStatus().equals("progressing")) {	
							enemy.setMove(home, board, createTile());
						}
						if(enemy.getX() == home.getX() && enemy.getY() == home.getY()) {
							home.setHp(home.getHp() - 1);
							enemy.selfDestroy();
							if(home.getHp() <= 0){
//								isGameOver = true;
							}
						}
				}
			}
		}
	}
	
}
