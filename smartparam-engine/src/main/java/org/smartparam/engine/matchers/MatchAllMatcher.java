/*
 * Copyright 2014 Adam Dubiel.
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
package org.smartparam.engine.matchers;

import org.smartparam.engine.annotated.annotations.ParamMatcher;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.core.type.Type;
import org.smartparam.engine.core.type.ValueHolder;

/**
 *
 * @author Adam Dubiel
 */
@ParamMatcher(MatchAllMatcher.MATCH_ALL)
public class MatchAllMatcher implements Matcher {

    public static final String MATCH_ALL = "equals/anything";

    @Override
    public <T extends ValueHolder> boolean matches(String value, String pattern, Type<T> type) {
        return true;
    }

}
