import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.awt.Font;
import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Acquire extends JFrame implements MouseListener
{
	private static final int WIDTH = 1920;
	private static final int HEIGHT = 1040;
	private static Tile[][] board;
	private static ArrayList<Tile> tiles;
	private static ArrayList<Company> companies;
	private static Company hydra;
	private static Company quantum;
	private static Company phoenix;
	private static Company zeta;
	private static Company sackson;
	private static Company fusion;
	private static Company america;
	private static ArrayList<Tile> usedTiles; 
	public static GameWindow GW1;
	public static ArrayList<Player> players;
	public static String button;
	
	public Acquire(Player p)
	{
		setSize(WIDTH,HEIGHT);
		GW1 = new GameWindow(board,players,companies,p);
		getContentPane().add(GW1);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GW1.addMouseListener(this);
	}
	
	public static void main( String args [] )
	{
		hydra=new Company("Hydra",300,3000,1500,new Color(219,72,4));
		quantum=new Company("Quantum",400,4000,2000,new Color(1,120,133));
		phoenix=new Company("Phoenix",400,4000,2000,new Color(102,16,160));
		zeta=new Company("Zeta",200,2000,1000,new Color(234,175,16));
		sackson=new Company("Sackson",200,2000,1000,new Color(183,12,12));
		fusion=new Company("Fusion",300,3000,1500,new Color(12,137,12));
		america=new Company("America",300,3000,1500,new Color(28,35,168));
		
		button = "";
		
		companies=new ArrayList<>();
		companies.add(hydra);
		companies.add(quantum);
		companies.add(phoenix);
		companies.add(zeta);
		companies.add(sackson);
		companies.add(fusion);
		companies.add(america);
		
		Player p1=new Player("p1");
		Player p2=new Player("CPU1");
		Player p3=new Player("CPU2");
		Player p4=new Player("CPU3");
		
		players=new ArrayList<>();
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
		
		
		board=new Tile[9][12];
		tiles=new ArrayList<>();
		usedTiles=new ArrayList<>();
		makeBoardAndTiles();
		
		Tile t1=p1.getNewTile(tiles,0);
		usedTiles.add(t1);
		boardTile(t1).setIsPlaced(true);
		boardTile(t1).setColor(Color.GRAY);
		tiles.remove(t1);
		
		Tile t2=p2.getNewTile(tiles,0);
		usedTiles.add(t2);
		boardTile(t2).setIsPlaced(true);
		boardTile(t2).setColor(Color.GRAY);
		tiles.remove(t2);
		
		Tile t3=p3.getNewTile(tiles,0);
		usedTiles.add(t3);
		boardTile(t3).setIsPlaced(true);
		boardTile(t3).setColor(Color.GRAY);
		tiles.remove(t3);
		
		Tile t4=p4.getNewTile(tiles,0);
		usedTiles.add(t4);
		boardTile(t4).setIsPlaced(true);
		boardTile(t4).setColor(Color.GRAY);
		tiles.remove(t4);
		
		
		players=order(players);
		givePlayersTiles(players);
		
		p1.removeTile(t1);
		p2.removeTile(t2);
		p3.removeTile(t3);
		p4.removeTile(t4);
		
		Acquire run = new Acquire(p1);
		
		boolean gameOver=false;
		do
		{
			button = "NA";
			if(canGameOver())
			{
				GW1.setEndGame(true);
				GW1.setInstruct("Click End Game To End Game or Click 0 To Keep Playing");
				GW1.repaint();
				while(!button.equals("END") && !button.equals("zero"))
				{
					System.out.println();
				}
				
				if(button.equals("END"))
				{
					gameOver=true;
				}
			}
			
			Player p=players.get(0);
			
			if(p.getName().contains("CPU"))
			{
				GW1.setInstruct("Click Continue to Proceed");
				GW1.repaint();
				while(!button.equals("CONTINUE"))
				{
					System.out.println();
				}
				
				System.out.println(p.getName());
				p.printTiles();
				String turn=p.getName()+" ";
				boolean placedTile=false;
				Tile tilePlaced=null;
				Tile m=null;
				for(Tile t:p.getTiles())
				{
					ArrayList<Tile> l=getSurroundingTiles(boardTile(t));

					if(!placedTile && l.size()>0 && companyIsMade(l) && !unplayable(boardTile(t)))
					{
						Tile ti=t;
						m=ti;
						ti.setIsPlaced(true);
						ti.setColor(Color.GRAY);

						Tile tile=boardTile(ti);
						tile.setIsPlaced(true);
						tile.setColor(Color.GRAY);

						//System.out.println("Company Tile");
						tilePlaced=tile;
						placedTile=true;
					}
				}

				if(!placedTile)
				{
					for(Tile t:p.getTiles())
					{
						if(!placedTile && !merge(getSurroundingTiles(boardTile(t))).isEmpty() && !merge(getSurroundingTiles(boardTile(t))).contains(null) && !unplayable(boardTile(t)))
						{
							Tile ti=t;
							m=ti;
							ti.setIsPlaced(true);
							ti.setColor(Color.GRAY);

							Tile tile=boardTile(ti);
							tile.setIsPlaced(true);
							tile.setColor(Color.GRAY);

							tilePlaced=tile;
							placedTile=true;
						}
					}
				}

				if(!placedTile)
				{
					Tile ti=null;
					int rand=0;
					do
					{
					rand=(int)(Math.random()*6);
					ti=p.getTiles().get(rand);
					m=ti;
					}while(unplayable(boardTile(ti)));

					ti.setIsPlaced(true);
					ti.setColor(Color.GRAY);

					Tile tile=boardTile(ti);
					tile.setIsPlaced(true);
					tile.setColor(Color.GRAY);

					tilePlaced=tile;
					placedTile=true;
				}

				p.removeTile(m);

				turn+="Placed Tile " + tilePlaced.getName();

				ArrayList<Tile> list=getSurroundingTiles(tilePlaced);

				ArrayList<Tile> a=new ArrayList<>();
				a.add(tilePlaced);
				ArrayList<Tile> used=new ArrayList<>();
				used.add(tilePlaced);
				ArrayList<Tile> tL=getAllSurroundingTiles(tilePlaced,a,used);

				ArrayList<Company> merging=merge(list);
				Company co=companyGrows(list);

				if(list.size()!=0)
				{
					if(companyIsMade(list))
					{
						ArrayList<Company> canPick=new ArrayList<>();
						for(Company c:companies)
						{
							if(!c.exists())
								canPick.add(c);
						}
						int rand=(int)(Math.random()*canPick.size());
						Company c=canPick.get(rand);

						c.setExist(true);
						Color companyColor=c.getColor();
						tilePlaced.setCompany(c);
						tilePlaced.setColor(companyColor);
						for(Tile t:tL)
						{
							t.setCompany(c);
							t.setColor(companyColor);
						}
						///give player who made it a stock
						Map<String,Integer> map=new TreeMap<>();
						map.put(c.getName(),1);
						p.setStocks(map);
						c.setStocks(c.getStocksRemaining()-1);
						c.updateSize(tL.size());
						ArrayList<Integer> prices=new ArrayList<>();
						prices=c.getMap().get(c.getSize());
						c.setStockPrice(prices.get(0));
						c.setMajorityMinority(prices.get(1),prices.get(2));

						turn+="  Created "+c.getName()+"  ";
						turn+="Got 1 Stock from "+c.getName()+"@";
					}

					else if(!merging.isEmpty() && !merging.contains(null))
					{
						turn+="  Merged ";
						for(Company comp:merging)
						{
							turn+=comp.getName()+"("+comp.getSize()+")  ";
						}
						turn+="@";
						p.setLastMove(turn);

						ArrayList<Company> mergeException=mergeException(list);

						if(!mergeException.isEmpty())
						{
							Company stay=mergeException.get(0);
							int stocks=p.getCompanyStocks(stay);
							for(int i=1;i<mergeException.size();i++)
							{
								if(p.getCompanyStocks(mergeException.get(i))<stocks)
								{
									stay=mergeException.get(i);
									stocks=p.getCompanyStocks(stay);
								}
							}

							merging.remove(stay);
							merging.add(0,stay);

						}

						Company chosen=merging.get(0);
						chosen.updateSize(1);

						ArrayList<Company> lost=new ArrayList<>();
						for(int i=merging.size()-1;i>0;i--)
						{
							Company lose=merging.get(i);
							chosen.merge(lose);
							lost.add(lose);
						}

						for(Company c:lost)
						{
							for(Tile t:tL)
							{
								t.setCompany(chosen);
								t.setColor(chosen.getColor());
							}

							int majorityNum=0;
							ArrayList<Player> majority=new ArrayList<>();;

							for(int i=0;i<players.size();i++)
							{
								Player pl=players.get(i);
								if(pl.getCompanyStocks(c)>majorityNum)
								{
									majority=new ArrayList<>();
									majority.add(pl);
									majorityNum=pl.getCompanyStocks(c);
								}

								else if(pl.getCompanyStocks(c)==majorityNum && pl.getCompanyStocks(c)>0)
								{
									majority.add(pl);
								}
							}

							int minorityNum=0;
							ArrayList<Player> minority=new ArrayList<>();

							for(int i=0;i<players.size();i++)
							{
								Player pl=players.get(i);
								if(!majority.contains(pl) && pl.getCompanyStocks(c)>0)
								{
									if(pl.getCompanyStocks(c)>minorityNum)
									{
										minority=new ArrayList<>();
										minority.add(pl);
										majorityNum=pl.getCompanyStocks(c);
									}

									else if(pl.getCompanyStocks(c)==minorityNum)
									{
										minority.add(pl);
									}
								}
							}

							//give out money
							for(Player pl:majority)
							{
								pl.setMoney(c.getMajority()/majority.size());
								pl.setLastMove(pl.getLastMove()+"Got Majority of "+c.getName()+"($"+c.getMajority()/majority.size()+")@");
							}

							if(majority.size()>1 || minority.isEmpty())
							{
								for(Player pl:majority)
								{
									pl.setMoney(c.getMinority()/majority.size());
									pl.setLastMove(pl.getLastMove()+"Got Minority of "+c.getName()+"($"+c.getMinority()/majority.size()+")@");
								}
							}

							else
							{
								for(Player pl:minority)
								{
									pl.setMoney(c.getMinority()/minority.size());
									pl.setLastMove(pl.getLastMove()+"Got Minority of "+c.getName()+"($"+c.getMinority()/majority.size()+")@");
								}
							}
							GW1.repaint();
							for(Player pl:players)
							{
								int stocks=pl.getCompanyStocks(c);
								Map<String,Integer> map=new TreeMap<>();
								boolean keep=false;
								int sold=0;
								int traded=0;
								while(!keep && stocks!=0)
								{
									if(pl.getName().contains("CPU"))
									{
										int x=0;
										if(p.getMoney()<=2000)
										{
											x=stocks;
											int money=c.getStockPrice()*x;
											pl.setMoney(money);

											map.put(c.getName(),-x);
											pl.setStocks(map);
											map=new TreeMap<>();

											c.setStocks(c.getStocksRemaining()+x);

											stocks=0;
											sold=x;
										}

										if(pl.getCompanyStocks(chosen)>3 && stocks!=0)
										{
											x=stocks/2;

											map.put(chosen.getName(),x);
											pl.setStocks(map);
											map=new TreeMap<>();
											chosen.setStocks(chosen.getStocksRemaining()-x);

											map.put(c.getName(),-x*2);
											pl.setStocks(map);
											map=new TreeMap<>();

											c.setStocks(c.getStocksRemaining()+(x*2));

											stocks=pl.getCompanyStocks(c);
											traded=x*2;
										}

										keep=true;
									}

									else
									{
										GW1.setInstruct("Choose to Sell($"+c.getStockPrice()+"), Keep, or Trade @"+pl.getCompanyStocks(c)+" "+c.getName()+" Stocks Left)");
										GW1.repaint();

										while( !button.equals("SELL") && !button.equals("KEEP") && !button.equals("TRADE"))
										{
											System.out.println("");
										}
										
										String s = button;
										
										if(s.equalsIgnoreCase("Sell"))
										{
											sold++;

											int money=c.getStockPrice();
											pl.setMoney(money);

											map.put(c.getName(),-1);
											pl.setStocks(map);
											map=new TreeMap<>();

											c.setStocks(c.getStocksRemaining()+1);

											stocks=pl.getCompanyStocks(c);
											
											
											button = "NA";
											s="";
										}

										else if(s.equalsIgnoreCase("Trade"))
										{
											traded++;
											
											if(chosen.getStocksRemaining()==0 || pl.getCompanyStocks(c)<2)
											{
												button = "NA";
												s="";
												
												GW1.setInstruct("Not Enough Stocks For Trade@Choose to Sell($"+c.getStockPrice()+") or Keep");
												GW1.repaint();
											}
											
											
											else
											{
												map.put(chosen.getName(),1);
												pl.setStocks(map);
												map=new TreeMap<>();
												chosen.setStocks(chosen.getStocksRemaining()-1);


												map.put(c.getName(),-2);
												pl.setStocks(map);
												map=new TreeMap<>();

												c.setStocks(c.getStocksRemaining()+2);

												stocks=pl.getCompanyStocks(c);
												
												button = "NA";
												s="";
											}
										}

										else if(s.equalsIgnoreCase("keep"))
										{
											keep=true;
										}
									}
									
								}
								
								if(sold!=0)
									pl.setLastMove(pl.getLastMove()+"Sold "+ sold +" Stock(s)  ");
								if(traded!=0)
									pl.setLastMove(pl.getLastMove()+"Traded in "+ traded +" Stock(s)  ");
								if(pl.getCompanyStocks(c)!=0)
									pl.setLastMove(pl.getLastMove()+"Kept "+ pl.getCompanyStocks(c) +" Stock(s)@");
									
								GW1.repaint();
							}

							c.reset();
						}
					}

					else if(co!=null)
					{
						tilePlaced.setCompany(co);
						tilePlaced.setColor(co.getColor());
						for(Tile t:list)
						{
							t.setCompany(co);
							t.setColor(co.getColor());
						}
						co.updateSize(list.size());
						ArrayList<Integer> prices=new ArrayList<>();
						prices=co.getMap().get(co.getSize());
						co.setStockPrice(prices.get(0));
						co.setMajorityMinority(prices.get(1),prices.get(2));
					}
				}
					if(companiesExist())
					{
						Map<String,Integer> map=new TreeMap<>();

						ArrayList<Company> canPick=new ArrayList<>();
						for(Company c:companies)
						{
							if(c.exists() && c.getStockPrice()<700 && p.getMoney()>c.getStockPrice() && c.getStocksRemaining()>0)
								canPick.add(c);
						}
						
						if(!canPick.isEmpty())
						{
						int rand=(int)(Math.random()*canPick.size());
						Company picked=canPick.get(rand);
						int x=p.getMoney()/picked.getStockPrice();
						if(x>3)
							x=3;
						if(x>picked.getStocksRemaining())
							x=picked.getStocksRemaining();

						map.put(picked.getName(), x);
						int money=picked.getStockPrice()*x;
						p.setMoney(-money);

						p.setStocks(map);
						map.clear();

						picked.setStocks(picked.getStocksRemaining()-x);

						turn+="Bought "+x+" Stocks from "+picked.getName()+"@";
						}
					}

					for(int i=p.getTiles().size()-1;i>=0;i--)
					{
						Tile til=p.getTiles().get(i);
						Tile tl=boardTile(til);
						if(unplayable(tl))
						{
								p.removeTile(tl);
								p.getNewTile(tiles,0);
								usedTiles.add(tl);
								tiles.remove(tl);
								turn+="Discarded "+tl.getName()+"@";
						}
					}

				p.setLastMove(turn);
				players.remove(p);
				players.add(p);
				Tile ttile=p.getNewTile(tiles,0);
				usedTiles.add(ttile);
				tiles.remove(ttile);
				GW1.repaint();
				System.out.println("\n\n");
			}
			else //REAL PLAYER PLAYING LOOP
			{
			String ins = "Play a tile@";
			GW1.setInstruct(ins);
			GW1.repaint();
			
			String turn = p.getName()+ " ";
			
			while(!button.equals("1") && !button.equals("2") && !button.equals("3") && !button.equals("4") && !button.equals("5") && !button.equals("6") )
			{
				System.out.print("");
			}
			
			String str=button;
			
			String n = p.getTiles().get(Integer.valueOf(str)-1).getName();
			
			ArrayList<String> unplayableButtons=new ArrayList<>();
			ArrayList<Tile> unplayableTiles=new ArrayList<>();
			if(unplayable(boardTile(p.findTile(n))))
			{
				unplayableButtons.add(str);
				unplayableTiles.add(p.findTile(n));
			}
			
			button = "NA";
			
			while(unplayable(p.findTile(n)))
			{
				GW1.setInstruct(p.findTile(n).getName()+" is Unplayable, Play Another Tile");
				GW1.repaint();
				
				button="NA";
				
				while(!button.equals("1") && !button.equals("2") && !button.equals("3") && !button.equals("4") && !button.equals("5") && !button.equals("6") )
				{
					System.out.print("");
				}
				
				str=button;
				
				n = p.getTiles().get(Integer.valueOf(str)-1).getName();

				if(unplayable(boardTile(p.findTile(n))))
				{
					unplayableButtons.add(str);
					unplayableTiles.add(p.findTile(n));
				}
			}
			Tile ti=p.findTile(n);
			int index = p.getTiles().indexOf(ti);
			int xPos = ti.getX();
			p.removeTile(ti);
			ti.setIsPlaced(true);
			ti.setColor(Color.GRAY);
			
			Tile tile=boardTile(ti);
			tile.setIsPlaced(true);
			tile.setColor(Color.GRAY);
			
			turn+="Placed Tile " + tile.getName();

			ArrayList<Tile> list=getSurroundingTiles(tile);
			ArrayList<Tile> a=new ArrayList<>();
			a.add(tile);
			ArrayList<Tile> used=new ArrayList<>();
			used.add(tile);
			ArrayList<Tile> tL=getAllSurroundingTiles(tile,a,used);

			ArrayList<Company> merging=merge(list);
			Company co=companyGrows(list);

			if(list.size()!=0)
			{
				//check if company made
				if(companyIsMade(list))
				{
					String instruct = "Select a Corporation: @";
					GW1.repaint();
					
					for(Company c:companies)
					{
						if(!c.exists())
						{
							instruct+=c.getName()+"@";
						}
					}
					GW1.setInstruct(instruct);
					GW1.repaint();
					
					while(!button.equals("Hydra") && !button.equals("Quantum") && !button.equals("Phoenix") && !button.equals("Zeta") && !button.equals("Sackson") && !button.equals("Fusion") && !button.equals("America"))
					{
						System.out.print("");
					}
					
					String companyName = button;
					button = "NA";
					
					Company c=getCompany(companyName);
					c.setExist(true);
					Color companyColor=c.getColor();
					tile.setCompany(c);
					tile.setColor(companyColor);
					for(Tile t:tL)
					{
						t.setCompany(c);
						t.setColor(companyColor);
					}
					///give player who made it a stock
					Map<String,Integer> map=new TreeMap<>();
					map.put(c.getName(),1);
					p.setStocks(map);
					c.setStocks(c.getStocksRemaining()-1);
					c.updateSize(tL.size());
					ArrayList<Integer> prices=new ArrayList<>();
					prices=c.getMap().get(c.getSize());
					c.setStockPrice(prices.get(0));
					c.setMajorityMinority(prices.get(1),prices.get(2));
					
					turn+="  Created "+c.getName()+"  ";
					turn+="Got 1 Stock from "+c.getName()+"@";
					
					GW1.repaint();
				}

				//check if merge
				else if(!merging.isEmpty() && !merging.contains(null))
				{
					String instruct = "Merging ";
					
					turn+="  Merged ";
					for(Company comp:merging)
					{
						turn+=comp.getName()+"("+comp.getSize()+")  ";
						instruct+=comp.getName()+"("+comp.getSize()+")  ";
					}
					turn+="@";
					instruct+="@";
					ArrayList<Company> mergeException=mergeException(list);
					if(!mergeException.isEmpty())
					{
						String exception="Same size Corporations, @Click On Which One Stays@";
						for(Company c:mergeException)
						{
							exception+=c.getName()+"@";
						}
						GW1.setInstruct(instruct+exception);
						GW1.repaint();
						while( button.equals("NA")  && !button.equals("Hydra") && !button.equals("Quantum") && !button.equals("Phoenix") && !button.equals("Zeta") && !button.equals("Sackson") && !button.equals("Fusion") && !button.equals("America") )
						{
							System.out.print("");
						}
						
						String cName = button;
						button = "NA";
						Company staying=getCompany(cName);
						merging.remove(staying);
						merging.add(0,staying);

					}
					GW1.setInstruct(instruct);
					GW1.repaint();

					Company chosen=merging.get(0);
					chosen.updateSize(1);

					ArrayList<Company> lost=new ArrayList<>();
					for(int i=merging.size()-1;i>0;i--)
					{
						Company lose=merging.get(i);
						chosen.merge(lose);
						lost.add(lose);
					}

					for(Company c:lost)
					{
						for(Tile t:tL)
						{
							t.setCompany(chosen);
							t.setColor(chosen.getColor());
						}

						///GET MAJORITY AND MINORITY
						int majorityNum=0;
						ArrayList<Player> majority=new ArrayList<>();;

						for(int i=0;i<players.size();i++)
						{
							Player pl=players.get(i);
							if(pl.getCompanyStocks(c)>majorityNum)
							{
								majority=new ArrayList<>();
								majority.add(pl);
								majorityNum=pl.getCompanyStocks(c);
							}

							else if(pl.getCompanyStocks(c)==majorityNum && pl.getCompanyStocks(c)>0)
							{
								majority.add(pl);
							}
						}

						int minorityNum=0;
						ArrayList<Player> minority=new ArrayList<>();

						for(int i=0;i<players.size();i++)
						{
							Player pl=players.get(i);
							if(!majority.contains(pl) && pl.getCompanyStocks(c)>0)
							{
								if(pl.getCompanyStocks(c)>minorityNum)
								{
									minority=new ArrayList<>();
									minority.add(pl);
									majorityNum=pl.getCompanyStocks(c);
								}

								else if(pl.getCompanyStocks(c)==minorityNum  && pl.getCompanyStocks(c)>0)
								{
									minority.add(pl);
								}
							}
						}

						//give out money
						for(Player pl:majority)
						{
							pl.setMoney(c.getMajority()/majority.size());
							pl.setLastMove(pl.getLastMove()+"Got Majority of "+c.getName()+"($"+c.getMajority()/majority.size()+")@");
						}
						System.out.println("");
						if(majority.size()>1 || minority.isEmpty())
						{
							for(Player pl:majority)
							{
								pl.setMoney(c.getMinority()/majority.size());
								pl.setLastMove(pl.getLastMove()+"Got Minority of "+c.getName()+"($"+c.getMinority()/majority.size()+")@");
							}
						}

						else
						{
							for(Player pl:minority)
							{
								pl.setMoney(c.getMinority()/minority.size());
								pl.setLastMove(pl.getLastMove()+"Got Minority of "+c.getName()+"($"+c.getMinority()/minority.size()+")@");
							}
						}

						GW1.repaint();
						//go through each player and ask what they do with stocks:sell,trade,keep
						for(Player pl:players)
							{
								int stocks=pl.getCompanyStocks(c);
								Map<String,Integer> map=new TreeMap<>();
								boolean keep=false;
								int sold=0;
								int traded=0;
								while(!keep && stocks!=0)
								{
									if(pl.getName().contains("CPU"))
									{
										int x=0;
										if(p.getMoney()<=1200)
										{
											x=stocks;
											int money=c.getStockPrice()*x;
											pl.setMoney(money);

											map.put(c.getName(),-x);
											pl.setStocks(map);
											map=new TreeMap<>();

											c.setStocks(c.getStocksRemaining()+x);

											stocks=0;
											sold=x;
										}

										if(pl.getCompanyStocks(chosen)>3 && stocks!=0)
										{
											x=stocks/2;

											map.put(chosen.getName(),x);
											pl.setStocks(map);
											map=new TreeMap<>();
											chosen.setStocks(chosen.getStocksRemaining()-x);

											map.put(c.getName(),-x*2);
											pl.setStocks(map);
											map=new TreeMap<>();

											c.setStocks(c.getStocksRemaining()+(x*2));

											stocks=pl.getCompanyStocks(c);
											traded=2*x;
										}

										keep=true;
									}

									else
									{
										GW1.setInstruct("Choose to Sell($"+c.getStockPrice()+"), Keep, or Trade @"+pl.getCompanyStocks(c)+" "+c.getName()+" Stocks Left)");
										GW1.repaint();

										while( !button.equals("SELL") && !button.equals("KEEP") && !button.equals("TRADE"))
										{
											System.out.println("");
										}
										
										String s = button;
										
										if(s.equalsIgnoreCase("Sell"))
										{
											sold++;

											int money=c.getStockPrice();
											pl.setMoney(money);

											map.put(c.getName(),-1);
											pl.setStocks(map);
											map=new TreeMap<>();

											c.setStocks(c.getStocksRemaining()+1);

											stocks=pl.getCompanyStocks(c);
											
											button = "NA";
											s="";
										}

										else if(s.equalsIgnoreCase("Trade"))
										{
											traded++;
											
											if(chosen.getStocksRemaining()==0 || pl.getCompanyStocks(c)<2)
											{
												button = "NA";
												s="";
												
												GW1.setInstruct("Not Enough Stocks For Trade@Choose to Sell($"+c.getStockPrice()+") or Keep");
												GW1.repaint();
											}
											
											
											else
											{
												map.put(chosen.getName(),1);
												pl.setStocks(map);
												map=new TreeMap<>();
												chosen.setStocks(chosen.getStocksRemaining()-1);


												map.put(c.getName(),-1*2);
												pl.setStocks(map);
												map=new TreeMap<>();

												c.setStocks(c.getStocksRemaining()+(1*2));

												stocks=pl.getCompanyStocks(c);
												
												button = "NA";
											s="";
											}
										}

										else if(s.equalsIgnoreCase("keep"))
										{
											keep=true;
										}
									}
									
								}
								
								if(sold!=0)
										pl.setLastMove(pl.getLastMove()+"Sold "+ sold +" Stock  ");
								if(traded!=0)
									pl.setLastMove(pl.getLastMove()+"Traded in "+ traded +" Stocks ");
								if(pl.getCompanyStocks(c)!=0)
									pl.setLastMove(pl.getLastMove()+"Kept "+ pl.getCompanyStocks(c) +" Stocks@");
									
								GW1.repaint();
								
							}
						c.reset();
					}
				}


				//check if company grows

				else if(co!=null)
				{
					tile.setCompany(co);
					tile.setColor(co.getColor());
					for(Tile t:list)
					{
						t.setCompany(co);
						t.setColor(co.getColor());
					}
					co.updateSize(list.size());
					ArrayList<Integer> prices=new ArrayList<>();
					prices=co.getMap().get(co.getSize());
					co.setStockPrice(prices.get(0));
					co.setMajorityMinority(prices.get(1),prices.get(2));
					GW1.repaint();
				}

			}

			//buy stocks  *Company must exist
			boolean stop=false;
			int count=0;
			Map<String,Integer> map=new TreeMap<>();
			while(count<3 && !stop && companiesExist() && p.getMoney()>200)
			{
				String instruct = "Choose a company to buy from:@";
				instruct += "If you dont want to buy just choose@a company then choose 0@";
				for(Company c:companies)
				{
					if(c.exists() && c.getStocksRemaining()>0)
					{
						System.out.print(c.getName()+"\t");
						instruct+=c.getName()+"@";
					}
				}
				
				GW1.setInstruct(instruct);
				GW1.repaint();
				
				while(!button.equals("Hydra") && !button.equals("Quantum") && !button.equals("Phoenix") && !button.equals("Zeta") && !button.equals("Sackson") && !button.equals("Fusion") && !button.equals("America"))
				{
					System.out.print("");
				}
				
				String name = button;
				button = "NA";
				
				GW1.setInstruct("Choose the # you want to buy");
				GW1.repaint();
				
				while(!button.equals("zero") && !button.equals("one") && !button.equals("two") && !button.equals("three") )
				{
					System.out.print("");
				}
				
				int x;
				
				if( button.equals("zero") )
				{
					x = 0;
				}
				else if ( button.equals("one") )
				{
					x = 1;
				}
				else if ( button.equals("two") )
				{
					x = 2;
				}
				else
				{
					x = 3;
				}
				button = "NA";
				
				if( x != 0 && p.getMoney()>=getCompany(name).getStockPrice()*x)
				{
					Company c=getCompany(name);
					map.put(name, x);
					count+=x;
					int money=c.getStockPrice()*x;
					p.setMoney(-money);
					p.setStocks(map);
					map.clear();
					c.setStocks(c.getStocksRemaining()-x);
					turn+="Bought "+x+" Stocks from "+name+"@";
				}
				else
					stop=true;

			}
			
			int cnt=0;
			for(Tile t:unplayableTiles)
			{
				GW1.setInstruct("Cick On "+t.getName()+"To Remove @Or Click 0 To Keep Tile");
				GW1.repaint();
				
				while(!button.equals(unplayableButtons.get(cnt)) && !button.equals("zero"))
				{
					System.out.println();
				}
				
				String s=button;
				cnt++;
				
				if(!s.equalsIgnoreCase("zero"))
				{
					p.removeTile(t);
					p.getNewTile(tiles,t.getX());
					usedTiles.add(t);
					tiles.remove(t);
					turn+="Discarded "+t.getName()+"@";
				}
			}

			p.setLastMove(turn);
			players.remove(p);
			players.add(p);
			Tile ttile=p.getNewTile(tiles,ti.getX());
			for( int i = index; i < p.getTiles().size(); i++ )
			{
				p.getTiles().get(i).setX(xPos);
				xPos+=141;
			}
			
			usedTiles.add(ttile);
			tiles.remove(ttile);
			GW1.repaint();
			}
		}
		while(gameOver==false);
		
		button = "NA";
		
		String instruct="Game Over@";
		instruct+=getWinner()+" Wins";
		
		GW1.setInstruct(instruct);
		GW1.repaint();
		
	}
	
	public static String getWinner()
	{
		//ArrayList<Company> exist=new ArrayList<>();
		//for(Company c:companies)
		//{
			//if(c.exists())
		//		exist.add(c);
		//}
		
		//for(Company c:exist)
		//{
			
		//}
		
		return "Everyone";
	}

	private static boolean companiesExist()
	{
		for(Company c:companies)
		{
			if(c.exists())
				return true;
		}
		return false;
	}

	public static void makeBoardAndTiles()
	{
		char c='A';
		for(int l=0;l<9;l++)
		{
			for(int n=0;n<12;n++)
			{
				String s=c+"";
				Tile t=new Tile(n+1,s);
				board[l][n]=t;
				tiles.add(t);
			}
			c++;
		}
	}
	
	public static ArrayList<Player> order(ArrayList<Player> players)
	{
		int closest=109;
		Player first=null;
		for(int i=0;i<players.size();i++)
		{
			Tile t=players.get(i).getTiles().get(0);
			if(closest>t.distanceFrom1A())
			{
				closest=t.distanceFrom1A();
				first=players.get(i);
			}
		}

		ArrayList<Player> order = new ArrayList<>();
		order.add(first);

		int index=players.indexOf(first);
		for(int i=index+1;i<players.size();i++)
		{
			order.add(players.get(i));
		}

		for(int i=0;i<index;i++)
		{
			order.add(players.get(i));
		}

		return order;
	}

	public static void givePlayersTiles(ArrayList<Player> players)
	{
		int x = 1065;
		
		for(Player p:players)
		{
			for(int i=0;i<6;i++)
			{
				Tile t=p.getNewTile(tiles,x);
				x+=141;
				usedTiles.add(t);
				tiles.remove(t);
			}
			x=1065;
		}
	}

	public static Tile boardTile(Tile t)
	{
		for(int r=0;r<board.length;r++)
		{
			for(int c=0;c<board[r].length;c++)
			{
				if(board[r][c].equals(t))
					return board[r][c];

			}
		}
		return null;
	}

	public static boolean canGameOver()
	{
		for(Company c:companies)
		{
			if(c.getSize()>=41)
				return true;
			if(c.getSize()<11)
				return false;
		}
		return true;
	}

	public static Company getCompany(String name)
	{
		for(Company c:companies)
		{
			if(c.getName().equals(name))
				return c;
		}
		return null;
	}

	public static int[] getRowAndCol(Tile t)
	{
		int[] a=new int[2];
		for(int r=0;r<board.length;r++)
		{
			for(int c=0;c<board[r].length;c++)
			{
				if(board[r][c].equals(t))
				{
					a[0]=r;
					a[1]=c;
					return a;
				}


			}
		}
		return null;
	}

	public static boolean companyIsMade(ArrayList<Tile> list)
	{
			for(Tile t:list)
			{
				if(t.getColor().getRGB()!=Color.GRAY.getRGB())
					return false;
			}
			return true;
	}

	public static Company companyGrows(ArrayList<Tile> list)
	{
		for(Tile t:list)
		{
			if(t.getColor().getRGB()!=Color.GRAY.getRGB())
				return getCompany(t.getCompany());
		}
		return null;
	}

	public static ArrayList<Company> merge(ArrayList<Tile> list)
	{
		ArrayList<Company> mergingCompanies=new ArrayList<>();
		if(list.size()>=2)
		{
			for(int i=0;i<list.size()-1;i++)
			{
				String companyName=list.get(i).getCompany();
				if(!companyName.equalsIgnoreCase("."))
				{
					for(int j=i+1;j<list.size();j++)
					{
						String other=list.get(j).getCompany();
						if(!other.equalsIgnoreCase(".") && !companyName.equalsIgnoreCase(other))
						{
							mergingCompanies.add(getCompany(list.get(i).getCompany()));
							mergingCompanies.add(getCompany(list.get(j).getCompany()));
						}
					}
				}
			}

			for(int i=0;i<mergingCompanies.size()-1;i++)
			{
				for(int j=i+1;j<mergingCompanies.size();j++)
				{
					if(mergingCompanies.get(i).getName().equalsIgnoreCase(mergingCompanies.get(j).getName()))
						mergingCompanies.remove(j);
				}
		
			}

			int biggest=0;
			Company largest=null;
			for(Company c:mergingCompanies)
			{
				if(c.getSize()>biggest)
				{
					biggest=c.getSize();
					largest=c;
				}
			}

			mergingCompanies.remove(largest);
			mergingCompanies.add(0,largest);

			return mergingCompanies;
		}

		return mergingCompanies;
	}

	public static ArrayList<Company> mergeException(ArrayList<Tile> list)
	{
		ArrayList<Company> sameSize=new ArrayList<>();
		ArrayList<Company> merging=merge(list);

		for(int i=0;i<merging.size()-1;i++)
		{
			if(merging.get(i).getSize()==merging.get(i+1).getSize())
			{
				if(sameSize.contains(merging.get(i))==false)
					sameSize.add(merging.get(i));
				if(sameSize.contains(merging.get(i+1))==false)
					sameSize.add(merging.get(i+1));
			}

		}
		for(int i=sameSize.size()-1;i>0;i--)
		{
			if(sameSize.get(i).equals(sameSize.get(i-1)))
				sameSize.remove(i);
		}

		return sameSize;
	}

	public static ArrayList<Tile> getSurroundingTiles(Tile t)
	{
		ArrayList<Tile> list=new ArrayList<>();
		int[] a=getRowAndCol(t);
		int row=a[0];
		int col=a[1];

		if(row+1<board.length && board[row+1][col].isPlaced())
			list.add(board[row+1][col]);

		if((row-1>=0 && board[row-1][col].isPlaced()))
			list.add(board[row-1][col]);

		if(col+1<board[0].length && board[row][col+1].isPlaced())
			list.add(board[row][col+1]);

		if((col-1>=0 && board[row][col-1].isPlaced()))
			list.add(board[row][col-1]);

		return list;
	}

	public static ArrayList<Tile> getAllSurroundingTiles(Tile t,ArrayList<Tile> list,ArrayList<Tile> used)
	{
		boolean allUsed=true;
		for(Tile tile:getSurroundingTiles(t))
		{
			if(!used.contains(tile))
			{
				allUsed=false;
			}
		}

		if(allUsed)
		{
			return list;
		}

		else
		{
			for(Tile tile:getSurroundingTiles(t))
			{
				if(!used.contains(tile))
				{
					list.add(tile);
					used.add(tile);
					list.addAll(getAllSurroundingTiles(tile,list,used));
				}
			}

			for(int i=0;i<list.size();i++)
			{
				for(int j=list.size()-1;j>i;j--)
				{
					if(list.get(i).equals(list.get(j)))
						list.remove(j);
				}
			}
			return list;
		}


	}

	public static boolean unplayable(Tile tile)
	{
		boolean allExist=true;
		for(Company c:companies)
		{
			if(c.exists()==false)
				allExist=false;
		}

		if(getSurroundingTiles(tile).size()>0 && companyIsMade(getSurroundingTiles(tile)) && allExist)
		{
			return true;
		}

		ArrayList<Company> merging=merge(getSurroundingTiles(tile));
		if(getSurroundingTiles(tile).size()>0 && !merging.isEmpty() && !merging.contains(null))
		{
			boolean cantMerge=true;
			for(Company c:merging)
			{
				if(c.getSize()<11)
				{
					cantMerge=false;
				}
			}
			return cantMerge;
		}
		return false;
	}

	public void mouseClicked(MouseEvent e) 
	{
		int x = e.getX();
		int y = e.getY();
		
		if ( x>=1190 && x<=1442 && y>=275 && y<=375 )
		{
			button = "TRADE";
		}
		else if ( x>=1474 && x<=1726 && y>=275 && y<=375 )
		{
			button = "KEEP";
		}
		else if ( x>=1332 && x<=1584 && y>=150 && y<=250 )
		{
			button = "SELL";
		}
		else if ( x>=1332 && x<=1442 && y>=400 && y<=510 )
		{
			button = "Sackson";
		}
		else if ( x>=1474 && x<=1584 && y>=400 && y<=510 )
		{
			button = "Zeta";
		}
		else if ( x>=1262 && x<=1372 && y>=541 && y<=651 )
		{
			button = "Hydra";
		}
		else if ( x>=1403 && x<=1513 && y>=541 && y<=651 )
		{
			button = "Fusion";
		}
		else if ( x>=1544 && x<=1654 && y>=541 && y<=651 )
		{
			button = "America";
		}
		else if ( x>=1332 && x<=1442 && y>=682 && y<=792 )
		{
			button = "Phoenix";
		}
		else if ( x>=1474 && x<=1584 && y>=682 && y<=792 )
		{
			button = "Quantum";
		}
		else if ( x>=1060 && x<=1150 && y>=905 && y<=995 )
		{
			button = "1";
		}
		else if ( x>=1201 && x<=1291 && y>=905 && y<=995 )
		{
			button = "2";
		}
		else if ( x>=1342 && x<=1432 && y>=905 && y<=995 )
		{
			button = "3";
		}
		else if ( x>=1483 && x<=1573 && y>=905 && y<=995 )
		{
			button = "4";
		}
		else if ( x>=1624 && x<=1714 && y>=905 && y<=995 )
		{
			button = "5";
		}
		else if ( x>=1765 && x<=1855 && y>=905 && y<=995 )
		{
			button = "6";
		}
		else if ( x>=1674 && x<=1734 && y>=682 && y<=742 )
		{
			button = "zero";
		}
		else if ( x>=1739 && x<=1799 && y>=682 && y<=742 )
		{
			button = "one";
		}
		else if ( x>=1674 && x<=1734 && y>=747 && y<=807 )
		{
			button = "two";
		}
		else if ( x>=1739 && x<=1799 && y>=747 && y<=807 )
		{
			button = "three";
		}
		else if ( x>=1025 && x<=1135 && y>=575 && y<=655 )
		{
			button = "END";
		}
		else if ( x>=1025 && x<=1225 && y>=425 && y<=500 )
		{
			button = "CONTINUE";
		}
		else
		{
			button = "NA";
		}
	}
	public void mousePressed(MouseEvent e) 
	{
		button = "NA";
	}
	public void mouseReleased(MouseEvent e) 
	{
		button = "NA";
	}
	public void mouseEntered(MouseEvent e) 
	{
		button = "NA";
	}
	public void mouseExited(MouseEvent e) 
	{
		button = "NA";
	}
}
class GameWindow extends Canvas
{
	private static Tile [][] tiles;
	private static Image img1;
	private static ArrayList<Player> players;
	private static ArrayList<Company> companies;
	private static Player player;
	private static String instruct;
	private static boolean endGame;
	
