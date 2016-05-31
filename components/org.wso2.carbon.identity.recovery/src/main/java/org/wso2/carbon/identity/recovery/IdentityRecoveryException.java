/*
 *  Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.identity.recovery;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used for creating checked exceptions that can be handled.
 */
public class IdentityRecoveryException extends Exception {

    private static final long serialVersionUID = -7476500041182670526L;
    private List<ErrorInfo> errorInfoList = new ArrayList<>();

    public static class ErrorInfo {

        private String contextId = null;
        private String errorCode  = null;
        private String errorDescription = null;
        private String userErrorDescription = null;
        private Throwable cause = null;
        private Map<String, Object> parameters = new HashMap<>();

        private ErrorInfo(ErrorInfoBuilder builder) {

            this.contextId = builder.contextId;
            this.errorCode = builder.errorCode;
            this.userErrorDescription = builder.userErrorDescription;
            this.errorDescription = builder.errorDescription;
            if(MapUtils.isNotEmpty(builder.parameters)){
                this.parameters = builder.parameters;
            }
            this.cause = builder.cause;
        }

        public String getContextId() {
            return contextId;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public String getUserErrorDescription() {
            return userErrorDescription;
        }

        public Throwable getCause() {
            return cause;
        }

        public Map<String,Object> getParameters() {
            return this.parameters;
        }

        public Object getParameter(String key) {
            return this.parameters.get(key);
        }

        public static class ErrorInfoBuilder {

            private String contextId = null;
            private String errorCode  = null;
            private String errorDescription = null;
            private String userErrorDescription = null;
            private Throwable cause = null;
            private Map<String, Object> parameters = new HashMap<>();

            public ErrorInfoBuilder(String errorDescription) {
                this.errorDescription = errorDescription;
            }

            public ErrorInfoBuilder contextId(String contextId) {
                this.contextId = contextId;
                return this;
            }

            public ErrorInfoBuilder errorCode(String errorCode) {
                this.errorCode = errorCode;
                return this;
            }

            public ErrorInfoBuilder userErrorDescription(String userErrorDescription) {
                this.userErrorDescription = userErrorDescription;
                return this;
            }

            public ErrorInfoBuilder cause(Throwable cause) {
                this.cause = cause;
                return this;
            }

            public ErrorInfoBuilder parameters(Map<String,Object> parameters) {
                if(MapUtils.isNotEmpty(parameters)) {
                    this.parameters = parameters;
                }
                return this;
            }

            public ErrorInfoBuilder parameter(String key, Object value) {
                if(key != null){
                    this.parameters.put(key, value);
                }
                return this;
            }

            public ErrorInfo build(){
                return new ErrorInfo(this);
            }
        }
    }

    public IdentityRecoveryException(String errorDescription) {
        super(errorDescription);
    }

    public IdentityRecoveryException(String errorDescription, Throwable cause) {
        super(errorDescription, cause);
    }

    public void addErrorInfo(ErrorInfo errorInfo) {
        if(errorInfo == null || StringUtils.isBlank(errorInfo.errorDescription)){
            throw new IllegalArgumentException("ErrorInfo object is null or Error Description is blank");
        }
        this.errorInfoList.add(errorInfo);
    }

    public List<ErrorInfo> getErrorInfoList() {
        return errorInfoList;
    }

    public String getError() {

        StringBuilder builder = new StringBuilder();
        for(int i = this.errorInfoList.size() - 1; i >= 0; i--) {
            ErrorInfo info = this.errorInfoList.get(i);
            builder.append('[');
            builder.append(info.errorDescription);
            builder.append(':');
            builder.append(info.errorCode);
            builder.append(']');
        }
        return builder.toString();
    }
}
