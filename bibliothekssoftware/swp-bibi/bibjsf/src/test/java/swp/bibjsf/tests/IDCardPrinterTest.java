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

package swp.bibjsf.tests;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import swp.bibjsf.renderer.Content;
import swp.bibjsf.renderer.IDCardPrinter;
import swp.bibjsf.renderer.IDContent;

/**
 * Test for IDCardPrinter.
 *
 * @author koschke
 *
 */
public class IDCardPrinterTest {

    /**
     * Prints example reader ID cards to file Barcode.pdf. Used
     * for testing.
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
	@Test
    public void printIDCards() throws FileNotFoundException, IOException {
        List<Content> idcontent = new ArrayList<Content>();
        idcontent.add(new IDContent("Supersupersehrsehrsehrlangervorname",  "Supersupersehrsehrsehrlangernachname", "1000000"));
        idcontent.add(new IDContent("Monika",   "Meyer", "1000"));
        idcontent.add(new IDContent("Manfred",  "Meier", "1001"));
        idcontent.add(new IDContent("Lena",     "Maier", "1002"));
        idcontent.add(new IDContent("Nicolas",  "Meier", "1003"));
        idcontent.add(new IDContent("Peter",    "Meyer", "1004"));
        idcontent.add(new IDContent("Gisela",   "Mayer", "1005"));
        idcontent.add(new IDContent("Thomas",   "Meier", "1006"));
        idcontent.add(new IDContent("Harald",   "Meyer", "1007"));
        idcontent.add(new IDContent("Susanne",  "Maier", "1008"));
        idcontent.add(new IDContent("Kurt",     "Meyer", "1009"));
        idcontent.add(new IDContent("Wolfgang", "Meier", "1010"));
        idcontent.add(new IDContent("Elfriede", "Maier", "1011"));
        idcontent.add(new IDContent("Jürgen",   "Meier", "1012"));
        idcontent.add(new IDContent("Andrea",   "Meyer", "1013"));
        idcontent.add(new IDContent("Traudl",   "Mayer", "1014"));
        idcontent.add(new IDContent("Bianca",    "Meier", "1015"));
        idcontent.add(new IDContent("Franziska", "Meyer", "1016"));
        IDCardPrinter printer = new IDCardPrinter();
        OutputStream outStream = new FileOutputStream("BarCode.pdf");
        printer.printCards(idcontent, outStream);
        outStream.close();
    }
}
