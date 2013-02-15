package main.texture;

/**
 * Author: FreezerburnVinny
 * Date: 2/11/12
 * Time: $(TIME}
 */
public interface TileMapLoader extends Runnable {
    public SimpleTexture get( int x, int y );
    public SimpleTexture[] getAll();
    public int getRows();
    public int getColumns();
}
