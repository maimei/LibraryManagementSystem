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

/**
 * A PDF printer for Medium tags (machine-readable sticker on Mediums).
 *
 * @author koschke
 *
 */
public class MediumTagPrinter extends Printer {

	private static final int fontSize = 12;

    /**
     * A scaling factor for the barcode image.
     */
    private final static float barCodeStretch = 1.0f;
    /**
     * Width of the barcode image.
     */
    private final static int barCodeWidth   = (int)(80 * barCodeStretch);
    /**
     * Height of the barcode image.
     */
    private final static int barCodeHeight  = (int)(40 * barCodeStretch);

    /**
     * Width of an Medium tag (format A9).
     */
    private final static int width  = mmToPt(52.0f);
    /**
     * Height of an Medium tag (format A9).
     */
    private final static int height = mmToPt(37.0f);


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
        return 8;
    }

	@Override
	protected void printCard(PDDocument document, PDPage page,
			int columnNumber, int rowNumber, Content content) throws IOException
	{
        final int leftX  = 2;
        final int leftY  = 2;
		printMediumTag(document, page, leftX + (columnNumber % 4) * (width + CardSeparation), leftY + rowNumber * height, (MediumTagContent)content);
	}

	/**
	 * Prints the Medium tag at coordinate (x, y).
	 *
	 * @param document document where to print the Medium tag
	 * @param page page on which to print the Medium tag
	 * @param x x coordinate
	 * @param y y coordinate
	 * @param content Medium tag content to be printed
	 * @throws IOException thrown in case of IO errors
	 */
	private void printMediumTag(PDDocument document, PDPage page, int x, int y, MediumTagContent content) throws IOException {
		final String tagCode = ((Integer)content.ID).toString();
        final int barCodeX = x + 25;
        final int barCodeY = y + 25;

        // One cannot add images if a content stream is still open.
        // Hence, we first create the image and only then the content stream (below).
        PDXObjectImage barCodeImage = barcodeImage(document, tagCode, barCodeWidth, barCodeHeight, false);

		// Start a new content stream which will "hold" the to be created content
		PDPageContentStream contentStream = new PDPageContentStream(document, page, true, false);

        if(barCodeImage != null) {
            contentStream.drawImage(barCodeImage, barCodeX, barCodeY);
        }
		try {
	        drawCuttingAide(x, y, width, height, contentStream);
	        printText(content.firstLine, x + textLeftMargin, y + height - fontSize - textUpperMargin, normalFont, fontSize, 0, width, contentStream, false);
	        printText(content.secondLine, x + textLeftMargin, y + height - 2 * (fontSize + textUpperMargin), normalFont, fontSize, 0, width, contentStream, false);
			printText(tagCode, barCodeX, barCodeY - fontSize - 2, normalFont, fontSize, 0, barCodeWidth, contentStream, false);
		} finally {
			// Make sure that the content stream is closed:
			contentStream.close();
		}
	}

}
