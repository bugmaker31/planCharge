package libreoffice;

// Lo.java
// Andrew Davison, ad@fivedots.coe.psu.ac.th, February 2015

/* A growing collection of utility functions to make Office
   easier to use. They are currently divided into the following
   groups:

     * interface object creation (uses generics)

     * office starting
     * office shutdown

     * document opening
     * document creation
     * document saving
     * document closing

     * initialization via Addon-supplied context
     * initialization via script context

     * dispatch
     * UNO cmds

     * use Inspectors extension

     * color methods
     * other utils

     * container manipulation
*/


import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XIntrospection;
import com.sun.star.beans.XIntrospectionAccess;
import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.container.XChild;
import com.sun.star.document.MacroExecMode;
import com.sun.star.frame.*;
import com.sun.star.io.IOException;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.lang.XMultiServiceFactory;
import com.sun.star.reflection.XIdlMethod;
import com.sun.star.script.provider.XScriptContext;
import com.sun.star.uno.Exception;
import com.sun.star.uno.Type;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.CloseVetoException;
import com.sun.star.util.XCloseable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;


public class Lo {

    private static final Logger LOGGER = LoggerFactory.getLogger(Lo.class);

    // docType ints
    public static final int UNKNOWN = 0;
    public static final int WRITER = 1;
    public static final int BASE = 2;
    public static final int CALC = 3;
    public static final int DRAW = 4;
    public static final int IMPRESS = 5;
    public static final int MATH = 6;

    // docType strings
    public static final String UNKNOWN_STR = "unknown";
    public static final String WRITER_STR = "swriter";
    public static final String BASE_STR = "sbase";
    public static final String CALC_STR = "scalc";
    public static final String DRAW_STR = "sdraw";
    public static final String IMPRESS_STR = "simpress";
    public static final String MATH_STR = "smath";

    // docType service names
    public static final String UNKNOWN_SERVICE = "com.sun.frame.XModel";
    public static final String WRITER_SERVICE = "com.sun.star.text.TextDocument";
    public static final String BASE_SERVICE = "com.sun.star.sdb.OfficeDatabaseDocument";
    public static final String CALC_SERVICE = "com.sun.star.sheet.SpreadsheetDocument";
    public static final String DRAW_SERVICE = "com.sun.star.drawing.DrawingDocument";
    public static final String IMPRESS_SERVICE = "com.sun.star.presentation.PresentationDocument";
    public static final String MATH_SERVICE = "com.sun.star.formula.FormulaProperties";

    // connect to locally running Office via port 8100
    private static final int SOCKET_PORT = 8100;


    // CLSIDs for Office documents
    // defined in <OFFICE>\officecfg\registry\data\org\openoffice\Office\Embedding.xcu
    public static final String WRITER_CLSID = "8BC6B165-B1B2-4EDD-aa47-dae2ee689dd6";
    public static final String CALC_CLSID = "47BBB4CB-CE4C-4E80-a591-42d9ae74950f";
    public static final String DRAW_CLSID = "4BAB8970-8A3B-45B3-991c-cbeeac6bd5e3";
    public static final String IMPRESS_CLSID = "9176E48A-637A-4D1F-803b-99d9bfac1047";
    public static final String MATH_CLSID = "078B7ABA-54FC-457F-8551-6147e776a997";
    public static final String CHART_CLSID = "12DCAE26-281F-416F-a234-c3086127382e";

/* unsure about these:
     chart2 "80243D39-6741-46C5-926E-069164FF87BB"
          service: com.sun.star.chart2.ChartDocument

     applet "970B1E81-CF2D-11CF-89CA-008029E4B0B1"
          service: com.sun.star.comp.sfx2.AppletObject

     plug-in "4CAA7761-6B8B-11CF-89CA-008029E4B0B1"
           service: com.sun.star.comp.sfx2.PluginObject

     frame "1A8A6701-DE58-11CF-89CA-008029E4B0B1"
           service: com.sun.star.comp.sfx2.IFrameObject

     XML report chart "D7896D52-B7AF-4820-9DFE-D404D015960F"
           service: com.sun.star.report.ReportDefinition

*/


    // remote component context
    private static XComponentContext xcc = null;

    // remote desktop UNO service
    private static XDesktop xDesktop = null;

    // remote service managers
    private static XMultiComponentFactory mcFactory = null;
    // has replaced XMultiServiceFactory
    private static XMultiServiceFactory msFactory = null;

    private static XComponent bridgeComponent = null;
    // this is only set if office is opened via a socket

    private static boolean isOfficeTerminated = false;


    public static XComponentContext getContext() {
        return xcc;
    }

    public static XDesktop getDesktop() {
        return xDesktop;
    }

    public static XMultiComponentFactory getComponentFactory() {
        return mcFactory;
    }

    public static XMultiServiceFactory getServiceFactory() {
        return msFactory;
    }

    public static XComponent getBridge() {
        return bridgeComponent;
    }


    // ====== interface object creation (uses generics) ===========

    public static <T> T qi(Class<T> aType, Object o)
    // the "Loki" function -- reduces typing
    {
        return UnoRuntime.queryInterface(aType, o);
    }


