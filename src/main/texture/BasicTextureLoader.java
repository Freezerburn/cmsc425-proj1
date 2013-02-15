package main.texture;

import org.lwjgl.opengl.OpenGLException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.lwjgl.opengl.GL11.*;

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

    protected ByteBuffer convertBufferedImageToByteBuffer(BufferedImage image) {
        ByteBuffer buffer = ByteBuffer.allocateDirect(width * height * 4);
        buffer.order(ByteOrder.nativeOrder());
        byte[] bytes = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        switch (image.getType()) {
            case BufferedImage.TYPE_3BYTE_BGR:
                convertBGRBufferedImageToByteBuffer(buffer, bytes);
                break;
            case BufferedImage.TYPE_4BYTE_ABGR:
                convertABGRBufferedImageToByteBuffer(buffer, bytes);
                break;
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
                convertBGRBufferedImageToByteBuffer(buffer, bytes);
                break;
            case BufferedImage.TYPE_INT_ARGB:
                convertARGBBufferedImageToByteBuffer(buffer, bytes);
                break;
            case BufferedImage.TYPE_INT_BGR:
                convertBGRBufferedImageToByteBuffer(buffer, bytes);
                break;
            case BufferedImage.TYPE_INT_RGB:
                convertRGBBufferedImageToByteBuffer(buffer, bytes);
                break;
            case 12:
                convertRGBBufferedImageToByteBuffer(buffer, bytes);
                break;
            default:
                throw new OpenGLException("Unsupported image type: " + image.getType());
        }
        return buffer;
    }

    protected void convertARGBBufferedImageToByteBuffer(ByteBuffer buffer, byte[] bytes) {
        byte backgrounda = (byte) 255;
        byte backgroundr = (byte) 255;
        byte backgroundg = (byte) 255;
        byte backgroundb = (byte) 255;
        for (int i = 0; i < bytes.length; i += 4) {
            byte alpha = bytes[i];
            byte red = bytes[i + 1];
            byte green = bytes[i + 2];
            byte blue = bytes[i + 3];
            if (mShouldRemoveBackground) {
                if (i == 0) {
                    backgrounda = alpha;
                    backgroundr = red;
                    backgroundg = green;
                    backgroundb = blue;
                } else if (alpha == backgrounda && red == backgroundr &&
                        green == backgroundg && blue == backgroundb) {
                    alpha = 0;
                }
            }
            buffer.put(red);
            buffer.put(green);
            buffer.put(blue);
            buffer.put(alpha);
        }
        buffer.rewind();
    }

    protected void convertABGRBufferedImageToByteBuffer(ByteBuffer buffer, byte[] bytes) {
        byte backgrounda = (byte) 255;
        byte backgroundr = (byte) 255;
        byte backgroundg = (byte) 255;
        byte backgroundb = (byte) 255;
//        System.err.println( buffer.limit() );
        for (int i = 0; i < bytes.length; i += 4) {
            byte alpha = bytes[i];
            byte blue = bytes[i + 1];
            byte green = bytes[i + 2];
            byte red = bytes[i + 3];
            if (mShouldRemoveBackground) {
                if (i == 0) {
                    backgrounda = alpha;
                    backgroundr = red;
                    backgroundg = green;
                    backgroundb = blue;
                } else if (alpha == backgrounda && red == backgroundr &&
                        green == backgroundg && blue == backgroundb) {
                    alpha = 0;
                }
            }
            buffer.put(red);
            buffer.put(green);
            buffer.put(blue);
            buffer.put(alpha);
        }
        buffer.rewind();
    }

    protected void convertBGRBufferedImageToByteBuffer(ByteBuffer buffer, byte[] bytes) {
        byte backgrounda = (byte) 255;
        byte backgroundr = (byte) 255;
        byte backgroundg = (byte) 255;
        byte backgroundb = (byte) 255;
        for (int i = 0; i < bytes.length; i += 3) {
            byte blue = bytes[i];
            byte green = bytes[i + 1];
            byte red = bytes[i + 2];
            byte alpha = (byte) 0xFF;
            buffer.put(red);
            buffer.put(green);
            buffer.put(blue);
            if (mShouldRemoveBackground) {
                if (i == 0) {
                    backgrounda = alpha;
                    backgroundr = red;
                    backgroundg = green;
                    backgroundb = blue;
                } else if (alpha == backgrounda && red == backgroundr &&
                        green == backgroundg && blue == backgroundb) {
                    alpha = 0;
                }
            }
            buffer.put(alpha);
        }
        buffer.rewind();
    }

    protected void convertRGBBufferedImageToByteBuffer(ByteBuffer buffer, byte[] bytes) {
        byte backgrounda = (byte) 255;
        byte backgroundr = (byte) 255;
        byte backgroundg = (byte) 255;
        byte backgroundb = (byte) 255;
        for (int i = 0; i < bytes.length; i += 3) {
            byte red = bytes[i];
            byte green = bytes[i + 1];
            byte blue = bytes[i + 2];
            byte alpha = (byte) 0xFF;
            buffer.put(red);
            buffer.put(green);
            buffer.put(blue);
            if (mShouldRemoveBackground) {
                if (i == 0) {
                    backgrounda = alpha;
                    backgroundr = red;
                    backgroundg = green;
                    backgroundb = blue;
                } else if (alpha == backgrounda && red == backgroundr &&
                        green == backgroundg && blue == backgroundb) {
                    alpha = 0;
                }
            }
            buffer.put(alpha);
        }
        buffer.rewind();
    }

    protected int nextPowerOf2(int num) {
        int ret = 2;
        while (ret < num) ret *= 2;
        return ret;
    }

    protected int genTextureFromBufferedImage(BufferedImage image) {
        int tex = -1;
        try {
//            width = nextPowerOf2( image.getWidth() );
//            height = nextPowerOf2( image.getHeight() );
            width = image.getWidth();
            height = image.getHeight();
            ByteBuffer imageBuffer = convertBufferedImageToByteBuffer(image);
            tex = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, tex);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height,
                    0, GL_RGBA, GL_UNSIGNED_BYTE, imageBuffer);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
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
//                System.err.println( fileName );
                image = ImageIO.read(new URL(fileName));
            }
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
