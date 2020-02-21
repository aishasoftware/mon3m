package aisha.service;
import aisha.bean.Connection;

public interface ConnectionService {
 
    public long addConnection(Connection Connection);
    public Connection listConnections(Connection Connection);
    public Connection getConnection(Connection Connection);
    public void updateConnection(Connection Connection);
    
}