    public static <T> T createInstanceMSF(Class<T> aType, String serviceName)
  /* create an interface object of class aType from the named service;
     uses 'old' XMultiServiceFactory, so a document must have already been loaded/created
  */ {
        if (msFactory == null) {
            LOGGER.debug("No document found");
            return null;
        }

        T interfaceObj = null;
        try {
            Object o = msFactory.createInstance(serviceName);     // create service component
            interfaceObj = Lo.qi(aType, o);
            // uses bridge to obtain proxy to remote interface inside service;
            // implements casting across process boundaries
        } catch (Exception e) {
            LOGGER.debug("Couldn't create interface for \"" + serviceName + "\": " + e);
        }
        return interfaceObj;
    }  // end of createInstanceMSF()


    public static <T> T createInstanceMSF(Class<T> aType, String serviceName,
                                          XMultiServiceFactory msf)
  /* create an interface object of class aType from the named service; 
     uses 'old' XMultiServiceFactory, so a document must have been already loaded/created
  */ {
        if (msf == null) {
            LOGGER.debug("No document found");
            return null;
        }

        T interfaceObj = null;
        try {
            Object o = msf.createInstance(serviceName);   // create service component
            interfaceObj = Lo.qi(aType, o);
            // uses bridge to obtain proxy to remote interface inside service;
            // implements casting across process boundaries
        } catch (Exception e) {
            LOGGER.debug("Couldn't create interface for \"" + serviceName + "\":\n  " + e);
        }
        return interfaceObj;
    }  // end of createInstanceMSF()


    public static <T> T createInstanceMCF(Class<T> aType, String serviceName)
  /* create an interface object of class aType from the named service;
     uses XComponentContext and 'new' XMultiComponentFactory 
     so only a bridge to office is needed
  */ {
        if ((xcc == null) || (mcFactory == null)) {
            LOGGER.debug("No office connection found");
            return null;
        }

        T interfaceObj = null;
        try {
            Object o = mcFactory.createInstanceWithContext(serviceName, xcc);
            // create service component using the specified component context
            interfaceObj = Lo.qi(aType, o);
            // uses bridge to obtain proxy to remote interface inside service;
            // implements casting across process boundaries
        } catch (Exception e) {
            LOGGER.debug("Couldn't create interface for \"" + serviceName + "\": " + e);
        }
        return interfaceObj;
    }  // end of createInstanceMCF()


    public static <T> T createInstanceMCF(Class<T> aType, String serviceName, Object[] args)
  /* create an interface object of class aType from the named service and arguments;
     uses XComponentContext and 'new' XMultiComponentFactory 
     so only a bridge to office is needed
  */ {
        if ((xcc == null) || (mcFactory == null)) {
            LOGGER.debug("No office connection found");
            return null;
        }

        T interfaceObj = null;
        try {
            Object o = mcFactory.createInstanceWithArgumentsAndContext(serviceName, args, xcc);
            // create service component using the specified args and component context
            interfaceObj = Lo.qi(aType, o);
            // uses bridge to obtain proxy to remote interface inside service;
            // implements casting across process boundaries
        } catch (Exception e) {
            LOGGER.debug("Couldn't create interface for \"" + serviceName + "\": " + e);
        }
        return interfaceObj;
    }  // end of createInstanceMCF()


    public static <T> T getParent(Object aComponent, Class<T> aType)
    // retrieves the parent of the given object
    {
        XChild xAsChild = Lo.qi(XChild.class, aComponent);
        return Lo.qi(aType, xAsChild.getParent());
    }


    // ======================== start office ==============


    public static XComponentLoader loadOffice() throws LOException {
        return loadOffice(true);
    }    // default is to using office via pipes


    public static XComponentLoader loadSocketOffice() throws LOException {
        return loadOffice(false);
    }


    public static XComponentLoader loadOffice(boolean usingPipes)
  /* Creation sequence: remote component content (xcc) --> 
                        remote service manager (mcFactory) -->
                        remote desktop (xDesktop) -->
                        component loader (XComponentLoader)
    Once we have a component loader, we can load a document. 
    xcc, mcFactory, and xDesktop are stored as static globals.
  */ throws LOException {
        LOGGER.debug("Loading Office...");
        if (usingPipes)
            xcc = bootstrapContext(); // connects to office via pipes
        else
            xcc = socketContext();    // connects to office via a socket
        if (xcc == null) {
            throw new LOException("Office context could not be created");
        }

        // get the remote office service manager
        mcFactory = xcc.getServiceManager();
        if (mcFactory == null) {
            throw new LOException("Office SERVICE Manager is unavailable");
        }

        // desktop service handles application windows and documents
        xDesktop = createInstanceMCF(XDesktop.class, "com.sun.star.frame.Desktop");
        if (xDesktop == null) {
            throw new LOException("Could not create a desktop service");
        }

        // XComponentLoader provides ability to load components
        return Lo.qi(XComponentLoader.class, xDesktop);
    }  // end of loadOffice()


