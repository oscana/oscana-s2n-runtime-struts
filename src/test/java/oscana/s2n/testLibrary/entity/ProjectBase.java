package oscana.s2n.testLibrary.entity;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;


/**
 * プロジェクト
 *
 */
@MappedSuperclass
@Generated(value = {"S2JDBC-Gen 2.4.41", "org.seasar.extension.jdbc.gen.internal.model.EntityModelFactoryImpl"}, date = "2010/08/02 17:54:25")
@Entity
@Table(name = "PROJECT")
public class ProjectBase extends AbstractEntity implements Serializable {

    /** シリアル・バージョンID */
    private static final long serialVersionUID = 1L;

    /** プロジェクトID */
    public String projectId;

    /** プロジェクト名 */
    public String projectNm;

    /** プロジェクト種別 */
    public String projectType;


    /**
     * プロジェクトIDを取得する
     *
     * @return projectId プロジェクトID
     **/
    @Id
    @Column(name = "PROJECT_ID", length = 128, nullable = false, unique = true)
    public String getProjectId() {
        return projectId;
    }

    /**
     * プロジェクトIDを設定する
     *
     * @param projectId プロジェクトID
     **/
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * プロジェクト名を取得する
     *
     * @return projectNm プロジェクト名
     **/
    @Column(name = "PROJECT_NAME", length = 256, nullable = true, unique = false)
    public String getProjectNm() {
        return projectNm;
    }

    /**
     * プロジェクト名を設定する
     *
     * @param projectNm プロジェクト名
     **/
    public void setProjectNm(String projectNm) {
        this.projectNm = projectNm;
    }

    /**
     * プロジェクト種別を取得する
     *
     * @return projectType プロジェクト種別
     **/
    @Column(name = "PROJECT_TYPE", length = 128, nullable = true, unique = false)
    public String getProjectType() {
        return projectType;
    }

    /**
     * プロジェクト種別を設定する
     *
     * @param projectType プロジェクト種別
     **/
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

}