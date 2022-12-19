/*
* SPDX-FileCopyrightText: (C) Copyright 2021 CSI Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */
package it.csi.test.epoll.pec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.QPDecoderStream;


/**
 * <p>
 * PecParser2 class.
 * </p>
 *
 * @author mirco
 * @version $Id: $Id
 */
public class PecParser2 {

    /** Constant <code>logger</code> */
    protected static final Logger logger = Logger.getLogger ( PecParser2.class.getName () );

    private int level = 0;

    private boolean showStructure = true;

    private boolean saveAttachments = false;

    private int attnum = 1;

    private static String indentStr = "                                               ";

    private final String postacertEmlName = "postacert.eml";

    private final String daticertXmlName = "daticert.xml";

    private final String segnaturaXmlName = "Segnatura.xml";

    // boolean emlFound = false;
    // File f;
    // EmailBody testo;

    private String daticertXml;

    private String segnaturaXml;

    private DataHandler postacertEml;

    private String postacertEmlSubject;

    private String postacertEmlSenderAddress;

    private EmailBody postacertEmlBody;

    Map<String, DataHandler> attachments = new HashMap<String, DataHandler> ();

    private StringBuffer dumpLog = new StringBuffer ();

    /**
     * <p>
     * Getter for the field <code>dumpLog</code>.
     * </p>
     *
     * @return a {@link java.lang.StringBuffer} object.
     */
    public StringBuffer getDumpLog () {
        return dumpLog;
    }

    // public EmailBody getTesto() {
    // return testo;
    // }

    /**
     * Ritorna una lista degli allegati all'email. Gli allegati devono essere specificati come javax.mail.Part.ATTACHMENT altrimenti non vengono considerati Nel
     * caso di nomi doppi, oppure dello stesso allegato contenuto in un eml allegato il nome viene generato con incluso livello o progessivo
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, DataHandler> getAttachments () {
        return attachments;
    }

    /**
     * ritorna il contenuto xml di daticert.xml
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDaticertXml () {
        return daticertXml;
    }

    /**
     * <p>
     * Getter for the field <code>segnaturaXml</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSegnaturaXml () {
        return segnaturaXml;
    }

    /**
     * ritorna il messaggio postacert.eml
     *
     * @return a {@link javax.activation.DataHandler} object.
     */
    public DataHandler getPostacertEml () {
        return postacertEml;
    }

    /**
     * <p>
     * Getter for the field <code>postacertEmlSubject</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPostacertEmlSubject () {
        return postacertEmlSubject;
    }

    /**
     * <p>
     * Getter for the field <code>postacertEmlSenderAddress</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPostacertEmlSenderAddress () {
        return postacertEmlSenderAddress;
    }

    /**
     * <p>
     * Getter for the field <code>postacertEmlBody</code>.
     * </p>
     *
     * @return a {@link it.attocchi.mail.parts.EmailBody} object.
     */
    public EmailBody getPostacertEmlBody () {
        return postacertEmlBody;
    }

    /**
     * <p>
     * Constructor for PecParser2.
     * </p>
     */
    public PecParser2 () {
        super ();
        // String emlFileName, boolean saveFile, File f
        // this.emlFileName = emlFileName;
        // this.saveAttachments = saveFile;
        // this.f = f;
    }

