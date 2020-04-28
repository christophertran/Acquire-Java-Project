import java.util.ArrayList;
import java.util.Map;

public class Player {

	private int money;
	private int hStock;
	private int aStock;
	private int sStock;
	private int qStock;
	private int fStock;
	private int zStock;
	private int pStock;
	private String name;
	private ArrayList<Tile> tiles;
	private String lastMove;
	
	public Player()
	{
		name="";
		money=6000;
		hStock=0;
		aStock=0;
		sStock=0;
		qStock=0;
		fStock=0;
		zStock=0;
		pStock=0;
		tiles=new ArrayList<Tile>();
		lastMove="";
	}
	
	public Player(String nm)
	{
		name=nm;
		money=6000;
		hStock=0;
		aStock=0;
		sStock=0;
		qStock=0;
		fStock=0;
		zStock=0;
		pStock=0;
		tiles=new ArrayList<>();
		lastMove="";
	}
	
	public String getLastMove()
	{
		return lastMove;
	}
	
	public void setLastMove(String s)
	{
		lastMove=s;
	}
	
	public Tile getNewTile(ArrayList<Tile> t , int x)
	{
		int rand=(int)(Math.random()*t.size());
		Tile tile=t.get(rand);
		tile.setX(x);
		tile.setY(905);
		tiles.add(tile);
		return tile;
	}
	
	public void removeTile(Tile tile)
	{
		for(int i=tiles.size()-1;i>=0;i--)
		{
			if(tiles.get(i).equals(tile))
				tiles.remove(i);
		}
	}
	
	public ArrayList<Tile> getTiles()
	{
		return tiles;
	}
	
	public void printTiles()
	{
		for(Tile t:tiles)
			System.out.print(t.getName()+"\t");
		System.out.println("");
	}
	
	public Tile findTile(String name)
	{
		for(Tile t:tiles)
		{
			if(t.getName().equals(name))
				return t;
		}
		
		return null;
	}
	
	public void setStocks(Map<String,Integer> map)
	{
		for(String s:map.keySet())
		{
			if(s.equalsIgnoreCase("Hydra"))
				setHStock(map.get(s));
			
			else if(s.equalsIgnoreCase("America"))
				setAStock(map.get(s));
			
			else if(s.equalsIgnoreCase("Quantum"))
				setQStock(map.get(s));
			
			else if(s.equalsIgnoreCase("Fusion"))
				setFStock(map.get(s));
			
			else if(s.equalsIgnoreCase("Zeta"))
				setZStock(map.get(s));
			
			else if(s.equalsIgnoreCase("Phoenix"))
				setPStock(map.get(s));
			
			else 
				setSStock(map.get(s));			
		}
	}
		
	public int getMoney()
	{
		return money;
	}
	
	public void setMoney(int m)
	{
		money+=m;
	}
	
	public int getCompanyStocks(Company c)
	{
		String s=c.getName();
		
		if(s.equalsIgnoreCase("Hydra"))
			return getHStock();
		
		else if(s.equalsIgnoreCase("America"))
			return getAStock();
		
		else if(s.equalsIgnoreCase("Quantum"))
			return getQStock();
		
		else if(s.equalsIgnoreCase("Fusion"))
			return getFStock();
		
		else if(s.equalsIgnoreCase("Zeta"))
			return getZStock();
		
		else if(s.equalsIgnoreCase("Phoenix"))
			return getPStock();
		
		else 
			return getSStock();
	}
	
	public int getHStock()
	{
		return hStock;
	}
	
	public int getAStock()
	{
		return aStock;
	}
	
	public int getSStock()
	{
		return sStock;
	}
	
	public int getQStock()
	{
		return qStock;
	}
	
	public int getFStock()
	{
		return fStock;
	}
	
	public int getZStock()
	{
		return zStock;
	}
	
	public int getPStock()
	{
		return pStock;
	}
	
	public int getAllStock()
	{
		return hStock+aStock+sStock+qStock+fStock+zStock+pStock;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setHStock(int s)
	{
		hStock+=s;
	}
	
	public void setAStock(int s)
	{
		aStock+=s;
	}
	
	public void setSStock(int s)
	{
		sStock+=s;
	}
	
	public void setQStock(int s)
	{
		qStock+=s;
	}
	
	public void setFStock(int s)
	{
		fStock+=s;
	}
	
	public void setZStock(int s)
	{
		zStock+=s;
	}
	
	public void setPStock(int s)
	{
		pStock+=s;
	}
	
}