    private static XComponentContext bootstrapContext() throws LOException
    // connect pipes to office using the Bootstrap class
    // i.e. see code at http://svn.apache.org/repos/asf/openoffice/symphony/trunk/main/
    //                    javaunohelper/com/sun/star/comp/helper/Bootstrap.java
    {
        XComponentContext xcc;   // the remote office component context
        try {
            xcc = Bootstrap.bootstrap();  //  get remote office component context
            // Connected to office, if office is not running then it's started
        } catch (BootstrapException e) {
            throw new LOException("Unable to bootstrap Office", e);
        }
        return xcc;
    }  // end of bootstrapContext()


    private static XComponentContext socketContext() throws LOException
    // use socket connection to Office
    // https://forum.openoffice.org/en/forum/viewtopic.php?f=44&t=1014
    {
        XComponentContext xcc;   // the remote office component context
        try {
            String[] cmdArray = new String[3];
            cmdArray[0] = "soffice";
            // requires soffice to be in Windows PATH env var.
            cmdArray[1] = "-headless";
            cmdArray[2] = "-accept=socket,host=localhost,port=" +
                    SOCKET_PORT + ";urp;";
            Process p = Runtime.getRuntime().exec(cmdArray);
/*
            if (p != null)
                LOGGER.debug("Office process created");
*/
            delay(5000); // Wait 5 seconds, until office is in listening mode

            // Create a local Component Context
            XComponentContext localContext =
                    Bootstrap.createInitialComponentContext(null);

            // Get the local service manager
            XMultiComponentFactory localFactory = localContext.getServiceManager();

            // connect to Office via its socket
/*
      Object urlResolver = localFactory.createInstanceWithContext(
                                  "com.sun.star.bridge.UnoUrlResolver", localContext);
      XUnoUrlResolver xUrlResolver = Lo.qi(XUnoUrlResolver.class, urlResolver);
      Object initObject = xUrlResolver.resolve(
            "uno:socket,host=localhost,port=" + SOCKET_PORT + 
                                          ";urp;StarOffice.ServiceManager");
*/
            XConnector connector = Lo.qi(XConnector.class,
                    localFactory.createInstanceWithContext(
                            "com.sun.star.connection.Connector", localContext));

            XConnection connection = connector.connect(
                    "socket,host=localhost,port=" + SOCKET_PORT);

            // create a bridge to Office via the socket
            XBridgeFactory bridgeFactory = Lo.qi(XBridgeFactory.class,
                    localFactory.createInstanceWithContext(
                            "com.sun.star.bridge.BridgeFactory", localContext));

            // create a nameless bridge with no instance provider
            XBridge bridge = bridgeFactory.createBridge("socketBridgeAD", "urp", connection, null);

            bridgeComponent = Lo.qi(XComponent.class, bridge);

            // get the remote service manager
            XMultiComponentFactory serviceManager = Lo.qi(XMultiComponentFactory.class,
                    bridge.getInstance("StarOffice.ServiceManager"));

            // retrieve Office's remote component context as a property
            XPropertySet props = Lo.qi(XPropertySet.class, serviceManager);
            // initObject);
            Object defaultContext = props.getPropertyValue("DefaultContext");

            // get the remote interface XComponentContext
            xcc = Lo.qi(XComponentContext.class, defaultContext);
        } catch (java.lang.Exception e) {
            throw new LOException("Unable to socket connect to Office");
        }

        return xcc;
    }  // end of socketContext()


    // ================== office shutdown =========================


    public static void closeOffice() throws LOException
    // tell office to terminate
    {
        LOGGER.debug("Closing Office");
        if (xDesktop == null) {
            throw new LOException("No office connection found");
        }

        if (isOfficeTerminated) {
            throw new LOException("Office has already been requested to terminate");
        }

        int numTries = 1;
        while (!isOfficeTerminated && (numTries < 4)) {
            delay(200);
            isOfficeTerminated = tryToTerminate(numTries);
            numTries++;
        }
    }  // end of closeOffice()


    public static boolean tryToTerminate(int numTries) throws LOException {
        try {
            boolean isDead = xDesktop.terminate();
            if (isDead) {
/*
                if (numTries > 1)
                    LOGGER.debug(numTries + ". Office terminated");
                else
                    LOGGER.debug("Office terminated");
*/
/*
            } else {
                LOGGER.debug(numTries + ". Office failed to terminate");
*/
            }
            return isDead;
        } catch (com.sun.star.lang.DisposedException e) {
            throw new LOException("Office link disposed", e);
        } catch (java.lang.Exception e) {
            throw new LOException("Termination exception: " + e, e);
        }
    }  // end of tryToTerminate()


    public static void killOffice() throws LOException
    // kill office processes using a batch file
    // or use JNAUtils.killOffice()
    {
        try {
            Runtime.getRuntime().exec("cmd /c lokill.bat");
            LOGGER.debug("Killed Office");
        } catch (java.lang.Exception e) {
            throw new LOException("Unable to kill Office: " + e, e);
        }
    }  // end of killOffice()


    // ======================== document opening ==============


    public static XComponent openFlatDoc(String fnm, String docType,
                                         XComponentLoader loader) throws LOException {
        String nm = XML.getFlatFilterName(docType);
        LOGGER.debug("Flat filter Name: " + nm);
        return openDoc(fnm, loader, Props.makeProps("FilterName", nm));
    }


    public static XComponent openDoc(String fnm, XComponentLoader loader) throws LOException {
        return openDoc(fnm, loader, Props.makeProps("Hidden", true));
    }


