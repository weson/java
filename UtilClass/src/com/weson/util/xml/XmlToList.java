package com.weson.util.xml;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class XmlToList {
	/**
	 * @Description 把xml格式字符串转化成List<Map>集合
	 * @author 漫画-temdy
	 * @Date 2014-11-19
	 * @param xmlDoc xml格式字符串
	 * @return
	 */
	public static Map<String, String> xmlElements(String xmlDoc) {
		Map<String, String> entity = new HashMap<String, String>();
		try {
			// 创建一个新的字符串
			StringReader read = new StringReader(xmlDoc);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = sb.build(source);
			// 取的根元素
			Element root = doc.getRootElement();
			// System.out.println(root.getName());//输出根元素的名称（测试）
			// 得到根元素所有子元素的集合
			List<?> jiedian = root.getChildren();
			// 获得XML中的命名空间（XML中未定义可不写）
			Namespace ns = root.getNamespace();
			Element et = null;

			for (int i = 0; i < jiedian.size(); i++) {
				et = (Element) jiedian.get(i);// 循环依次得到子元素
				entity.put(et.getName(), et.getText());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return entity;
	}
}
