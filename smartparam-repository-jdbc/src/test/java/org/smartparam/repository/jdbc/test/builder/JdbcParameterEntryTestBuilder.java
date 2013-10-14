/*
 * Copyright 2013 Adam Dubiel.
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
package org.smartparam.repository.jdbc.test.builder;

import org.smartparam.engine.test.builder.AbstractParameterEntryTestBuilder;
import org.smartparam.repository.jdbc.model.JdbcParameterEntry;

/**
 *
 * @author Adam Dubiel
 */
public class JdbcParameterEntryTestBuilder extends AbstractParameterEntryTestBuilder<JdbcParameterEntry, JdbcParameterEntryTestBuilder> {

    private JdbcParameterEntryTestBuilder() {
        super(new JdbcParameterEntry());
    }

    public static JdbcParameterEntryTestBuilder jdbcParameterEntry() {
        return new JdbcParameterEntryTestBuilder();
    }

    @Override
    protected JdbcParameterEntryTestBuilder self() {
        return this;
    }

    public JdbcParameterEntryTestBuilder withId(long id) {
        entry.setId(id);
        return this;
    }
}