	public GameWindow(Tile [][] t , ArrayList<Player>players , ArrayList<Company> comp , Player p)
	{
		tiles = t;
		this.players = players;
		companies = comp;
		this.player = p;
		instruct = "";
		endGame=false;
		
		try
		{
			img1 = ImageIO.read((new File("wood1.jpg")));
		}
		catch(Exception e)
		{
			System.out.println("Error Loading images: " + e.getMessage());
		}
	}
	public void paint( Graphics window )
	{
		drawBackground(window);
		board(window);
		printTiles(window);
		
		for ( Player p : players )
		{
			if ( p.getName().equalsIgnoreCase("p1") )
			{
				player1Info(window,p);
			}
			else if ( p.getName().equalsIgnoreCase("CPU1"))
			{
				player2Info(window,p);
			}
			else if ( p.getName().equalsIgnoreCase("CPU2"))
			{
				player3Info(window,p);
			}
			else
			{
				player4Info(window,p);
			}
		}
		
		playerTiles(window);
		
		for ( Company c : companies )
		{
			if( c.getName().equalsIgnoreCase("Sackson") )
			{
				button1(window,c);
			}
			else if ( c.getName().equalsIgnoreCase("Zeta") )
			{
				button2(window,c);
			}
			else if ( c.getName().equalsIgnoreCase("Hydra") )
			{
				button3(window,c);
			}
			else if ( c.getName().equalsIgnoreCase("Fusion") )
			{
				button4(window,c);
			}
			else if ( c.getName().equalsIgnoreCase("America") )
			{
				button5(window,c);
			}
			else if ( c.getName().equalsIgnoreCase("Phoenix") )
			{
				button6(window,c);
			}
			else
			{
				button7(window,c);
			}
		}
		SellButton(window);
		TradeButton(window);
		KeepButton(window);
		Instructions(window);
		StocksZero(window);
		StocksOne(window);
		StocksTwo(window);
		StocksThree(window);
		endGame(window);
		cont(window);
	}
	private void drawBackground( Graphics window )
	{
		window.drawImage(img1,0,0,1920,1040,null);
	}
	private void board( Graphics window )
	{
		//window.setColor(Color.GRAY);
		//Rectangle brd = new Rectangle(5,5,993,746); //(x,y,width,height)
		//window.fillRect((int)brd.getX(), (int)brd.getY(), (int)brd.getWidth(), (int)brd.getHeight());
		
		window.setColor(Color.BLACK);
		window.setFont(new Font("Times New Roman",Font.BOLD,60));
		window.drawString("ACQUIRE", 1315 , 75);
		
		Color c = new Color(219,195,17);
		window.setColor(c);
		window.setFont(new Font("Times New Roman",Font.BOLD,60));
		window.drawString("ACQUIRE", 1315 , 75);
		
	}
	private void printTiles( Graphics window )
	{
		int r = 0;
		int c = 0;
		Color black = new Color(0,0,0,90);
		
		for( int y = 10; y <= 695 ; y+=82 )
		{
			for ( int x = 10; x <= 950; x+=82 )
			{
				if( tiles[r][c].isPlaced() == true )
				{
					window.setColor(tiles[r][c].getColor());
				}
				else
				{
					window.setColor(black);
				}
				Rectangle brd = new Rectangle(x,y,80,80); //(x,y,width,height)
				window.fillRect((int)brd.getX(), (int)brd.getY(), (int)brd.getWidth(), (int)brd.getHeight());
				
				
				window.setColor(black);
				window.setColor(Color.WHITE);
				window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
				window.drawString(tiles[r][c].getName(), x+30, y+45);
				c++;
			}
			r++;
			c=0;
		}
	}
	private void player1Info( Graphics window , Player p )
	{	
		//window.setColor(Color.GRAY);
		//Rectangle rect = new Rectangle(5,757,494,125); //(x,y,width,height)
		//window.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		Color c = new Color(0,0,0,95);
		window.setColor(c);
		Rectangle rect1 = new Rectangle(10,762,484,115); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,24));
		window.drawString("Player 1:", 15 , 787);
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("Money: $"+p.getMoney(), 215 , 787); //+200
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("# Stocks: "+p.getAllStock(), 365 , 787); //+350
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("Last Turn: ", 15 , 805); //+35
		
		ArrayList <String> i = new ArrayList<>(Arrays.asList(p.getLastMove().split("@")));

		int y = 820;
		for ( String z : i )
		{
			window.drawString(z, 15 , y);
			y+=15;
		}
	}
	private void player2Info( Graphics window , Player p )
	{
		//window.setColor(Color.GRAY);
		//Rectangle rect = new Rectangle(502,757,495,125); //(x,y,width,height)
		//window.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		Color c = new Color(0,0,0,95);
		window.setColor(c);
		Rectangle rect1 = new Rectangle(507,762,484,115); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,24));
		window.drawString("CPU1", 512 , 787);
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("Money: $"+p.getMoney(), 712 , 787); //+200
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("# Stocks: "+p.getAllStock(), 862 , 787); //+350
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("Last Turn: ", 512 , 805); //+35
		
		ArrayList <String> i = new ArrayList<>(Arrays.asList(p.getLastMove().split("@")));

		int y = 820;
		for ( String z : i )
		{
			window.drawString(z, 512 , y);
			y+=15;
		}
	}
	private void player3Info( Graphics window , Player p )
	{
		//window.setColor(Color.GRAY);
		//Rectangle rect = new Rectangle(5,887,494,125); //(x,y,width,height)
		//window.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		Color c = new Color(0,0,0,95);
		window.setColor(c);
		Rectangle rect1 = new Rectangle(10,892,484,115); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,24));
		window.drawString("CPU2", 15 , 917);
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("Money: $"+p.getMoney(), 215 , 917); //+200
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("# Stocks: "+p.getAllStock(), 365 , 917); //+350
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("Last Turn: ", 15 , 935); //+35
		
		ArrayList <String> i = new ArrayList<>(Arrays.asList(p.getLastMove().split("@")));

		int y = 950;
		for ( String z : i )
		{
			window.drawString(z, 15 , y);
			y+=15;
		}
	}
	private void player4Info( Graphics window , Player p )
	{
		//window.setColor(Color.GRAY);
		//Rectangle rect = new Rectangle(502,887,495,125); //(x,y,width,height)
		//window.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		
		Color c = new Color(0,0,0,95);
		window.setColor(c);
		Rectangle rect1 = new Rectangle(507,892,484,115); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,24));
		window.drawString("CPU3", 512 , 917);
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("Money: $"+p.getMoney(), 712 , 917); //+200
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("# Stocks: "+p.getAllStock(), 862 , 917); //+350
		
		window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
		window.drawString("Last Turn: ", 512 , 935); //+35
		
		ArrayList <String> i = new ArrayList<>(Arrays.asList(p.getLastMove().split("@")));

		int y = 950;
		for ( String z : i )
		{
			window.drawString(z, 512 , y);
			y+=15;
		}
	}
	private void playerTiles( Graphics window )
	{	
		
		for ( int x = 1060; x <= 1800; x+=141 )
		{
			window.setColor(Color.GRAY);
			Rectangle rect = new Rectangle(x,905,90,90); //(x,y,width,height)
			window.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight());
		}
		
		for ( int x = 1065; x <= 1800; x+=141 )
		{
			window.setColor(Color.BLACK);
			Rectangle rect2 = new Rectangle(x,910,80,80); //(x,y,width,height)
			window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
		}
		
		ArrayList <Tile> t = player.getTiles();
		
		for (Tile til : t)
		{
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,12));
			window.drawString(til.getName(), til.getX()+30 , 955);
		}
	}
	public void setPlayer(Player p)
	{
		player = p;
	}
	private void button1( Graphics window , Company co ) //Sackson //red
	{	
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1332,400,110,110); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		if (true) // if the player is buying stocks
		{
			Color c = new Color(183,12,12);
			window.setColor(c);
			Rectangle rect2 = new Rectangle(1337,405,100,100); //(x,y,width,height)
			window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,18));
			window.drawString("Sackson", 1342 , 426); //+10,+26
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString("Has: "+player.getSStock(), 1342 , 450); //+0,+24
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString(co.getStocksRemaining()+" Left/25 Total", 1340 , 501); //+0,+75
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,13));
			window.drawString("Price:"+co.getStockPrice(), 1342 , 476); //+0,+50
		}
	}
	private void button2( Graphics window , Company co ) //Zeta //yellow
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1474,400,110,110); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		if (true)
		{
			Color c = new Color(234,175,16);
			window.setColor(c);
			Rectangle rect2 = new Rectangle(1479,405,100,100); //(x,y,width,height)
			window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,18));
			window.drawString("Zeta", 1484 , 426);
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString("Has: "+player.getZStock(), 1484 , 450); //+0,+24
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString(co.getStocksRemaining()+" Left/25 Total", 1482 , 501); //+0,+75
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,13));
			window.drawString("Price:"+co.getStockPrice(), 1484 , 476); //+0,+50
		}
	}
	private void button3( Graphics window , Company co ) //Hydra //orange
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1262,541,110,110); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		if (true)
		{
			Color c = new Color(219,72,4);
			window.setColor(c);
			Rectangle rect2 = new Rectangle(1267,546,100,100); //(x,y,width,height)
			window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,18));
			window.drawString("Hydra", 1272 , 567);
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString("Has: "+player.getHStock(), 1272 , 591); //+0,+24
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString(co.getStocksRemaining()+" Left/25 Total", 1270 , 642); //+0,+75
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,13));
			window.drawString("Price:"+co.getStockPrice(), 1272 , 617); //+0,+50
		}
	}
	private void button4( Graphics window , Company co ) //Fusion //green
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1403,541,110,110); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		if (true)
		{
			Color c = new Color(12,137,12);
			window.setColor(c);
			Rectangle rect2 = new Rectangle(1408,546,100,100); //(x,y,width,height)
			window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,18));
			window.drawString("Fusion", 1413 , 567);
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString("Has: "+player.getFStock(), 1412 , 591); //+0,+24
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString(co.getStocksRemaining()+" Left/25 Total", 1411 , 642); //+0,+75
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,13));
			window.drawString("Price:"+co.getStockPrice(), 1413 , 617); //+0,+50
		}
	}
	private void button5( Graphics window , Company co ) //America //blue
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1544,541,110,110); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		if (true)
		{
			Color c = new Color(28,35,168);
			window.setColor(c);
			Rectangle rect2 = new Rectangle(1549,546,100,100); //(x,y,width,height)
			window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,18));
			window.drawString("America", 1554 , 567);
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString("Has: "+player.getAStock(), 1554 , 591); //+0,+24
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString(co.getStocksRemaining()+" Left/25 Total", 1552 , 642); //+0,+75
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,13));
			window.drawString("Price:"+co.getStockPrice(), 1554 , 617); //+0,+50
		}
	}
	private void button6( Graphics window , Company co ) //Phoenix //purple
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1332,682,110,110); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		if (true)
		{
			Color c = new Color(102,16,160);
			window.setColor(c);
			Rectangle rect2 = new Rectangle(1337,687,100,100); //(x,y,width,height)
			window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,18));
			window.drawString("Phoenix", 1342 , 708);
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString("Has: "+player.getPStock(), 1342 , 732); //+0,+24
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString(co.getStocksRemaining()+" Left/25 Total", 1340 , 783); //+0,+75
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,13));
			window.drawString("Price:"+co.getStockPrice(), 1342 , 758); //+0,+50
		}
	}
	private void button7( Graphics window , Company co ) //Quantum //light blue
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1474,682,110,110); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		if (true)
		{
			Color c = new Color(1,120,133);
			window.setColor(c);
			Rectangle rect2 = new Rectangle(1479,687,100,100); //(x,y,width,height)
			window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,18));
			window.drawString("Quantum", 1484 , 708);
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString("Has: "+player.getQStock(), 1484 , 732); //+0,+24
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,11));
			window.drawString(co.getStocksRemaining()+" Left/25 Total", 1482 , 783); //+0,+75
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,13));
			window.drawString("Price:"+co.getStockPrice(), 1484 , 758); //+0,+50
		}
	}
	private void SellButton( Graphics window )
	{
		window.setColor(Color.GRAY);
		RoundRectangle2D r1 = new RoundRectangle2D.Double(1332,150,252,100,100,100);
		window.fillRoundRect((int)r1.getX(), (int)r1.getY(), (int)r1.getWidth(), (int)r1.getHeight(), (int)r1.getArcWidth(), (int)r1.getArcHeight());
		
		Color c = new Color(0,0,0);
		window.setColor(c);
		RoundRectangle2D r2 = new RoundRectangle2D.Double(1337,155,242,90,100,100);
		window.fillRoundRect((int)r2.getX(), (int)r2.getY(), (int)r2.getWidth(), (int)r2.getHeight(), (int)r2.getArcWidth(), (int)r2.getArcHeight());
		
		c = new Color(255,255,255);
		window.setColor(c);
		window.setFont(new Font("Times New Roman",Font.BOLD,72));
		window.drawString("SELL", 1370 , 225);
	}
	private void TradeButton( Graphics window )
	{
		window.setColor(Color.GRAY);
		RoundRectangle2D r1 = new RoundRectangle2D.Double(1190,275,252,100,100,100);
		window.fillRoundRect((int)r1.getX(), (int)r1.getY(), (int)r1.getWidth(), (int)r1.getHeight(), (int)r1.getArcWidth(), (int)r1.getArcHeight());
		
		Color c = new Color(0,0,0);
		window.setColor(c);
		RoundRectangle2D r2 = new RoundRectangle2D.Double(1195,280,242,90,100,100);
		window.fillRoundRect((int)r2.getX(), (int)r2.getY(), (int)r2.getWidth(), (int)r2.getHeight(), (int)r2.getArcWidth(), (int)r2.getArcHeight());
		
		c = new Color(255,255,255);
		window.setColor(c);
		window.setFont(new Font("Times New Roman",Font.BOLD,62));
		window.drawString("TRADE", 1207 , 345);
	}
	private void KeepButton( Graphics window )
	{
		window.setColor(Color.GRAY);
		RoundRectangle2D r1 = new RoundRectangle2D.Double(1474,275,252,100,100,100);
		window.fillRoundRect((int)r1.getX(), (int)r1.getY(), (int)r1.getWidth(), (int)r1.getHeight(), (int)r1.getArcWidth(), (int)r1.getArcHeight());
		
		Color c = new Color(0,0,0);
		window.setColor(c);
		RoundRectangle2D r2 = new RoundRectangle2D.Double(1479,280,242,90,100,100);
		window.fillRoundRect((int)r2.getX(), (int)r2.getY(), (int)r2.getWidth(), (int)r2.getHeight(), (int)r2.getArcWidth(), (int)r2.getArcHeight());
		
		c = new Color(255,255,255);
		window.setColor(c);
		window.setFont(new Font("Times New Roman",Font.BOLD,72));
		window.drawString("KEEP", 1510 , 350);
	}
	private void Instructions( Graphics window )
	{
		window.setColor(Color.GRAY);
		RoundRectangle2D r1 = new RoundRectangle2D.Double(1025,675,280,215,15,15);
		window.fillRoundRect((int)r1.getX(), (int)r1.getY(), (int)r1.getWidth(), (int)r1.getHeight(), (int)r1.getArcWidth(), (int)r1.getArcHeight());
		
		window.setColor(Color.BLACK);
		RoundRectangle2D r2 = new RoundRectangle2D.Double(1030,680,270,205,15,15);
		window.fillRoundRect((int)r2.getX(), (int)r2.getY(), (int)r2.getWidth(), (int)r2.getHeight(), (int)r2.getArcWidth(), (int)r2.getArcHeight());
		
		Color c = new Color(255,255,255);
		window.setColor(c);
		window.setFont(new Font("Times New Roman",Font.BOLD,24));
		
		ArrayList <String> i = new ArrayList<>(Arrays.asList(instruct.split("@")));
		
		window.drawString("Instructions: ", 1040 , 710);
		
		window.setColor(c);
		window.setFont(new Font("Times New Roman",Font.BOLD,16));
		
		int x = 730;
		for ( String z : i )
		{
			window.drawString(z, 1040 , x);
			x+=16;
		}
	}
	public void setInstruct(String s)
	{
		instruct = s;
	}
	public void StocksZero( Graphics window )
	{
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,18));
		window.drawString("Stock # To Buy:", 1670 , 660); //+19,+42
		
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1674,682,60,60); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		window.setColor(Color.BLACK);
		Rectangle rect2 = new Rectangle(1679,687,50,50); //(x,y,width,height)
		window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,36));
		window.drawString("0", 1693 , 724); //+19,+42
	}
	public void StocksOne( Graphics window )
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1739,682,60,60); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		window.setColor(Color.BLACK);
		Rectangle rect2 = new Rectangle(1744,687,50,50); //(x,y,width,height)
		window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,36));
		window.drawString("1", 1758 , 724); //+19,+42
	}
	public void StocksTwo( Graphics window )
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1674,747,60,60); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		window.setColor(Color.BLACK);
		Rectangle rect2 = new Rectangle(1679,752,50,50); //(x,y,width,height)
		window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,36));
		window.drawString("2", 1693 , 789); //+19,+42
	}
	public void StocksThree( Graphics window )
	{
		window.setColor(Color.GRAY);
		Rectangle rect1 = new Rectangle(1739,747,60,60); //(x,y,width,height)
		window.fillRect((int)rect1.getX(), (int)rect1.getY(), (int)rect1.getWidth(), (int)rect1.getHeight());
		
		window.setColor(Color.BLACK);
		Rectangle rect2 = new Rectangle(1744,752,50,50); //(x,y,width,height)
		window.fillRect((int)rect2.getX(), (int)rect2.getY(), (int)rect2.getWidth(), (int)rect2.getHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Comic Sans MS",Font.BOLD,36));
		window.drawString("3", 1758 , 789); //+19,+42
	}
	private void endGame( Graphics window )
	{
		if( endGame == true )
		{
			window.setColor(Color.GRAY);
			RoundRectangle2D r1 = new RoundRectangle2D.Double(1025,575,110,80,15,15);
			window.fillRoundRect((int)r1.getX(), (int)r1.getY(), (int)r1.getWidth(), (int)r1.getHeight(), (int)r1.getArcWidth(), (int)r1.getArcHeight());
				
			window.setColor(Color.BLACK);
			r1 = new RoundRectangle2D.Double(1030,580,100,70,15,15);
			window.fillRoundRect((int)r1.getX(), (int)r1.getY(), (int)r1.getWidth(), (int)r1.getHeight(), (int)r1.getArcWidth(), (int)r1.getArcHeight());
			
			window.setColor(Color.WHITE);
			window.setFont(new Font("Comic Sans MS",Font.BOLD,16));
			window.drawString("End Game", 1043 , 620); 
		}
	}
	public void setEndGame( boolean b )
	{
		endGame = b;
	}	
	public void cont ( Graphics window )
	{
		window.setColor(Color.GRAY);
		RoundRectangle2D r1 = new RoundRectangle2D.Double(1025,425,200,75,15,15);
		window.fillRoundRect((int)r1.getX(), (int)r1.getY(), (int)r1.getWidth(), (int)r1.getHeight(), (int)r1.getArcWidth(), (int)r1.getArcHeight());
		
		window.setColor(Color.BLACK);
		r1 = new RoundRectangle2D.Double(1030,430,190,65,15,15);
		window.fillRoundRect((int)r1.getX(), (int)r1.getY(), (int)r1.getWidth(), (int)r1.getHeight(), (int)r1.getArcWidth(), (int)r1.getArcHeight());
		
		window.setColor(Color.WHITE);
		window.setFont(new Font("Times New Roman",Font.BOLD,36));
		window.drawString("Continue", 1055, 473);
	}
}
