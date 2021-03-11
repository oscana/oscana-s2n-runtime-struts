package oscana.s2n.sample.form;

import java.io.Serializable;
import java.util.Map;

import nablarch.core.validation.PropertyName;
import nablarch.fw.dicontainer.web.SessionScoped;
import oscana.s2n.common.web.interceptor.Execute;
import oscana.s2n.struts.action.ActionMessage;
import oscana.s2n.struts.action.ActionMessages;
import oscana.s2n.validation.Required;

/**
 *　{@link Execute}のテスト用フォーム。
 */
@SessionScoped
public class ExecuteErrTestForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String userId; // @Required

    private String name;

    private String resetVal;

    private String formAttr;

    @Required(target="notRemove, doValidNotStopWithErr, defaultExecute")
    private String requiredVal;

    public ExecuteErrTestForm() {
    }

    public ExecuteErrTestForm(Map<String, Object> params) {
        userId = (String) params.get("userId");
    }

    public String getUserId() {
        return userId;
    }

    @PropertyName("ユーザID")
    @Domain(DomainType.USER_ID)
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return resetVal
     */
    public String getResetVal() {
        return resetVal;
    }

    /**
     * @param resetVal セットする resetVal
     */
    public void setResetVal(String resetVal) {
        this.resetVal = resetVal;
    }

    public void reset1() {
        this.resetVal = "0";
    }

    /**
     * @return formAttr
     */
    public String getFormAttr() {
        return formAttr;
    }

    /**
     * @param formAttr セットする formAttr
     */
    public void setFormAttr(String formAttr) {
        this.formAttr = formAttr;
    }

    /**
     * @return requiredVal
     */
    public String getRequiredVal() {
        return requiredVal;
    }

    /**
     * @param requiredVal セットする requiredVal
     */
    public void setRequiredVal(String requiredVal) {
        this.requiredVal = requiredVal;
    }

    /**
     * formで定義したバリデーションメソッド（S2NValidationStrategyから呼び出し）
     */
    public ActionMessages validateForm() {
        ActionMessages errors = new ActionMessages();
        errors.add("validateForm", new ActionMessage("error.S2NValidForm"));
        return errors;
    }

}
