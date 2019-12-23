package org.ivy;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class XwpfTest {

    public static void main(String[] args) throws Exception {
        // XWPFDocument doc;//word文档对象
        // InputStream in_stream=new FileInputStream("D:\\test.docx");//文件流
        // doc=new XWPFDocument(in_stream);
        // List<XWPFParagraph> paraList=doc.getParagraphs();
        // XWPFParagraph p=paraList.get(0);//获取到第一个段落,根据需要读取相应段落
        // List<XWPFRun> runsList=p.getRuns();//获取段落中的run列表
        // XWPFRun run=runsList.get(0);//获取第一个run对象
        // String s=run.getText(0);//获取到run里面的子句字符串
        // System.out.println(s);
        InputStream is = new FileInputStream("D:\\test.docx");
        XWPFDocument doc = new XWPFDocument(is);
        List<XWPFParagraph> paras = doc.getParagraphs(); // 将得到包含段落列表
        for (XWPFParagraph para : paras) {
            // 当前段落的属性
            // CTPPr pr = para.getCTP().getPPr();
            // System.out.println(para.getText());
            List<XWPFRun> runsLists = para.getRuns();// 获取段楼中的句列表
            for (XWPFRun item : runsLists) {
                int position = item.getTextPosition();
                String font = item.getFontFamily();
                String c = item.getColor();// 获取句的字体颜色
                int f = item.getFontSize();// 获取句中字的大小
                String s = item.getText(0);// 获取文本内容
                System.out.println(position);
                System.out.println(font);
                System.out.println(c);
                System.out.println(f);
                System.out.println(s);
                System.out.println("-------------------");
            }
            List<XWPFTable> tables = doc.getTables();
            List<XWPFTableRow> rows;
            List<XWPFTableCell> cells;
            for (XWPFTable table : tables) {
                // 表格属性
                // CTTable ppr = (CTTable) table.getCTTbl().getTblPr();
                // 获取行
                rows = table.getRows();
                for (XWPFTableRow row : rows) {
                    // 获取行对应的单元格
                    cells = row.getTableCells();
                    for (XWPFTableCell cell : cells) {

                        System.out.println(cell.getText());
                    }
                }
            }

        }
        // 关闭流
        // close(is);
    }

    // private static void close(InputStream is){
    // if(is != null){
    // try{
    // is.close();
    // }catch (IOException e){
    // e.printStackTrace();
    // }
    // }
    // }
}