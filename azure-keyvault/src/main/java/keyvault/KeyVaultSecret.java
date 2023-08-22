package keyvault;

/**
 * The object backing a KeyVault secret.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class KeyVaultSecret {
    
    /**
     * Stores the id.
     */
    private String id;

    /**
     * Stores the value.
     */
    private String value;

    /**
     * Constructor.
     */
    public KeyVaultSecret() {
    }
    
    /**
     * Constructor.
     * 
     * @param value the secret value.
     */
    public KeyVaultSecret(String value) {
        this.value = value;
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
