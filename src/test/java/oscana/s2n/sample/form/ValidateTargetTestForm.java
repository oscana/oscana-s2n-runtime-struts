package oscana.s2n.sample.form;

import java.io.Serializable;

import nablarch.fw.dicontainer.web.SessionScoped;
import oscana.s2n.common.web.interceptor.Execute;
import oscana.s2n.validation.ByteLength;
import oscana.s2n.validation.FieldName;
import oscana.s2n.validation.Length;
import oscana.s2n.validation.Pattern;
import oscana.s2n.validation.Required;

/**
 *　{@link Execute}のテスト用フォーム。
 */
@SessionScoped
public class ValidateTargetTestForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private String requiredField;

    private String lengthField;

    private String byteLengthField;

    private String patternField;

    /**
     * @return requiredField
     */
    @Required(target="targetMethod1, targetMethod2")
    @FieldName(value = "targetTestForRequiredField")
    public String getRequiredField() {
        return requiredField;
    }

    /**
     * @param requiredField セットする requiredField
     */
    public void setRequiredField(String requiredField) {
        this.requiredField = requiredField;
    }

    /**
     * @return lengthField
     */
    @Length(min=0,max=5,target=" targetMethod1")
    @FieldName(value = "targetTestForLengthField")
    public String getLengthField() {
        return lengthField;
    }

    /**
     * @param lengthField セットする lengthField
     */
    public void setLengthField(String lengthField) {
        this.lengthField = lengthField;
    }

    /**
     * @return byteLengthField
     */
    @ByteLength(min=6,max=7,target="targetMethod2 ")
    @FieldName(value = "targetTestForByteLengthField")
    public String getByteLengthField() {
        return byteLengthField;
    }

    /**
     * @param byteLengthField セットする byteLengthField
     */
    public void setByteLengthField(String byteLengthField) {
        this.byteLengthField = byteLengthField;
    }

    /**
     * @return patternField
     */
    @Pattern(regexp="0|1",target=" targetMethod1")
    @FieldName(value = "targetTestForPatternField")
    public String getPatternField() {
        return patternField;
    }

    /**
     * @param patternField セットする patternField
     */
    public void setPatternField(String patternField) {
        this.patternField = patternField;
    }

    public void reset() {
    }
}
