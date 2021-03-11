/*
 * 取り込み元
 *    ライブラリ名：     struts1
 *    クラス名：         org.apache.struts.action.ActionMapping
 *    ソースリポジトリ： https://github.com/apache/struts1/blob/trunk/core/src/main/java/org/apache/struts/action/ActionMapping.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package oscana.s2n.struts.action;

import java.util.Map;

/**
 * <p>An <strong>ActionMapping</strong> represents the information that the
 * controller, <code>RequestProcessor</code>, knows about the mapping of a
 * particular request to an instance of a particular <code>Action</code>
 * class. The <code>ActionMapping</code> instance used to select a particular
 * <code>Action</code> is passed on to that <code>Action</code>, thereby
 * providing access to any custom configuration information included with the
 * <code>ActionMapping</code> object.</p>
 *
 * <p>Since Struts 1.1 this class extends <code>ActionConfig</code>.
 *
 * <p><strong>NOTE</strong> - This class would have been deprecated and
 * replaced by <code>org.apache.struts.config.ActionConfig</code> except for
 * the fact that it is part of the public API that existing applications are
 * using.</p>
 *
 * @version $Rev$ $Date: 2005-08-26 21:58:39 -0400 (Fri, 26 Aug 2005)
 *          $
 *
 * 移植内容の変更点：<br>
 * <br>
 * {@link org.apache.struts.config.ActionConfig}を継承しないように変更する。<br>
 *
 * @see org.apache.struts.action.ActionMapping
 */
public class ActionMapping {

    private Map<String, ActionForward> forwardMap;

    protected String actionId = null;
    protected String attribute = null;
    protected String scope = "session";
    protected boolean validate = true;
    protected String path = null;
    protected String parameter = null;
    protected String type = null;
    protected String name = null;

    /**
     * コンストラクタ。
     * @param forwardMap マップ
     */
    public ActionMapping(Map<String, ActionForward> forwardMap) {
        this.forwardMap = forwardMap;
    }

    /**
     * コンストラクタ。
     * @param name 名
     * @return ActionForward
     */
    public ActionForward findForward(String name) {
        return this.forwardMap.get(name);
    }

    /**
     * アクションIdを取得する。
     * @return アクションId
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * アクションIdを設定する。
     * @param actionId アクションId
     */
    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    /**
     * バリデーションかどうかを判定する。
     * @return バリデーションかどうか
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * バリデーションを設定する。
     * @param validate バリデーション
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    /**
     * 属性を設定する。
     * @param attribute 属性
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    /**
     * スコープを設定する。
     * @param scope スコープ
     */
    public void setScope(String scope) {
        this.scope = scope;
    }

    /**
     * パスを設定する。
     * @param path パス
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * パラメータを設定する。
     * @param parameter パラメータ
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * スコープを取得する。
     * @return スコープ
     */
    public String getScope() {
        return scope;
    }

    /**
     * パスを取得する。
     * @return パス
     */
    public String getPath() {
        return path;
    }

    /**
     * パラメータを取得する。
     * @return パラメータ
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * 属性を取得する。
     * @return 属性
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * タイプを取得する。
     * @return タイプ
     */
    public String getType() {
        return type;
    }

    /**
     * タイプを設定する。
     * @param type タイプ
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 名を取得する。
     * @return 名
     */
    public String getName() {
        return name;
    }

    /**
     * 名を設定する。
     * @param name 名
     */
    public void setName(String name) {
        this.name = name;
    }
}
