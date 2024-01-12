package keyvault;

/**
 * The SecretAttributes.
 * 
 * @author Manfred Riem (mriem@manorrock.com)
 */
public class SecretAttributes {
    
    /**
     * Stores the enabled flag.
     */
    private boolean enabled = true;
    
    /**
     * Stores the recovery level.
     */
    private String recoveryLevel = "";

    /**
     * Get the recovery level.
     * 
     * @return the recovery level.
     */
    public String getRecoveryLevel() {
        return recoveryLevel;
    }

    /**
     * Get the enabled flag.
     * 
     * @return the enabled flag.
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Set the enabled flag.
     * 
     * @param enabled the enabled flag.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /**
     * Set the recovery level.
     * 
     * @param recoveryLevel the recovery level.
     */
    public void setRecoveryLevel(String recoveryLevel) {
        this.recoveryLevel = recoveryLevel;
    }
}
