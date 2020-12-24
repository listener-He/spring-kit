package org.hehh.utils.file;

import cn.hutool.core.collection.CollectionUtil;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.hehh.utils.file.pojo.InputStreamFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-12-22 19:46
 * @description: csv工具类
 */
public class CsvUtil {

    public static final String CSV_DELIMITER = ",";
    private static final String NEW_LINE = System.lineSeparator();


    /**
     * 将多行记录，写入到输出流后面
     *
     * @param data   数据
     * @param output 输出
     */
    public static void appendToOutputForList(List<List<String>> data, OutputStream output) {
        assert output != null;
        try {
            write(data.parallelStream()
                .filter(row -> !CollectionUtil.isEmpty(row))
                .map(row -> row.stream().collect(Collectors.joining(CSV_DELIMITER)))
                .collect(Collectors.toList()), new BufferedWriter(new OutputStreamWriter(output)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将多行记录，写入到流后面
     *
     * @param data   多行记录
     * @param output 输出流
     */
    public static void appendToOutputForArray(List<String[]> data, OutputStream output) {
        assert output != null;
        try {
            write(data.parallelStream()
                .filter(row -> row.length != 0)
                .map(row -> String.join(CSV_DELIMITER, row))
                .collect(Collectors.toList()), new BufferedWriter(new OutputStreamWriter(output)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 将多行记录，写入到文件后面
     *
     * @param data 多行记录
     * @param file 被写入文件
     */
    public static void appendToFileForList(List<List<String>> data, File file) {
        assert file != null;
        try {
            write(data.parallelStream()
                .filter(row -> !CollectionUtil.isEmpty(row))
                .map(row -> row.stream().collect(Collectors.joining(CSV_DELIMITER)))
                .collect(Collectors.toList()), new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将多行记录，写入到文件后面
     *
     * @param data 多行记录
     * @param file 被写入文件
     */
    public static void appendToFileForArray(List<String[]> data, File file) {
        assert file != null;
        try {
            write(data.parallelStream()
                .filter(row -> row.length != 0)
                .map(row -> String.join(CSV_DELIMITER, row))
                .collect(Collectors.toList()), new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 直接重写文件
     *
     * @param data 多行记录
     * @param file 被写入文件
     */
    public static void rewriteToFileForArray(List<String[]> data, File file) {
        assert file != null;
        try {
            write(data.parallelStream()
                .filter(row -> row.length != 0)
                .map(row -> String.join(CSV_DELIMITER, row))
                .collect(Collectors.toList()), new BufferedWriter(new FileWriter(file)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读文件
     *
     * @param file 文件
     *
     * @return {@link List<String[]>}
     */
    public static List<String[]> readFile(File file) {
        if (!file.exists()) {
            return Collections.emptyList();
        }
        try {
            return read(new FileInputStream(file)).parallelStream().map(row -> row.split(CSV_DELIMITER)).collect(Collectors.toList());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 读文件
     *
     * @param file 文件
     *
     * @return {@link List<String[]>}
     */
    public static List<List<String>> readFileToList(File file) {
        List<String[]> readList = readFile(file);
        if(CollectionUtil.isEmpty(readList)){
            return Collections.emptyList();
        }

        return readList.stream().map(row -> Arrays.asList(row)).collect(Collectors.toList());
    }


    /**
     * 读
     *
     * @param inputStream 输入流
     *
     * @return {@link List<String>}
     */
    private static List<String> read(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        List<String> data = new ArrayList<>();
        String temp;
        try {
            while ((temp = reader.readLine()) != null) {
                data.add(temp);
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 写
     *
     * @param data   数据
     * @param writer 作家
     */
    private static void write(List<String> data, Writer writer) {
        if (CollectionUtil.isEmpty(data)) {
            return;
        }
        try {
            for (String row : data) {
                writer.write(row);
                writer.write(NEW_LINE);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        ByteOutputStream outputStream = new ByteOutputStream();
        List<String> t = new ArrayList<>();
        t.add("标题1");
        t.add("标题2");
        t.add("标题3");
        List<String> a = new ArrayList<>();
        a.add("哈哈");
        a.add("1");
        a.add("就是把咖啡");

        List<List<String>> list = new ArrayList<>();
        list.add(t);
        list.add(a);
        list.add(a);
        list.add(a);
        list.add(a);
        list.add(a);
        list.add(a);
        list.add(a);

        /**
         * 把数据写入输出流
         */
        appendToOutputForList(list, outputStream);

        ByteOutputStream zip = new ByteOutputStream();

        /**
         *  输出流压缩 加密
         */
        InputStreamFile inputStreamFile =
            new InputStreamFile(new ByteArrayInputStream(outputStream.getBytes()), "比如你说的标题.csv");
        ZipUtil.zip(zip, "123456", inputStreamFile);


        FileUtil.writeFromStream(
            new ByteArrayInputStream(zip.getBytes()),
            "/Users/hehui/dev/file/1.zip");


        System.out.println("ss");

    }
}
