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
package org.smartparam.engine.index;

import java.util.List;
import org.smartparam.engine.core.index.LevelIndex;
import org.smartparam.engine.core.matcher.Matcher;
import org.smartparam.engine.matchers.MatchAllMatcher;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.smartparam.engine.core.index.LevelIndexTestBuilder.levelIndex;
import static org.smartparam.engine.index.IndexTraversalOverridesTestBuilder.indexTraversalOverrides;

/**
 *
 * @author Adam Dubiel
 */
public class CustomizableLevelIndexWalkerTest {

    @Test
    public void shouldTraverseWholeTreeGreedilyAndReturnAllMatchingValues() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"*", "B", "*"}, "default");
        index.add(new String[]{"A", "B", "C"}, "value");
        index.add(new String[]{"A", "C", "*"}, "noise");

        CustomizableLevelIndexWalker<String> crawler = new CustomizableLevelIndexWalker<String>(
                indexTraversalOverrides().withGreediness(true, true, true).build(),
                new SimpleLevelLeafValuesExtractor<String>(),
                index,
                "A", "B", "C");

        // when
        List<String> values = crawler.find();

        // then
        assertThat(values).containsOnly("value", "default");
    }

    @Test
    public void shouldTraverseWhleTreeGreedilyWithOverridingAllowAllMatcherAndReturnAllMatchingValues() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"*", "B", "*"}, "default");
        index.add(new String[]{"A", "B", "C"}, "value");
        index.add(new String[]{"A", "C", "*"}, "allowAllValue");

        Matcher allowAll = new MatchAllMatcher();

        CustomizableLevelIndexWalker<String> crawler = new CustomizableLevelIndexWalker<String>(
                indexTraversalOverrides().withGreediness(true, true, true).overridingMatchers(null, allowAll, null).build(),
                new SimpleLevelLeafValuesExtractor<String>(),
                index, "A", "B", "C");

        // when
        List<String> values = crawler.find();

        // then
        assertThat(values).containsOnly("value", "default", "allowAllValue");
    }

    @Test
    public void shouldTraverseOnlyOneLevelGreedilyWhileUsingDefaultModeInRestOfLevels() {
        // given
        LevelIndex<String> index = levelIndex().withLevelCount(3).build();
        index.add(new String[]{"*", "B", "*"}, "default");
        index.add(new String[]{"A", "B", "C"}, "value");
        index.add(new String[]{"A", "C", "*"}, "allowAllValue");

        Matcher allowAll = new MatchAllMatcher();

        CustomizableLevelIndexWalker<String> crawler = new CustomizableLevelIndexWalker<String>(
                indexTraversalOverrides().withGreediness(false, true, false).overridingMatchers(null, allowAll, null).build(),
                new SimpleLevelLeafValuesExtractor<String>(),
                index, "A", "B", "C");

        // when
        List<String> values = crawler.find();

        // then
        assertThat(values).containsOnly("value", "allowAllValue");
    }

}
