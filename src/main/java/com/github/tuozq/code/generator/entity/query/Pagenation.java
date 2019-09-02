package com.github.tuozq.code.generator.entity.query;


/**
 * Copyright: Copyright (c) 2018 zq_tuo
 *
 * @ClassName: PageData.java
 * @Description: 该类的功能描述
 * @version: v1.0.0
 * @author: tuozq
 * @date: 2018年6月28日 下午5:16:10
 * Modification History:
 * Date         Author          Version            Description
 * ---------------------------------------------------------*
 * 2018年6月28日     tuozq           v1.0.0               修改原因
 */
public class Pagenation {

    private int page;

    private int pagesize;

    public int getPage() {
        if (this.page == 0) {
            this.page = getDefaultPage();
        }
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        if (this.pagesize == 0) {
            this.pagesize = getDefaultPageSize();
        }
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public int getDefaultPage(){
        return 1;
    }

    public int getDefaultPageSize(){
        return 15;
    }


}
