/*
 * 取り込み元
 *    ライブラリ名：     struts1
 *    クラス名：         org.apache.struts.action.ActionForm
 *    ソースリポジトリ： https://github.com/apache/struts1/blob/trunk/core/src/main/java/org/apache/struts/action/ActionForm.java
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

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>An <strong>ActionForm</strong> is a JavaBean optionally associated with
 * one or more <code>ActionMappings</code>. Such a bean will have had its
 * properties initialized from the corresponding request parameters before the
 * corresponding <code>Action.execute</code> method is called.</p>
 *
 * <p>When the properties of this bean have been populated, but before the
 * <code>execute</code> method of the <code>Action</code> is called, this
 * bean's <code>validate</code> method will be called, which gives the bean a
 * chance to verify that the properties submitted by the user are correct and
 * valid. If this method finds problems, it returns an error messages object
 * that encapsulates those problems, and the controller servlet will return
 * control to the corresponding input form. Otherwise, the
 * <code>validate</code> method returns <code>null</code>, indicating that
 * everything is acceptable and the corresponding <code>Action.execute</code>
 * method should be called.</p>
 *
 * <p>This class must be subclassed in order to be instantiated. Subclasses
 * should provide property getter and setter methods for all of the bean
 * properties they wish to expose, plus override any of the public or
 * protected methods for which they wish to provide modified functionality.
 * </p>
 *
 * <p>Because ActionForms are JavaBeans, subclasses should also implement
 * <code>Serializable</code>, as required by the JavaBean specification. Some
 * containers require that an object meet all JavaBean requirements in order
 * to use the introspection API upon which ActionForms rely.</p>
 *
 * @version $Rev$ $Date: 2005-11-12 08:14:24 -0500 (Sat, 12 Nov 2005)
 *          $
 *
 * @see org.apache.struts.action.ActionForm
 */
public class ActionForm implements Serializable {

    /**
     * <p>Can be used to reset all bean properties to their default state.
     * This method is called before the properties are repopulated by the
     * controller.</p>
     *
     * <p>The default implementation attempts to forward to the HTTP version
     * of this method.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, ServletRequest request) {
        try {
            reset(mapping, (HttpServletRequest) request);
        } catch (ClassCastException e) {
            ; // FIXME: Why would this ever happen except a null
        }
    }

    /**
     * <p>Can be used to reset bean properties to their default state, as
     * needed.  This method is called before the properties are repopulated by
     * the controller.</p>
     *
     * <p>The default implementation does nothing. In practice, the only
     * properties that need to be reset are those which represent checkboxes
     * on a session-scoped form. Otherwise, properties can be given initial
     * values where the field is declared. </p>
     *
     * <p>If the form is stored in session-scope so that values can be
     * collected over multiple requests (a "wizard"), you must be very careful
     * of which properties, if any, are reset. As mentioned, session-scope
     * checkboxes must be reset to false for any page where this property is
     * set. This is because the client does not submit a checkbox value when
     * it is clear (false). If a session-scoped checkbox is not proactively
     * reset, it can never be set to false.</p>
     *
     * <p>This method is <strong>not</strong> the appropriate place to
     * initialize form value for an "update" type page (this should be done in
     * a setup Action). You mainly need to worry about setting checkbox values
     * to false; most of the time you can leave this method unimplemented.
     * </p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        // Default implementation does nothing
    }

    /**
     * <p>Can be used to validate the properties that have been set for this
     * non-HTTP request, and return an <code>ActionErrors</code> object that
     * encapsulates any validation errors that have been found. If no errors
     * are found, return <code>null</code> or an <code>ActionErrors</code>
     * object with no recorded error messages.</p>
     *
     * <p>The default implementation attempts to forward to the HTTP version
     * of this method.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return The set of validation errors, if validation failed; an empty
     *         set or <code>null</code> if validation succeeded.
     */
    public ActionErrors validate(ActionMapping mapping, ServletRequest request) {
        try {
            return (validate(mapping, (HttpServletRequest) request));
        } catch (ClassCastException e) {
            return (null);
        }
    }

    /**
     * <p>Can be used to validate the properties that have been set for this
     * HTTP request, and return an <code>ActionErrors</code> object that
     * encapsulates any validation errors that have been found. If no errors
     * are found, return <code>null</code> or an <code>ActionErrors</code>
     * object with no recorded error messages.</p>
     *
     * <p>The default implementation performs no validation and returns
     * <code>null</code>. Subclasses must override this method to provide any
     * validation they wish to perform.</p>
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     * @return The set of validation errors, if validation failed; an empty
     *         set or <code>null</code> if validation succeeded.
     * @see DynaActionForm
     */
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        return (null);
    }

    /**
     * リセットメソッド
     */
    public void reset() {
     // Default implementation does nothing
    }
}
