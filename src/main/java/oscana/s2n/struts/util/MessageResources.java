/*
 * 取り込み元
 *    ライブラリ名：     struts1
 *    クラス名：         org.apache.struts.util.MessageResources
 *    ソースリポジトリ： https://github.com/apache/struts1/blob/trunk/core/src/main/java/org/apache/struts/util/MessageResources.java
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
package oscana.s2n.struts.util;

import java.util.Locale;

import nablarch.core.message.MessageLevel;
import nablarch.core.message.MessageUtil;

/**
 * リソースファイルからメッセージを取得するクラス。<br>
 *
 * <br>
 * 移植内容の変更点：<br>
 * <br>
 *    ・{@link nablarch.core.message.MessageUtil} を利用して、メッセージを取得する。<br>
 *    ・ローケールはサポートしない。<br>
 *    ・Strutsでは機能単位でメッセージファイルを作成するが、Nablarchではアプリケーション単位で作成する。<br>
 *
 */
public class MessageResources {

    private volatile static MessageResources messageResources;

    /**
     * default constructor method.
     */
    private MessageResources() {

    }

    /**
     * Create and return an instance of <code>MessageResources</code>.
     *
     * @param string Strutsではメッセージファイルの場所を表す文字列が入るがNablarchでは使用しない。
     * @return MessageResources
     */
    public static MessageResources getMessageResources(String string) {
        if (messageResources == null) {
            synchronized (MessageResources.class) {
                if (messageResources == null) {
                    messageResources = new MessageResources();
                }
            }
        }
        return messageResources;
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param messageId  The message messageId to look up
     * @param path The replacement for placeholder in the message
     * @param name The replacement for placeholder in the message
     * @return message
     */
    public String getMessage(String messageId, Object path, String name) {
        return MessageUtil.createMessage(MessageLevel.ERROR, messageId, path, name).formatMessage();
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param messageId  The message messageId to look up
     * @param path The replacement for placeholder in the message
     * @return message
     */
    public String getMessage(String messageId, Object... path) {
        return getMessage(null, messageId, path);
    }

    /**
     * Returns a text message for the specified messageId
     *
     * @param messageId The message messageId to look up
     * @return message
     */
    public String getMessage(String messageId) {
        return getMessage(null, messageId);
    }

    /**
     * Returns a text message after parametric replacement of the specified
     * parameter placeholders.
     *
     * @param locale The requested message Locale, or <code>null</code> for
     *               the system default Locale
     * @param messageId    The message messageId to look up
     * @param args   The replacement for placeholder in the message
     * @return message
     */
    public String getMessage(Locale locale, String messageId, Object... args) {
        return locale == null
                ? MessageUtil.createMessage(MessageLevel.ERROR, messageId, args).formatMessage()
                : MessageUtil.createMessage(MessageLevel.ERROR, messageId, args).formatMessage(locale);
    }

    /**
     * Returns a text message for the specified key, for the default Locale.
     *
     * @param locale The requested message Locale, or <code>null</code> for
     *               the system default Locale
     * @param messageId    The message messageId to look up
     * @return message
     */
    public String getMessage(Locale locale, String messageId) {
        return locale == null
                ? MessageUtil.createMessage(MessageLevel.ERROR, messageId).formatMessage()
                : MessageUtil.createMessage(MessageLevel.ERROR, messageId).formatMessage(locale);
    }
}
