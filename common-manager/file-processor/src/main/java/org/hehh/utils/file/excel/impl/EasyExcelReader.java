package org.hehh.utils.file.excel.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.hehh.utils.file.excel.ExcelListenerReader;
import org.hehh.utils.file.excel.ExcelReader;
import org.hehh.utils.file.excel.ListenerCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author: HeHui
 * @date: 2020-08-22 14:38
 * @description: 阿里easy excel读取
 */
public class EasyExcelReader<T> implements ExcelListenerReader<T> {

    private final InputStream inputStream;


    /**
     * 简单的excel读者
     *
     * @param filePath 文件路径
     * @throws FileNotFoundException 文件未发现异常
     */
    public EasyExcelReader(String filePath) throws FileNotFoundException {
        this(new File(filePath));
    }

    /**
     * 简单的excel读者
     *
     * @param file 文件
     * @throws FileNotFoundException 文件未发现异常
     */
    public EasyExcelReader(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    /**
     * 简单的excel读者
     *
     * @param inputStream 输入流
     */
    public EasyExcelReader(InputStream inputStream) {
        assert inputStream != null : "文件不能为空";
        this.inputStream = inputStream;
    }


    /**
     * 调用读
     *
     * @param eventListener 事件监听器
     * @return {@link ExcelReader}
     */
    private com.alibaba.excel.ExcelReader invokeRead(AnalysisEventListener<T> eventListener) {
        if (null == eventListener) {
            return EasyExcel.read(inputStream).build();
        } else {
            return EasyExcel.read(inputStream, eventListener).build();
        }

    }


    /**
     * 读回调
     *
     * @param sheetIndex 表索引
     * @param startIndex 开始指数
     * @param callback   回调
     */
    @Override
    public void readCallback(int sheetIndex, int startIndex, ListenerCallback<T> callback) {
        com.alibaba.excel.ExcelReader excelReader = null;
        try {

            excelReader = invokeRead(null);

            ReadSheet readSheet = EasyExcel.readSheet(sheetIndex).registerReadListener(new AnalysisEventListenerCallback(callback, startIndex)).autoTrim(true).build();

            excelReader.read(readSheet);
        } finally {
            /**这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的*/
            if (excelReader != null) {
                excelReader.finish();
            }
        }
    }

    /**
     * 分析事件监听器回调
     *
     * @author hehui
     * @date 2020/08/22
     */
    static class AnalysisEventListenerCallback<T> extends AnalysisEventListener<T> {

        private final ListenerCallback<T> callback;
        private final int startIndex;

        private final AtomicInteger at = new AtomicInteger(0);

        AnalysisEventListenerCallback(ListenerCallback<T> callback, int startIndex) {
            this.callback = callback;
            this.startIndex = startIndex;
        }

        /**
         * When analysis one row trigger invoke function.
         *
         * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
         * @param context
         */
        @Override
        public void invoke(T data, AnalysisContext context) {
            if (data != null) {
                if (Map.class.isAssignableFrom(data.getClass())) {
                    Map tempMap = ((Map) data);
                    data = ((T) tempMap.values().stream().collect(Collectors.toList()));
                }

                at.incrementAndGet();

                if (callback != null && at.get() >= startIndex) {
                    callback.invoke(data, at.get());
                }

            }
        }

        /**
         * if have something to do after all analysis
         *
         * @param context
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            if (callback != null) {
                callback.doAfter();
            }
        }
    }
}
