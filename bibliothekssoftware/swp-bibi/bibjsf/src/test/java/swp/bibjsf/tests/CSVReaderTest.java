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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
//import java.io.StringBufferInputStream;

import org.junit.Test;

import swp.bibjsf.utils.CSVReader;
import swp.bibjsf.utils.CSVReader.CorruptInput;
import swp.bibjsf.utils.CSVReader.UnknownColumn;

/**
 * Unit test cases for CSVReader.
 *
 * @author koschke
 */
public class CSVReaderTest {

    /**
     * Creates an artificial table.
     *
     * @param separator the separator between column items
     * @param numberOfColumns the number of columns to be created
     * @param numberOfRows the number of rows to be created
     * @return the table in an input stream
     */
    private static InputStream createTable(final char separator, final String quote,
            final int numberOfColumns, final int numberOfRows) {
        StringBuilder sb = createHeader(separator, numberOfColumns);
        for (int i = 0; i < numberOfRows; i++) {
            addRow(sb, separator, quote, numberOfColumns, i);
        }
        //System.out.println("CSVReaderTest.createTable(): " + sb.toString());
        //return new StringBufferInputStream(sb.toString());
        return new ByteArrayInputStream(sb.toString().getBytes());
    }

    /**
     * Creates the table header.
     *
     * @param separator to separate entries
     * @param numberOfColumns the number of entries to be created
     * @return the header as a string builder
     */
    private static StringBuilder createHeader(final char separator,
    		final int numberOfColumns) {
    	StringBuilder sb = new StringBuilder();
    	if (numberOfColumns > 0) {
    		for (int i = 0; i < numberOfColumns; i++) {
    			sb.append(headerName(i));
    			sb.append(separator);
    		}
    		// delete last separator again
    		sb.deleteCharAt(sb.length() - 1);
    		sb.append(System.getProperty("line.separator"));
    	}
    	return sb;
    }

    /**
     * Returns the artificial name of a header at column.
     *
     * @param column the position of the column
     * @return name of the header at column
     */
    private static String headerName(final int column) {
        return "header " + column;
    }

    /**
     * Creates a table row of integers.
     *
     * @param sb string builder where to add the row
     * @param separator to separate entries
     * @param numberOfColumns the number of entries to be created
     * @param rowNumber the current row number of the row to be added
     */
    private static void addRow(final StringBuilder sb,
            final char separator,
            final String quote,
            final int numberOfColumns,
            final int rowNumber) {
        if (numberOfColumns > 0) {
            for (int i = 0; i < numberOfColumns; i++) {
                sb.append(quote + entryValue(i, rowNumber) + quote);
                sb.append(separator);
            }
            // delete last separator again
            sb.deleteCharAt(sb.length() - 1);
            sb.append(System.getProperty("line.separator"));
        }
    }

    /**
     * Returns the value of a table at entry (row, column).
     *
     * @param column table column
     * @param row table row
     * @return value of table entry at (row, column)
     */
    private static int entryValue(final int column, final int row) {
        return column * row;
    }

    /**
     * Test for a correct table with header and data rows.
     */
    @Test
    public final void testFilledCorrectTable() {
        final char separator = ',';
        final int numberOfColumns = 5;
        final int numberOfRows = 10;

        InputStream input = createTable(separator, "", numberOfColumns, numberOfRows);
        try {
            CSVReader reader = new CSVReader(input, separator, "\"");
            assertEquals(numberOfColumns, reader.numberOfColumns());
            assertEquals(numberOfRows, reader.numberOfRows());

            // check header
            String[] columns = new String[reader.numberOfColumns()];
            for (int c = 0; c < reader.numberOfColumns(); c++) {
                final String column = reader.getHeader(c);
                assertTrue("expected: " + headerName(c) + " actual: " + column,
                            headerName(c).equals(column));
                assertTrue(reader.hasColumn(column));
                columns[c] = column;
            }

            reader.hasColumns(columns);

            assertFalse(reader.hasColumn("FOOBAR XYZ 12"));

            // check content
            for (int r = 0; r < reader.numberOfRows(); r++) {
                for (int c = 0; c < reader.numberOfColumns(); c++) {
                final String column = reader.getHeader(c);
                    assertEquals(entryValue(r, c), Integer.parseInt(reader.get(column, r)));
                }
            }
        } catch (CorruptInput e) {
            fail("exception " + e.getClass().getName()
                    + "(" + e.getMessage() + ") not expected");
        }  catch (UnknownColumn e) {
            fail("exception not expected");
        }
    }

