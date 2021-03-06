package Dom4JforGooglEearth_Kml;

import Dom4JforGooglEearth_Kml.Entity.PlacemarkEntity;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dom4JforKml {

    public static ArrayList<PlacemarkEntity> readkml() throws MalformedURLException {
        ArrayList pathlist=UploadServlet.getUploadFilePath();
        System.out.println("goto:"+pathlist.get(0));
        String filePath = "file:"+pathlist.get(0);
        //URL url = new URL("file:E:/readkml4methed/沪昆线12.11.kml");
        URL url = new URL(filePath);
        ArrayList<PlacemarkEntity> placemarkEntitiesList = new ArrayList<PlacemarkEntity>();
        try {
            Document document = parse(url);
            XPath xPath_Folder = document.createXPath("/xmlns:kml/xmlns:Document/xmlns:Folder/xmlns:Folder/xmlns:name[text() = '区间节点']/../xmlns:Folder//xmlns:Placemark");  //读取placemark
            xPath_Folder.setNamespaceURIs(createNamespace());

            List<Node> placemarkList = xPath_Folder.selectNodes(document);
            System.out.println("共找到" + placemarkList.size() + "个节点。");

            //PlacemarkEntity placemarkEntity = new PlacemarkEntity();

            for (Node placemark : placemarkList) {

                XPath xPath_Name_Coordinates = placemark.createXPath("./xmlns:name|.//xmlns:coordinates");
                xPath_Name_Coordinates.setNamespaceURIs(createNamespace());
                List<Node> attsofNode = xPath_Name_Coordinates.selectNodes(placemark);
                if (attsofNode.get(0).getStringValue().equals("未命名路径")) {

                } else {
                    PlacemarkEntity placemarkEntity = new PlacemarkEntity();
                    placemarkEntity.setName_Placemark(attsofNode.get(0).getStringValue());
                    placemarkEntity.setCoordinates_Placemark(attsofNode.get(1).getStringValue());
                    placemarkEntitiesList.add(placemarkEntity);
                }
            }
            for (PlacemarkEntity placemark:
                 placemarkEntitiesList) {
                System.out.println(placemark.getName_Placemark() + placemark.getCoordinates_Placemark());
            }
            System.out.println("刚打印的" + "共" + placemarkEntitiesList.size() + "个节点");

        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        return placemarkEntitiesList;
    }

    /**
     * 创建SAXReader的对象reader
     * @param url xml文件路径
     * @return
     * @throws DocumentException
     */
    public static Document parse(URL url) throws DocumentException {
        SAXReader reader = new SAXReader(new DocumentFactory());
        Document document = reader.read(url);
        if (document == null) {
            return null;
        } else {
            return document;
        }
    }

    /**
     * 创建命名空间
     * @return 返回命名空间变量
     */
    public static Map createNamespace() {
        Map namespace = new HashMap();
        //String nsURI = root.getNamespaceURI();
        namespace.put("xmlns", "http://www.opengis.net/kml/2.2");
        namespace.put("kml", "http://www.opengis.net/kml/2.2");
        return namespace;
    }
}
