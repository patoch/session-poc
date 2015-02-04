package com.datastax.poc.dao;

import com.datastax.driver.core.*;
import com.datastax.poc.AppSession;

import java.util.Date;
import java.util.UUID;


/**
 * Created by patrick on 03/02/15.
 */
public class AppSessionDAO {

    private static AppSessionDAO onlyInstance;
    private PreparedStatement selectPstmt;
    private PreparedStatement insertPstmt;

    public static synchronized AppSessionDAO getInstance() {
        if (onlyInstance == null) {
            onlyInstance = new AppSessionDAO();
        }
        return onlyInstance;
    }


    private AppSessionDAO() {
        this.init();
    }


    private void init() {
        // Create keyspace and tables
        String ksCQL = "CREATE KEYSPACE IF NOT EXISTS my_app WITH replication = {'class': 'NetworkTopologyStrategy', 'datacenter1': 1};";
        String sessionTableCQL = "CREATE TABLE IF NOT EXISTS my_app.sessions (id TIMEUUID, update_ts TIMESTAMP, data TEXT, PRIMARY KEY (id, update_ts));";
        Cassandra.getSession().execute(ksCQL);
        Cassandra.getSession().execute(sessionTableCQL);

        // Prepare statements
        String cql;
        cql= "SELECT data FROM my_app.sessions WHERE id = ?;";
        selectPstmt = Cassandra.getSession().prepare(cql);

        cql = "INSERT INTO my_app.sessions(id, update_ts, data) VALUES (?, ?, ?);";
        insertPstmt = Cassandra.getSession().prepare(cql);
    }


    public AppSession getAppSession(UUID id) {
        AppSession appSession = new AppSession(id);
        BoundStatement bstmt = selectPstmt.bind(id);
        ResultSet rs = Cassandra.getSession().execute(bstmt);
        String jsonStr;
        for (Row row: rs.all()) {
            jsonStr = row.getString("data");
            appSession.mergeJson(jsonStr);
        }
        appSession.mergeChanges();
        return appSession;
    }


    public void setAppSession(AppSession appSession) {
        BoundStatement bstmt = insertPstmt.bind(appSession.getId(), new Date(), appSession.getChangeJson());
        Cassandra.getSession().execute(bstmt);
        appSession.mergeChanges();
    }

}