    /**
     * Test for a correct table data rows having empty columns.
     */
    @Test
    public final void testCorrectTableWithEmptyColumns() {
        final char separator = ',';
        final int numberOfColumns = 5;
        final int numberOfRows = 10;

        StringBuilder sb = createHeader(separator, numberOfColumns);
        for (int i = 0; i < numberOfRows; i++) {
            for (int c = 0; c < numberOfColumns; c++) {
                sb.append(""); // empty column
                sb.append(separator);
            }
            // remove last separator again
            sb.deleteCharAt(sb.length() - 1);
            sb.append(System.getProperty("line.separator"));
        }

        //InputStream input = new StringBufferInputStream(sb.toString());
        InputStream input = new ByteArrayInputStream(sb.toString().getBytes());
        try {
            CSVReader reader = new CSVReader(input, separator, "\"");
            assertEquals(numberOfColumns, reader.numberOfColumns());
            assertEquals(numberOfRows, reader.numberOfRows());

            // check content
            for (int r = 0; r < reader.numberOfRows(); r++) {
                for (int c = 0; c < reader.numberOfColumns(); c++) {
                	final String column = reader.getHeader(c);
                    assertTrue(reader.get(column, r).isEmpty());
                }
            }
        } catch (CorruptInput e) {
        	System.err.println("CSVReaderTest.testCorrectTableWithEmptyColumns(): " + e.getLocalizedMessage());
            fail("exception " + e.getClass().getName()
                    + "(" + e.getMessage() + ") not expected");
        } catch (UnknownColumn e) {
            fail("exception not expected");
        }
    }

    /**
     * Test for a table with no header but data rows.
     *
     * @throws CorruptInput expected
     */
    @Test(expected = CorruptInput.class)
    public final void testEmptyTableNoHeader() throws CorruptInput {
        final int numberOfColumns = 10;
        InputStream input = createTable(';', "\"", 0, numberOfColumns);
        new CSVReader(input);
    }

    /**
     * Test for a table with only one column and no data rows.
     *
     * @throws CorruptInput not expected
     */
    @Test
    public final void testEmptyTableWithHeader() throws CorruptInput {
        final int numberOfColumns = 1;
        final int numberOfRows = 0;
        InputStream input = createTable(';', "", numberOfColumns, numberOfRows);
        CSVReader reader = new CSVReader(input);
        assertEquals(numberOfColumns, reader.numberOfColumns());
        assertEquals(numberOfRows, reader.numberOfRows());
    }

    /**
     * Test for a table with only one column with data rows
     *
     * @throws CorruptInput not expected
     */
    @Test
    public final void testTableWithOneColumn() throws CorruptInput {
        final int numberOfColumns = 1;
        final int numberOfRows = 1;
        InputStream input = createTable(';', "", numberOfColumns, numberOfRows);
        CSVReader reader = new CSVReader(input);
        assertEquals(numberOfColumns, reader.numberOfColumns());
        assertEquals(numberOfRows, reader.numberOfRows());
    }

    static final char separator = ';';
    static final String quote = "\"";

