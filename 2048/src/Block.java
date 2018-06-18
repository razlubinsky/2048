import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Block extends JButton 
{
	
	private static final long serialVersionUID = 1L;
	private int id = 0;
	
	public Block()
	{
		
	}

	//add mouse listener to each block
	public Block(int id)
	{
		this.setId(id);
	}

	public int getId()
	{
		return this.id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public void render()
	{
		this.setIcon(new ImageIcon(Tile.type[id]));
	}

}	
	
		
		


	
