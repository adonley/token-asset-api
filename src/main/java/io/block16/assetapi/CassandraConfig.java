package io.block16.assetapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.convert.CustomConversions;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

import java.util.Collections;

@Configuration
@EnableCassandraRepositories(basePackages = "io.block16.assetapi.domain.cassandra")
class CassandraConfig extends AbstractCassandraConfiguration {

    @Value("${cassandra.contactpoints:localhost}")
    private String contactPoints;

    @Value("${cassandra.keyspace:ethereum}")
    private String keySpace;

    @Override
    public String getContactPoints() {
        return contactPoints;
    }

    @Override
    protected String getKeyspaceName() {
        return keySpace;
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }
}
