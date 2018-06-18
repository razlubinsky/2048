import java.awt.image.BufferedImage;

public class SpriteSheet 
{
	private BufferedImage sheet;
	
	public SpriteSheet(BufferedImage sheet)
	{
		this.sheet = sheet;
	}
	//cut from the sprite sheet each image by id
	public BufferedImage crop(int id, int width, int height)
	{
		return sheet.getSubimage(id*width, 0, width, height);
	}
}
 