    public static XComponent openReadOnlyDoc(String fnm, XComponentLoader loader) throws LOException {
        return openDoc(fnm, loader, Props.makeProps("Hidden", true, "ReadOnly", true));
    }


    public static XComponent openDoc(String fnm, XComponentLoader loader,
                                     PropertyValue[] props) throws LOException
    // open the specified document
    // the possibly props for a document are listed in the MediaDescriptor service
    {
        if (fnm == null) {
            throw new LOException("Filename is null");
        }

        String openFileURL = null;
        if (!FileIO.isOpenable(fnm)) {
            if (isURL(fnm)) {
                LOGGER.debug("Will treat filename as a URL: \"" + fnm + "\"");
                openFileURL = fnm;
            } else
                return null;
        } else {
            LOGGER.debug("Opening " + fnm);
            openFileURL = FileIO.fnmToURL(fnm);
            if (openFileURL == null)
                return null;
        }


        XComponent doc = null;
        try {
            doc = loader.loadComponentFromURL(openFileURL, "_blank", 0, props);
            msFactory = Lo.qi(XMultiServiceFactory.class, doc);
        } catch (Exception e) {
            throw new LOException("Unable to open the document", e);
        }
        return doc;
    }  // end of openDoc()


    public static boolean isURL(String fnm) {
        try {
            java.net.URL u = new java.net.URL(fnm);   // check for the protocol
            u.toURI();                                // check validation of URI
            return true;
        } catch (java.net.MalformedURLException e) {
            return false;
        } catch (java.net.URISyntaxException e) {
            return false;
        }
    }  // end of isURL()


    // ======================== document creation ==============


    public static String ext2DocType(String ext) {
        switch (ext) {
            case "odt":
                return WRITER_STR;
            case "odp":
                return IMPRESS_STR;
            case "odg":
                return DRAW_STR;
            case "ods":
                return CALC_STR;
            case "odb":
                return BASE_STR;
            case "odf":
                return MATH_STR;
            default:
                LOGGER.debug("Do not recognize extension \"" + ext + "\"; using writer");
                return WRITER_STR;    // could use UNKNOWN_STR
        }
    }  // end of ext2DocType()


/* docType (without the ""private:factory/") is:
    "private:factory/swriter"       Writer document
    "private:factory/simpress"      Impress presentation document
    "private:factory/sdraw"         Draw document
    "private:factory/scalc"         Calc document
    "private:factory/sdatabase"     Base document
    "private:factory/smath"         Math formula document

  These are not handled:
    "private:factory/schart"                    Chart
    "private:factory/swriter/web"               Writer HTML Web document
    "private:factory/swriter/GlobalDocument"    Master document
     
    ".component:Bibliography/View1"       Bibliography-Edit the bibliography entries
     
    ".component:DB/QueryDesign"           Database comp
    ".component:DB/TableDesign"
    ".component:DB/RelationDesign"
    ".component:DB/DataSourceBrowser"
    ".component:DB/FormGridView"
*/


    public static String docTypeStr(int docTypeVal) {
        switch (docTypeVal) {
            case WRITER:
                return WRITER_STR;
            case IMPRESS:
                return IMPRESS_STR;
            case DRAW:
                return DRAW_STR;
            case CALC:
                return CALC_STR;
            case BASE:
                return BASE_STR;
            case MATH:
                return MATH_STR;
            default:
                LOGGER.debug("Do not recognize extension \"" + docTypeVal + "\"; using writer");
                return WRITER_STR;    // could use UNKNOWN_STR
        }
    }  // end of docTypeStr()


    public static XComponent createDoc(String docType, XComponentLoader loader) throws LOException {
        return createDoc(docType, loader, Props.makeProps("Hidden", true));
    }


    public static XComponent createMacroDoc(String docType, XComponentLoader loader) throws LOException {
        return createDoc(docType, loader, Props.makeProps("Hidden", false,
                //"MacroExecutionMode", MacroExecMode.ALWAYS_EXECUTE) );  }
                "MacroExecutionMode", MacroExecMode.ALWAYS_EXECUTE_NO_WARN));
    }


    public static XComponent createDoc(String docType, XComponentLoader loader,
                                       PropertyValue[] props) throws LOException
    // create a new document of the specified type
    {
        LOGGER.debug("Creating Office document " + docType);
        // PropertyValue[] props = Props.makeProps("Hidden", true);
        // if Hidden == true, office will not terminate properly
        XComponent doc = null;
        try {
            doc = loader.loadComponentFromURL("private:factory/" + docType, "_blank", 0, props);
            msFactory = Lo.qi(XMultiServiceFactory.class, doc);
        } catch (Exception e) {
            throw new LOException("Could not create a document", e);
        }
        return doc;
    }  // end of createDoc()


