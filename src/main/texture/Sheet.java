package main.texture;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;

public class Sheet
{	
	public static BufferedImage load( String fileName )
	{
		BufferedImage load = null;
		
		try
		{
			File file = new File( fileName );
			
			load = ImageIO.read(file);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");
			System.exit(0);
		}
		catch(IOException e)
		{
			System.out.println("Something happened when loading the image");
			System.exit(0);
		}
		
		return load;
	}
	
	public static BufferedImage[] split( String fileName )
	{
		BufferedImage load = null;
		
		try
		{
			File file = new File( fileName );
			
			load = ImageIO.read(file);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found");
			System.exit(0);
		}
		catch(IOException e)
		{
			System.out.println("Something happened when loading the image");
			System.exit(0);
		}
		
		Color background = new Color( load.getRGB( 0, 0 ), true );
		
		return parse( load, background );
	}
	
	public static BufferedImage[] parse( BufferedImage source )
	{
		List< BufferedImage > sprites = new ArrayList< BufferedImage >();
		boolean[][] searched = new boolean[ source.getWidth() ][ source.getHeight() ];
		Color background = new Color( source.getRGB( 0, 0 ), true );
		
		for( int x = 0; x < source.getHeight(); x++ )
		{
			for(int y = 0; y < source.getWidth(); y++ )
			{
				if( searched[ x ][ y ] == false )
				{
					if( source.getRGB( x, y ) != background.getRGB() )
					{
						sprites.add( findSprite( source, background, searched, new Point( x, y ) ) );
					}
					searched[ x ][ y ] = true;
				}
			}
		}
		
		BufferedImage[] imArray = new BufferedImage[ sprites.size() ];
		int max = -1;
		int may = -1;
		for( int x = 0; x < sprites.size(); x++ )
		{
			if( max < sprites.get(x).getWidth() ) max = sprites.get(x).getWidth();
			if( may < sprites.get(x).getHeight() ) may = sprites.get(x).getHeight();
		}
		
		for( int x = 0; x < sprites.size(); x++ )
		{			
			imArray[x] = new BufferedImage( max, may, BufferedImage.TYPE_INT_ARGB );
			imArray[x].getGraphics().drawImage( sprites.get(x),
					max - sprites.get(x).getWidth(), may - sprites.get(x).getHeight(), max, may,
					0, 0, sprites.get(x).getWidth(), sprites.get(x).getHeight(), 
					null);
		}
		
		return imArray;
	}
	
	public static BufferedImage[] parse( BufferedImage source, Color background )
	{
		List< BufferedImage > sprites = new ArrayList< BufferedImage >();
		boolean[][] searched = new boolean[ source.getWidth() ][ source.getHeight() ];
		
		for( int y = 0; y < source.getHeight(); y++ )
		{
			for(int x = 0; x < source.getWidth(); x++ )
			{
				if( source.getRGB( x, y ) != background.getRGB() && !searched[ x ][ y ] )
				{
					sprites.add( findSprite( source, background, searched, new Point( x, y ) ) );
				}
				searched[ x ][ y ] = true;
			}
		}
		
		BufferedImage[] imArray = new BufferedImage[ sprites.size() ];
		int max = -1;
		int may = -1;
		for( int x = 0; x < sprites.size(); x++ )
		{
			if( max < sprites.get(x).getWidth() ) max = sprites.get(x).getWidth();
			if( may < sprites.get(x).getHeight() ) may = sprites.get(x).getHeight();
		}
		
		for( int x = 0; x < sprites.size(); x++ )
		{			
			imArray[x] = new BufferedImage( max, may, BufferedImage.TYPE_INT_ARGB );
			imArray[x].getGraphics().drawImage( sprites.get(x),
					max - sprites.get(x).getWidth(), may - sprites.get(x).getHeight(), max, may,
					0, 0, sprites.get(x).getWidth(), sprites.get(x).getHeight(), 
					null);
		}
		
		return imArray;
	}
	
	private static BufferedImage findSprite( BufferedImage source, Color background, boolean[][] searched, Point loc )
	{
		Stack< Point > search = new Stack< Point >();
		search.add( loc );
		Point cur;
		BufferedImage sprite;
		int max, mix, may, miy;
		int lefx = Integer.MAX_VALUE;
		int rigx = -1;
		int topy = Integer.MAX_VALUE;
		int boty = -1;
		
		while( !search.isEmpty() )
		{
			cur = search.pop();
			
			if( cur.x <= 0 ) mix = 0; else mix = cur.x - 1;
			if( cur.x >= source.getWidth() - 1 ) max = source.getWidth() - 1; else max = cur.x + 1;
			if( cur.y <= 0 ) miy = 0; else miy = cur.y - 1;
			if( cur.y >= source.getHeight() - 1 ) may = source.getHeight() - 1; else may = cur.y + 1;
			
			if( mix < lefx ) lefx = mix;
			if( max > rigx ) rigx = max;
			if( miy < topy ) topy = miy;
			if( may > boty ) boty = may;
			
			for( int y = miy; y <= may; y++ )
			{
				for( int x = mix; x <= max; x++ )
				{
					if( searched[ x ][ y ] == false )
					{ 
						if( source.getRGB( x, y ) != background.getRGB() )
						{
							search.add( new Point( x, y ) ); 
						}
						searched[ x ][ y ] = true;
					}
				}
			}
		}
		
		sprite = new BufferedImage( rigx - lefx, boty - topy, BufferedImage.TYPE_INT_ARGB );
		
		sprite.getGraphics().drawImage( source, 
					0, 0, sprite.getWidth(), sprite.getHeight(),
					lefx, topy, rigx, boty, 
					null);
			
		return sprite;
	}
}
