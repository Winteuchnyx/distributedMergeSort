/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skripsiwinto;

/**
 *
 * @author Winto Junior Khosasi
 */
public class containerHostConfig {
    private String hostname = null;
    private int core = 0;
    
    public containerHostConfig(String hostname, int core){
        this.hostname = hostname;
        this.core = core;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getCore() {
        return core;
    }

    public void setCore(int core) {
        this.core = core;
    }
    
    
}
