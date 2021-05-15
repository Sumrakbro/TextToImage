package com.company;

import java.awt.*;
import java.io.IOException;

class Main {

    private static ImageService imageService = new ImageService();

    public static void main(String[] args) throws IOException {
        String text = "Какой-то длинный текст. Преобразователь в черное белое. ... Эти классы задают систему координат изображения, предоставляют доступ к отдельным ... Я пытаюсь скопировать цвета из BufferedImage в другой bufferedImage, и ниже приведен мой код";
        System.out.println(imageService.addTextToImage(text, 5, 5, 400, 1,
                TextMode.LEFT, "Calibri", Font.ITALIC, 16, Color.BLUE,"text.jpg"));
        System.out.println(imageService.addTextToImage(text, 5, 5, 400, 1,
                TextMode.LEFT, "Calibri", Font.ITALIC, 16, Color.BLUE));

    }

}
