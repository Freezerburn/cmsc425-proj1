package main.texture;

import stuff.ThreeTuple;
import stuff.TwoTuple;
import stuff.Vector2;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

/**
 * Author: FreezerburnVinny
 * Date: 2/10/12
 * Time: $(TIME}
 */
public class AutoFindTileMapLoader implements TileMapLoader {
    protected BasicTextureLoader loader;
    public int tilesWide, tilesHigh;
    public SimpleTexture[] textures;

    public AutoFindTileMapLoader(String file, String managerHandle, boolean shouldRemoveBackground) {
        loader = new BasicTextureLoader(file, managerHandle, shouldRemoveBackground, true);
    }

    @SuppressWarnings("ConstantConditions")
    private static TwoTuple<Point, Point> extractSprite(BufferedImage source, Color background, boolean[][] searched, Point loc) {
        Stack<Point> search = new Stack<Point>();
        search.add(loc);
        Point cur;
        int max, mix, may, miy;
        int lefx = Integer.MAX_VALUE;
        int rigx = -1;
        int topy = Integer.MAX_VALUE;
        int boty = -1;
        int SEARCH_SIZE = 3;

        while (!search.isEmpty()) {
            cur = search.pop();

            if (cur.x <= SEARCH_SIZE - 1) mix = 0;
            else mix = cur.x - SEARCH_SIZE;
            if (cur.x >= source.getWidth() - SEARCH_SIZE) max = source.getWidth() - SEARCH_SIZE;
            else max = cur.x + SEARCH_SIZE;
            if (cur.y <= SEARCH_SIZE) miy = 0;
            else miy = cur.y - SEARCH_SIZE;
            if (cur.y >= source.getHeight() - SEARCH_SIZE) may = source.getHeight() - SEARCH_SIZE;
            else may = cur.y + SEARCH_SIZE;

            if (mix < lefx) lefx = mix;
            if (max > rigx) rigx = max;
            if (miy < topy) topy = miy;
            if (may > boty) boty = may;

            for (int y = miy; y <= may; y++) {
                for (int x = mix; x <= max; x++) {
                    if (!searched[x][y]) {
                        if (source.getRGB(x, y) != background.getRGB()) {
                            search.add(new Point(x, y));
                        }
                        searched[x][y] = true;
                    }
                }
            }
        }

        return new TwoTuple<Point, Point>(new Point(lefx, topy), new Point(rigx, boty));
    }

    protected java.util.List<ThreeTuple<Vector2, Vector2, Vector2>> parse(BufferedImage image) {
        ArrayList<ThreeTuple<Vector2, Vector2, Vector2>> ret = new ArrayList<ThreeTuple<Vector2, Vector2, Vector2>>();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        // boolean values default to false, so no need to set them.
        boolean[][] searched = new boolean[imageWidth][imageHeight];
        // Stack should be ok? Doubt we need a Queue.
        Color background = new Color(image.getRGB(0, 0), true);

        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                if (!searched[x][y]) {
                    if (image.getRGB(x, y) != background.getRGB()) {
                        TwoTuple<Point, Point> found = extractSprite(image, background, searched, new Point(x, y));
                        int width = found.two.x - found.one.x;
                        int height = found.two.y - found.one.y;
                        Vector2 loc = new Vector2(found.one.x / (float) imageWidth, found.one.y / (float) imageHeight);
                        Vector2 size = new Vector2(width / (double) imageWidth,
                                width / (double) imageHeight);
                        ret.add(new ThreeTuple<Vector2, Vector2, Vector2>(loc, size,
                                new Vector2((double) width, (double) height)));
                    }
                    searched[x][y] = true;
                }
            }
        }

        return ret;
    }

    // TODO: Fix this horribly broken auto-loader
    @Override
    public void run() {
        loader.run();
        java.util.List<ThreeTuple<Vector2, Vector2, Vector2>> coords = parse(loader.image);
        textures = new SimpleTexture[coords.size()];
        Collections.sort(coords, new Comparator<ThreeTuple<Vector2, Vector2, Vector2>>() {
            @Override
            public int compare(ThreeTuple<Vector2, Vector2, Vector2> o1, ThreeTuple<Vector2, Vector2, Vector2> o2) {
                int xResultLocation = Double.compare(o1.one.x, o2.one.x);
                int yResultLocation = Double.compare(o1.one.y, o2.one.y);
                if (xResultLocation < 0) {
                    return yResultLocation;
                } else if (xResultLocation > 0) {
                    return yResultLocation;
                }
                return yResultLocation;
            }
        });
        // Sort the list of coordinates, so that it organizes them in order of x position, then y position. So
        // it should basically be like flattening all of the rows into a long line.
        // Then figure out some way of organizing things into orderly columns/rows, so we can have a "square"
        // grid to grab images from. Then just turn all of these into a texture of arrays and set the variable
        // to that array.
        for (int i = 0; i < coords.size(); i++) {
            ThreeTuple<Vector2, Vector2, Vector2> tt = coords.get(i);
            textures[i] = new SimpleTexture(loader.texture.getTarget(),
                    loader.texture.getName(),
                    loader.texture.getManagerHandle(),
                    tt.three.x, tt.three.y,
                    tt.one.x, tt.one.x + tt.two.x, tt.one.y, tt.one.y + tt.two.y);
//            System.err.println( "Location: " + tt.one + ", Size: " + tt.two +
//                    ", realSize:" + tt.three );
        }

        // Destroy the image we were using, as we don't need it anymore.
        loader.image.flush();
    }

    @Override
    public SimpleTexture get(int x, int y) {
        return textures[y * tilesWide + x];
    }

    @Override
    public SimpleTexture[] getAll() {
        return textures;
    }

    @Override
    public int getRows() {
        return tilesWide;
    }

    @Override
    public int getColumns() {
        return tilesHigh;
    }
}
