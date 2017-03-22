package example.com.hipocampo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by florencio on 11/02/17.
 */

public class Password {
    @SerializedName("description")
    private String description;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("observation")
    private String observation;

    public Password(String description, String username, String password, String observation) {
        this.description = description;
        this.username = username;
        this.password = password;
        this.observation = observation;
    }

    public Password() {
        //empty
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
