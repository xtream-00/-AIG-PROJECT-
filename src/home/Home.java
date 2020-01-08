package home;

public class Home implements Runnable{

	private Integer x;
	private Integer y;
	private Integer hp;
	private String status;
	private Thread thread;
	
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

	public Integer getHp() {
		return hp;
	}

	public void setHp(Integer hp) {
		this.hp = hp;
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

	public Home(Integer x, Integer y, Integer hp) {
		super();
		this.x = x;
		this.y = y;
		this.hp = hp;
		this.status = "not_attacked";
		thread = new Thread(this);
	}

	public Home() {
		
	}
	
	public void active() {
		thread.start();
	}

	@Override
	public void run() {
		while (this.getStatus().equals("attacked")) {
			setHp(getHp() - 1); 
		}
	}

}
