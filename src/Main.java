import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.DecimalFormat;

public class Main {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Falta el nombre del archivo XML como argumento.");
            System.exit(1);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(args[0]));
            doc.getDocumentElement().normalize();

            sales(doc);

            saveDocument(doc, "new_sales.xml");

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static void sales(Document doc) {

        Element root = doc.getDocumentElement();

        try {
            int numero = Integer.parseInt(JOptionPane.showInputDialog("¿Cuál es el porcentaje entre 5% y 15% que desea aumentar?"));

            if (numero < 5 || numero > 15) {
                JOptionPane.showMessageDialog(null, "Haga caso primo.");
                return;
            }

            String department = JOptionPane.showInputDialog("¿Qué departamento desea afectar?");
            double num = 1 + (numero * 0.01);

            NodeList salesData = root.getElementsByTagName("sale_record");

            for (int i = 0; i < salesData.getLength(); i++) {

                Element saleElement = (Element) salesData.item(i);
                String Department = saleElement.getElementsByTagName("department").item(0).getTextContent();

                if (Department.equals(department)) {

                    String sales = saleElement.getElementsByTagName("sales").item(0).getTextContent();
                    double newValue = Double.parseDouble(sales) * num;
                    DecimalFormat df = new DecimalFormat("#.##");
                    String formattedValue = df.format(newValue);
                    saleElement.getElementsByTagName("sales").item(0).setTextContent(formattedValue);
                }
            }

            System.out.println("Incremento aplicado al departamento '" + department + "'.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Fakkkkkk.");
        }
    }

    public static void saveDocument(Document doc, String fileName) {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        try {
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);

            FileWriter writer = new FileWriter(new File(fileName));
            StreamResult result = new StreamResult(writer);

            transformer.transform(source, result);
            
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
    }
}
