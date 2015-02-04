package com.datastax.poc.dao;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.RoundRobinPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.poc.Settings;


/**
 * Created by patrick on 03/02/15.
 */
public class Cassandra {

    private static Cluster s_cluster;
    private static Session s_session;


    public static synchronized Cluster getCluster() {

        if (s_cluster == null) {


            QueryOptions queryOptions = new QueryOptions()
                    .setConsistencyLevel(ConsistencyLevel.QUORUM)
                    .setFetchSize(100);

            SocketOptions socketOptions = new SocketOptions()
                    .setReadTimeoutMillis(105000);


            s_cluster = Cluster.builder()
                    .addContactPoints(Settings.getInstance().getContactPoints())
                    .withProtocolVersion(ProtocolVersion.V2)
                    .withPort(9042)
                    .withLoadBalancingPolicy(new TokenAwarePolicy(new RoundRobinPolicy()))
                    .withQueryOptions(queryOptions)
                    .withSocketOptions(socketOptions)
                    .build();
        }
        return s_cluster;
    }


    public static synchronized Session getSession() {
        if (s_session == null) {
            s_session = getCluster().connect();
        }
        return s_session;
    }

}
