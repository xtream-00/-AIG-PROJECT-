package tile;

public class Tile {
	
	private Integer xSrc;
	private Integer ySrc;
	private Integer cost;
	private String status;
	private Tile parent;
	
	public Integer getxSrc() {
		return xSrc;
	}
	
	public void setxSrc(Integer xSrc) {
		this.xSrc = xSrc;
	}
	
	public Integer getySrc() {
		return ySrc;
	}
	
	public void setySrc(Integer ySrc) {
		this.ySrc = ySrc;
	}
	
	public Integer getCost() {
		return cost;
	}
	
	public void setCost(Integer cost) {
		this.cost = cost;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Tile getParent() {
		return parent;
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}

	public Tile(Integer xSrc, Integer ySrc, Integer cost, String status, Tile parent) {
		super();
		this.xSrc = xSrc;
		this.ySrc = ySrc;
		this.cost = cost;
		this.status = status;
		this.parent = parent;
	}
	
	public Tile() {
		
	}
	
}
