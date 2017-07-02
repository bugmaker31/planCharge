package fr.gouv.agriculture.dal.ct.libreoffice;

import com.sun.star.beans.PropertyVetoException;
import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.IndexOutOfBoundsException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.sheet.XCellAddressable;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.table.CellAddress;
import com.sun.star.table.CellContentType;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.util.XSearchDescriptor;
import com.sun.star.util.XSearchable;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import libreoffice.LOException;
import libreoffice.Lo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings({"ClassNamingConvention", "ClassWithTooManyMethods"})
public class Calc {

    private static final int OFFICE_EPOCH_YEAR = 1900;
    private static XComponentLoader loader = null;

    @NotNull
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

    @NotNull
    public static XSpreadsheetDocument openDoc(@NotNull String docName) throws LibreOfficeException {
        XSpreadsheetDocument ssd = libreoffice.Calc.openDoc(docName, getLoader());
        if (ssd == null) {
            throw new LibreOfficeException("Doc not found: '" + docName + "'.");
        }
        return ssd;
    }

    @NotNull
    public static XSpreadsheetDocument createDoc() throws LibreOfficeException {
        XSpreadsheetDocument ssd = libreoffice.Calc.createDoc(getLoader());
        if (ssd == null) {
            throw new LibreOfficeException("Can't create Calc doc.");
        }
        return ssd;
    }

    public static void closeDoc(@NotNull XSpreadsheetDocument doc) throws LibreOfficeException {
        libreoffice.Calc.closeDoc(doc);
    }

    public static void saveDoc(@NotNull XSpreadsheetDocument doc, String docName) throws LibreOfficeException {
        libreoffice.Calc.saveDoc(doc, docName);
    }

    public static void saveDoc(@NotNull XSpreadsheetDocument doc) throws LibreOfficeException {
        libreoffice.Calc.saveDoc(doc);
    }

    @NotNull
    public static XSpreadsheet getSheet(@NotNull XSpreadsheetDocument doc, @NotNull String sheetName) throws LibreOfficeException {
        XSpreadsheet ss = libreoffice.Calc.getSheet(doc, sheetName);
        if (ss == null) {
            throw new LibreOfficeException("Sheet not found: '" + sheetName + "'.");
        }
        return ss;
    }

    @NotNull
    public static XSpreadsheet getSheet(@NotNull XSpreadsheetDocument doc, int sheetIndex) throws LibreOfficeException {
        XSpreadsheet ss = libreoffice.Calc.getSheet(doc, sheetIndex);
        if (ss == null) {
            throw new LibreOfficeException("Sheet not found: '" + sheetIndex + "'.");
        }
        return ss;
    }

    public static boolean isEmpty(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        return isEmpty(cell);
    }

    public static boolean isEmpty(@NotNull XCell cell) throws LibreOfficeException {
        Object cellVal = getVal(cell);
        return (cellVal == null);
    }

    public static void setString(@NotNull XSpreadsheet sheet, int column, int row, @NotNull String value) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        setString(cell, value);
    }

    public static void setDouble(@NotNull XSpreadsheet sheet, int column, int row, @NotNull Double value) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        setDouble(cell, value);
    }

    public static void setInteger(@NotNull XSpreadsheet sheet, int column, int row, int value) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        setInteger(cell, value);
    }

    public static void setDate(@NotNull XSpreadsheet sheet, int column, int row, @NotNull Date value) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        setDate(cell, value);
    }

    public static void setDate(@NotNull XCell cell, @NotNull Date value) {

        Calendar date = new GregorianCalendar();
        date.setTime(value);

        Calendar OFFICE_EPOCH = new GregorianCalendar(OFFICE_EPOCH_YEAR, Calendar.JANUARY, 1, 0, 0, 0);

        Double doubleValue = Double.valueOf((date.getTimeInMillis() - OFFICE_EPOCH.getTimeInMillis()) / 1000 / 60 / 60 / 24) + 3; // TODO FDA 2017/03 Comprendre pourquoi il faut faire "+3".
        setDouble(cell, doubleValue);
    }

    public static void setInteger(@NotNull XCell cell, int value) {
        setDouble(cell, Double.valueOf(value));
    }

    private static void setString(@NotNull XCell cell, @NotNull String value) {
        libreoffice.Calc.setVal(cell, value);
    }

    private static void setDouble(@NotNull XCell cell, @NotNull Double value) {
        libreoffice.Calc.setVal(cell, value);
    }

    @NotNull
    public static XCell getCell(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = libreoffice.Calc.getCell(sheet, column, row);
        if (cell == null) {
            throw new LibreOfficeException("Cell not found.");
        }
        return cell;
    }

