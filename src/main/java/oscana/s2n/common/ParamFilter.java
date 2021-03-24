package oscana.s2n.common;

import java.util.Arrays;
import java.util.List;

/**
 * UniversalDao呼び出し時のパラメータを変換するためのユーティリティクラス。<br>
 * <br>
 *   本クラスは自動生成されたソースコード中に埋め込まれ、変換元のFWで使われていた表現を、Nablarchの表現に変換する用途で用いる。
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ParamFilter {

    /** SQLファイル名のサフィックス*/
    private static final String SQL_FILE_SUFFIX = ".sql";

    /**
     * INPUTをリストに変換する。<br>
     * <br>
     *  ・引数部分は変換ツールにより、変換前アプリに記述されているものがそのまま入る。<br>
     *  ・変換前アプリの様々な書き方に対応できるよう、オーバーロードしたメソッドを用意する。<br>
     *  ・本メソッドは引数に可変長型引数が渡された場合の処理である。<br>
     *
     * @param entities 可変長引数
     * @param <T> パラメータ型
     * @return entitiesをリストに変換するもの
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(T... entities) {
        return Arrays.asList(entities);
    }

    /**
     * INPUTをリストに変換する。<br>
     * <br>
     *    ・引数部分は変換ツールにより、変換前アプリに記述されているものがそのまま入る。<br>
     *    ・変換前アプリの様々な書き方に対応できるよう、オーバーロードしたメソッドを用意する。<br>
     *    ・本メソッドは引数にリストが渡された場合の処理である。<br>
     *
     * @param entityList リスト
     * @param <T> パラメータ型
     * @return 引数で与えられたリスト
     */
    public static <T> List<T> toList(List<T> entityList) {
        return entityList;
    }

    /**
     * 変換元FWのSQLファイル名をNablarchのSQLキーに変換する。
     * @param sqlFileName 変換元FWのSQLファイル名
     * @return NablarchのSQLキー
     */
    public static String sqlFileNameToKey(String sqlFileName) {

        if ((sqlFileName != null) && (sqlFileName.endsWith(SQL_FILE_SUFFIX))) {
            return sqlFileName.substring(0, sqlFileName.length() - SQL_FILE_SUFFIX.length());
        }
        return sqlFileName;
    }
}
