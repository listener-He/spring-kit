package org.hehh.utils.file;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.LocalFileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.hehh.utils.file.pojo.InputStreamFile;
import org.hehh.utils.file.pojo.OutputStreamFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author: HeHui
 * @date: 2020-12-22 19:31
 * @description: zip工具类
 */
public class ZipUtil extends cn.hutool.core.util.ZipUtil {

    /**
     * Default buff byte size
     */
    private static final int DEFAULT_BUFF_SIZE = 1024;

    /**
     * Default basedir value
     */
    private static final boolean DEFAULT_DIR = false;

    /**
     * 初始化文件
     *
     * @param file 文件
     */
    private static void initFile(File file) {
        assert FileUtil.isFile(file);
        if (!FileUtil.exist(file)) {
            FileUtil.touch(file);
        }
    }

    /**
     * 压缩
     *
     * @param zipFile zip文件
     * @param files   文件
     *
     * @return boolean
     */
    public static boolean zip(String zipFile, String... files) {
        return zip(new File(zipFile), Arrays.stream(files).map(v -> new File(v)).toArray(File[]::new));
    }

    /**
     * 压缩
     *
     * @param zipFile zip文件
     * @param inputs  输入
     *
     * @return boolean
     */
    public static boolean zip(String zipFile, InputStreamFile... inputs) {
        return zip(new File(zipFile), inputs);
    }


    /**
     * 压缩
     *
     * @param zipFile zip文件
     * @param files   文件
     *
     * @return boolean
     */
    public static boolean zip(String zipFile, File... files) {
        return zip(new File(zipFile), files);
    }

    /**
     * 压缩
     *
     * @param zipFile zip文件
     * @param files   文件
     *
     * @return boolean
     */
    public static boolean zip(File zipFile, File... files) {
        return zip(zipFile, Arrays.stream(files).map(f -> new InputStreamFile(FileUtil.getInputStream(f), f.getName())).toArray(InputStreamFile[]::new));
    }


