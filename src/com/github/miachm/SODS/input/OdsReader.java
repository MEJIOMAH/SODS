package com.github.miachm.SODS.com.github.miachm.SODS.input;

import com.github.miachm.SODS.com.github.miachm.SODS.spreadsheet.SpreadSheet;
import com.github.miachm.SODS.com.github.miachm.SODS.exceptions.NotAnOds;
import com.github.miachm.SODS.com.github.miachm.SODS.exceptions.OperationNotSupported;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class OdsReader {
    private static final int TAM_BUFFER = 1000;
    private static final String CORRECT_MIMETYPE = "application/vnd.oasis.opendocument.spreadsheet";
    private static final String MANIFEST_PATH = "META-INF/manifest.xml";

    static public void load(InputStream in,SpreadSheet spread) throws IOException {
        /* TODO This code if for ods files in zip. But we could have XML-ONLY FILES */

        Map<String,byte[]> files = uncompress(in);

        checkMimeType(files);

        byte[] manifest = getManifest(files);
        readManifest(manifest);
    }

    private static Map<String,byte[]> uncompress(InputStream in) throws IOException {
        Map<String,byte[]> map = new HashMap<String,byte[]>();
        ZipInputStream zip = new ZipInputStream(in);
        byte buff[] = new byte[TAM_BUFFER];

        ZipEntry entry = zip.getNextEntry();
        while (entry != null){
            if (!entry.isDirectory()) {
                processEntry(map, zip, buff, entry);
                zip.closeEntry();
            }
            entry = zip.getNextEntry();
        }

        zip.close();

        return map;
    }

    private static void processEntry(Map<String,byte[]> map,ZipInputStream zin, byte[] buff,ZipEntry entry) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int len;

        while (true){
            len = zin.read(buff);
            if (len == -1) {
                stream.close();
                map.put(entry.getName(),stream.toByteArray());
                return;
            }
            stream.write(buff,0,len);
        }
    }

    private static void checkMimeType(Map<String,byte[]> map){
        byte[] mimetype = map.get("mimetype");
        if (mimetype == null)
            throw new NotAnOds("This file doesn't contain a mimetype");

        String mimetype_string = new String(mimetype);
        if (!mimetype_string.equals(CORRECT_MIMETYPE))
            throw new NotAnOds("This file doesn't look like an ODS file. Mimetype: " + mimetype_string);
    }

    private static byte[] getManifest(Map<String,byte[]> map){
        byte[] manifest = map.get(MANIFEST_PATH);
        if (manifest == null) {
            throw new NotAnOds("Error loading, it doesn't like an ODS file");
        }

        return manifest;
    }

    private static void readManifest(byte[] manifest) {
        try{
            DocumentBuilderFactory factory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(manifest));

            Element root = doc.getDocumentElement();
            if (!root.getNodeName().equals("manifest:manifest")) {
                throw new NotAnOds("The signature of the manifest is not valid. Is it an ODS file?");
            }


            NodeList files = doc.getElementsByTagName("manifest:file-entry");
            iterateFilesEntryManifest(files);

        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void iterateFilesEntryManifest(NodeList files){
        for (int i = 0;i < files.getLength();i++) {
            NodeList children = files.item(i).getChildNodes();

            for (int j = 0;j <children.getLength();j++) {
                if (children.item(j).getNodeName().equals("manifest:encryption-data")) {
                    throw new OperationNotSupported("This file has encription technology that it's not supported" +
                            "by this library");
                }
            }
        }
    }
}
