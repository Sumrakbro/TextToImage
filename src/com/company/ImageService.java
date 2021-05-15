package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class ImageService {

    private BufferedImage bufferedImage;
    private File result;

    /**
     * Если нет фона и желаемого размера выходной картинки
     */
    public ImageService() {
        try {
            createNewImage(500, 500);
            bufferedImage = ImageIO.read(new File("input.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Если есть желаемые размеры картинки
     *
     * @param imageWidth
     * @param imageHeight
     */
    public ImageService(int imageWidth, int imageHeight) {
        try {
            createNewImage(imageWidth, imageHeight);
            bufferedImage = ImageIO.read(new File("input.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Если есть готовый фон для текста
     *
     * @param backgroundFileName
     */
    public ImageService(String backgroundFileName) {
        try {
            bufferedImage = ImageIO.read(new File(backgroundFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сохранение картинки с текстом с указанием местоположения и названия
     *
     * @param fileName
     */
    private File saveAs(String fileName) {
        return saveImage(new File(fileName));
    }

    /**
     * Сохранение картинки с текстом в корне и с дефолтным названием
     */
    private File saveAs() {
        return saveImage(new File("output.jpg"));
    }

    /**
     * Возвращает расширение файла
     *
     * @param file
     * @return
     */
    private String getFileType(File file) {
        String fileName = file.getName();
        int idx = fileName.lastIndexOf(".");
        if (idx == -1) {
            throw new RuntimeException("Invalid file name");
        }

        return fileName.substring(idx + 1);
    }

    /**
     * Сохранение картинки
     *
     * @param file
     */
    private File saveImage(File file) {
        try {
            ImageIO.write(bufferedImage, getFileType(file), file);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Создание картинки с текстом в определенном месте
     *
     * @param textForAdd
     * @param x_cord-                   координата левого края текста
     * @param y_cord-                   координата верхнего края текста
     * @param columnWidth-              ширина колонки с текстом
     * @param transparency-прозрачность
     * @param mode-                     тип выравнивания
     * @param textStyle-                шрифт
     * @param textFont
     * @param textSize
     * @param color
     * @param pathToSave
     */
    public File addTextToImage(String textForAdd, int x_cord, int y_cord, int columnWidth,
                               float transparency, TextMode mode, String textStyle,
                               int textFont, int textSize, Color color, String pathToSave) {
        addText(textForAdd, x_cord, y_cord, columnWidth, transparency, mode, textStyle, textFont, textSize, color);
        return saveAs(pathToSave);
    }

    /**
     * Создание картинки с текстом в дефолтном месте
     *
     * @param textForAdd
     * @param x_cord-                   координата левого края текста
     * @param y_cord-                   координата верхнего края текста
     * @param columnWidth-              ширина колонки с текстом
     * @param transparency-прозрачность
     * @param mode-                     тип выравнивания
     * @param textStyle-                шрифт
     * @param textFont
     * @param textSize
     * @param color
     */
    public File addTextToImage(String textForAdd, int x_cord, int y_cord, int columnWidth,
                               float transparency, TextMode mode, String textStyle,
                               int textFont, int textSize, Color color) {
        addText(textForAdd, x_cord, y_cord, columnWidth, transparency, mode, textStyle, textFont, textSize, color);
        return saveAs();
    }

    /**
     * Добавление текста на картинку
     *
     * @param textForAdd
     * @param x_cord-                   координата левого края текста
     * @param y_cord-                   координата верхнего края текста
     * @param columnWidth-              ширина колонки с текстом
     * @param transparency-прозрачность
     * @param mode-                     тип выравнивания
     * @param textStyle-                шрифт
     * @param textFont
     * @param textSize
     * @param color
     */
    private void addText(String textForAdd, int x_cord, int y_cord, int columnWidth,
                         float transparency, TextMode mode, String textStyle, int textFont, int textSize, Color color) {
        FontMetrics fontMetrics = createFontMetrics(textStyle, textFont, textSize);
        int lineHeight = fontMetrics.getHeight();
        String[] words = textForAdd.split(" ");
        StringBuilder line = new StringBuilder();
        List lines = new ArrayList();
        for (String word : words) {
            if (fontMetrics.stringWidth(line + word) > columnWidth) {
                lines.add(line.toString());
                line = new StringBuilder();
            }

            line.append(word).append(" ");
        }
        lines.add(line.toString());
        for (int i = 0; i < lines.size(); i++) {
            addTextLineToImage((String) lines.get(i),
                    x_cord, lineHeight + y_cord + i * lineHeight,
                    columnWidth, i == (lines.size() - 1),
                    transparency, mode, textStyle, textFont, textSize, color);
        }
    }


    /**
     * Задание параметров шрифта
     *
     * @param textStyle
     * @param textFont
     * @param textSize
     * @return
     */
    private FontMetrics createFontMetrics(String textStyle, int textFont, int textSize) {
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(Color.BLACK);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(new Font(textStyle, textFont, textSize));
        g.dispose();
        return g.getFontMetrics();
    }

    /**
     * Создаёт фон для картинки
     *
     * @param imageWidth
     * @param imageHeight
     * @throws IOException
     */
    private void createNewImage(int imageWidth, int imageHeight) throws IOException {
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, imageWidth, imageHeight);
        ImageIO.write(image, "jpg", new File("input.jpg"));

    }

    /**
     * Добавляет строку в нужное место картинки в зависмости от параметров
     *
     * @param text
     * @param xCord
     * @param yCord
     * @param columnWidth
     * @param isLastLine
     * @param transparency
     * @param mode
     * @param textStyle
     * @param textFont
     * @param textSize
     * @param color
     */
    private void addTextLineToImage(String text, int xCord, int yCord, int columnWidth, boolean isLastLine,
                                    float transparency, TextMode mode,
                                    String textStyle, int textFont,
                                    int textSize, Color color) {
        String[] words = text.trim().split(" ");
        if (words.length == 1) {
            addTextToImage(text, xCord, yCord, transparency, textStyle, textFont, textSize, color);
        } else {
            FontMetrics fontMetrics = createFontMetrics(textStyle, textFont, textSize);

            if (mode == TextMode.LEFT) {
                addTextToImage(text, xCord, yCord, transparency, textStyle, textFont, textSize, color);
            } else if (mode == TextMode.CENTER) {
                xCord += (columnWidth - fontMetrics.stringWidth(text)) / 2;
                addTextToImage(text, xCord, yCord, transparency, textStyle, textFont, textSize, color);
            } else if (mode == TextMode.RIGHT) {
                xCord += columnWidth - fontMetrics.stringWidth(text);
                addTextToImage(text, xCord, yCord, transparency, textStyle, textFont, textSize, color);
            }
        }
    }

    /**
     * Стандартный метод добавления текста на картинку
     *
     * @param text
     * @param topX
     * @param topY
     * @param transparency
     * @param font
     * @param type
     * @param size
     * @param color
     */
    private void addTextToImage(String text, int topX, int topY, float transparency, String font, int type,
                                int size, Color color) {
        Graphics2D g = bufferedImage.createGraphics();
        g.setColor(color);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transparency));
        g.setFont(new Font(font, type, size));
        g.drawString(text, topX, topY);
        g.dispose();
    }
}
