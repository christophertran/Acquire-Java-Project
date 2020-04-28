import java.awt.Color;

public class Tile implements Comparable<Tile>
{
	private int number;
	private String letter;
	private String company;
	private boolean isPlaced;
	private Color color;
	private int x;
	private int y;
	
	public Tile(int num, String s)
	{
		number=num;
		letter=s;
		company=".";
		isPlaced=false;
		color=new Color(0,0,0,95);
		x=0;
		y=0;
	}
	
	public int getNum()
	{
		return number;
	}
	
	public String getLetter()
	{
		return letter;
	}
	
	public String print()
	{
		return getName()+getCompany().toLowerCase().charAt(0);
	}
	
	public String getName()
	{
		return getNum()+getLetter();
	}
	
	public void setCompany(Company c)
	{
		company=c.getName();
	}
	
	public String getCompany()
	{
		return company;
	}
	
	public int compareTo(Tile tile)
	{
		if(getX()>tile.getX())
		{
			return 1;
		}
		else if ( getX()<tile.getX())
		{
			return -1;
		}
		else
			return 0;
	}
	
	public boolean equals(Tile tile)
	{
		if(getNum()==tile.getNum() && getLetter().equals(tile.getLetter()))
			return true;
		return false;
	}
	
	public int distanceFrom1A()
	{
		int count=0;
		int num=getNum();
		char c=getLetter().charAt(0);
		while(num!=1)
		{
			count++;
			num--;
		}
		
		while(c!='A')
		{
			count++;
			c--;
		}
		
		return count;
	}
	
	public boolean isPlaced()
	{
		return isPlaced;
	}
	
	public void setIsPlaced(boolean b)
	{
		isPlaced=b;
		if(isPlaced)
			color=Color.BLACK;
	}
	
	public void setColor(Color c)
	{
		color=c;
	}
	
	public Color getColor()
	{
		return color;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void setX(int x)
	{
		this.x=x;
	}
	
	public void setY(int y)
	{
		this.y=y;
	}
	
}