    /*
     * 
     */
    /**
     * <p>
     * dumpPart.
     * </p>
     *
     * @param part a {@link javax.mail.Part} object.
     * @throws java.lang.Exception if any.
     */
    public void dumpPart ( Part part ) throws Exception {
        if ( part instanceof Message )
            dumpEnvelope ( (Message) part );

        //		/**
        //		 * Dump input stream ..
        //		 *
        //		 * InputStream is = p.getInputStream(); // If "is" is not already
        //		 * buffered, wrap a BufferedInputStream // around it. if (!(is
        //		 * instanceof BufferedInputStream)) is = new BufferedInputStream(is);
        //		 * int c; while ((c = is.read()) != -1) System.out.write(c);
        //		 **/

        String partContentType = part.getContentType ();
        try {
            log ( "CONTENT-TYPE: " + ( new ContentType ( partContentType ) ).toString () );
        } catch ( ParseException pex ) {
            log ( "BAD CONTENT-TYPE: " + partContentType );
        }
        String partFilename = part.getFileName ();
        String partFilenameDecoded = partFilename;
        if ( partFilename != null ) {
            log ( "FILENAME: " + partFilename );
            // rilevati dei casi con =?utf-8?Q?segnatura.xml?=;
            partFilenameDecoded = MimeUtility.decodeText ( partFilename );
        }

        /*
         * Using isMimeType to determine the content type avoids fetching the actual content data until we need it.
         */
        // if (p.isMimeType("text/plain")) {
        // if (p.isMimeType("text/*")) {
        if ( part.isMimeType ( "text/plain" ) || part.isMimeType ( "text/html" ) ) {
            log ( "This is plain text" );
            log ( "---------------------------" );
            if ( !showStructure && !saveAttachments )
                logger.debug ( (String) part.getContent () );

            // if (emlFound) {
            // testo = new EmailBody((String) part.getContent(),
            // part.getContentType());
            // return;
            // }
        } else if ( part.isMimeType ( "application/xml" ) || part.isMimeType ( "text/xml" ) || part.isMimeType ( "application/octet-stream" ) ) {
            log ( "This is xml or octet-stream" );
            log ( "---------------------------" );
            if ( !showStructure && !saveAttachments )
                logger.debug ( (String) part.getContent () );

            // verifica daticert.xml e segnatura.xml
            if ( daticertXmlName.equalsIgnoreCase ( partFilenameDecoded ) && level <= 2 ) {
                /*
                 * il filtro sul livello 2 viene impostato per evitare di leggere dati da eventuali eml allegati dentro la pec
                 */
                log ( "detected " + daticertXmlName + " at level " + level );
                daticertXml = decodeXml ( part );
            } else if ( segnaturaXmlName.equalsIgnoreCase ( partFilenameDecoded ) && level <= 4 ) {
                /*
                 * il filtro sul livello 4 viene impostato per arrivare a leggere nel messaggio ricevuto esempio: messaggio/busta pec --> * embedded message
                 * postacert.eml --> allegato segnatura.xml
                 */
                log ( "detected " + segnaturaXmlName + " at level " + level );
                segnaturaXml = decodeXml ( part );
            }

        } else if ( part.isMimeType ( "multipart/*" ) ) {
            log ( "This is a Multipart" );
            log ( "---------------------------" );
            Multipart mp = (Multipart) part.getContent ();
            level++;
            int count = mp.getCount ();
            for ( int i = 0; i < count; i++ ) {
                dumpPart ( mp.getBodyPart ( i ) );
                // /*
                // * Blocco se ne ho trovato il testo (per evitare di assegnare
                // il
                // * contenuto di eventuali allegati txt
                // */
                // if (testo != null)
                // break;
            }
            level--;
        } else if ( part.isMimeType ( "message/rfc822" ) ) {
            log ( "This is a Nested Message" );
            log ( "---------------------------" );

            // verifica postacert.eml
            if ( postacertEmlName.equalsIgnoreCase ( partFilenameDecoded ) && level <= 2 ) {
                log ( "detected " + postacertEmlName + " at level " + level );
                // saveAttachment((Part) part.getContent(), filename);
                // emlFound = true;
                Message tmp = (Message) ( (Part) part.getContent () );
                postacertEml = tmp.getDataHandler ();
                postacertEmlSenderAddress = MailUtils.getSenderAddress ( tmp );
                postacertEmlSubject = tmp.getSubject ();
                postacertEmlBody = MailUtils.getBody ( tmp );
            }

            level++;
            dumpPart ( (Part) part.getContent () );

            // if (emlFound)
            // return;

            level--;
        } else {
            if ( !showStructure && !saveAttachments ) {
                /*
                 * If we actually want to see the data, and it's not a MIME type we know, fetch it and check its Java type.
                 */
                Object o = part.getContent ();
                if ( o instanceof String ) {
                    log ( "This is a string" );
                    log ( "---------------------------" );
                    logger.debug ( (String) o );
                    // if (emlFound) {
                    // testo = new EmailBody((String) part.getContent(),
                    // part.getContentType());
                    // return;
                    // }

                } else if ( o instanceof InputStream ) {
                    log ( "This is just an input stream" );
                    log ( "---------------------------" );
                    InputStream is = (InputStream) o;
                    int c;
                    while ( ( c = is.read () ) != -1 ) {
                        System.out.write ( c );
                    }
                } else {
                    log ( "This is an unknown type" );
                    log ( "---------------------------" );
                    log ( o.toString () );
                    // if (emlFound) {
                    // testo = new EmailBody(o.toString(),
                    // part.getContentType());
                    // return;
                    // }
                }
            } else {
                // just a separator
                log ( "---------------------------" );
            }
        }

        /*
         * If we're saving attachments, write out anything that looks like an attachment into an appropriately named file. Don't overwrite existing files to
         * prevent mistakes.
         */
        if ( level != 0 && part instanceof MimeBodyPart && !part.isMimeType ( "multipart/*" ) ) {
            String disp = part.getDisposition ();
            // many mailers don't include a Content-Disposition
            // if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
            if ( disp != null && disp.equalsIgnoreCase ( Part.ATTACHMENT ) ) {
                if ( partFilenameDecoded == null ) {
                    partFilenameDecoded = "Attachment" + attnum++;
                }
                // partFilename = MimeUtility.decodeText(partFilename);

                try {
                    if ( saveAttachments ) {
                        log ( "Saving attachment to file " + partFilenameDecoded );
                        File f = new File ( partFilenameDecoded );
                        if ( f.exists () )
                            // XXX - could try a series of names
                            throw new IOException ( "file exists" );
                        ( (MimeBodyPart) part ).saveFile ( f );
                    }
                    // attachments.add(((MimeBodyPart) part).getDataHandler());
                    /*
                     * alcuni allegati potrebbero avere lo stesso nome, nel caso di eml annidati, quindi concateno un tocken univoco per livello
                     */
                    int count = 0;
                    while ( attachments.containsKey ( partFilenameDecoded ) ) {

                        int extPos = partFilenameDecoded.lastIndexOf ( '.' );

                        String filenameNoExt = null;
                        String ext = null;
                        if ( extPos > 0 ) {
                            filenameNoExt = partFilenameDecoded.substring ( 0, extPos );
                            ext = partFilenameDecoded.substring ( extPos + 1 );
                        } else {
                            filenameNoExt = partFilenameDecoded;
                            ext = "";
                        }

                        if ( count == 0 )
                            partFilenameDecoded = filenameNoExt + "_Level" + level + ext;
                        else
                            partFilenameDecoded = filenameNoExt + "_Level" + level + "_" + count + ext;
                        count++;
                    }
                    attachments.put ( partFilenameDecoded, ( (MimeBodyPart) part ).getDataHandler () );

                } catch ( IOException ex ) {
                    log ( "Failed to save attachment: " + ex );
                }
                log ( "---------------------------" );
            }
        }
    }

