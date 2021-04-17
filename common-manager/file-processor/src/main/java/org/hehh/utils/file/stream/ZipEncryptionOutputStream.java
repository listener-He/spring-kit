package org.hehh.utils.file.stream;

import net.lingala.zip4j.headers.HeaderSignature;
import net.lingala.zip4j.io.outputstream.CountingOutputStream;
import net.lingala.zip4j.io.outputstream.SplitOutputStream;
import net.lingala.zip4j.model.ZipModel;
import net.lingala.zip4j.util.RawIO;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipOutputStream;

/**
 * @author: HeHui
 * @date: 2020-12-23 15:32
 * @description: zip加解密输出流
 */
public class ZipEncryptionOutputStream extends ZipOutputStream {


    private final char[] password;

    private final ZipModel model;


    private RawIO rawIO = new RawIO();


    /**
     * Creates a new ZIP output stream.
     *
     * <p>The UTF-8 {@link Charset charset} is used
     * to encode the entry names and comments.
     *
     * @param out      the actual output stream
     * @param password
     */
    public ZipEncryptionOutputStream(OutputStream out, char[] password) {
        this(out, StandardCharsets.UTF_8, password, new ZipModel());
    }


    /**
     * Creates a new ZIP output stream.
     *
     * @param out      the actual output stream
     * @param charset  the {@linkplain Charset charset}
     *                 to be used to encode the entry names and comments
     * @param password
     * @param model
     *
     * @since 1.7
     */
    public ZipEncryptionOutputStream(OutputStream out, Charset charset, char[] password, ZipModel model) {
        super(out, charset);
        this.password = password;
        this.model = initializeZipModel(model, out);


        writeSplitZipHeaderIfApplicable();
    }


    /**
     * 初始化压缩模型
     *
     * @param zipModel     邮政编码模型
     * @param outputStream 输出流
     *
     * @return {@link ZipModel}
     */
    private ZipModel initializeZipModel(ZipModel zipModel, OutputStream outputStream) {
        if (zipModel == null) {
            zipModel = new ZipModel();
        }
        if (outputStream instanceof CountingOutputStream) {
            zipModel.setSplitArchive(true);
            zipModel.setSplitLength(((CountingOutputStream) outputStream).getSplitLength());
        }
        return zipModel;
    }


    /**
     * 写把拉链头如果适用
     *
     * @throws IOException ioexception
     */
    private void writeSplitZipHeaderIfApplicable()  {
        if (!isSplitZipFile()) {
            return;
        }
        try {
            rawIO.writeIntLittleEndian(out, (int) HeaderSignature.SPLIT_ZIP.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 分裂的zip文件
     *
     * @return boolean
     */
    public boolean isSplitZipFile() {
        return out instanceof SplitOutputStream
            && ((SplitOutputStream)out).isSplitZipFile();
    }
}
