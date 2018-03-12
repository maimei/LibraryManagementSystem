/*
 * Copyright (c) 2013 AG Softwaretechnik, University of Bremen, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package swp.bibjsf.renderer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

public abstract class Printer {

    // 1 pt = 1/72 inch = 2,54 cm/72 = 0,35 mm
    // 1 cm = 1/2,54 inch = (1/2,54) * 72 pt ~ 28.346456693 pt

	/**
	 * Centimeters per inch.
	 */
	private static final double inch = 2.54;
	/**
	 * Millimeters per centimeter.
	 */
	private static final double millimeterPerCentimeter = 10.0;
	/**
	 * Inch per centimeter.
	 */
	private static final double inchPerCm = 1.0 / inch;

	protected static final double inchPerMillimeter = inchPerCm / millimeterPerCentimeter;
	/**
	 * Number of dots (pt) per inch.
	 */
	protected static final double ptPerInch = 72.0;
	/**
	 * The type of barcode used to encode the reader ID.
	 */
	protected static final BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
	/**
	 * Degree of compression quality for JPEG images (barcode).
	 */
	protected static final float compressionQuality = 0.9f;

    /**
     * The font to be used for all normal text.
     */
	protected final static PDFont normalFont = PDType1Font.HELVETICA;

    /**
     * The font to be used for all emphasized text.
     */
	protected static final PDType1Font boldFont = PDType1Font.HELVETICA_BOLD;

    /**
     * Space from the left margin of the ID card to the first text appearance.
     */
    protected final static int textLeftMargin  = 5;
    /**
     * Space from the upper margin of the ID card to the first text appearance.
     */
    protected final static int textUpperMargin = 5;

    /**
     * Space between the printout of two neighboring cards.
     */
    protected final static int CardSeparation = 3;

    /**
     * The type of barcode writer to be used to encode the reader ID.
     */
    private final Writer writer = new Code128Writer();

    /**
     * Logger of this class
     */
    protected static final Logger logger = Logger.getLogger(Printer.class);

    /**
     * Converts value [mm] into PDF points (pt).
     *
     * @param value a length in millimeters (mm)
     * @return PDF points (pt)
     */
	protected static final int mmToPt(double value) {
        return (int)((value * ptPerInch) * inchPerMillimeter);
    }

    /**
     * Corners are printed for each card to ease cutting the cards.
     * Constant lineLength defines how long each line of such a corner is in pt.
     */
	protected static final int lineLength = 4;

	public Printer() {
		super();
	}

    /**
     * Draw corners of a rectangle at position x, y with given width and height
     * to aide in cutting.
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param contentStream
     * @throws IOException
     */
    protected void drawCuttingAide(int x, int y, int width, int height, PDPageContentStream contentStream)
            throws IOException {
        // left lower corner
        contentStream.drawLine(x, y, x, y + lineLength);              // right
        contentStream.drawLine(x, y, x + lineLength, y);              // up
        // right lower corner
        contentStream.drawLine(x + width - lineLength, y, x + width, y);  // right
        contentStream.drawLine(x + width, y, x + width, y + lineLength);  // up
        // left upper corner
        contentStream.drawLine(x, y + height - lineLength, x, y + height); // up
        contentStream.drawLine(x, y + height, x + lineLength, y + height); // right
        // right upper corner
        contentStream.drawLine(x + width - lineLength, y + height, x + width, y + height); //right
        contentStream.drawLine(x + width, y + height, x + width, y + height - lineLength); // down
    }

    /**
     * Prints ID cards for all readers listed in <code>idcontent</code> as a PDF
     * file that is to be saved under given <code>filename</code>.
     *
     * @param idcontent data of readers whose ID card is to be printed
     * @param filename name of the PDF file to be created
     */
    public void printCards(List<Content> idcontent, OutputStream outStream) {

       if (idcontent.isEmpty()) {
           return;
       }
       try {
            PDDocument document = createDocument(idcontent);

            // Save the results and ensure that the document is properly closed:
            try {
                document.save(outStream);
            } catch (COSVisitorException e) {
                e.printStackTrace();
            }
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a barcode image encoding given text with given width and height; if rotate is true,
     * the image is returned by 90 degree counter clockwise.
     *
     * @param document the PDF document in which to print this image
     * @param text text to be encoded in barcode
     * @param width width of the image
     * @param height height of the image
     * @param rotate whether image should be rotated
     * @return the barcode image
     * @throws IOException
     */
    protected PDXObjectImage barcodeImage(PDDocument document, String text, int width, int height, boolean rotate)
            throws IOException {
        PDXObjectImage barCodeImage = null;
        try {
            BitMatrix bitMatrix = writer.encode(text, barcodeFormat, width, height);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
            if (rotate) {
                BufferedImage rotatedImg = rotate(bufferedImage, 90.0);
                barCodeImage = new PDJpeg(document, rotatedImg, compressionQuality);
            } else {
                barCodeImage = new PDJpeg(document, bufferedImage, compressionQuality);
            }
        } catch (WriterException e1) {
            logger.error("Could not write barcode");
            e1.printStackTrace();
        }
        return barCodeImage;
    }

    /**
     * The number of cards per row of a DIN A4 page.
     *
     * @return number of cards per row of a DIN A4 page
     */
    protected abstract int numberOfCardsPerRow();

    /**
     * The number of cards per column of a DIN A4 page.
     *
     * @return number of cards per row of a DIN A4 page
     */
    protected abstract int numberOfCardsPerColumn();

    //
    /**
     * Creates a PDF document containing the ID cards for <code>idcontent</code>.
     *
     * @param idcontent idcontent data of readers whose ID card is to be printed
     * @return PDF document containing the ID cards
     * @throws IOException thrown if document cannot be created
     */
    protected PDDocument createDocument(List<Content> idcontent)
            throws IOException {
        // Create a document and add a page to it
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        page.setMediaBox(PDPage.PAGE_SIZE_A4);
        document.addPage(page);

        int rowNumber    = 0;
        int columnNumber = 0;
        // print numberOfCardsPerColumn x numberOfCardsPerRow cards per A4 page
        for(Content data : idcontent) {
            // System.out.println("IDCardPrinter.createDocument() " + columnNumber + " " + rowNumber);
            printCard(document, page, columnNumber, rowNumber, data);
            columnNumber++;
            if (columnNumber % numberOfCardsPerRow() == 0) {
                // start new row
                rowNumber++;
                columnNumber = 0;
            }
            if (rowNumber == numberOfCardsPerColumn()) {
                // start new page
                page = new PDPage();
                page.setMediaBox(PDPage.PAGE_SIZE_A4);
                document.addPage(page);
                rowNumber = 0;
            }
        }
        return document;
    }

	/**
	 * Prints an individual card on 'page' in 'document' at 'rowNumber' and 'columnNumber'
	 * using given 'data'.
	 *
	 * @param document
	 * @param page
	 * @param columnNumber
	 * @param rowNumber
	 * @param data
	 * @throws IOException
	 */
	protected abstract void printCard(PDDocument document, PDPage page,
			int columnNumber, int rowNumber, Content data) throws IOException;

    /**
     * Returns a rotated copy of the given image.
     *
     * @param image image to be rotated
     * @param angle angle in degrees
     * @return rotated image
     */
    protected static BufferedImage rotate(BufferedImage image, double angle){
        final double cos = Math.abs(Math.cos(Math.toRadians(angle)));
        final double sin = Math.abs(Math.sin(Math.toRadians(angle)));

        final int height = image.getHeight(null);
        final int width  = image.getWidth(null);

        final int newHeight = (int) Math.floor(height * cos + width * sin);
        final int newWidth  = (int) Math.floor(width * cos + height * sin);

        BufferedImage result = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D graphics = result.createGraphics();
        graphics.translate((newWidth - width) / 2, (newHeight - height) / 2);
        graphics.rotate(Math.toRadians(angle), width / 2.0, height / 2.0);
        graphics.drawRenderedImage(image, null);
        graphics.dispose();
        return result;
    }

    /**
     * Prints text to contentStream at x and y coordinate. The default font size used
     * to print the text is given by fontSize; if the whole text in that font size
     * does not fit into maximalLength including textOffset, this font size will be reduced
     * until it finally fits.
     *
     * @param text text to be printed
     * @param x X coordinate
     * @param y Y coordinate
     * @param font the font used to print the text
     * @param fontSize the font size used to print the text
     * @param textOffset
     * @param maximalLength maximal length of the print out in dots; this means
     *    height if rotate = true, and otherwise width
     * @param contentStream stream where the text is to be printed
     * @param rotate if rotate, the printed text is rotated by 90 degree counter-clockwise
     * @throws IOException thrown if the ID card cannot be printed
     */
    protected static void printText(final String text, final int x, final int y,
            final PDFont font, int fontSize,
            final int textOffset, final int maximalLength,
            PDPageContentStream contentStream, final boolean rotate)
                    throws IOException {
        if (text == null || text.isEmpty()) {
            return;
        }
        contentStream.setFont(font, fontSize);

        // reduce font size until text fits on card
        while(true)
        {
            if (fontSize <= 0) {
                throw new IOException(text + " has zero font size");
            }
            final float textWidth = font.getStringWidth(text) / 1000 * fontSize;
            //float textHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;

            if (textOffset + textWidth < maximalLength) {
                break; // exit loop
            }
            // text does not fit; reduce font size
            fontSize--;
            contentStream.setFont(font, fontSize);
            logger.warn(text + " does not fit on card: shrinked font size to " + fontSize);
        }

        contentStream.beginText();
        if (rotate) {
        	contentStream.setTextRotation(90*Math.PI*0.25, x, y);
        } else {
            contentStream.moveTextPositionByAmount(x, y);
        }
        contentStream.drawString(text);
        contentStream.endText();
    }
}