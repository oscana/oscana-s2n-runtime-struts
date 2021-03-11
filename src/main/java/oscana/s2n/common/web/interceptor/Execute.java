package oscana.s2n.common.web.interceptor;

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nablarch.core.beans.BeanUtil;
import nablarch.core.beans.CopyOptions;
import nablarch.core.message.ApplicationException;
import nablarch.core.message.Message;
import nablarch.core.util.StringUtil;
import nablarch.core.validation.ee.ValidatorUtil;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Interceptor;
import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.upload.PartInfo;
import oscana.s2n.common.OscanaActionForm;
import oscana.s2n.common.S2NConstants;
import oscana.s2n.struts.action.ActionMessages;
import oscana.s2n.struts.upload.FormFile;

/**
 * フォームをリクエストスコープに設定する{@link Interceptor}クラス。
 * <p/>
 * 共通で下記の処理を実施する。
 * <ul>
 * <li>アクションのフィールド値をリクエストスコープに設定する。</li>
 * <ul>
 * <li>アクションのpublicフィールドの値、publicのgetterメソッド(getXXX, isXXX)の戻り値をリクエストスコープに設定する。</li>
 * <li>フィールド, getterメソッドの順に実施する。</li>
 * </ul>
 * </ul>
 * アクションにActionFormアノテーションでフォームがフィールド定義されている場合は下記の処理を実施する。
 * <ul>
 * <li>バリデーション有無に関わらず、リクエストパラメータをフォームに設定する。</li>
 * <li>フォームのフィールド値をリクエストスコープに設定する。</li>
 * <ul>
 * <li>フォームのpublicフィールドの値、publicのgetterメソッド(getXXX, isXXX)の戻り値をリクエストスコープに設定する。</li>
 * <li>フィールド, getterメソッドの順に実施する。</li>
 * </ul>
 * <li>アップロードファイルがある場合はフォームのフィールドに設定する。</li>
 * </ul>
 * 上記に加え、アノテーション属性で指定された処理を行う。
 *
 * @author Rai Shuu
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Interceptor(Execute.Impl.class)
public @interface Execute {

    /**
     * バリデーション対象メソッド。デフォルトは空文字（指定なし）。
     * <p/>
     * 単項目バリデーションとは別にバリデーションしたい内容がある場合は、
     * FormまたはActionに設けたバリデーションメソッドを指定する。
     * バリデーションメソッドのシグネチャは、引数はなし、戻り型は{@link oscana.s2n.struts.action.ActionMessages}とする。
     * <p/>
     * 複数指定したい場合はカンマ区切りで指定する。複数指定した場合は前から順にメソッドを呼び出す。
     * 単項目バリデーションのタイミングは「@」で指定する。
     * <p/>
     * バリデーションメソッドを指定し、「@」がない場合は単項目バリデーションを呼ばない。
     */
    String validate() default "";

    /**
     * バリデーションをするか否か。デフォルトはtrue。
     * <p/>
     * trueの場合は{@link #validate()}に指定されたバリデーション内容でバリデーションを行う。
     * {@link #validate()}の指定がない場合は単項目バリデーションのみ行う。
     * <p/>
     * falseの場合はバリデーションしない。
     */
    boolean validator() default true;

    /**
     * アクションが正常終了したときにコンテナからフォームを削除するか否か。デフォルトはfalse
     */
    boolean removeActionForm() default false;

    /**
     * フォームのリセットメソッド名。デフォルトは"reset"
     * <p/>
     * フォームに値を設定する直前に呼び出す。
     * リセットメソッド名が空文字の場合は呼び出さない。
     */
    String reset() default "reset";

    /**
     * {@link #validate()}で複数指定された場合に、バリデーションエラーがあった場合にバリデーションを続行するか否か。デフォルトはtrue
     */
    boolean stopOnValidationError() default true;

    /**
     * {@link Execute}アノテーションのインターセプタクラス。
     *
     * @author kawasima
     * @author Kiyohito Itoh
     */
    @SuppressWarnings("unchecked")
    public static class Impl extends Interceptor.Impl<HttpRequest, HttpResponse, Execute> {

        /** 空の{@link CopyOptions} */
        private static final CopyOptions EMPTY = CopyOptions.options().build();

        /** ブリッジメソッド  */
        private static final Method IS_BRIDGE_METHOD = getIsBridgeMethod();

        /** 合成メソッド  */
        private static final Method IS_SYNTHETIC_METHOD = getIsSyntheticMethod();


        /**
         * フォームを生成し、リクエストスコープに設定する。<br>
         * S2NRouteMthodBinderにForm情報を引き渡すためにrequestにForm情報を登録する。
         * @param request リクエスト
         * @param context 実行コンテキスト
         * @return レスポンス
         */
        @Override
        public HttpResponse handle(final HttpRequest request, final ExecutionContext context) {

            final Execute execute = getInterceptor();
            final Method actionMethod = context.getRequestScopedVar(S2NConstants.REQUEST_SCOPED_KEY_CALL_METHOD);
            final Class<?> actionClazz = actionMethod.getDeclaringClass();
            final Class<? extends Serializable> formClazz = getActionForm(actionClazz);

            try {
                if(formClazz != null) {
                    final Serializable form = Containers.get().getComponent(formClazz);
                    if (StringUtil.hasValue(execute.reset())) {
                        if (!"reset".equals(execute.reset()) || hasMethod(form, execute.reset())) {
                            invoke(form, execute.reset());
                        }
                    }

                    BeanUtil.copy(formClazz, form, request.getParamMap(), EMPTY);
                    copyFormFile(request, form);
                    if (execute.validator()) {
                        Serializable formForValid = BeanUtil.createAndCopy(formClazz, form);
                        BeanUtil.copy(formClazz, formForValid, normalize(request.getParamMap()), EMPTY);
                        copyFormFile(request, formForValid);

                        validate(Containers.get().getComponent(actionClazz), formForValid, form,
                                execute.validate(), execute.stopOnValidationError());
                    }

                    context.setRequestScopedVar(S2NConstants.FORM, form);
                    context.setRequestScopedVar(S2NConstants.FORM_CLASS, formClazz);

                }

                HttpResponse response = getOriginalHandler().handle(request, context);

                if (formClazz != null && execute.removeActionForm()) {
                    Containers.get().removeComponent(formClazz);
                }

                return response;
            } finally {
                setFieldsToRequestScopedVar(actionClazz, context);
                setFieldsToRequestScopedVar(formClazz, context);
            }
        }

        private void setFieldsToRequestScopedVar(Class<?> targetClass, ExecutionContext context) {
            if(targetClass == null) {
                return;
            }
            List<Field> fieldList = new ArrayList<>();
            List<Method> methodList = new ArrayList<>();
            Object target = Containers.get().getComponent(targetClass);
            try {
                Class<?> clazz = targetClass;
                while (clazz != null) {
                    fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
                    methodList.addAll(Arrays.asList(clazz.getMethods()));
                    clazz = clazz.getSuperclass();
                }
                for (Field field : fieldList) {
                    if(!isPublicField(field)) {
                       continue;
                    }
                    field.setAccessible(true);
                    context.setRequestScopedVar(field.getName(), field.get(target));
                }

                for(Method method : methodList) {
                    if (isBridgeMethod(method) || isSyntheticMethod(method)) {
                        continue;
                    }
                   if (method.getName().startsWith("get")) {
                        if (method.getParameterTypes().length != 0
                                || method.getName().equals("getClass")
                                || method.getReturnType() == void.class) {
                            continue;
                        }
                        method.setAccessible(true);
                        context.setRequestScopedVar(decapitalizePropertyName(method.getName()
                                .substring(3)), method.invoke(target));
                    } else if(method.getName().startsWith("is")
                            && method.getReturnType().equals(Boolean.TYPE)
                            && method.getParameterTypes().length == 0) {
                        method.setAccessible(true);
                        context.setRequestScopedVar(decapitalizePropertyName(method.getName().substring(2)), (boolean) method.invoke(target));
                   }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * リクエストパラメータの値をノーマライズする。
         *
         * @param parameters リクエストパラメータ
         * @return ノーマライズしたリクエストパラメータの値
         */
        private Map<String, String[]> normalize(final Map<String, String[]> parameters) {
            final Map<String, String[]> normalizeParameters = new HashMap<String, String[]>();
            for (final Map.Entry<String, String[]> entry : parameters.entrySet()) {
                final String key = entry.getKey();
                String[] value = entry.getValue();

                for (int i = 0; i < value.length; i++) {
                    value[i] = trimWhiteSpace(value[i]);
                }

                normalizeParameters.put(key, value);

            }
            return normalizeParameters;

        }

        /**
         * ホワイトスペースをトリムする。
         *
         * @param value トリム対象の値
         * @return トリム後の値
         */
        private static String trimWhiteSpace(final String value) {
            int start = value.length();
            int end = value.length();
            for (int i = 0; i < value.length(); i++) {
                if (!Character.isWhitespace(value.codePointAt(i))) {
                    start = i;
                    break;
                }
            }

            for (int i = value.length(); i > 0; i--) {
                if (!Character.isWhitespace(value.charAt(i - 1))) {
                    end = i;
                    break;
                }
            }
            final String trimmed = value.substring(start, end);
            return StringUtil.isNullOrEmpty(trimmed) ? null : trimmed;
        }

        private <T> T invoke(Object target, String methodName) {
            try {
                return (T) target.getClass().getMethod(methodName).invoke(target);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private boolean hasMethod(Object target, String methodName) {
            try {
                target.getClass().getMethod(methodName);
                return true;
            } catch (NoSuchMethodException e) {
                return false;
            }
        }

        private Class<? extends Serializable> getActionForm(Class<?> actionClazz) {
            for (Field field : actionClazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(OscanaActionForm.class)) {
                    return (Class<? extends Serializable>) field.getType();
                }
            }
            return null;
        }

        /**
         * 全てのバリデーションを行う。
         */
        private void validate(Object action, Serializable normalizeform, Serializable originalform, String validate,
                boolean stopOnValidationError) {
            List<Message> messages = new ArrayList<Message>();

            List<String> validateList = new ArrayList<String>();
            String[] validates = validate.trim().equals("") ? new String[] {} : validate.split(",");
            if(!Arrays.asList(validates).contains("@")) {
                validateList.add("@");
            }
            validateList.addAll(Arrays.asList(validates));

            for (String method : validateList) {
                if (!messages.isEmpty() && stopOnValidationError) {
                    break;
                }
                if ("@".equals(method)) {
                    messages.addAll(validate(normalizeform));
                } else if (hasMethod(originalform, method)) {
                    messages.addAll(((ActionMessages) invoke(originalform, method)).getMessages());
                } else if (hasMethod(action, method)) {
                    messages.addAll(((ActionMessages) invoke(action, method)).getMessages());
                } else {
                    throw new IllegalStateException(
                            String.format("method not found. method=[%s] form=[%s] action=[%s]",
                                    method, originalform.getClass().getName(), action.getClass().getName()));
                }
            }
            if (!messages.isEmpty()) {

                throw new ApplicationException(messages);
            }
        }

        /**
         * 単項目バリデーションを行う。
         */
        private List<Message> validate(Object bean) {
            try {
                ValidatorUtil.validate(bean);
                return Collections.emptyList();
            } catch (ApplicationException e) {
                return e.getMessages();
            }
        }

        /**
         * アップロードされたファイルをフォームのFormFileフィールドに設定する。
         */
        private void copyFormFile(HttpRequest request, Serializable form) {
            Map<String, List<PartInfo>> multipart = request.getMultipart();
            for (Map.Entry<String, List<PartInfo>> entry : multipart.entrySet()) {
                List<PartInfo> partInfos = entry.getValue();
                Object propertyValue;
                if (partInfos.size() == 1) {
                    propertyValue = toFormFile(partInfos.get(0));
                } else {
                    propertyValue = partInfos.stream().map(partInfo -> toFormFile(partInfo))
                            .collect(Collectors.toList()).toArray(new PartInfo[partInfos.size()]);
                }
                BeanUtil.setProperty(form, entry.getKey(), propertyValue);
            }
        }

        private FormFile toFormFile(PartInfo partInfo) {
            FormFile formFile = new FormFile();
            formFile.setContentType(partInfo.getContentType());
            formFile.setFileName(partInfo.getFileName());
            formFile.setSaveFile(partInfo.getSavedFile());
            return formFile;
        }

      /**
       * フィールド名を作成する。
       * @param name パラメータ名
       * @return フィールド名
       */
      private static String decapitalizePropertyName(String name) {
        if (StringUtil.isNullOrEmpty(name)) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))
                && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
      }

      /**
       * パブリックフィールドかどうか返却する。
       *
       * @param field フィールド
       * @return パブリックフィールドかどうか
       */
      private static boolean isPublicField(Field field) {
          int mod = field.getModifiers();
          return Modifier.isPublic(mod);
      }

        /**
         * 合成メソッドかどうかを返す。
         *
         * @param method
         * @return 合成メソッドかどうか
         */
      private static boolean isSyntheticMethod(final Method method) {
            if (IS_SYNTHETIC_METHOD == null) {
                return false;
            }
            try {
                return ((Boolean) IS_SYNTHETIC_METHOD.invoke(method, (Object[]) null)).booleanValue();
            } catch (Exception e) {
                throw new RuntimeException(IS_SYNTHETIC_METHOD.getDeclaringClass().getName(),
                        e);

            }
        }

        /**
         * ブリッジメソッドかどうか返す。
         * @param method メソッド
         * @return ブリッジメソッドかどうか
         */
      private static boolean isBridgeMethod(final Method method) {
            if (IS_BRIDGE_METHOD == null) {
                return false;
            }
            try {
                return ((Boolean) IS_BRIDGE_METHOD.invoke(method, (Object[]) null)).booleanValue();
            } catch (Exception e) {
                throw new RuntimeException(IS_BRIDGE_METHOD.getDeclaringClass().getName(),
                        e);

            }
        }

        /**
         * ブリッジメソッドを返す。
         * @return ブリッジメソッド
         */
        private static Method getIsBridgeMethod() {
            try {
                return Method.class.getMethod("isBridge", (Class<?>) null);
            } catch (final NoSuchMethodException e) {
                return null;
            }
        }

        /**
         * 合成メソッドを返す。
         * @return 合成メソッド
         */
        private static Method getIsSyntheticMethod() {
            try {
                return Method.class.getMethod("isSynthetic", (Class<?>) null);
            } catch (final NoSuchMethodException e) {
                return null;
            }
        }

    }
}
