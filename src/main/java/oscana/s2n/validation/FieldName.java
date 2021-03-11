package oscana.s2n.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * エラーメッセージに挿入する項目名をメッセージプロパティから検索するアノテーション。
 * @author Ko Ho
 *
 */
@Target({ FIELD, METHOD })
@Retention(RUNTIME)
public @interface FieldName {

    String value();

}
