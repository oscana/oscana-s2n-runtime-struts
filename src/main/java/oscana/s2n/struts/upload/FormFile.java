/*
 * 取り込み元
 *    ライブラリ名：     struts1
 *    クラス名：         org.apache.struts.upload.FormFile
 *    ソースリポジトリ： https://github.com/apache/struts1/blob/trunk/core/src/main/java/org/apache/struts/upload/FormFile.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package oscana.s2n.struts.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;

/**
 * This interface represents a file that has been uploaded by a client. It
 * is the only interface or class in upload package which is typically
 * referenced directly by a Struts application. <br>
 *
 * <br>
 * 移植内容の変更点：<br>
 * <br>
 * ・インターフェースとデフォルトの実装クラス{@link org.apache.struts.upload.S2MultipartRequestHandler.S2FormFile}を統合する。<br>
 * ・destoryメソッドを空実装に修正する(テンポラリファイルの削除はNablarchが実施するので互換ライブラリ側では実施しない。)
 *
 * @see org.apache.struts.upload.FormFile
 */
public class FormFile implements Serializable {

    private String fileName;
    private String contentType;
    private File saveFile;

    /**
     * @return contentType
     */
    public String getContentType() {
        return this.contentType;
    }

    /**
     * @param contentType セットする contentType
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * <p> Returns the size of this file. </p>
     *
     * @return The size of the file, in bytes.
     */
    public int getFileSize() {
        if (saveFile == null) {
            return 0;
        }
        return (int) this.saveFile.length();
    }

    /**
     * <p> Returns the file name of this file. This is the base name of the
     * file, as supplied by the user when the file was uploaded. </p>
     *
     * @return The base file name.
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * <p> Sets the file name of this file. </p>
     *
     * @param fileName The base file name.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * <p> Returns the data for the entire file as byte array. Care is needed
     * when using this method, since a large upload could easily exhaust
     * available memory. The preferred method for accessing the file data is
     * {@link #getInputStream() getInputStream}. </p>
     *
     * @return The file data as a byte array.
     * @throws FileNotFoundException if the uploaded file is not found. Some
     *              implementations may not deal with files and/or throw
     *              this exception.
     * @throws IOException if an error occurred while reading the
     *                               file.
     */
    public byte[] getFileData() throws FileNotFoundException, IOException {
        return Files.readAllBytes(this.saveFile.toPath());
    }

    /**
     * <p> Returns an input stream for this file. The caller must close the
     * stream when it is no longer needed. </p>
     * @return InputStream
     * @throws FileNotFoundException if the uploaded file is not found. Some
     *              implementations may not deal with files and/or throw
     *              this exception.
     */
    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(this.saveFile);
    }

    /**
     * @return saveFile
     */
    public File getSaveFile() {
        return saveFile;
    }

    /**
     * @param saveFile セットする saveFile
     */
    public void setSaveFile(File saveFile) {
        this.saveFile = saveFile;
    }

    /**
     * <p> Destroys all content for the uploaded file, including any
     * underlying data files. </p>
     */
    public void destroy() {
    }

}