package main.texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.OpenGLException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

/**
 * User: FreezerburnVinny
 * Date: 1/1/12
 * Time: 5:59 PM
 */
public class BasicTextureLoader implements Runnable {
    public SimpleTexture texture;
    public BufferedImage image;
    public int width, height;
    protected String mFile, managerHandle;
    protected boolean mShouldRemoveBackground, mKeepBufferedImage;

    public BasicTextureLoader(String file, String managerHandle, boolean shouldRemoveBackground) {
        mFile = file;
        this.managerHandle = managerHandle;
        this.texture = null;
        this.width = 0;
        this.height = 0;
        this.mShouldRemoveBackground = shouldRemoveBackground;
        this.mKeepBufferedImage = false;
    }

    public BasicTextureLoader(String file, String managerHandle, boolean shouldRemoveBackground, boolean keepBufferedImage) {
        mFile = file;
        this.managerHandle = managerHandle;
        this.texture = null;
        this.width = 0;
        this.height = 0;
        this.mShouldRemoveBackground = shouldRemoveBackground;
        this.mKeepBufferedImage = keepBufferedImage;
    }

    protected ByteBuffer newConvertImage(BufferedImage image) {
        int[] pixels = new int[image.getWidth() * image.getHeight()];
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        for(int y = 0; y < image.getHeight(); y++){
            for(int x = 0; x < image.getWidth(); x++){
                int pixel = pixels[y * image.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));     // Red component
                buffer.put((byte) ((pixel >> 8) & 0xFF));      // Green component
                buffer.put((byte) (pixel & 0xFF));               // Blue component
                buffer.put((byte) ((pixel >> 24) & 0xFF));    // Alpha component. Only for RGBA
            }
        }

        buffer.flip(); //FOR THE LOVE OF GOD DO NOT FORGET THIS
        return buffer;
    }

    protected int genTextureFromBufferedImage(BufferedImage image) {
        int tex = -1;
        try {
            width = image.getWidth();
            height = image.getHeight();
            ByteBuffer imageBuffer = newConvertImage(image);
            tex = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, tex);

            // These parameters make the image actually look nice and look like the image with no artifacts.
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                    0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            glDeleteTextures(tex);
            tex = -1;
        } catch (OpenGLException e) {
            e.printStackTrace();
            glDeleteTextures(tex);
            tex = -1;
        }
        return tex;
    }

    protected int genTexture(String file) {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                String fileName = System.getProperty("user.dir");
                fileName = fileName.substring(2);
                fileName = "file:" + fileName + "\\" + file;
                image = ImageIO.read(new URL(fileName));
            } else {
                String fileName = "file:" + System.getProperty("user.dir") + "/" + file;
                image = ImageIO.read(new URL(fileName));
            }
            BufferedImage compatImage = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration()
                    .createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());
            Graphics2D g = compatImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            System.out.println(image);
            image = compatImage;
            System.out.println(image);
            return genTextureFromBufferedImage(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // If we reach here, an error happened
        return -1;
    }

    @Override
    public void run() {
        texture = new SimpleTexture(GL_TEXTURE_2D, genTexture(mFile), managerHandle);
        if (!mKeepBufferedImage) {
            image.flush();
            image = null;
        }
        texture.setWidth(width);
        texture.setHeight(height);
        System.out.println(width + ", " + height + ", " + texture.getName());
    }
}
