package cosc202.andie;

import java.io.*;
import java.util.Properties;

/**
 * Manages the application settings, including loading and storing
 * configurations,
 * and handling language specific properties.
 * @author Jonathan Chan
 * @version 1.0
 */

public class Settings {

    // Path to the configuration file
    private static final String CONFIG_FILE_PATH = "config.properties";
    // Properties object to hold the configuration settings
    private static Properties configProperties = new Properties();
    // Properties object to hold the language specific texts
    private static Properties languageProperties = new Properties();

    // Static initializer block to load configuration and language properties at
    // startup
    static {
        loadConfigProperties();
        loadLanguageProperties(); // Load language properties based on the current setting at startup
    }

    /**
     * Loads the configuration properties from a file.
     * If the configuration file does not exist, it creates one and sets a default
     * language.
     */
    private static void loadConfigProperties() {
        try {
            File configFile = new File(CONFIG_FILE_PATH);
            if (!configFile.exists()) {
                // If the configuration file does not exist, create it and set default language
                configFile.createNewFile();
                setConfigProperty("language", "English"); // Default language
            } else {
                // If the configuration file exists, load the properties from it
                FileInputStream in = new FileInputStream(configFile);
                configProperties.load(in);
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets a configuration property and saves the changes to the configuration
     * file.
     *
     * @param key   The key of the configuration property to set.
     * @param value The value for the configuration property.
     */
    public static void setConfigProperty(String key, String value) {
        try {
            configProperties.setProperty(key, value);
            // Save the properties to the configuration file
            try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE_PATH)) {
                configProperties.store(fos, null);
            }
            // No immediate language switch or listener notification
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the language properties based on the current language setting.
     * If the language file does not exist, it logs an error.
     */
    private static void loadLanguageProperties() {
        String language = configProperties.getProperty("language", "English");
        // Determine the file name based on the current language setting
        String fileName = switch (language.toLowerCase()) {
            case "french" -> "fr_lang.properties";
            case "spanish" -> "es_lang.properties";
            default -> "en_lang.properties";
        };

        String relativePath = "./src/cosc202/" + fileName;
        File file = new File(relativePath);
        if (!file.exists()) {
            System.err.println("Language file '" + fileName + "' not found at path: " + file.getAbsolutePath());
            return;
        }

        // Load the language specific properties from the file
        try (FileReader reader = new FileReader(file)) {
            languageProperties.load(reader);
        } catch (IOException e) {
            System.err.println(Settings.getLanguageProperty("WARN_LANGUAGE_LOAD") + e.getMessage());
        }
    }

    /**
     * Retrieves a language specific property value based on a given key.
     * If the key is not found, it returns a default message indicating so.
     *
     * @param key The key of the language specific property to retrieve.
     * @return The value of the language specific property, or a default message if
     *         not found.
     */
    public static String getLanguageProperty(String key) {
        return languageProperties.getProperty(key, "Key not found: " + key);
    }
}
