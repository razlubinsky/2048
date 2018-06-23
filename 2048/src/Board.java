import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;

public class Board extends JFrame implements Runnable,KeyListener
{
	
	private static final long serialVersionUID = 1L;
	private static int ind_x=4;
	private static int ind_y=4;
	
	private int left = 0;
	private int right = 0;
	private int up = 0;
	private int down = 0;
	boolean endGame = false;
	boolean isStuck = false;
	private boolean isEmptyTile = false;
    private int id = 0;
	private static Block[][] block = new Block[ind_x][ind_y];
	private static Image screen;
	private static Dimension size = new Dimension(ind_x*Tile.getTileWidth() +10 ,ind_y*Tile.getTileHeight() +10);
	public Board()
	{
		this.setSize(size.width,size.height);
		this.addKeyListener(this);
		setFocusable(true);
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		Dimension dim = tk.getScreenSize();
		
		int xPos= (dim.width /2 ) - (this.getWidth() /2);
		int yPos= (dim.height /2 ) - (this.getHeight() /2);
			
		this.setLocation(xPos, yPos);	
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setTitle("2048");
		
		this.setVisible(true);
		GridLayout grid = new GridLayout(ind_x, ind_y);
		this.setLayout(grid);
		

		//adding (and creating)the blocks to the board
		for (int x=0; x<ind_x;x++)
		{
			for(int y=0;y<ind_y;y++)
			{
				block[x][y] = new Block(id);
				this.add(block[x][y]);
			}
		}
		generateLevel();
	}
	public void generateLevel()
	{
		for (int x = 0 ; x<ind_x ; x++)
		{
			for (int y = 0; y<ind_y ; y++)
			{
				block[x][y].setId(Tile.getId(0));
			}
		}
		addBlock();
		addBlock();
	}
	public void addBlock()
	{
		int x,y;
		boolean isEmptyTile = true;
		Random rand = new Random();
		while (isEmptyTile)
		{
			x = rand.nextInt(4);
			y = rand.nextInt(4);
			if (block[x][y].getId() == Tile.getId(0))
			{
				isEmptyTile = false;
				block[x][y].setId(1);
			}
		}
	}
	public static int getInd_x()
	{
		return ind_x;
	}
	public static int getInd_Y()
	{
		return ind_y;
	}

	public void start()
	{
		new Tile(); 
		new Thread(this).start();
	}

	@Override
	public void run() 
	{
		screen = createVolatileImage(size.width, size.height);
		render();
	}
	