    /*
     * Test for mixed quoted and unquoted values.
     */
    @Test
    public final void testMixedQuoteValues() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"unquoted", "\"quoted\"", "mid\"quoted\"", "\"has;separator;\"", "x", "\";\""};
        final String[] unquotedValues = {"unquoted", "quoted",     "mid\"quoted\"", "has;separator;",     "x", ";"};
        final String[] secondRow      = {"0", "\"1\"", "\"2\"", "3", "4", "\"\""};
        final String[] thirdRow       = {"0", "\"1\"", "\"2\"", "3", "4", ""};

        StringBuilder sb = new StringBuilder();
        // header
        createLine(separator, quotedValues, sb);
        // first-row data same as header
        createLine(separator, quotedValues, sb);
        // second-row data
        createLine(separator, secondRow, sb);
        // third-row data; empty column at the end
        createLine(separator, thirdRow, sb);

        //InputStream input = new StringBufferInputStream(sb.toString());
        InputStream input = new ByteArrayInputStream(sb.toString().getBytes());
        CSVReader reader = new CSVReader(input, separator, quote);
        assertEquals(quotedValues.length, reader.numberOfColumns());
        assertEquals(3, reader.numberOfRows());

        // check content of first row
        for (int c = 0; c < reader.numberOfColumns(); c++) {
            final String column = reader.getHeader(c);
            assertTrue(reader.get(column, 0).equals(unquotedValues[c]));
        }
        // check content of second row; from first entry to one but last
        for (int c = 0; c < reader.numberOfColumns() - 1; c++) {
            final String column = reader.getHeader(c);
            assertEquals(Integer.parseInt(reader.get(column, 1)), c);
        }
        // check last entry of second row (empty)
        assertTrue(reader.get(reader.getHeader(reader.numberOfColumns() - 1), 1).isEmpty());

        // check content of third row; from first entry to one but last
        for (int c = 0; c < reader.numberOfColumns() - 1; c++) {
            final String column = reader.getHeader(c);
            assertEquals(Integer.parseInt(reader.get(column, 2)), c);
        }
        // check last entry of third row (empty)
        assertTrue(reader.get(reader.getHeader(reader.numberOfColumns() - 1), 2).isEmpty());

    }

    /*
     * Test single column-table.
     */
    @Test
    public final void testInput1() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"\";\""};
        final String[] unquotedValues = {";"};
        check(quotedValues, unquotedValues);
    }

    @Test
    public final void testInput2() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"a"};
        final String[] unquotedValues = {"a"};
        check(quotedValues, unquotedValues);
    }

    @Test
    public final void testInput3() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"a\""};
        final String[] unquotedValues = {"a\""};
        check(quotedValues, unquotedValues);
    }

    @Test
    public final void testInput4() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"\"a\"", "\"b\""};
        final String[] unquotedValues = {"a", "b"};
        check(quotedValues, unquotedValues);
    }

    @Test
    public final void testInput5() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"\"a\"", "b"};
        final String[] unquotedValues = {"a", "b"};
        check(quotedValues, unquotedValues);
    }

    @Test
    public final void testInput6() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"\"\"\"a\"", "b"};
        final String[] unquotedValues = {"\"a", "b"};
        check(quotedValues, unquotedValues);
    }

    @Test
    public final void testInput7() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"a", "\"b\"\"\""};
        final String[] unquotedValues = {"a", "b\""};
        check(quotedValues, unquotedValues);
    }

    @Test
    public final void testInput8() throws CorruptInput, UnknownColumn {
        final String[] quotedValues =   {"a", "\"a\"\"b\"\"c\""};
        final String[] unquotedValues = {"a", "a\"b\"c"};
        check(quotedValues, unquotedValues);
    }

    /**
     * @param quotedValues
     * @param unquotedValues
     * @throws CorruptInput
     * @throws UnknownColumn
     */
    private void check(final String[] quotedValues,
            final String[] unquotedValues) throws CorruptInput, UnknownColumn {
        StringBuilder sb = new StringBuilder();
        // header
        createLine(separator, quotedValues, sb);
        // first-row data same as header
        createLine(separator, quotedValues, sb);

        //InputStream input = new StringBufferInputStream(sb.toString());
        InputStream input = new ByteArrayInputStream(sb.toString().getBytes());
        CSVReader reader = new CSVReader(input, separator, quote);
        assertEquals(quotedValues.length, reader.numberOfColumns());
        assertEquals(1, reader.numberOfRows());

        // check content of first row
        for (int c = 0; c < reader.numberOfColumns(); c++) {
            final String column = reader.getHeader(c);
            assertTrue(reader.get(column, 0).equals(unquotedValues[c]));
        }
    }

    /**
     * @param separator
     * @param quotedValues
     * @param sb
     */
    private void createLine(final char separator,
            final String[] quotedValues, StringBuilder sb) {
        for (String value : quotedValues) {
            sb.append(value);
            sb.append(separator);
        }
        // remove last separator again
        sb.deleteCharAt(sb.length() - 1);
        sb.append(System.getProperty("line.separator"));
    }
}
