package com.bailun.gogirl.util.qrcode;


import jp.sourceforge.qrcode.data.QRCodeImage;
 
import java.awt.image.BufferedImage;
 
public class QRCodeImg implements QRCodeImage {
    BufferedImage bufImg;
 
    public QRCodeImg(BufferedImage bufImg) {
        this.bufImg = bufImg;
    }
 
    @Override
    public int getHeight() {
        return bufImg.getHeight();
    }
 
    @Override
    public int getPixel(int x, int y) {
        return bufImg.getRGB(x, y);
    }
 
    @Override
    public int getWidth() {
        return bufImg.getWidth();
    }
}