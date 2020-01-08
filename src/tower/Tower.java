package tower;

import enemy.Enemy;

public class Tower {
	
	private Integer x;
	private Integer y;
	private Enemy targetEnemy;
	
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

	public Enemy getTargetEnemy() {
		return targetEnemy;
	}

	public void setTargetEnemy(Enemy targetEnemy) {
		this.targetEnemy = targetEnemy;
	}

	public Tower(Integer x, Integer y, Enemy targetEnemy) {
		super();
		this.x = x;
		this.y = y;
		this.targetEnemy = targetEnemy;
	}

	public Tower() {
	
	}
	
}
