/*
 * 取り込み元
 *    ライブラリ名：     struts1
 *    クラス名：         org.apache.struts.action.ActionForward
 *    ソースリポジトリ： https://github.com/apache/struts1/blob/trunk/core/src/main/java/org/apache/struts/action/ActionForward.java
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

import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;

/**
 * <p>An <strong>ActionForward</strong> represents a destination to which the
 * controller, RequestProcessor, might be directed to perform a
 * RequestDispatcher.forward or HttpServletResponse.sendRedirect to, as a
 * result of processing activities of an Action class. Instances of this class
 * may be created dynamically as necessary, or configured in association with
 * an ActionMapping instance for named lookup of potentially multiple
 * destinations for a particular mapping instance.</p>
 *
 * <p>An ActionForward has the following minimal set of properties. Additional
 * properties can be provided as needed by subclassses.</p>
 *
 * <ul>
 *
 * <li><strong>contextRelative</strong> - Should the path value be interpreted
 * as context-relative (instead of module-relative, if it starts with a '/'
 * character? [false]</li>
 *
 * <li><strong>name</strong> - Logical name by which this instance may be
 * looked up in relationship to a particular ActionMapping. </li>
 *
 * <li><strong>path</strong> - Module-relative or context-relative URI to
 * which control should be forwarded, or an absolute or relative URI to which
 * control should be redirected.</li>
 *
 * <li><strong>redirect</strong> - Set to true if the controller servlet
 * should call HttpServletResponse.sendRedirect() on the associated path;
 * otherwise false. [false]</li>
 *
 * </ul>
 *
 * <p>Since Struts 1.1 this class extends ForwardConfig and inherits the
 * contextRelative property.
 *
 * <p><strong>NOTE</strong> - This class would have been deprecated and
 * replaced by org.apache.struts.config.ForwardConfig except for the fact that
 * it is part of the public API that existing applications are using.</p>
 *
 * @version $Rev$ $Date: 2005-08-14 17:24:39 -0400 (Sun, 14 Aug 2005)
 *          $
 *
 * 移植内容の変更点：<br>
 *   <br>{@link org.apache.struts.config.ForwardConfig}を継承しないように変更する。<br>
 *
 * @see org.apache.struts.action.ActionForward
 *
 */
public class ActionForward {

    @SuppressWarnings("unused")
    private HttpRequest nabHttpRequest;
    @SuppressWarnings("unused")
    private ExecutionContext context;

    protected String name = null;
    protected String path = null;

    public ActionForward() {
        name = null;
        path = null;
    }

    /**
     * コンストラクタ。
     * @param nabHttpRequest リクエスト
     * @param context コンテキスト
     */
    public ActionForward(HttpRequest nabHttpRequest, ExecutionContext context) {
        this.nabHttpRequest = nabHttpRequest;
        this.context = context;
    }

    /**
     * Construct a new instance with the specified path.
     *
     * @param path Path for this instance
     */
    public ActionForward(String path) {
        this.path = path;
    }

    /**
     * パスを取得する。
     * @return パス
     */
    public String getPath() {
        return path;
    }

    /**
     * パスを設定する。
     * @param path パス
     */
    public void setPath(String path) {
        this.path = path;
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

    /**
     * リスポンスを返却する。
     * @return リスポンス
     */
    public HttpResponse toResponse() {
        if (this.path.endsWith(".jsp")) {
            return new HttpResponse(this.path);
        } else {
            return new HttpResponse("forward://" + this.path);
        }
    }

}