    /**
     * 压缩
     *
     * @param zipFile zip文件
     * @param inputs  输入
     *
     * @return boolean
     */
    public static boolean zip(File zipFile, InputStreamFile... inputs) {
        try {
            initFile(zipFile);
            return zip(new FileOutputStream(zipFile), inputs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 压缩文件
     *
     * @param output 输出
     * @param inputs 输入
     *
     * @return boolean
     */
    public static boolean zip(OutputStream output, InputStreamFile... inputs) {
        ZipOutputStream out = new ZipOutputStream(output);
        byte[] buffer = new byte[DEFAULT_BUFF_SIZE];
        try {
            for (int i = 0; i < inputs.length; i++) {
                try {

                    /**
                     *  添加文件元素开始
                     */
                    out.putNextEntry(new ZipEntry(inputs[i].getName()));

                    int len;
                    /**
                     * 读入需要下载的文件的内容，打包到zip文件
                     */
                    while ((len = inputs[i].getStream().read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    /**
                     *  添加文件元素结束
                     */
                    out.closeEntry();
                    /**
                     *  输入流关闭
                     */
                    inputs[i].getStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        return true;
    }


    /**
     * 压缩加密
     * 压缩
     *
     * @param zipFile  zip文件
     * @param files    文件
     * @param password 密码
     *
     * @return boolean
     */
    public static boolean zip(String zipFile, String password, String... files) {
        return zip(new File(zipFile), password, Arrays.stream(files).map(v -> new File(v)).toArray(File[]::new));
    }

    /**
     * 压缩加密
     * 压缩
     *
     * @param zipFile  zip文件
     * @param inputs   输入
     * @param password 密码
     *
     * @return boolean
     */
    public static boolean zip(String zipFile, String password, InputStreamFile... inputs) {
        return zip(new File(zipFile), password, inputs);
    }


    /**
     * 压缩加密
     * 压缩
     *
     * @param zipFile  zip文件
     * @param files    文件
     * @param password 密码
     *
     * @return boolean
     */
    public static boolean zip(String zipFile, String password, File... files) {
        return zip(new File(zipFile), password, files);
    }

    /**
     * 压缩加密
     * 压缩
     *
     * @param zipFile  zip文件
     * @param files    文件
     * @param password 密码
     *
     * @return boolean
     */
    public static boolean zip(File zipFile, String password, File... files) {
        return zip(zipFile, password, Arrays.stream(files).map(f -> new InputStreamFile(FileUtil.getInputStream(f), f.getName())).toArray(InputStreamFile[]::new));
    }


    /**
     * 压缩加密
     * 压缩
     *
     * @param zipFile  zip文件
     * @param inputs   输入
     * @param password 密码
     *
     * @return boolean
     */
    public static boolean zip(File zipFile, String password, InputStreamFile... inputs) {
        try {
            initFile(zipFile);
            return zip(new FileOutputStream(zipFile), password, inputs);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 压缩zip标准加密
     *
     * @param output   输出
     * @param password 密码
     * @param inputs   输入
     *
     * @return boolean
     */
    public static boolean zip(OutputStream output, String password, InputStreamFile... inputs) {
        return zip(output, password, EncryptionMethod.ZIP_STANDARD, inputs);
    }


    /**
     * 压缩加密
     *
     * @param output           输出
     * @param password         密码
     * @param inputs           输入
     * @param encryptionMethod 加密方法
     *
     * @return boolean
     */
    public static boolean zip(OutputStream output, String password, EncryptionMethod encryptionMethod, InputStreamFile... inputs) {
        assert output != null;
        assert StrUtil.isNotBlank(password);

        net.lingala.zip4j.io.outputstream.ZipOutputStream out = null;
        try {
            out = new net.lingala.zip4j.io.outputstream.ZipOutputStream(output, password.toCharArray());
            byte[] buffer = new byte[DEFAULT_BUFF_SIZE];

            for (int i = 0; i < inputs.length; i++) {
                try {

                    /**
                     *  添加文件元素开始
                     */
                    ZipParameters entry = new ZipParameters();
                    /**
                     * 设置压缩方法
                     */
                    entry.setCompressionMethod(CompressionMethod.DEFLATE);
                    /**
                     * 设置压缩级别
                     */
                    entry.setCompressionLevel(CompressionLevel.NORMAL);
                    /**
                     * 设置压缩文件是否加密
                     */
                    entry.setEncryptFiles(true);
                    /**
                     * 设置aes加密强度
                     */
                    entry.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
                    /**
                     * 设置加密方法 这里的配置要注意，配置不对将在LINUX下无法解压 AES -->  ZIP_STANDARD
                     */
                    entry.setEncryptionMethod(encryptionMethod);
                    entry.setFileNameInZip(inputs[i].getName());
                    out.putNextEntry(entry);

                    int len;
                    /**
                     * 读入需要下载的文件的内容，打包到zip文件
                     */
                    while ((len = inputs[i].getStream().read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    /**
                     *  添加文件元素结束
                     */
                    out.closeEntry();
                    /**
                     *  输入流关闭
                     */
                    inputs[i].getStream().close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return true;
    }


    /**
     * 解压
     *
     * @param input 输入
     *
     * @return {@link List<OutputStreamFile>}
     */
    public static List<OutputStreamFile> unzip(InputStream input) {
        ZipInputStream zin = new ZipInputStream(input);
        List<OutputStreamFile> outputStreamFiles = new ArrayList<>();
        try {
            while (true) {
                ZipEntry entry = null;
                try {
                    /**
                     *  获取下一个要解压的文件
                     */
                    entry = zin.getNextEntry();
                    if (entry == null) {
                        break;
                    } else {
                        OutputStreamFile streamFile = new OutputStreamFile(new ByteArrayOutputStream(), entry.getName());
                        unZipWrite(streamFile.getStream(), zin);
                        streamFile.setDirectory(entry.isDirectory());
                        streamFile.setSize(entry.getSize());

                        if (entry.getLastAccessTime() != null) {
                            streamFile.setLastAccessTime(entry.getLastAccessTime().toMillis());
                        }
                        if (entry.getCreationTime() != null) {
                            streamFile.setCreationTime(entry.getCreationTime().toMillis());
                        }
                        if (entry.getLastModifiedTime() != null) {
                            streamFile.setLastModifiedTime(entry.getLastModifiedTime().toMillis());
                        }
                        outputStreamFiles.add(streamFile);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (entry != null) {
                        /**
                         *  关闭已读取文件
                         */
                        try {
                            zin.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return outputStreamFiles;
    }

    /**
     * 解压缩
     *
     * @param zip             压缩包
     * @param password        密码
     * @param unpackDirectory 解压缩目录
     *
     * @return boolean
     */
    public static boolean unzip(String zip, String password, String unpackDirectory) {
        return unzip(new File(zip), password, unpackDirectory);
    }

    /**
     * 解压缩
     *
     * @param zip             压缩包
     * @param password        密码
     * @param unpackDirectory 解压缩目录
     *
     * @return boolean
     */
    public static boolean unzip(File zip, String password, String unpackDirectory) {
        assert FileUtil.exist(zip);
        assert FileUtil.isDirectory(unpackDirectory);
        FileUtil.mkdir(unpackDirectory);
        ZipFile zipFile = new ZipFile(zip, password.toCharArray());
        try {
            zipFile.extractAll(unpackDirectory);
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 解压缩
     *
     * @param zip      压缩包
     * @param password 密码
     *
     * @return {@link List<OutputStreamFile>}
     */
    public static List<OutputStreamFile> unzip(File zip, String password) {
        ZipFile zipFile = new ZipFile(zip, password.toCharArray());
        try {
            return zipFile.getFileHeaders().stream().map(v -> {
                net.lingala.zip4j.io.inputstream.ZipInputStream inputStream = null;
                try {
                    inputStream = zipFile.getInputStream(v);

                    /**
                     *  组装返回类
                     */
                    OutputStreamFile streamFile = new OutputStreamFile(new ByteArrayOutputStream(), v.getFileName());
                    streamFile.setDirectory(v.isDirectory());
                    streamFile.setSize(v.getCompressedSize());
                    streamFile.setLastModifiedTime(v.getLastModifiedTime());

                    unZipWrite(streamFile.getStream(), inputStream);

                    return streamFile;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }


    /**
     * 解压缩
     *
     * @param input           输入
     * @param password        密码
     * @param unpackDirectory 解压缩目录
     *
     * @return {@link List<File>}
     */
    public static List<File> unzip(InputStream input, String password, String unpackDirectory) {
        List<OutputStreamFile> streamFiles = unzip(input, password);
        if (CollUtil.isNotEmpty(streamFiles)) {
            return streamFiles.stream().filter(v -> v.getSize() > 0 && v.getStream() != null && v.getStream().size() > 0).map(f -> {
                File file = new File(unpackDirectory, f.getName());
                FileUtil.touch(file);
                FileUtil.writeFromStream(new ByteArrayInputStream(f.getStream().toByteArray()), file);
                return file;
            }).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 解压
     *
     * @param input 输入
     *
     * @return {@link List<OutputStreamFile>}
     */
    public static List<OutputStreamFile> unzip(InputStream input, String password) {
        net.lingala.zip4j.io.inputstream.ZipInputStream zin = new net.lingala.zip4j.io.inputstream.ZipInputStream(input, password.toCharArray());

        List<OutputStreamFile> outputStreamFiles = new ArrayList<>();
        try {
            while (true) {
                LocalFileHeader entry = null;
                try {
                    /**
                     *  获取下一个要解压的文件
                     */
                    try {
                        entry = zin.getNextEntry();
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }

                    if (entry == null) {
                        break;
                    } else {
                        OutputStreamFile streamFile = new OutputStreamFile(new ByteArrayOutputStream(), entry.getFileName());
                        unZipWrite(streamFile.getStream(), zin);
                        streamFile.setDirectory(entry.isDirectory());
                        streamFile.setSize(entry.getCompressedSize());
                        streamFile.setLastModifiedTime(entry.getLastModifiedTime());
                        outputStreamFiles.add(streamFile);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (entry != null) {
                        /**
                         *  关闭已读取文件
                         */
//                        try {
//                            zin.getNextEntry();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zin.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return outputStreamFiles;
    }

    /**
     * 解压读取
     *
     * @param output 输出
     * @param zis    子
     *
     * @throws Exception 异常
     */
    private static void unZipWrite(ByteArrayOutputStream output, InputStream zis) throws Exception {
        int len;
        byte[] buff = new byte[DEFAULT_BUFF_SIZE];
        try {
            while ((len = zis.read(buff, 0, DEFAULT_BUFF_SIZE)) != -1) {
                output.write(buff, 0, len);
            }
        } finally {
            output.close();
        }
    }

}
