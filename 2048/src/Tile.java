import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class Tile extends JComponent
{

	private static final long serialVersionUID = 1L;
	private static int width = 160;
	private static int height = 160;
	private static int[] id = {0,1,2,3,4,5,6,7,8,9,10,11};
	public static BufferedImage[] type = new BufferedImage[12];

	public Tile()
	{
		try 
		{
			SpriteSheet sheet = new SpriteSheet(ImageIO.read(new File("res/tile_set.png")));
			
			for (int i = 0; i<12 ; i++)
				Tile.type[i] = sheet.crop(id[i],width,height);
		}
		catch(Exception e)
		{
			
		}
	}
	public static int getTileWidth()
	{
		return width;
	}
	public static int getTileHeight()
	{
		return height;
	}
	public static BufferedImage getType(int index)
	{
		return type[index];
	}
	
	public static void setId(int index, int value)
	{
		id[index] = value;
	}
	public static int getId(int index)
	{
		return id[index];
	}	
}
