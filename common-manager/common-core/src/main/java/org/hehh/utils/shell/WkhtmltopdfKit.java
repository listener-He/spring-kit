package org.hehh.utils.shell;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author : HeHui
 * @date : 2019-04-13 22:44
 * @describe : html to pdf 插件执行类
 */
public class WkhtmltopdfKit {

    private static String WKHTMLTOPDF;

    private static String SHELL = " {} {}";

    public static String getWkhtmltopdfPath() {
        if (StrUtil.isNotBlank(WKHTMLTOPDF)) {
            return WKHTMLTOPDF;
        } else {

            return "wkhtmltopdf";
        }
    }

    /**
     * html文件转pdf
     *
     * @param htmlFile html文件地址
     * @param pdfFile  生产的pdf文件地址
     */
    public static void htmlTopdf(String htmlFile, String pdfFile) {
        if (StrUtil.isBlank(htmlFile)) {
            throw new NullPointerException("html模板不能为空。");
        }
        if (!FileUtil.exist(htmlFile)) {
            throw new RuntimeException(htmlFile + "  html模板不存在");
        }

        if (StrUtil.isBlank(pdfFile)) {
            pdfFile = FileUtil.getParent(htmlFile, 1) + FileUtil.extName(htmlFile) + ".pdf";
        }

        if (!FileUtil.isDirectory(FileUtil.getParent(htmlFile, 1))) {
            FileUtil.touch(pdfFile);
        }

        try {

            Process exec = RuntimeUtil.exec(getWkhtmltopdfPath() + StrUtil.format(SHELL, htmlFile, pdfFile));
            String result = RuntimeUtil.getResult(exec);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