    public static XComponent createDocFromTemplate(String templatePath,
                                                   XComponentLoader loader) throws LOException
    // create a new document using the specified template
    {
        if (!FileIO.isOpenable(templatePath))
            return null;
        LOGGER.debug("Opening template " + templatePath);
        String templateURL = FileIO.fnmToURL(templatePath);
        if (templateURL == null)
            return null;

        PropertyValue[] props = Props.makeProps("Hidden", true, "AsTemplate", true);
        XComponent doc = null;
        try {
            doc = loader.loadComponentFromURL(templateURL, "_blank", 0, props);
            msFactory = Lo.qi(XMultiServiceFactory.class, doc);
        } catch (Exception e) {
            throw new LOException("Could not create document from template: " + e, e);
        }
        return doc;
    }  // end of createDocFromTemplate()


    // ======================== document saving ==============


    public static void save(Object odoc) throws LOException
    // was XComponent
    {
        XStorable store = Lo.qi(XStorable.class, odoc);
        try {
            store.store();
            LOGGER.debug("Saved the document by overwriting");
        } catch (IOException e) {
            throw new LOException("Could not save the document", e);
        }
    }  // end of save()


    public static void saveDoc(Object odoc, String fnm) throws LOException
    // was XComponent
    {
        XStorable store = Lo.qi(XStorable.class, odoc);
        XComponent doc = Lo.qi(XComponent.class, odoc);
        int docType = Info.reportDocType(doc);
        storeDoc(store, docType, fnm, null);   // no password
    }


    public static void saveDoc(Object odoc, String fnm, String password) throws LOException
    // was XComponent
    {
        XStorable store = Lo.qi(XStorable.class, odoc);
        XComponent doc = Lo.qi(XComponent.class, odoc);
        int docType = Info.reportDocType(doc);
        storeDoc(store, docType, fnm, password);
    }


    public static void saveDoc(Object odoc, String fnm, String format, String password) throws LOException
    // was XComponent
    {
        XStorable store = Lo.qi(XStorable.class, odoc);
        storeDocFormat(store, fnm, format, password);
    }


    //public static void storeDoc(XStorable store, int docType, String fnm)
    //{  saveDoc(store, docType, fnm, null);  }     // no password


    public static void storeDoc(XStorable store, int docType, String fnm, String password) throws LOException
    // Save the document using the file's extension as a guide.
    {
        String ext = Info.getExt(fnm);
        String format = "Text";
        if (ext == null) {
            LOGGER.debug("Assuming a text format");
        } else {
            format = ext2Format(docType, ext);
        }
        storeDocFormat(store, fnm, format, password);
    }  // end of storeDoc()


    public static String ext2Format(String ext) {
        return ext2Format(Lo.UNKNOWN, ext);
    }


    public static String ext2Format(int docType, String ext)
  /* convert the extension string into a suitable office format string.
     The formats were chosen based on the fact that they
     are being used to save (or export) a document.

     The names were obtained from
     http://www.oooforum.org/forum/viewtopic.phtml?t=71294
     and by running my DocInfo application on files.

     I use the docType to distinguish between the various meanings of the PDF ext.

     This could be a lot more extensive.

     Use Info.getFilterNames() to get the filter names for your Office
     (the last time I tried it, I got back 246 names!)
  */ {
        switch (ext) {
            case "doc":
                return "MS Word 97";
            case "docx":
                return "Office Open XML Text";   // "MS Word 2007 XML"
            case "rtf":
                if (docType == Lo.CALC)
                    return "Rich Text Format (StarCalc)";
                else
                    return "Rich Text Format";   // assume writer

            case "odt":
                return "writer8";
            case "ott":
                return "writer8_template";

            case "pdf":
                if (docType == Lo.WRITER)
                    return "writer_pdf_Export";
                else if (docType == Lo.IMPRESS)
                    return "impress_pdf_Export";
                else if (docType == Lo.DRAW)
                    return "draw_pdf_Export";
                else if (docType == Lo.CALC)
                    return "calc_pdf_Export";
                else if (docType == Lo.MATH)
                    return "math_pdf_Export";
                else
                    return "writer_pdf_Export";    // assume we are saving a writer doc

            case "txt":
                return "Text";

            case "ppt":
                return "MS PowerPoint 97";
            case "pptx":
                return "Impress MS PowerPoint 2007 XML";
            case "odp":
                return "impress8";
            case "odg":
                return "draw8";

            case "jpg":
                if (docType == Lo.IMPRESS)
                    return "impress_jpg_Export";
                else
                    return "draw_jpg_Export";    // assume Draw doc

            case "png":
                if (docType == Lo.IMPRESS)
                    return "impress_png_Export";
                else
                    return "draw_png_Export";    // assume Draw doc

            case "xls":
                return "MS Excel 97";
            case "xlsx":
                return "Calc MS Excel 2007 XML";
            case "csv":
                return "Text - txt - csv (StarCalc)";   // "Text CSV";
            case "ods":
                return "calc8";
            case "odb":
                return "StarOffice XML (Base)";

            case "htm":
            case "html":
                if (docType == Lo.WRITER)
                    return "HTML (StarWriter)";  // "writerglobal8_HTML";
                else if (docType == Lo.IMPRESS)
                    return "impress_html_Export";
                else if (docType == Lo.DRAW)
                    return "draw_html_Export";
                else if (docType == Lo.CALC)
                    return "HTML (StarCalc)";
                else
                    return "HTML";

            case "xhtml":
                if (docType == Lo.WRITER)
                    return "XHTML Writer File";
                else if (docType == Lo.IMPRESS)
                    return "XHTML Impress File";
                else if (docType == Lo.DRAW)
                    return "XHTML Draw File";
                else if (docType == Lo.CALC)
                    return "XHTML Calc File";
                else
                    return "XHTML Writer File";    // assume we are saving a writer doc

            case "xml":
                if (docType == Lo.WRITER)
                    return "OpenDocument Text Flat XML";
                else if (docType == Lo.IMPRESS)
                    return "OpenDocument Presentation Flat XML";
                else if (docType == Lo.DRAW)
                    return "OpenDocument Drawing Flat XML";
                else if (docType == Lo.CALC)
                    return "OpenDocument Spreadsheet Flat XML";
                else
                    return "OpenDocument Text Flat XML";    // assume we are saving a writer doc


            default:   // assume user means text
                LOGGER.debug("Do not recognize extension \"" + ext + "\"; using text");
                return "Text";
        }
    }  // end of ext2Format()


