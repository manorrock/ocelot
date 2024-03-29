package keyvault;

/**
 * The object backing a KeyVault secret.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SecretBundle {
   
    /**
     * Stores the attributes.
     */
    private SecretAttributes attributes;

    /**
     *
     */
    private String id;
    
    /**
     * Stores the value.
     */
    private String value;
    
    /**
     * Get the attributes.
     * 
     * @return the attributes.
     */
    public SecretAttributes getAttributes() {
        return attributes;
    }

    /**
     * Get the id.
     * 
     * @return the id.
     */
    public String getId() {
        return id;
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
     * Set the attributes.
     * 
     * @param attributes the attributes.
     */
    public void setAttributes(SecretAttributes attributes) {
        this.attributes = attributes;
    }

    /**
     * Set the id.
     * 
     * @param id the id.
     */
    public void setId(String id) {
        this.id = id;
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