    private String decodeXml ( Part part ) throws IOException, MessagingException {
        String xmlDecoded = null;

        // da specifiche sempre UTF-8
        if ( part.getContent () instanceof BASE64DecoderStream ) {
            BASE64DecoderStream base64DecoderStream = (BASE64DecoderStream) part.getContent ();
            StringWriter writer = new StringWriter ();
            IOUtils.copy ( base64DecoderStream, writer );
            String base64decodedString = writer.toString ();
            // byte[] encodedMimeByteArray =
            // Base64.encodeBase64(base64decodedString.getBytes());
            // String encodedMimeString = new
            // String(encodedMimeByteArray);
            xmlDecoded = base64decodedString;

            // byte[] inCodec =
            // IOUtils.toByteArray(part.getInputStream());
            // byte[] outCodec = Base64.decodeBase64(inCodec);
            // daticertXml = new String(outCodec);
        } else if ( part.getContent () instanceof QPDecoderStream ) {
            /*
             * nelle ricevute provenienti da alcuni provider e' di questo tipo https://docs.oracle.com/cd/E19957-01/816-6028- 10/asd3j.htm#1028352
             */
            // BufferedInputStream bis = new
            // BufferedInputStream(part.getContent());
            // ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // while (true) {
            // int c = bis.read();
            // if (c == -1) {
            // break;
            // }
            // baos.write(c);
            // }
            // daticertXml = new String(baos.toByteArray());
            xmlDecoded = IOUtils.toString ( part.getInputStream () );

            // byte[] inCodec =
            // IOUtils.toByteArray(part.getInputStream());
            // byte[] outCodec =
            // QuotedPrintableCodec.decodeQuotedPrintable(inCodec);
            // daticertXml = new String(outCodec);
            // } else if (part.getContent() instanceof String) {
            // daticertXml = IOUtils.toString(part.getInputStream());
            // } else if (part.getContent() instanceof
            // javax.mail.util.SharedByteArrayInputStream) {
            // // SharedByteArrayInputStream sbais =
            // // (SharedByteArrayInputStream) contentObject;
            // daticertXml = IOUtils.toString(part.getInputStream());
        } else {
            logger.warn ( "unknow xml encoding instance type, used as String: " + part.getContent ().toString () );
            xmlDecoded = IOUtils.toString ( part.getInputStream () );
        }

        return xmlDecoded;
    }