    public static void storeDocFormat(XStorable store, String fnm, String format, String password) throws LOException
    // save the document in the specified file using the supplied office format
    {
        LOGGER.debug("Saving the document in " + fnm);
        LOGGER.debug("Using format: " + format);
        try {
            String saveFileURL = FileIO.fnmToURL(fnm);
            if (saveFileURL == null)
                return;

            PropertyValue[] storeProps;
            if (password == null)  // no password supplied
                storeProps = Props.makeProps("Overwrite", true, "FilterName", format);
            else {
                String[] nms = new String[]{"Overwrite", "FilterName", "Password"};
                Object[] vals = new Object[]{true, format, password};
                storeProps = Props.makeProps(nms, vals);
            }
            store.storeToURL(saveFileURL, storeProps);
        } catch (IOException e) {
            throw new LOException("Could not save " + fnm + ": " + e, e);
        }
    } // end of storeDocFormat()


    // ======================== document closing ==============


    public static void closeDoc(Object doc) throws LOException
    // was XComponent
    {
        try {
            XCloseable closeable = Lo.qi(XCloseable.class, doc);
            close(closeable);
        } catch (com.sun.star.lang.DisposedException e) {
            throw new LOException("Document close failed since Office link disposed", e);
        }
    }


    public static void close(XCloseable closeable) throws LOException {
        if (closeable == null)
            return;
        LOGGER.debug("Closing the document");
        try {
            closeable.close(false);   // true to force a close
            // set modifiable to false to close a modified doc without complaint setModified(False)
        } catch (CloseVetoException e) {
            throw new LOException("Close was vetoed", e);
        }
    }  // end of close()


    // ================= initialization via Addon-supplied context ====================


    public static XComponent addonInitialize(XComponentContext addonXcc) throws LOException {
        xcc = addonXcc;
        if (xcc == null) {
            throw new LOException("Could not access component context");
        }

        mcFactory = xcc.getServiceManager();
        if (mcFactory == null) {
            throw new LOException("Office SERVICE Manager is unavailable");
        }

        try {
            Object oDesktop = mcFactory.createInstanceWithContext(
                    "com.sun.star.frame.Desktop", xcc);
            xDesktop = Lo.qi(XDesktop.class, oDesktop);
        } catch (Exception e) {
            throw new LOException("Could not access desktop", e);
        }

        XComponent doc = xDesktop.getCurrentComponent();
        if (doc == null) {
            throw new LOException("Could not access document");
        }

        msFactory = Lo.qi(XMultiServiceFactory.class, doc);
        return doc;
    }  // end of addonInitialize()


    // ============= initialization via script context ======================


    public static XComponent scriptInitialize(XScriptContext sc) throws LOException {
        if (sc == null) {
            throw new LOException("Script Context is null");
        }

        xcc = sc.getComponentContext();
        if (xcc == null) {
            throw new LOException("Could not access component context");
        }
        mcFactory = xcc.getServiceManager();
        if (mcFactory == null) {
            throw new LOException("Office SERVICE Manager is unavailable");
        }

        xDesktop = sc.getDesktop();
        if (xDesktop == null) {
            throw new LOException("Could not access desktop");
        }

        XComponent doc = xDesktop.getCurrentComponent();
        if (doc == null) {
            throw new LOException("Could not access document");
        }

        msFactory = Lo.qi(XMultiServiceFactory.class, doc);
        return doc;
    }  // end of scriptInitialize()


    // ==================== dispatch ===============================
    // see https://wiki.documentfoundation.org/Development/DispatchCommands


    public static boolean dispatchCmd(String cmd) throws LOException {
        return dispatchCmd(xDesktop.getCurrentFrame(), cmd, null);
    }


    public static boolean dispatchCmd(String cmd, PropertyValue[] props) throws LOException {
        return dispatchCmd(xDesktop.getCurrentFrame(), cmd, props);
    }


