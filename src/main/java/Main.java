import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void writeString(String filename, String json) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .setPrettyPrinting()
                .create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        return gson.toJson(list, listType);
    }

    private static List<Employee> parseXML(String filename) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(filename));
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getOwnerDocument().getElementsByTagName("employee");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node employees = nodeList.item(i);
            if (Node.ELEMENT_NODE == employees.getNodeType()) {
                Element employee = (Element) employees;
                long id = Integer.parseInt(employee.getElementsByTagName("id").item(0).getTextContent());
                String firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();
                String lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();
                String country = employee.getElementsByTagName("country").item(0).getTextContent();
                int age = Integer.parseInt(employee.getElementsByTagName("age").item(0).getTextContent());
                list.add(new Employee(id, firstName, lastName, country, age));
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String absolutPath = "C:/Users/weyma/IdeaProjects/Task2 (XML-JSON)/";
        try {
            List<Employee> list = parseXML(absolutPath + "data.xml");
            String json = listToJson(list);
            writeString(absolutPath + "data.json", json);
        } catch (ParserConfigurationException err) {
            err.printStackTrace();
        } catch (IOException err) {
            err.printStackTrace();
        } catch (SAXException err) {
            err.printStackTrace();
        }
    }
}
