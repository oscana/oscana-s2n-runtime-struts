package oscana.s2n.sample.form;

import java.io.Serializable;
import java.util.Map;

import nablarch.core.validation.PropertyName;
import nablarch.fw.dicontainer.web.SessionScoped;
import oscana.s2n.common.web.interceptor.Execute;
import oscana.s2n.struts.action.ActionMessage;
import oscana.s2n.struts.action.ActionMessages;
import oscana.s2n.struts.upload.FormFile;
import oscana.s2n.validation.FieldName;
import oscana.s2n.validation.ParseInt;
import oscana.s2n.validation.Required;

/**
 * {@link Execute}のテスト用フォーム。
 */
@SessionScoped
public class ExecuteTestForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @ParseInt
    private String userId; // @Required

    private String name;

    private String resetVal = "1";

    private String formAttr;

    private String spaceVal;

    private String trimVal;

    private FormFile[] formFiles;

    @Required(target="notRemove, doValidNotStopWithErr, defaultExecute")
    private String requiredVal;

    public ExecuteTestForm() {
    }

    public ExecuteTestForm(Map<String, Object> params) {
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

    public void reset() {
        this.resetVal = "0";
    }

    public void testForReset() {
        this.resetVal = "test";
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
    @FieldName(value = "targetTestForRequiredField")
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

    /**
     * formで定義した値を設定するバリデーションメソッド
     */
    public ActionMessages validateFormAndSetValue() {
        ActionMessages errors = new ActionMessages();
        userId = "setByValid";
        return errors;
    }

    public boolean isAbc() {
      return true;
    }

    public boolean isAAA() {
        return true;
      }

    public boolean is() {
        return true;
    }

    public boolean isBbb(boolean param) {
        return true;
    }

    public String getCcc(String param) {
        return "ccc";
    }

    /**
     * @return spaceVal
     */
    public String getSpaceVal() {
        return spaceVal;
    }

    /**
     * @param spaceVal セットする spaceVal
     */
    public void setSpaceVal(String spaceVal) {
        this.spaceVal = spaceVal;
    }

    /**
     * @return trimVal
     */
    public String getTrimVal() {
        return trimVal;
    }

    /**
     * @param trimVal セットする trimVal
     */
    public void setTrimVal(String trimVal) {
        this.trimVal = trimVal;
    }

    /**
     * @return formFiles
     */
    public FormFile[] getFormFiles() {
        return formFiles;
    }

    /**
     * @param formFiles セットする formFiles
     */
    public void setFormFiles(FormFile[] formFiles) {
        this.formFiles = formFiles;
    }
}
