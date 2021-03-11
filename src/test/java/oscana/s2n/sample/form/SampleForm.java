package oscana.s2n.sample.form;

import java.io.Serializable;

import nablarch.fw.dicontainer.web.SessionScoped;
import oscana.s2n.validation.Required;

/**
 * テストフォーム
 *
 */
@SessionScoped
public class SampleForm implements Serializable {

    /**
     * serialVersionUID
     */
    public static final long serialVersionUID = 1L;

    @Required
    public String name;

    public String age;

    /**
     * 名前を取得する
     *
     * @return name 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 名前を設定する
     *
     * @param name 名前
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 年齢を取得する
     *
     * @return age 年齢
     */
    public String getAge() {
        return age;
    }

    /**
     * 年齢を設定する
     *
     * @param age 年齢
     */
    public void setAge(String age) {
        this.age = age;
    }

}