/*
    @NotNull
    public static Object getVal(XSpreadsheet sheet, String cellName) {
        Object val = libreoffice.Calc.getVal(sheet, cellName);
        return val;
    }
*/

    @Null
    public static Object getVal(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        return getVal(cell);
    }

    @Null
    public static Object getVal(@NotNull XCell cell) {
        Object val = libreoffice.Calc.getVal(cell);
        return val;
    }

    @NotNull
    public static Date getDate(@NotNull XCell cell) throws LibreOfficeException {
        Double cellValue = cell.getValue();
        return doubleToDate(cellValue);
    }

    public static Date getDate(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        return getDate(cell);
    }

    @NotNull
    public static Date getDate(@NotNull XSpreadsheet sheet, @NotNull String cellRangeName) throws LibreOfficeException {
        try {
            XCellRange cellRange = sheet.getCellRangeByName(cellRangeName);
            XCell cell = cellRange.getCellByPosition(0, 0);
            return getDate(cell);
        } catch (IndexOutOfBoundsException e) {
            throw new LibreOfficeException("Impossible de retrouver une date dans le Range '" + cellRangeName + "'.", e);
        }
    }

    @NotNull
    private static Date doubleToDate(@NotNull Double val) throws LibreOfficeException {
        if (val == null) {
            throw new LibreOfficeException("Pas de date.");
        }
        // Cf. https://www.talendforge.org/forum/viewtopic.php?id=4576
        Calendar gc = new GregorianCalendar(OFFICE_EPOCH_YEAR, Calendar.JANUARY, 1, 0, 0, 0);
        gc.add(Calendar.DATE, val.intValue() - 2); // TODO FDA 2017/03 Comprendre pourquoi il faut faire "-2" !?
        return gc.getTime();
    }

    @NotNull
    public static Integer getInt(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        return getInt(cell);
    }

    @NotNull
    public static Integer getInt(@NotNull XCell cell) throws LibreOfficeException {
        Double val = (Double) getVal(cell);
        Integer integer = val.intValue();
        return integer;
    }

    @NotNull
    public static String getString(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = getCell(sheet, column, row);
        return getString(cell);
    }

    @NotNull
    public static String getString(@NotNull XCell cell) throws LibreOfficeException {
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

    public static double getDouble(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        Double doubleVal = (Double) getVal(sheet, column, row);
        if (doubleVal == null) {
            throw new LibreOfficeException("La cellule (" + (row + 1) + ", " + (column + 1) + ") ne contient pas de nombre (vide).");
        }
        return doubleVal;
    }

    public static boolean isNumericValue(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = libreoffice.Calc.getCell(sheet, column, row);
        return isNumericValue(cell);
    }

    public static boolean isNumericValue(@NotNull XCell cell) throws LibreOfficeException {
        return (cell.getType() == CellContentType.VALUE);
    }

    public static void clearCell(@NotNull XSpreadsheet sheet, int column, int row) throws LibreOfficeException {
        XCell cell = libreoffice.Calc.getCell(sheet, column, row);
        clearCell(cell);
    }

    public static void clearCell(@NotNull XCell cell) throws LibreOfficeException {
        // Cf. https://wiki.openoffice.org/wiki/Documentation/BASIC_Guide/Editing_Spreadsheet_Documents#Deleting_Cell_Contents
        setString(cell, "");
    }

    public static XCell findFirst(@NotNull String cellContents, @NotNull XCellRange searchRange) throws LibreOfficeException {
        // Cf. http://fivedots.coe.psu.ac.th/~ad/jlop/chaps/26.%20Search%20&%20Replace.pdf

        XSearchable srch = Lo.qi(XSearchable.class, searchRange);

        // set up search string and properties
        XSearchDescriptor sd = srch.createSearchDescriptor();
        sd.setSearchString(cellContents);
        try {
            sd.setPropertyValue("SearchWords", true);
        } catch (Exception e) {
            throw new LibreOfficeException("Impossible de rechercher.", e);
        }

        XCellRange[] cellRanges = libreoffice.Calc.findAll(srch, sd);

        if ((cellRanges == null) || (cellRanges.length == 0)) {
            return null;//throw new LibreOfficeException("Impossible de retrouver les cellules.");
        }

/*
        if (cellRanges.length >= 2) {
            throw new LibreOfficeException("Le texte '" + cellContents + "' est trouvé dans plus d'une cellule de la plage de recherche.");
        }
*/

        XCellRange cellRange = cellRanges[0];
        XCell cell = libreoffice.Calc.getCell(cellRange, 0, 0);
        assert cell.getFormula().equals(cellContents);

        return cell;
    }

    public static XCellRange getCellRange(@NotNull XSpreadsheet sheet, @NotNull String rangeName) throws LibreOfficeException {
        try {
            return libreoffice.Calc.getCellRange(sheet, rangeName);
        } catch (LOException e) {
            throw new LibreOfficeException("Impossible de déterminer la plage de cellules.", e);
        }
    }

    public static CellAddress getCellAddress(@NotNull XCell cell) throws LibreOfficeException {
        try {
            return libreoffice.Calc.getCellAddress(cell);
        } catch (LOException e) {
            throw new LibreOfficeException("Impossible de retrouver l'adresse de la cellule.", e);
        }
    }

}