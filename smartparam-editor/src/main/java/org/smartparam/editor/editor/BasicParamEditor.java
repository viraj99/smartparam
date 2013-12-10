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
package org.smartparam.editor.editor;

import java.util.List;
import org.smartparam.editor.identity.DescribedCollection;
import org.smartparam.editor.identity.DescribedEntity;
import org.smartparam.editor.identity.RepositoryName;
import org.smartparam.editor.store.RepositoryStore;
import org.smartparam.engine.core.ParamEngine;
import org.smartparam.engine.core.parameter.ParamRepository;
import org.smartparam.engine.core.parameter.Level;
import org.smartparam.engine.core.parameter.Parameter;
import org.smartparam.engine.core.parameter.ParameterEntry;
import org.smartparam.editor.model.EditableLevel;
import org.smartparam.editor.model.LevelKey;
import org.smartparam.editor.model.ParameterEntryKey;

/**
 *
 * @author Adam Dubiel
 */
public class BasicParamEditor implements ParamEditor {

    private final RepositoryStore<EditableParamRepository> repositories;

    public BasicParamEditor(ParamEngine paramEngine) {
        List<ParamRepository> registeredRepositories = paramEngine.getConfiguration().getParamRepositories();
        repositories = new RepositoryStore<EditableParamRepository>(registeredRepositories, EditableParamRepository.class);
    }

    @Override
    public List<RepositoryName> repositories() {
        return repositories.storedRepositories();
    }

    @Override
    public void createParameter(RepositoryName in, Parameter parameter) {
        EditableParamRepository repository = repositories.get(in);
        repository.createParameter(parameter);
    }

    @Override
    public void updateParameter(RepositoryName in, String parameterName, Parameter parameter) {
        EditableParamRepository repository = repositories.get(in);
        repository.updateParameter(parameterName, parameter);
    }

    public void deleteParameter(RepositoryName in, String parameterName) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteParameter(parameterName);
    }

    @Override
    public DescribedEntity<EditableLevel> getLevel(RepositoryName from, LevelKey levelKey) {
        EditableParamRepository repository = repositories.get(from);
        return new DescribedEntity<EditableLevel>(from, (EditableLevel) repository.getLevel(levelKey));
    }

    @Override
    public DescribedEntity<LevelKey> addLevel(RepositoryName in, String parameterName, Level level) {
        EditableParamRepository repository = repositories.get(in);
        return new DescribedEntity<LevelKey>(in, repository.addLevel(parameterName, level));
    }

    @Override
    public void reorderLevels(RepositoryName in, List<LevelKey> orderedLevels) {
        EditableParamRepository repository = repositories.get(in);
        repository.reorderLevels(orderedLevels);
    }

    @Override
    public void updateLevel(RepositoryName in, LevelKey levelKey, Level level) {
        EditableParamRepository repository = repositories.get(in);
        repository.updateLevel(levelKey, level);
    }

    @Override
    public void deleteLevel(RepositoryName in, String parameterName, LevelKey levelKey) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteLevel(parameterName, levelKey);
    }

    @Override
    public DescribedEntity<ParameterEntryKey> addEntry(RepositoryName in, String parameterName, ParameterEntry entry) {
        EditableParamRepository repository = repositories.get(in);
        return new DescribedEntity<ParameterEntryKey>(in, repository.addEntry(parameterName, entry));
    }

    @Override
    public DescribedCollection<ParameterEntryKey> addEntries(RepositoryName in, String parameterName, List<ParameterEntry> entries) {
        EditableParamRepository repository = repositories.get(in);
        return new DescribedCollection<ParameterEntryKey>(in, repository.addEntries(parameterName, entries));
    }

    @Override
    public void updateEntry(RepositoryName in, ParameterEntryKey entryKey, ParameterEntry entry) {
        EditableParamRepository repository = repositories.get(in);
        repository.updateEntry(entryKey, entry);
    }

    @Override
    public void deleteEntry(RepositoryName in, ParameterEntryKey entryKey) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteEntry(entryKey);
    }

    @Override
    public void deleteEntries(RepositoryName in, Iterable<ParameterEntryKey> entryKeys) {
        EditableParamRepository repository = repositories.get(in);
        repository.deleteEntries(entryKeys);
    }
}