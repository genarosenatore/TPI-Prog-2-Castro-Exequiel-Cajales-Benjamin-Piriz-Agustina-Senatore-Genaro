/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.config;

/**
 *
 * @author genar
 */
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import java.util.Map;
public class ConexionDB {

    private static final String CONFIG_FILE = "persistence.xml";
    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    static {
        cargarConfiguracion();
    }

    private ConexionDB() {
    }

    private static void cargarConfiguracion() {
        try {
            InputStream inputStream = ConexionDB.class
                    .getClassLoader()
                    .getResourceAsStream(CONFIG_FILE);
            if (inputStream == null) {
                throw new RuntimeException("No se encontró el archivo " + CONFIG_FILE);
            }
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(inputStream);
            NodeList properties = document.getElementsByTagName("property");
            Map<String, String> config = new HashMap<>();
            for (int i = 0; i < properties.getLength(); i++) {
                String name = properties.item(i).getAttributes().getNamedItem("name").getNodeValue();
                String value = properties.item(i).getAttributes().getNamedItem("value").getNodeValue();
                config.put(name, value);
            }
            driver = config.get("jdbc.driver");
            url = config.get("jdbc.url");
            user = config.get("jdbc.user");
            password = config.get("jdbc.password");
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException("Error cargando configuración de base de datos: " + e.getMessage(), e);
        }
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
