package appconfig;

/**
 * The bundle for Key-Value responses.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class KeyValueBundle {

    /**
     * Stores the key.
     */
    private String key;
    
    /**
     * Stores the label.
     */
    private String label;
    
    /**
     * Stores the value.
     */
    private String value;

    /**
     * Get the key.
     * 
     * @return the key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the label.
     * 
     * @return the label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Get the value.
     * 
     * @return the value.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Set the key.
     * 
     * @param key the key.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Set the label.
     * 
     * @param label the label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Set the value.
     * 
     * @param value the value. 
     */
    public void setValue(String value) {
        this.value = value;
    }
}