	public void render(Graphics g)
	{
		for (int x=0; x<ind_x;x++)
		{
			for(int y=0;y<ind_y;y++)
			{
				block[x][y].render();
			}
		}
		
	}
	public void render()
	{
		Graphics g = screen.getGraphics();
		this.render(g);
		
		g = getGraphics();
		g.drawImage(screen, 5, 30, size.width , size.height , 0, 0, size.width , size.height, null);
	}
    public static void main(String args[]) 
    {
		Board board = new Board();
		board.start();
        
    }
    public void metZero(int row,int col,int targetRow,int targetCol)
    {
		block[targetRow][targetCol].setId(block[row][col].getId());
		block[row][col].setId(Tile.getId(0));    	
    }
    public void  metIdentical(int row,int col,int targetRow,int targetCol)
    {
    	if (block[row][col].getId()!= Tile.getId(11))
    	{
    		block[targetRow][targetCol].setId(block[row][col].getId()+1);
			block[row][col].setId(Tile.getId(0));    	
    	}
    }
    public void setIsEmptyTile(boolean isEmptyTile)
    {
    	this.isEmptyTile = isEmptyTile;
    }
    public void skipEmptyTiles(int targetRow,int targetCol)
    {
    	
    }
    public void movements(int left,int right,int up, int down)
    {
    	if (endGame == false)
    	{
	    	//run over the rows
	    	for (int row=up+(2*down); ((row < 4) && (down == 0)) || ((row>=0) && (down == 1)); row=row + 1 - (2*down))
			{
	    		//run over the columns
				for (int col=left+(2*right); ((col< 4) && (right == 0)) || ((col>=0) && (right==1)); col = col+1-(2*right) )
				{
					//check for tiles with numbers
					if (block[row][col].getId() != Tile.getId(0))
					{
						int targetRow = row-up+down;
						int  targetCol = col-left+right;
						//skip the empty tiles direct to the occupied ones
						skipEmptyTiles(targetRow,targetCol);
						while  		(
							    		(
							    				(    (targetCol > 0) && (targetCol<3) && (left+right >0)  ) 
							    				|| 
							    				(    (targetRow > 0) && (targetRow<3) && (up+ down >0)    )
							    		)
							    		&& 
							    		(     block[targetRow][targetCol].getId() == Tile.getId(0)        )
							    	)
	
						{
							targetCol=targetCol-left+right;
							targetRow=targetRow-up+down;
						}
						//results
						
						//if destination tile is empty
						if(block[targetRow][targetCol].getId() == Tile.getId(0))
						{
							if(isStuck== false)
								metZero(row,col,targetRow,targetCol);
							setIsEmptyTile(true);
						}
						//if destination tile is not empty
						else
						{
							//if destination tile is equal to current tile
							if(block[targetRow][targetCol].getId() == block[row][col].getId())	
							{
								if(isStuck== false)
									metIdentical(row, col, targetRow, targetCol);
								setIsEmptyTile(true);
							}
							//if destination tile isn't equal to current tile
							else 
							{	
								if(
										(              (targetCol+left-right != col)                 &&                 (left+right>0)	                 ) 
										|| 
										(          (targetRow+up-down != row)           &&             (down+up>0)                    )
								  )
								{
									if(isStuck== false)
										metZero(row,col,targetRow+up-down,targetCol+left-right);
									setIsEmptyTile(true);
								}
							}
						}
					}
				}
			}
		}
    }
    public void victory()
    {
    	endGame = true;
    	this.setTitle("2048 - WON");    	
    }
    public void lost()
    {
    	endGame = true;
    	this.setTitle("2048 - LOST");
    }
    public boolean checkVictory()
    {
    	for (int row = 0;row<4;row++)
    	{
    		for (int col =0; col<4;col++)
    		{
    			if (block[row][col].getId()==Tile.getId(11))
    				return true;
    		}
    	}
    	return false;
    }
    public void initDirections()
    {
    	left = 0;
    	right = 0;
    	up = 0;
    	down = 0;
    }
    public boolean checkLost()
    {
    	isStuck = true;
    	initDirections();
    	left = 1;
    	movements(left,right,up,down);
    	left = 0;
    	right = 1;
    	movements(left,right,up,down);
    	right = 0;
    	up = 1;
    	movements(left,right,up,down);
    	up = 0;
    	down = 1;
    	movements(left,right,up,down);
    	down = 0;
    	if(!isEmptyTile)
    		return true;
    	isStuck = false;
    	setIsEmptyTile(false);
    	return false;
    }
    public void checkEndGame()
    {
    	if (checkLost())
    		lost();
    	if (checkVictory())
    		victory();
    }
    public void endTurn()
    {
    	render();
		if(isEmptyTile)
		{
			addBlock();
			render();
			setIsEmptyTile(false);
		}
		checkEndGame();
    }
	@Override
	public void keyPressed(KeyEvent e) 
	{
		if(e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			left = 1;
			movements(left,right,up,down);
			endTurn();
			left = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			right = 1;
			movements(left,right,up,down);
			endTurn();
			right = 0;

		}
		if(e.getKeyCode() == KeyEvent.VK_UP)
		{
			up = 1;
			movements(left,right,up,down);
			endTurn();
			up = 0;
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN)
		{
			down = 1;
			movements(left,right,up,down);
			endTurn();
			down = 0;
		}
	}
	@Override
	public void keyReleased(KeyEvent e) 
	{
		
		
	}
	@Override
	public void keyTyped(KeyEvent e) 
	{
		
	}
}