    public static boolean dispatchCmd(XFrame frame, String cmd, PropertyValue[] props) throws LOException
    // cmd does not include the ".uno:" substring; e.g. pass "Zoom" not ".uno:Zoom"
    {
        XDispatchHelper helper =
                createInstanceMCF(XDispatchHelper.class, "com.sun.star.frame.DispatchHelper");
        if (helper == null) {
            throw new LOException("Could not create dispatch helper for command " + cmd);
        }

        try {
            XDispatchProvider provider = Lo.qi(XDispatchProvider.class, frame);

      /* returns failure even when the event works (?), and an illegal value 
         when the dispatch actually does fail */
      /*
      DispatchResultEvent res =  (DispatchResultEvent)
                  helper.executeDispatch(provider, (".uno:" + cmd), "", 0, props);
      if (res.State == DispatchResultState.FAILURE)
        LOGGER.debug("Dispatch failed for \"" + cmd + "\"");
      else if (res.State == DispatchResultState.DONTKNOW)
        LOGGER.debug("Dispatch result unknown for \"" + cmd + "\"");
      */
            helper.executeDispatch(provider, (".uno:" + cmd), "", 0, props);
            return true;
        } catch (java.lang.Exception e) {
            throw new LOException("Could not dispatch \"" + cmd + "\":\n  " + e, e);
        }
    }  // end of dispatchCmd()


    // ================= Uno cmds =========================


    public static String makeUnoCmd(String itemName)
    // use a dummy Java class name, Foo
    {
        return "vnd.sun.star.script:Foo/Foo." + itemName +
                "?language=Java&location=share";
    }


    public static String extractItemName(String unoCmd) throws LOException
  /* format is:
       "vnd.sun.star.script:Foo/Foo." + itemName + 
                                  "?language=Java&location=share";
  */ {
        int fooPos = unoCmd.indexOf("Foo.");
        if (fooPos == -1) {
            throw new LOException("Could not find Foo header in command: \"" + unoCmd + "\"");
        }

        int langPos = unoCmd.indexOf("?language");
        if (langPos == -1) {
            throw new LOException("Could not find language header in command: \"" + unoCmd + "\"");
        }

        return unoCmd.substring(fooPos + 4, langPos);
    }  // end of extractItemName()


    // ======================== use Inspector extensions ====================


    public static void inspect(Object obj) throws LOException
  /* call XInspector.inspect() in the Inspector.oxt extension
     Available from https://wiki.openoffice.org/wiki/Object_Inspector
  */ {
        if ((xcc == null) || (mcFactory == null)) {
            throw new LOException("No office connection found");
        }

        try {
            Type[] ts = Info.getInterfaceTypes(obj);   // get class name for title
            String title = "Object";
            if ((ts != null) && (ts.length > 0))
                title = ts[0].getTypeName() + " " + title;

            Object inspector = mcFactory.createInstanceWithContext(
                    "org.openoffice.InstanceInspector", xcc);
            // hangs on second use
            if (inspector == null) {
                throw new LOException("Inspector SERVICE could not be instantiated");
            }

            LOGGER.debug("Inspector SERVICE instantiated");
/*
      // report on inspector
      XServiceInfo si = Lo.qi(XServiceInfo.class, inspector);
      LOGGER.debug("Implementation name: " + si.getImplementationName());
      String[] serviceNames = si.getSupportedServiceNames();
      for(String nm : serviceNames)
         LOGGER.debug("SERVICE name: " + nm);
*/
            XIntrospection intro = createInstanceMCF(XIntrospection.class,
                    "com.sun.star.beans.Introspection");
            XIntrospectionAccess introAcc = intro.inspect(inspector);
            XIdlMethod method = introAcc.getMethod("inspect", -1);   // get ref to XInspector.inspect()
/*
      // alternative, low-level way of getting the method
      Object coreReflect = mcFactory.createInstanceWithContext(
                                     "com.sun.star.reflection.CoreReflection", xcc);
      XIdlReflection idlReflect = Lo.qi(XIdlReflection.class, coreReflect);
      XIdlClass idlClass = idlReflect.forName("org.openoffice.XInstanceInspector");
      XIdlMethod[] methods = idlClass.getMethods();
      LOGGER.debug("No of methods: " + methods.length);
      for(XIdlMethod m : methods)
         LOGGER.debug("  " + m.getName());

      XIdlMethod method = idlClass.getMethod("inspect");
*/
            LOGGER.debug("inspect() method was found: " + (method != null));

            Object[][] params = new Object[][]{new Object[]{obj, title}};
            method.invoke(inspector, params);
        } catch (Exception e) {
            throw new LOException("Could not access Inspector: " + e, e);
        }
    }  // end of accessInspector()


    public static void mriInspect(Object obj) throws LOException
  /* call MRI's inspect()
     Available from http://extensions.libreoffice.org/extension-center/mri-uno-object-inspection-tool
                  or http://extensions.services.openoffice.org/en/project/MRI
     Docs: https://github.com/hanya/MRI/wiki
     Forum tutorial: https://forum.openoffice.org/en/forum/viewtopic.php?f=74&t=49294
  */ {
        XIntrospection xi = createInstanceMCF(XIntrospection.class, "mytools.Mri");
        if (xi == null) {
            throw new LOException("MRI Inspector SERVICE could not be instantiated");
        }

        LOGGER.debug("MRI Inspector SERVICE instantiated");
        xi.inspect(obj);
    }  // end of mriInspect()


    // ------------------ color methods ---------------------


