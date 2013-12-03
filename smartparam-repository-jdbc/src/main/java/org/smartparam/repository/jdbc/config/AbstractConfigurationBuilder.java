/*
 * Copyright 2013 Adam Dubiel, Przemek Hertel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smartparam.repository.jdbc.config;

import org.polyjdbc.core.dialect.Dialect;
import org.polyjdbc.core.dialect.DialectRegistry;

/**
 *
 * @author Adam Dubiel
 */
public abstract class AbstractConfigurationBuilder<C extends DefaultJdbcConfig, B extends AbstractConfigurationBuilder<?, ?>> {

    private final C configuration;

    protected AbstractConfigurationBuilder(C configuration) {
        this.configuration = configuration;
    }

    public C build() {
        return configuration;
    }

    protected abstract B self();

    protected C configuration() {
        return configuration;
    }

    public B withDialect(Dialect dialect) {
        configuration.setDialect(dialect);
        return self();
    }

    public B withDialect(String dialectCode) {
        return withDialect(DialectRegistry.dialect(dialectCode));
    }

    public B withParameterTableName(String parameterTableName) {
        configuration.setParameterTable(parameterTableName);
        return self();
    }

    public B withParameterEntryTableName(String parameterEntryTableName) {
        configuration.setParameterEntryTable(parameterEntryTableName);
        return self();
    }

    public B withLevelTableName(String levelTableName) {
        configuration.setLevelTable(levelTableName);
        return self();
    }
}
