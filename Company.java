import java.awt.Color;
import java.util.*;

public class Company {
	
	private String name;
	private int stockPrice;
	private int size;
	private ArrayList<Tile> tiles;
	private int majority;
	private int minority;
	private int stocks;
	private boolean doesExist;
	private final Color color;
	private final int originalStockPrice;
	private final int originalMajority;
	private final int originalMinority;
	private TreeMap<Integer,ArrayList<Integer>> map;
	
	public Company(String nm,int price,int maj,int min,Color c)
	{
		name=nm;
		originalStockPrice=price;
		originalMajority=maj;
		originalMinority=min;
		stockPrice=price;
		majority=maj;
		minority=min;
		stocks=25;
		size=0;
		tiles=new ArrayList<>();
		doesExist=false;
		color=c;
		map=new TreeMap<>();
		setMap();
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getSize()
	{
		return size;
	}
	
	public int getStockPrice()
	{
		return stockPrice;
	}
	
	public void addTile(Tile t)
	{
		tiles.add(t);
	}

	public ArrayList<Tile> getTiles() 
	{
		return tiles;
	}
	
	public int getMajority()
	{
		return majority;
	}
	
	public int getMinority()
	{
		return minority;
	}
	
	public int getStocksRemaining()
	{
		return stocks;
	}
	
	public void setMap()
	{
		int price=0;
		int maj=0;
		int min=0;
		
		if(getName().equalsIgnoreCase("Sackson") || getName().equalsIgnoreCase("Zeta"))
		{
			price=200;
			maj=2000;
			min=maj/2;
		}
	
		else if(getName().equalsIgnoreCase("Hydra") || getName().equalsIgnoreCase("Fusion") || getName().equalsIgnoreCase("America"))
		{
			price=300;
			maj=3000;
			min=maj/2;
		}
	
		else if(getName().equalsIgnoreCase("Quantum") || getName().equalsIgnoreCase("Phoenix"))
		{
			price=400;
			maj=4000;
			min=maj/2;
		}
	
		for(int i=2;i<=6;i++)
		{	
			ArrayList<Integer> list=new ArrayList<>();
			list.add(price);
			list.add(maj);
			list.add(min);
			map.put(i,list);
			price+=100;
			maj+=1000;
			min=maj/2;
		
		}
		for(int i=7;i<=41;i++)
		{
			ArrayList<Integer> list=new ArrayList<>();
			list.add(price);
			list.add(maj);
			list.add(min);
			map.put(i,list);
			if(i%10==0)
			{
				price+=100;
				maj+=1000;
				min=maj/2;
			}
		}
	}
	
	public TreeMap<Integer,ArrayList<Integer>> getMap()
	{
		return map;
	}
	
	public void setMajorityMinority(int x,int y)
	{
		majority=x;
		minority=y;
	}
	
	public void setStocks(int x)
	{
		stocks=x;
	}
	
	public void setStockPrice(int x)
	{
		stockPrice=x;
	}
	
	public void updateSize(int x)
	{
		size+=x;
	}
	
	public boolean exists()
	{
		return doesExist;
	}
	
	public void setExist(boolean b)
	{
		doesExist=b;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public void reset()
	{
		stockPrice=originalStockPrice;
		majority=originalMajority;
		minority=originalMinority;
		size=0;
		setExist(false);
		tiles.clear();;
	}
	
	public void merge(Company c)
	{
		updateSize(c.getSize());
		for(Tile t:c.getTiles())
		{
			t.setColor(getColor());
			t.setCompany(this);
			addTile(t);
		}
		
		ArrayList<Integer> list=new ArrayList<>();
		list=map.get(getSize());
		setStockPrice(list.get(0));
		setMajorityMinority(list.get(1),list.get(2));
	}
	
}
