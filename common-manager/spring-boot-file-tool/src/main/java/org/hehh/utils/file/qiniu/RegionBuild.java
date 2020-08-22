package org.hehh.utils.file.qiniu;

import com.qiniu.storage.Region;

/**
 * @author: HeHui
 * @date: 2020-08-22 17:23
 * @description: 七牛云区域构建器
 */
public class RegionBuild {


    /**
     * 构建
     *
     * @param zone 区
     * @return {@link Region}
     */
    public static Region build(String zone){
        Region region;
        switch (zone) {
            case "z0":
                region = Region.region0();
                break;
            case "z1":
                region = Region.region1();
                break;
            case "z2":
                region = Region.region2();
                break;
            case "na0":
                region = Region.regionNa0();
                break;
            case "as0":
                region = Region.regionAs0();
                break;
            default:
                // Default is detecting zone automatically
                region = Region.autoRegion();
                break;
        }
        return region;
    }
}
