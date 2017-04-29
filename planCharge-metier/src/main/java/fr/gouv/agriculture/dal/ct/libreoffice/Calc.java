package fr.gouv.agriculture.dal.ct.libreoffice;

import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.CellContentType;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import libreoffice.LOException;
import libreoffice.Lo;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
public class Calc {

    private static final int OFFICE_EPOCH_YEAR = 1900;
    private static XComponentLoader loader;

    public static XComponentLoader getLoader() throws LibreOfficeException {
        if (loader == null) {
            try {
                loader = Lo.loadOffice();
            } catch (LOException e) {
                throw new LibreOfficeException("Can't load Office.", e);
            }
        }
        return loader;
    }

    public static void closeOffice() throws LibreOfficeException {
        try {
            Lo.closeOffice();
        } catch (LOException e) {
            throw new LibreOfficeException("Can't close Office.", e);
        }
    }

    public static XSpreadsheetDocument openDoc(String docName, XComponentLoader loader) throws LibreOfficeException {
        XSpreadsheetDocument ssd = libreoffice.Calc.openDoc(docName, loader);
        if (ssd == null) {
            throw new LibreOfficeException("Doc not found: '" + docName + "'.");
        }
        return ssd;
    }

    public static XSpreadsheetDocument createDoc(XComponentLoader loader) throws LibreOfficeException {
        XSpreadsheetDocument ssd = libreoffice.Calc.createDoc(loader);
        if (ssd == null) {
            throw new LibreOfficeException("Can't create Calc doc.");
        }
        return ssd;
    }

    public static void closeDoc(XSpreadsheetDocument doc) throws LibreOfficeException {
        libreoffice.Calc.closeDoc(doc);
    }

    public static void saveDoc(XSpreadsheetDocument doc, String docName) throws LibreOfficeException {
        libreoffice.Calc.saveDoc(doc, docName);
    }

    public static void saveDoc(XSpreadsheetDocument doc) throws LibreOfficeException {
        libreoffice.Calc.saveDoc(doc);
    }

    public static XSpreadsheet getSheet(XSpreadsheetDocument doc, String sheetName) throws LibreOfficeException {
        XSpreadsheet ss = libreoffice.Calc.getSheet(doc, sheetName);
        if (ss == null) {
            throw new LibreOfficeException("Sheet not found: '" + sheetName + "'.");
        }
        return ss;
    }


    public static XSpreadsheet getSheet(XSpreadsheetDocument doc, int sheetIndex) throws LibreOfficeException {
        XSpreadsheet ss = libreoffice.Calc.getSheet(doc, sheetIndex);
        if (ss == null) {
            throw new LibreOfficeException("Sheet not found: '" + sheetIndex + "'.");
        }
        return ss;
    }

    public static boolean isEmpty(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        return isEmpty(cell);
    }

    public static boolean isEmpty(XCell cell) throws LibreOfficeException {
        Object cellVal = getVal(cell);
        return (cellVal == null);
    }

    public static void setString(XSpreadsheet sheet, int column, int row, String value) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        setString(cell, value);
    }

    public static void setDouble(XSpreadsheet sheet, int column, int row, Double value) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        setDouble(cell, value);
    }

    public static void setInteger(XSpreadsheet sheet, int column, int row, int value) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        setInteger(cell, value);
    }

    public static void setDate(XSpreadsheet sheet, int column, int row, Date value) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        setDate(cell, value);
    }

    public static void setDate(XCell cell, Date value) {

        Calendar date = new GregorianCalendar();
        date.setTime(value);

        Calendar OFFICE_EPOCH = new GregorianCalendar(OFFICE_EPOCH_YEAR, Calendar.JANUARY, 1, 0, 0, 0);

        Double doubleValue = Double.valueOf((date.getTimeInMillis() - OFFICE_EPOCH.getTimeInMillis()) / 1000 / 60 / 60 / 24) + 3; // TODO FDA 2017/03 Comprendre pourquoi il faut faire "+3".
        setDouble(cell, doubleValue);
    }

    public static void setInteger(XCell cell, int value) {
        setDouble(cell, Double.valueOf(value));
    }

    private static void setString(XCell cell, String value) {
        libreoffice.Calc.setVal(cell, value);
    }

    private static void setDouble(XCell cell, Double value) {
        libreoffice.Calc.setVal(cell, value);
    }

    public static XCell getCell(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = libreoffice.Calc.getCell(sheet, column, row);
        if (cell == null) {
            throw new LibreOfficeException("Cell not found.");
        }
        return cell;
    }

/*
    public static Object getVal(XSpreadsheet sheet, String cellName) {
        Object val = libreoffice.Calc.getVal(sheet, cellName);
        return val;
    }
*/

    public static Object getVal(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        return getVal(cell);
    }

    public static Object getVal(XCell cell) {
        Object val = libreoffice.Calc.getVal(cell);
        return val;
    }

    public static Date getDate(XCell cell) throws LibreOfficeException {
        Double cellValue = cell.getValue();
        return doubleToDate(cellValue);
    }

    public static Date getDate(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        return getDate(cell);
    }

    public static Date getDate(XSpreadsheet sheet, String cellRangeName) throws LibreOfficeException {
        try {
            XCellRange cellRange = sheet.getCellRangeByName(cellRangeName);
            XCell cell = cellRange.getCellByPosition(0, 0);
            return getDate(cell);
        } catch (IndexOutOfBoundsException e) {
            throw new LibreOfficeException("Impossible de retrouver une date dans le Range '" + cellRangeName + "'.", e);
        }
    }

    private static Date doubleToDate(Double val) throws LibreOfficeException {
        if (val == null) {
            throw new LibreOfficeException("Pas de date.");
        }
        // Cf. https://www.talendforge.org/forum/viewtopic.php?id=4576
        Calendar gc = new GregorianCalendar(OFFICE_EPOCH_YEAR, Calendar.JANUARY, 1, 0, 0, 0);
        gc.add(Calendar.DATE, val.intValue() - 2); // TODO FDA 2017/03 Comprendre pourquoi il faut faire "-2" !?
        return gc.getTime();
    }

    public static Integer getInt(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        Double val = (Double) getVal(sheet, column, row);
        Integer integer = val.intValue();
        return integer;
    }

    public static String getString(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        Object cellVal = getVal(cell);
        if (cellVal == null) {
            return "";
        }
        if (cellVal instanceof Double) {
            Double val = (Double) cellVal;
            if (val == 0) {
                return "";
            }
            return String.valueOf(val);
        }
        if (cellVal instanceof String) {
            return (String) cellVal;
        }
        throw new LibreOfficeException("Type de contenu non géré : " + cell.getType().getValue());
    }

    public static double getDouble(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        Double doubleVal = (Double) getVal(sheet, column, row);
        return doubleVal;
    }

    public static boolean isNumericValue(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = libreoffice.Calc.getCell(sheet, column, row);
        return isNumericValue(cell);
    }

    public static boolean isNumericValue(XCell cell) throws LibreOfficeException {
        return (cell.getType() == CellContentType.VALUE);
    }

    public static void clearCell(XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = libreoffice.Calc.getCell(sheet, column, row);
        clearCell(cell);
    }

    public static void clearCell(XCell cell) throws LibreOfficeException {
        // Cf. https://wiki.openoffice.org/wiki/Documentation/BASIC_Guide/Editing_Spreadsheet_Documents#Deleting_Cell_Contents
        setString(cell, "");
    }

}