    public static int getColorInt(java.awt.Color color)
    // return the color as an integer, ignoring the alpha channel
    {
        if (color == null) {
            LOGGER.debug("No color supplied");
            return 0;
        } else
            return (color.getRGB() & 0xffffff);
    } // end of getColorInt()


    public static int hexString2ColorInt(String colStr)
    // e.g. "#FF0000", "0xFF0000"
    {
        java.awt.Color color = java.awt.Color.decode(colStr);
        return getColorInt(color);
    }


    public static String getColorHexString(java.awt.Color color) {
        if (color == null) {
            LOGGER.debug("No color supplied");
            return "#000000";
        } else
            return int2HexString(color.getRGB() & 0xffffff);
    }  // end of getColorHexString()


    public static String int2HexString(int val) {
        String hex = Integer.toHexString(val);
        if (hex.length() < 6)
            hex = "000000".substring(0, 6 - hex.length()) + hex;
        return "#" + hex;
    }  // end of int2HexString()


    // ================== other utils =============================


    public static void wait(int ms) throws LOException    // I can never remember the name :)
    {
        delay(ms);
    }


    public static void delay(int ms) throws LOException {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new LOException("Can't delay.", e);
        }
    }


    public static boolean isNullOrEmpty(String s) {
        return ((s == null) || (s.length() == 0));
    }


/*
    public static void waitEnter() {
        LOGGER.debug("Press Enter to continue...");
        try {
            System.in.read();
        } catch (java.io.IOException e) {
        }
    }  // end of waitEnter()
*/


    public static String getTimeStamp() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date());
    }


    public static void printNames(String[] names) {
        printNames(names, 4);
    }

    public static void printNames(String[] names, int numPerLine)
    // print a large array with <numPerLine> strings/line, indented by 2 spaces
    {
        if (names == null)
            LOGGER.debug("  No names found");
        else {
            Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
            int nlCounter = 0;
            LOGGER.debug("No. of names: " + names.length);
            for (String name : names) {
                LOGGER.debug("  \"" + name + "\"");
                nlCounter++;
                if (nlCounter % numPerLine == 0) {
                    LOGGER.debug("\n");
                    nlCounter = 0;
                }
            }
            LOGGER.debug("\n\n");
        }
    }  // end of printNames()

    public static void printTable(String name, Object[][] table) {
        LOGGER.debug("-- " + name + " ----------------");
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++)
                LOGGER.debug("  " + table[i][j]);
            LOGGER.debug("\n");
        }
        LOGGER.debug("-----------------------------\n");
    } // end of printTable()


    public static String capitalize(String s) {
        if ((s == null) || (s.length() == 0))
            return null;
        else if (s.length() == 1)
            return s.toUpperCase();
        else
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }  // end of capitalize()


/*
    public static int parseInt(String s) {
        if (s == null)
            return 0;
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            LOGGER.debug(s + " could not be parsed as an int; using 0");
            return 0;
        }
    }  // end of parseInt()
*/


/*
    public static void addJar(String jarPath)
    // load this JAR into the classloader at run time
    // from http://stackoverflow.com/questions/60764/how-should-i-load-jars-dynamically-at-runtime
    {
        try {
            URLClassLoader classLoader =
                    (URLClassLoader) ClassLoader.getSystemClassLoader();
            java.lang.reflect.Method m =
                    URLClassLoader.class.getDeclaredMethod("addURL", java.net.URL.class);
            m.setAccessible(true);
            m.invoke(classLoader, new java.net.URL(jarPath));
        } catch (java.lang.Exception e) {
            LOGGER.debug(e);
        }
    }  // end of addJar()
*/


    // ------------------- container manipulation --------------------


/*
    public static String[] getContainerNames(XIndexAccess con)
    // extract the names of the elements in the indexed container
    {
        if (con == null) {
            LOGGER.debug("Container is null");
            return null;
        }

        int numElems = con.getCount();
        if (numElems == 0) {
            LOGGER.debug("No elements in the container");
            return null;
        }

        ArrayList<String> namesList = new ArrayList<String>();
        for (int i = 0; i < numElems; i++) {
            try {
                XNamed named = Lo.qi(XNamed.class, con.getByIndex(i));
                namesList.add(named.getName());
            } catch (Exception e) {
                LOGGER.debug("Could not access name of element " + i);
            }
        }

        int sz = namesList.size();
        if (sz == 0) {
            LOGGER.debug("No element names found in the container");
            return null;
        }

        String[] names = new String[sz];
        for (int i = 0; i < sz; i++)
            names[i] = namesList.get(i);

        return names;
    }  // end of getContainerNames()


    public static XPropertySet findContainerProps(XIndexAccess con, String nm) {
        if (con == null) {
            LOGGER.debug("Container is null");
            return null;
        }

        for (int i = 0; i < con.getCount(); i++) {
            try {
                Object oElem = con.getByIndex(i);
                XNamed named = Lo.qi(XNamed.class, oElem);
                if (named.getName().equals(nm)) {
                    return (XPropertySet) Lo.qi(XPropertySet.class, oElem);
                }
            } catch (Exception e) {
                LOGGER.debug("Could not access element " + i);
            }
        }

        LOGGER.debug("Could not find a \"" + nm + "\" property set in the container");
        return null;
    }  // end of findContainerProps()
*/


}  // end of Lo class

