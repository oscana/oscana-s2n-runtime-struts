package oscana.s2n.validation;

import java.util.ArrayList;

import nablarch.core.ThreadContext;
import nablarch.core.util.StringUtil;
import oscana.s2n.common.S2NConstants;

/**
 * 特定のAction時にのみバリデーションを実行させる機能をもつバリデータ向けの共通処理。
 *
 * @author Rai Shuu
 */
public class ValidateTarget {

    /** 対象メソッドのリスト */
    private final java.util.List<String> targets = new ArrayList<>();

    /**
     * Actionメソッド名がバリデーションの対象であるかをチェックする。
     * @return バリデーション対象の場合はtrue
     */
    protected boolean isTarget() {
        String methodName = (String) ThreadContext.getObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME);
        return targets.isEmpty() || targets.contains(methodName);
    }

    /**
     * 対象メソッドを設定する。
     * @param targetsString 対象メソッド
     */
    protected void setTargets(String targetsString) {
        if (StringUtil.isNullOrEmpty(targetsString)) {
            return;
        }
        for (String target : targetsString.split(",")) {
            targets.add(target.trim());
        }
    }
}
