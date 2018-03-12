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

import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import swp.bibjsf.utils.Configuration;

/**
 * A printer for ID cards of readers. The ID card has general information on the
 * library (name, address, phone, URL), which is defined here, and specific
 * reader data (first name, last name, reader ID) that passed into this printer.
 * The ID cards are printed into a new PDF document where a grid layout (two
 * columns, four rows) is chosen so that multiple ID cards fit onto one page
 * in DIN A4. Each ID card has a barcode image of the reader's ID number, which
 * makes the ID card machine readable.
 *
 * @author koschke
 *
 */
public class IDCardPrinter extends Printer {

	public IDCardPrinter() {
    }

    /**
     * Width of an ID card.
     */
    private final static int width  = mmToPt(52.0f); // 74.0f
    /**
     * Height of an ID card.
     */
    private final static int height = mmToPt(74.0f); // 52.0f

    /**
     * Base font size. Larger text will be printed in fontSize + 2.
     * All other text in fontSize.
     */
    private final static int fontSize = 11;

    /**
     * A scaling factor for the barcode image.
     */
    private final static float barCodeStretch = 1.3f;
    /**
     * Width of the barcode image.
     */
    private final static int barCodeWidth   = (int)(80 * barCodeStretch);
    /**
     * Height of the barcode image.
     */
    private final static int barCodeHeight  = (int)(40 * barCodeStretch);
    /**
     * The number of dots between the lower margin of the ID card and
     * the barcode image.
     */
    private final static int barCodeLowerMargin  = 50;

    /**
     * Space in between two text lines.
     */
    private final static int lineSpace = 3;

    /**
     * URL that is to printed on the ID card.
     */
    private static final String URL = Configuration.getKey("LIBRARY_URL");
    /**
     * Phone number of the library that is to printed on the ID card.
     */
    private static final String Library_Phone = Configuration.getKey("LIBRARY_PHONE");
	/**
	 * Name of the library that is to printed on the ID card.
	 */
	private static final String Library_Name = Configuration.getKey("LIBRARY_NAME");

    /**
     * The X coordinate for a given <code>linenumber</code> relative to the left
     * for an ID card whose X coordinate of the origin is <code>leftX</code>.
     *
     * @param linenumber line number whose X coordinate is to be determined
     * @param leftX the X coordinate of the origin of the ID card
     * @return resulting absolute X coordinate
     */
    private static int lineToXFromLeft(int linenumber, int leftX) {
        return leftX + textLeftMargin + linenumber * (fontSize + lineSpace);
    }

    /* (non-Javadoc)
     * @see swp.bibjsf.renderer.Printer#printCard(org.apache.pdfbox.pdmodel.PDDocument, org.apache.pdfbox.pdmodel.PDPage, int, int, swp.bibjsf.renderer.Content)
     */
    @Override
    protected void printCard(PDDocument document, PDPage page,
    		int columnNumber, int rowNumber, Content content) throws IOException
    {
        final int leftX  = 2;
        final int leftY  = 2;
    	printIDCard(document, page, leftX + (columnNumber % 4) * (width + CardSeparation), leftY + rowNumber * height, (IDContent)content);
    }

    /**
     * Prints one individual ID card for a single <code>IDContent</code>.
     *
     * @param document the PDF document to which this ID card is to be added
     * @param page the page of the PDF document to which this ID card is to be added
     * @param leftX the X coordinate of the origin of the ID card on the page
     * @param leftY the Y coordinate of the origin of the ID card on the page
     * @param idcontent the content to be printed
     * @throws IOException thrown if the ID card cannot be printed
     */
    private void printIDCard(PDDocument document, PDPage page,
            int leftX, int leftY, IDContent idcontent) throws IOException {

        final int barCodeX = leftX + 85;
        final int barCodeY = leftY + barCodeLowerMargin;

        // One cannot add images if a content stream is still open.
        // Hence, we first create the image and only then the content stream (below).
        PDXObjectImage barCodeImage = barcodeImage(document, idcontent.barCodeText, barCodeWidth, barCodeHeight, true);

        // Start a new content stream which will "hold" the to be created content
        PDPageContentStream contentStream = new PDPageContentStream(document, page, true, false);

        if(barCodeImage != null) {
            contentStream.drawImage(barCodeImage, barCodeX, barCodeY);
        }

        drawCuttingAide(leftX, leftY, width, height, contentStream);

        final int textBeginY = leftY + textUpperMargin; // y coordinate where to start printing text

        // title in bold
        printText(Library_Name, lineToXFromLeft(1, leftX), textBeginY, boldFont, fontSize + 4, textUpperMargin, height, contentStream, true);
        printText(Library_Phone, lineToXFromLeft(2, leftX), textBeginY, normalFont, fontSize, textUpperMargin, height, contentStream, true);
        printText(URL, lineToXFromLeft(3, leftX), textBeginY, normalFont, fontSize, textUpperMargin, height, contentStream, true);

        printText(idcontent.firstname + " " + idcontent.lastname, lineToXFromLeft(5, leftX), textBeginY, normalFont, fontSize, textUpperMargin, height, contentStream, true);
        printText(idcontent.barCodeText, lineToXFromLeft(7, leftX), textBeginY, normalFont, fontSize, textUpperMargin, height, contentStream, true);

        // Make sure that the content stream is closed:
        contentStream.close();
    }

    /* (non-Javadoc)
     * @see swp.bibrenderer.Printer#numberOfCardsPerRow()
     */
    @Override
    protected int numberOfCardsPerRow() {
        return 4;
    }

    /* (non-Javadoc)
     * @see swp.bibrenderer.Printer#numberOfCardsPerColumn()
     */
    @Override
    protected int numberOfCardsPerColumn() {
        return 4;
    }

}
