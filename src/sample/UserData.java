package sample;

import java.io.Serializable;

public class UserData implements Serializable
{
    private String lastSourceDir = "";
    private String lastBuildDir = "";

    public String getLastSourceDir() {
        return lastSourceDir;
    }

    public void setLastSourceDir(String lastSourceDir) {
        this.lastSourceDir = lastSourceDir;
    }

    public String getLastBuildDir() {
        return lastBuildDir;
    }

    public void setLastBuildDir(String lastBuildDir) {
        this.lastBuildDir = lastBuildDir;
    }
}
