package com.carson;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageMaker {

    @Deprecated
    public File makeImage(/*List<MathBot> bots*/) throws IOException {
        File image = new File("img/" + (int)(Math.random()*1000)+".png");
        BufferedImage im = new BufferedImage(10,10,1);
        Graphics2D g2 = im.createGraphics();
        im.setRGB(5,5,Color.RED.getRGB());

        ImageIO.write(im, "png", image);
        return image;
    }


    /**
     *
     * @param bots #size() is a square number
     * @return
     * @throws IOException
     */
    public File makeImage(List<MathBot> bots) throws IOException {
        return makeImage(bots,(int)(Math.random()*1000) + "");
    }

    public File makeImage(List<MathBot> bots, String name) throws IOException {
        if(Math.pow((double)(int)Math.sqrt(bots.size()),2) != bots.size()){
            throw new UnsupportedOperationException("YOU NEED TO HAVE A SQUARE NUMBER SIZED LIST");
        }
        if(MathBot.base == -1) {
            int max = 0;
            for (MathBot bot : bots) {
                int score = Math.abs((int) (bot.getScore(Main.pairs) + 0.5));//can safely cast to int cause it rounds up
                if (score > max) max = score;
            }
            MathBot.base = max;
            System.out.println("set base:" + max);
        }
        List<Plotable> points = new ArrayList<>();
        for(MathBot bot : bots){
            points.add(new Plotable(bot.toRGB(Main.pairs)));
        }
        File image = new File("img/" + name + ".png");
        image.createNewFile();
        BufferedImage im = new BufferedImage((int)Math.sqrt(bots.size()),(int)Math.sqrt(bots.size()), IndexColorModel.BITMASK);
        int i = 0;
        for(int y = 0;y<Math.sqrt(bots.size());y++){
            for(int x = 0;x<Math.sqrt(bots.size());x++){
                im.setRGB(x,y,points.get(i).value);
                i++;
            }
        }
        im = scale(im);
        ImageIO.write(im, "png", image);
        return image;
    }

    private BufferedImage scale(BufferedImage original) {
        int newWidth = 750;
        int newHeight = 750;

        BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
        Graphics2D g = resized.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );

        g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(),
                original.getHeight(), null);
        g.dispose();
        return resized;
    }


}
