/*
 * 取り込み元
 *    ライブラリ名：     struts1
 *    クラス名：         org.apache.struts.action.ActionMessages
 *    ソースリポジトリ： https://github.com/apache/struts1/blob/trunk/core/src/main/java/org/apache/struts/action/ActionMessages.java
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

import java.util.ArrayList;
import java.util.List;

import nablarch.core.message.ApplicationException;
import nablarch.core.message.Message;
import nablarch.core.validation.ValidationUtil;

/**
 * ActionMessagesとNablarch messageの変換をするクラス。<br>
 *
 * <br>
 * 移植内容の変更点：<br>
 * <br>
 * ・message属性：メッセージデータの形式をStrutsのもの(ActionMessageItem)からNablarchのもの(Message)に変更。<br>
 * ・addメソッド：add(String property, String messageId)を追加。<br>
 * ・sizeメソッド：メッセージデータの形式の変更に伴うカウント方法の変更。<br>
 * ・Serializable：直列化対象外に変更。
 *
 * @see org.apache.struts.action.ActionMessages
 *
 */
public class ActionMessages {

    /** グローバルメッセージ */
    public static final String GLOBAL_MESSAGE = "org.apache.struts.action.GLOBAL_MESSAGE";

    /** メッセージのリスト */
    private List<Message> messages = new ArrayList<Message>();

    public ActionMessages() {
        super();
    }

    /**
     * コンストラクタ。
     * @param messages メッセージ
     */
    public ActionMessages(ActionMessages messages) {
        this.messages = messages.getMessages();
    }

    /**
     * コンストラクタ。
     * @param applicationException ApplicationExceptionのオブジェクト
     */
    public ActionMessages(final ApplicationException applicationException) {
        messages.addAll(applicationException.getMessages());
    }

    /**
     * メッセージを追加する。
     *
     * @param property プロパティ
     * @param messageId メッセージid
     */
    public void add(String property, String messageId) {
        messages.add(ValidationUtil.createMessageForProperty(property, messageId));
    }

    /**
     * メッセージを消去する。
     */
    public void clear() {
        messages.clear();
    }

    /**
     * メッセージが空であればtrueを返却する。
     *
     * @return 空であればtrue、その以外はfalse
     */
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    /**
     * メッセージの個数を返却する。
     *
     * @return 長さ
     */
    public int size() {
        return messages.size();
    }

    /**
     * Messageリストを取得する。
     * @return Messageリスト
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * メッセージを追加する。
     * @param errors {@link ActionMessages}のオブジェクト
     */
    public void add(ActionMessages errors) {
        messages.addAll(errors.getMessages());

    }

    /**
     * ActionMessageを登録する。
     * @param property プロパティ
     * @param actionMessage {@link ActionMessages}のオブジェクト
     */
    public void add(String property, ActionMessage actionMessage) {
        messages.add(
                ValidationUtil.createMessageForProperty(property, actionMessage.getKey(), actionMessage.getValues()));
    }
}
