package VideoServer.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public class CheckImageGenerator {
    private static final int WIDTH = 120;
    private static final int HEIGHT = 40;
    private static final String ImageType="png";

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


    public static String GenerateString(Integer length) {
        StringBuilder res = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            res.append(CHARACTERS.charAt(index));
        }
        return res.toString();
    }

    public static byte[] Generate(String result) throws IOException {
        String checkCode = result;
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 设置背景颜色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 设置边框颜色
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);

        // 绘制验证码
        Font font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int x = (WIDTH - fm.stringWidth(checkCode)) / 2;
        int y = (HEIGHT - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(checkCode, x, y);

        // 添加噪点
        Random random = new Random(System.currentTimeMillis());
        int noiseCount = WIDTH * HEIGHT / 10;
        for (int i = 0; i < noiseCount; i++) {
            int posX = random.nextInt(WIDTH);
            int posY = random.nextInt(HEIGHT);
            int rgb = GetRandomColor().getRGB();
            image.setRGB(posX, posY, rgb);
        }

        // 添加干扰线
        int lineCount = 5;
        for (int i = 0; i < lineCount; i++) {
            int startX = random.nextInt(WIDTH);
            int startY = random.nextInt(HEIGHT);
            int endX = random.nextInt(WIDTH);
            int endY = random.nextInt(HEIGHT);
            g.drawLine(startX, startY, endX, endY);
        }

        g.dispose();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(image,ImageType,output);
        output.close();
        return output.toByteArray();
    }

    private static Color GetRandomColor() {
        Random random = new Random();
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }
}
