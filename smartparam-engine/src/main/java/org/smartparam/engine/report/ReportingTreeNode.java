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
package org.smartparam.engine.report;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.smartparam.engine.util.Formatter;
import org.smartparam.engine.util.Printer;

/**
 *
 * @author Adam Dubiel
 */
public class ReportingTreeNode<T> {

    private final ReportingTree<T> tree;

    private final Map<String, ReportingTreeNode<T>> children = new HashMap<String, ReportingTreeNode<T>>();

    private final ReportingTreeNode<T> parent;

    private final String level;

    private T value;

    private boolean containsDefault = false;

    public ReportingTreeNode(ReportingTree<T> tree, ReportingTreeNode<T> parent, String level) {
        this.tree = tree;
        this.parent = parent;
        this.level = level;
    }

    public ReportingTreeNode(ReportingTreeNode<T> cloneOf) {
        this.tree = cloneOf.tree;
        this.parent = cloneOf.parent;
        this.value = cloneOf.value;
        this.level = cloneOf.level;

        for (Entry<String, ReportingTreeNode<T>> child : cloneOf.children.entrySet()) {
            this.children.put(child.getKey(), new ReportingTreeNode<T>(child.getValue()));
        }
    }

    public T value() {
        return value;
    }

    public ReportingTreeNode<T> parent() {
        return parent;
    }

    public ReportingTreeNode<T> addDictionaryLevel(String levelValue) {
        return addLevel(levelValue);
    }

    public ReportingTreeNode<T> addAnyLevel() {
        containsDefault = true;
        return addLevel("*");
    }

    private ReportingTreeNode<T> addLevel(String levelValue) {
        ReportingTreeNode<T> child = new ReportingTreeNode<T>(tree, this, levelValue);
        children.put(levelValue, child);

        return child;
    }

    public void insertPath(String[] levelValues, int currentDepth, T value) {
        if (currentDepth >= levelValues.length) {
            this.value = tree.operationsFor(currentDepth).chooseValue(this.value, value);
            return;
        }

        String levelValue = levelValues[currentDepth];
        if ("*".equals(levelValue)) {
            if (containsDefault) {
                children.get("*").insertPath(levelValues, currentDepth + 1, value);
            } else {
                insertPathToAllChildren(levelValues, currentDepth, value);
            }
        } else {
            ReportingTreeNode<T> childNode = children.get(levelValue);
            if (childNode == null && containsDefault) {
                childNode = addLevelFromPath(levelValue, currentDepth, levelValues.length);
            }
            if (childNode != null) {
                insertPathTo(children.get(levelValue), levelValues, currentDepth, value);
            }
        }
    }

    private ReportingTreeNode<T> addLevelFromPath(String levelValue, int currentDepth, int levelsTotal) {
        ReportingTreeNode<T> addedLevel = addLevel(levelValue);
        if (currentDepth < levelsTotal - 1) {
            addedLevel.addAnyLevel();
        }
        return addedLevel;
    }

    private void insertPathToAllChildren(String[] levelValues, int currentDepth, T value) {
        for (ReportingTreeNode<T> child : children.values()) {
            insertPathTo(child, levelValues, currentDepth, value);
        }
    }

    private void insertPathTo(ReportingTreeNode<T> child, String[] levelValues, int currentDepth, T value) {
        int childDepth = currentDepth + 1;
        if (tree.isPotentiallyAmbiguous(childDepth)) {
            disambiguateChildren(levelValues[childDepth], childDepth);
        } else {
            child.insertPath(levelValues, childDepth, value);
        }
    }

    private void disambiguateChildren(String childValue, int childDepth) {
        if (children.containsKey(childValue)) {
            return;
        }

        Iterator<Entry<String, ReportingTreeNode<T>>> iterator = children.entrySet().iterator();
        Entry<String, ReportingTreeNode<T>> childEntry;
        while(iterator.hasNext()) {
            childEntry = iterator.next();
            DisjointSets<String> disjoint = tree.operationsFor(childDepth).split(childEntry.getKey(), childValue);

        }
    }

    public void harvestLeavesValues(List<T> appendTo) {
        if (isLeaf()) {
            if (value != null) {
                appendTo.add(value);
            }
        } else {
            for (ReportingTreeNode<T> child : children.values()) {
                child.harvestLeavesValues(appendTo);
            }
        }
    }

    private boolean isLeaf() {
        return children.isEmpty();
    }

    public void printNode(StringBuilder sb, int level) {
        String indent = Printer.repeat(' ', level << 2);
        boolean leaf = isLeaf();

        sb.append(indent).append("path : ").append(getLevelPath());
        if (leaf) {
            sb.append("   (leaf=").append(value).append(')');
        }
        sb.append(Formatter.NL);

        if (children != null) {
            for (ReportingTreeNode<T> child : children.values()) {
                child.printNode(sb, level + 1);
            }
        }
    }

    private String getLevelPath() {
        String lv = level != null ? level : "";
        return parent != null ? parent.getLevelPath() + "/" + lv : lv;
    }
}
