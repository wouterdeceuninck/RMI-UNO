package rmi.unogame.applicationServer.service.authentication.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.Objects;

public class JWTToken implements Serializable {

    private String userName;
    private long startTime;
    private String hashedChecksum;

    public JWTToken(String userName, long startTime, String hashedChecksum) {
        this.userName = userName;
        this.startTime = startTime;
        this.hashedChecksum = hashedChecksum;
    }

    public JWTToken() {
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setHashedChecksum(String hashedChecksum) {
        this.hashedChecksum = hashedChecksum;
    }

    public String getUserName() {
        return userName;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getHashedChecksum() {
        return hashedChecksum;
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JWTToken jwtToken = (JWTToken) o;
        return startTime == jwtToken.startTime &&
                Objects.equals(userName, jwtToken.userName) &&
                Objects.equals(hashedChecksum, jwtToken.hashedChecksum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, startTime, hashedChecksum);
    }
}
