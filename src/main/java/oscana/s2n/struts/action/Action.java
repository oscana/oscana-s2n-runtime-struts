/*
 * 取り込み元
 *    ライブラリ名：     struts1
 *    クラス名：         org.apache.struts.action.Action
 *    ソースリポジトリ： https://github.com/apache/struts1/blob/trunk/core/src/main/java/org/apache/struts/action/Action.java
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

import static nablarch.fw.ExecutionContext.*;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nablarch.core.message.ApplicationException;
import nablarch.core.message.Message;
import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.servlet.ServletExecutionContext;
import oscana.s2n.struts.Globals;

/**
 * <p>An <strong>Action</strong> is an adapter between the contents of an
 * incoming HTTP request and the corresponding business logic that should be
 * executed to process this request. The controller (RequestProcessor) will
 * select an appropriate Action for each request, create an instance (if
 * necessary), and call the <code>execute</code> method.</p>
 *
 * <p>Actions must be programmed in a thread-safe manner, because the
 * controller will share the same instance for multiple simultaneous requests.
 * This means you should design with the following items in mind: </p>
 *
 * <ul>
 *
 * <li>Instance and static variables MUST NOT be used to store information
 * related to the state of a particular request. They MAY be used to share
 * global resources across requests for the same action.</li>
 *
 * <li>Access to other resources (JavaBeans, session variables, etc.) MUST be
 * synchronized if those resources require protection. (Generally, however,
 * resource classes should be designed to provide their own protection where
 * necessary.</li>
 *
 * </ul>
 *
 * <p>When an <code>Action</code> instance is first created, the controller
 * will call <code>setServlet</code> with a non-null argument to identify the
 * servlet instance to which this Action is attached. When the servlet is to
 * be shut down (or restarted), the <code>setServlet</code> method will be
 * called with a <code>null</code> argument, which can be used to clean up any
 * allocated resources in use by this Action.</p>
 *
 * @version $Rev: 471754 $ $Date: 2005-08-26 21:58:39 -0400 (Fri, 26 Aug 2005)
 *          $
 *
 * @see org.apache.struts.action.Action
 */
public class Action {

    protected ActionMappingTool createStrutsMapping(HttpRequest nabHttpRequest, ExecutionContext context) {
        return new ActionMappingTool(nabHttpRequest, context);
    }

    protected HttpServletResponse createStrutsResponse(ExecutionContext context) {
        ServletExecutionContext cxt = (ServletExecutionContext) context;
        return cxt.getServletResponse();

    }

    protected HttpServletRequest createStrutsRequest(ExecutionContext context) {
        ServletExecutionContext cxt = (ServletExecutionContext) context;
        return cxt.getServletRequest();
    }

    /**
     * <p>Returns <code>true</code> if the current form's cancel button was
     * pressed. This method will check if the <code>Globals.CANCEL_KEY</code>
     * request attribute has been set, which normally occurs if the cancel
     * button generated by <strong>CancelTag</strong> was pressed by the user
     * in the current request. If <code>true</code>, validation performed by
     * an <strong>ActionForm</strong>'s <code>validate()</code> method will
     * have been skipped by the controller servlet.</p>
     *
     * <p> Since Action 1.3.0, the mapping for a cancellable Action must also have
     * the new "cancellable" property set to true. If "cancellable" is not set, and
     * the magic Cancel token is found in the request, the standard Composable
     * Request Processor will throw an InvalidCancelException. </p>
     *
     * @param request The servlet request we are processing
     * @return <code>true</code> if the cancel button was pressed;
     *         <code>false</code> otherwise.
     */
    protected boolean isCancelled(HttpServletRequest request) {

        return (request.getAttribute(Globals.CANCEL_KEY) != null);
    }

    /**
     * <p>Save the specified messages keys into the appropriate request
     * attribute for use by the &lt;html:messages&gt; tag (if messages="true"
     * is set), if any messages are required. Otherwise, ensure that the
     * request attribute is not created.</p>
     *
     * @param request  The servlet request we are processing.
     * @param messages The messages to save. <code>null</code> or empty
     *                 messages removes any existing ActionMessages in the
     *                 request.
     * @since Struts 1.1
     */
    protected void saveMessages(HttpServletRequest request, ActionMessages messages) {
        // Remove any messages attribute if none are required
        if ((messages == null) || messages.isEmpty()) {
            request.removeAttribute(Globals.MESSAGE_KEY);

            return;
        }

        // Save the messages we need
        List<String> msgs = new ArrayList<String>();
        for(Message msg :messages.getMessages()) {
            msgs.add(msg.formatMessage());
        }

        request.setAttribute(Globals.MESSAGE_KEY, msgs);
    }

    /**
     * <p>Save the specified error messages keys into the appropriate request
     * attribute for use by the &lt;html:errors&gt; tag, if any messages are
     * required. Otherwise, ensure that the request attribute is not
     * created.</p>
     *
     * @param request The servlet request we are processing
     * @param errors  Error messages object
     * @since Struts 1.2
     */
    protected void saveErrors(HttpServletRequest request, ActionMessages errors) {
        // Remove any error messages attribute if none are required
        if ((errors == null) || errors.isEmpty()) {
            request.removeAttribute(THROWN_APPLICATION_EXCEPTION_KEY);

            return;
        }

        // Save the error messages we need
        ApplicationException nablarchError = new ApplicationException();
        nablarchError.addMessages(errors.getMessages());
        request.setAttribute(THROWN_APPLICATION_EXCEPTION_KEY, nablarchError);
    }

    /**
     * <p>Save the specified error messages keys into the appropriate session
     * attribute for use by the &lt;html:messages&gt; tag (if
     * messages="false") or &lt;html:errors&gt;, if any error messages are
     * required. Otherwise, ensure that the session attribute is empty.</p>
     *
     * @param session The session to save the error messages in.
     * @param errors  The error messages to save. <code>null</code> or empty
     *                messages removes any existing error ActionMessages in
     *                the session.
     * @since Struts 1.3
     */
    protected void saveErrors(HttpSession session, ActionMessages errors) {
        // Remove the error attribute if none are required
        if ((errors == null) || errors.isEmpty()) {
            session.removeAttribute(THROWN_APPLICATION_EXCEPTION_KEY);

            return;
        }

        // Save the errors we need
        ApplicationException nablarchError = new ApplicationException();
        nablarchError.addMessages(errors.getMessages());
        session.setAttribute(THROWN_APPLICATION_EXCEPTION_KEY, nablarchError);
    }

}