    /**
     * <p>
     * dumpEnvelope.
     * </p>
     *
     * @param m a {@link javax.mail.Message} object.
     * @throws java.lang.Exception if any.
     */
    public void dumpEnvelope ( Message m ) throws Exception {
        log ( "This is the message envelope" );
        log ( "---------------------------" );
        Address [] a;
        // FROM
        if ( ( a = m.getFrom () ) != null ) {
            for ( int j = 0; j < a.length; j++ )
                log ( "FROM: " + a [j].toString () );
        }

        // REPLY TO
        if ( ( a = m.getReplyTo () ) != null ) {
            for ( int j = 0; j < a.length; j++ )
                log ( "REPLY TO: " + a [j].toString () );
        }

        // TO
        if ( ( a = m.getRecipients ( Message.RecipientType.TO ) ) != null ) {
            for ( int j = 0; j < a.length; j++ ) {
                log ( "TO: " + a [j].toString () );
                InternetAddress ia = (InternetAddress) a [j];
                if ( ia.isGroup () ) {
                    InternetAddress [] aa = ia.getGroup ( false );
                    for ( int k = 0; k < aa.length; k++ )
                        log ( "  GROUP: " + aa [k].toString () );
                }
            }
        }

        // SUBJECT
        log ( "SUBJECT: " + m.getSubject () );

        // DATE
        Date d = m.getSentDate ();
        log ( "SendDate: " + ( d != null ? d.toString () : "UNKNOWN" ) );

        // FLAGS
        Flags flags = m.getFlags ();
        StringBuffer sb = new StringBuffer ();
        Flags.Flag [] sf = flags.getSystemFlags (); // get the system flags

        boolean first = true;
        for ( int i = 0; i < sf.length; i++ ) {
            String s;
            Flags.Flag f = sf [i];
            if ( f == Flags.Flag.ANSWERED )
                s = "\\Answered";
            else if ( f == Flags.Flag.DELETED )
                s = "\\Deleted";
            else if ( f == Flags.Flag.DRAFT )
                s = "\\Draft";
            else if ( f == Flags.Flag.FLAGGED )
                s = "\\Flagged";
            else if ( f == Flags.Flag.RECENT )
                s = "\\Recent";
            else if ( f == Flags.Flag.SEEN )
                s = "\\Seen";
            else
                continue; // skip it
            if ( first )
                first = false;
            else
                sb.append ( ' ' );
            sb.append ( s );
        }
    }

    /**
     * <p>
     * log.
     * </p>
     *
     * @param s a {@link java.lang.String} object.
     */
    public void log ( String s ) {
        if ( showStructure )
            s = indentStr.substring ( 0, level * 2 ) + s;
        logger.debug ( s );
        dumpLog.append ( s + "\n" );
    }

    // private void saveAttachment(Part part, String filename) throws Exception
    // {
    // /*
    // * If we're saving attachments, write out anything that looks like an
    // * attachment into an appropriately named file. Don't overwrite existing
    // * files to prevent mistakes.
    // */
    // // if (saveAttachments && level != 0 && p instanceof MimeBodyPart &&
    // // !p.isMimeType("multipart/*")) {
    // if (saveAttachments && level != 0 && part instanceof MimeMessage) {
    // String disp = part.getDisposition();
    // // many mailers don't include a Content-Disposition
    // // if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
    // if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT) ||
    // disp.equalsIgnoreCase(Part.INLINE)) {
    // if (filename == null)
    // filename = "Attachment" + attnum++;
    // log("Saving attachment to file " + filename);
    // try {
    // File f = new File(filename);
    // if (f == null)
    // f = new File(filename);
    // if (f.exists())
    // // XXX - could try a series of names
    // throw new IOException("file exists");
    // ((MimeBodyPart) p).saveFile(f);
    // MailUtils.saveToEml((MimeMessage) part, f);
    // } catch (IOException ex) {
    // log("Failed to save attachment: " + ex);
    // }
    // log("---------------------------");
    // }
    // }
    // }
}
