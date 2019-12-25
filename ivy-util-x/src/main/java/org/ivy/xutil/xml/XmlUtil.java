package org.ivy.xutil.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * XmlUtil
 *
 * @author miao.xl
 * @version 1.0
 * @date 2014年9月2日 下午3:51:25
 */
public class XmlUtil {

    private static final String encoding = "utf-8";


    /**
     * format a xml string
     *
     * @param data xml data
     * @return String
     */
    public static String format(String data) {
        StringWriter out = new StringWriter();
        SAXReader reader = new SAXReader();
        StringReader in = new StringReader(data);
        Document doc;
        try {
            doc = reader.read(in);
            OutputFormat formater = OutputFormat.createPrettyPrint();
            formater.setEncoding(encoding);
            XMLWriter writer = new XMLWriter(out, formater);
            writer.setEscapeText(true);
            writer.write(doc);
            writer.flush();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.getBuffer().toString();
    }
}
