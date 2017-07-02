//package com.asiainfo.dtdt.common.util;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.jdom2.Document;
//import org.jdom2.Element;
//import org.jdom2.JDOMException;
//import org.jdom2.input.SAXBuilder;
//
///**
// * Description: 对象和xml之间的转换
// */
//public class XmlUtils {
//	/**
//    * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
//    * @param strxml
//    * @return
//    * @throws JDOMException
//    * @throws IOException
//    */
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	public static Map doXMLParse(String strxml) throws JDOMException, IOException {
//      strxml = strxml.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
//
//      if(null == strxml || "".equals(strxml)) {
//         return null;
//      }
//
//      Map m = new TreeMap();
//
//      InputStream in = new ByteArrayInputStream(strxml.getBytes("UTF-8"));
//      SAXBuilder builder = new SAXBuilder();
//      Document doc = builder.build(in);
//      Element root = doc.getRootElement();
//      List list = root.getChildren();
//      Iterator it = list.iterator();
//      while(it.hasNext()) {
//         Element e = (Element) it.next();
//         String k = e.getName();
//         String v = "";
//         List children = e.getChildren();
//         if(children.isEmpty()) {
//            v = e.getTextNormalize();
//         } else {
//            v = XmlUtils.getChildrenText(children);
//         }
//
//         m.put(k, v);
//      }
//
//      //关闭流
//      in.close();
//
//      return m;
//   }
//	/**
//    * 获取子结点的xml
//    * @param children
//    * @return String
//    */
//   @SuppressWarnings("rawtypes")
//   public static String getChildrenText(List children) {
//      StringBuffer sb = new StringBuffer();
//      if(!children.isEmpty()) {
//         Iterator it = children.iterator();
//         while(it.hasNext()) {
//            Element e = (Element) it.next();
//            String name = e.getName();
//            String value = e.getTextNormalize();
//            List list = e.getChildren();
//            sb.append("<" + name + ">");
//            if(!list.isEmpty()) {
//               sb.append(XmlUtils.getChildrenText(list));
//            }
//            sb.append(value);
//            sb.append("</" + name + ">");
//         }
//      }
//
//      return sb.toString();
//   }
//}
