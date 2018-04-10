package model.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class FastRGB {
    public int width;
    public int height;
    private byte[] pixels;
    
   public FastRGB(BufferedImage image) {
        pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        width = image.getWidth();
        height = image.getHeight();
    }
    
    public int getRGB(int x, int y) {
        int pos = (y * 3 * width) + (x * 3);
        return ( (pixels[pos++] & 0xFF) << 16)+((pixels[pos++] & 0xFF) << 8)+((pixels[pos++] & 0xFF));
    }
}